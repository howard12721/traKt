package jp.xhw.trakt.bot.model

import kotlin.time.Instant

/** WebSocket から配信される Bot イベントの共通型。 */
sealed interface Event {
    val occurredAt: Instant
}

/** メッセージ本体を含むイベントの共通型。 */
sealed interface MessageEvent : Event {
    val message: Message
}

/** パブリックチャンネルにメッセージが投稿されたときのイベント。 */
data class MessageCreated(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

/** パブリックチャンネルのメッセージが更新されたときのイベント。 */
data class MessageUpdated(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

/** DM にメッセージが投稿されたときのイベント。 */
data class DirectMessageCreated(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

/** DM のメッセージが更新されたときのイベント。 */
data class DirectMessageUpdated(
    override val occurredAt: Instant,
    override val message: Message,
) : MessageEvent

/** パブリックチャンネルのメッセージが削除されたときのイベント。 */
data class MessageDeleted(
    override val occurredAt: Instant,
    val messageId: MessageId,
    val channelId: ChannelId,
) : Event

/** DM のメッセージが削除されたときのイベント。 */
data class DirectMessageDeleted(
    override val occurredAt: Instant,
    val messageId: MessageId,
    val channelId: ChannelId,
) : Event

/** Bot がチャンネルへ追加されたときのイベント。 */
data class BotJoinedChannel(
    override val occurredAt: Instant,
    val channelId: ChannelId,
) : Event {
    /** 追加先チャンネルのハンドル。 */
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

/** Bot がチャンネルから削除されたときのイベント。 */
data class BotLeftChannel(
    override val occurredAt: Instant,
    val channelId: ChannelId,
) : Event {
    /** 削除元チャンネルのハンドル。 */
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

/** パブリックチャンネルが作成されたときのイベント。 */
data class ChannelCreated(
    override val occurredAt: Instant,
    val channel: Channel.Meta,
) : Event

/** チャンネルトピックが変更されたときのイベント。 */
data class ChannelTopicChanged(
    override val occurredAt: Instant,
    val channel: Channel.Meta,
    val topic: String,
    val updaterId: UserId,
) : Event {
    /** 更新実行ユーザーのハンドル。 */
    val updater: UserHandle
        get() = UserHandle(updaterId)
}

/** ユーザーが作成されたときのイベント。 */
data class UserCreated(
    override val occurredAt: Instant,
    val user: User.Minimal,
) : Event

/** ユーザーが有効化されたときのイベント。 */
data class UserActivated(
    override val occurredAt: Instant,
    val user: User.Minimal,
) : Event

/** スタンプが作成されたときのイベント。 */
data class StampCreated(
    override val occurredAt: Instant,
    val stamp: Stamp.Basic,
) : Event

/** ユーザータグが追加されたときのイベント。 */
data class TagAdded(
    override val occurredAt: Instant,
    val tagId: UserTagId,
    val tag: String,
) : Event

/** ユーザータグが削除されたときのイベント。 */
data class TagRemoved(
    override val occurredAt: Instant,
    val tagId: UserTagId,
    val tag: String,
) : Event
