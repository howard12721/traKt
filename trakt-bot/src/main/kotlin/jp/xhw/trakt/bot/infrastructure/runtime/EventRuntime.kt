package jp.xhw.trakt.bot.infrastructure.runtime

import jp.xhw.trakt.bot.context.ClientContext
import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.context.user.UserContext
import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.infrastructure.LoggerFactory
import jp.xhw.trakt.bot.infrastructure.client.BaseRuntime
import jp.xhw.trakt.bot.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.TimeSource

typealias TraktBot = EventRuntime<BotContext, BotEvent>

typealias TraktSelfBot = EventRuntime<UserContext, UserEvent>

@TraktDsl
class EventRuntime<C : ClientContext, E : Any> internal constructor(
    private val base: BaseRuntime<C, E>,
    private val dispatcher: EventDispatcher<C, E>,
    private val eventSource: Flow<*>,
    private val eventMapper: (Any?) -> E?,
    private val lifecycle: Lifecycle,
    scheduledTasks: List<ScheduledTask<C>>,
) {
    internal val context: C get() = base.context

    private val lifecycleEvents =
        MutableSharedFlow<Event>(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.SUSPEND,
        )
    private val logger = LoggerFactory.getLogger("jp.xhw.trakt.bot.infrastructure.runtime.EventRuntime")
    private val scheduledTasks = scheduledTasks.toList()

    private var eventSubscription: Job? = null
    private var scheduledTaskJobs = emptyList<Job>()
    private var started = false
    private val stopMutex = Mutex()

    suspend fun execute(block: suspend C.() -> Unit) = base.execute(block)

    fun launchAndExecute(block: suspend C.() -> Unit): Job = base.launchAndExecute(block)

    suspend fun start() {
        base.ensureOpen()
        check(!started) { "Client is already started" }
        started = true
        eventSubscription =
            dispatcher.subscribe(
                merge(
                    eventSource.mapNotNull { raw ->
                        try {
                            eventMapper(raw)
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            logger.warn("Failed to map websocket event, skipping: {}", e.message)
                            null
                        }
                    },
                    lifecycleEvents.mapNotNull(::toEvent),
                ),
                base.scope,
            )
        val lifecycleJob =
            base.scope.launch {
                lifecycle.start()
            }
        lifecycle.awaitStarted()
        lifecycleEvents.emit(Initialized(occurredAt = Clock.System.now()))
        scheduledTaskJobs = startScheduledTasks()
        lifecycleJob.join()
    }

    suspend fun run() {
        try {
            start()
        } finally {
            stop()
        }
    }

    suspend fun stop() {
        withContext(NonCancellable) {
            stopMutex.withLock {
                if (base.isClosed) {
                    return@withLock
                }

                val wasStarted = started
                started = false
                scheduledTaskJobs.forEach { it.cancel() }
                scheduledTaskJobs = emptyList()

                try {
                    if (wasStarted) {
                        lifecycle.stop()
                        lifecycleEvents.emit(Disposed(occurredAt = Clock.System.now()))
                    }
                } finally {
                    eventSubscription?.cancel()
                    eventSubscription = null
                    base.close()
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun toEvent(event: Event): E? = event as? E

    private fun startScheduledTasks(): List<Job> =
        scheduledTasks.map { task ->
            base.scope.launch {
                delay(task.initialDelay)
                var nextStart = TimeSource.Monotonic.markNow()
                while (isActive) {
                    launchScheduledTask(task)
                    nextStart += task.interval
                    val delayUntilNextStart = -nextStart.elapsedNow()
                    if (delayUntilNextStart > Duration.ZERO) {
                        delay(delayUntilNextStart)
                    } else {
                        yield()
                    }
                }
            }
        }

    private fun launchScheduledTask(task: ScheduledTask<C>): Job =
        base.scope.launch {
            try {
                task.block(context)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logger.error("Error while running scheduled task", e)
            }
        }
}
