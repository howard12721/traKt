package jp.xhw.trakt.bot.model

import kotlin.time.Instant

/** クリップフォルダ。 */
@ConsistentCopyVisibility
data class ClipFolder internal constructor(
    val id: ClipFolderId,
    val name: String,
    val description: String,
    val ownerId: UserId,
    val createdAt: Instant,
) {
    val owner: UserId
        get() = ownerId
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
    val folder: ClipFolderId
        get() = folderId
}
