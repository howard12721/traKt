package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.CurrentUser

internal interface BotSelfPort {
    suspend fun fetchMe(): CurrentUser
}
