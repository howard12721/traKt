package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.context.bot.BotContext

typealias CommandExecutor = suspend BotContext.(CommandContext) -> Unit

internal sealed class CommandNode(
    val name: String,
) {
    private val mutableChildren: MutableList<CommandNode> = mutableListOf()
    private val mutableLiteralChildren: MutableMap<String, LiteralCommandNode> = linkedMapOf()
    private val mutableArgumentChildren: MutableList<ArgumentCommandNode<*>> = mutableListOf()

    val children: List<CommandNode>
        get() = mutableChildren

    val literalChildren: Map<String, LiteralCommandNode>
        get() = mutableLiteralChildren

    val argumentChildren: List<ArgumentCommandNode<*>>
        get() = mutableArgumentChildren

    var executor: CommandExecutor? = null

    fun literalChild(name: String): LiteralCommandNode? = mutableLiteralChildren[name]

    fun addLiteralChild(child: LiteralCommandNode): LiteralCommandNode {
        val existing = mutableLiteralChildren[child.name]
        if (existing != null) {
            return existing
        }

        mutableChildren += child
        mutableLiteralChildren[child.name] = child
        return child
    }

    fun addArgumentChild(child: ArgumentCommandNode<*>): ArgumentCommandNode<*> {
        mutableChildren += child
        mutableArgumentChildren += child
        return child
    }
}

internal class LiteralCommandNode(
    name: String,
) : CommandNode(name)

internal class ArgumentCommandNode<T>(
    name: String,
    val type: CommandArgumentType<T>,
) : CommandNode(name)
