package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.CurrentUser

internal interface BotSelfPort {
    suspend fun fetchMe(): CurrentUser

    suspend fun editMe(
        displayName: String? = null,
        twitterId: String? = null,
        bio: String? = null,
        homeChannelId: ChannelId? = null,
    )
}
