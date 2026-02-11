package jp.xhw.trakt.bot

import jp.xhw.trakt.bot.dsl.TraktBuilder
import jp.xhw.trakt.bot.event.Event
import jp.xhw.trakt.bot.scope.BotScope

fun trakt(
    token: String,
    origin: String = "q.trap.jp",
    block: TraktBuilder.() -> Unit = {},
): TraktClient {
    val client = TraktClient(token = token, origin = origin)
    TraktBuilder(client).block()
    return client
}

suspend fun runTrakt(
    token: String,
    origin: String = "q.trap.jp",
    block: TraktBuilder.() -> Unit,
) {
    TraktClient(token = token, origin = origin).use { client ->
        TraktBuilder(client).block()
        client.start()
    }
}

inline fun <reified T : Event> TraktClient.on(noinline handler: suspend BotScope.(T) -> Unit) {
    on(T::class, handler)
}
