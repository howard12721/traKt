package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant
import kotlin.uuid.Uuid

// --- Fetch ---

/**
 * チャンネル詳細を取得します。
 *
 * @param channelId 取得対象チャンネルID
 * @return チャンネル詳細情報
 */
context(scope: BotScope)
suspend fun fetchChannel(channelId: ChannelId): Channel.Detail = scope.context.channelPort.fetchChannel(channelId)

/**
 * チャンネル詳細を取得します。
 *
 * @param channelId 取得対象チャンネルID(UUID)
 * @return チャンネル詳細情報
 */
context(scope: BotScope)
suspend fun fetchChannel(channelId: Uuid): Channel.Detail = fetchChannel(ChannelId(channelId))

/**
 * このハンドルが指すチャンネル詳細を取得します。
 *
 * @return チャンネル詳細情報
 */
context(scope: BotScope)
suspend fun ChannelHandle.resolve(): Channel.Detail = scope.context.channelPort.fetchChannel(id)

/**
 * チャンネルを詳細型として解決します。
 *
 * 既に [Channel.Detail] の場合はそのまま返し、そうでなければ API から再取得します。
 *
 * @return チャンネル詳細情報
 */
context(scope: BotScope)
suspend fun Channel.resolve(): Channel.Detail = this as? Channel.Detail ?: handle.resolve()

/**
 * チャンネル詳細を再取得します。
 *
 * @return 最新のチャンネル詳細情報
 */
context(scope: BotScope)
suspend fun Channel.Detail.refresh(): Channel.Detail = handle.resolve()

/**
 * チャンネル一覧を取得します。
 *
 * @param includeDirectMessages `true` の場合は DM チャンネルも含めます
 * @param path 指定した場合は一致するパスのチャンネルのみ取得します
 * @return チャンネル一覧
 */
context(scope: BotScope)
suspend fun fetchChannels(
    includeDirectMessages: Boolean = false,
    path: String? = null,
): ChannelDirectory = scope.context.channelPort.fetchChannels(includeDirectMessages, path)

/**
 * チャンネルのパスを取得します。
 *
 * @return チャンネルパス
 */
context(scope: BotScope)
suspend fun ChannelHandle.fetchPath(): ChannelPath = scope.context.channelPort.fetchChannelPath(id)

/**
 * チャンネルのトピックを取得します。
 *
 * @return チャンネルトピック文字列
 */
context(scope: BotScope)
suspend fun ChannelHandle.fetchTopic(): String = scope.context.channelPort.fetchChannelTopic(id)

/**
 * チャンネルのトピックを更新します。
 *
 * @param topic 新しいトピック文字列
 */
context(scope: BotScope)
suspend fun ChannelHandle.setTopic(topic: String) {
    scope.context.channelPort.setChannelTopic(id, topic)
}

/**
 * チャンネル購読者一覧を取得します。
 *
 * @return 購読ユーザーID一覧
 */
context(scope: BotScope)
suspend fun ChannelHandle.fetchSubscribers(): List<UserId> = scope.context.channelPort.fetchSubscribers(id)

/**
 * チャンネル購読者一覧を設定します。
 *
 * @param subscribers 設定する購読ユーザーID一覧
 */
context(scope: BotScope)
suspend fun ChannelHandle.setSubscribers(subscribers: List<UserId>) {
    scope.context.channelPort.setSubscribers(id, subscribers)
}

/**
 * チャンネル閲覧者一覧を取得します。
 *
 * @return 閲覧状態付きユーザー一覧
 */
context(scope: BotScope)
suspend fun ChannelHandle.fetchViewers(): List<ChannelViewer> = scope.context.channelPort.fetchViewers(id)

/**
 * チャンネルに参加中の Bot 一覧を取得します。
 *
 * @return Bot 一覧
 */
context(scope: BotScope)
suspend fun ChannelHandle.fetchBots(): List<Bot> = scope.context.channelPort.fetchBots(id)

/**
 * チャンネル購読者一覧を取得します。
 *
 * @return 購読ユーザーID一覧
 */
context(scope: BotScope)
suspend fun Channel.fetchSubscribers(): List<UserId> = handle.fetchSubscribers()

/**
 * チャンネル閲覧者一覧を取得します。
 *
 * @return 閲覧状態付きユーザー一覧
 */
context(scope: BotScope)
suspend fun Channel.fetchViewers(): List<ChannelViewer> = handle.fetchViewers()

/**
 * チャンネルに参加中の Bot 一覧を取得します。
 *
 * @return Bot 一覧
 */
context(scope: BotScope)
suspend fun Channel.fetchBots(): List<Bot> = handle.fetchBots()

/**
 * チャンネルにピン留めされたメッセージ一覧を取得します。
 *
 * @return ピン情報一覧
 */
context(scope: BotScope)
suspend fun ChannelHandle.fetchPins(): List<Pin> = scope.context.channelPort.fetchChannelPins(id)

/**
 * チャンネル統計情報を取得します。
 *
 * @return チャンネル統計情報
 */
context(scope: BotScope)
suspend fun ChannelHandle.fetchStats(): ChannelStats = scope.context.channelPort.fetchChannelStats(id)

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
context(scope: BotScope)
suspend fun ChannelHandle.fetchMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> = scope.context.channelPort.fetchMessages(id, limit, offset, since, until, inclusive, order)

// --- Send ---

/**
 * チャンネルへメッセージを送信します。
 *
 * @param content 送信本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 * @return 送信されたメッセージ
 */
context(scope: BotScope)
suspend fun ChannelHandle.sendMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = scope.context.channelPort.sendMessage(id, content, embed, nonce)

/**
 * チャンネルへメッセージを送信します。
 *
 * @param content 送信本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 * @return 送信されたメッセージ
 */
context(scope: BotScope)
suspend fun Channel.sendMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = handle.sendMessage(content, embed, nonce)
