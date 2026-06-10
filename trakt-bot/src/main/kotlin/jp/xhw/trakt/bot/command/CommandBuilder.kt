package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.model.Channel
import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.Message
import jp.xhw.trakt.bot.model.User

@TraktDsl
class CommandRegistryBuilder internal constructor(
    internal val registry: CommandRegistry,
) {
    fun command(
        name: String,
        block: CommandNodeBuilder.() -> Unit,
    ) {
        require(name.isNotBlank()) { "Command name must not be blank" }
        val node = registry.root(name)
        CommandNodeBuilder(node).block()
    }
}

@TraktDsl
class CommandNodeBuilder internal constructor(
    private val node: CommandNode,
) {
    fun literal(
        name: String,
        block: CommandNodeBuilder.() -> Unit,
    ) {
        require(name.isNotBlank()) { "Literal name must not be blank" }
        val child = node.literal(name)
        CommandNodeBuilder(child).block()
    }

    fun <T> argument(
        name: String,
        type: CommandArgumentType<T>,
        block: CommandNodeBuilder.(CommandArgument<T>) -> Unit,
    ) {
        require(name.isNotBlank()) { "Argument name must not be blank" }
        val child = ArgumentCommandNode(name, type)
        node.addArgumentChild(child)
        CommandNodeBuilder(child).block(CommandArgument(name))
    }

    fun string(
        name: String,
        block: CommandNodeBuilder.(CommandArgument<String>) -> Unit,
    ) = argument(name, StringArgumentType, block)

    fun greedyString(
        name: String,
        block: CommandNodeBuilder.(CommandArgument<String>) -> Unit,
    ) = argument(name, GreedyStringArgumentType, block)

    fun int(
        name: String,
        block: CommandNodeBuilder.(CommandArgument<Int>) -> Unit,
    ) = argument(name, IntArgumentType, block)

    fun long(
        name: String,
        block: CommandNodeBuilder.(CommandArgument<Long>) -> Unit,
    ) = argument(name, LongArgumentType, block)

    fun boolean(
        name: String,
        block: CommandNodeBuilder.(CommandArgument<Boolean>) -> Unit,
    ) = argument(name, BooleanArgumentType, block)

    fun user(
        name: String,
        block: CommandNodeBuilder.(CommandArgument<User.Detail>) -> Unit,
    ) = argument(name, UserArgumentType, block)

    fun channel(
        name: String,
        block: CommandNodeBuilder.(CommandArgument<Channel.Detail>) -> Unit,
    ) = argument(name, ChannelArgumentType, block)

    fun group(
        name: String,
        block: CommandNodeBuilder.(CommandArgument<Group.Detail>) -> Unit,
    ) = argument(name, GroupArgumentType, block)

    fun message(
        name: String,
        block: CommandNodeBuilder.(CommandArgument<Message.Detail>) -> Unit,
    ) = argument(name, MessageArgumentType, block)

    fun executes(executor: CommandExecutor) {
        node.executor = executor
    }

    private fun CommandNode.literal(name: String): LiteralCommandNode {
        literalChild(name)?.let { return it }
        return addLiteralChild(LiteralCommandNode(name))
    }
}

class CommandArgument<T> internal constructor(
    internal val name: String,
)
