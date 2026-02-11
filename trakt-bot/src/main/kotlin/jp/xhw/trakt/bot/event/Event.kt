package jp.xhw.trakt.bot.event

import jp.xhw.trakt.bot.model.Channel
import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.ChannelSnapshot
import jp.xhw.trakt.bot.model.MessageId
import jp.xhw.trakt.bot.model.MessageSnapshot
import jp.xhw.trakt.bot.model.StampSnapshot
import jp.xhw.trakt.bot.model.User
import jp.xhw.trakt.bot.model.UserSnapshot
import jp.xhw.trakt.bot.model.UserTagId
import kotlin.time.Instant

sealed interface Event {
    val occurredAt: Instant
}

sealed interface MessageEvent : Event {
    val message: MessageSnapshot
}

data class MessageCreated(
    override val occurredAt: Instant,
    override val message: MessageSnapshot,
) : MessageEvent

data class MessageUpdated(
    override val occurredAt: Instant,
    override val message: MessageSnapshot,
) : MessageEvent

data class DirectMessageCreated(
    override val occurredAt: Instant,
    override val message: MessageSnapshot,
) : MessageEvent

data class DirectMessageUpdated(
    override val occurredAt: Instant,
    override val message: MessageSnapshot,
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

data class Joined(
    override val occurredAt: Instant,
    val channel: Channel,
) : Event {
    val channelId: ChannelId
        get() = channel.id
}

data class Left(
    override val occurredAt: Instant,
    val channel: Channel,
) : Event {
    val channelId: ChannelId
        get() = channel.id
}

data class ChannelCreated(
    override val occurredAt: Instant,
    val channel: ChannelSnapshot,
) : Event {
    val handle: Channel
        get() = channel.channel
}

data class ChannelTopicChanged(
    override val occurredAt: Instant,
    val channel: Channel,
    val topic: String,
    val updater: User,
) : Event

data class UserCreated(
    override val occurredAt: Instant,
    val user: UserSnapshot,
) : Event

data class UserActivated(
    override val occurredAt: Instant,
    val user: UserSnapshot,
) : Event

data class StampCreated(
    override val occurredAt: Instant,
    val stamp: StampSnapshot,
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
