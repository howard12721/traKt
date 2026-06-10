package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.model.*

/** コマンド実行時に executor へ渡される context。 */
class CommandContext internal constructor(
    val event: BotEvents.MessageCreated,
    val message: Message.Detail,
    val commandName: String,
    val rawInput: String,
    private val values: Map<String, Any?>,
) {
    operator fun <T> get(argument: CommandArgument<T>): T = value(argument)

    @Suppress("UNCHECKED_CAST")
    fun <T> value(argument: CommandArgument<T>): T = values.getValue(argument.name) as T
}
