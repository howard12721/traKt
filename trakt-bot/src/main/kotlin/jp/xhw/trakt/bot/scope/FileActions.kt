package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.ChannelHandle
import jp.xhw.trakt.bot.model.FileHandle
import jp.xhw.trakt.bot.model.FileMeta

/**
 * チャンネルへファイルをアップロードします。
 *
 * @param channelHandle アップロード先チャンネル
 * @param file ファイル本体
 * @param fileName 保存時のファイル名
 * @param contentType MIME タイプ。`null` の場合はサーバー側判定
 * @return アップロードされたファイルのメタ情報
 */
context(scope: BotScope)
suspend fun uploadFile(
    channelHandle: ChannelHandle,
    file: ByteArray,
    fileName: String,
    contentType: String? = null,
): FileMeta = scope.context.filePort.uploadFile(channelHandle.id, file, fileName, contentType)

/**
 * ファイルメタ情報を取得します。
 *
 * @return ファイルメタ情報
 */
context(scope: BotScope)
suspend fun FileHandle.fetchMeta(): FileMeta = scope.context.filePort.fetchFileMeta(id)

/**
 * ファイル本体をダウンロードします。
 *
 * @return ファイル本体
 */
context(scope: BotScope)
suspend fun FileHandle.download(): ByteArray = scope.context.filePort.downloadFile(id)

/**
 * ファイル本体をダウンロードします。
 *
 * @return ファイル本体
 */
context(scope: BotScope)
suspend fun FileMeta.download(): ByteArray = handle.download()

/** ファイルを削除します。 */
context(scope: BotScope)
suspend fun FileHandle.delete() {
    scope.context.filePort.deleteFile(id)
}

/** ファイルを削除します。 */
context(scope: BotScope)
suspend fun FileMeta.delete() = handle.delete()

/**
 * ファイルメタ情報を再取得します。
 *
 * @return 最新のファイルメタ情報
 */
context(scope: BotScope)
suspend fun FileMeta.refresh(): FileMeta = handle.fetchMeta()
