package jp.xhw.trakt.bot.context.bot

import jp.xhw.trakt.bot.model.ChannelHandle

/** Bot をチャンネルに参加させます。 */
context(ctx: BotContext)
suspend fun ChannelHandle.join() = ctx.botPort.joinChannel(ctx.requireBotId("channel.join"), id)

/** Bot をチャンネルから退出させます。 */
context(ctx: BotContext)
suspend fun ChannelHandle.leave() = ctx.botPort.leaveChannel(ctx.requireBotId("channel.leave"), id)

private fun BotContext.requireBotId(actionName: String) =
    requireNotNull(botId) {
        "`$actionName` requires `botId`. Pass `botId` to trakt(...) to use bot-specific actions."
    }
