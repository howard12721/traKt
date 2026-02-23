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
value class ChannelPath(
    val value: String,
)

/** チャンネルのトピック。 */
@JvmInline
value class ChannelTopic(
    val value: String,
)

/** チャンネルを参照するためのハンドル。 */
@JvmInline
value class ChannelHandle(
    val id: ChannelId,
) {
    companion object {
        /**
         * [ChannelId] から [ChannelHandle] を生成します。
         *
         * @param id チャンネルID
         * @return 生成されたチャンネルハンドル
         */
        fun of(id: ChannelId): ChannelHandle = ChannelHandle(id)

        /**
         * [Uuid] から [ChannelHandle] を生成します。
         *
         * @param id チャンネルID(UUID)
         * @return 生成されたチャンネルハンドル
         */
        fun of(id: Uuid): ChannelHandle = ChannelHandle(ChannelId(id))

        /**
         * UUID 文字列から [ChannelHandle] を生成します。
         *
         * @param id チャンネルID(UUID文字列)
         * @return 生成されたチャンネルハンドル
         */
        fun of(id: String): ChannelHandle = of(Uuid.parse(id))
    }
}

/** チャンネルの統計情報。 */
data class ChannelStats(
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
data class ChannelViewer(
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
    data class DirectMessage(
        override val id: ChannelId,
        val userId: UserId,
    ) : Channel {
        /** DM 相手ユーザーのハンドル。 */
        val user: UserHandle
            get() = UserHandle(userId)
    }

    /** チャンネル作成イベント等で使うチャンネル。 */
    data class Meta(
        override val id: ChannelId,
        override val parentId: ChannelId?,
        override val name: String,
        val creator: User,
        val path: ChannelPath,
    ) : PublicChannel

    /** API から取得するチャンネル。 */
    data class Detail(
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
    }
}

/** チャンネル一覧取得結果。 */
data class ChannelDirectory(
    val publicChannels: List<Channel.Detail>,
    val directMessageChannels: List<Channel.DirectMessage>,
)

/** チャンネルにピン留めされたメッセージ情報。 */
data class Pin(
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
