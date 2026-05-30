package jp.xhw.trakt.bot

import jp.xhw.trakt.bot.infrastructure.client.BotEventRegistrar
import jp.xhw.trakt.bot.infrastructure.client.SelfEventRegistrar
import jp.xhw.trakt.bot.infrastructure.client.TraktClient
import jp.xhw.trakt.bot.infrastructure.client.TraktSelfClient
import jp.xhw.trakt.bot.infrastructure.runtime.TraktBot
import jp.xhw.trakt.bot.infrastructure.runtime.TraktSelfBot
import jp.xhw.trakt.bot.infrastructure.runtime.bot.createBotClient
import jp.xhw.trakt.bot.infrastructure.runtime.user.createUserClient
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid

/**
 * traQ Bot クライアントを構築します。
 *
 * @param token traQ Bot アクセストークン
 * @param botId Bot の UUID。Bot 固有アクションを使う場合に指定
 * @param origin traQ サーバーのホスト名
 * @param coroutineContext イベント処理に使うコルーチンコンテキスト
 * @param debugMode DEBUG ログを有効にするかどうか
 * @return 設定済みの [TraktClient]
 */
fun trakt(
    token: String,
    botId: Uuid? = null,
    origin: String = "q.trap.jp",
    coroutineContext: CoroutineContext = Dispatchers.Default,
    debugMode: Boolean = false,
): TraktClient =
    createBotClient(
        token = token,
        botId = botId,
        origin = origin,
        coroutineContext = coroutineContext,
        debugMode = debugMode,
    )

/**
 * traQ Bot クライアントを構築し、イベントハンドラを登録します。
 *
 * `block` でイベントハンドラを登録し、返り値の [TraktBot] に対して
 * [start][jp.xhw.trakt.bot.infrastructure.runtime.EventRuntime.start] /
 * [stop][jp.xhw.trakt.bot.infrastructure.runtime.EventRuntime.stop] を呼び出して利用します。
 *
 * @param token traQ Bot アクセストークン
 * @param botId Bot の UUID。Bot 固有アクションを使う場合に指定
 * @param origin traQ サーバーのホスト名
 * @param coroutineContext イベント処理に使うコルーチンコンテキスト
 * @param debugMode DEBUG ログを有効にするかどうか
 * @param block クライアント初期化時に実行する設定ブロック
 * @return 設定済みの [TraktBot]
 */
fun traktBot(
    token: String,
    botId: Uuid? = null,
    origin: String = "q.trap.jp",
    coroutineContext: CoroutineContext = Dispatchers.Default,
    debugMode: Boolean = false,
    block: BotEventRegistrar.() -> Unit,
): TraktBot =
    createBotClient(
        token = token,
        botId = botId,
        origin = origin,
        coroutineContext = coroutineContext,
        debugMode = debugMode,
    ).buildEventRuntime(block)

/**
 * traQ User token で動くセルフボット用クライアントを構築します。
 *
 * @param token traQ User アクセストークン
 * @param origin traQ サーバーのホスト名
 * @param coroutineContext イベント処理に使うコルーチンコンテキスト
 * @param debugMode DEBUG ログを有効にするかどうか
 * @return 設定済みの [TraktSelfClient]
 */
fun traktSelf(
    token: String,
    origin: String = "q.trap.jp",
    coroutineContext: CoroutineContext = Dispatchers.Default,
    debugMode: Boolean = false,
): TraktSelfClient =
    createUserClient(
        token = token,
        origin = origin,
        coroutineContext = coroutineContext,
        debugMode = debugMode,
    )

/**
 * traQ User token で動くセルフボット用クライアントを構築し、イベントハンドラを登録します。
 *
 * `block` でイベントハンドラを登録し、返り値の [TraktSelfBot] に対して
 * [start][jp.xhw.trakt.bot.infrastructure.runtime.EventRuntime.start] /
 * [stop][jp.xhw.trakt.bot.infrastructure.runtime.EventRuntime.stop] を呼び出して利用します。
 *
 * @param token traQ User アクセストークン
 * @param origin traQ サーバーのホスト名
 * @param coroutineContext イベント処理に使うコルーチンコンテキスト
 * @param debugMode DEBUG ログを有効にするかどうか
 * @param block クライアント初期化時に実行する設定ブロック
 * @return 設定済みの [TraktSelfBot]
 */
fun traktSelfBot(
    token: String,
    origin: String = "q.trap.jp",
    coroutineContext: CoroutineContext = Dispatchers.Default,
    debugMode: Boolean = false,
    block: SelfEventRegistrar.() -> Unit,
): TraktSelfBot =
    createUserClient(
        token = token,
        origin = origin,
        coroutineContext = coroutineContext,
        debugMode = debugMode,
    ).buildEventRuntime(block)
