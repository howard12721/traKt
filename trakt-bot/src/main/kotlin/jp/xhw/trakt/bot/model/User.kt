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
value class UserHandle(
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
data class Bot(
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
    data class Minimal(
        override val id: UserId,
        override val name: String,
        override val displayName: String,
        override val iconFileId: FileId,
        override val isBot: Boolean,
    ) : User

    /** 一覧取得等で使う基本的なユーザー情報。 */
    data class Basic(
        override val id: UserId,
        override val name: String,
        override val displayName: String,
        override val iconFileId: FileId,
        override val isBot: Boolean,
        override val state: UserState,
        override val updatedAt: Instant,
    ) : StatefulUser

    /** API から取得するユーザー詳細情報。 */
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
        /** 所属グループのハンドル一覧。 */
        val groups: List<GroupHandle>
            get() = groupIds.map { GroupHandle(it) }

        /** ホームチャンネルのハンドル。 */
        val homeChannelHandle: ChannelHandle?
            get() = homeChannelId?.let { ChannelHandle(it) }
    }
}

/** ユーザータグ情報。 */
data class UserTag(
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
data class UserStampStats(
    val stampId: StampId,
    val count: Long,
    val total: Long,
)

/** ユーザー統計情報。 */
data class UserStats(
    val totalMessageCount: Long,
    val stamps: List<UserStampStats>,
    val datetime: Instant,
)
