package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.ChannelViewState
import jp.xhw.trakt.bot.model.WebRtcState
import jp.xhw.trakt.bot.port.UserWebSocketPort

internal class TraqUserWebSocketPort(
    private val apiGateway: TraqApiGateway,
) : UserWebSocketPort {
    override suspend fun setViewState(
        channelId: ChannelId,
        state: ChannelViewState,
    ) {
        apiGateway.userWs.sendCommand("viewstate:${channelId.value}:${state.toWsCommandValue()}")
    }

    override suspend fun clearViewState() {
        apiGateway.userWs.sendCommand("viewstate:null")
    }

    override suspend fun setWebRtcState(
        channelId: ChannelId,
        states: List<WebRtcState>,
    ) {
        require(states.isNotEmpty()) { "WebRTC states must not be empty. Use clearWebRtcState() to reset." }
        val command =
            buildString {
                append("rtcstate:")
                append(channelId.value)
                states.forEach { state ->
                    append(':')
                    append(state.state)
                    append(':')
                    append(state.sessionId)
                }
            }
        apiGateway.userWs.sendCommand(command)
    }

    override suspend fun clearWebRtcState() {
        apiGateway.userWs.sendCommand("rtcstate:null")
    }

    override suspend fun setTimelineStreaming(enabled: Boolean) {
        apiGateway.userWs.sendCommand("timeline_streaming:${if (enabled) "on" else "off"}")
    }

    override suspend fun sendRawCommand(command: String) {
        require(command.isNotBlank()) { "WebSocket command must not be blank" }
        apiGateway.userWs.sendCommand(command)
    }
}

private fun ChannelViewState.toWsCommandValue(): String =
    when (this) {
        ChannelViewState.NONE -> {
            "none"
        }

        ChannelViewState.MONITORING -> {
            "monitoring"
        }

        ChannelViewState.EDITING -> {
            "editing"
        }

        ChannelViewState.STALE_VIEWING -> {
            "stale_viewing"
        }
    }
