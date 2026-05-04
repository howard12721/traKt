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
    channel: ChannelId,
    secret: String,
): Webhook = ctx.webhookPort.createWebhook(name, description, channel, secret)

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
    channel: Channel.PublicChannel,
    secret: String,
): Webhook = createWebhook(name, description, channel.id, secret)

/**
 * この ID が指す Webhook を取得します。
 *
 * @return Webhook 情報
 */
context(ctx: UserContext)
suspend fun WebhookId.fetch(): Webhook = ctx.webhookPort.fetchWebhook(this)

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
suspend fun WebhookId.update(
    name: String? = null,
    description: String? = null,
    channel: ChannelId? = null,
    secret: String? = null,
    owner: UserId? = null,
) = ctx.webhookPort.editWebhook(this, name, description, channel, secret, owner)

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
suspend fun WebhookId.update(
    name: String? = null,
    description: String? = null,
    channel: Channel.PublicChannel,
    secret: String? = null,
    owner: UserId? = null,
) = update(name, description, channel.id, secret, owner)

/** Webhook を削除します。 */
context(ctx: UserContext)
suspend fun WebhookId.delete() = ctx.webhookPort.deleteWebhook(this)

/**
 * Webhook でメッセージを投稿します。
 *
 * @param content 投稿本文
 * @param signature secure Webhook 用署名
 * @param channel 一時的に変更する投稿先チャンネル
 * @param embed `true` の場合に埋め込み展開を有効化します
 */
context(ctx: UserContext)
suspend fun WebhookId.send(
    content: String,
    signature: String? = null,
    channel: ChannelId? = null,
    embed: Boolean = false,
) = ctx.webhookPort.postWebhook(this, content, signature, channel, embed)

/**
 * Webhook でメッセージを投稿します。
 *
 * @param content 投稿本文
 * @param signature secure Webhook 用署名
 * @param channel 一時的に変更する投稿先チャンネル
 * @param embed `true` の場合に埋め込み展開を有効化します
 */
context(ctx: UserContext)
suspend fun WebhookId.send(
    content: String,
    signature: String? = null,
    channel: Channel.PublicChannel,
    embed: Boolean = false,
) = send(content, signature, channel.id, embed)

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
suspend fun WebhookId.fetchMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> = ctx.webhookPort.fetchWebhookMessages(this, limit, offset, since, until, inclusive, order)

/**
 * Webhook が投稿したメッセージを削除します。
 *
 * @param message 削除対象メッセージ
 */
context(ctx: UserContext)
suspend fun WebhookId.deleteMessage(message: MessageId) = ctx.webhookPort.deleteWebhookMessage(this, message)

/**
 * Webhook が投稿したメッセージを削除します。
 *
 * @param message 削除対象メッセージ
 */
context(ctx: UserContext)
suspend fun WebhookId.deleteMessage(message: Message) = deleteMessage(message.id)

/**
 * Webhook アイコンを取得します。
 *
 * @return アイコン画像のバイト列
 */
context(ctx: UserContext)
suspend fun WebhookId.downloadIcon(): ByteArray = ctx.webhookPort.downloadWebhookIcon(this)

/**
 * Webhook アイコンを変更します。
 *
 * @param file アイコン画像のバイト列
 * @param fileName ファイル名
 * @param contentType MIME タイプ。`null` の場合はサーバー側判定
 */
context(ctx: UserContext)
suspend fun WebhookId.changeIcon(
    file: ByteArray,
    fileName: String,
    contentType: String? = null,
) = ctx.webhookPort.changeWebhookIcon(this, file, fileName, contentType)

/**
 * Webhook を再取得します。
 *
 * @return 最新の Webhook 情報
 */
context(ctx: UserContext)
suspend fun Webhook.fetch(): Webhook = id.fetch()

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
    channel: ChannelId? = null,
    secret: String? = null,
    owner: UserId? = null,
) = id.update(name, description, channel, secret, owner)

/**
 * Webhook を編集します。
 *
 * @param name 新しい表示名。`null` の場合は変更しません
 * @param description 新しい説明。`null` の場合は変更しません
 * @param channel 新しいデフォルト投稿先
 * @param secret 新しいシークレット。`null` の場合は変更しません
 * @param owner 新しい所有者。`null` の場合は変更しません
 */
context(ctx: UserContext)
suspend fun Webhook.update(
    name: String? = null,
    description: String? = null,
    channel: Channel.PublicChannel,
    secret: String? = null,
    owner: UserId? = null,
) = id.update(name, description, channel, secret, owner)

/** Webhook を削除します。 */
context(ctx: UserContext)
suspend fun Webhook.delete() = id.delete()

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
    channel: ChannelId? = null,
    embed: Boolean = false,
) = id.send(content, signature, channel, embed)

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
    channel: Channel.PublicChannel,
    embed: Boolean = false,
) = id.send(content, signature, channel, embed)

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
suspend fun Webhook.fetchMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> = id.fetchMessages(limit, offset, since, until, inclusive, order)

/**
 * Webhook が投稿したメッセージを削除します。
 *
 * @param message 削除対象メッセージ
 */
context(ctx: UserContext)
suspend fun Webhook.deleteMessage(message: MessageId) = id.deleteMessage(message)

/**
 * Webhook が投稿したメッセージを削除します。
 *
 * @param message 削除対象メッセージ
 */
context(ctx: UserContext)
suspend fun Webhook.deleteMessage(message: Message) = id.deleteMessage(message)

/**
 * Webhook アイコンを取得します。
 *
 * @return アイコン画像のバイト列
 */
context(ctx: UserContext)
suspend fun Webhook.downloadIcon(): ByteArray = id.downloadIcon()

/**
 * Webhook アイコンを変更します。
 *
 * @param file アイコン画像のバイト列
 * @param fileName ファイル名
 * @param contentType MIME タイプ。`null` の場合はサーバー側判定
 */
context(ctx: UserContext)
suspend fun Webhook.changeIcon(
    file: ByteArray,
    fileName: String,
    contentType: String? = null,
) = id.changeIcon(file, fileName, contentType)
