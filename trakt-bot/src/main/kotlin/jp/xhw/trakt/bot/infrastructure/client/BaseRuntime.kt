@file:OptIn(kotlin.concurrent.atomics.ExperimentalAtomicApi::class)

package jp.xhw.trakt.bot.infrastructure.client

import jp.xhw.trakt.bot.context.ClientContext
import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.context.user.UserContext
import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.infrastructure.runtime.*
import jp.xhw.trakt.bot.model.BotEvent
import jp.xhw.trakt.bot.model.UserEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

typealias TraktClient = BaseRuntime<BotContext, BotEvent>

typealias TraktSelfClient = BaseRuntime<UserContext, UserEvent>

typealias BotEventRegistrar = EventRegistrar<BotContext, BotEvent>

typealias SelfEventRegistrar = EventRegistrar<UserContext, UserEvent>

@TraktDsl
class BaseRuntime<C : ClientContext, E : Any> internal constructor(
    internal val context: C,
    coroutineContext: CoroutineContext,
    internal val eventSource: Flow<*>? = null,
    internal val eventMapper: ((Any?) -> E?)? = null,
    internal val lifecycle: Lifecycle? = null,
    private val onClose: (() -> Unit)? = null,
) : AutoCloseable {
    internal val supervisorJob = SupervisorJob()
    internal val scope = CoroutineScope(supervisorJob + coroutineContext)
    private var runtimeInvoked = false
    private val closed = AtomicBoolean(false)
    internal val isClosed: Boolean get() = closed.load()

    suspend fun execute(block: suspend C.() -> Unit) {
        ensureOpen()
        context.block()
    }

    fun launchAndExecute(block: suspend C.() -> Unit): Job {
        ensureOpen()
        return scope.launch {
            context.block()
        }
    }

    /** このクライアントが所有するコルーチンと通信リソースを解放します。 */
    override fun close() {
        if (!closed.compareAndSet(expectedValue = false, newValue = true)) {
            return
        }

        supervisorJob.cancel()
        onClose?.invoke()
    }

    internal fun buildEventRuntime(block: EventRegistrar<C, E>.() -> Unit): EventRuntime<C, E> {
        ensureOpen()
        check(!runtimeInvoked) { "runtime() can only be called once" }
        check(eventSource != null && eventMapper != null && lifecycle != null) {
            "Event infrastructure is not available on this client"
        }

        val dispatcher = EventDispatcher<C, E>()
        val registrar = EventRegistrar(context, dispatcher)
        registrar.block()

        runtimeInvoked = true

        return EventRuntime(
            base = this,
            dispatcher = dispatcher,
            eventSource = eventSource,
            eventMapper = eventMapper,
            lifecycle = lifecycle,
            scheduledTasks = registrar.scheduledTasks,
        )
    }

    internal fun ensureOpen() {
        check(!isClosed) { "Client is closed" }
    }
}

@JvmName("botRuntime")
fun TraktClient.runtime(block: BotEventRegistrar.() -> Unit): TraktBot = buildEventRuntime(block)

@JvmName("selfRuntime")
fun TraktSelfClient.runtime(block: SelfEventRegistrar.() -> Unit): TraktSelfBot = buildEventRuntime(block)
