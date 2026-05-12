package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.model.UserId
import kotlin.uuid.Uuid

/** コマンド引数の検証と変換に使う型。 */
sealed interface CommandArgumentType<T> {
    val displayName: String

    fun parse(token: String): T?
}

object StringArgumentType : CommandArgumentType<String> {
    override val displayName: String = "string"

    override fun parse(token: String): String = token
}

object GreedyStringArgumentType : CommandArgumentType<String> {
    override val displayName: String = "string"

    override fun parse(token: String): String = token
}

object IntArgumentType : CommandArgumentType<Int> {
    override val displayName: String = "int"

    override fun parse(token: String): Int? = token.toIntOrNull()
}

object LongArgumentType : CommandArgumentType<Long> {
    override val displayName: String = "long"

    override fun parse(token: String): Long? = token.toLongOrNull()
}

object BooleanArgumentType : CommandArgumentType<Boolean> {
    override val displayName: String = "boolean"

    override fun parse(token: String): Boolean? =
        when (token.lowercase()) {
            "true", "yes", "on", "1" -> true
            "false", "no", "off", "0" -> false
            else -> null
        }
}

object UserIdArgumentType : CommandArgumentType<UserId> {
    override val displayName: String = "userId"

    override fun parse(token: String): UserId? =
        runCatching {
            UserId(Uuid.parse(token))
        }.getOrNull()
}
