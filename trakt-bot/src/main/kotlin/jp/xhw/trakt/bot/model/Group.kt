package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** グループID。 */
@JvmInline
value class GroupId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): GroupId = parse(value)

        fun parse(value: String): GroupId = GroupId(Uuid.parse(value))
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
    /** グループアイコンファイル ID。 */
    val iconFile: FileId
        get() = iconFileId

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
    /** メンバーのユーザー ID。 */
    val user: UserId
        get() = userId
}
