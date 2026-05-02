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
 * このハンドルが指す Bot を取得します。
 *
 * @param detail `true` の場合は詳細情報を取得します
 * @return Bot 情報
 */
context(ctx: UserContext)
suspend fun BotHandle.fetch(detail: Boolean = false): ManagedBot = ctx.managedBotPort.fetchBot(id, detail)

/**
 * このハンドルが指す Bot 詳細を取得します。
 *
 * @return Bot 詳細
 */
context(ctx: UserContext)
suspend fun BotHandle.fetchDetail(): ManagedBot.Detail = ctx.managedBotPort.fetchBot(id, detail = true) as ManagedBot.Detail

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
suspend fun BotHandle.update(
    displayName: String? = null,
    description: String? = null,
    privileged: Boolean? = null,
    mode: ManagedBotMode? = null,
    endpoint: String? = null,
    developer: UserHandle? = null,
    subscribeEvents: List<String>? = null,
    bio: String? = null,
) = ctx.managedBotPort.editBot(id, displayName, description, privileged, mode, endpoint, developer?.id, subscribeEvents, bio)

/** Bot を削除します。 */
context(ctx: UserContext)
suspend fun BotHandle.delete() = ctx.managedBotPort.deleteBot(id)

/** Bot を有効化します。 */
context(ctx: UserContext)
suspend fun BotHandle.activate() = ctx.managedBotPort.activateBot(id)

/** Bot を無効化します。 */
context(ctx: UserContext)
suspend fun BotHandle.inactivate() = ctx.managedBotPort.inactivateBot(id)

/**
 * Bot のトークンを再発行します。
 *
 * @return 再発行された Bot トークン
 */
context(ctx: UserContext)
suspend fun BotHandle.reissueTokens(): BotTokens = ctx.managedBotPort.reissueBot(id)

/**
 * Bot のイベントログを取得します。
 *
 * @param limit 取得件数上限。`null` の場合はサーバーデフォルト
 * @param offset 取得開始位置
 * @return Bot イベントログ一覧
 */
context(ctx: UserContext)
suspend fun BotHandle.fetchLogs(
    limit: Int? = null,
    offset: Int = 0,
): List<BotEventLog> = ctx.managedBotPort.fetchBotLogs(id, limit, offset)

/**
 * Bot をチャンネルに参加させます。
 *
 * @param channel 参加先チャンネル
 */
context(ctx: UserContext)
suspend fun BotHandle.join(channel: ChannelHandle) = ctx.managedBotPort.joinChannel(id, channel.id)

/**
 * Bot をチャンネルから退出させます。
 *
 * @param channel 退出元チャンネル
 */
context(ctx: UserContext)
suspend fun BotHandle.leave(channel: ChannelHandle) = ctx.managedBotPort.leaveChannel(id, channel.id)

/**
 * Bot アイコンを取得します。
 *
 * @return アイコン画像のバイト列
 */
context(ctx: UserContext)
suspend fun BotHandle.downloadIcon(): ByteArray = ctx.managedBotPort.downloadBotIcon(id)

/**
 * Bot アイコンを変更します。
 *
 * @param file アイコン画像のバイト列
 * @param fileName ファイル名
 * @param contentType MIME タイプ。`null` の場合はサーバー側判定
 */
context(ctx: UserContext)
suspend fun BotHandle.changeIcon(
    file: ByteArray,
    fileName: String,
    contentType: String? = null,
) = ctx.managedBotPort.changeBotIcon(id, file, fileName, contentType)

/**
 * Bot を再取得します。
 *
 * @param detail `true` の場合は詳細情報を取得します
 * @return 最新の Bot 情報
 */
context(ctx: UserContext)
suspend fun ManagedBot.refresh(detail: Boolean = this is ManagedBot.Detail): ManagedBot = handle.fetch(detail)

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
    developer: UserHandle? = null,
    subscribeEvents: List<String>? = null,
    bio: String? = null,
) = handle.update(displayName, description, privileged, mode, endpoint, developer, subscribeEvents, bio)

/** Bot を削除します。 */
context(ctx: UserContext)
suspend fun ManagedBot.delete() = handle.delete()

/** Bot を有効化します。 */
context(ctx: UserContext)
suspend fun ManagedBot.activate() = handle.activate()

/** Bot を無効化します。 */
context(ctx: UserContext)
suspend fun ManagedBot.inactivate() = handle.inactivate()
