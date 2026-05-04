package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.ChannelViewState
import jp.xhw.trakt.bot.model.WebRtcState

internal interface UserWebSocketPort {
    suspend fun setViewState(
        channelId: ChannelId,
        state: ChannelViewState,
    )

    suspend fun clearViewState()

    suspend fun setWebRtcState(
        channelId: ChannelId,
        states: List<WebRtcState>,
    )

    suspend fun clearWebRtcState()

    suspend fun setTimelineStreaming(enabled: Boolean)

    suspend fun sendRawCommand(command: String)
}
