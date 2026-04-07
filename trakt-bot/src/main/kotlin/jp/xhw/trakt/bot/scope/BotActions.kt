package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.ChannelHandle

/** Bot をチャンネルに参加させます。 */
context(scope: BotScope)
suspend fun ChannelHandle.join() = scope.context.botPort.joinChannel(scope.requireBotId("channel.join"), id)

/** Bot をチャンネルから退出させます。 */
context(scope: BotScope)
suspend fun ChannelHandle.leave() = scope.context.botPort.leaveChannel(scope.requireBotId("channel.leave"), id)

private fun BotScope.requireBotId(actionName: String) =
    requireNotNull(context.botId) {
        "`$actionName` requires `botId`. Pass `botId` to trakt(...) to use bot-specific actions."
    }
