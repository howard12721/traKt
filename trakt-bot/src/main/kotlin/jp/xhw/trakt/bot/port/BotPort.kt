package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.BotId
import jp.xhw.trakt.bot.model.ChannelId

internal interface BotPort {
    /**
     * Bot をチャンネルへ参加させます。
     *
     * @param botId 操作対象Bot
     * @param channelId 参加先チャンネル
     */
    suspend fun joinChannel(
        botId: BotId,
        channelId: ChannelId,
    )

    /**
     * Bot をチャンネルから退出させます。
     *
     * @param botId 操作対象Bot
     * @param channelId 退出元チャンネル
     */
    suspend fun leaveChannel(
        botId: BotId,
        channelId: ChannelId,
    )
}
