package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

@JvmInline
value class UserId(
    val value: Uuid,
)

@JvmInline
value class UserHandle(
    val id: UserId,
)

@JvmInline
value class UserTagId(
    val value: Uuid,
)

enum class UserState(
    val value: Int,
) {
    DEACTIVATED(0),
    ACTIVE(1),
    SUSPENDED(2),
}

@JvmInline
value class BotId(
    val value: Uuid,
)

data class Bot(
    val botId: BotId,
    val userId: UserId,
) {
    val user: UserHandle
        get() = UserHandle(userId)
}

sealed interface User {
    val id: UserId
    val name: String
    val displayName: String
    val iconFileId: FileId
    val isBot: Boolean

    val handle: UserHandle
        get() = UserHandle(id)
    val iconFile: FileHandle
        get() = FileHandle(iconFileId)

    sealed interface StatefulUser : User {
        val state: UserState
        val updatedAt: Instant
    }

    data class Minimal(
        override val id: UserId,
        override val name: String,
        override val displayName: String,
        override val iconFileId: FileId,
        override val isBot: Boolean,
    ) : User

    data class Basic(
        override val id: UserId,
        override val name: String,
        override val displayName: String,
        override val iconFileId: FileId,
        override val isBot: Boolean,
        override val state: UserState,
        override val updatedAt: Instant,
    ) : StatefulUser

    data class Detail(
        override val id: UserId,
        override val name: String,
        override val displayName: String,
        override val iconFileId: FileId,
        override val isBot: Boolean,
        override val state: UserState,
        override val updatedAt: Instant,
        val twitterId: String,
        val lastOnline: Instant?,
        val tags: List<UserTag>,
        val groupIds: List<GroupId>,
        val bio: String,
        val homeChannelId: ChannelId?,
    ) : StatefulUser {
        val groups: List<GroupHandle>
            get() = groupIds.map { GroupHandle(it) }
        val homeChannelHandle: ChannelHandle?
            get() = homeChannelId?.let { ChannelHandle(it) }
    }
}

data class UserTag(
    val tagId: UserTagId,
    val tag: String,
    val isLocked: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
)

/**
 * ユーザーのスタンプの使用統計情報
 *
 * @param stampId スタンプID
 * @param count 同一メッセージ上のものは複数カウントしないスタンプ数
 * @param total 同一メッセージ上のものも複数カウントするスタンプ数
 */
data class UserStampStats(
    val stampId: StampId,
    val count: Long,
    val total: Long,
)

data class UserStats(
    val totalMessageCount: Long,
    val stamps: List<UserStampStats>,
    val datetime: Instant,
)
