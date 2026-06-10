package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.context.base.reply
import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.model.BotEvents

internal class CommandRegistry internal constructor(
    private val options: CommandOptions,
) {
    private val roots = linkedMapOf<String, LiteralCommandNode>()
    private val userNameCache = UserNameCache()

    internal fun root(name: String): LiteralCommandNode {
        roots[name]?.let { return it }
        return LiteralCommandNode(name).also { roots[name] = it }
    }

    internal suspend fun handle(
        botContext: BotContext,
        event: BotEvents.MessageCreated,
    ) {
        val commandInput = CommandTriggerParser.parse(event.message.content, options) ?: return
        val rawCommandInput = commandInput.body
        val tokens =
            when (val result = CommandTokenizer.tokenize(rawCommandInput, options.errorMessages)) {
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
        val executorReachableNodes = executorReachableNodes(root)
        if (root !in executorReachableNodes) {
            return
        }
        val resolver = CommandArgumentResolver(botContext, userNameCache)
        when (
            val match =
                match(
                    root = root,
                    node = root,
                    input = rawCommandInput,
                    tokens = tokens,
                    tokenIndex = 1,
                    arguments = emptyMap(),
                    resolver = resolver,
                    executorReachableNodes = executorReachableNodes,
                )
        ) {
            is CommandMatch.Success -> {
                val commandContext =
                    CommandContext(
                        event = event,
                        message = event.message,
                        commandName = root.name,
                        rawInput = rawCommandInput,
                        values = match.arguments,
                    )
                match.executor.invoke(botContext, commandContext)
            }

            is CommandMatch.Failure -> {
                botContext.replyError(event, match.message)
            }
        }
    }

    internal suspend fun preloadArgumentCaches(botContext: BotContext) {
        userNameCache.preload(botContext)
    }

    private suspend fun match(
        root: CommandNode,
        node: CommandNode,
        input: String,
        tokens: List<CommandToken>,
        tokenIndex: Int,
        arguments: Map<String, Any?>,
        resolver: CommandArgumentResolver,
        executorReachableNodes: Set<CommandNode>,
    ): CommandMatch {
        if (tokenIndex >= tokens.size) {
            val executor = node.executor
            return if (executor != null) {
                CommandMatch.Success(executor, arguments)
            } else {
                CommandMatch.Failure(
                    options.errorMessages.missingArguments(usageFor(root, node, executorReachableNodes)),
                )
            }
        }

        val token = tokens[tokenIndex]
        val literalMatch = node.literalChildren[token.value]
        if (literalMatch != null && literalMatch in executorReachableNodes) {
            return match(root, literalMatch, input, tokens, tokenIndex + 1, arguments, resolver, executorReachableNodes)
        }

        val argumentErrors = mutableListOf<String>()
        var childFailure: CommandMatch.Failure? = null
        for (child in node.argumentChildren) {
            if (child !in executorReachableNodes) {
                continue
            }
            if (child.type === GreedyStringArgumentType) {
                val value = input.substring(token.start).trim()
                val executor = child.executor
                val nextArguments = arguments + (child.name to value)
                if (executor != null) {
                    return CommandMatch.Success(executor, nextArguments)
                }
                return match(root, child, input, tokens, tokens.size, nextArguments, resolver, executorReachableNodes)
            }

            val value = child.type.parse(token.value, resolver)
            if (value == null) {
                argumentErrors += options.errorMessages.invalidArgument(child.name, child.type.displayName)
                continue
            }

            val result =
                match(
                    root = root,
                    node = child,
                    input = input,
                    tokens = tokens,
                    tokenIndex = tokenIndex + 1,
                    arguments = arguments + (child.name to value),
                    resolver = resolver,
                    executorReachableNodes = executorReachableNodes,
                )
            if (result is CommandMatch.Success) {
                return result
            }
            childFailure = childFailure ?: result as CommandMatch.Failure
        }

        return childFailure
            ?: if (argumentErrors.isNotEmpty()) {
                CommandMatch.Failure(argumentErrors.first())
            } else {
                CommandMatch.Failure(options.errorMessages.unexpectedArgument(token.value))
            }
    }

    private suspend fun BotContext.replyError(
        event: BotEvents.MessageCreated,
        message: String,
    ) {
        if (options.replyOnError) {
            event.message.reply(message)
        }
    }

    private fun collectUsagesFrom(
        node: CommandNode,
        parts: List<String>,
        executorReachableNodes: Set<CommandNode>,
    ): List<String> {
        val usages = mutableListOf<String>()

        fun visit(
            node: CommandNode,
            parts: List<String>,
        ) {
            if (node.executor != null) {
                usages += parts.joinToString(" ")
            }
            node.children.forEach { child ->
                if (child !in executorReachableNodes) {
                    return@forEach
                }
                val part =
                    when (child) {
                        is LiteralCommandNode -> child.name
                        is ArgumentCommandNode<*> -> "<${child.name}>"
                    }
                visit(child, parts + part)
            }
        }

        visit(node, parts)
        return usages.distinct()
    }

    private fun usageFor(
        root: CommandNode,
        node: CommandNode,
        executorReachableNodes: Set<CommandNode>,
    ): String {
        val parts = pathParts(root, node) ?: return root.name
        return collectUsagesFrom(node, parts, executorReachableNodes)
            .minWithOrNull(compareBy({ it.split(" ").size }, { it.length }))
            ?: root.name
    }

    private fun executorReachableNodes(root: CommandNode): Set<CommandNode> =
        buildSet {
            fun visit(node: CommandNode): Boolean {
                var reachable = node.executor != null
                node.children.forEach { child ->
                    if (visit(child)) {
                        reachable = true
                    }
                }
                if (reachable) {
                    add(node)
                }
                return reachable
            }

            visit(root)
        }

    private fun pathParts(
        root: CommandNode,
        target: CommandNode,
    ): List<String>? {
        val parts = mutableListOf<String>()
        var current: CommandNode? = target
        while (current != null) {
            parts += current.usagePart()
            if (current === root) {
                return parts.asReversed()
            }
            current = current.parent
        }
        return null
    }

    private fun CommandNode.usagePart(): String =
        when (this) {
            is LiteralCommandNode -> name
            is ArgumentCommandNode<*> -> "<$name>"
        }
}

internal data class CommandOptions(
    val prefix: String,
    val botUserIdProvider: () -> String? = { null },
    val replyOnError: Boolean = true,
    val errorMessages: CommandErrorMessages = DefaultCommandErrorMessages,
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
