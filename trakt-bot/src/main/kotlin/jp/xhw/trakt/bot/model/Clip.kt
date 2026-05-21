package jp.xhw.trakt.bot.model

import kotlin.jvm.JvmInline
import kotlin.time.Instant

/** クリップフォルダ。 */
sealed interface ClipFolder {
    val id: ClipFolderId

    /** ID のみを持つクリップフォルダ参照。 */
    @JvmInline
    value class Ref(
        override val id: ClipFolderId,
    ) : ClipFolder

    /** クリップフォルダ詳細。 */
    @ConsistentCopyVisibility
    data class Detail internal constructor(
        override val id: ClipFolderId,
        val name: String,
        val description: String,
        val owner: User.Ref,
        val createdAt: Instant,
    ) : ClipFolder
}

/** クリップフォルダ内のメッセージ。 */
@ConsistentCopyVisibility
data class ClippedMessage internal constructor(
    val message: Message.Detail,
    val clippedAt: Instant,
)

/** メッセージが自分によりクリップされている先。 */
@ConsistentCopyVisibility
data class MessageClip internal constructor(
    val folder: ClipFolder.Ref,
    val clippedAt: Instant,
)
