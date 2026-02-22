package jp.xhw.trakt.bot

import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.infrastructure.gateway.*
import jp.xhw.trakt.bot.infrastructure.runtime.RuleRegistry
import jp.xhw.trakt.bot.model.Event
import jp.xhw.trakt.bot.port.BotRuntimeContext
import jp.xhw.trakt.bot.scope.BotScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

@TraktDsl
class TraktClient(
    token: String,
    origin: String = "q.trap.jp",
    coroutineContext: CoroutineContext = Dispatchers.Default,
) {
    private val apiGateway = TraqApiGateway(token = token, origin = origin)
    private val ruleRegistry = RuleRegistry()
    private val supervisorJob = SupervisorJob()
    private val runtimeScope = CoroutineScope(supervisorJob + coroutineContext)

    private val context =
        BotRuntimeContext(
            origin = origin,
            channelPort = TraqChannelPort(apiGateway),
            messagePort = TraqMessagePort(apiGateway),
            userPort = TraqUserPort(apiGateway),
            stampPort = TraqStampPort(apiGateway),
            groupPort = TraqGroupPort(apiGateway),
            filePort = TraqFilePort(apiGateway),
        )
    private val bot = BotScope(context)
    private var subscriptions: List<Job> = emptyList()

    inline fun <reified T : Event> on(noinline handler: suspend BotScope.(T) -> Unit) {
        on(T::class, handler)
    }

    @PublishedApi
    internal fun <T : Event> on(
        eventClass: KClass<T>,
        handler: suspend BotScope.(T) -> Unit,
    ) {
        check(subscriptions.isEmpty()) { "Handlers must be registered before start()" }
        ruleRegistry.on(eventClass, bot, handler)
    }

    suspend fun execute(block: suspend BotScope.() -> Unit) {
        bot.block()
    }

    fun launchAndExecute(block: suspend BotScope.() -> Unit): Job =
        runtimeScope.launch {
            bot.block()
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
        apiGateway.ws.close()
        apiGateway.httpClient.close()
        supervisorJob.cancel()
    }
}
