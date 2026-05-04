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
 * この ID が指すクリップフォルダを取得します。
 *
 * @return クリップフォルダ
 */
context(ctx: UserContext)
suspend fun ClipFolderId.fetch(): ClipFolder = ctx.clipPort.fetchFolder(this)

/**
 * クリップフォルダを編集します。
 *
 * @param name 新しいフォルダ名。`null` の場合は変更しません
 * @param description 新しい説明。`null` の場合は変更しません
 */
context(ctx: UserContext)
suspend fun ClipFolderId.update(
    name: String? = null,
    description: String? = null,
) = ctx.clipPort.editFolder(this, name, description)

/** クリップフォルダを削除します。 */
context(ctx: UserContext)
suspend fun ClipFolderId.delete() = ctx.clipPort.deleteFolder(this)

/**
 * メッセージをクリップフォルダに追加します。
 *
 * @param message 追加するメッセージ
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun ClipFolderId.add(message: MessageId): ClippedMessage = ctx.clipPort.clipMessage(this, message)

/**
 * メッセージをクリップフォルダに追加します。
 *
 * @param message 追加するメッセージ
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun ClipFolderId.add(message: Message): ClippedMessage = add(message.id)

/**
 * メッセージをクリップフォルダから外します。
 *
 * @param message 外すメッセージ
 */
context(ctx: UserContext)
suspend fun ClipFolderId.remove(message: MessageId) = ctx.clipPort.unclipMessage(this, message)

/**
 * メッセージをクリップフォルダから外します。
 *
 * @param message 外すメッセージ
 */
context(ctx: UserContext)
suspend fun ClipFolderId.remove(message: Message) = remove(message.id)

/**
 * クリップフォルダ内のメッセージ一覧を取得します。
 *
 * @param limit 取得件数上限。`null` の場合はサーバーデフォルト
 * @param offset 取得開始位置
 * @param order 並び順
 * @return クリップされたメッセージ一覧
 */
context(ctx: UserContext)
suspend fun ClipFolderId.fetchMessages(
    limit: Int? = null,
    offset: Int = 0,
    order: SortDirection = SortDirection.DESCENDING,
): List<ClippedMessage> = ctx.clipPort.fetchClips(this, limit, offset, order)

/**
 * メッセージを指定フォルダにクリップします。
 *
 * @param folder 追加先クリップフォルダ
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun MessageId.clipTo(folder: ClipFolderId): ClippedMessage = folder.add(this)

/**
 * メッセージを指定フォルダにクリップします。
 *
 * @param folder 追加先クリップフォルダ
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun MessageId.clipTo(folder: ClipFolder): ClippedMessage = clipTo(folder.id)

/**
 * メッセージを指定フォルダのクリップから外します。
 *
 * @param folder 外す対象のクリップフォルダ
 */
context(ctx: UserContext)
suspend fun MessageId.unclipFrom(folder: ClipFolderId) = folder.remove(this)

/**
 * メッセージを指定フォルダのクリップから外します。
 *
 * @param folder 外す対象のクリップフォルダ
 */
context(ctx: UserContext)
suspend fun MessageId.unclipFrom(folder: ClipFolder) = unclipFrom(folder.id)

/**
 * このメッセージの自分のクリップ一覧を取得します。
 *
 * @return メッセージのクリップ先一覧
 */
context(ctx: UserContext)
suspend fun MessageId.fetchClips(): List<MessageClip> = ctx.clipPort.fetchMessageClips(this)

/**
 * クリップフォルダを取得します。
 *
 * @return 最新のクリップフォルダ情報
 */
context(ctx: UserContext)
suspend fun ClipFolder.fetch(): ClipFolder = id.fetch()

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
) = id.update(name, description)

/** クリップフォルダを削除します。 */
context(ctx: UserContext)
suspend fun ClipFolder.delete() = id.delete()

/**
 * メッセージをクリップフォルダに追加します。
 *
 * @param message 追加するメッセージ
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun ClipFolder.add(message: MessageId): ClippedMessage = id.add(message)

/**
 * メッセージをクリップフォルダに追加します。
 *
 * @param message 追加するメッセージ
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun ClipFolder.add(message: Message): ClippedMessage = id.add(message)

/**
 * メッセージをクリップフォルダから外します。
 *
 * @param message 外すメッセージ
 */
context(ctx: UserContext)
suspend fun ClipFolder.remove(message: MessageId) = id.remove(message)

/**
 * メッセージをクリップフォルダから外します。
 *
 * @param message 外すメッセージ
 */
context(ctx: UserContext)
suspend fun ClipFolder.remove(message: Message) = id.remove(message)

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
): List<ClippedMessage> = id.fetchMessages(limit, offset, order)

/**
 * メッセージを指定フォルダにクリップします。
 *
 * @param folder 追加先クリップフォルダ
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun Message.clipTo(folder: ClipFolderId): ClippedMessage = id.clipTo(folder)

/**
 * メッセージを指定フォルダにクリップします。
 *
 * @param folder 追加先クリップフォルダ
 * @return 追加されたクリップ情報
 */
context(ctx: UserContext)
suspend fun Message.clipTo(folder: ClipFolder): ClippedMessage = id.clipTo(folder)

/**
 * メッセージを指定フォルダのクリップから外します。
 *
 * @param folder 外す対象のクリップフォルダ
 */
context(ctx: UserContext)
suspend fun Message.unclipFrom(folder: ClipFolderId) = id.unclipFrom(folder)

/**
 * メッセージを指定フォルダのクリップから外します。
 *
 * @param folder 外す対象のクリップフォルダ
 */
context(ctx: UserContext)
suspend fun Message.unclipFrom(folder: ClipFolder) = id.unclipFrom(folder)

/**
 * このメッセージの自分のクリップ一覧を取得します。
 *
 * @return メッセージのクリップ先一覧
 */
context(ctx: UserContext)
suspend fun Message.fetchClips(): List<MessageClip> = id.fetchClips()
