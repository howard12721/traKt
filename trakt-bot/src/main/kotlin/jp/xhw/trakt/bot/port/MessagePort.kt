package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant

internal interface MessagePort {
    suspend fun fetchMessage(messageId: MessageId): Message

    suspend fun editMessage(
        messageId: MessageId,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    )

    suspend fun deleteMessage(messageId: MessageId)

    suspend fun addStamp(
        messageId: MessageId,
        stampId: StampId,
        count: Int = 1,
    )

    suspend fun removeStamp(
        messageId: MessageId,
        stampId: StampId,
    )

    suspend fun fetchStamps(messageId: MessageId): List<MessageStamp>

    suspend fun fetchPinInfo(messageId: MessageId): PinInfo

    suspend fun createPin(messageId: MessageId): PinInfo

    suspend fun removePin(messageId: MessageId)

    suspend fun sendDirectMessage(
        userId: UserId,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): Message

    suspend fun fetchDirectMessages(
        userId: UserId,
        limit: Int? = null,
        offset: Int = 0,
        since: Instant? = null,
        until: Instant? = null,
        inclusive: Boolean = false,
        order: SortDirection = SortDirection.DESCENDING,
    ): List<Message>

    suspend fun searchMessages(
        word: String? = null,
        after: Instant? = null,
        before: Instant? = null,
        inChannel: ChannelId? = null,
        to: List<UserId>? = null,
        from: List<UserId>? = null,
        citation: MessageId? = null,
        bot: Boolean? = null,
        hasUrl: Boolean? = null,
        hasAttachments: Boolean? = null,
        hasImage: Boolean? = null,
        hasVideo: Boolean? = null,
        hasAudio: Boolean? = null,
        limit: Int? = null,
        offset: Int? = null,
        sort: SortDirection = SortDirection.DESCENDING,
    ): SearchResult
}
