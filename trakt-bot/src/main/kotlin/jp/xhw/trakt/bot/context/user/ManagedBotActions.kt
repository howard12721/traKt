package jp.xhw.trakt.bot.context.user

import jp.xhw.trakt.bot.model.*

/**
 * Bot を作成します。
 *
 * @param name Bot ユーザーID。サーバー側で `BOT_` 接頭辞が付与されます
 * @param displayName Bot ユーザー表示名
 * @param description Bot 説明
 * @param mode Bot 動作モード
 * @param endpoint HTTP Mode の Bot サーバーエンドポイント
 * @return 作成された Bot 詳細
 */
context(ctx: UserContext)
suspend fun createManagedBot(
    name: String,
    displayName: String,
    description: String,
    mode: ManagedBotMode,
    endpoint: String? = null,
): ManagedBot.Detail = ctx.managedBotPort.createBot(name, displayName, description, mode, endpoint)

/**
 * 自分が管理できる Bot 一覧を取得します。
 *
 * @param all `true` の場合は権限のある全 Bot を取得します
 * @return Bot 一覧
 */
context(ctx: UserContext)
suspend fun fetchManagedBots(all: Boolean = false): List<ManagedBot.Basic> = ctx.managedBotPort.fetchBots(all)

/**
 * この ID が指す Bot を取得します。
 *
 * @param detail `true` の場合は詳細情報を取得します
 * @return Bot 情報
 */
context(ctx: UserContext)
suspend fun BotId.fetch(detail: Boolean = false): ManagedBot = ctx.managedBotPort.fetchBot(this, detail)

/**
 * この ID が指す Bot 詳細を取得します。
 *
 * @return Bot 詳細
 */
context(ctx: UserContext)
suspend fun BotId.fetchDetail(): ManagedBot.Detail = ctx.managedBotPort.fetchBot(this, detail = true) as ManagedBot.Detail

/**
 * Bot を編集します。
 *
 * @param displayName 新しい表示名。`null` の場合は変更しません
 * @param description 新しい説明。`null` の場合は変更しません
 * @param privileged 特権状態。`null` の場合は変更しません
 * @param mode 新しい動作モード。`null` の場合は変更しません
 * @param endpoint 新しいエンドポイント。`null` の場合は変更しません
 * @param developer 新しい開発者。`null` の場合は変更しません
 * @param subscribeEvents 新しい購読イベント一覧。`null` の場合は変更しません
 * @param bio 新しい自己紹介。`null` の場合は変更しません
 */
context(ctx: UserContext)
suspend fun BotId.update(
    displayName: String? = null,
    description: String? = null,
    privileged: Boolean? = null,
    mode: ManagedBotMode? = null,
    endpoint: String? = null,
    developer: UserId? = null,
    subscribeEvents: List<String>? = null,
    bio: String? = null,
) = ctx.managedBotPort.editBot(this, displayName, description, privileged, mode, endpoint, developer, subscribeEvents, bio)

/** Bot を削除します。 */
context(ctx: UserContext)
suspend fun BotId.delete() = ctx.managedBotPort.deleteBot(this)

/** Bot を有効化します。 */
context(ctx: UserContext)
suspend fun BotId.activate() = ctx.managedBotPort.activateBot(this)

/** Bot を無効化します。 */
context(ctx: UserContext)
suspend fun BotId.inactivate() = ctx.managedBotPort.inactivateBot(this)

/**
 * Bot のトークンを再発行します。
 *
 * @return 再発行された Bot トークン
 */
context(ctx: UserContext)
suspend fun BotId.reissueTokens(): BotTokens = ctx.managedBotPort.reissueBot(this)

/**
 * Bot のイベントログを取得します。
 *
 * @param limit 取得件数上限。`null` の場合はサーバーデフォルト
 * @param offset 取得開始位置
 * @return Bot イベントログ一覧
 */
context(ctx: UserContext)
suspend fun BotId.fetchLogs(
    limit: Int? = null,
    offset: Int = 0,
): List<BotEventLog> = ctx.managedBotPort.fetchBotLogs(this, limit, offset)

/**
 * Bot をチャンネルに参加させます。
 *
 * @param channel 参加先チャンネル
 */
context(ctx: UserContext)
suspend fun BotId.join(channel: ChannelId) = ctx.managedBotPort.joinChannel(this, channel)

/**
 * Bot をチャンネルから退出させます。
 *
 * @param channel 退出元チャンネル
 */
context(ctx: UserContext)
suspend fun BotId.leave(channel: ChannelId) = ctx.managedBotPort.leaveChannel(this, channel)

/**
 * Bot アイコンを取得します。
 *
 * @return アイコン画像のバイト列
 */
context(ctx: UserContext)
suspend fun BotId.downloadIcon(): ByteArray = ctx.managedBotPort.downloadBotIcon(this)

/**
 * Bot アイコンを変更します。
 *
 * @param file アイコン画像のバイト列
 * @param fileName ファイル名
 * @param contentType MIME タイプ。`null` の場合はサーバー側判定
 */
context(ctx: UserContext)
suspend fun BotId.changeIcon(
    file: ByteArray,
    fileName: String,
    contentType: String? = null,
) = ctx.managedBotPort.changeBotIcon(this, file, fileName, contentType)

/**
 * Bot を取得します。
 *
 * @param detail `true` の場合は詳細情報を取得します
 * @return 最新の Bot 情報
 */
context(ctx: UserContext)
suspend fun ManagedBot.refresh(detail: Boolean = this is ManagedBot.Detail): ManagedBot = id.fetch(detail)

/**
 * Bot を編集します。
 *
 * @param displayName 新しい表示名。`null` の場合は変更しません
 * @param description 新しい説明。`null` の場合は変更しません
 * @param privileged 特権状態。`null` の場合は変更しません
 * @param mode 新しい動作モード。`null` の場合は変更しません
 * @param endpoint 新しいエンドポイント。`null` の場合は変更しません
 * @param developer 新しい開発者。`null` の場合は変更しません
 * @param subscribeEvents 新しい購読イベント一覧。`null` の場合は変更しません
 * @param bio 新しい自己紹介。`null` の場合は変更しません
 */
context(ctx: UserContext)
suspend fun ManagedBot.update(
    displayName: String? = null,
    description: String? = null,
    privileged: Boolean? = null,
    mode: ManagedBotMode? = null,
    endpoint: String? = null,
    developer: UserId? = null,
    subscribeEvents: List<String>? = null,
    bio: String? = null,
) = id.update(displayName, description, privileged, mode, endpoint, developer, subscribeEvents, bio)

/** Bot を削除します。 */
context(ctx: UserContext)
suspend fun ManagedBot.delete() = id.delete()

/** Bot を有効化します。 */
context(ctx: UserContext)
suspend fun ManagedBot.activate() = id.activate()

/** Bot を無効化します。 */
context(ctx: UserContext)
suspend fun ManagedBot.inactivate() = id.inactivate()
