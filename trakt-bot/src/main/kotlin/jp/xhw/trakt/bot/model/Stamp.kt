package jp.xhw.trakt.bot.model

import kotlin.jvm.JvmInline
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

    /** ID のみを持つスタンプ参照。 */
    class Ref(
        override val id: StampId,
    ) : Stamp {
        override fun equals(other: Any?): Boolean = sameStampId(this, other)

        override fun hashCode(): Int = id.hashCode()
    }

    sealed interface WithMeta : Stamp {
        val name: String
        val creator: User.Ref
        val file: File.Ref
    }

    /** 基本的なスタンプ情報。 */
    class Basic internal constructor(
        override val id: StampId,
        override val name: String,
        override val creator: User.Ref,
        override val file: File.Ref,
    ) : WithMeta {
        override fun equals(other: Any?): Boolean = sameStampId(this, other)

        override fun hashCode(): Int = id.hashCode()
    }

    /** API から取得するスタンプ詳細。 */
    class Detail internal constructor(
        override val id: StampId,
        override val name: String,
        override val creator: User.Ref,
        override val file: File.Ref,
        val createdAt: Instant,
        val updatedAt: Instant,
        val isUnicode: Boolean,
    ) : WithMeta {
        override fun equals(other: Any?): Boolean = sameStampId(this, other)

        override fun hashCode(): Int = id.hashCode()
    }
}

/** スタンプ種別。 */
enum class StampType {
    UNICODE,
    ORIGINAL,
}

@ConsistentCopyVisibility
data class StampStats internal constructor(
    val count: Long,
    val totalCount: Long,
)
