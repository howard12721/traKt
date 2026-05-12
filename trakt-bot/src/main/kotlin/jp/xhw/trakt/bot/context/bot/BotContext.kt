package jp.xhw.trakt.bot.context.bot

import jp.xhw.trakt.bot.context.base.BaseContext
import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.model.BotId
import jp.xhw.trakt.bot.model.CurrentUser
import jp.xhw.trakt.bot.model.UserId
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
    origin: String,
    internal val botPort: BotPort,
    internal val selfPort: BotSelfPort,
    channelPort: ChannelPort,
    messagePort: MessagePort,
    userPort: UserPort,
    stampPort: StampPort,
    groupPort: GroupPort,
    filePort: FilePort,
) : BaseContext(
        origin,
        channelPort,
        messagePort,
        userPort,
        stampPort,
        groupPort,
        filePort,
    ) {
    var currentUser: CurrentUser? = null
        internal set

    var currentUsername: String? = null
        internal set

    var currentUserId: UserId? = null
        internal set
}
