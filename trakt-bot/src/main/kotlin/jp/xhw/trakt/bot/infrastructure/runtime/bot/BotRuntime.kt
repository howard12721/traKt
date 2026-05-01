package jp.xhw.trakt.bot.infrastructure.runtime.bot

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.infrastructure.gateway.*
import jp.xhw.trakt.bot.infrastructure.runtime.RuleRegistry
import jp.xhw.trakt.bot.infrastructure.runtime.Runtime
import jp.xhw.trakt.bot.infrastructure.runtime.RuntimeLifecycle
import jp.xhw.trakt.bot.infrastructure.runtime.TraktClient
import jp.xhw.trakt.bot.model.BotId
import jp.xhw.trakt.websocket.bot.BotEvent
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid

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
                apiGateway.botWs.start()
            }

            override suspend fun stop() {
                apiGateway.botWs.stop()
                apiGateway.botWs.close()
                apiGateway.httpClient.close()
            }
        }

    return Runtime(
        context = ctx,
        ruleRegistry =
            RuleRegistry(
                eventMapper = { event -> (event as? BotEvent)?.toEventOrNull() },
            ),
        eventSource = apiGateway.botWs.events,
        lifecycle = lifecycle,
        coroutineContext = coroutineContext,
    )
}
