package jp.xhw.trakt.bot.infrastructure.runtime.user

import jp.xhw.trakt.bot.context.user.UserContext
import jp.xhw.trakt.bot.infrastructure.gateway.*
import jp.xhw.trakt.bot.infrastructure.runtime.RuleRegistry
import jp.xhw.trakt.bot.infrastructure.runtime.Runtime
import jp.xhw.trakt.bot.infrastructure.runtime.RuntimeLifecycle
import jp.xhw.trakt.bot.infrastructure.runtime.SelfTraktClient
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import jp.xhw.trakt.websocket.user.UserEvent as WebSocketUserEvent

/** User token 用の context、Port、イベントソース、lifecycle を組み立てます。 */
internal fun createUserClient(
    token: String,
    origin: String,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    debugMode: Boolean = false,
): SelfTraktClient {
    val apiGateway = TraqApiGateway(token = token, origin = origin, debugMode = debugMode)
    val ctx =
        UserContext(
            origin = origin,
            channelPort = TraqChannelPort(apiGateway),
            messagePort = TraqMessagePort(apiGateway),
            userPort = TraqUserPort(apiGateway),
            stampPort = TraqStampPort(apiGateway),
            groupPort = TraqGroupPort(apiGateway),
            filePort = TraqFilePort(apiGateway),
            selfPort = TraqSelfPort(apiGateway),
            clipPort = TraqClipPort(apiGateway),
            webhookPort = TraqWebhookPort(apiGateway),
            managedBotPort = TraqManagedBotPort(apiGateway),
            userWebSocketPort = TraqUserWebSocketPort(apiGateway),
        )
    val lifecycle =
        object : RuntimeLifecycle {
            override suspend fun start() {
                apiGateway.userWs.start()
            }

            override suspend fun awaitStarted() {
                apiGateway.userWs.awaitConnected()
            }

            override suspend fun stop() {
                apiGateway.userWs.stop()
                apiGateway.userWs.close()
                apiGateway.httpClient.close()
            }
        }

    return Runtime(
        context = ctx,
        ruleRegistry = RuleRegistry(),
        eventSource = apiGateway.userWs.events,
        eventMapper = { event -> (event as? WebSocketUserEvent)?.toEventOrNull() },
        lifecycle = lifecycle,
        coroutineContext = coroutineContext,
    )
}
