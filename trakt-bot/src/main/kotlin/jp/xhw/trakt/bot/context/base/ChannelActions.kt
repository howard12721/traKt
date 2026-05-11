package jp.xhw.trakt.bot.context.base

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant

// --- Fetch ---

/**
 * チャンネル詳細を取得します。
 *
 * @param channelId 取得対象チャンネルID
 * @return チャンネル詳細情報
 */
context(ctx: BaseContext)
suspend fun fetchChannel(channelId: ChannelId): Channel.Detail = ctx.channelPort.fetchChannel(channelId)

/**
 * チャンネル詳細を取得します。存在しない場合は `null` を返します。
 *
 * ユーザー入力など、存在が保証できない ID を扱う場合に使います。
 *
 * @param channelId 取得対象チャンネルID
 * @return チャンネル詳細情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun fetchChannelOrNull(channelId: ChannelId): Channel.Detail? = ctx.channelPort.fetchChannelOrNull(channelId)

/**
 * この ID が指すチャンネル詳細を取得します。
 *
 * @return チャンネル詳細情報
 */
context(ctx: BaseContext)
suspend fun ChannelId.fetch(): Channel.Detail = ctx.channelPort.fetchChannel(this)

/**
 * この ID が指すチャンネル詳細を取得します。存在しない場合は `null` を返します。
 *
 * ユーザー入力など、存在が保証できない ID を扱う場合に使います。
 *
 * @return チャンネル詳細情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun ChannelId.fetchOrNull(): Channel.Detail? = ctx.channelPort.fetchChannelOrNull(this)

/**
 * チャンネル詳細を取得します。
 *
 * @return チャンネル詳細情報
 */
context(ctx: BaseContext)
suspend fun Channel.fetch(): Channel.Detail = id.fetch()

/**
 * チャンネル詳細を取得します。存在しない場合は `null` を返します。
 *
 * @return チャンネル詳細情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun Channel.fetchOrNull(): Channel.Detail? = id.fetchOrNull()

/**
 * チャンネル一覧を取得します。
 *
 * @param includeDirectMessages `true` の場合は DM チャンネルも含めます
 * @param path 指定した場合は一致するパスのチャンネルのみ取得します
 * @return チャンネル一覧
 */
context(ctx: BaseContext)
suspend fun fetchChannels(
    includeDirectMessages: Boolean = false,
    path: String? = null,
): ChannelDirectory = ctx.channelPort.fetchChannels(includeDirectMessages, path)

/**
 * チャンネルのパスを取得します。
 *
 * @return チャンネルパス
 */
context(ctx: BaseContext)
suspend fun ChannelId.fetchPath(): ChannelPath = ctx.channelPort.fetchChannelPath(this)

/**
 * チャンネルのパスを取得します。
 *
 * @return チャンネルパス
 */
context(ctx: BaseContext)
suspend fun Channel.PublicChannel.fetchPath(): ChannelPath = id.fetchPath()

/**
 * チャンネルのトピックを取得します。
 *
 * @return チャンネルトピック文字列
 */
context(ctx: BaseContext)
suspend fun ChannelId.fetchTopic(): String = ctx.channelPort.fetchChannelTopic(this)

/**
 * チャンネルのトピックを取得します。
 *
 * @return チャンネルトピック文字列
 */
context(ctx: BaseContext)
suspend fun Channel.PublicChannel.fetchTopic(): String = id.fetchTopic()

/**
 * チャンネルのトピックを更新します。
 *
 * @param topic 新しいトピック文字列
 */
context(ctx: BaseContext)
suspend fun ChannelId.setTopic(topic: String) {
    ctx.channelPort.setChannelTopic(this, topic)
}

/**
 * チャンネルのトピックを更新します。
 *
 * @param topic 新しいトピック文字列
 */
context(ctx: BaseContext)
suspend fun Channel.PublicChannel.setTopic(topic: String) {
    id.setTopic(topic)
}

/**
 * チャンネル購読者一覧を取得します。
 *
 * @return 購読ユーザーID一覧
 */
context(ctx: BaseContext)
suspend fun ChannelId.fetchSubscribers(): List<UserId> = ctx.channelPort.fetchSubscribers(this)

/**
 * チャンネル購読者一覧を設定します。
 *
 * @param subscribers 設定する購読ユーザーID一覧
 */
context(ctx: BaseContext)
suspend fun ChannelId.setSubscribers(subscribers: List<UserId>) {
    ctx.channelPort.setSubscribers(this, subscribers)
}

