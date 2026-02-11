package jp.xhw.trakt.bot.gateway

import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.InputProvider
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import jp.xhw.trakt.bot.model.CreateUserRequest
import jp.xhw.trakt.bot.model.DirectMessageChannelSnapshot
import jp.xhw.trakt.bot.model.EditUserRequest
import jp.xhw.trakt.bot.model.MessageHistoryQuery
import jp.xhw.trakt.bot.model.MessageSnapshot
import jp.xhw.trakt.bot.model.User
import jp.xhw.trakt.bot.model.UserIconUpload
import jp.xhw.trakt.bot.model.UserId
import jp.xhw.trakt.bot.model.UserSnapshot
import jp.xhw.trakt.bot.model.UserStatsSnapshot
import jp.xhw.trakt.bot.model.UserTagId
import jp.xhw.trakt.bot.model.UserTagSnapshot
import jp.xhw.trakt.bot.port.MessagePort
import jp.xhw.trakt.bot.port.UserPort
import jp.xhw.trakt.rest.models.PatchUserRequest
import jp.xhw.trakt.rest.models.PatchUserTagRequest
import jp.xhw.trakt.rest.models.PostUserRequest
import jp.xhw.trakt.rest.models.PostUserTagRequest
import jp.xhw.trakt.rest.models.PutUserPasswordRequest

internal class TraqUserPort(
    private val apiGateway: TraqApiGateway,
    private val messagePort: MessagePort,
) : UserPort {
    override suspend fun getUser(userId: UserId): User? = getSnapshot(userId)?.user

    override suspend fun getSnapshot(userId: UserId): UserSnapshot? {
        val response = apiGateway.userApi.getUser(userId.value)
        if (!response.success && response.status == 404) {
            return null
        }
        return response
            .bodyOrThrow(operation = "getUser(userId=${userId.value})")
            .toSnapshot()
    }

    override suspend fun listUsers(
        includeSuspended: Boolean,
        name: String?,
    ): List<UserSnapshot> {
        val response =
            apiGateway.userApi.getUsers(
                includeSuspended = includeSuspended,
                name = name,
            )
        return response
            .bodyOrThrow(
                operation =
                    "listUsers(includeSuspended=$includeSuspended, name=$name)",
            ).map { user -> user.toSnapshot() }
    }

    override suspend fun createUser(request: CreateUserRequest): UserSnapshot {
        val response =
            apiGateway.userApi.createUser(
                PostUserRequest(
                    name = request.name,
                    password = request.password,
                ),
            )
        return response
            .bodyOrThrow(operation = "createUser(name=${request.name})")
            .toSnapshot()
    }

    override suspend fun editUser(
        userId: UserId,
        request: EditUserRequest,
    ) {
        apiGateway.userApi
            .editUser(
                userId = userId.value,
                patchUserRequest =
                    PatchUserRequest(
                        displayName = request.displayName,
                        twitterId = request.twitterId,
                        state = request.state?.toApiModel(),
                        role = request.role,
                    ),
            ).requireSuccess(operation = "editUser(userId=${userId.value})")
    }

    override suspend fun changePassword(
        userId: UserId,
        newPassword: String,
    ) {
        apiGateway.userApi
            .changeUserPassword(
                userId = userId.value,
                putUserPasswordRequest = PutUserPasswordRequest(newPassword = newPassword),
            ).requireSuccess(operation = "changePassword(userId=${userId.value})")
    }

    override suspend fun changeIcon(
        userId: UserId,
        icon: UserIconUpload,
    ) {
        apiGateway.userApi
            .changeUserIcon(
                userId = userId.value,
                file = icon.toFormPart(),
            ).requireSuccess(operation = "changeIcon(userId=${userId.value})")
    }

    override suspend fun getIcon(userId: UserId): ByteArray? {
        val response = apiGateway.userApi.getUserIcon(userId.value)
        if (!response.success && response.status == 404) {
            return null
        }
        return response
            .bodyOrThrow(operation = "getIcon(userId=${userId.value})")
            .value
    }

    override suspend fun getDirectMessageChannel(userId: UserId): DirectMessageChannelSnapshot? {
        val response = apiGateway.userApi.getUserDMChannel(userId.value)
        if (!response.success && response.status == 404) {
            return null
        }
        return response
            .bodyOrThrow(operation = "getDirectMessageChannel(userId=${userId.value})")
            .toSnapshot()
    }

    override suspend fun getDirectMessages(
        userId: UserId,
        query: MessageHistoryQuery,
    ): List<MessageSnapshot> = messagePort.getDirectMessages(userId, query)

    override suspend fun postDirectMessage(
        userId: UserId,
        content: String,
        embed: Boolean,
        nonce: String?,
    ): MessageSnapshot = messagePort.postDirectMessage(userId, content, embed, nonce)

    override suspend fun getTags(userId: UserId): List<UserTagSnapshot> {
        val response = apiGateway.userApi.getUserTags(userId.value)
        return response
            .bodyOrThrow(operation = "getTags(userId=${userId.value})")
            .map { tag -> tag.toSnapshot() }
    }

    override suspend fun addTag(
        userId: UserId,
        tag: String,
    ): UserTagSnapshot {
        val response =
            apiGateway.userApi.addUserTag(
                userId = userId.value,
                postUserTagRequest = PostUserTagRequest(tag = tag),
            )
        return response
            .bodyOrThrow(operation = "addTag(userId=${userId.value}, tag=$tag)")
            .toSnapshot()
    }

    override suspend fun editTag(
        userId: UserId,
        tagId: UserTagId,
        isLocked: Boolean,
    ) {
        apiGateway.userApi
            .editUserTag(
                userId = userId.value,
                tagId = tagId.value,
                patchUserTagRequest = PatchUserTagRequest(isLocked = isLocked),
            ).requireSuccess(operation = "editTag(userId=${userId.value}, tagId=${tagId.value})")
    }

    override suspend fun removeTag(
        userId: UserId,
        tagId: UserTagId,
    ) {
        apiGateway.userApi
            .removeUserTag(
                userId = userId.value,
                tagId = tagId.value,
            ).requireSuccess(operation = "removeTag(userId=${userId.value}, tagId=${tagId.value})")
    }

    override suspend fun getStats(userId: UserId): UserStatsSnapshot? {
        val response = apiGateway.userApi.getUserStats(userId.value)
        if (!response.success && response.status == 404) {
            return null
        }
        return response
            .bodyOrThrow(operation = "getStats(userId=${userId.value})")
            .toSnapshot()
    }
}

private fun UserIconUpload.toFormPart(): FormPart<InputProvider> =
    FormPart(
        key = "file",
        value =
            InputProvider {
                buildPacket {
                    writeFully(bytes)
                }
            },
        headers =
            Headers.build {
                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                contentType?.let { append(HttpHeaders.ContentType, it) }
            },
    )
