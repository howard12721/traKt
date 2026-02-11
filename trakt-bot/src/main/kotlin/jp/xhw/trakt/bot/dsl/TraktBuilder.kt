package jp.xhw.trakt.bot.dsl

import jp.xhw.trakt.bot.TraktClient
import jp.xhw.trakt.bot.event.Event
import jp.xhw.trakt.bot.scope.BotScope

@TraktDsl
class TraktBuilder internal constructor(
    @PublishedApi internal val client: TraktClient,
) {
    inline fun <reified T : Event> on(noinline handler: suspend BotScope.(T) -> Unit) {
        client.on(T::class, handler)
    }
}
