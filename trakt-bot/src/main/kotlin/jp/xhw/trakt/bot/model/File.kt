package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class FileId(
    val value: Uuid,
)

data class File(
    val id: FileId,
)

data class FileSnapshot(
    val file: File,
    val name: String,
    val mime: String,
    val size: Long,
    val md5: String,
    val isAnimatedImage: Boolean,
    val createdAt: Instant,
    val channel: Channel? = null,
    val uploader: User? = null,
)
