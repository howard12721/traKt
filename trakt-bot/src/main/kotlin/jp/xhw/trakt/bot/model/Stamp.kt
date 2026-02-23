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
value class StampHandle(
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
    data class Basic(
        override val id: StampId,
        override val name: String,
        override val creatorId: UserId,
        override val fileId: FileId,
    ) : Stamp

    /** API から取得するスタンプ詳細。 */
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

/** スタンプ種別。 */
enum class StampType {
    UNICODE,
    ORIGINAL,
}
