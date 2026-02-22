package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.ChannelHandle

context(scope: BotScope)
suspend fun ChannelHandle.join() = scope.context.botPort.joinChannel(scope.context.botId, id)

context(scope: BotScope)
suspend fun ChannelHandle.leave() = scope.context.botPort.leaveChannel(scope.context.botId, id)
