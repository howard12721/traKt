package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** ユーザーID。 */
@JvmInline
value class UserId(
    val value: Uuid,
)

/** ユーザーを参照するためのハンドル。 */
@JvmInline
value class UserHandle internal constructor(
    val id: UserId,
)

/** ユーザータグID。 */
@JvmInline
value class UserTagId(
    val value: Uuid,
)

/** ユーザーアカウント状態。 */
enum class UserState(
    val value: Int,
) {
    DEACTIVATED(0),
    ACTIVE(1),
    SUSPENDED(2),
}

/** BotID。 */
@JvmInline
value class BotId(
    val value: Uuid,
)

/** Bot と対応ユーザーの紐付け情報。 */
@ConsistentCopyVisibility
data class Bot internal constructor(
    val botId: BotId,
    val userId: UserId,
) {
    /** Bot に対応するユーザーハンドル。 */
    val user: UserHandle
        get() = UserHandle(userId)
}

/** ユーザー。 */
sealed interface User {
    val id: UserId
    val name: String
    val displayName: String
    val iconFileId: FileId
    val isBot: Boolean

    /** このユーザーを指すハンドル。 */
    val handle: UserHandle
        get() = UserHandle(id)

    /** アイコンファイルのハンドル。 */
    val iconFile: FileHandle
        get() = FileHandle(iconFileId)

    /** 状態を持つユーザー情報。 */
    sealed interface StatefulUser : User {
        val state: UserState
        val updatedAt: Instant
    }

    /** イベントで使われるユーザー情報。 */
    class Minimal internal constructor(
        override val id: UserId,
        override val name: String,
        override val displayName: String,
        override val iconFileId: FileId,
        override val isBot: Boolean,
    ) : User {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is User) return false
            return this.id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }

    /** 一覧取得等で使う基本的なユーザー情報。 */
    class Basic internal constructor(
        override val id: UserId,
        override val name: String,
        override val displayName: String,
        override val iconFileId: FileId,
        override val isBot: Boolean,
        override val state: UserState,
        override val updatedAt: Instant,
    ) : StatefulUser {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is User) return false
            return this.id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }

    /** API から取得するユーザー詳細情報。 */
    class Detail internal constructor(
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
        /** 所属グループのハンドル一覧。 */
        val groups: List<GroupHandle>
            get() = groupIds.map { GroupHandle(it) }

        /** ホームチャンネルのハンドル。 */
        val homeChannelHandle: ChannelHandle?
            get() = homeChannelId?.let { ChannelHandle(it) }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is User) return false
            return this.id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }
}

/** ユーザータグ情報。 */
@ConsistentCopyVisibility
data class UserTag internal constructor(
    val tagId: UserTagId,
    val tag: String,
    val isLocked: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
)

/**
 * ユーザーのスタンプ使用統計。
 *
 * @param stampId スタンプID
 * @param count 同一メッセージ上のものは複数カウントしないスタンプ数
 * @param total 同一メッセージ上のものも複数カウントするスタンプ数
 */
@ConsistentCopyVisibility
data class UserStampStats internal constructor(
    val stampId: StampId,
    val count: Long,
    val total: Long,
)

/** ユーザー統計情報。 */
@ConsistentCopyVisibility
data class UserStats internal constructor(
    val totalMessageCount: Long,
    val stamps: List<UserStampStats>,
    val datetime: Instant,
)

/**
 * [UserId] からユーザーハンドルを作成します。
 *
 * @param id ユーザーID
 * @return ユーザーハンドル
 */
fun user(id: UserId) = UserHandle(id)

/**
 * UUID からユーザーハンドルを作成します。
 *
 * @param id ユーザーID
 * @return ユーザーハンドル
 */
fun user(id: Uuid) = user(UserId(id))

/**
 * UUID 文字列からユーザーハンドルを作成します。
 *
 * @param id ユーザーID
 * @return ユーザーハンドル
 */
fun user(id: String) = user(Uuid.parse(id))
