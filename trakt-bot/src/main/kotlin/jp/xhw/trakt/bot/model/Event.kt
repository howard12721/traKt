package jp.xhw.trakt.bot.model

import kotlin.time.Instant

sealed interface Event {
    val occurredAt: Instant
}

sealed interface MessageEvent : Event {
    val message: Message
}

data class MessageCreated(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

data class MessageUpdated(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

data class DirectMessageCreated(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

data class DirectMessageUpdated(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

data class MessageDeleted(
    override val occurredAt: Instant,
    val messageId: MessageId,
    val channelId: ChannelId,
) : Event

data class DirectMessageDeleted(
    override val occurredAt: Instant,
    val messageId: MessageId,
    val channelId: ChannelId,
) : Event

data class BotJoinedChannel(
    override val occurredAt: Instant,
    val channelId: ChannelId,
) : Event {
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

data class BotLeftChannel(
    override val occurredAt: Instant,
    val channelId: ChannelId,
) : Event {
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

data class ChannelCreated(
    override val occurredAt: Instant,
    val channel: Channel.Meta,
) : Event

data class ChannelTopicChanged(
    override val occurredAt: Instant,
    val channel: Channel.Meta,
    val topic: String,
    val updaterId: UserId,
) : Event {
    val updater: UserHandle
        get() = UserHandle(updaterId)
}

data class UserCreated(
    override val occurredAt: Instant,
    val user: User.Minimal,
) : Event

data class UserActivated(
    override val occurredAt: Instant,
    val user: User.Minimal,
) : Event

data class StampCreated(
    override val occurredAt: Instant,
    val stamp: Stamp.Basic,
) : Event

data class TagAdded(
    override val occurredAt: Instant,
    val tagId: UserTagId,
    val tag: String,
) : Event

data class TagRemoved(
    override val occurredAt: Instant,
    val tagId: UserTagId,
    val tag: String,
) : Event
