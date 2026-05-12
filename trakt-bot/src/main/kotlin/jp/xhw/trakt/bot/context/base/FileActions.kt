package jp.xhw.trakt.bot.context.base

import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.File
import jp.xhw.trakt.bot.model.FileId
import jp.xhw.trakt.bot.model.FileMeta

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
