package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** グループID。 */
@JvmInline
value class GroupId(
    val value: Uuid,
)

/** グループを参照するためのハンドル。 */
@JvmInline
value class GroupHandle internal constructor(
    val id: GroupId,
) {
    companion object {
        /**
         * [GroupId] から [GroupHandle] を生成します。
         *
         * @param id グループID
         * @return 生成されたグループハンドル
         */
        @Deprecated(
            message = "Replace with group method instead.",
            replaceWith = ReplaceWith("group(GroupId)"),
        )
        fun of(id: GroupId): GroupHandle = GroupHandle(id)

        /**
         * [Uuid] から [GroupHandle] を生成します。
         *
         * @param id グループID(UUID)
         * @return 生成されたグループハンドル
         */
        @Deprecated(
            message = "Replace with group method instead.",
            replaceWith = ReplaceWith("group(Uuid)"),
        )
        fun of(id: Uuid): GroupHandle = GroupHandle(GroupId(id))

        /**
         * UUID 文字列から [GroupHandle] を生成します。
         *
         * @param id グループID(UUID文字列)
         * @return 生成されたグループハンドル
         */
        @Deprecated(
            message = "Replace with group method instead.",
            replaceWith = ReplaceWith("group(String)"),
        )
        fun of(id: String): GroupHandle = GroupHandle(GroupId(Uuid.parse(id)))
    }
}

/** グループ詳細情報。 */
class Group internal constructor(
    val id: GroupId,
    val name: String,
    val description: String?,
    val type: String,
    val iconFileId: FileId,
    val members: List<GroupMember>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val admins: List<UserId>,
) {
    /** このグループを指すハンドル。 */
    val handle: GroupHandle
        get() = GroupHandle(id)

    /** グループアイコンファイルのハンドル。 */
    val iconFile: FileHandle
        get() = FileHandle(iconFileId)

    /** メンバーのユーザーハンドル一覧。 */
    val memberUsers: List<UserHandle>
        get() = members.map { it.user }

    /** 管理者のユーザーハンドル一覧。 */
    val adminUsers: List<UserHandle>
        get() = admins.map { UserHandle(it) }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Group) return false
        return this.id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

/** グループメンバー情報。 */
@ConsistentCopyVisibility
data class GroupMember internal constructor(
    val userId: UserId,
    val role: String,
) {
    /** メンバーのユーザーハンドル。 */
    val user: UserHandle
        get() = UserHandle(userId)
}

fun group(id: GroupId) = GroupHandle(id)

fun group(id: Uuid) = group(GroupId(id))

fun group(id: String) = group(Uuid.parse(id))
