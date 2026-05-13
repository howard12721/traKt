package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.model.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.net.URI
import kotlin.uuid.Uuid

/** コマンド引数の検証と変換に使う型。 */
sealed interface CommandArgumentType<T> {
    val displayName: String

    suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): T?
}

object StringArgumentType : CommandArgumentType<String> {
    override val displayName: String = "string"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): String = token
}

object GreedyStringArgumentType : CommandArgumentType<String> {
    override val displayName: String = "string"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): String = token
}

object IntArgumentType : CommandArgumentType<Int> {
    override val displayName: String = "int"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): Int? = token.toIntOrNull()
}

object LongArgumentType : CommandArgumentType<Long> {
    override val displayName: String = "long"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): Long? = token.toLongOrNull()
}

object BooleanArgumentType : CommandArgumentType<Boolean> {
    override val displayName: String = "boolean"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): Boolean? =
        when (token.lowercase()) {
            "true", "yes", "on", "1" -> true
            "false", "no", "off", "0" -> false
            else -> null
        }
}

object UserArgumentType : CommandArgumentType<User.Ref> {
    override val displayName: String = "user"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): User.Ref? = resolver.resolveUser(token)
}

object ChannelArgumentType : CommandArgumentType<Channel.Ref> {
    override val displayName: String = "channel"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): Channel.Ref? = resolver.resolveChannel(token)
}

object GroupArgumentType : CommandArgumentType<Group.Ref> {
    override val displayName: String = "group"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): Group.Ref? = resolver.resolveGroup(token)
}

object MessageArgumentType : CommandArgumentType<Message.Ref> {
    override val displayName: String = "message"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): Message.Ref? = resolver.resolveMessage(token)
}

class CommandArgumentResolver internal constructor(
    private val ctx: BotContext,
    private val userNameCache: UserNameCache,
) {
    suspend fun resolveUser(token: String): User.Ref? {
        parseUuid(token)?.let { return User.Ref(UserId(it)) }
        CommandMentionParser.parseId(token, type = "user")?.let { return User.Ref(UserId(it)) }
        return userNameCache.resolve(ctx, token)?.let { User.Ref(it) }
    }

    suspend fun resolveChannel(token: String): Channel.Ref? {
        parseUuid(token)?.let { return Channel.Ref(ChannelId(it)) }
        CommandMentionParser.parseId(token, type = "channel")?.let { return Channel.Ref(ChannelId(it)) }

        val path = runCatching { ChannelPath(token.removePrefix("#")) }.getOrNull() ?: return null
        val channel =
            ctx.channelPort
                .fetchChannels(path = path.value)
                .publicChannels
                .firstOrNull() ?: return null
        return Channel.Ref(channel.id)
    }

    fun resolveGroup(token: String): Group.Ref? {
        parseUuid(token)?.let { return Group.Ref(GroupId(it)) }
        CommandMentionParser.parseId(token, type = "group")?.let { return Group.Ref(GroupId(it)) }
        return null
    }

    fun resolveMessage(token: String): Message.Ref? {
        parseUuid(token)?.let { return Message.Ref(MessageId(it)) }
        parseMessageUrl(token)?.let { return Message.Ref(MessageId(it)) }
        return null
    }
}

internal class UserNameCache {
    private val mutex = Mutex()
    private val names = mutableMapOf<String, UserId>()
    private val misses = mutableSetOf<String>()
    private var initialized = false

    suspend fun preload(ctx: BotContext) {
        mutex.withLock {
            if (initialized) {
                return
            }

            ctx.userPort.fetchUsers(includeSuspended = true).forEach { user ->
                names[user.name] = user.id
            }
            initialized = true
        }
    }

    suspend fun resolve(
        ctx: BotContext,
        name: String,
    ): UserId? =
        mutex.withLock {
            resolveLocked(ctx, name)
        }

    private suspend fun resolveLocked(
        ctx: BotContext,
        name: String,
    ): UserId? {
        names[name]?.let { return it }
        if (name in misses) {
            return null
        }

        val user = ctx.userPort.fetchUsers(includeSuspended = true, name = name).firstOrNull()
        if (user == null) {
            misses += name
            return null
        }

        names[user.name] = user.id
        return user.id
    }
}

private val messageUrlRegex = Regex("""^https?://[^/]+/messages/([0-9a-fA-F-]{36})(?:[?#].*)?$""")

private fun parseUuid(token: String): Uuid? =
    runCatching {
        Uuid.parse(token)
    }.getOrNull()

private fun parseMessageUrl(token: String): Uuid? {
    val match = messageUrlRegex.matchEntire(token) ?: return null
    val uri = runCatching { URI(token) }.getOrNull() ?: return null
    if (uri.path?.startsWith("/messages/") != true) {
        return null
    }
    return parseUuid(match.groupValues[1])
}
