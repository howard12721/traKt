package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.bot.port.MessagePort
import jp.xhw.trakt.rest.apis.MessageApi
import jp.xhw.trakt.rest.models.PostMessageRequest
import jp.xhw.trakt.rest.models.PostMessageStampRequest

internal class TraqMessagePort(
    private val apiGateway: TraqApiGateway,
) : MessagePort {
    override suspend fun fetchMessage(messageId: MessageId): Message {
        val response = apiGateway.messageApi.getMessage(messageId.value)
        return response.bodyOrThrow(operation = "fetchMessage(messageId=${messageId.value})").toModel()
    }

    override suspend fun editMessage(
        messageId: MessageId,
        content: String,
        embed: Boolean,
        nonce: String?,
    ) {
        apiGateway.messageApi
            .editMessage(
                messageId = messageId.value,
                postMessageRequest =
                    PostMessageRequest(
                        content = content,
                        embed = embed,
                    ),
            ).requireSuccess(operation = "editMessage(messageId=${messageId.value})")
    }

    override suspend fun deleteMessage(messageId: MessageId) {
        apiGateway.messageApi
            .deleteMessage(messageId.value)
            .requireSuccess(operation = "deleteMessage(messageId=${messageId.value})")
    }

    override suspend fun addStamp(
        messageId: MessageId,
        stampId: StampId,
        count: Int,
    ) {
        require(count > 0) { "count must be positive" }
        apiGateway.messageApi
            .addMessageStamp(
                messageId = messageId.value,
                stampId = stampId.value,
                postMessageStampRequest = PostMessageStampRequest(count = count),
            ).requireSuccess(operation = "addStamp(messageId=${messageId.value}, stampId=${stampId.value})")
    }

    override suspend fun removeStamp(
        messageId: MessageId,
        stampId: StampId,
    ) {
        apiGateway.messageApi
            .removeMessageStamp(
                messageId = messageId.value,
                stampId = stampId.value,
            ).requireSuccess(operation = "removeStamp(messageId=${messageId.value}, stampId=${stampId.value})")
    }

    override suspend fun fetchStamps(messageId: MessageId): List<MessageStamp> {
        val response = apiGateway.messageApi.getMessageStamps(messageId.value)
        return response.bodyOrThrow(operation = "fetchStamps(messageId=${messageId.value})").map { it.toModel() }
    }

    override suspend fun createPin(messageId: MessageId): PinInfo {
        val response = apiGateway.messageApi.createPin(messageId.value)
        return response.bodyOrThrow(operation = "createPin(messageId=${messageId.value})").toModel()
    }

    override suspend fun removePin(messageId: MessageId) {
        apiGateway.messageApi
            .removePin(messageId.value)
            .requireSuccess(operation = "removePin(messageId=${messageId.value})")
    }

    override suspend fun fetchPinInfo(messageId: MessageId): PinInfo {
        val response = apiGateway.messageApi.getPin(messageId.value)
        return response.bodyOrThrow(operation = "fetchPinInfo(messageId=${messageId.value})").toModel()
    }

    override suspend fun sendDirectMessage(
        userId: UserId,
        content: String,
        embed: Boolean,
        nonce: String?,
    ): Message {
        val response =
            apiGateway.messageApi.postDirectMessage(
                userId = userId.value,
                postMessageRequest =
                    PostMessageRequest(
                        content = content,
                        embed = embed,
                        nonce = nonce,
                    ),
            )
        return response.bodyOrThrow(operation = "sendDirectMessage(userId=${userId.value})").toModel()
    }

    override suspend fun fetchDirectMessages(
        userId: UserId,
        limit: Int?,
        offset: Int,
        since: kotlin.time.Instant?,
        until: kotlin.time.Instant?,
        inclusive: Boolean,
        order: SortDirection,
    ): List<Message> {
        val response =
            apiGateway.messageApi.getDirectMessages(
                userId = userId.value,
                limit = limit,
                offset = offset,
                since = since,
                until = until,
                inclusive = inclusive,
                order = order.toDirectMessageOrder(),
            )
        return response.bodyOrThrow(operation = "fetchDirectMessages(userId=${userId.value})").map { it.toModel() }
    }

    override suspend fun searchMessages(
        word: String?,
        after: kotlin.time.Instant?,
        before: kotlin.time.Instant?,
        inChannel: ChannelId?,
        to: List<UserId>?,
        from: List<UserId>?,
        citation: MessageId?,
        bot: Boolean?,
        hasUrl: Boolean?,
        hasAttachments: Boolean?,
        hasImage: Boolean?,
        hasVideo: Boolean?,
        hasAudio: Boolean?,
        limit: Int?,
        offset: Int?,
        sort: SortDirection,
    ): SearchResult {
        val response =
            apiGateway.messageApi.searchMessages(
                word = word,
                after = after,
                before = before,
                `in` = inChannel?.value,
                to = to?.map { it.value }?.ifEmpty { null },
                from = from?.map { it.value }?.ifEmpty { null },
                citation = citation?.value,
                bot = bot,
                hasURL = hasUrl,
                hasAttachments = hasAttachments,
                hasImage = hasImage,
                hasVideo = hasVideo,
                hasAudio = hasAudio,
                limit = limit,
                offset = offset,
                sort = sort.toSearchSort(),
            )
        val result = response.bodyOrThrow(operation = "searchMessages()")
        return SearchResult(
            totalHits = result.totalHits,
            hits = result.hits.map { it.toModel() },
        )
    }
}

private fun SortDirection.toDirectMessageOrder(): MessageApi.OrderGetDirectMessages =
    when (this) {
        SortDirection.ASCENDING -> MessageApi.OrderGetDirectMessages.ASC
        SortDirection.DESCENDING -> MessageApi.OrderGetDirectMessages.DESC
    }

private fun SortDirection.toSearchSort(): MessageApi.SortSearchMessages =
    when (this) {
        SortDirection.DESCENDING -> MessageApi.SortSearchMessages._CREATED_AT
        SortDirection.ASCENDING -> MessageApi.SortSearchMessages.CREATED_AT
    }
