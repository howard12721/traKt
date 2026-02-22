package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant
import kotlin.uuid.Uuid

// --- Fetch ---

context(scope: BotScope)
suspend fun fetchUser(userId: UserId): User.Detail = scope.context.userPort.fetchUser(userId)

context(scope: BotScope)
suspend fun fetchUser(userId: Uuid): User.Detail = fetchUser(UserId(userId))

context(scope: BotScope)
suspend fun UserHandle.resolve(): User = scope.context.userPort.fetchUser(id)

context(scope: BotScope)
suspend fun User.Minimal.resolve(): User = handle.resolve()

context(scope: BotScope)
suspend fun User.Basic.resolve(): User = handle.resolve()

context(scope: BotScope)
suspend fun User.Detail.refresh(): User = handle.resolve()

context(scope: BotScope)
suspend fun fetchUsers(
    includeSuspended: Boolean = false,
    name: String? = null,
): List<User.Basic> = scope.context.userPort.fetchUsers(includeSuspended, name)

context(scope: BotScope)
suspend fun UserHandle.downloadIcon(): ByteArray = scope.context.userPort.fetchUserIcon(id)

context(scope: BotScope)
suspend fun UserHandle.fetchDirectMessageChannel(): Channel.DirectMessage = scope.context.userPort.fetchDirectMessageChannel(id)

context(scope: BotScope)
suspend fun UserHandle.fetchDirectMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> =
    scope.context.messagePort.fetchDirectMessages(
        id,
        limit,
        offset,
        since,
        until,
        inclusive,
        order,
    )

context(scope: BotScope)
suspend fun UserHandle.sendDirectMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = scope.context.messagePort.sendDirectMessage(id, content, embed, nonce)

context(scope: BotScope)
suspend fun User.sendDirectMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = handle.sendDirectMessage(content, embed, nonce)

// --- Tags ---

context(scope: BotScope)
suspend fun UserHandle.fetchTags(): List<UserTag> = scope.context.userPort.fetchUserTags(id)

context(scope: BotScope)
suspend fun UserHandle.tag(tag: String): UserTag = scope.context.userPort.addUserTag(id, tag)

context(scope: BotScope)
suspend fun UserHandle.untag(tagId: UserTagId) = scope.context.userPort.removeUserTag(id, tagId)

// --- Stats ---

context(scope: BotScope)
suspend fun UserHandle.fetchStats(): UserStats = scope.context.userPort.fetchUserStats(id)

// --- User convenience extensions ---

context(scope: BotScope)
suspend fun User.downloadIcon(): ByteArray = handle.downloadIcon()

context(scope: BotScope)
suspend fun User.fetchDirectMessageChannel(): Channel.DirectMessage = handle.fetchDirectMessageChannel()

context(scope: BotScope)
suspend fun User.fetchDirectMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> = handle.fetchDirectMessages(limit, offset, since, until, inclusive, order)

context(scope: BotScope)
suspend fun User.fetchTags(): List<UserTag> = handle.fetchTags()

context(scope: BotScope)
suspend fun User.tag(tag: String): UserTag = handle.tag(tag)

context(scope: BotScope)
suspend fun User.untag(tagId: UserTagId) = handle.untag(tagId)

context(scope: BotScope)
suspend fun User.fetchStats(): UserStats = handle.fetchStats()
