package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** スタンプID。 */
@JvmInline
value class StampId(
    val value: Uuid,
)

/** スタンプを参照するためのハンドル。 */
@JvmInline
value class StampHandle internal constructor(
    val id: StampId,
)

/** スタンプ。 */
sealed interface Stamp {
    val id: StampId
    val name: String
    val creatorId: UserId
    val fileId: FileId

    /** このスタンプを指すハンドル。 */
    val handle: StampHandle
        get() = StampHandle(id)

    /** 作成者のユーザーハンドル。 */
    val creator: UserHandle
        get() = UserHandle(creatorId)

    /** 画像ファイルのハンドル。 */
    val fileHandle: FileHandle
        get() = FileHandle(fileId)

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

fun stampHandle(id: StampId) = StampHandle(id)

fun stampHandle(id: Uuid) = stampHandle(StampId(id))

fun stampHandle(id: String) = stampHandle(Uuid.parse(id))
