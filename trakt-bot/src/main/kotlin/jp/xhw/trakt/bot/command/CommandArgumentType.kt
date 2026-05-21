package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.model.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

object UserArgumentType : CommandArgumentType<User.Detail> {
    override val displayName: String = "user"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): User.Detail? = resolver.resolveUser(token)
}

object ChannelArgumentType : CommandArgumentType<Channel.Detail> {
    override val displayName: String = "channel"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): Channel.Detail? = resolver.resolveChannel(token)
}

object GroupArgumentType : CommandArgumentType<Group.Detail> {
    override val displayName: String = "group"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): Group.Detail? = resolver.resolveGroup(token)
}

object MessageArgumentType : CommandArgumentType<Message.Detail> {
    override val displayName: String = "message"

    override suspend fun parse(
        token: String,
        resolver: CommandArgumentResolver,
    ): Message.Detail? = resolver.resolveMessage(token)
}

class CommandArgumentResolver internal constructor(
    private val ctx: BotContext,
    private val userNameCache: UserNameCache,
) {
    suspend fun resolveUser(token: String): User.Detail? {
        parseUuid(token)?.let { return resolveUserId(UserId(it)) }
        CommandMentionParser.parseId(token, type = "user")?.let { return resolveUserId(UserId(it)) }
        return userNameCache.resolve(ctx, token)?.let { resolveUserId(it) }
    }

    suspend fun resolveChannel(token: String): Channel.Detail? {
        parseUuid(token)?.let { return resolveChannelId(ChannelId(it)) }
        CommandMentionParser.parseId(token, type = "channel")?.let { return resolveChannelId(ChannelId(it)) }

        val path = runCatching { ChannelPath(token.removePrefix("#")) }.getOrNull() ?: return null
        return ctx.channelPort
            .fetchChannels(path = path.value)
            .publicChannels
            .firstOrNull()
    }

    suspend fun resolveGroup(token: String): Group.Detail? {
        parseUuid(token)?.let { return resolveGroupId(GroupId(it)) }
        CommandMentionParser.parseId(token, type = "group")?.let { return resolveGroupId(GroupId(it)) }
        return null
    }

    suspend fun resolveMessage(token: String): Message.Detail? {
        parseUuid(token)?.let { return resolveMessageId(MessageId(it)) }
        parseMessageUrl(token)?.let { return resolveMessageId(MessageId(it)) }
        return null
    }

    private suspend fun resolveUserId(userId: UserId): User.Detail? = ctx.userPort.fetchUserOrNull(userId)

    private suspend fun resolveChannelId(channelId: ChannelId): Channel.Detail? = ctx.channelPort.fetchChannelOrNull(channelId)

    private suspend fun resolveGroupId(groupId: GroupId): Group.Detail? = ctx.groupPort.fetchGroupOrNull(groupId)

    private suspend fun resolveMessageId(messageId: MessageId): Message.Detail? = ctx.messagePort.fetchMessageOrNull(messageId)
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

        val user = ctx.userPort.fetchUsers(name = name).firstOrNull()
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
    return parseUuid(match.groupValues[1])
}
