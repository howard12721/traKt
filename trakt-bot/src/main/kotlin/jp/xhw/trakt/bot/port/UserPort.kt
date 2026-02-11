package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.CreateUserRequest
import jp.xhw.trakt.bot.model.DirectMessageChannelSnapshot
import jp.xhw.trakt.bot.model.EditUserRequest
import jp.xhw.trakt.bot.model.MessageHistoryQuery
import jp.xhw.trakt.bot.model.MessageSnapshot
import jp.xhw.trakt.bot.model.User
import jp.xhw.trakt.bot.model.UserIconUpload
import jp.xhw.trakt.bot.model.UserId
import jp.xhw.trakt.bot.model.UserSnapshot
import jp.xhw.trakt.bot.model.UserStatsSnapshot
import jp.xhw.trakt.bot.model.UserTagId
import jp.xhw.trakt.bot.model.UserTagSnapshot

internal interface UserPort {
    suspend fun getUser(userId: UserId): User?

    suspend fun getSnapshot(userId: UserId): UserSnapshot?

    suspend fun listUsers(
        includeSuspended: Boolean = false,
        name: String? = null,
    ): List<UserSnapshot>

    suspend fun createUser(request: CreateUserRequest): UserSnapshot

    suspend fun editUser(
        userId: UserId,
        request: EditUserRequest,
    )

    suspend fun changePassword(
        userId: UserId,
        newPassword: String,
    )

    suspend fun changeIcon(
        userId: UserId,
        icon: UserIconUpload,
    )

    suspend fun getIcon(userId: UserId): ByteArray?

    suspend fun getDirectMessageChannel(userId: UserId): DirectMessageChannelSnapshot?

    suspend fun getDirectMessages(
        userId: UserId,
        query: MessageHistoryQuery = MessageHistoryQuery(),
    ): List<MessageSnapshot>

    suspend fun postDirectMessage(
        userId: UserId,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot

    suspend fun getTags(userId: UserId): List<UserTagSnapshot>

    suspend fun addTag(
        userId: UserId,
        tag: String,
    ): UserTagSnapshot

    suspend fun editTag(
        userId: UserId,
        tagId: UserTagId,
        isLocked: Boolean,
    )

    suspend fun removeTag(
        userId: UserId,
        tagId: UserTagId,
    )

    suspend fun getStats(userId: UserId): UserStatsSnapshot?
}
