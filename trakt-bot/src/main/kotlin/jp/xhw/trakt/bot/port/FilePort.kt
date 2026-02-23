package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.FileId
import jp.xhw.trakt.bot.model.FileMeta

internal interface FilePort {
    /**
     * ファイルをアップロードします。
     *
     * @param channelId アップロード先チャンネル
     * @param file ファイル本体
     * @param fileName 保存ファイル名
     * @param contentType MIME タイプ
     * @return アップロード後のファイルメタ情報
     */
    suspend fun uploadFile(
        channelId: ChannelId,
        file: ByteArray,
        fileName: String,
        contentType: String? = null,
    ): FileMeta

    /**
     * ファイルメタ情報を取得します。
     *
     * @param fileId 取得対象ファイル
     * @return ファイルメタ情報
     */
    suspend fun fetchFileMeta(fileId: FileId): FileMeta

    /**
     * ファイル本体をダウンロードします。
     *
     * @param fileId 取得対象ファイル
     * @return ファイル本体
     */
    suspend fun downloadFile(fileId: FileId): ByteArray

    /**
     * ファイルを削除します。
     *
     * @param fileId 削除対象ファイル
     */
    suspend fun deleteFile(fileId: FileId)
}
