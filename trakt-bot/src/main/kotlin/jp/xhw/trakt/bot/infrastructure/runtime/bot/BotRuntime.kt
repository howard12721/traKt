package jp.xhw.trakt.bot.infrastructure.runtime.bot

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.infrastructure.gateway.*
import jp.xhw.trakt.bot.infrastructure.runtime.RuleRegistry
import jp.xhw.trakt.bot.infrastructure.runtime.Runtime
import jp.xhw.trakt.bot.infrastructure.runtime.RuntimeLifecycle
import jp.xhw.trakt.bot.infrastructure.runtime.TraktClient
import jp.xhw.trakt.bot.model.BotId
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid
import jp.xhw.trakt.websocket.Event as WebSocketEvent

/** Bot token 用の context、Port、イベントソース、lifecycle を組み立てます。 */
internal fun createBotClient(
    token: String,
    botId: Uuid?,
    origin: String,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    debugMode: Boolean = false,
): TraktClient {
    val apiGateway = TraqApiGateway(token = token, origin = origin, debugMode = debugMode)
    val ctx =
        BotContext(
            botId = botId?.let(::BotId),
            origin = origin,
            channelPort = TraqChannelPort(apiGateway),
            messagePort = TraqMessagePort(apiGateway),
            userPort = TraqUserPort(apiGateway),
            stampPort = TraqStampPort(apiGateway),
            groupPort = TraqGroupPort(apiGateway),
            filePort = TraqFilePort(apiGateway),
            botPort = TraqBotPort(apiGateway),
        )
    val lifecycle =
        object : RuntimeLifecycle {
            override suspend fun start() {
                apiGateway.ws.start()
            }

            override suspend fun stop() {
                apiGateway.ws.stop()
                apiGateway.ws.close()
                apiGateway.httpClient.close()
            }
        }

    return Runtime(
        context = ctx,
        ruleRegistry =
            RuleRegistry(
                eventMapper = { event -> (event as? WebSocketEvent)?.toEventOrNull() },
            ),
        eventSource = apiGateway.ws.events,
        lifecycle = lifecycle,
        coroutineContext = coroutineContext,
    )
}
