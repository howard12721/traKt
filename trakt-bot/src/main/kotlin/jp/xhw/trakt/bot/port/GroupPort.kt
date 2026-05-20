package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember
import jp.xhw.trakt.bot.model.UserId

internal interface GroupPort {
    suspend fun fetchGroup(groupId: GroupId): Group.Detail

    suspend fun fetchGroupOrNull(groupId: GroupId): Group.Detail?

    suspend fun fetchGroups(): List<Group.Detail>

    suspend fun fetchMembers(groupId: GroupId): List<GroupMember>

    suspend fun addMember(
        groupId: GroupId,
        userId: UserId,
        role: String,
    )

    suspend fun addAdmin(
        groupId: GroupId,
        userId: UserId,
    )

    suspend fun createGroup(
        name: String,
        description: String,
        type: String,
    ): Group.Detail

    suspend fun editGroup(
        groupId: GroupId,
        name: String? = null,
        description: String? = null,
    )

    suspend fun deleteGroup(groupId: GroupId)

    suspend fun changeGroupIcon(
        groupId: GroupId,
        file: ByteArray,
        fileName: String,
        contentType: String? = null,
    )

    suspend fun removeMember(
        groupId: GroupId,
        userId: UserId,
    )

    suspend fun editMember(
        groupId: GroupId,
        userId: UserId,
        role: String,
    )

    suspend fun removeAllMembers(groupId: GroupId)
}
