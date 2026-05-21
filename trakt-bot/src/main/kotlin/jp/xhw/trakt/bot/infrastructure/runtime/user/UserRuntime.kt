package jp.xhw.trakt.bot.infrastructure.runtime.user

import jp.xhw.trakt.bot.context.user.UserContext
import jp.xhw.trakt.bot.infrastructure.LoggerFactory
import jp.xhw.trakt.bot.infrastructure.gateway.*
import jp.xhw.trakt.bot.infrastructure.runtime.RuleRegistry
import jp.xhw.trakt.bot.infrastructure.runtime.RuntimeBuilder
import jp.xhw.trakt.bot.infrastructure.runtime.RuntimeLifecycle
import jp.xhw.trakt.bot.infrastructure.runtime.SelfTraktClientBuilder
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import jp.xhw.trakt.websocket.user.UserEvent as WebSocketUserEvent

/** User token 用の context、Port、イベントソース、lifecycle を組み立てます。 */
internal fun createUserClient(
    token: String,
    origin: String,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    debugMode: Boolean = false,
): SelfTraktClientBuilder {
    val apiGateway = TraqApiGateway(token = token, origin = origin, debugMode = debugMode)
    val selfPort = TraqSelfPort(apiGateway)
    val ctx =
        UserContext(
            origin = origin,
            channelPort = TraqChannelPort(apiGateway),
            messagePort = TraqMessagePort(apiGateway),
            userPort = TraqUserPort(apiGateway),
            stampPort = TraqStampPort(apiGateway),
            groupPort = TraqGroupPort(apiGateway),
            filePort = TraqFilePort(apiGateway),
            selfPort = selfPort,
            clipPort = TraqClipPort(apiGateway),
            webhookPort = TraqWebhookPort(apiGateway),
            managedBotPort = TraqManagedBotPort(apiGateway),
            userWebSocketPort = TraqUserWebSocketPort(apiGateway),
        )
    val logger = LoggerFactory.getLogger("jp.xhw.trakt.bot.UserRuntime")
    val lifecycle =
        object : RuntimeLifecycle {
            override suspend fun start() {
                apiGateway.userWs.start()
            }

            override suspend fun awaitStarted() {
                apiGateway.userWs.awaitConnected()
                val currentUser = selfPort.fetchMe()
                ctx.currentUser = currentUser
                ctx.currentUsername = currentUser.name
                ctx.currentUserId = currentUser.id
                logger.info("Logged in as {} ({})", currentUser.name, currentUser.id.value)
            }

            override suspend fun stop() {
                apiGateway.userWs.stop()
                apiGateway.userWs.close()
                apiGateway.httpClient.close()
            }
        }

    return RuntimeBuilder(
        context = ctx,
        ruleRegistry = RuleRegistry(),
        eventSource = apiGateway.userWs.events,
        eventMapper = { event -> (event as? WebSocketUserEvent)?.toEventOrNull() },
        lifecycle = lifecycle,
        coroutineContext = coroutineContext,
    )
}
