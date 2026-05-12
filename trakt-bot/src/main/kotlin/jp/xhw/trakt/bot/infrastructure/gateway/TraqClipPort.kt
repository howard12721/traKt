package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.bot.port.ClipPort
import jp.xhw.trakt.rest.apis.ClipApi
import jp.xhw.trakt.rest.models.PatchClipFolderRequest
import jp.xhw.trakt.rest.models.PostClipFolderMessageRequest
import jp.xhw.trakt.rest.models.PostClipFolderRequest

internal class TraqClipPort(
    private val apiGateway: TraqApiGateway,
) : ClipPort {
    override suspend fun createFolder(
        name: String,
        description: String,
    ): ClipFolder.Detail =
        apiGateway.clipApi
            .createClipFolder(PostClipFolderRequest(name = name, description = description))
            .bodyOrThrow(operation = "createClipFolder(name=$name)")
            .toModel()

    override suspend fun fetchFolders(): List<ClipFolder.Detail> =
        apiGateway.clipApi
            .getClipFolders()
            .bodyOrThrow(operation = "fetchClipFolders()")
            .map { it.toModel() }

    override suspend fun fetchFolder(folderId: ClipFolderId): ClipFolder.Detail =
        apiGateway.clipApi
            .getClipFolder(folderId.value)
            .bodyOrThrow(operation = "fetchClipFolder(folderId=${folderId.value})")
            .toModel()

    override suspend fun editFolder(
        folderId: ClipFolderId,
        name: String?,
        description: String?,
    ) {
        apiGateway.clipApi
            .editClipFolder(
                folderId.value,
                PatchClipFolderRequest(
                    name = name,
                    description = description,
                ),
            ).requireSuccess(operation = "editClipFolder(folderId=${folderId.value})")
    }

    override suspend fun deleteFolder(folderId: ClipFolderId) {
        apiGateway.clipApi
            .deleteClipFolder(folderId.value)
            .requireSuccess(operation = "deleteClipFolder(folderId=${folderId.value})")
    }

    override suspend fun clipMessage(
        folderId: ClipFolderId,
        messageId: MessageId,
    ): ClippedMessage =
        apiGateway.clipApi
            .clipMessage(folderId.value, PostClipFolderMessageRequest(messageId.value))
            .bodyOrThrow(operation = "clipMessage(folderId=${folderId.value}, messageId=${messageId.value})")
            .toModel()

    override suspend fun fetchClips(
        folderId: ClipFolderId,
        limit: Int?,
        offset: Int,
        order: SortDirection,
    ): List<ClippedMessage> =
        apiGateway.clipApi
            .getClips(
                folderId = folderId.value,
                limit = limit,
                offset = offset,
                order = order.toClipOrder(),
            ).bodyOrThrow(operation = "fetchClips(folderId=${folderId.value})")
            .map { it.toModel() }

    override suspend fun unclipMessage(
        folderId: ClipFolderId,
        messageId: MessageId,
    ) {
        apiGateway.clipApi
            .unclipMessage(folderId.value, messageId.value)
            .requireSuccess(operation = "unclipMessage(folderId=${folderId.value}, messageId=${messageId.value})")
    }

    override suspend fun fetchMessageClips(messageId: MessageId): List<MessageClip> =
        apiGateway.clipApi
            .getMessageClips(messageId.value)
            .bodyOrThrow(operation = "fetchMessageClips(messageId=${messageId.value})")
            .map { it.toModel() }
}

private fun SortDirection.toClipOrder(): ClipApi.OrderGetClips =
    when (this) {
        SortDirection.ASCENDING -> ClipApi.OrderGetClips.ASC
        SortDirection.DESCENDING -> ClipApi.OrderGetClips.DESC
    }
