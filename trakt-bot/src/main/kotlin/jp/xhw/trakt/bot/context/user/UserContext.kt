package jp.xhw.trakt.bot.context.user

import jp.xhw.trakt.bot.context.base.BaseContext
import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.model.CurrentUser
import jp.xhw.trakt.bot.model.UserId
import jp.xhw.trakt.bot.port.*

/**
 * User token で実行されるセルフボット用 context。
 *
 * Bot 固有の `channel.join` / `channel.leave` を除く、通常の traQ 操作を利用できます。
 */
@TraktDsl
class UserContext internal constructor(
    origin: String,
    channelPort: ChannelPort,
    messagePort: MessagePort,
    userPort: UserPort,
    stampPort: StampPort,
    groupPort: GroupPort,
    filePort: FilePort,
    internal val selfPort: SelfPort,
    internal val clipPort: ClipPort,
    internal val webhookPort: WebhookPort,
    internal val managedBotPort: ManagedBotPort,
    internal val userWebSocketPort: UserWebSocketPort,
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
