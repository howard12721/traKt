package jp.xhw.trakt.bot

import jp.xhw.trakt.bot.event.Event
import jp.xhw.trakt.bot.gateway.TraqApiGateway
import jp.xhw.trakt.bot.gateway.TraqChannelPort
import jp.xhw.trakt.bot.gateway.TraqMessagePort
import jp.xhw.trakt.bot.gateway.TraqUserPort
import jp.xhw.trakt.bot.port.BotRuntimeContext
import jp.xhw.trakt.bot.runtime.RuleRegistry
import jp.xhw.trakt.bot.scope.BotScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

class TraktClient(
    token: String,
    origin: String = "q.trap.jp",
) : AutoCloseable {
    private val apiGateway = TraqApiGateway(token = token, origin = origin)
    private val ruleRegistry = RuleRegistry()
    private val runtimeScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val messagePort = TraqMessagePort(apiGateway)

    private val context =
        BotRuntimeContext(
            channelPort = TraqChannelPort(apiGateway),
            messagePort = messagePort,
            userPort = TraqUserPort(apiGateway, messagePort),
        )
    val bot: BotScope = BotScope(context)

    private var subscriptions: List<Job> = emptyList()

    fun <T : Event> on(
        eventClass: KClass<T>,
        handler: suspend BotScope.(T) -> Unit,
    ) {
        check(subscriptions.isEmpty()) { "Handlers must be registered before start()" }
        ruleRegistry.on(eventClass, bot, handler)
    }

    suspend fun start() {
        check(subscriptions.isEmpty()) { "Client is already started" }
        subscriptions = ruleRegistry.install(apiGateway.ws, runtimeScope)
        apiGateway.ws.start()
    }

    suspend fun stop() {
        subscriptions.forEach(Job::cancel)
        subscriptions = emptyList()
        apiGateway.ws.stop()
        runtimeScope.coroutineContext.cancelChildren()
    }

    override fun close() {
        var failure: Throwable? = null
        try {
            runBlocking {
                stop()
            }
        } catch (e: Throwable) {
            failure = e
        }

        try {
            apiGateway.ws.close()
        } catch (e: Throwable) {
            if (failure == null) {
                failure = e
            } else {
                failure.addSuppressed(e)
            }
        }

        try {
            apiGateway.httpClient.close()
        } catch (e: Throwable) {
            if (failure == null) {
                failure = e
            } else {
                failure.addSuppressed(e)
            }
        }

        if (failure != null) {
            throw failure
        }
    }
}
