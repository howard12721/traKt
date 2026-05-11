package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.FileId
import jp.xhw.trakt.bot.model.FileMeta
import jp.xhw.trakt.bot.port.FilePort

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
}
