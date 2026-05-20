package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.bot.port.FilePort
import jp.xhw.trakt.rest.apis.FileApi
import kotlin.time.Instant

internal class TraqFilePort(
    private val apiGateway: TraqApiGateway,
) : FilePort {
    override suspend fun uploadFile(
        channelId: ChannelId,
        file: ByteArray,
        fileName: String,
        contentType: String?,
    ): FileMeta {
        val filePart = file.toFilePart(fileName, contentType)
        val response =
            apiGateway.fileApi.postFile(
                file = filePart,
                channelId = channelId.value,
            )
        return response.bodyOrThrow(operation = "uploadFile(channelId=${channelId.value})").toModel()
    }

    override suspend fun fetchFileMeta(fileId: FileId): FileMeta {
        val response = apiGateway.fileApi.getFileMeta(fileId.value)
        return response.bodyOrThrow(operation = "fetchFileMeta(fileId=${fileId.value})").toModel()
    }

    override suspend fun fetchFileMetaOrNull(fileId: FileId): FileMeta? {
        val response = apiGateway.fileApi.getFileMeta(fileId.value)
        return response.bodyOrNullIfNotFound(operation = "fetchFileMetaOrNull(fileId=${fileId.value})")?.toModel()
    }

    override suspend fun downloadFile(fileId: FileId): ByteArray {
        val response = apiGateway.fileApi.getFile(fileId.value)
        return response.bodyOrThrow(operation = "downloadFile(fileId=${fileId.value})").value
    }

    override suspend fun deleteFile(fileId: FileId) {
        apiGateway.fileApi.deleteFile(fileId.value).requireSuccess(operation = "deleteFile(fileId=${fileId.value})")
    }

    override suspend fun fetchFiles(
        channelId: ChannelId?,
        mine: Boolean,
        limit: Int?,
        offset: Int,
        since: Instant?,
        until: Instant?,
        inclusive: Boolean,
        order: SortDirection,
    ): List<FileMeta> {
        val response =
            apiGateway.fileApi.getFiles(
                channelId = channelId?.value,
                mine = mine,
                limit = limit,
                offset = offset,
                since = since,
                until = until,
                inclusive = inclusive,
                order = order.toApiModel(),
            )
        return response.bodyOrThrow(operation = "fetchFiles(channelId=$channelId, mine=$mine)").map { it.toModel() }
    }

    override suspend fun fetchThumbnail(
        fileId: FileId,
        type: ThumbnailType,
    ): ByteArray {
        val response = apiGateway.fileApi.getThumbnailImage(fileId.value, type = type.toApiModel())
        return response.bodyOrThrow(operation = "fetchThumbnail(fileId=${fileId.value}, type=$type)").value
    }
}

private fun SortDirection.toApiModel(): FileApi.OrderGetFiles =
    when (this) {
        SortDirection.ASCENDING -> FileApi.OrderGetFiles.ASC
        SortDirection.DESCENDING -> FileApi.OrderGetFiles.DESC
    }

private fun ThumbnailType.toApiModel(): jp.xhw.trakt.rest.models.ThumbnailType =
    when (this) {
        ThumbnailType.IMAGE -> jp.xhw.trakt.rest.models.ThumbnailType.IMAGE
        ThumbnailType.WAVEFORM -> jp.xhw.trakt.rest.models.ThumbnailType.WAVEFORM
    }
