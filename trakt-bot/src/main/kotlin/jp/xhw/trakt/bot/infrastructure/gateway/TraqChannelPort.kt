package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toDetail
import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toDirectory
import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toViewerModel
import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.bot.port.ChannelPort
import jp.xhw.trakt.rest.apis.ChannelApi
import jp.xhw.trakt.rest.models.PostMessageRequest
import jp.xhw.trakt.rest.models.PutChannelSubscribersRequest
import jp.xhw.trakt.rest.models.PutChannelTopicRequest
import kotlin.time.Instant

internal class TraqChannelPort(
    private val apiGateway: TraqApiGateway,
) : ChannelPort {
    override suspend fun fetchChannels(
        includeDirectMessages: Boolean,
        path: String?,
    ): ChannelDirectory {
        val response =
            apiGateway.channelApi.getChannels(
                includeDm = includeDirectMessages,
                path = path,
            )
        return response
            .bodyOrThrow(operation = "fetchChannels(includeDirectMessages=$includeDirectMessages, path=$path)")
            .toDirectory()
    }

    override suspend fun fetchChannel(channelId: ChannelId): Channel.Detail {
        val response = apiGateway.channelApi.getChannel(channelId.value)
        return response.bodyOrThrow(operation = "fetchChannel(channelId=${channelId.value})").toDetail()
    }

    override suspend fun fetchChannelPath(channelId: ChannelId): ChannelPath {
        val response = apiGateway.channelApi.getChannelPath(channelId.value)
        return ChannelPath(
            response.bodyOrThrow(operation = "fetchChannelPath(channelId=${channelId.value})").path,
        )
    }

    override suspend fun fetchChannelTopic(channelId: ChannelId): String {
        val response = apiGateway.channelApi.getChannelTopic(channelId.value)
        return response.bodyOrThrow(operation = "fetchChannelTopic(channelId=${channelId.value})").topic
    }

    override suspend fun setChannelTopic(
        channelId: ChannelId,
        topic: String,
    ) {
        apiGateway.channelApi
            .editChannelTopic(
                channelId = channelId.value,
                putChannelTopicRequest = PutChannelTopicRequest(topic = topic),
            ).requireSuccess(operation = "setChannelTopic(channelId=${channelId.value})")
    }

    override suspend fun fetchSubscribers(channelId: ChannelId): List<UserId> {
        val response = apiGateway.channelApi.getChannelSubscribers(channelId.value)
        return response.bodyOrThrow(operation = "fetchSubscribers(channelId=${channelId.value})").map { UserId(it) }
    }

    override suspend fun setSubscribers(
        channelId: ChannelId,
        subscribers: List<UserId>,
    ) {
        apiGateway.channelApi
            .setChannelSubscribers(
                channelId = channelId.value,
                putChannelSubscribersRequest =
                    PutChannelSubscribersRequest(
                        on = subscribers.map { it.value },
                    ),
            ).requireSuccess(operation = "setSubscribers(channelId=${channelId.value})")
    }

    override suspend fun fetchViewers(channelId: ChannelId): List<ChannelViewer> {
        val response = apiGateway.channelApi.getChannelViewers(channelId.value)
        return response.bodyOrThrow(operation = "fetchViewers(channelId=${channelId.value})").map { it.toViewerModel() }
    }

    override suspend fun fetchBots(channelId: ChannelId): List<Bot> {
        val response = apiGateway.channelApi.getChannelBots(channelId.value)
        return response
            .bodyOrThrow(operation = "fetchBots(channelId=${channelId.value})")
            .map { Bot(botId = BotId(it.id), userId = UserId(it.botUserId)) }
    }

    override suspend fun fetchChannelPins(channelId: ChannelId): List<Pin> {
        val response = apiGateway.channelApi.getChannelPins(channelId.value)
        return response.bodyOrThrow(operation = "fetchChannelPins(channelId=${channelId.value})").map { it.toModel() }
    }

    override suspend fun fetchChannelStats(channelId: ChannelId): ChannelStats {
        val response = apiGateway.channelApi.getChannelStats(channelId.value)
        val stats = response.bodyOrThrow(operation = "fetchChannelStats(channelId=${channelId.value})")
        return ChannelStats(
            messageCount = stats.totalMessageCount.toInt(),
            stamps = emptyList(),
            users = emptyList(),
            datetime = stats.datetime,
        )
    }

    override suspend fun fetchMessages(
        channelId: ChannelId,
        limit: Int?,
        offset: Int,
        since: Instant?,
        until: Instant?,
        inclusive: Boolean,
        order: SortDirection,
    ): List<Message> {
        val response =
            apiGateway.channelApi.getMessages(
                channelId = channelId.value,
                limit = limit,
                offset = offset,
                since = since,
                until = until,
                inclusive = inclusive,
                order = order.toChannelOrder(),
            )
        return response.bodyOrThrow(operation = "fetchMessages(channelId=${channelId.value})").map { it.toModel() }
    }

    override suspend fun sendMessage(
        channelId: ChannelId,
        content: String,
        embed: Boolean,
        nonce: String?,
    ): Message {
        val response =
            apiGateway.channelApi.postMessage(
                channelId = channelId.value,
                postMessageRequest =
                    PostMessageRequest(
                        content = content,
                        embed = embed,
                        nonce = nonce,
                    ),
            )
        return response.bodyOrThrow(operation = "sendMessage(channelId=${channelId.value})").toModel()
    }
}

private fun SortDirection.toChannelOrder(): ChannelApi.OrderGetMessages =
    when (this) {
        SortDirection.ASCENDING -> ChannelApi.OrderGetMessages.ASC
        SortDirection.DESCENDING -> ChannelApi.OrderGetMessages.DESC
    }
