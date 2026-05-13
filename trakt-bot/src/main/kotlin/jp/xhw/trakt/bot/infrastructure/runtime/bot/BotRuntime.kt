package jp.xhw.trakt.bot.infrastructure.runtime.bot

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.infrastructure.gateway.*
import jp.xhw.trakt.bot.infrastructure.runtime.RuleRegistry
import jp.xhw.trakt.bot.infrastructure.runtime.RuntimeBuilder
import jp.xhw.trakt.bot.infrastructure.runtime.RuntimeLifecycle
import jp.xhw.trakt.bot.infrastructure.runtime.TraktClientBuilder
import jp.xhw.trakt.bot.model.BotId
import jp.xhw.trakt.websocket.bot.BotEvent
import kotlinx.coroutines.Dispatchers
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid

/** Bot token 用の context、Port、イベントソース、lifecycle を組み立てます。 */
internal fun createBotClient(
    token: String,
    botId: Uuid?,
    origin: String,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    debugMode: Boolean = false,
): TraktClientBuilder {
    val apiGateway = TraqApiGateway(token = token, origin = origin, debugMode = debugMode)
    val selfPort = TraqBotSelfPort(apiGateway)
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
            selfPort = selfPort,
        )
    val logger = LoggerFactory.getLogger("jp.xhw.trakt.bot.BotRuntime")
    val lifecycle =
        object : RuntimeLifecycle {
            override suspend fun start() {
                apiGateway.botWs.start()
            }

            override suspend fun awaitStarted() {
                apiGateway.botWs.awaitConnected()
                val currentUser = selfPort.fetchMe()
                ctx.currentUser = currentUser
                ctx.currentUsername = currentUser.name
                ctx.currentUserId = currentUser.id
                logger.info("Logged in as {} ({})", currentUser.name, currentUser.id.value)
            }

            override suspend fun stop() {
                apiGateway.botWs.stop()
                apiGateway.botWs.close()
                apiGateway.httpClient.close()
            }
        }

    return RuntimeBuilder(
        context = ctx,
        ruleRegistry = RuleRegistry(),
        eventSource = apiGateway.botWs.events,
        eventMapper = { event -> (event as? BotEvent)?.toEventOrNull() },
        lifecycle = lifecycle,
        coroutineContext = coroutineContext,
    )
}
