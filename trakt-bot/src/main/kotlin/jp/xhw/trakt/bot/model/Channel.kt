package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** チャンネルID。 */
@JvmInline
value class ChannelId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): ChannelId = parse(value)

        fun parse(value: String): ChannelId = ChannelId(Uuid.parse(value))
    }
}

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
    /** 閲覧者ユーザーの ID。 */
    val user: UserId
        get() = userId
}

/** チャンネル。 */
sealed interface Channel {
    val id: ChannelId

    /** パブリックチャンネル。 */
    sealed interface PublicChannel : Channel {
        val parentId: ChannelId?
        val name: String

        /** 親チャンネルの ID。 */
        val parent: ChannelId?
            get() = parentId
    }

    /** DM チャンネル。 */
    class DirectMessage internal constructor(
        override val id: ChannelId,
        val userId: UserId,
    ) : Channel {
        /** DM 相手ユーザーの ID。 */
        val user: UserId
            get() = userId

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
        /** 子チャンネル ID 一覧。 */
        val children: List<ChannelId>
            get() = childrenIds

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
    /** ピン留めしたユーザーの ID。 */
    val pinner: UserId
        get() = pinnerId

    /** ピン留め対象メッセージID。 */
    val messageId: MessageId
        get() = message.id
}
