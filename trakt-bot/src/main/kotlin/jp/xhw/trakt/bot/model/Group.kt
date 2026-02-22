package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

@JvmInline
value class GroupId(
    val value: Uuid,
)

@JvmInline
value class GroupHandle(
    val id: GroupId,
) {
    companion object {
        fun of(id: GroupId): GroupHandle = GroupHandle(id)

        fun of(id: Uuid): GroupHandle = GroupHandle(GroupId(id))

        fun of(id: String): GroupHandle = of(Uuid.parse(id))
    }
}

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
    val handle: GroupHandle
        get() = GroupHandle(id)
    val iconFile: FileHandle
        get() = FileHandle(iconFileId)
    val memberUsers: List<UserHandle>
        get() = members.map { it.user }
    val adminUsers: List<UserHandle>
        get() = admins.map { UserHandle(it) }
}

data class GroupMember(
    val userId: UserId,
    val role: String,
) {
    val user: UserHandle
        get() = UserHandle(userId)
}

