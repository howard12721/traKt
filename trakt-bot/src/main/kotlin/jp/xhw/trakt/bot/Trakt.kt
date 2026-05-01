package jp.xhw.trakt.bot

import jp.xhw.trakt.bot.infrastructure.runtime.TraktClient
import jp.xhw.trakt.bot.infrastructure.runtime.SelfTraktClient
import jp.xhw.trakt.bot.infrastructure.runtime.bot.createBotClient
import jp.xhw.trakt.bot.infrastructure.runtime.user.createUserClient
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid

/**
 * traQ Bot クライアントを構築します。
 *
 * `block` でイベントハンドラを登録し、返り値の [TraktClient] に対して
 * [TraktClient.start] / [TraktClient.stop] を呼び出して利用します。
 *
 * @param token traQ Bot アクセストークン
 * @param botId Bot の UUID。Bot 固有アクションを使う場合に指定
 * @param origin traQ サーバーのホスト名
 * @param coroutineContext イベント処理に使うコルーチンコンテキスト
 * @param debugMode DEBUG ログを有効にするかどうか
 * @param block クライアント初期化時に実行する設定ブロック
 * @return 設定済みの [TraktClient]
 */
fun trakt(
    token: String,
    botId: Uuid? = null,
    origin: String = "q.trap.jp",
    coroutineContext: CoroutineContext = Dispatchers.Default,
    debugMode: Boolean = false,
    block: TraktClient.() -> Unit = {},
): TraktClient =
    createBotClient(
        token = token,
        botId = botId,
        origin = origin,
        coroutineContext = coroutineContext,
        debugMode = debugMode,
    ).apply(block)

/**
 * traQ User token で動くセルフボット用クライアントを構築します。
 *
 * `block` で [jp.xhw.trakt.bot.model.UserEvent] のハンドラを登録し、
 * 返り値の [SelfTraktClient] に対して [SelfTraktClient.start] / [SelfTraktClient.stop]
 * を呼び出して利用します。
 *
 * @param token traQ User アクセストークン
 * @param origin traQ サーバーのホスト名
 * @param coroutineContext イベント処理に使うコルーチンコンテキスト
 * @param debugMode DEBUG ログを有効にするかどうか
 * @param block クライアント初期化時に実行する設定ブロック
 * @return 設定済みの [SelfTraktClient]
 */
fun selfTrakt(
    token: String,
    origin: String = "q.trap.jp",
    coroutineContext: CoroutineContext = Dispatchers.Default,
    debugMode: Boolean = false,
    block: SelfTraktClient.() -> Unit = {},
): SelfTraktClient =
    createUserClient(
        token = token,
        origin = origin,
        coroutineContext = coroutineContext,
        debugMode = debugMode,
    ).apply(block)
