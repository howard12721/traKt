package jp.xhw.trakt.bot.context.user

import jp.xhw.trakt.bot.model.*

/**
 * クリップフォルダを作成します。
 *
 * @param name フォルダ名
 * @param description フォルダ説明
 * @return 作成されたクリップフォルダ
 */
context(ctx: UserContext)
suspend fun createClipFolder(
    name: String,
    description: String = "",
): ClipFolder.Detail = ctx.clipPort.createFolder(name, description)

/**
 * 自分のクリップフォルダ一覧を取得します。
 *
 * @return クリップフォルダ一覧
 */
context(ctx: UserContext)
suspend fun fetchClipFolders(): List<ClipFolder.Detail> = ctx.clipPort.fetchFolders()

/**
 * クリップフォルダを取得します。
 *
 * @param folderId 取得対象クリップフォルダID
 * @return 最新のクリップフォルダ情報
 */
context(ctx: UserContext)
suspend fun fetchClipFolder(folderId: ClipFolderId): ClipFolder.Detail = ctx.clipPort.fetchFolder(folderId)

/**
 * クリップフォルダを取得します。
 *
 * @return 最新のクリップフォルダ情報
 */
context(ctx: UserContext)
suspend fun ClipFolder.fetch(): ClipFolder.Detail = fetchClipFolder(id)

/**
 * クリップフォルダを編集します。
 *
 * @param name 新しいフォルダ名。`null` の場合は変更しません
 * @param description 新しい説明。`null` の場合は変更しません
 */
context(ctx: UserContext)
suspend fun ClipFolder.update(
    name: String? = null,
    description: String? = null,
) = ctx.clipPort.editFolder(id, name, description)

/** クリップフォルダを削除します。 */
context(ctx: UserContext)
suspend fun ClipFolder.delete() = ctx.clipPort.deleteFolder(id)

/**
 * メッセージをクリップフォルダに追加します。
 *
 * @param messageId 追加するメッセージID
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun ClipFolder.add(messageId: MessageId): ClippedMessage = ctx.clipPort.clipMessage(id, messageId)

/**
 * メッセージをクリップフォルダから外します。
 *
 * @param messageId 外すメッセージID
 */
context(ctx: UserContext)
suspend fun ClipFolder.remove(messageId: MessageId) = ctx.clipPort.unclipMessage(id, messageId)

/**
 * クリップフォルダ内のメッセージ一覧を取得します。
 *
 * @param limit 取得件数上限。`null` の場合はサーバーデフォルト
 * @param offset 取得開始位置
 * @param order 並び順
 * @return クリップされたメッセージ一覧
 */
context(ctx: UserContext)
suspend fun ClipFolder.fetchMessages(
    limit: Int? = null,
    offset: Int = 0,
    order: SortDirection = SortDirection.DESCENDING,
): List<ClippedMessage> = ctx.clipPort.fetchClips(id, limit, offset, order)

/**
 * メッセージを指定フォルダにクリップします。
 *
 * @param folderId 追加先クリップフォルダID
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun Message.clipTo(folderId: ClipFolderId): ClippedMessage = ctx.clipPort.clipMessage(folderId, id)

/**
 * メッセージを指定フォルダのクリップから外します。
 *
 * @param folderId 外す対象のクリップフォルダID
 */
context(ctx: UserContext)
suspend fun Message.unclipFrom(folderId: ClipFolderId) = ctx.clipPort.unclipMessage(folderId, id)

/**
 * このメッセージの自分のクリップ一覧を取得します。
 *
 * @return メッセージのクリップ先一覧
 */
context(ctx: UserContext)
suspend fun Message.fetchClips(): List<MessageClip> = ctx.clipPort.fetchMessageClips(id)
