package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.context.base.reply
import jp.xhw.trakt.bot.context.base.sendMessage
import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.model.BotMessageCreated

internal class CommandRegistry internal constructor(
    private val options: CommandOptions,
) {
    private val roots = linkedMapOf<String, LiteralCommandNode>()

    internal fun root(name: String): LiteralCommandNode =
        roots.getOrPut(name) {
            LiteralCommandNode(name)
        }

    internal fun installHelpIfAbsent() {
        if ("help" in roots) {
            return
        }

        val help = root("help")
        help.description = options.helpDescription
        help.executor = { command ->
            command.message.channel.sendMessage(helpText())
        }
        help.addArgumentChild(
            ArgumentCommandNode("command", StringArgumentType).also { commandArgument ->
                commandArgument.executor = { command ->
                    command.message.channel.sendMessage(commandHelpText(command.args.string("command")))
                }
            },
        )
    }

    internal suspend fun handle(
        botContext: BotContext,
        event: BotMessageCreated,
    ) {
        val commandInput = CommandTriggerParser.parse(event.message.content, options) ?: return
        val rawCommandInput = commandInput.body
        val tokens =
            when (val result = CommandTokenizer.tokenize(rawCommandInput)) {
                is TokenizeResult.Success -> {
                    result.tokens
                }

                is TokenizeResult.Failure -> {
                    botContext.replyError(event, result.message)
                    return
                }
            }

        if (tokens.isEmpty()) {
            return
        }

        val root = roots[tokens.first().value] ?: return
        when (val match = match(root, rawCommandInput, tokens, tokenIndex = 1, arguments = emptyMap())) {
            is CommandMatch.Success -> {
                val commandContext =
                    CommandContext(
                        event = event,
                        message = event.message,
                        commandName = root.name,
                        rawInput = rawCommandInput,
                        args = CommandArguments(match.arguments),
                    )
                match.executor(botContext, commandContext)
            }

            is CommandMatch.Failure -> {
                botContext.replyError(event, match.message)
            }
        }
    }

    private fun match(
        node: CommandNode,
        input: String,
        tokens: List<CommandToken>,
        tokenIndex: Int,
        arguments: Map<String, Any?>,
    ): CommandMatch {
        if (tokenIndex >= tokens.size) {
            val executor = node.executor
            return if (executor != null) {
                CommandMatch.Success(executor, arguments)
            } else {
                CommandMatch.Failure("Missing arguments. Usage: ${usageFor(node)}")
            }
        }

        val token = tokens[tokenIndex]
        val literalMatch = node.literalChildren[token.value]
        if (literalMatch != null) {
            return match(literalMatch, input, tokens, tokenIndex + 1, arguments)
        }

        val argumentErrors = mutableListOf<String>()
        var childFailure: CommandMatch.Failure? = null
        for (child in node.argumentChildren) {
            if (child.type === GreedyStringArgumentType) {
                val value = input.substring(token.start).trim()
                val executor = child.executor
                val nextArguments = arguments + (child.name to value)
                if (executor != null) {
                    return CommandMatch.Success(executor, nextArguments)
                }
                return match(child, input, tokens, tokens.size, nextArguments)
            }

            val value = child.type.parse(token.value)
            if (value == null) {
                argumentErrors += "${child.name} must be ${child.type.displayName}"
                continue
            }

            val result = match(child, input, tokens, tokenIndex + 1, arguments + (child.name to value))
            if (result is CommandMatch.Success) {
                return result
            }
            childFailure = childFailure ?: result as CommandMatch.Failure
        }

        return childFailure
            ?: if (argumentErrors.isNotEmpty()) {
                CommandMatch.Failure(argumentErrors.first())
            } else {
                CommandMatch.Failure("Unexpected argument: ${token.value}")
            }
    }

    private suspend fun BotContext.replyError(
        event: BotMessageCreated,
        message: String,
    ) {
        if (options.replyOnError) {
            event.message.reply(message)
        }
    }

    private fun helpText(): String {
        val lines = mutableListOf("Available commands:")
        roots.values.forEach { root ->
            collectUsages(root).forEach { usage ->
                val description = root.description?.takeIf(String::isNotBlank)
                lines +=
                    if (description == null) {
                        "${options.prefix}$usage"
                    } else {
                        "${options.prefix}$usage - $description"
                    }
            }
        }
        return lines.joinToString("\n")
    }

    private fun commandHelpText(commandName: String): String {
        val root = roots[commandName] ?: return "Unknown command: $commandName"

        val lines = mutableListOf("Usage:")
        collectUsages(root).forEach { usage ->
            lines += "${options.prefix}$usage"
        }
        root.description?.takeIf(String::isNotBlank)?.let { description ->
            lines += ""
            lines += description
        }
        return lines.joinToString("\n")
    }

    private fun collectUsages(root: CommandNode): List<String> {
        val usages = mutableListOf<String>()

        fun visit(
            node: CommandNode,
            parts: List<String>,
        ) {
            if (node.executor != null) {
                usages += parts.joinToString(" ")
            }
            if (node.children.isEmpty() && node.executor == null) {
                usages += parts.joinToString(" ")
            }
            node.children.forEach { child ->
                val part =
                    when (child) {
                        is LiteralCommandNode -> child.name
                        is ArgumentCommandNode<*> -> "<${child.name}>"
                    }
                visit(child, parts + part)
            }
        }

        visit(root, listOf(root.name))
        return usages.distinct()
    }

    private fun usageFor(node: CommandNode): String {
        val root = roots.values.firstOrNull { candidate -> containsNode(candidate, node) } ?: return node.name
        return collectUsages(root).firstOrNull() ?: root.name
    }

    private fun containsNode(
        current: CommandNode,
        target: CommandNode,
    ): Boolean = current === target || current.children.any { containsNode(it, target) }
}

internal data class CommandOptions(
    val prefix: String,
    val botUserIdProvider: () -> String? = { null },
    val replyOnError: Boolean = true,
    val helpDescription: String = "ヘルプを表示します",
)

private sealed interface CommandMatch {
    data class Success(
        val executor: CommandExecutor,
        val arguments: Map<String, Any?>,
    ) : CommandMatch

    data class Failure(
        val message: String,
    ) : CommandMatch
}
