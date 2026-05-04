package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** スタンプID。 */
@JvmInline
value class StampId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): StampId = parse(value)

        fun parse(value: String): StampId = StampId(Uuid.parse(value))
    }
}

/** スタンプ。 */
sealed interface Stamp {
    val id: StampId
    val name: String
    val creatorId: UserId
    val fileId: FileId

    /** 基本的なスタンプ情報。 */
    class Basic internal constructor(
        override val id: StampId,
        override val name: String,
        override val creatorId: UserId,
        override val fileId: FileId,
    ) : Stamp {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Stamp) return false
            return this.id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }

    /** API から取得するスタンプ詳細。 */
    class Detail internal constructor(
        override val id: StampId,
        override val name: String,
        override val creatorId: UserId,
        override val fileId: FileId,
        val createdAt: Instant,
        val updatedAt: Instant,
        val isUnicode: Boolean,
    ) : Stamp {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Stamp) return false
            return this.id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }
}

/** スタンプ種別。 */
enum class StampType {
    UNICODE,
    ORIGINAL,
}
