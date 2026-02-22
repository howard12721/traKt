package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

@JvmInline
value class ChannelId(
    val value: Uuid,
)

@JvmInline
value class ChannelPath(
    val value: String,
)

@JvmInline
value class ChannelViewers(
    val value: List<User>,
)

@JvmInline
value class ChannelSubscribers(
    val value: List<UserId>,
)

@JvmInline
value class JoinedBots(
    val value: List<Bot>,
)

@JvmInline
value class ChannelTopic(
    val value: String,
)

@JvmInline
value class ChannelHandle(
    val id: ChannelId,
) {
    companion object {
        fun of(id: ChannelId): ChannelHandle = ChannelHandle(id)

        fun of(id: Uuid): ChannelHandle = ChannelHandle(ChannelId(id))

        fun of(id: String): ChannelHandle = of(Uuid.parse(id))
    }
}

data class ChannelStats(
    val messageCount: Int,
    val stamps: List<Stamp>,
    val users: List<User>,
    val datetime: Instant,
)

sealed interface Channel {
    val id: ChannelId

    val handle: ChannelHandle
        get() = ChannelHandle(id)

    sealed interface PublicChannel : Channel {
        val parentId: ChannelId?
        val name: String

        val parent: ChannelHandle?
            get() = parentId?.let { ChannelHandle(it) }
    }

    data class DirectMessage(
        override val id: ChannelId,
        val userId: UserId,
    ) : Channel {
        val user: UserHandle
            get() = UserHandle(userId)
    }

    data class Meta(
        override val id: ChannelId,
        override val parentId: ChannelId?,
        override val name: String,
        val creator: User,
        val path: ChannelPath,
    ) : PublicChannel

    data class Detail(
        override val id: ChannelId,
        override val parentId: ChannelId?,
        override val name: String,
        val isArchived: Boolean,
        val isForcedNotified: Boolean,
        val topic: ChannelTopic,
        val childrenIds: List<ChannelId>,
    ) : PublicChannel {
        val children: List<ChannelHandle>
            get() = childrenIds.map { ChannelHandle(it) }
    }
}

data class ChannelDirectory(
    val publicChannels: List<Channel.Detail>,
    val directMessageChannels: List<Channel.DirectMessage>,
)

data class Pin(
    val pinnerId: UserId,
    val pinnedAt: Instant,
    val message: Message,
) {
    val pinner: UserHandle
        get() = UserHandle(pinnerId)

    val messageId: MessageId
        get() = message.id
}
