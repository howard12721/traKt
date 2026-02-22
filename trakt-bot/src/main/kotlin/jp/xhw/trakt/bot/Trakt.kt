package jp.xhw.trakt.bot

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid

fun trakt(
    token: String,
    botId: Uuid,
    origin: String = "q.trap.jp",
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: TraktClient.() -> Unit = {},
): TraktClient =
    TraktClient(
        token = token,
        botId = botId,
        origin = origin,
        coroutineContext = coroutineContext,
    ).apply(block)
