package jp.xhw.trakt.bot.context.user

import jp.xhw.trakt.bot.model.*

/**
 * 自分自身の詳細を取得します。
 *
 * @return 自分自身の詳細情報
 */
context(ctx: UserContext)
suspend fun fetchMe(): CurrentUser = ctx.selfPort.fetchMe()

/**
 * 自分のプロフィールを編集します。
 *
 * @param displayName 新しい表示名。`null` の場合は変更しません
 * @param twitterId 新しい Twitter ID。`null` の場合は変更しません
 * @param bio 新しい自己紹介。`null` の場合は変更しません
 * @param homeChannel 新しいホームチャンネル。`null` の場合は変更しません
 */
context(ctx: UserContext)
suspend fun editMe(
    displayName: String? = null,
    twitterId: String? = null,
    bio: String? = null,
    homeChannel: ChannelId? = null,
) = ctx.selfPort.editMe(displayName, twitterId, bio, homeChannel)

/**
 * 自分のアイコン画像を取得します。
 *
 * @return アイコン画像のバイト列
 */
context(ctx: UserContext)
suspend fun downloadMyIcon(): ByteArray = ctx.selfPort.downloadMyIcon()

/**
 * 自分のアイコン画像を変更します。
 *
 * @param file アイコン画像のバイト列
 * @param fileName ファイル名
 * @param contentType MIME タイプ。`null` の場合はサーバー側判定
 */
context(ctx: UserContext)
suspend fun changeMyIcon(
    file: ByteArray,
    fileName: String,
    contentType: String? = null,
) = ctx.selfPort.changeMyIcon(file, fileName, contentType)

/**
 * 自分のパスワードを変更します。
 *
 * @param currentPassword 現在のパスワード
 * @param newPassword 新しいパスワード
 */
context(ctx: UserContext)
suspend fun changeMyPassword(
    currentPassword: String,
    newPassword: String,
) = ctx.selfPort.changeMyPassword(currentPassword, newPassword)

/**
 * 自分のユーザー設定を取得します。
 *
 * @return ユーザー設定
 */
context(ctx: UserContext)
suspend fun fetchMySettings(): UserSettings = ctx.selfPort.fetchMySettings()

/**
 * メッセージ引用通知が有効か取得します。
 *
 * @return 有効な場合は `true`
 */
context(ctx: UserContext)
suspend fun isNotifyCitationEnabled(): Boolean = ctx.selfPort.isNotifyCitationEnabled()

/**
 * メッセージ引用通知設定を変更します。
 *
 * @param enabled 有効にする場合は `true`
 */
context(ctx: UserContext)
suspend fun setNotifyCitation(enabled: Boolean) = ctx.selfPort.setNotifyCitation(enabled)

/**
 * 自分のタグ一覧を取得します。
 *
 * @return 自分に付与されているタグ一覧
 */
context(ctx: UserContext)
suspend fun fetchMyTags(): List<UserTag> = ctx.selfPort.fetchMyTags()

/**
 * 自分にタグを追加します。
 *
 * @param tag 追加するタグ文字列
 * @return 追加後のタグ情報
 */
context(ctx: UserContext)
suspend fun addMyTag(tag: String): UserTag = ctx.selfPort.addMyTag(tag)

/**
 * 自分のタグのロック状態を変更します。
 *
 * @param isLocked ロックする場合は `true`
 */
context(ctx: UserContext)
suspend fun UserTagId.setMyTagLocked(isLocked: Boolean) = ctx.selfPort.editMyTag(this, isLocked)

/** 自分からこのタグを削除します。 */
context(ctx: UserContext)
suspend fun UserTagId.removeFromMe() = ctx.selfPort.removeMyTag(this)

/**
 * 自分がスターしているチャンネル一覧を取得します。
 *
 * @return スターしているチャンネル ID 一覧
 */
context(ctx: UserContext)
suspend fun fetchMyStars(): List<ChannelId> = ctx.selfPort.fetchMyStars()

/** チャンネルをスターします。 */
context(ctx: UserContext)
suspend fun ChannelId.star() = ctx.selfPort.addMyStar(this)

/** チャンネルのスターを外します。 */
context(ctx: UserContext)
suspend fun ChannelId.unstar() = ctx.selfPort.removeMyStar(this)

/**
 * 自分のチャンネル購読状態一覧を取得します。
 *
 * @return チャンネル購読状態一覧
 */
context(ctx: UserContext)
suspend fun fetchMySubscriptions(): List<ChannelSubscription> = ctx.selfPort.fetchMySubscriptions()

