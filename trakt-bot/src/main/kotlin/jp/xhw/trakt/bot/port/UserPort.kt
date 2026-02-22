package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.*

internal interface UserPort {
    suspend fun fetchUser(userId: UserId): User.Detail

    suspend fun fetchUsers(
        includeSuspended: Boolean = false,
        name: String? = null,
    ): List<User.Basic>

    suspend fun fetchUserIcon(userId: UserId): ByteArray

    suspend fun fetchDirectMessageChannel(userId: UserId): Channel.DirectMessage

    suspend fun fetchUserStats(userId: UserId): UserStats

    suspend fun fetchUserTags(userId: UserId): List<UserTag>

    suspend fun addUserTag(
        userId: UserId,
        tag: String,
    ): UserTag

    suspend fun removeUserTag(
        userId: UserId,
        tagId: UserTagId,
    )
}
