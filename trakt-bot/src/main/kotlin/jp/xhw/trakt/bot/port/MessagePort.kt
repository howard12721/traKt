package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.Message
import jp.xhw.trakt.bot.model.MessageClipSnapshot
import jp.xhw.trakt.bot.model.MessageHistoryQuery
import jp.xhw.trakt.bot.model.MessageId
import jp.xhw.trakt.bot.model.MessagePinSnapshot
import jp.xhw.trakt.bot.model.MessageSearchQuery
import jp.xhw.trakt.bot.model.MessageSearchResult
import jp.xhw.trakt.bot.model.MessageSnapshot
import jp.xhw.trakt.bot.model.MessageStampSnapshot
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.UserId

internal interface MessagePort {
    suspend fun getMessage(messageId: MessageId): Message?

    suspend fun getSnapshot(messageId: MessageId): MessageSnapshot?

    suspend fun editMessage(
        messageId: MessageId,
        content: String,
        embed: Boolean = false,
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

    suspend fun getStamps(messageId: MessageId): List<MessageStampSnapshot>

    suspend fun getClips(messageId: MessageId): List<MessageClipSnapshot>

    suspend fun createPin(messageId: MessageId): MessagePinSnapshot

    suspend fun getPin(messageId: MessageId): MessagePinSnapshot?

    suspend fun removePin(messageId: MessageId)

    suspend fun getChannelMessages(
        channelId: ChannelId,
        query: MessageHistoryQuery = MessageHistoryQuery(),
    ): List<MessageSnapshot>

    suspend fun getDirectMessages(
        userId: UserId,
        query: MessageHistoryQuery = MessageHistoryQuery(),
    ): List<MessageSnapshot>

    suspend fun postDirectMessage(
        userId: UserId,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot

    suspend fun searchMessages(query: MessageSearchQuery = MessageSearchQuery()): MessageSearchResult
}
