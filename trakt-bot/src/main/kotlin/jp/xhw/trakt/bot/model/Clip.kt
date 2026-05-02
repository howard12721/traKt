package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** クリップフォルダを参照するためのハンドル。 */
@JvmInline
value class ClipFolderHandle internal constructor(
    val id: ClipFolderId,
)

/** クリップフォルダ。 */
@ConsistentCopyVisibility
data class ClipFolder internal constructor(
    val id: ClipFolderId,
    val name: String,
    val description: String,
    val ownerId: UserId,
    val createdAt: Instant,
) {
    val handle: ClipFolderHandle
        get() = ClipFolderHandle(id)

    val owner: UserHandle
        get() = UserHandle(ownerId)
}

/** クリップフォルダ内のメッセージ。 */
@ConsistentCopyVisibility
data class ClippedMessage internal constructor(
    val message: Message,
    val clippedAt: Instant,
)

/** メッセージが自分によりクリップされている先。 */
@ConsistentCopyVisibility
data class MessageClip internal constructor(
    val folderId: ClipFolderId,
    val clippedAt: Instant,
) {
    val folder: ClipFolderHandle
        get() = ClipFolderHandle(folderId)
}

/**
 * [ClipFolderId] からクリップフォルダハンドルを作成します。
 *
 * @param id クリップフォルダID
 * @return クリップフォルダハンドル
 */
fun clipFolder(id: ClipFolderId) = ClipFolderHandle(id)

/**
 * UUID からクリップフォルダハンドルを作成します。
 *
 * @param id クリップフォルダID
 * @return クリップフォルダハンドル
 */
fun clipFolder(id: Uuid) = clipFolder(ClipFolderId(id))

/**
 * UUID 文字列からクリップフォルダハンドルを作成します。
 *
 * @param id クリップフォルダID
 * @return クリップフォルダハンドル
 */
fun clipFolder(id: String) = clipFolder(Uuid.parse(id))
