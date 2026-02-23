package jp.xhw.trakt.bot

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid

/**
 * traQ Bot クライアントを構築します。
 *
 * 返り値の [TraktClient] に対して [TraktClient.start] / [TraktClient.stop] を呼び、
 * `block` で `on<Event>` ハンドラを登録して利用します。
 *
 * @param token traQ Bot アクセストークン
 * @param botId Bot の UUID
 * @param origin traQ サーバーのホスト名
 * @param coroutineContext イベント処理に使うコルーチンコンテキスト
 * @param block クライアント初期化時に実行する設定ブロック
 * @return 設定済みの [TraktClient]
 */
fun trakt(
    token: String,
    botId: Uuid,
    origin: String = "q.trap.jp",
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: TraktClient.() -> Unit = {},
): TraktClient =
    TraktClient(
        token = token,
        botId = botId,
        origin = origin,
        coroutineContext = coroutineContext,
    ).apply(block)
