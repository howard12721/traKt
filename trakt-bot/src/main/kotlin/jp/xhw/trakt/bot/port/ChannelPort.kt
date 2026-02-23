package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant

internal interface ChannelPort {
    /**
     * チャンネル一覧を取得します。
     *
     * @param includeDirectMessages `true` の場合は DM チャンネルも含めます
     * @param path 指定時は一致するパスのチャンネルに絞り込みます
     * @return チャンネル一覧
     */
    suspend fun fetchChannels(
        includeDirectMessages: Boolean = false,
        path: String? = null,
    ): ChannelDirectory

    /**
     * チャンネル詳細を取得します。
     *
     * @param channelId 取得対象チャンネル
     * @return チャンネル詳細
     */
    suspend fun fetchChannel(channelId: ChannelId): Channel.Detail

    /**
     * チャンネルのパスを取得します。
     *
     * @param channelId 取得対象チャンネル
     * @return チャンネルパス
     */
    suspend fun fetchChannelPath(channelId: ChannelId): ChannelPath

    /**
     * チャンネルトピックを取得します。
     *
     * @param channelId 取得対象チャンネル
     * @return チャンネルトピック
     */
    suspend fun fetchChannelTopic(channelId: ChannelId): String

    /**
     * チャンネルトピックを更新します。
     *
     * @param channelId 更新対象チャンネル
     * @param topic 新しいトピック
     */
    suspend fun setChannelTopic(
        channelId: ChannelId,
        topic: String,
    )

    /**
     * チャンネル購読者一覧を取得します。
     *
     * @param channelId 取得対象チャンネル
     * @return 購読ユーザーID一覧
     */
    suspend fun fetchSubscribers(channelId: ChannelId): List<UserId>

    /**
     * チャンネル購読者一覧を設定します。
     *
     * @param channelId 設定対象チャンネル
     * @param subscribers 購読ユーザーID一覧
     */
    suspend fun setSubscribers(
        channelId: ChannelId,
        subscribers: List<UserId>,
    )

    /**
     * チャンネル閲覧者一覧を取得します。
     *
     * @param channelId 取得対象チャンネル
     * @return 閲覧状態付きユーザー一覧
     */
    suspend fun fetchViewers(channelId: ChannelId): List<ChannelViewer>

    /**
     * チャンネルに参加中の Bot 一覧を取得します。
     *
     * @param channelId 取得対象チャンネル
     * @return Bot 一覧
     */
    suspend fun fetchBots(channelId: ChannelId): List<Bot>

    /**
     * チャンネルのピン留め一覧を取得します。
     *
     * @param channelId 取得対象チャンネル
     * @return ピン一覧
     */
    suspend fun fetchChannelPins(channelId: ChannelId): List<Pin>

    /**
     * チャンネル統計を取得します。
     *
     * @param channelId 取得対象チャンネル
     * @return チャンネル統計
     */
    suspend fun fetchChannelStats(channelId: ChannelId): ChannelStats

    /**
     * チャンネルのメッセージ一覧を取得します。
     *
     * @param channelId 取得対象チャンネル
     * @param limit 取得件数上限
     * @param offset 取得開始位置
     * @param since 指定時刻以降に絞り込みます
     * @param until 指定時刻以前に絞り込みます
     * @param inclusive 境界時刻を含めるかどうか
     * @param order 並び順
     * @return メッセージ一覧
     */
    suspend fun fetchMessages(
        channelId: ChannelId,
        limit: Int? = null,
        offset: Int = 0,
        since: Instant? = null,
        until: Instant? = null,
        inclusive: Boolean = false,
        order: SortDirection = SortDirection.DESCENDING,
    ): List<Message>

    /**
     * チャンネルへメッセージを送信します。
     *
     * @param channelId 送信先チャンネル
     * @param content 送信本文
     * @param embed 埋め込み展開を有効化するかどうか
     * @param nonce 重複送信防止に使う任意文字列
     * @return 送信されたメッセージ
     */
    suspend fun sendMessage(
        channelId: ChannelId,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): Message
}
