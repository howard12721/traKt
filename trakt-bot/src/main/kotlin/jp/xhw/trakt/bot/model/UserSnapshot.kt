package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

enum class UserState {
    DEACTIVATED,
    ACTIVE,
    SUSPENDED,
}

data class UserSnapshot(
    val user: User,
    val name: String,
    val displayName: String,
    val iconFileId: FileId,
    val isBot: Boolean,
    val state: UserState? = null,
    val twitterId: String? = null,
    val lastOnline: Instant? = null,
    val updatedAt: Instant? = null,
    val tags: List<UserTagSnapshot> = emptyList(),
    val groups: List<Uuid> = emptyList(),
    val bio: String? = null,
    val homeChannel: Channel? = null,
) {
    val id: UserId
        get() = user.id
}

data class UserTagSnapshot(
    val tagId: UserTagId,
    val tag: String,
    val isLocked: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
)

data class UserStampStatsSnapshot(
    val stampId: StampId,
    val count: Long,
    val total: Long,
)

data class UserStatsSnapshot(
    val totalMessageCount: Long,
    val stamps: List<UserStampStatsSnapshot>,
    val datetime: Instant,
)

data class CreateUserRequest(
    val name: String,
    val password: String? = null,
)

data class EditUserRequest(
    val displayName: String? = null,
    val twitterId: String? = null,
    val state: UserState? = null,
    val role: String? = null,
)

data class UserIconUpload(
    val fileName: String,
    val bytes: ByteArray,
    val contentType: String? = null,
)
