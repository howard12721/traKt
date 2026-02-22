package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.FileId
import jp.xhw.trakt.bot.model.FileMeta

internal interface FilePort {
    suspend fun uploadFile(
        channelId: ChannelId,
        file: ByteArray,
        fileName: String,
        contentType: String? = null,
    ): FileMeta

    suspend fun fetchFileMeta(fileId: FileId): FileMeta

    suspend fun downloadFile(fileId: FileId): ByteArray

    suspend fun deleteFile(fileId: FileId)
}
