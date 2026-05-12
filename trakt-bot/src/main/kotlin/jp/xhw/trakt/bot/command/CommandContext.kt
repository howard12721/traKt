package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.model.BotMessageCreated
import jp.xhw.trakt.bot.model.Message
import jp.xhw.trakt.bot.model.UserId

/** Context passed to command executors. */
class CommandContext internal constructor(
    val event: BotMessageCreated,
    val message: Message,
    val commandName: String,
    val rawInput: String,
    val args: CommandArguments,
)

/** Parsed command arguments keyed by their DSL names. */
class CommandArguments internal constructor(
    private val values: Map<String, Any?>,
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(name: String): T = values.getValue(name) as T

    fun string(name: String): String = get(name)

    fun int(name: String): Int = get(name)

    fun long(name: String): Long = get(name)

    fun boolean(name: String): Boolean = get(name)

    fun userId(name: String): UserId = get(name)

    operator fun contains(name: String): Boolean = name in values
}
