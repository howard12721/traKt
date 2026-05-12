package jp.xhw.trakt.bot.context.user

import jp.xhw.trakt.bot.model.Channel
import jp.xhw.trakt.bot.model.ChannelViewState
import jp.xhw.trakt.bot.model.WebRtcState

/** この WebSocket セッションが閲覧中のチャンネルと閲覧状態を設定します。 */
context(ctx: UserContext)
suspend fun Channel.setViewState(state: ChannelViewState) = ctx.userWebSocketPort.setViewState(id, state)

/** この WebSocket セッションの閲覧状態を解除します。 */
context(ctx: UserContext)
suspend fun clearViewState() = ctx.userWebSocketPort.clearViewState()

/** この WebSocket セッションの WebRTC 状態を設定します。 */
context(ctx: UserContext)
suspend fun Channel.setWebRtcState(vararg states: WebRtcState) = ctx.userWebSocketPort.setWebRtcState(id, states.toList())

/** この WebSocket セッションの WebRTC 状態を解除します。 */
context(ctx: UserContext)
suspend fun clearWebRtcState() = ctx.userWebSocketPort.clearWebRtcState()

/** 全パブリックチャンネルの `MESSAGE_CREATED` イベント受信を設定します。 */
context(ctx: UserContext)
suspend fun setTimelineStreaming(enabled: Boolean) = ctx.userWebSocketPort.setTimelineStreaming(enabled)

/** User WebSocket へ任意のコマンドを送信します。 */
context(ctx: UserContext)
suspend fun sendRawWebSocketCommand(command: String) = ctx.userWebSocketPort.sendRawCommand(command)
