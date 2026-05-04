package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** メッセージID。 */
@JvmInline
value class MessageId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): MessageId = parse(value)

        fun parse(value: String): MessageId = MessageId(Uuid.parse(value))
    }
}

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
)

/** メッセージのピン情報。 */
@ConsistentCopyVisibility
data class PinInfo internal constructor(
    val pinnerId: UserId,
    val pinnedAt: Instant,
)

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
