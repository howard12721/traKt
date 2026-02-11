package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class MessageSnapshot(
    val handle: Message,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val plainText: String? = null,
    val embedded: List<EmbeddedItem> = emptyList(),
    val pinned: Boolean? = null,
    val stamps: List<MessageStampSnapshot> = emptyList(),
    val threadId: MessageId? = null,
    val nonce: String? = null,
) {
    val id: MessageId
        get() = handle.id

    val channel: Channel
        get() = handle.channel

    val channelId: ChannelId
        get() = channel.id

    val author: User
        get() = handle.author
}

data class MessageStampSnapshot(
    val userId: UserId,
    val stampId: StampId,
    val count: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
)

data class MessageClipSnapshot(
    val folderId: Uuid,
    val clippedAt: Instant,
)

data class MessagePinSnapshot(
    val pinnedBy: User,
    val pinnedAt: Instant,
)

data class MessageSearchResult(
    val totalHits: Long,
    val hits: List<MessageSnapshot>,
)