/**
 * チャンネル購読レベルを設定します。
 *
 * @param level 新しい購読レベル
 */
context(ctx: UserContext)
suspend fun ChannelId.setSubscription(level: ChannelSubscriptionLevel) = ctx.selfPort.setMySubscription(this, level)

/** チャンネル購読を無効にします。 */
context(ctx: UserContext)
suspend fun ChannelId.unsubscribe() = setSubscription(ChannelSubscriptionLevel.NONE)

/** チャンネルを未読管理対象にします。 */
context(ctx: UserContext)
suspend fun ChannelId.subscribe() = setSubscription(ChannelSubscriptionLevel.SUBSCRIBED)

/** チャンネルを通知対象にします。 */
context(ctx: UserContext)
suspend fun ChannelId.notify() = setSubscription(ChannelSubscriptionLevel.NOTIFIED)

/**
 * 自分の未読チャンネル一覧を取得します。
 *
 * @return 未読チャンネル一覧
 */
context(ctx: UserContext)
suspend fun fetchMyUnreadChannels(): List<UnreadChannel> = ctx.selfPort.fetchMyUnreadChannels()

/** チャンネルを既読にします。 */
context(ctx: UserContext)
suspend fun ChannelId.markRead() = ctx.selfPort.readChannel(this)

/**
 * 自分の閲覧状態一覧を取得します。
 *
 * @return 閲覧状態一覧
 */
context(ctx: UserContext)
suspend fun fetchMyViewStates(): List<MyChannelViewState> = ctx.selfPort.fetchMyViewStates()

/**
 * FCM デバイスを登録します。
 *
 * @param token FCM デバイストークン
 */
context(ctx: UserContext)
suspend fun registerFCMDevice(token: String) = ctx.selfPort.registerFCMDevice(token)

/**
 * 自分のログインセッション一覧を取得します。
 *
 * @return ログインセッション一覧
 */
context(ctx: UserContext)
suspend fun fetchMySessions(): List<LoginSession> = ctx.selfPort.fetchMySessions()

/** このログインセッションを無効化します。 */
context(ctx: UserContext)
suspend fun LoginSessionId.revoke() = ctx.selfPort.revokeMySession(this)

/**
 * 自分に発行された OAuth2 トークン一覧を取得します。
 *
 * @return 有効な OAuth2 トークン一覧
 */
context(ctx: UserContext)
suspend fun fetchMyTokens(): List<ActiveOAuth2Token> = ctx.selfPort.fetchMyTokens()

/** この OAuth2 トークンの認可を取り消します。 */
context(ctx: UserContext)
suspend fun OAuth2TokenId.revoke() = ctx.selfPort.revokeMyToken(this)

/**
 * 自分に紐付く外部ログインアカウント一覧を取得します。
 *
 * @return 外部ログインアカウント一覧
 */
context(ctx: UserContext)
suspend fun fetchMyExternalAccounts(): List<ExternalAccount> = ctx.selfPort.fetchMyExternalAccounts()

/**
 * 外部ログインアカウントの紐付けを開始します。
 *
 * @param providerName 外部ログインプロバイダ名
 */
context(ctx: UserContext)
suspend fun linkExternalAccount(providerName: String) = ctx.selfPort.linkExternalAccount(providerName)

/**
 * 外部ログインアカウントの紐付けを解除します。
 *
 * @param providerName 外部ログインプロバイダ名
 */
context(ctx: UserContext)
suspend fun unlinkExternalAccount(providerName: String) = ctx.selfPort.unlinkExternalAccount(providerName)

/**
 * 自分のスタンプ履歴を取得します。
 *
 * @param limit 取得件数上限
 * @return スタンプ履歴
 */
context(ctx: UserContext)
suspend fun fetchMyStampHistory(limit: Int = 100): List<StampHistoryEntry> = ctx.selfPort.fetchMyStampHistory(limit)

/**
 * 自分へのスタンプレコメンドを取得します。
 *
 * @param limit 取得件数上限
 * @return スタンプレコメンド一覧
 */
context(ctx: UserContext)
suspend fun fetchMyStampRecommendations(limit: Int = 100): List<StampRecommendation> = ctx.selfPort.fetchMyStampRecommendations(limit)

/**
 * 自分の QR コード画像を取得します。
 *
 * @return QR コード PNG 画像のバイト列
 */
context(ctx: UserContext)
suspend fun fetchMyQRCode(): ByteArray = ctx.selfPort.fetchMyQRCode()
