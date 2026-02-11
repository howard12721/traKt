package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class StampId(
    val value: Uuid,
)

data class Stamp(
    val id: StampId,
)

data class StampSnapshot(
    val stamp: Stamp,
    val name: String,
    val creator: User? = null,
    val file: File? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val isUnicode: Boolean? = null,
    val hasThumbnail: Boolean? = null,
)
