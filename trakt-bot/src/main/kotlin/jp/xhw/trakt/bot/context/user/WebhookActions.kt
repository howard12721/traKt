package jp.xhw.trakt.bot.context.user

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant

/**
 * Webhook 一覧を取得します。
 *
 * @param all `true` の場合は権限のある全 Webhook を取得します
 * @return Webhook 一覧
 */
context(ctx: UserContext)
suspend fun fetchWebhooks(all: Boolean = false): List<Webhook> = ctx.webhookPort.fetchWebhooks(all)

/**
 * Webhook を作成します。
 *
 * @param name Webhook ユーザー表示名
 * @param description Webhook 説明
 * @param channel デフォルト投稿先チャンネル
 * @param secret Webhook シークレット。空文字の場合は insecure Webhook になります
 * @return 作成された Webhook
 */
context(ctx: UserContext)
suspend fun createWebhook(
    name: String,
    description: String,
    channel: ChannelHandle,
    secret: String,
): Webhook = ctx.webhookPort.createWebhook(name, description, channel.id, secret)

/**
 * このハンドルが指す Webhook を取得します。
 *
 * @return Webhook 情報
 */
context(ctx: UserContext)
suspend fun WebhookHandle.fetch(): Webhook = ctx.webhookPort.fetchWebhook(id)

/**
 * Webhook を編集します。
 *
 * @param name 新しい表示名。`null` の場合は変更しません
 * @param description 新しい説明。`null` の場合は変更しません
 * @param channel 新しいデフォルト投稿先。`null` の場合は変更しません
 * @param secret 新しいシークレット。`null` の場合は変更しません
 * @param owner 新しい所有者。`null` の場合は変更しません
 */
context(ctx: UserContext)
suspend fun WebhookHandle.update(
    name: String? = null,
    description: String? = null,
    channel: ChannelHandle? = null,
    secret: String? = null,
    owner: UserHandle? = null,
) = ctx.webhookPort.editWebhook(id, name, description, channel?.id, secret, owner?.id)

/** Webhook を削除します。 */
context(ctx: UserContext)
suspend fun WebhookHandle.delete() = ctx.webhookPort.deleteWebhook(id)

/**
 * Webhook でメッセージを投稿します。
 *
 * @param content 投稿本文
 * @param signature secure Webhook 用署名
 * @param channel 一時的に変更する投稿先チャンネル
 * @param embed `true` の場合に埋め込み展開を有効化します
 */
context(ctx: UserContext)
suspend fun WebhookHandle.send(
    content: String,
    signature: String? = null,
    channel: ChannelHandle? = null,
    embed: Boolean = false,
) = ctx.webhookPort.postWebhook(id, content, signature, channel?.id, embed)

/**
 * Webhook が投稿したメッセージ一覧を取得します。
 *
 * @param limit 取得件数上限。`null` の場合はサーバーデフォルト
 * @param offset 取得開始位置
 * @param since 指定時刻以降のメッセージに絞り込みます
 * @param until 指定時刻以前のメッセージに絞り込みます
 * @param inclusive `true` の場合は境界時刻と一致するメッセージを含めます
 * @param order 並び順
 * @return Webhook が投稿したメッセージ一覧
 */
context(ctx: UserContext)
suspend fun WebhookHandle.fetchMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> = ctx.webhookPort.fetchWebhookMessages(id, limit, offset, since, until, inclusive, order)

/**
 * Webhook が投稿したメッセージを削除します。
 *
 * @param message 削除対象メッセージ
 */
context(ctx: UserContext)
suspend fun WebhookHandle.deleteMessage(message: MessageHandle) = ctx.webhookPort.deleteWebhookMessage(id, message.id)

/**
 * Webhook アイコンを取得します。
 *
 * @return アイコン画像のバイト列
 */
context(ctx: UserContext)
suspend fun WebhookHandle.downloadIcon(): ByteArray = ctx.webhookPort.downloadWebhookIcon(id)

/**
 * Webhook アイコンを変更します。
 *
 * @param file アイコン画像のバイト列
 * @param fileName ファイル名
 * @param contentType MIME タイプ。`null` の場合はサーバー側判定
 */
context(ctx: UserContext)
suspend fun WebhookHandle.changeIcon(
    file: ByteArray,
    fileName: String,
    contentType: String? = null,
) = ctx.webhookPort.changeWebhookIcon(id, file, fileName, contentType)

/**
 * Webhook を再取得します。
 *
 * @return 最新の Webhook 情報
 */
context(ctx: UserContext)
suspend fun Webhook.refresh(): Webhook = handle.fetch()

/**
 * Webhook を編集します。
 *
 * @param name 新しい表示名。`null` の場合は変更しません
 * @param description 新しい説明。`null` の場合は変更しません
 * @param channel 新しいデフォルト投稿先。`null` の場合は変更しません
 * @param secret 新しいシークレット。`null` の場合は変更しません
 * @param owner 新しい所有者。`null` の場合は変更しません
 */
context(ctx: UserContext)
suspend fun Webhook.update(
    name: String? = null,
    description: String? = null,
    channel: ChannelHandle? = null,
    secret: String? = null,
    owner: UserHandle? = null,
) = handle.update(name, description, channel, secret, owner)

/** Webhook を削除します。 */
context(ctx: UserContext)
suspend fun Webhook.delete() = handle.delete()

/**
 * Webhook でメッセージを投稿します。
 *
 * @param content 投稿本文
 * @param signature secure Webhook 用署名
 * @param channel 一時的に変更する投稿先チャンネル
 * @param embed `true` の場合に埋め込み展開を有効化します
 */
context(ctx: UserContext)
suspend fun Webhook.send(
    content: String,
    signature: String? = null,
    channel: ChannelHandle? = null,
    embed: Boolean = false,
) = handle.send(content, signature, channel, embed)
