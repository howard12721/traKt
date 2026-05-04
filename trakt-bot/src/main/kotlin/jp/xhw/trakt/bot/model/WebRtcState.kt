package jp.xhw.trakt.bot.model

/** WebSocket に通知する WebRTC セッション状態。 */
data class WebRtcState(
    val state: String,
    val sessionId: String,
) {
    init {
        require(state.isNotBlank()) { "WebRTC state must not be blank" }
        require(sessionId.isNotBlank()) { "WebRTC sessionId must not be blank" }
        require(':' !in state) { "WebRTC state must not contain ':'" }
        require(':' !in sessionId) { "WebRTC sessionId must not contain ':'" }
    }
}
