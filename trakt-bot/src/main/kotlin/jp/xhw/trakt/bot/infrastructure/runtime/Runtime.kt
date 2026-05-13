package jp.xhw.trakt.bot.infrastructure.runtime

import jp.xhw.trakt.bot.context.RuntimeContext
import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.context.user.UserContext
import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.TimeSource

typealias TraktClient = Runtime<BotContext, BotEvent>

typealias SelfTraktClient = Runtime<UserContext, UserEvent>

typealias TraktClientBuilder = RuntimeBuilder<BotContext, BotEvent>

typealias SelfTraktClientBuilder = RuntimeBuilder<UserContext, UserEvent>

@TraktDsl
class RuntimeBuilder<R : RuntimeContext, E : Any> internal constructor(
    internal val context: R,
    private val ruleRegistry: RuleRegistry<R, E>,
    private val eventSource: Flow<*>,
    private val eventMapper: (Any?) -> E?,
    private val lifecycle: RuntimeLifecycle = RuntimeLifecycle.Noop,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
) {
    private val scheduledTasks = mutableListOf<ScheduledTask<R>>()
    private var built = false

    /**
     * 指定イベント型のハンドラを登録します。
     *
     * 例: `on<BotMessageCreated> { ... }`
     *
     * @param handler イベント受信時に実行するハンドラ
     */
    inline fun <reified T : E> on(noinline handler: suspend R.(T) -> Unit) {
        on(T::class, handler)
    }

    /**
     * 指定イベント型のハンドラを登録します。
     *
     * @param eventClass 受信対象イベント型
     * @param handler イベント受信時に実行するハンドラ
     */
    @PublishedApi
    internal fun <T : E> on(
        eventClass: KClass<T>,
        handler: suspend R.(T) -> Unit,
    ) {
        check(!built) { "Handlers must be registered before build()" }
        ruleRegistry.on(eventClass, context, handler)
    }

    /**
     * 一定間隔で実行するタスクを登録します。
     *
     * タスクは runtime の [Runtime.start] 後、lifecycle の準備ができてから実行されます。
     * [interval] は前回の開始から次回の開始までの間隔です。
     * 前回の実行が [interval] を超えた場合でも、次の実行は予定どおり開始されます。
     * 次回の実行時刻は予定時刻を基準に計算し、遅れている場合は待たずに次の実行へ進みます。
     * 例外が発生しても runtime は停止せず、次回以降の実行は継続します。
     *
     * 例: `every(1.minutes) { ... }`
     *
     * @param interval 実行開始の間隔。正の値を指定してください
     * @param initialDelay 初回実行までの待ち時間。0 以上を指定してください
     * @param block 定期実行する処理
     */
    fun every(
        interval: Duration,
        initialDelay: Duration = interval,
        block: suspend R.() -> Unit,
    ) {
        check(!built) { "Scheduled tasks must be registered before build()" }
        require(interval > Duration.ZERO) { "interval must be positive" }
        require(initialDelay >= Duration.ZERO) { "initialDelay must be zero or positive" }
        scheduledTasks += ScheduledTask(interval, initialDelay, block)
    }

    internal fun build(): Runtime<R, E> {
        check(!built) { "Client is already built" }
        built = true
        return Runtime(
            context = context,
            ruleRegistry = ruleRegistry,
            eventSource = eventSource,
            eventMapper = eventMapper,
            lifecycle = lifecycle,
            coroutineContext = coroutineContext,
            scheduledTasks = scheduledTasks.toList(),
        )
    }
}

/**
 * context、イベント購読、lifecycle を組み合わせて実行するクライアント。
 *
 * [RuntimeBuilder] で登録されたイベントハンドラを使い、[start] でイベント購読と lifecycle を開始します。
 *
 * @param coroutineContext イベント処理に使うコルーチンコンテキスト
 */
@TraktDsl
class Runtime<R : RuntimeContext, E : Any> internal constructor(
    internal val context: R,
    internal val ruleRegistry: RuleRegistry<R, E>,
    internal val eventSource: Flow<*>,
    private val eventMapper: (Any?) -> E?,
    private val lifecycle: RuntimeLifecycle = RuntimeLifecycle.Noop,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    scheduledTasks: List<ScheduledTask<R>> = emptyList(),
) {
    private val supervisorJob = SupervisorJob()
    private val runtimeScope = CoroutineScope(supervisorJob + coroutineContext)
    private val lifecycleEvents =
        MutableSharedFlow<Event>(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.SUSPEND,
        )
    private val logger = LoggerFactory.getLogger(Runtime::class.java)
    private val scheduledTasks = scheduledTasks.toList()

    private var eventSubscription: Job? = null
    private var scheduledTaskJobs = emptyList<Job>()
    private var started = false

    /**
     * 現在の context で単発処理を実行します。
     *
     * @param block 実行する処理
     */
    suspend fun execute(block: suspend R.() -> Unit) {
        context.block()
    }

    /**
     * 現在の context で非同期に単発処理を実行し、[Job] を返します。
     *
     * @param block 実行する処理
     * @return 実行中処理を表す [Job]
     */
    fun launchAndExecute(block: suspend R.() -> Unit): Job =
        runtimeScope.launch {
            context.block()
        }

    /**
     * lifecycle と登録済みイベントハンドラの購読を開始します。
     */
    suspend fun start() {
        check(!started) { "Client is already started" }
        started = true
        eventSubscription =
            ruleRegistry.subscribe(
                merge(
                    eventSource.mapNotNull(eventMapper),
                    lifecycleEvents.mapNotNull(::toRuntimeEvent),
                ),
                runtimeScope,
            )
        val lifecycleJob =
            runtimeScope.launch {
                lifecycle.start()
            }
        lifecycle.awaitStarted()
        lifecycleEvents.emit(Initialized(occurredAt = Clock.System.now()))
        scheduledTaskJobs = startScheduledTasks()
        lifecycleJob.join()
    }

    /**
     * lifecycle と登録済みイベントハンドラの購読を開始し、終了時に必ず停止します。
     */
    suspend fun run() {
        try {
            start()
        } finally {
            stop()
        }
    }

    /**
     * イベント購読と lifecycle を停止します。
     */
    suspend fun stop() {
        scheduledTaskJobs.forEach { it.cancel() }
        scheduledTaskJobs = emptyList()
        lifecycle.stop()
        lifecycleEvents.emit(Disposed(occurredAt = Clock.System.now()))
        eventSubscription?.cancel()
        eventSubscription = null
        started = false
        supervisorJob.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    private fun toRuntimeEvent(event: Event): E? = event as? E

    private fun startScheduledTasks(): List<Job> =
        scheduledTasks.map { task ->
            runtimeScope.launch {
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

    private fun launchScheduledTask(task: ScheduledTask<R>): Job =
        runtimeScope.launch {
            try {
                task.block(context)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logger.error("Error while running scheduled task", e)
            }
        }
}

internal data class ScheduledTask<R : RuntimeContext>(
    val interval: Duration,
    val initialDelay: Duration,
    val block: suspend R.() -> Unit,
)
