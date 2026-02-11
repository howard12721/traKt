package jp.xhw.trakt.bot.gateway

import jp.xhw.trakt.bot.model.Channel
import jp.xhw.trakt.bot.model.ChannelDirectory
import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.ChannelPinSnapshot
import jp.xhw.trakt.bot.model.ChannelSnapshot
import jp.xhw.trakt.bot.model.EditChannelRequest
import jp.xhw.trakt.bot.model.MessageHistoryQuery
import jp.xhw.trakt.bot.model.MessageSnapshot
import jp.xhw.trakt.bot.model.SortDirection
import jp.xhw.trakt.bot.model.User
import jp.xhw.trakt.bot.model.UserId
import jp.xhw.trakt.bot.port.ChannelPort
import jp.xhw.trakt.rest.apis.ChannelApi
import jp.xhw.trakt.rest.models.PatchChannelRequest
import jp.xhw.trakt.rest.models.PostChannelRequest
import jp.xhw.trakt.rest.models.PostMessageRequest
import jp.xhw.trakt.rest.models.PutChannelTopicRequest

internal class TraqChannelPort(
    private val apiGateway: TraqApiGateway,
) : ChannelPort {
    override suspend fun getChannel(channelId: ChannelId): Channel? = getSnapshot(channelId)?.channel

    override suspend fun getSnapshot(channelId: ChannelId): ChannelSnapshot? {
        val response = apiGateway.channelApi.getChannel(channelId.value)
        if (!response.success && response.status == 404) {
            return null
        }
        return response
            .bodyOrThrow(operation = "getChannel(channelId=${channelId.value})")
            .toSnapshot()
    }

    override suspend fun createChannel(
        name: String,
        parent: Channel?,
    ): Channel {
        val response =
            apiGateway.channelApi.createChannel(
                PostChannelRequest(
                    name = name,
                    parent = parent?.id?.value,
                ),
            )
        return response
            .bodyOrThrow(operation = "createChannel(name=$name)")
            .toHandle()
    }

    override suspend fun editChannel(
        channelId: ChannelId,
        request: EditChannelRequest,
    ) {
        apiGateway.channelApi
            .editChannel(
                channelId = channelId.value,
                patchChannelRequest =
                    PatchChannelRequest(
                        name = request.name,
                        archived = request.archived,
                        force = request.force,
                        parent = request.parent?.id?.value,
                    ),
            ).requireSuccess(operation = "editChannel(channelId=${channelId.value})")
    }

    override suspend fun getPath(channelId: ChannelId): String? {
        val response = apiGateway.channelApi.getChannelPath(channelId.value)
        if (!response.success && response.status == 404) {
            return null
        }
        return response
            .bodyOrThrow(operation = "getPath(channelId=${channelId.value})")
            .path
    }

    override suspend fun getTopic(channelId: ChannelId): String? {
        val response = apiGateway.channelApi.getChannelTopic(channelId.value)
        if (!response.success && response.status == 404) {
            return null
        }
        return response
            .bodyOrThrow(operation = "getTopic(channelId=${channelId.value})")
            .topic
    }

    override suspend fun setTopic(
        channelId: ChannelId,
        topic: String,
    ) {
        apiGateway.channelApi
            .editChannelTopic(
                channelId = channelId.value,
                putChannelTopicRequest = PutChannelTopicRequest(topic = topic),
            ).requireSuccess(operation = "setTopic(channelId=${channelId.value})")
    }

    override suspend fun listChannels(
        includeDirectMessage: Boolean,
        path: String?,
    ): ChannelDirectory {
        val response =
            apiGateway.channelApi.getChannels(
                includeDm = includeDirectMessage,
                path = path,
            )
        return response
            .bodyOrThrow(
                operation =
                    "listChannels(includeDirectMessage=$includeDirectMessage, path=$path)",
            ).toDirectory()
    }

    override suspend fun getSubscribers(channelId: ChannelId): List<User> {
        val response = apiGateway.channelApi.getChannelSubscribers(channelId.value)
        return response
            .bodyOrThrow(operation = "getSubscribers(channelId=${channelId.value})")
            .map { userId -> User(UserId(userId)) }
    }

    override suspend fun getMessages(
        channelId: ChannelId,
        query: MessageHistoryQuery,
    ): List<MessageSnapshot> {
        val response =
            apiGateway.channelApi.getMessages(
                channelId = channelId.value,
                limit = query.limit,
                offset = query.offset,
                since = query.since,
                until = query.until,
                inclusive = query.inclusive,
                order = query.order.toChannelOrder(),
            )
        return response
            .bodyOrThrow(operation = "getMessages(channelId=${channelId.value})")
            .map { message -> message.toSnapshot() }
    }

    override suspend fun getPins(channelId: ChannelId): List<ChannelPinSnapshot> {
        val response = apiGateway.channelApi.getChannelPins(channelId.value)
        return response
            .bodyOrThrow(operation = "getPins(channelId=${channelId.value})")
            .map { pin -> pin.toSnapshot() }
    }

    override suspend fun postMessage(
        channelId: ChannelId,
        content: String,
        embed: Boolean,
        nonce: String?,
    ): MessageSnapshot {
        val response =
            apiGateway.messageApi.postMessage(
                channelId = channelId.value,
                postMessageRequest =
                    PostMessageRequest(
                        content = content,
                        embed = embed,
                        nonce = nonce,
                    ),
            )
        return response
            .bodyOrThrow(operation = "postMessage(channelId=${channelId.value})")
            .toSnapshot()
    }
}

private fun SortDirection.toChannelOrder(): ChannelApi.OrderGetMessages =
    when (this) {
        SortDirection.ASC -> ChannelApi.OrderGetMessages.ASC
        SortDirection.DESC -> ChannelApi.OrderGetMessages.DESC
    }
