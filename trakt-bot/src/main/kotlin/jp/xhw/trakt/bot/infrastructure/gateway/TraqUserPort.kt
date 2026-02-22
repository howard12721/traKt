package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.bot.port.UserPort
import jp.xhw.trakt.rest.models.PostUserTagRequest

internal class TraqUserPort(
    private val apiGateway: TraqApiGateway,
) : UserPort {
    override suspend fun fetchUser(userId: UserId): User.Detail {
        val response = apiGateway.userApi.getUser(userId.value)
        return response.bodyOrThrow(operation = "fetchUser(userId=${userId.value})").toModel()
    }

    override suspend fun fetchUsers(
        includeSuspended: Boolean,
        name: String?,
    ): List<User.Basic> {
        val response =
            apiGateway.userApi.getUsers(
                includeSuspended = includeSuspended,
                name = name,
            )
        return response
            .bodyOrThrow(operation = "fetchUsers(includeSuspended=$includeSuspended, name=$name)")
            .map { it.toModel() }
    }

    override suspend fun fetchUserIcon(userId: UserId): ByteArray {
        val response = apiGateway.userApi.getUserIcon(userId.value)
        return response.bodyOrThrow(operation = "fetchUserIcon(userId=${userId.value})").value
    }

    override suspend fun fetchDirectMessageChannel(userId: UserId): Channel.DirectMessage {
        val response = apiGateway.userApi.getUserDMChannel(userId.value)
        return response.bodyOrThrow(operation = "fetchDirectMessageChannel(userId=${userId.value})").toModel()
    }

    override suspend fun fetchUserStats(userId: UserId): UserStats {
        val response = apiGateway.userApi.getUserStats(userId.value)
        val stats = response.bodyOrThrow(operation = "fetchUserStats(userId=${userId.value})")
        return stats.toModel()
    }

    override suspend fun fetchUserTags(userId: UserId): List<UserTag> {
        val response = apiGateway.userApi.getUserTags(userId.value)
        return response.bodyOrThrow(operation = "fetchUserTags(userId=${userId.value})").map { it.toModel() }
    }

    override suspend fun addUserTag(
        userId: UserId,
        tag: String,
    ): UserTag {
        val response =
            apiGateway.userApi.addUserTag(
                userId = userId.value,
                postUserTagRequest = PostUserTagRequest(tag = tag),
            )
        return response.bodyOrThrow(operation = "addUserTag(userId=${userId.value}, tag=$tag)").toModel()
    }

    override suspend fun removeUserTag(
        userId: UserId,
        tagId: UserTagId,
    ) {
        apiGateway.userApi
            .removeUserTag(
                userId = userId.value,
                tagId = tagId.value,
            ).requireSuccess(operation = "removeUserTag(userId=${userId.value}, tagId=${tagId.value})")
    }
}
