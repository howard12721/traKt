package jp.xhw.trakt.bot.context.bot

import jp.xhw.trakt.bot.model.CurrentUser

/** Bot token でログインしているユーザー情報を取得します。 */
context(ctx: BotContext)
suspend fun fetchMe(): CurrentUser = ctx.selfPort.fetchMe()
