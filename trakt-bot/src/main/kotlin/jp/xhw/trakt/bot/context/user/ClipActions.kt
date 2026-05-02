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
): ClipFolder = ctx.clipPort.createFolder(name, description)

/**
 * 自分のクリップフォルダ一覧を取得します。
 *
 * @return クリップフォルダ一覧
 */
context(ctx: UserContext)
suspend fun fetchClipFolders(): List<ClipFolder> = ctx.clipPort.fetchFolders()

/**
 * このハンドルが指すクリップフォルダを取得します。
 *
 * @return クリップフォルダ
 */
context(ctx: UserContext)
suspend fun ClipFolderHandle.fetch(): ClipFolder = ctx.clipPort.fetchFolder(id)

/**
 * クリップフォルダを編集します。
 *
 * @param name 新しいフォルダ名。`null` の場合は変更しません
 * @param description 新しい説明。`null` の場合は変更しません
 */
context(ctx: UserContext)
suspend fun ClipFolderHandle.update(
    name: String? = null,
    description: String? = null,
) = ctx.clipPort.editFolder(id, name, description)

/** クリップフォルダを削除します。 */
context(ctx: UserContext)
suspend fun ClipFolderHandle.delete() = ctx.clipPort.deleteFolder(id)

/**
 * メッセージをクリップフォルダに追加します。
 *
 * @param message 追加するメッセージ
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun ClipFolderHandle.add(message: MessageHandle): ClippedMessage = ctx.clipPort.clipMessage(id, message.id)

/**
 * メッセージをクリップフォルダから外します。
 *
 * @param message 外すメッセージ
 */
context(ctx: UserContext)
suspend fun ClipFolderHandle.remove(message: MessageHandle) = ctx.clipPort.unclipMessage(id, message.id)

/**
 * クリップフォルダ内のメッセージ一覧を取得します。
 *
 * @param limit 取得件数上限。`null` の場合はサーバーデフォルト
 * @param offset 取得開始位置
 * @param order 並び順
 * @return クリップされたメッセージ一覧
 */
context(ctx: UserContext)
suspend fun ClipFolderHandle.fetchMessages(
    limit: Int? = null,
    offset: Int = 0,
    order: SortDirection = SortDirection.DESCENDING,
): List<ClippedMessage> = ctx.clipPort.fetchClips(id, limit, offset, order)

/**
 * メッセージを指定フォルダにクリップします。
 *
 * @param folder 追加先クリップフォルダ
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun MessageHandle.clipTo(folder: ClipFolderHandle): ClippedMessage = folder.add(this)

/**
 * メッセージを指定フォルダのクリップから外します。
 *
 * @param folder 外す対象のクリップフォルダ
 */
context(ctx: UserContext)
suspend fun MessageHandle.unclipFrom(folder: ClipFolderHandle) = folder.remove(this)

/**
 * このメッセージの自分のクリップ一覧を取得します。
 *
 * @return メッセージのクリップ先一覧
 */
context(ctx: UserContext)
suspend fun MessageHandle.fetchClips(): List<MessageClip> = ctx.clipPort.fetchMessageClips(id)

/**
 * クリップフォルダを再取得します。
 *
 * @return 最新のクリップフォルダ情報
 */
context(ctx: UserContext)
suspend fun ClipFolder.refresh(): ClipFolder = handle.fetch()

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
) = handle.update(name, description)

/** クリップフォルダを削除します。 */
context(ctx: UserContext)
suspend fun ClipFolder.delete() = handle.delete()

/**
 * メッセージをクリップフォルダに追加します。
 *
 * @param message 追加するメッセージ
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun ClipFolder.add(message: MessageHandle): ClippedMessage = handle.add(message)

/**
 * メッセージをクリップフォルダから外します。
 *
 * @param message 外すメッセージ
 */
context(ctx: UserContext)
suspend fun ClipFolder.remove(message: MessageHandle) = handle.remove(message)

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
): List<ClippedMessage> = handle.fetchMessages(limit, offset, order)
