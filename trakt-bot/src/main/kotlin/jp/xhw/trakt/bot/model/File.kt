package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

@JvmInline
value class FileId(
    val value: Uuid,
)

@JvmInline
value class FileHandle(
    val id: FileId,
) {
    companion object {
        fun of(id: FileId): FileHandle = FileHandle(id)

        fun of(id: Uuid): FileHandle = FileHandle(FileId(id))

        fun of(id: String): FileHandle = of(Uuid.parse(id))
    }
}

data class FileMeta(
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
    val handle: FileHandle
        get() = FileHandle(id)

    val channel: ChannelHandle?
        get() = channelId?.let { ChannelHandle(it) }

    val uploader: UserHandle?
        get() = uploaderId?.let { UserHandle(it) }
}
