package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.model.UserId

@TraktDsl
class CommandRegistryBuilder internal constructor(
    internal val registry: CommandRegistry,
) {
    fun command(
        name: String,
        description: String? = null,
        block: CommandNodeBuilder.() -> Unit,
    ) {
        require(name.isNotBlank()) { "Command name must not be blank" }
        val node = registry.root(name)
        if (description != null) {
            node.description = description
        }
        CommandNodeBuilder(node).block()
    }
}

@TraktDsl
class CommandNodeBuilder internal constructor(
    private val node: CommandNode,
) {
    fun literal(
        name: String,
        description: String? = null,
        block: CommandNodeBuilder.() -> Unit,
    ) {
        require(name.isNotBlank()) { "Literal name must not be blank" }
        val child = node.literal(name)
        if (description != null) {
            child.description = description
        }
        CommandNodeBuilder(child).block()
    }

    fun <T> argument(
        name: String,
        type: CommandArgumentType<T>,
        description: String? = null,
        block: CommandNodeBuilder.() -> Unit,
    ) {
        require(name.isNotBlank()) { "Argument name must not be blank" }
        val child = ArgumentCommandNode(name, type, description)
        node.addArgumentChild(child)
        CommandNodeBuilder(child).block()
    }

    fun string(
        name: String,
        description: String? = null,
        block: CommandNodeBuilder.() -> Unit,
    ) = argument(name, StringArgumentType, description, block)

    fun greedyString(
        name: String,
        description: String? = null,
        block: CommandNodeBuilder.() -> Unit,
    ) = argument(name, GreedyStringArgumentType, description, block)

    fun int(
        name: String,
        description: String? = null,
        block: CommandNodeBuilder.() -> Unit,
    ) = argument(name, IntArgumentType, description, block)

    fun long(
        name: String,
        description: String? = null,
        block: CommandNodeBuilder.() -> Unit,
    ) = argument(name, LongArgumentType, description, block)

    fun boolean(
        name: String,
        description: String? = null,
        block: CommandNodeBuilder.() -> Unit,
    ) = argument(name, BooleanArgumentType, description, block)

    fun userId(
        name: String,
        description: String? = null,
        block: CommandNodeBuilder.() -> Unit,
    ) = argument<UserId>(name, UserIdArgumentType, description, block)

    fun executes(executor: CommandExecutor) {
        node.executor = executor
    }

    private fun CommandNode.literal(name: String): LiteralCommandNode {
        literalChild(name)?.let { return it }
        return addLiteralChild(LiteralCommandNode(name))
    }
}
