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
    val message: Message.Detail
}

/** クライアントの初期化が完了したときのイベント。 */
@ConsistentCopyVisibility
data class Initialized internal constructor(
    override val occurredAt: Instant,
) : BotEvent,
    UserEvent,
    TimedEvent

/** クライアントが停止したときのイベント。 */
@ConsistentCopyVisibility
data class Disposed internal constructor(
    override val occurredAt: Instant,
) : BotEvent,
    UserEvent,
    TimedEvent

object BotEvents {
    /** パブリックチャンネルにメッセージが投稿されたときのイベント。 */
    @ConsistentCopyVisibility
    data class MessageCreated internal constructor(
        override val occurredAt: Instant,
        override val message: Message.Detail,
    ) : BotMessageEvent

    /** パブリックチャンネルのメッセージが更新されたときのイベント。 */
    @ConsistentCopyVisibility
    data class MessageUpdated internal constructor(
        override val occurredAt: Instant,
        override val message: Message.Detail,
    ) : BotMessageEvent

    /** DM にメッセージが投稿されたときのイベント。 */
    @ConsistentCopyVisibility
    data class DirectMessageCreated internal constructor(
        override val occurredAt: Instant,
        override val message: Message.Detail,
    ) : BotMessageEvent

    /** DM のメッセージが更新されたときのイベント。 */
    @ConsistentCopyVisibility
    data class DirectMessageUpdated internal constructor(
        override val occurredAt: Instant,
        override val message: Message.Detail,
    ) : BotMessageEvent

    /** パブリックチャンネルのメッセージが削除されたときのイベント。 */
    @ConsistentCopyVisibility
    data class MessageDeleted internal constructor(
        override val occurredAt: Instant,
        val message: Message.Ref,
        val channel: Channel.Ref,
    ) : BotEvent,
        TimedEvent

    /** DM のメッセージが削除されたときのイベント。 */
    @ConsistentCopyVisibility
    data class DirectMessageDeleted internal constructor(
        override val occurredAt: Instant,
        val message: Message.Ref,
        val channel: Channel.Ref,
    ) : BotEvent,
        TimedEvent

    /** Bot がチャンネルへ追加されたときのイベント。 */
    @ConsistentCopyVisibility
    data class JoinedChannel internal constructor(
        override val occurredAt: Instant,
        val channel: Channel.Ref,
    ) : BotEvent,
        TimedEvent

    /** Bot がチャンネルから削除されたときのイベント。 */
    @ConsistentCopyVisibility
    data class LeftChannel internal constructor(
        override val occurredAt: Instant,
        val channel: Channel.Ref,
    ) : BotEvent,
        TimedEvent

    /** パブリックチャンネルが作成されたときのイベント。 */
    @ConsistentCopyVisibility
    data class ChannelCreated internal constructor(
        override val occurredAt: Instant,
        val channel: Channel.Meta,
    ) : BotEvent,
        TimedEvent

    /** チャンネルトピックが変更されたときのイベント。 */
    @ConsistentCopyVisibility
    data class ChannelTopicChanged internal constructor(
        override val occurredAt: Instant,
        val channel: Channel.Meta,
        val topic: String,
        val updater: User.Ref,
    ) : BotEvent,
        TimedEvent

    /** ユーザーが作成されたときのイベント。 */
    @ConsistentCopyVisibility
    data class UserCreated internal constructor(
        override val occurredAt: Instant,
        val user: User.Minimal,
    ) : BotEvent,
        TimedEvent

    /** ユーザーが有効化されたときのイベント。 */
    @ConsistentCopyVisibility
    data class UserActivated internal constructor(
        override val occurredAt: Instant,
        val user: User.Minimal,
    ) : BotEvent,
        TimedEvent

    /** スタンプが作成されたときのイベント。 */
    @ConsistentCopyVisibility
    data class StampCreated internal constructor(
        override val occurredAt: Instant,
        val stamp: Stamp.Basic,
    ) : BotEvent,
        TimedEvent

    /** ユーザータグが追加されたときのイベント。 */
    @ConsistentCopyVisibility
    data class TagAdded internal constructor(
        override val occurredAt: Instant,
        val userTag: UserTag.Ref,
        val tag: String,
    ) : BotEvent,
        TimedEvent

    /** ユーザータグが削除されたときのイベント。 */
    @ConsistentCopyVisibility
    data class TagRemoved internal constructor(
        override val occurredAt: Instant,
        val userTag: UserTag.Ref,
        val tag: String,
    ) : BotEvent,
        TimedEvent
}
