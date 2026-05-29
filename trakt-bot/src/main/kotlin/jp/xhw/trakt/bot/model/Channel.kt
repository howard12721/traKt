package jp.xhw.trakt.bot.model

import kotlin.jvm.JvmInline
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
value class ChannelPath(
    val value: String,
) {
    init {
        require(CHANNEL_PATH_REGEX.matches(value)) {
            "Invalid channel path: $value"
        }
    }

    companion object {
        private val CHANNEL_PATH_REGEX = Regex("^[a-zA-Z0-9-_]+(/[a-zA-Z0-9-_]+)*$")

        fun parse(raw: String): ChannelPath = ChannelPath(raw.trim().removePrefix("#"))
    }
}

/** チャンネルのトピック。 */
@JvmInline
value class ChannelTopic internal constructor(
    val value: String,
)

/** チャンネルの統計情報。 */
@ConsistentCopyVisibility
data class ChannelStats internal constructor(
    val messageCount: Int,
    val stamps: List<Stamp.Ref>,
    val users: List<User.Ref>,
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
    val user: User.Ref,
    val state: ChannelViewState,
    val updatedAt: Instant,
)

/** チャンネル。 */
sealed interface Channel {
    val id: ChannelId

    /** ID のみを持つチャンネル参照。 */
    class Ref(
        override val id: ChannelId,
    ) : Channel {
        override fun equals(other: Any?): Boolean = sameChannelId(this, other)

        override fun hashCode(): Int = id.hashCode()
    }

    /** パブリックチャンネル。 */
    sealed interface PublicChannel : Channel {
        val parent: Ref?
        val name: String
    }

    /** DM チャンネル。 */
    class DirectMessage internal constructor(
        override val id: ChannelId,
        val user: User.Ref,
    ) : Channel {
        override fun equals(other: Any?): Boolean = sameChannelId(this, other)

        override fun hashCode(): Int = id.hashCode()
    }

    /** チャンネル作成イベント等で使うチャンネル。 */
    class Meta internal constructor(
        override val id: ChannelId,
        override val parent: Ref?,
        override val name: String,
        val creator: User.Minimal,
        val path: ChannelPath,
    ) : PublicChannel {
        override fun equals(other: Any?): Boolean = sameChannelId(this, other)

        override fun hashCode(): Int = id.hashCode()
    }

    /** API から取得するチャンネル。 */
    class Detail internal constructor(
        override val id: ChannelId,
        override val parent: Ref?,
        override val name: String,
        val isArchived: Boolean,
        val isForcedNotified: Boolean,
        val topic: ChannelTopic,
        val children: List<Ref>,
    ) : PublicChannel {
        override fun equals(other: Any?): Boolean = sameChannelId(this, other)

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
    val pinner: User.Ref,
    val pinnedAt: Instant,
    val message: Message.Detail,
)
