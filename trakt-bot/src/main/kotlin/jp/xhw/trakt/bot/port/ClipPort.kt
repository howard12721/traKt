package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.*

internal interface ClipPort {
    suspend fun createFolder(
        name: String,
        description: String,
    ): ClipFolder

    suspend fun fetchFolders(): List<ClipFolder>

    suspend fun fetchFolder(folderId: ClipFolderId): ClipFolder

    suspend fun editFolder(
        folderId: ClipFolderId,
        name: String? = null,
        description: String? = null,
    )

    suspend fun deleteFolder(folderId: ClipFolderId)

    suspend fun clipMessage(
        folderId: ClipFolderId,
        messageId: MessageId,
    ): ClippedMessage

    suspend fun fetchClips(
        folderId: ClipFolderId,
        limit: Int? = null,
        offset: Int = 0,
        order: SortDirection = SortDirection.DESCENDING,
    ): List<ClippedMessage>

    suspend fun unclipMessage(
        folderId: ClipFolderId,
        messageId: MessageId,
    )

    suspend fun fetchMessageClips(messageId: MessageId): List<MessageClip>
}
