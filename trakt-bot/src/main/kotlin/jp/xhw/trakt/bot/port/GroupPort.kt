package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember

internal interface GroupPort {
    suspend fun fetchGroup(groupId: GroupId): Group

    suspend fun fetchGroups(): List<Group>

    suspend fun fetchMembers(groupId: GroupId): List<GroupMember>
}
