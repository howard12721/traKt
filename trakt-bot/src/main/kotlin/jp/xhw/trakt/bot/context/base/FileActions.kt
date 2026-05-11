package jp.xhw.trakt.bot.context.base

import jp.xhw.trakt.bot.model.Channel
import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.FileId
import jp.xhw.trakt.bot.model.FileMeta

/**
 * チャンネルへファイルをアップロードします。
 *
 * @param channelId アップロード先チャンネル ID
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
 * チャンネルへファイルをアップロードします。
 *
 * @param channel アップロード先チャンネル
 * @param file ファイル本体
 * @param fileName 保存時のファイル名
 * @param contentType MIME タイプ。`null` の場合はサーバー側判定
 * @return アップロードされたファイルのメタ情報
 */
context(ctx: BaseContext)
suspend fun uploadFile(
    channel: Channel,
    file: ByteArray,
    fileName: String,
    contentType: String? = null,
): FileMeta = uploadFile(channel.id, file, fileName, contentType)

/**
 * ファイルメタ情報を取得します。
 *
 * @return ファイルメタ情報
 */
context(ctx: BaseContext)
suspend fun FileId.fetchMeta(): FileMeta = ctx.filePort.fetchFileMeta(this)

/**
 * ファイルメタ情報を取得します。存在しない場合は `null` を返します。
 *
 * ユーザー入力など、存在が保証できない ID を扱う場合に使います。
 *
 * @return ファイルメタ情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun FileId.fetchMetaOrNull(): FileMeta? = ctx.filePort.fetchFileMetaOrNull(this)

/**
 * ファイル本体をダウンロードします。
 *
 * @return ファイル本体
 */
context(ctx: BaseContext)
suspend fun FileId.download(): ByteArray = ctx.filePort.downloadFile(this)

/**
 * ファイル本体をダウンロードします。
 *
 * @return ファイル本体
 */
context(ctx: BaseContext)
suspend fun FileMeta.download(): ByteArray = id.download()

/** ファイルを削除します。 */
context(ctx: BaseContext)
suspend fun FileId.delete() {
    ctx.filePort.deleteFile(this)
}

/** ファイルを削除します。 */
context(ctx: BaseContext)
suspend fun FileMeta.delete() = id.delete()

/**
 * ファイルメタ情報を取得します。
 *
 * @return 最新のファイルメタ情報
 */
context(ctx: BaseContext)
suspend fun FileMeta.fetch(): FileMeta = id.fetchMeta()

/**
 * ファイルメタ情報を取得します。存在しない場合は `null` を返します。
 *
 * @return 最新のファイルメタ情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun FileMeta.fetchOrNull(): FileMeta? = id.fetchMetaOrNull()

/**
 * ファイルメタ情報を取得します。
 *
 * @return 最新のファイルメタ情報
 */
context(ctx: BaseContext)
suspend fun FileMeta.fetchMeta(): FileMeta = id.fetchMeta()

/**
 * ファイルメタ情報を取得します。存在しない場合は `null` を返します。
 *
 * @return 最新のファイルメタ情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun FileMeta.fetchMetaOrNull(): FileMeta? = id.fetchMetaOrNull()

/**
 * ファイルのURLを生成します。
 *
 * @return ファイルのURL
 */
context(ctx: BaseContext)
fun FileId.url(): String = "https://${ctx.origin}/files/$value"

/**
 * ファイルのURLを生成します。
 *
 * @return ファイルのURL
 */
context(ctx: BaseContext)
fun FileMeta.url(): String = id.url()
