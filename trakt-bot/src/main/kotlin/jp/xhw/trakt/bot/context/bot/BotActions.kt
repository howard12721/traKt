package jp.xhw.trakt.bot.context.bot

import jp.xhw.trakt.bot.model.ChannelId

/** Bot をチャンネルに参加させます。 */
context(ctx: BotContext)
suspend fun ChannelId.join() = ctx.botPort.joinChannel(ctx.requireBotId("channel.join"), this)

/** Bot をチャンネルから退出させます。 */
context(ctx: BotContext)
suspend fun ChannelId.leave() = ctx.botPort.leaveChannel(ctx.requireBotId("channel.leave"), this)

private fun BotContext.requireBotId(actionName: String) =
    requireNotNull(botId) {
        "`$actionName` requires `botId`. Pass `botId` to trakt(...) to use bot-specific actions."
    }
