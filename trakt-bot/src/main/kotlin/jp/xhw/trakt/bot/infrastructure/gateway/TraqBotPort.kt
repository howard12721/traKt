package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.model.BotId
import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.port.BotPort
import jp.xhw.trakt.rest.models.PostBotActionJoinRequest
import jp.xhw.trakt.rest.models.PostBotActionLeaveRequest

internal class TraqBotPort(
    private val apiGateway: TraqApiGateway,
) : BotPort {
    override suspend fun joinChannel(
        botId: BotId,
        channelId: ChannelId,
    ) {
        apiGateway.botApi
            .letBotJoinChannel(botId.value, PostBotActionJoinRequest(channelId.value))
            .requireSuccess(operation = "letBotJoinChannel(botId=${botId.value}, channelId=${channelId.value})")
    }

    override suspend fun leaveChannel(
        botId: BotId,
        channelId: ChannelId,
    ) {
        apiGateway.botApi
            .letBotLeaveChannel(botId.value, PostBotActionLeaveRequest(channelId.value))
            .requireSuccess(operation = "letBotLeaveChannel(botId=${botId.value}, channelId=${channelId.value})")
    }
}
