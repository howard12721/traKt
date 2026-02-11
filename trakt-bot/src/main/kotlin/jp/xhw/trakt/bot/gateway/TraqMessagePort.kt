package jp.xhw.trakt.bot.gateway

import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.Message
import jp.xhw.trakt.bot.model.MessageClipSnapshot
import jp.xhw.trakt.bot.model.MessageHistoryQuery
import jp.xhw.trakt.bot.model.MessageId
import jp.xhw.trakt.bot.model.MessagePinSnapshot
import jp.xhw.trakt.bot.model.MessageSearchQuery
import jp.xhw.trakt.bot.model.MessageSearchResult
import jp.xhw.trakt.bot.model.MessageSearchSort
import jp.xhw.trakt.bot.model.MessageSnapshot
import jp.xhw.trakt.bot.model.MessageStampSnapshot
import jp.xhw.trakt.bot.model.SortDirection
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.UserId
import jp.xhw.trakt.bot.port.MessagePort
import jp.xhw.trakt.rest.apis.MessageApi
import jp.xhw.trakt.rest.models.PostMessageRequest
import jp.xhw.trakt.rest.models.PostMessageStampRequest

internal class TraqMessagePort(
    private val apiGateway: TraqApiGateway,
) : MessagePort {
    override suspend fun getMessage(messageId: MessageId): Message? = getSnapshot(messageId)?.handle

    override suspend fun getSnapshot(messageId: MessageId): MessageSnapshot? = fetchMessage(messageId)?.toSnapshot()

    override suspend fun editMessage(
        messageId: MessageId,
        content: String,
        embed: Boolean,
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

    override suspend fun getStamps(messageId: MessageId): List<MessageStampSnapshot> {
        val response = apiGateway.messageApi.getMessageStamps(messageId.value)
        return response
            .bodyOrThrow(operation = "getStamps(messageId=${messageId.value})")
            .map { stamp -> stamp.toSnapshot() }
    }

    override suspend fun getClips(messageId: MessageId): List<MessageClipSnapshot> {
        val response = apiGateway.messageApi.getMessageClips(messageId.value)
        return response
            .bodyOrThrow(operation = "getClips(messageId=${messageId.value})")
            .map { clip -> clip.toSnapshot() }
    }

    override suspend fun createPin(messageId: MessageId): MessagePinSnapshot {
        val response = apiGateway.messageApi.createPin(messageId.value)
        return response
            .bodyOrThrow(operation = "createPin(messageId=${messageId.value})")
            .toSnapshot()
    }

    override suspend fun getPin(messageId: MessageId): MessagePinSnapshot? {
        val response = apiGateway.messageApi.getPin(messageId.value)
        if (!response.success && response.status == 404) {
            return null
        }
        return response
            .bodyOrThrow(operation = "getPin(messageId=${messageId.value})")
            .toSnapshot()
    }

    override suspend fun removePin(messageId: MessageId) {
        apiGateway.messageApi
            .removePin(messageId.value)
            .requireSuccess(operation = "removePin(messageId=${messageId.value})")
    }

    override suspend fun getChannelMessages(
        channelId: ChannelId,
        query: MessageHistoryQuery,
    ): List<MessageSnapshot> {
        val response =
            apiGateway.messageApi.getMessages(
                channelId = channelId.value,
                limit = query.limit,
                offset = query.offset,
                since = query.since,
                until = query.until,
                inclusive = query.inclusive,
                order = query.order.toMessageOrder(),
            )
        return response
            .bodyOrThrow(operation = "getChannelMessages(channelId=${channelId.value})")
            .map { message -> message.toSnapshot() }
    }

    override suspend fun getDirectMessages(
        userId: UserId,
        query: MessageHistoryQuery,
    ): List<MessageSnapshot> {
        val response =
            apiGateway.messageApi.getDirectMessages(
                userId = userId.value,
                limit = query.limit,
                offset = query.offset,
                since = query.since,
                until = query.until,
                inclusive = query.inclusive,
                order = query.order.toDirectMessageOrder(),
            )
        return response
            .bodyOrThrow(operation = "getDirectMessages(userId=${userId.value})")
            .map { message -> message.toSnapshot() }
    }

    override suspend fun postDirectMessage(
        userId: UserId,
        content: String,
        embed: Boolean,
        nonce: String?,
    ): MessageSnapshot {
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
        return response
            .bodyOrThrow(operation = "postDirectMessage(userId=${userId.value})")
            .toSnapshot()
    }

    override suspend fun searchMessages(query: MessageSearchQuery): MessageSearchResult {
        val response =
            apiGateway.messageApi.searchMessages(
                word = query.word,
                after = query.after,
                before = query.before,
                `in` = query.inChannel?.id?.value,
                to = query.toUsers.map { user -> user.id.value }.ifEmpty { null },
                from = query.fromUsers.map { user -> user.id.value }.ifEmpty { null },
                citation = query.citation?.id?.value,
                bot = query.fromBot,
                hasURL = query.hasUrl,
                hasAttachments = query.hasAttachments,
                hasImage = query.hasImage,
                hasVideo = query.hasVideo,
                hasAudio = query.hasAudio,
                limit = query.limit,
                offset = query.offset,
                sort = query.sort.toApiModel(),
            )
        return response
            .bodyOrThrow(operation = "searchMessages()")
            .toResult()
    }

    private suspend fun fetchMessage(messageId: MessageId): jp.xhw.trakt.rest.models.Message? {
        val response = apiGateway.messageApi.getMessage(messageId.value)
        if (!response.success && response.status == 404) {
            return null
        }
        return response.bodyOrThrow(operation = "getMessage(messageId=${messageId.value})")
    }
}

private fun SortDirection.toMessageOrder(): MessageApi.OrderGetMessages =
    when (this) {
        SortDirection.ASC -> MessageApi.OrderGetMessages.ASC
        SortDirection.DESC -> MessageApi.OrderGetMessages.DESC
    }

private fun SortDirection.toDirectMessageOrder(): MessageApi.OrderGetDirectMessages =
    when (this) {
        SortDirection.ASC -> MessageApi.OrderGetDirectMessages.ASC
        SortDirection.DESC -> MessageApi.OrderGetDirectMessages.DESC
    }

private fun MessageSearchSort.toApiModel(): MessageApi.SortSearchMessages =
    when (this) {
        MessageSearchSort.CREATED_AT_DESC -> MessageApi.SortSearchMessages._CREATED_AT
        MessageSearchSort.CREATED_AT_ASC -> MessageApi.SortSearchMessages.CREATED_AT
        MessageSearchSort.UPDATED_AT_DESC -> MessageApi.SortSearchMessages._UPDATED_AT
        MessageSearchSort.UPDATED_AT_ASC -> MessageApi.SortSearchMessages.UPDATED_AT
    }
