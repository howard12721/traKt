package jp.xhw.trakt.bot.infrastructure.runtime

import jp.xhw.trakt.bot.context.RuntimeContext
import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.context.user.UserContext
import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.model.BotEvent
import jp.xhw.trakt.bot.model.UserEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

typealias TraktClient = Runtime<BotContext, BotEvent>

typealias SelfTraktClient = Runtime<UserContext, UserEvent>

/**
 * context、イベント購読、lifecycle を組み合わせて実行するクライアント。
 *
 * [on] でイベントハンドラを登録し、[start] でイベント購読と lifecycle を開始します。
 * ハンドラ登録は [start] 呼び出し前に完了させる必要があります。
 *
 * @param coroutineContext イベント処理に使うコルーチンコンテキスト
 */
@TraktDsl
class Runtime<R : RuntimeContext, E : Any> internal constructor(
    internal val context: R,
    internal val ruleRegistry: RuleRegistry<R, E>,
    internal val eventSource: Flow<*>,
    private val lifecycle: RuntimeLifecycle = RuntimeLifecycle.Noop,
    coroutineContext: CoroutineContext = Dispatchers.Default,
) {
    private val supervisorJob = SupervisorJob()
    private val runtimeScope = CoroutineScope(supervisorJob + coroutineContext)

    private var subscriptions: List<Job> = emptyList()

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
        check(subscriptions.isEmpty()) { "Handlers must be registered before start()" }
        ruleRegistry.on(eventClass, context, handler)
    }

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
        check(subscriptions.isEmpty()) { "Client is already started" }
        subscriptions = ruleRegistry.install(eventSource, runtimeScope)
        lifecycle.start()
    }

    /**
     * イベント購読と lifecycle を停止します。
     */
    suspend fun stop() {
        subscriptions.forEach(Job::cancel)
        subscriptions = emptyList()
        lifecycle.stop()
        supervisorJob.cancel()
    }
}
