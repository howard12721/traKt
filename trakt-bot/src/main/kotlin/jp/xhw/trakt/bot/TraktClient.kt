package jp.xhw.trakt.bot

import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.infrastructure.gateway.*
import jp.xhw.trakt.bot.infrastructure.runtime.RuleRegistry
import jp.xhw.trakt.bot.model.BotId
import jp.xhw.trakt.bot.model.Event
import jp.xhw.trakt.bot.port.BotRuntimeContext
import jp.xhw.trakt.bot.scope.BotScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.uuid.Uuid

/**
 * traQ Bot の実行クライアント。
 *
 * [on] でイベントハンドラを登録し、[start] で購読を開始します。
 * ハンドラ登録は [start] 呼び出し前に完了させる必要があります。
 *
 * @param token traQ Bot アクセストークン
 * @param botId Bot の UUID
 * @param origin traQ サーバーのホスト名
 * @param coroutineContext イベント処理に使うコルーチンコンテキスト
 */
@TraktDsl
class TraktClient(
    token: String,
    botId: Uuid,
    origin: String = "q.trap.jp",
    coroutineContext: CoroutineContext = Dispatchers.Default,
) {
    private val apiGateway = TraqApiGateway(token = token, origin = origin)
    private val ruleRegistry = RuleRegistry()
    private val supervisorJob = SupervisorJob()
    private val runtimeScope = CoroutineScope(supervisorJob + coroutineContext)

    private val context =
        BotRuntimeContext(
            botId = BotId(botId),
            origin = origin,
            channelPort = TraqChannelPort(apiGateway),
            messagePort = TraqMessagePort(apiGateway),
            userPort = TraqUserPort(apiGateway),
            stampPort = TraqStampPort(apiGateway),
            groupPort = TraqGroupPort(apiGateway),
            filePort = TraqFilePort(apiGateway),
            botPort = TraqBotPort(apiGateway),
        )
    private val bot = BotScope(context)
    private var subscriptions: List<Job> = emptyList()

    /**
     * 指定イベント型のハンドラを登録します。
     *
     * 例: `on<MessageCreated> { ... }`
     *
     * @param handler イベント受信時に実行するハンドラ
     */
    inline fun <reified T : Event> on(noinline handler: suspend BotScope.(T) -> Unit) {
        on(T::class, handler)
    }

    /**
     * 指定イベント型のハンドラを登録します。
     *
     * @param eventClass 受信対象イベント型
     * @param handler イベント受信時に実行するハンドラ
     */
    @PublishedApi
    internal fun <T : Event> on(
        eventClass: KClass<T>,
        handler: suspend BotScope.(T) -> Unit,
    ) {
        check(subscriptions.isEmpty()) { "Handlers must be registered before start()" }
        ruleRegistry.on(eventClass, bot, handler)
    }

    /**
     * 現在の [BotScope] で単発処理を実行します。
     *
     * @param block 実行する処理
     */
    suspend fun execute(block: suspend BotScope.() -> Unit) {
        bot.block()
    }

    /**
     * 現在の [BotScope] で非同期に単発処理を実行し、[Job] を返します。
     *
     * @param block 実行する処理
     * @return 実行中処理を表す [Job]
     */
    fun launchAndExecute(block: suspend BotScope.() -> Unit): Job =
        runtimeScope.launch {
            bot.block()
        }

    /**
     * WebSocket 接続を開始し、登録済みイベントハンドラの購読を開始します。
     */
    suspend fun start() {
        check(subscriptions.isEmpty()) { "Client is already started" }
        subscriptions = ruleRegistry.install(apiGateway.ws, runtimeScope)
        apiGateway.ws.start()
    }

    /**
     * 購読と通信リソースを停止・解放します。
     */
    suspend fun stop() {
        subscriptions.forEach(Job::cancel)
        subscriptions = emptyList()
        apiGateway.ws.stop()
        apiGateway.ws.close()
        apiGateway.httpClient.close()
        supervisorJob.cancel()
    }
}
