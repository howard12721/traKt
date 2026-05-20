package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember
import jp.xhw.trakt.bot.model.UserId
import jp.xhw.trakt.bot.port.GroupPort
import jp.xhw.trakt.rest.models.*

internal class TraqGroupPort(
    private val apiGateway: TraqApiGateway,
) : GroupPort {
    override suspend fun fetchGroup(groupId: GroupId): Group.Detail {
        val response = apiGateway.groupApi.getUserGroup(groupId.value)
        return response.bodyOrThrow(operation = "fetchGroup(groupId=${groupId.value})").toModel()
    }

    override suspend fun fetchGroupOrNull(groupId: GroupId): Group.Detail? {
        val response = apiGateway.groupApi.getUserGroup(groupId.value)
        return response.bodyOrNullIfNotFound(operation = "fetchGroupOrNull(groupId=${groupId.value})")?.toModel()
    }

    override suspend fun fetchGroups(): List<Group.Detail> {
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

    override suspend fun createGroup(
        name: String,
        description: String,
        type: String,
    ): Group.Detail {
        val response =
            apiGateway.groupApi.createUserGroup(
                postUserGroupRequest = PostUserGroupRequest(name = name, description = description, type = type),
            )
        return response.bodyOrThrow(operation = "createGroup(name=$name)").toModel()
    }

    override suspend fun editGroup(
        groupId: GroupId,
        name: String?,
        description: String?,
    ) {
        apiGateway.groupApi
            .editUserGroup(
                groupId = groupId.value,
                patchUserGroupRequest = PatchUserGroupRequest(name = name, description = description),
            ).requireSuccess(operation = "editGroup(groupId=${groupId.value})")
    }

    override suspend fun deleteGroup(groupId: GroupId) {
        apiGateway.groupApi
            .deleteUserGroup(groupId.value)
            .requireSuccess(operation = "deleteGroup(groupId=${groupId.value})")
    }

    override suspend fun changeGroupIcon(
        groupId: GroupId,
        file: ByteArray,
        fileName: String,
        contentType: String?,
    ) {
        apiGateway.groupApi
            .changeUserGroupIcon(
                groupId = groupId.value,
                file = file.toFilePart(fileName, contentType),
            ).requireSuccess(operation = "changeGroupIcon(groupId=${groupId.value})")
    }

    override suspend fun removeMember(
        groupId: GroupId,
        userId: UserId,
    ) {
        apiGateway.groupApi
            .removeUserGroupMember(groupId = groupId.value, userId = userId.value)
            .requireSuccess(operation = "removeMember(groupId=${groupId.value}, userId=${userId.value})")
    }

    override suspend fun editMember(
        groupId: GroupId,
        userId: UserId,
        role: String,
    ) {
        apiGateway.groupApi
            .editUserGroupMember(
                groupId = groupId.value,
                userId = userId.value,
                patchGroupMemberRequest = PatchGroupMemberRequest(role = role),
            ).requireSuccess(operation = "editMember(groupId=${groupId.value}, userId=${userId.value})")
    }

    override suspend fun removeAllMembers(groupId: GroupId) {
        apiGateway.groupApi
            .removeUserGroupMembers(groupId.value)
            .requireSuccess(operation = "removeAllMembers(groupId=${groupId.value})")
    }
}
