package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** チャンネルID。 */
@JvmInline
value class ChannelId(
    val value: Uuid,
)

/** チャンネルのフルパス。 */
@JvmInline
value class ChannelPath internal constructor(
    val value: String,
)

/** チャンネルのトピック。 */
@JvmInline
value class ChannelTopic internal constructor(
    val value: String,
)

/** チャンネルを参照するためのハンドル。 */
@JvmInline
value class ChannelHandle internal constructor(
    val id: ChannelId,
)

/** チャンネルの統計情報。 */
@ConsistentCopyVisibility
data class ChannelStats internal constructor(
    val messageCount: Int,
    val stamps: List<Stamp>,
    val users: List<User>,
    val datetime: Instant,
)

/** チャンネル閲覧状態。 */
enum class ChannelViewState {
    NONE,
    STALE_VIEWING,
    MONITORING,
    EDITING,
}

/** チャンネル閲覧者情報。 */
@ConsistentCopyVisibility
data class ChannelViewer internal constructor(
    val userId: UserId,
    val state: ChannelViewState,
    val updatedAt: Instant,
) {
    /** 閲覧者ユーザーのハンドル。 */
    val user: UserHandle
        get() = UserHandle(userId)
}

/** チャンネル。 */
sealed interface Channel {
    val id: ChannelId

    /** このチャンネルを指すハンドル。 */
    val handle: ChannelHandle
        get() = ChannelHandle(id)

    /** パブリックチャンネル。 */
    sealed interface PublicChannel : Channel {
        val parentId: ChannelId?
        val name: String

        /** 親チャンネルのハンドル。 */
        val parent: ChannelHandle?
            get() = parentId?.let { ChannelHandle(it) }
    }

    /** DM チャンネル。 */
    class DirectMessage internal constructor(
        override val id: ChannelId,
        val userId: UserId,
    ) : Channel {
        /** DM 相手ユーザーのハンドル。 */
        val user: UserHandle
            get() = UserHandle(userId)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Channel) return false

            return this.id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }

    /** チャンネル作成イベント等で使うチャンネル。 */
    class Meta internal constructor(
        override val id: ChannelId,
        override val parentId: ChannelId?,
        override val name: String,
        val creator: User,
        val path: ChannelPath,
    ) : PublicChannel {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Channel) return false

            return this.id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }

    /** API から取得するチャンネル。 */
    class Detail internal constructor(
        override val id: ChannelId,
        override val parentId: ChannelId?,
        override val name: String,
        val isArchived: Boolean,
        val isForcedNotified: Boolean,
        val topic: ChannelTopic,
        val childrenIds: List<ChannelId>,
    ) : PublicChannel {
        /** 子チャンネルのハンドル一覧。 */
        val children: List<ChannelHandle>
            get() = childrenIds.map { ChannelHandle(it) }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Channel) return false

            return this.id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }
}

/** チャンネル一覧取得結果。 */
@ConsistentCopyVisibility
data class ChannelDirectory internal constructor(
    val publicChannels: List<Channel.Detail>,
    val directMessageChannels: List<Channel.DirectMessage>,
)

/** チャンネルにピン留めされたメッセージ情報。 */
@ConsistentCopyVisibility
data class Pin internal constructor(
    val pinnerId: UserId,
    val pinnedAt: Instant,
    val message: Message,
) {
    /** ピン留めしたユーザーのハンドル。 */
    val pinner: UserHandle
        get() = UserHandle(pinnerId)

    /** ピン留め対象メッセージID。 */
    val messageId: MessageId
        get() = message.id
}

fun channel(id: ChannelId) = ChannelHandle(id)

fun channel(id: Uuid) = channel(ChannelId(id))

fun channel(id: String) = channel(Uuid.parse(id))
