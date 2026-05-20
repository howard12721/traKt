package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toApiModel
import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toManagedModel
import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.bot.port.ManagedBotPort
import jp.xhw.trakt.rest.models.PatchBotRequest
import jp.xhw.trakt.rest.models.PostBotActionJoinRequest
import jp.xhw.trakt.rest.models.PostBotActionLeaveRequest
import jp.xhw.trakt.rest.models.PostBotRequest

internal class TraqManagedBotPort(
    private val apiGateway: TraqApiGateway,
) : ManagedBotPort {
    override suspend fun createBot(
        name: String,
        displayName: String,
        description: String,
        mode: ManagedBotMode,
        endpoint: String?,
    ): ManagedBot.Detail =
        apiGateway.botApi
            .createBot(
                PostBotRequest(
                    name = name,
                    displayName = displayName,
                    description = description,
                    mode = mode.toApiModel(),
                    endpoint = endpoint,
                ),
            ).bodyOrThrow(operation = "createBot(name=$name)")
            .toManagedModel()

    override suspend fun fetchBots(all: Boolean): List<ManagedBot.Basic> =
        apiGateway.botApi
            .getBots(all)
            .bodyOrThrow(operation = "fetchManagedBots(all=$all)")
            .map { it.toManagedModel() }

    override suspend fun fetchBot(
        botId: BotId,
        detail: Boolean,
    ): ManagedBot.WithMeta =
        apiGateway.botApi
            .getBot(botId.value, detail)
            .bodyOrThrow(operation = "fetchManagedBot(botId=${botId.value}, detail=$detail)")
            .toManagedModel()

    override suspend fun editBot(
        botId: BotId,
        displayName: String?,
        description: String?,
        privileged: Boolean?,
        mode: ManagedBotMode?,
        endpoint: String?,
        developerId: UserId?,
        subscribeEvents: List<String>?,
        bio: String?,
    ) {
        apiGateway.botApi
            .editBot(
                botId = botId.value,
                patchBotRequest =
                    PatchBotRequest(
                        displayName = displayName,
                        description = description,
                        privileged = privileged,
                        mode = mode?.toApiModel(),
                        endpoint = endpoint,
                        developerId = developerId?.value,
                        subscribeEvents = subscribeEvents,
                        bio = bio,
                    ),
            ).requireSuccess(operation = "editBot(botId=${botId.value})")
    }

    override suspend fun deleteBot(botId: BotId) {
        apiGateway.botApi
            .deleteBot(botId.value)
            .requireSuccess(operation = "deleteBot(botId=${botId.value})")
    }

    override suspend fun activateBot(botId: BotId) {
        apiGateway.botApi
            .activateBot(botId.value)
            .requireSuccess(operation = "activateBot(botId=${botId.value})")
    }

    override suspend fun inactivateBot(botId: BotId) {
        apiGateway.botApi
            .inactivateBot(botId.value)
            .requireSuccess(operation = "inactivateBot(botId=${botId.value})")
    }

    override suspend fun reissueBot(botId: BotId): BotTokens =
        apiGateway.botApi
            .reissueBot(botId.value)
            .bodyOrThrow(operation = "reissueBot(botId=${botId.value})")
            .toModel()

    override suspend fun fetchBotLogs(
        botId: BotId,
        limit: Int?,
        offset: Int,
    ): List<BotEventLog> =
        apiGateway.botApi
            .getBotLogs(
                botId = botId.value,
                limit = limit,
                offset = offset,
            ).bodyOrThrow(operation = "fetchBotLogs(botId=${botId.value})")
            .map { it.toModel() }

    override suspend fun joinChannel(
        botId: BotId,
        channelId: ChannelId,
    ) {
        apiGateway.botApi
            .letBotJoinChannel(botId.value, PostBotActionJoinRequest(channelId.value))
            .requireSuccess(operation = "managedBotJoinChannel(botId=${botId.value}, channelId=${channelId.value})")
    }

    override suspend fun leaveChannel(
        botId: BotId,
        channelId: ChannelId,
    ) {
        apiGateway.botApi
            .letBotLeaveChannel(botId.value, PostBotActionLeaveRequest(channelId.value))
            .requireSuccess(operation = "managedBotLeaveChannel(botId=${botId.value}, channelId=${channelId.value})")
    }

    override suspend fun downloadBotIcon(botId: BotId): ByteArray =
        apiGateway.botApi
            .getBotIcon(botId.value)
            .bodyOrThrow(operation = "downloadBotIcon(botId=${botId.value})")
            .value

    override suspend fun changeBotIcon(
        botId: BotId,
        file: ByteArray,
        fileName: String,
        contentType: String?,
    ) {
        apiGateway.botApi
            .changeBotIcon(botId.value, file.toFilePart(fileName, contentType))
            .requireSuccess(operation = "changeBotIcon(botId=${botId.value}, fileName=$fileName)")
    }
}
