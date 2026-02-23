package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant
import kotlin.uuid.Uuid

// --- Fetch ---

/**
 * メッセージ詳細を取得します。
 *
 * @param messageId 取得対象メッセージID
 * @return メッセージ情報
 */
context(scope: BotScope)
suspend fun fetchMessage(messageId: MessageId): Message = scope.context.messagePort.fetchMessage(messageId)

/**
 * メッセージ詳細を取得します。
 *
 * @param messageId 取得対象メッセージID(UUID)
 * @return メッセージ情報
 */
context(scope: BotScope)
suspend fun fetchMessage(messageId: Uuid): Message = fetchMessage(MessageId(messageId))

/**
 * このハンドルが指すメッセージを取得します。
 *
 * @return メッセージ情報
 */
context(scope: BotScope)
suspend fun MessageHandle.resolve(): Message = scope.context.messagePort.fetchMessage(id)

/**
 * メッセージに付与されたスタンプ一覧を取得します。
 *
 * @return スタンプ情報一覧
 */
context(scope: BotScope)
suspend fun MessageHandle.fetchStamps(): List<MessageStamp> = scope.context.messagePort.fetchStamps(id)

/**
 * メッセージのピン情報を取得します。
 *
 * @return ピン情報
 */
context(scope: BotScope)
suspend fun MessageHandle.fetchPinInfo(): PinInfo = scope.context.messagePort.fetchPinInfo(id)

// --- Edit / Delete ---

/**
 * メッセージ本文を更新します。
 *
 * @param content 更新後本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 */
context(scope: BotScope)
suspend fun MessageHandle.update(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
) = scope.context.messagePort.editMessage(id, content, embed, nonce)

/** メッセージを削除します。 */
context(scope: BotScope)
suspend fun MessageHandle.delete() = scope.context.messagePort.deleteMessage(id)

// --- Stamps ---

/**
 * メッセージへスタンプを追加します。
 *
 * @param stampId 付与するスタンプID
 * @param count 追加個数
 */
context(scope: BotScope)
suspend fun MessageHandle.stamp(
    stampId: StampId,
    count: Int = 1,
) = scope.context.messagePort.addStamp(id, stampId, count)

/**
 * メッセージからスタンプを削除します。
 *
 * @param stampId 削除するスタンプID
 */
context(scope: BotScope)
suspend fun MessageHandle.unstamp(stampId: StampId) = scope.context.messagePort.removeStamp(id, stampId)

// --- Pins ---

/**
 * メッセージをピン留めします。
 *
 * @return 作成されたピン情報
 */
context(scope: BotScope)
suspend fun MessageHandle.pin(): PinInfo = scope.context.messagePort.createPin(id)

/** メッセージのピン留めを解除します。 */
context(scope: BotScope)
suspend fun MessageHandle.unpin() = scope.context.messagePort.removePin(id)

// --- Search ---

/**
 * メッセージを検索します。
 *
 * @param word 検索ワード（Simple Query String 構文）
 * @param after 投稿日時がこの時刻より後のメッセージに絞り込みます
 * @param before 投稿日時がこの時刻より前のメッセージに絞り込みます
 * @param inChannel 指定チャンネル内のメッセージに絞り込みます
 * @param to 指定ユーザーへのメンションを含むメッセージに絞り込みます
 * @param from 指定ユーザーが投稿したメッセージに絞り込みます
 * @param citation 指定メッセージを引用しているメッセージに絞り込みます
 * @param bot 投稿者が Bot かどうかで絞り込みます
 * @param hasUrl URL を含むかどうかで絞り込みます
 * @param hasAttachments 添付ファイルを含むかどうかで絞り込みます
 * @param hasImage 画像を含むかどうかで絞り込みます
 * @param hasVideo 動画を含むかどうかで絞り込みます
 * @param hasAudio 音声を含むかどうかで絞り込みます
 * @param limit 取得する最大件数
 * @param offset 取得開始位置
 * @param sort 並び順
 * @return 検索結果
 */
context(scope: BotScope)
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
): SearchResult =
    scope.context.messagePort.searchMessages(
        word,
        after,
        before,
        inChannel,
        to,
        from,
        citation,
        bot,
        hasUrl,
        hasAttachments,
        hasImage,
        hasVideo,
        hasAudio,
        limit,
        offset,
        sort,
    )

// --- Reply ---

/**
 * 元メッセージへのリンク付きで返信します。
 *
 * 本文末尾に対象メッセージ URL を付加して送信します。
 *
 * @param content 返信本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 * @return 送信された返信メッセージ
 */
context(scope: BotScope)
suspend fun MessageHandle.reply(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = resolve().channel.sendMessage(content + "\n${url()}", embed, nonce)

// --- Message convenience extensions ---

/**
 * メッセージ情報を再取得します。
 *
 * @return 最新のメッセージ情報
 */
context(scope: BotScope)
suspend fun Message.refresh(): Message = handle.resolve()

/**
 * メッセージに付与されたスタンプ一覧を取得します。
 *
 * @return スタンプ情報一覧
 */
context(scope: BotScope)
suspend fun Message.fetchStamps(): List<MessageStamp> = handle.fetchStamps()

/**
 * メッセージのピン情報を取得します。
 *
 * @return ピン情報
 */
context(scope: BotScope)
suspend fun Message.fetchPinInfo(): PinInfo = handle.fetchPinInfo()

/**
 * メッセージ本文を更新します。
 *
 * @param content 更新後本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 */
context(scope: BotScope)
suspend fun Message.update(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
) = handle.update(content, embed, nonce)

/** メッセージを削除します。 */
context(scope: BotScope)
suspend fun Message.delete() = handle.delete()

/**
 * メッセージへスタンプを追加します。
 *
 * @param stampId 付与するスタンプID
 * @param count 追加個数
 */
context(scope: BotScope)
suspend fun Message.stamp(
    stampId: StampId,
    count: Int = 1,
) = handle.stamp(stampId, count)

/**
 * メッセージからスタンプを削除します。
 *
 * @param stampId 削除するスタンプID
 */
context(scope: BotScope)
suspend fun Message.unstamp(stampId: StampId) = handle.unstamp(stampId)

/**
 * メッセージをピン留めします。
 *
 * @return 作成されたピン情報
 */
context(scope: BotScope)
suspend fun Message.pin(): PinInfo = handle.pin()

/** メッセージのピン留めを解除します。 */
context(scope: BotScope)
suspend fun Message.unpin() = handle.unpin()

/**
 * 元メッセージへのリンク付きで返信します。
 *
 * 本文末尾に対象メッセージ URL を付加して送信します。
 *
 * @param content 返信本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 * @return 送信された返信メッセージ
 */
context(scope: BotScope)
suspend fun Message.reply(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = channel.sendMessage(content + "\n${url()}", embed, nonce)

/** メッセージ URL を生成します。 */
context(scope: BotScope)
private fun MessageHandle.url(): String = "https://${scope.context.origin}/messages/${id.value}"

/** メッセージ URL を生成します。 */
context(scope: BotScope)
private fun Message.url(): String = handle.url()
