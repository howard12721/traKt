package jp.xhw.trakt.bot.scope.bot

import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.model.BotId
import jp.xhw.trakt.bot.port.*

/**
 * イベントハンドラおよび `execute` ブロック内で利用される実行スコープです。
 *
 * `context(scope: BotScope)` で定義された各種アクション関数を通じて
 * メッセージ送信・チャンネル操作・ユーザー取得などを行います。
 */
@TraktDsl
class BotContext internal constructor(
    internal val botId: BotId?,
    internal val origin: String,
    internal val channelPort: ChannelPort,
    internal val messagePort: MessagePort,
    internal val userPort: UserPort,
    internal val stampPort: StampPort,
    internal val groupPort: GroupPort,
    internal val filePort: FilePort,
    internal val botPort: BotPort,
)
