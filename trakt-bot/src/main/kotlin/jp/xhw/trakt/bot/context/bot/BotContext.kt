package jp.xhw.trakt.bot.context.bot

import jp.xhw.trakt.bot.context.base.BaseContext
import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.model.BotId
import jp.xhw.trakt.bot.port.*

/**
 * Bot token で実行されるイベントハンドラおよび `execute` ブロックの context。
 *
 * `context(ctx: BotContext)` で定義された各種アクション関数を通じて
 * 共通操作に加えて Bot 固有操作を利用できます。
 */
@TraktDsl
class BotContext internal constructor(
    internal val botId: BotId?,
    internal val origin: String,
    channelPort: ChannelPort,
    messagePort: MessagePort,
    userPort: UserPort,
    stampPort: StampPort,
    groupPort: GroupPort,
    filePort: FilePort,
    botPort: BotPort,
) : BaseContext(
        channelPort,
        messagePort,
        userPort,
        stampPort,
        groupPort,
        filePort,
        botPort,
    )
