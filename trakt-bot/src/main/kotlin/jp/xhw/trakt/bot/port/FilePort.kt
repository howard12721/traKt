package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant

internal interface FilePort {
    suspend fun uploadFile(
        channelId: ChannelId,
        file: ByteArray,
        fileName: String,
        contentType: String? = null,
    ): FileMeta

    suspend fun fetchFileMeta(fileId: FileId): FileMeta

    suspend fun fetchFileMetaOrNull(fileId: FileId): FileMeta?

    suspend fun downloadFile(fileId: FileId): ByteArray

    suspend fun deleteFile(fileId: FileId)

    suspend fun fetchFiles(
        channelId: ChannelId? = null,
        mine: Boolean = false,
        limit: Int? = null,
        offset: Int = 0,
        since: Instant? = null,
        until: Instant? = null,
        inclusive: Boolean = false,
        order: SortDirection = SortDirection.DESCENDING,
    ): List<FileMeta>

    suspend fun fetchThumbnail(
        fileId: FileId,
        type: ThumbnailType = ThumbnailType.IMAGE,
    ): ByteArray
}
