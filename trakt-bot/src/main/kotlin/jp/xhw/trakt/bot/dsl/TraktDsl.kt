package jp.xhw.trakt.bot.dsl

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.context.user.UserContext
import jp.xhw.trakt.bot.infrastructure.runtime.Runtime
import jp.xhw.trakt.bot.model.BotEvent
import jp.xhw.trakt.bot.model.UserEvent

@DslMarker
annotation class TraktDsl

typealias BotRuntime = Runtime<BotContext, BotEvent>

typealias UserRuntime = Runtime<UserContext, UserEvent>
