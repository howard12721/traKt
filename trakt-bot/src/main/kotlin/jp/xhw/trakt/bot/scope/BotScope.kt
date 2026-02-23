package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.port.BotRuntimeContext

/**
 * イベントハンドラおよび `execute` ブロック内で利用される実行スコープです。
 *
 * `context(scope: BotScope)` で定義された各種アクション関数を通じて
 * メッセージ送信・チャンネル操作・ユーザー取得などを行います。
 */
@TraktDsl
class BotScope internal constructor(
    internal val context: BotRuntimeContext,
)
