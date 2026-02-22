package jp.xhw.trakt.bot

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

fun trakt(
    token: String,
    origin: String = "q.trap.jp",
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: TraktClient.() -> Unit = {},
): TraktClient =
    TraktClient(
        token = token,
        origin = origin,
        coroutineContext = coroutineContext,
    ).apply(block)
