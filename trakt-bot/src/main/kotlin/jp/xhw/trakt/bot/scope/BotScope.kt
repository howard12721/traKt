package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.port.BotRuntimeContext

@TraktDsl
class BotScope internal constructor(
    internal val context: BotRuntimeContext,
)
