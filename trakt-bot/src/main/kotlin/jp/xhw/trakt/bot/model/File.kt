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

/** ファイルメタ情報。 */
@ConsistentCopyVisibility
data class FileMeta internal constructor(
    val id: FileId,
    val name: String,
    val mime: String,
    val size: Long,
    val md5: String,
    val isAnimatedImage: Boolean,
    val createdAt: Instant,
    val channelId: ChannelId?,
    val uploaderId: UserId?,
) {
    /** 所属チャンネル ID。 */
    val channel: ChannelId?
        get() = channelId

    /** アップロードユーザー ID。 */
    val uploader: UserId?
        get() = uploaderId
}
