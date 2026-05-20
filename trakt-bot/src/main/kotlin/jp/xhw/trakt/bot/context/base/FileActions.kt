package jp.xhw.trakt.bot.context.base

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant

/**
 * チャンネルへファイルをアップロードします。
 *
 * @param channelId アップロード先チャンネルID
 * @param file ファイル本体
 * @param fileName 保存時のファイル名
 * @param contentType MIME タイプ。`null` の場合はサーバー側判定
 * @return アップロードされたファイルのメタ情報
 */
context(ctx: BaseContext)
suspend fun uploadFile(
    channelId: ChannelId,
    file: ByteArray,
    fileName: String,
    contentType: String? = null,
): FileMeta = ctx.filePort.uploadFile(channelId, file, fileName, contentType)

/**
 * ファイルメタ情報を取得します。
 *
 * @param fileId 取得対象ファイルID
 * @return ファイルメタ情報
 */
context(ctx: BaseContext)
suspend fun fetchFileMeta(fileId: FileId): FileMeta = ctx.filePort.fetchFileMeta(fileId)

/**
 * ファイルメタ情報を取得します。存在しない場合は `null` を返します。
 *
 * @param fileId 取得対象ファイルID
 * @return ファイルメタ情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun fetchFileMetaOrNull(fileId: FileId): FileMeta? = ctx.filePort.fetchFileMetaOrNull(fileId)

/**
 * ファイルメタ情報を取得します。
 *
 * @return 最新のファイルメタ情報
 */
context(ctx: BaseContext)
suspend fun File.fetch(): FileMeta = ctx.filePort.fetchFileMeta(id)

/**
 * ファイル本体をダウンロードします。
 *
 * @return ファイル本体
 */
context(ctx: BaseContext)
suspend fun File.download(): ByteArray = ctx.filePort.downloadFile(id)

/** ファイルを削除します。 */
context(ctx: BaseContext)
suspend fun File.delete() {
    ctx.filePort.deleteFile(id)
}

/**
 * ファイルのURLを生成します。
 *
 * @return ファイルのURL
 */
context(ctx: BaseContext)
fun File.url(): String = "https://${ctx.origin}/files/${id.value}"

/**
 * ファイルメタのリストを取得します。
 *
 * @param channelId アップロード先チャンネルID。`null` の場合は絞り込みません
 * @param mine アップロード者が自分のファイルのみを取得するか
 * @param limit 取得件数上限。`null` の場合はサーバーデフォルト
 * @param offset 取得開始位置
 * @param since 指定時刻以降にアップロードされたファイルに絞り込みます
 * @param until 指定時刻以前にアップロードされたファイルに絞り込みます
 * @param inclusive `true` の場合は境界時刻と一致するファイルを含めます
 * @param order 並び順
 * @return ファイルメタ一覧
 */
context(ctx: BaseContext)
suspend fun fetchFiles(
    channelId: ChannelId? = null,
    mine: Boolean = false,
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<FileMeta> = ctx.filePort.fetchFiles(channelId, mine, limit, offset, since, until, inclusive, order)

/**
 * ファイルのサムネイル画像をダウンロードします。
 *
 * @param type サムネイルのタイプ
 * @return サムネイル画像のバイト列
 */
context(ctx: BaseContext)
suspend fun File.downloadThumbnail(type: ThumbnailType = ThumbnailType.IMAGE): ByteArray = ctx.filePort.fetchThumbnail(id, type)
