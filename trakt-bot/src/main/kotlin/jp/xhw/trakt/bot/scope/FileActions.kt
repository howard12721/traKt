package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.ChannelHandle
import jp.xhw.trakt.bot.model.FileHandle
import jp.xhw.trakt.bot.model.FileMeta

context(scope: BotScope)
suspend fun uploadFile(
    channelHandle: ChannelHandle,
    file: ByteArray,
    fileName: String,
    contentType: String? = null,
): FileMeta = scope.context.filePort.uploadFile(channelHandle.id, file, fileName, contentType)

context(scope: BotScope)
suspend fun FileHandle.fetchMeta(): FileMeta = scope.context.filePort.fetchFileMeta(id)

context(scope: BotScope)
suspend fun FileHandle.download(): ByteArray = scope.context.filePort.downloadFile(id)

context(scope: BotScope)
suspend fun FileMeta.download(): ByteArray = handle.download()

context(scope: BotScope)
suspend fun FileHandle.delete() {
    scope.context.filePort.deleteFile(id)
}

context(scope: BotScope)
suspend fun FileMeta.delete() = handle.delete()

context(scope: BotScope)
suspend fun FileMeta.refresh(): FileMeta = handle.fetchMeta()
