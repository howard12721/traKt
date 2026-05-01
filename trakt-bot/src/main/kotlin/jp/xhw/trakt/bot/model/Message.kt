package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** メッセージID。 */
@JvmInline
value class MessageId(
    val value: Uuid,
)

/** メッセージを参照するためのハンドル。 */
@JvmInline
value class MessageHandle internal constructor(
    val id: MessageId,
)

/** メッセージ本文とメタ情報。 */
class Message internal constructor(
    val id: MessageId,
    val authorId: UserId,
    val channelId: ChannelId,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isPinned: Boolean,
    val stamps: List<MessageStamp>,
    val threadId: Uuid?,
    val nonce: String? = null,
) {
    /** このメッセージを指すハンドル。 */
    val handle: MessageHandle
        get() = MessageHandle(id)

    /** 投稿者のハンドル。 */
    val author: UserHandle
        get() = UserHandle(authorId)

    /** 投稿先チャンネルのハンドル。 */
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)

    override fun equals(other: Any?): Boolean = other is Message && this.id == other.id

    override fun hashCode(): Int = id.hashCode()
}

/** メッセージへ付与されたスタンプ情報。 */
@ConsistentCopyVisibility
data class MessageStamp internal constructor(
    val userId: UserId,
    val stampId: StampId,
    val count: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    /** スタンプを押したユーザーのハンドル。 */
    val user: UserHandle
        get() = UserHandle(userId)

    /** 付与されたスタンプのハンドル。 */
    val stamp: StampHandle
        get() = StampHandle(stampId)
}

/** メッセージのピン情報。 */
@ConsistentCopyVisibility
data class PinInfo internal constructor(
    val pinnerId: UserId,
    val pinnedAt: Instant,
) {
    /** ピン留めしたユーザーのハンドル。 */
    val pinner: UserHandle
        get() = UserHandle(pinnerId)
}

/** メッセージ取得・検索時の並び順。 */
enum class SortDirection {
    ASCENDING,
    DESCENDING,
}

/** メッセージ検索結果。 */
@ConsistentCopyVisibility
data class SearchResult internal constructor(
    val totalHits: Long,
    val hits: List<Message>,
)

fun message(id: MessageId) = MessageHandle(id)

fun message(uuid: Uuid) = MessageHandle(MessageId(uuid))

fun message(id: String) = message(Uuid.parse(id))
