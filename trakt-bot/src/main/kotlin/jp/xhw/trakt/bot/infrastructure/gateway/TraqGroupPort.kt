package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember
import jp.xhw.trakt.bot.port.GroupPort

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
}
