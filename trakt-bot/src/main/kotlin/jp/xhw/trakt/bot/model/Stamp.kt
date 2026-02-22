package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

@JvmInline
value class StampId(
    val value: Uuid,
)

@JvmInline
value class StampHandle(
    val id: StampId,
)

sealed interface Stamp {
    val id: StampId
    val name: String
    val creatorId: UserId
    val fileId: FileId

    val handle: StampHandle
        get() = StampHandle(id)

    val creator: UserHandle
        get() = UserHandle(creatorId)

    val fileHandle: FileHandle
        get() = FileHandle(fileId)

    data class Basic(
        override val id: StampId,
        override val name: String,
        override val creatorId: UserId,
        override val fileId: FileId,
    ) : Stamp

    data class Detail(
        override val id: StampId,
        override val name: String,
        override val creatorId: UserId,
        override val fileId: FileId,
        val createdAt: Instant,
        val updatedAt: Instant,
        val isUnicode: Boolean,
    ) : Stamp
}

enum class StampType {
    UNICODE,
    ORIGINAL,
}

