package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** ファイルID。 */
@JvmInline
value class FileId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): FileId = parse(value)

        fun parse(value: String): FileId = FileId(Uuid.parse(value))
    }
}

/** ファイル。 */
sealed interface File {
    val id: FileId

    /** ID のみを持つファイル参照。 */
    @JvmInline
    value class Ref(
        override val id: FileId,
    ) : File
}

/** ファイルメタ情報。 */
@ConsistentCopyVisibility
data class FileMeta internal constructor(
    override val id: FileId,
    val name: String,
    val mime: String,
    val size: Long,
    val md5: String,
    val isAnimatedImage: Boolean,
    val createdAt: Instant,
    val channel: Channel.Ref?,
    val uploader: User.Ref?,
) : File

/** サムネイルの種類 */
enum class ThumbnailType {
    IMAGE,
    WAVEFORM,
}
