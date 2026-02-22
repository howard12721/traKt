package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

@JvmInline
value class MessageId(
    val value: Uuid,
)

@JvmInline
value class MessageHandle(
    val id: MessageId,
) {
    companion object {
        fun of(id: MessageId): MessageHandle = MessageHandle(id)

        fun of(id: Uuid): MessageHandle = MessageHandle(MessageId(id))

        fun of(id: String): MessageHandle = of(Uuid.parse(id))
    }
}

data class Message(
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
    val handle: MessageHandle
        get() = MessageHandle(id)

    val author: UserHandle
        get() = UserHandle(authorId)

    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

data class MessageStamp(
    val userId: UserId,
    val stampId: StampId,
    val count: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    val user: UserHandle
        get() = UserHandle(userId)

    val stamp: StampHandle
        get() = StampHandle(stampId)
}

data class PinInfo(
    val pinnerId: UserId,
    val pinnedAt: Instant,
) {
    val pinner: UserHandle
        get() = UserHandle(pinnerId)
}

enum class SortDirection {
    ASCENDING,
    DESCENDING,
}

data class SearchResult(
    val totalHits: Long,
    val hits: List<Message>,
)
