package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toCurrentUser
import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.CurrentUser
import jp.xhw.trakt.bot.port.BotSelfPort
import jp.xhw.trakt.rest.models.PatchMeRequest

internal class TraqBotSelfPort(
    private val apiGateway: TraqApiGateway,
) : BotSelfPort {
    override suspend fun fetchMe(): CurrentUser =
        apiGateway.meApi
            .getMe()
            .bodyOrThrow(operation = "botFetchMe()")
            .toCurrentUser()

    override suspend fun editMe(
        displayName: String?,
        twitterId: String?,
        bio: String?,
        homeChannelId: ChannelId?,
    ) {
        apiGateway.meApi
            .editMe(
                PatchMeRequest(
                    displayName = displayName,
                    twitterId = twitterId,
                    bio = bio,
                    homeChannel = homeChannelId?.value,
                ),
            ).requireSuccess(operation = "botEditMe()")
    }
}
