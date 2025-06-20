/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package com.github.howard12721.trakt.rest.apis

import com.github.howard12721.trakt.rest.infrastructure.*
import com.github.howard12721.trakt.rest.models.*
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.request.forms.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

open class GroupApi : ApiClient {

    constructor(
        baseUrl: String = ApiClient.BASE_URL,
        httpClientEngine: HttpClientEngine? = null,
        httpClientConfig: ((HttpClientConfig<*>) -> Unit)? = null,
        jsonSerializer: Json = ApiClient.JSON_DEFAULT
    ) : super(
        baseUrl = baseUrl,
        httpClientEngine = httpClientEngine,
        httpClientConfig = httpClientConfig,
        jsonBlock = jsonSerializer
    )

    constructor(
        baseUrl: String,
        httpClient: HttpClient
    ) : super(baseUrl = baseUrl, httpClient = httpClient)

    /**
     * グループ管理者を追加
     * 指定したグループに管理者を追加します。 対象のユーザーグループの管理者権限が必要です。
     * @param groupId ユーザーグループUUID
     * @param postUserGroupAdminRequest  (optional)
     * @return void
     */
    open suspend fun addUserGroupAdmin(
        groupId: kotlin.String,
        postUserGroupAdminRequest: PostUserGroupAdminRequest? = null
    ): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = postUserGroupAdminRequest

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.POST,
            "/groups/{groupId}/admins".replace("{" + "groupId" + "}", "$groupId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return jsonRequest(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


    /**
     * グループメンバーを追加
     * 指定したグループにメンバーを追加します。 対象のユーザーグループの管理者権限が必要です。
     * @param groupId ユーザーグループUUID
     * @param addUserGroupMemberRequest  (optional)
     * @return void
     */
    open suspend fun addUserGroupMember(
        groupId: kotlin.String,
        addUserGroupMemberRequest: AddUserGroupMemberRequest? = null
    ): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = addUserGroupMemberRequest

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.POST,
            "/groups/{groupId}/members".replace("{" + "groupId" + "}", "$groupId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return jsonRequest(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


    /**
     * ユーザーグループのアイコンを変更
     * ユーザーグループのアイコンを変更します。 対象のユーザーグループの管理者権限が必要です。
     * @param groupId ユーザーグループUUID
     * @param file アイコン画像(2MBまでのpng, jpeg, gif)
     * @return void
     */
    open suspend fun changeUserGroupIcon(
        groupId: kotlin.String,
        file: io.ktor.client.request.forms.FormPart<io.ktor.client.request.forms.InputProvider>
    ): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            formData {
                file?.apply { append(file) }
            }

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.PUT,
            "/groups/{groupId}/icon".replace("{" + "groupId" + "}", "$groupId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return multipartFormRequest(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


    /**
     * ユーザーグループを作成
     * ユーザーグループを作成します。 作成者は自動的にグループ管理者になります。
     * @param postUserGroupRequest  (optional)
     * @return UserGroup
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun createUserGroup(postUserGroupRequest: PostUserGroupRequest? = null): HttpResponse<UserGroup> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = postUserGroupRequest

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.POST,
            "/groups",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return jsonRequest(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


    /**
     * ユーザーグループを削除
     * 指定したユーザーグループを削除します。 対象のユーザーグループの管理者権限が必要です。
     * @param groupId ユーザーグループUUID
     * @return void
     */
    open suspend fun deleteUserGroup(groupId: kotlin.String): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.DELETE,
            "/groups/{groupId}".replace("{" + "groupId" + "}", "$groupId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


    /**
     * ユーザーグループを編集
     * 指定したユーザーグループの情報を編集します。 対象のユーザーグループの管理者権限が必要です。
     * @param groupId ユーザーグループUUID
     * @param patchUserGroupRequest  (optional)
     * @return void
     */
    open suspend fun editUserGroup(
        groupId: kotlin.String,
        patchUserGroupRequest: PatchUserGroupRequest? = null
    ): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = patchUserGroupRequest

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.PATCH,
            "/groups/{groupId}".replace("{" + "groupId" + "}", "$groupId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return jsonRequest(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


    /**
     * グループメンバーを編集
     * 指定したユーザーグループ内の指定したユーザーの属性を編集します。 対象のユーザーグループの管理者権限が必要です。
     * @param groupId ユーザーグループUUID
     * @param userId ユーザーUUID
     * @param patchGroupMemberRequest  (optional)
     * @return void
     */
    open suspend fun editUserGroupMember(
        groupId: kotlin.String,
        userId: kotlin.String,
        patchGroupMemberRequest: PatchGroupMemberRequest? = null
    ): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = patchGroupMemberRequest

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.PATCH,
            "/groups/{groupId}/members/{userId}".replace("{" + "groupId" + "}", "$groupId")
                .replace("{" + "userId" + "}", "$userId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return jsonRequest(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


    /**
     * ユーザーグループを取得
     * 指定したユーザーグループの情報を取得します。
     * @param groupId ユーザーグループUUID
     * @return UserGroup
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getUserGroup(groupId: kotlin.String): HttpResponse<UserGroup> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/groups/{groupId}".replace("{" + "groupId" + "}", "$groupId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


    /**
     * グループ管理者を取得
     * 指定したグループの管理者のリストを取得します。
     * @param groupId ユーザーグループUUID
     * @return kotlin.collections.List<kotlin.String>
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getUserGroupAdmins(groupId: kotlin.String): HttpResponse<kotlin.collections.List<kotlin.String>> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/groups/{groupId}/admins".replace("{" + "groupId" + "}", "$groupId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap<GetUserGroupAdminsResponse>().map { value }
    }

    @Serializable(GetUserGroupAdminsResponse.Companion::class)
    private class GetUserGroupAdminsResponse(val value: List<kotlin.String>) {
        companion object : KSerializer<GetUserGroupAdminsResponse> {
            private val serializer: KSerializer<List<kotlin.String>> = serializer<List<kotlin.String>>()
            override val descriptor = serializer.descriptor
            override fun serialize(encoder: Encoder, value: GetUserGroupAdminsResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = GetUserGroupAdminsResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * グループメンバーを取得
     * 指定したグループのメンバーのリストを取得します。
     * @param groupId ユーザーグループUUID
     * @return kotlin.collections.List<UserGroupMember>
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getUserGroupMembers(groupId: kotlin.String): HttpResponse<kotlin.collections.List<UserGroupMember>> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/groups/{groupId}/members".replace("{" + "groupId" + "}", "$groupId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap<GetUserGroupMembersResponse>().map { value }
    }

    @Serializable(GetUserGroupMembersResponse.Companion::class)
    private class GetUserGroupMembersResponse(val value: List<UserGroupMember>) {
        companion object : KSerializer<GetUserGroupMembersResponse> {
            private val serializer: KSerializer<List<UserGroupMember>> = serializer<List<UserGroupMember>>()
            override val descriptor = serializer.descriptor
            override fun serialize(encoder: Encoder, value: GetUserGroupMembersResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = GetUserGroupMembersResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * ユーザーグループのリストを取得
     * ユーザーグループのリストを取得します。
     * @return kotlin.collections.List<UserGroup>
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getUserGroups(): HttpResponse<kotlin.collections.List<UserGroup>> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/groups",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap<GetUserGroupsResponse>().map { value }
    }

    @Serializable(GetUserGroupsResponse.Companion::class)
    private class GetUserGroupsResponse(val value: List<UserGroup>) {
        companion object : KSerializer<GetUserGroupsResponse> {
            private val serializer: KSerializer<List<UserGroup>> = serializer<List<UserGroup>>()
            override val descriptor = serializer.descriptor
            override fun serialize(encoder: Encoder, value: GetUserGroupsResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = GetUserGroupsResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * グループ管理者を削除
     * 指定したユーザーグループから指定した管理者を削除します。 対象のユーザーグループの管理者権限が必要です。 グループから管理者が存在しなくなる場合は400エラーを返します。
     * @param groupId ユーザーグループUUID
     * @param userId ユーザーUUID
     * @return void
     */
    open suspend fun removeUserGroupAdmin(groupId: kotlin.String, userId: kotlin.String): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.DELETE,
            "/groups/{groupId}/admins/{userId}".replace("{" + "groupId" + "}", "$groupId")
                .replace("{" + "userId" + "}", "$userId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


    /**
     * グループメンバーを削除
     * 指定したユーザーグループから指定したユーザーを削除します。 既にグループから削除されているメンバーを指定した場合は204を返します。 対象のユーザーグループの管理者権限が必要です。
     * @param groupId ユーザーグループUUID
     * @param userId ユーザーUUID
     * @return void
     */
    open suspend fun removeUserGroupMember(groupId: kotlin.String, userId: kotlin.String): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.DELETE,
            "/groups/{groupId}/members/{userId}".replace("{" + "groupId" + "}", "$groupId")
                .replace("{" + "userId" + "}", "$userId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


    /**
     * グループメンバーを一括削除
     * 指定したグループから全てのメンバーを削除します。 対象のユーザーグループの管理者権限が必要です。
     * @param groupId ユーザーグループUUID
     * @return void
     */
    open suspend fun removeUserGroupMembers(groupId: kotlin.String): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.DELETE,
            "/groups/{groupId}/members".replace("{" + "groupId" + "}", "$groupId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


}
