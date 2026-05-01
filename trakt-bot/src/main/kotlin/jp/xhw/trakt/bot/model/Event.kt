package jp.xhw.trakt.bot.model

import kotlin.time.Instant

/** WebSocket から配信されるイベントの共通型。 */
sealed interface Event

/** Bot token の WebSocket から配信されるイベントの共通型。 */
sealed interface BotEvent : Event

/** 発生時刻を持つイベントの共通型。 */
sealed interface TimedEvent : Event {
    val occurredAt: Instant
}

/** メッセージ本体を含むイベントの共通型。 */
sealed interface MessageEvent : BotEvent, TimedEvent {
    val message: Message
}

/** パブリックチャンネルにメッセージが投稿されたときのイベント。 */
@ConsistentCopyVisibility
data class MessageCreated internal constructor(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

/** パブリックチャンネルのメッセージが更新されたときのイベント。 */
@ConsistentCopyVisibility
data class MessageUpdated internal constructor(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

/** DM にメッセージが投稿されたときのイベント。 */
@ConsistentCopyVisibility
data class DirectMessageCreated internal constructor(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

/** DM のメッセージが更新されたときのイベント。 */
@ConsistentCopyVisibility
data class DirectMessageUpdated internal constructor(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

/** パブリックチャンネルのメッセージが削除されたときのイベント。 */
@ConsistentCopyVisibility
data class MessageDeleted internal constructor(
    override val occurredAt: Instant,
    val messageId: MessageId,
    val channelId: ChannelId,
) : BotEvent, TimedEvent

/** DM のメッセージが削除されたときのイベント。 */
@ConsistentCopyVisibility
data class DirectMessageDeleted internal constructor(
    override val occurredAt: Instant,
    val messageId: MessageId,
    val channelId: ChannelId,
) : BotEvent, TimedEvent

/** Bot がチャンネルへ追加されたときのイベント。 */
@ConsistentCopyVisibility
data class BotJoinedChannel internal constructor(
    override val occurredAt: Instant,
    val channelId: ChannelId,
) : BotEvent, TimedEvent {
    /** 追加先チャンネルのハンドル。 */
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

/** Bot がチャンネルから削除されたときのイベント。 */
@ConsistentCopyVisibility
data class BotLeftChannel internal constructor(
    override val occurredAt: Instant,
    val channelId: ChannelId,
) : BotEvent, TimedEvent {
    /** 削除元チャンネルのハンドル。 */
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

/** パブリックチャンネルが作成されたときのイベント。 */
@ConsistentCopyVisibility
data class ChannelCreated internal constructor(
    override val occurredAt: Instant,
    val channel: Channel.Meta,
) : BotEvent, TimedEvent

/** チャンネルトピックが変更されたときのイベント。 */
@ConsistentCopyVisibility
data class ChannelTopicChanged internal constructor(
    override val occurredAt: Instant,
    val channel: Channel.Meta,
    val topic: String,
    val updaterId: UserId,
) : BotEvent, TimedEvent {
    /** 更新実行ユーザーのハンドル。 */
    val updater: UserHandle
        get() = UserHandle(updaterId)
}

/** ユーザーが作成されたときのイベント。 */
@ConsistentCopyVisibility
data class UserCreated internal constructor(
    override val occurredAt: Instant,
    val user: User.Minimal,
) : BotEvent, TimedEvent

/** ユーザーが有効化されたときのイベント。 */
@ConsistentCopyVisibility
data class UserActivated internal constructor(
    override val occurredAt: Instant,
    val user: User.Minimal,
) : BotEvent, TimedEvent

/** スタンプが作成されたときのイベント。 */
@ConsistentCopyVisibility
data class StampCreated internal constructor(
    override val occurredAt: Instant,
    val stamp: Stamp.Basic,
) : BotEvent, TimedEvent

/** ユーザータグが追加されたときのイベント。 */
@ConsistentCopyVisibility
data class TagAdded internal constructor(
    override val occurredAt: Instant,
    val tagId: UserTagId,
    val tag: String,
) : BotEvent, TimedEvent

/** ユーザータグが削除されたときのイベント。 */
@ConsistentCopyVisibility
data class TagRemoved internal constructor(
    override val occurredAt: Instant,
    val tagId: UserTagId,
    val tag: String,
) : BotEvent, TimedEvent
