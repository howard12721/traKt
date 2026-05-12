package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toCurrentUser
import jp.xhw.trakt.bot.model.CurrentUser
import jp.xhw.trakt.bot.port.BotSelfPort

internal class TraqBotSelfPort(
    private val apiGateway: TraqApiGateway,
) : BotSelfPort {
    override suspend fun fetchMe(): CurrentUser =
        apiGateway.meApi
            .getMe()
            .bodyOrThrow(operation = "botFetchMe()")
            .toCurrentUser()
}
