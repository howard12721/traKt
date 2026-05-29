package jp.xhw.trakt.bot.model

import kotlin.jvm.JvmInline
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

/** メッセージ。 */
sealed interface Message {
    val id: MessageId

    /** ID のみを持つメッセージ参照。 */
    class Ref(
        override val id: MessageId,
    ) : Message {
        override fun equals(other: Any?): Boolean = sameMessageId(this, other)

        override fun hashCode(): Int = id.hashCode()
    }

    /** メッセージ本文とメタ情報。 */
    class Detail internal constructor(
        override val id: MessageId,
        val author: User.Ref,
        val channel: Channel.Ref,
        val content: String,
        val createdAt: Instant,
        val updatedAt: Instant,
        val isPinned: Boolean,
        val stamps: List<MessageStamp>,
        val threadId: Uuid?,
        val nonce: String? = null,
    ) : Message {
        override fun equals(other: Any?): Boolean = sameMessageId(this, other)

        override fun hashCode(): Int = id.hashCode()
    }
}

/** メッセージへ付与されたスタンプ情報。 */
@ConsistentCopyVisibility
data class MessageStamp internal constructor(
    val user: User.Ref,
    val stamp: Stamp.Ref,
    val count: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
)

/** メッセージのピン情報。 */
@ConsistentCopyVisibility
data class PinInfo internal constructor(
    val pinner: User.Ref,
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
    val hits: List<Message.Detail>,
)
