package jp.xhw.trakt.bot.context.bot

import jp.xhw.trakt.bot.model.Channel
import jp.xhw.trakt.bot.model.ChannelId

/** Bot をチャンネルに参加させます。 */
context(ctx: BotContext)
suspend fun ChannelId.join() = ctx.botPort.joinChannel(ctx.requireBotId("channel.join"), this)

/** Bot をチャンネルに参加させます。 */
context(ctx: BotContext)
suspend fun Channel.PublicChannel.join() = id.join()

/** Bot をチャンネルから退出させます。 */
context(ctx: BotContext)
suspend fun ChannelId.leave() = ctx.botPort.leaveChannel(ctx.requireBotId("channel.leave"), this)

/** Bot をチャンネルから退出させます。 */
context(ctx: BotContext)
suspend fun Channel.PublicChannel.leave() = id.leave()

private fun BotContext.requireBotId(actionName: String) =
    requireNotNull(botId) {
        "`$actionName` requires `botId`. Pass `botId` to trakt(...) to use bot-specific actions."
    }
