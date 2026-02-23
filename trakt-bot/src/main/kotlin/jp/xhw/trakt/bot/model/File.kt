package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** ファイルID。 */
@JvmInline
value class FileId(
    val value: Uuid,
)

/** ファイルを参照するためのハンドル。 */
@JvmInline
value class FileHandle(
    val id: FileId,
) {
    companion object {
        /**
         * [FileId] から [FileHandle] を生成します。
         *
         * @param id ファイルID
         * @return 生成されたファイルハンドル
         */
        fun of(id: FileId): FileHandle = FileHandle(id)

        /**
         * [Uuid] から [FileHandle] を生成します。
         *
         * @param id ファイルID(UUID)
         * @return 生成されたファイルハンドル
         */
        fun of(id: Uuid): FileHandle = FileHandle(FileId(id))

        /**
         * UUID 文字列から [FileHandle] を生成します。
         *
         * @param id ファイルID(UUID文字列)
         * @return 生成されたファイルハンドル
         */
        fun of(id: String): FileHandle = of(Uuid.parse(id))
    }
}

/** ファイルメタ情報。 */
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
    /** このファイルを指すハンドル。 */
    val handle: FileHandle
        get() = FileHandle(id)

    /** 所属チャンネルのハンドル。 */
    val channel: ChannelHandle?
        get() = channelId?.let { ChannelHandle(it) }

    /** アップロードユーザーのハンドル。 */
    val uploader: UserHandle?
        get() = uploaderId?.let { UserHandle(it) }
}
