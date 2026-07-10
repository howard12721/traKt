package jp.xhw.trakt.bot.infrastructure.runtime.bot

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.infrastructure.LoggerFactory
import jp.xhw.trakt.bot.infrastructure.client.BaseRuntime
import jp.xhw.trakt.bot.infrastructure.gateway.*
import jp.xhw.trakt.bot.infrastructure.runtime.Lifecycle
import jp.xhw.trakt.bot.model.BotEvent
import jp.xhw.trakt.bot.model.BotId
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid
import jp.xhw.trakt.websocket.bot.BotEvent as WsBotEvent

internal fun createBotClient(
    token: String,
    botId: Uuid?,
    origin: String,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    debugMode: Boolean = false,
): BaseRuntime<BotContext, BotEvent> {
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
        object : Lifecycle {
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
            }
        }

    return BaseRuntime(
        context = ctx,
        coroutineContext = coroutineContext,
        eventSource = apiGateway.botWs.events,
        eventMapper = { event -> (event as? WsBotEvent)?.toEventOrNull() },
        lifecycle = lifecycle,
        onClose = apiGateway::close,
    )
}
