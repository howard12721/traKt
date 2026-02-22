package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.BotId
import jp.xhw.trakt.bot.model.ChannelId

internal interface BotPort {
    suspend fun joinChannel(
        botId: BotId,
        channelId: ChannelId,
    )

    suspend fun leaveChannel(
        botId: BotId,
        channelId: ChannelId,
    )
}
