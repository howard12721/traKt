package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.model.*

/** コマンド実行時に executor へ渡される context。 */
class CommandContext internal constructor(
    val event: BotMessageCreated,
    val message: Message.Detail,
    val commandName: String,
    val rawInput: String,
    val args: CommandArguments,
)

/** DSL で定義した名前から参照できるパース済みコマンド引数。 */
class CommandArguments internal constructor(
    private val values: Map<String, Any?>,
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(name: String): T = values.getValue(name) as T

    fun string(name: String): String = get(name)

    fun int(name: String): Int = get(name)

    fun long(name: String): Long = get(name)

    fun boolean(name: String): Boolean = get(name)

    fun user(name: String): User.Ref = get(name)

    fun channel(name: String): Channel.Ref = get(name)

    fun group(name: String): Group.Ref = get(name)

    fun message(name: String): Message.Ref = get(name)

    operator fun contains(name: String): Boolean = name in values
}
