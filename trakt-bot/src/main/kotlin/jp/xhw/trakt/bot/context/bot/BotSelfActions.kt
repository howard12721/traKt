package jp.xhw.trakt.bot.context.bot

import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.CurrentUser

/** Bot token でログインしているユーザー情報を取得します。 */
context(ctx: BotContext)
suspend fun fetchMe(): CurrentUser = ctx.selfPort.fetchMe()

/**
 * Bot token でログインしているユーザーのプロフィールを編集します。
 *
 * @param displayName 新しい表示名。`null` の場合は変更しません
 * @param twitterId 新しい Twitter ID。`null` の場合は変更しません
 * @param bio 新しい自己紹介。`null` の場合は変更しません
 * @param homeChannelId 新しいホームチャンネルID。`null` の場合は変更しません
 */
context(ctx: BotContext)
suspend fun editMe(
    displayName: String? = null,
    twitterId: String? = null,
    bio: String? = null,
    homeChannelId: ChannelId? = null,
) = ctx.selfPort.editMe(displayName, twitterId, bio, homeChannelId)
