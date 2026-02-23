package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant

internal interface MessagePort {
    /**
     * メッセージ詳細を取得します。
     *
     * @param messageId 取得対象メッセージ
     * @return メッセージ情報
     */
    suspend fun fetchMessage(messageId: MessageId): Message

    /**
     * メッセージ本文を更新します。
     *
     * @param messageId 更新対象メッセージ
     * @param content 更新後本文
     * @param embed 埋め込み展開を有効化するかどうか
     * @param nonce 重複送信防止に使う任意文字列
     */
    suspend fun editMessage(
        messageId: MessageId,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    )

    /**
     * メッセージを削除します。
     *
     * @param messageId 削除対象メッセージ
     */
    suspend fun deleteMessage(messageId: MessageId)

    /**
     * メッセージへスタンプを追加します。
     *
     * @param messageId 対象メッセージ
     * @param stampId 追加するスタンプ
     * @param count 追加個数
     */
    suspend fun addStamp(
        messageId: MessageId,
        stampId: StampId,
        count: Int = 1,
    )

    /**
     * メッセージからスタンプを削除します。
     *
     * @param messageId 対象メッセージ
     * @param stampId 削除するスタンプ
     */
    suspend fun removeStamp(
        messageId: MessageId,
        stampId: StampId,
    )

    /**
     * メッセージのスタンプ一覧を取得します。
     *
     * @param messageId 取得対象メッセージ
     * @return スタンプ情報一覧
     */
    suspend fun fetchStamps(messageId: MessageId): List<MessageStamp>

    /**
     * メッセージのピン情報を取得します。
     *
     * @param messageId 取得対象メッセージ
     * @return ピン情報
     */
    suspend fun fetchPinInfo(messageId: MessageId): PinInfo

    /**
     * メッセージをピン留めします。
     *
     * @param messageId 対象メッセージ
     * @return 作成されたピン情報
     */
    suspend fun createPin(messageId: MessageId): PinInfo

    /**
     * メッセージのピン留めを解除します。
     *
     * @param messageId 対象メッセージ
     */
    suspend fun removePin(messageId: MessageId)

    /**
     * ユーザーへ DM を送信します。
     *
     * @param userId 送信先ユーザー
     * @param content 送信本文
     * @param embed 埋め込み展開を有効化するかどうか
     * @param nonce 重複送信防止に使う任意文字列
     * @return 送信されたメッセージ
     */
    suspend fun sendDirectMessage(
        userId: UserId,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): Message

    /**
     * ユーザーとの DM メッセージを取得します。
     *
     * @param userId 対象ユーザー
     * @param limit 取得件数上限
     * @param offset 取得開始位置
     * @param since 指定時刻以降に絞り込みます
     * @param until 指定時刻以前に絞り込みます
     * @param inclusive 境界時刻を含めるかどうか
     * @param order 並び順
     * @return メッセージ一覧
     */
    suspend fun fetchDirectMessages(
        userId: UserId,
        limit: Int? = null,
        offset: Int = 0,
        since: Instant? = null,
        until: Instant? = null,
        inclusive: Boolean = false,
        order: SortDirection = SortDirection.DESCENDING,
    ): List<Message>

    /**
     * メッセージを検索します。
     *
     * @param word 検索ワード
     * @param after 投稿日時がこの時刻より後
     * @param before 投稿日時がこの時刻より前
     * @param inChannel 投稿チャンネル
     * @param to メンション先ユーザー
     * @param from 投稿ユーザー
     * @param citation 引用元メッセージ
     * @param bot 投稿者が Bot かどうか
     * @param hasUrl URL を含むかどうか
     * @param hasAttachments 添付ファイルを含むかどうか
     * @param hasImage 画像を含むかどうか
     * @param hasVideo 動画を含むかどうか
     * @param hasAudio 音声を含むかどうか
     * @param limit 取得件数上限
     * @param offset 取得開始位置
     * @param sort 並び順
     * @return 検索結果
     */
    suspend fun searchMessages(
        word: String? = null,
        after: Instant? = null,
        before: Instant? = null,
        inChannel: ChannelId? = null,
        to: List<UserId>? = null,
        from: List<UserId>? = null,
        citation: MessageId? = null,
        bot: Boolean? = null,
        hasUrl: Boolean? = null,
        hasAttachments: Boolean? = null,
        hasImage: Boolean? = null,
        hasVideo: Boolean? = null,
        hasAudio: Boolean? = null,
        limit: Int? = null,
        offset: Int? = null,
        sort: SortDirection = SortDirection.DESCENDING,
    ): SearchResult
}
