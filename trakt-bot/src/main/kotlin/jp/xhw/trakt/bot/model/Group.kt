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
value class GroupHandle(
    val id: GroupId,
) {
    companion object {
        /**
         * [GroupId] から [GroupHandle] を生成します。
         *
         * @param id グループID
         * @return 生成されたグループハンドル
         */
        fun of(id: GroupId): GroupHandle = GroupHandle(id)

        /**
         * [Uuid] から [GroupHandle] を生成します。
         *
         * @param id グループID(UUID)
         * @return 生成されたグループハンドル
         */
        fun of(id: Uuid): GroupHandle = GroupHandle(GroupId(id))

        /**
         * UUID 文字列から [GroupHandle] を生成します。
         *
         * @param id グループID(UUID文字列)
         * @return 生成されたグループハンドル
         */
        fun of(id: String): GroupHandle = of(Uuid.parse(id))
    }
}

/** グループ詳細情報。 */
data class Group(
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
}

/** グループメンバー情報。 */
data class GroupMember(
    val userId: UserId,
    val role: String,
) {
    /** メンバーのユーザーハンドル。 */
    val user: UserHandle
        get() = UserHandle(userId)
}
