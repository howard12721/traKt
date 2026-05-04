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

/** Bot token のメッセージ本体を含むイベントの共通型。 */
sealed interface BotMessageEvent :
    BotEvent,
    TimedEvent {
    val message: Message
}

/** パブリックチャンネルにメッセージが投稿されたときのイベント。 */
@ConsistentCopyVisibility
data class BotMessageCreated internal constructor(
    override val occurredAt: Instant,
    override val message: Message,
) : BotMessageEvent

/** パブリックチャンネルのメッセージが更新されたときのイベント。 */
@ConsistentCopyVisibility
data class BotMessageUpdated internal constructor(
    override val occurredAt: Instant,
    override val message: Message,
) : BotMessageEvent

/** DM にメッセージが投稿されたときのイベント。 */
@ConsistentCopyVisibility
data class BotDirectMessageCreated internal constructor(
    override val occurredAt: Instant,
    override val message: Message,
) : BotMessageEvent

/** DM のメッセージが更新されたときのイベント。 */
@ConsistentCopyVisibility
data class BotDirectMessageUpdated internal constructor(
    override val occurredAt: Instant,
    override val message: Message,
) : BotMessageEvent

/** パブリックチャンネルのメッセージが削除されたときのイベント。 */
@ConsistentCopyVisibility
data class BotMessageDeleted internal constructor(
    override val occurredAt: Instant,
    val messageId: MessageId,
    val channelId: ChannelId,
) : BotEvent,
    TimedEvent

/** DM のメッセージが削除されたときのイベント。 */
@ConsistentCopyVisibility
data class BotDirectMessageDeleted internal constructor(
    override val occurredAt: Instant,
    val messageId: MessageId,
    val channelId: ChannelId,
) : BotEvent,
    TimedEvent

/** Bot がチャンネルへ追加されたときのイベント。 */
@ConsistentCopyVisibility
data class BotJoinedChannel internal constructor(
    override val occurredAt: Instant,
    val channelId: ChannelId,
) : BotEvent,
    TimedEvent {
    /** 追加先チャンネルのID。 */
    val channel: ChannelId
        get() = channelId
}

/** Bot がチャンネルから削除されたときのイベント。 */
@ConsistentCopyVisibility
data class BotLeftChannel internal constructor(
    override val occurredAt: Instant,
    val channelId: ChannelId,
) : BotEvent,
    TimedEvent {
    /** 削除元チャンネルのID。 */
    val channel: ChannelId
        get() = channelId
}

/** パブリックチャンネルが作成されたときのイベント。 */
@ConsistentCopyVisibility
data class BotChannelCreated internal constructor(
    override val occurredAt: Instant,
    val channel: Channel.Meta,
) : BotEvent,
    TimedEvent

/** チャンネルトピックが変更されたときのイベント。 */
@ConsistentCopyVisibility
data class BotChannelTopicChanged internal constructor(
    override val occurredAt: Instant,
    val channel: Channel.Meta,
    val topic: String,
    val updaterId: UserId,
) : BotEvent,
    TimedEvent {
    /** 更新実行ユーザーのID。 */
    val updater: UserId
        get() = updaterId
}

/** ユーザーが作成されたときのイベント。 */
@ConsistentCopyVisibility
data class BotUserCreated internal constructor(
    override val occurredAt: Instant,
    val user: User.Minimal,
) : BotEvent,
    TimedEvent

/** ユーザーが有効化されたときのイベント。 */
@ConsistentCopyVisibility
data class BotUserActivated internal constructor(
    override val occurredAt: Instant,
    val user: User.Minimal,
) : BotEvent,
    TimedEvent

/** スタンプが作成されたときのイベント。 */
@ConsistentCopyVisibility
data class BotStampCreated internal constructor(
    override val occurredAt: Instant,
    val stamp: Stamp.Basic,
) : BotEvent,
    TimedEvent

/** ユーザータグが追加されたときのイベント。 */
@ConsistentCopyVisibility
data class BotTagAdded internal constructor(
    override val occurredAt: Instant,
    val tagId: UserTagId,
    val tag: String,
) : BotEvent,
    TimedEvent

/** ユーザータグが削除されたときのイベント。 */
@ConsistentCopyVisibility
data class BotTagRemoved internal constructor(
    override val occurredAt: Instant,
    val tagId: UserTagId,
    val tag: String,
) : BotEvent,
    TimedEvent
