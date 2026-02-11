package jp.xhw.trakt.bot.port

internal class BotRuntimeContext(
    val channelPort: ChannelPort,
    val messagePort: MessagePort,
    val userPort: UserPort,
)
