package jp.xhw.trakt.bot.port

internal class BotRuntimeContext(
    val origin: String,
    val channelPort: ChannelPort,
    val messagePort: MessagePort,
    val userPort: UserPort,
    val stampPort: StampPort,
    val groupPort: GroupPort,
    val filePort: FilePort,
)
