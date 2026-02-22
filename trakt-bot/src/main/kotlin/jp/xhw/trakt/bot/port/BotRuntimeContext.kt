package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.BotId

internal class BotRuntimeContext(
    val botId: BotId,
    val origin: String,
    val channelPort: ChannelPort,
    val messagePort: MessagePort,
    val userPort: UserPort,
    val stampPort: StampPort,
    val groupPort: GroupPort,
    val filePort: FilePort,
    val botPort: BotPort,
)
