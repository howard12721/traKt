package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember
import jp.xhw.trakt.bot.model.UserId
import jp.xhw.trakt.bot.port.GroupPort
import jp.xhw.trakt.rest.models.AddUserGroupMemberRequest
import jp.xhw.trakt.rest.models.PostUserGroupAdminRequest

internal class TraqGroupPort(
    private val apiGateway: TraqApiGateway,
) : GroupPort {
    override suspend fun fetchGroup(groupId: GroupId): Group {
        val response = apiGateway.groupApi.getUserGroup(groupId.value)
        return response.bodyOrThrow(operation = "fetchGroup(groupId=${groupId.value})").toModel()
    }

    override suspend fun fetchGroups(): List<Group> {
        val response = apiGateway.groupApi.getUserGroups()
        return response.bodyOrThrow(operation = "fetchGroups()").map { it.toModel() }
    }

    override suspend fun fetchMembers(groupId: GroupId): List<GroupMember> {
        val response = apiGateway.groupApi.getUserGroupMembers(groupId.value)
        return response.bodyOrThrow(operation = "fetchMembers(groupId=${groupId.value})").map { it.toModel() }
    }

    override suspend fun addMember(
        groupId: GroupId,
        userId: UserId,
        role: String,
    ) {
        apiGateway.groupApi
            .addUserGroupMember(
                groupId = groupId.value,
                addUserGroupMemberRequest =
                    AddUserGroupMemberRequest(
                        id = userId.value,
                        role = role,
                    ),
            ).requireSuccess(operation = "addMember(groupId=${groupId.value}, userId=${userId.value}, role=$role)")
    }

    override suspend fun addAdmin(
        groupId: GroupId,
        userId: UserId,
    ) {
        apiGateway.groupApi
            .addUserGroupAdmin(
                groupId = groupId.value,
                postUserGroupAdminRequest = PostUserGroupAdminRequest(id = userId.value),
            ).requireSuccess(operation = "addAdmin(groupId=${groupId.value}, userId=${userId.value})")
    }
}
