package jp.xhw.trakt.bot.context.base

import jp.xhw.trakt.bot.context.RuntimeContext
import jp.xhw.trakt.bot.port.ChannelPort
import jp.xhw.trakt.bot.port.FilePort
import jp.xhw.trakt.bot.port.GroupPort
import jp.xhw.trakt.bot.port.MessagePort
import jp.xhw.trakt.bot.port.StampPort
import jp.xhw.trakt.bot.port.UserPort

/**
 * Bot/User のどちらの context からも使える traQ 操作を束ねる基底 context。
 *
 * lifecycle は runtime が管理し、この class は handler から利用する Port だけを保持します。
 */
abstract class BaseContext internal constructor(
    internal val origin: String,
    internal val channelPort: ChannelPort,
    internal val messagePort: MessagePort,
    internal val userPort: UserPort,
    internal val stampPort: StampPort,
    internal val groupPort: GroupPort,
    internal val filePort: FilePort,
) : RuntimeContext