/**
 * チャンネル購読者一覧を設定します。
 *
 * @param subscribers 設定する購読ユーザーID一覧
 */
context(ctx: BaseContext)
suspend fun Channel.PublicChannel.setSubscribers(subscribers: List<UserId>) {
    id.setSubscribers(subscribers)
}

/**
 * チャンネル閲覧者一覧を取得します。
 *
 * @return 閲覧状態付きユーザー一覧
 */
context(ctx: BaseContext)
suspend fun ChannelId.fetchViewers(): List<ChannelViewer> = ctx.channelPort.fetchViewers(this)

/**
 * チャンネルに参加中の Bot 一覧を取得します。
 *
 * @return Bot 一覧
 */
context(ctx: BaseContext)
suspend fun ChannelId.fetchBots(): List<Bot> = ctx.channelPort.fetchBots(this)

/**
 * チャンネル購読者一覧を取得します。
 *
 * @return 購読ユーザーID一覧
 */
context(ctx: BaseContext)
suspend fun Channel.fetchSubscribers(): List<UserId> = id.fetchSubscribers()

/**
 * チャンネル閲覧者一覧を取得します。
 *
 * @return 閲覧状態付きユーザー一覧
 */
context(ctx: BaseContext)
suspend fun Channel.fetchViewers(): List<ChannelViewer> = id.fetchViewers()

/**
 * チャンネルに参加中の Bot 一覧を取得します。
 *
 * @return Bot 一覧
 */
context(ctx: BaseContext)
suspend fun Channel.fetchBots(): List<Bot> = id.fetchBots()

/**
 * チャンネルにピン留めされたメッセージ一覧を取得します。
 *
 * @return ピン情報一覧
 */
context(ctx: BaseContext)
suspend fun ChannelId.fetchPins(): List<Pin> = ctx.channelPort.fetchChannelPins(this)

/**
 * チャンネルにピン留めされたメッセージ一覧を取得します。
 *
 * @return ピン情報一覧
 */
context(ctx: BaseContext)
suspend fun Channel.fetchPins(): List<Pin> = id.fetchPins()

/**
 * チャンネル統計情報を取得します。
 *
 * @return チャンネル統計情報
 */
context(ctx: BaseContext)
suspend fun ChannelId.fetchStats(): ChannelStats = ctx.channelPort.fetchChannelStats(this)

/**
 * チャンネル統計情報を取得します。
 *
 * @return チャンネル統計情報
 */
context(ctx: BaseContext)
suspend fun Channel.fetchStats(): ChannelStats = id.fetchStats()

/**
 * チャンネルのメッセージ一覧を取得します。
 *
 * @param limit 取得件数上限。`null` の場合はサーバーデフォルト
 * @param offset 取得開始位置
 * @param since 指定時刻以降のメッセージに絞り込みます
 * @param until 指定時刻以前のメッセージに絞り込みます
 * @param inclusive `true` の場合は境界時刻と一致するメッセージを含めます
 * @param order 並び順
 * @return メッセージ一覧
 */
context(ctx: BaseContext)
suspend fun ChannelId.fetchMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> = ctx.channelPort.fetchMessages(this, limit, offset, since, until, inclusive, order)

/**
 * チャンネルのメッセージ一覧を取得します。
 *
 * @param limit 取得件数上限。`null` の場合はサーバーデフォルト
 * @param offset 取得開始位置
 * @param since 指定時刻以降のメッセージに絞り込みます
 * @param until 指定時刻以前のメッセージに絞り込みます
 * @param inclusive `true` の場合は境界時刻と一致するメッセージを含めます
 * @param order 並び順
 * @return メッセージ一覧
 */
context(ctx: BaseContext)
suspend fun Channel.fetchMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> = id.fetchMessages(limit, offset, since, until, inclusive, order)

// --- Send ---

/**
 * チャンネルへメッセージを送信します。
 *
 * @param content 送信本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 * @return 送信されたメッセージ
 */
context(ctx: BaseContext)
suspend fun ChannelId.sendMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = ctx.channelPort.sendMessage(this, content, embed, nonce)

/**
 * チャンネルへメッセージを送信します。
 *
 * @param content 送信本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 * @return 送信されたメッセージ
 */
context(ctx: BaseContext)
suspend fun Channel.sendMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = id.sendMessage(content, embed, nonce)

/**
 * パスからチャンネルを取得します。
 *
 * @param path 取得対象チャンネルのパス
 */
context(ctx: BaseContext)
suspend fun fetchChannelByPath(path: ChannelPath): Channel.Detail? = fetchChannels(path = path.value).publicChannels.firstOrNull()
