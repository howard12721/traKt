package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.*

internal interface ManagedBotPort {
    suspend fun createBot(
        name: String,
        displayName: String,
        description: String,
        mode: ManagedBotMode,
        endpoint: String? = null,
    ): ManagedBot.Detail

    suspend fun fetchBots(all: Boolean = false): List<ManagedBot.Basic>

    suspend fun fetchBot(
        botId: BotId,
        detail: Boolean = false,
    ): ManagedBot

    suspend fun editBot(
        botId: BotId,
        displayName: String? = null,
        description: String? = null,
        privileged: Boolean? = null,
        mode: ManagedBotMode? = null,
        endpoint: String? = null,
        developerId: UserId? = null,
        subscribeEvents: List<String>? = null,
        bio: String? = null,
    )

    suspend fun deleteBot(botId: BotId)

    suspend fun activateBot(botId: BotId)

    suspend fun inactivateBot(botId: BotId)

    suspend fun reissueBot(botId: BotId): BotTokens

    suspend fun fetchBotLogs(
        botId: BotId,
        limit: Int? = null,
        offset: Int = 0,
    ): List<BotEventLog>

    suspend fun joinChannel(
        botId: BotId,
        channelId: ChannelId,
    )

    suspend fun leaveChannel(
        botId: BotId,
        channelId: ChannelId,
    )

    suspend fun downloadBotIcon(botId: BotId): ByteArray

    suspend fun changeBotIcon(
        botId: BotId,
        file: ByteArray,
        fileName: String,
        contentType: String? = null,
    )
}
