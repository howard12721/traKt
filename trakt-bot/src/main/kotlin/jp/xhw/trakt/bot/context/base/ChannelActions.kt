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
 * @param channelId 取得対象チャンネルID
 * @return チャンネル詳細情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun fetchChannelOrNull(channelId: ChannelId): Channel.Detail? = ctx.channelPort.fetchChannelOrNull(channelId)

/**
 * チャンネル詳細を取得します。
 *
 * @return チャンネル詳細情報
 */
context(ctx: BaseContext)
suspend fun Channel.fetch(): Channel.Detail = ctx.channelPort.fetchChannel(id)

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
suspend fun Channel.fetchPath(): ChannelPath = ctx.channelPort.fetchChannelPath(id)

/**
 * チャンネルのトピックを取得します。
 *
 * @return チャンネルトピック文字列
 */
context(ctx: BaseContext)
suspend fun Channel.fetchTopic(): String = ctx.channelPort.fetchChannelTopic(id)

/**
 * チャンネルのトピックを更新します。
 *
 * @param topic 新しいトピック文字列
 */
context(ctx: BaseContext)
suspend fun Channel.setTopic(topic: String) {
    ctx.channelPort.setChannelTopic(id, topic)
}

/**
 * チャンネル購読者一覧を取得します。
 *
 * @return 購読ユーザー参照一覧
 */
context(ctx: BaseContext)
suspend fun Channel.fetchSubscribers(): List<User.Ref> = ctx.channelPort.fetchSubscribers(id)

/**
 * チャンネル購読者一覧を設定します。
 *
 * @param subscribers 設定する購読ユーザーID一覧
 */
context(ctx: BaseContext)
suspend fun Channel.setSubscribers(subscribers: List<UserId>) {
    ctx.channelPort.setSubscribers(id, subscribers)
}

/**
 * チャンネル閲覧者一覧を取得します。
 *
 * @return 閲覧状態付きユーザー一覧
 */
context(ctx: BaseContext)
suspend fun Channel.fetchViewers(): List<ChannelViewer> = ctx.channelPort.fetchViewers(id)

/**
 * チャンネルに参加中の Bot 一覧を取得します。
 *
 * @return Bot 一覧
 */
context(ctx: BaseContext)
suspend fun Channel.fetchBots(): List<Bot> = ctx.channelPort.fetchBots(id)

/**
 * チャンネルにピン留めされたメッセージ一覧を取得します。
 *
 * @return ピン情報一覧
 */
context(ctx: BaseContext)
suspend fun Channel.fetchPins(): List<Pin> = ctx.channelPort.fetchChannelPins(id)

/**
 * チャンネル統計情報を取得します。
 *
 * @return チャンネル統計情報
 */
context(ctx: BaseContext)
suspend fun Channel.fetchStats(): ChannelStats = ctx.channelPort.fetchChannelStats(id)

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
): List<Message.Detail> = ctx.channelPort.fetchMessages(id, limit, offset, since, until, inclusive, order)

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
suspend fun Channel.sendMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message.Detail = ctx.channelPort.sendMessage(id, content, embed, nonce)

/**
 * パスからチャンネルを取得します。
 *
 * @param path 取得対象チャンネルのパス
 */
context(ctx: BaseContext)
suspend fun fetchChannelByPath(path: ChannelPath): Channel.Detail? = fetchChannels(path = path.value).publicChannels.firstOrNull()

/**
 * チャンネルを作成します。
 *
 * @param name チャンネル名
 * @param parent 親チャンネルID。`null` の場合はルートに作成します
 * @return 作成されたチャンネル詳細
 */
context(ctx: BaseContext)
suspend fun createChannel(
    name: String,
    parent: ChannelId? = null,
): Channel.Detail = ctx.channelPort.createChannel(name, parent)

/**
 * チャンネルの通知購読者を増分編集します。
 *
 * @param on 通知をオンにするユーザーID一覧
 * @param off 通知をオフにするユーザーID一覧
 */
context(ctx: BaseContext)
suspend fun Channel.editSubscribers(
    on: List<UserId> = emptyList(),
    off: List<UserId> = emptyList(),
) {
    ctx.channelPort.editChannelSubscribers(id, on, off)
}
