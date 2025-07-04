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
import com.github.howard12721.trakt.rest.models.PatchUserTagRequest
import com.github.howard12721.trakt.rest.models.PostUserTagRequest
import com.github.howard12721.trakt.rest.models.Tag
import com.github.howard12721.trakt.rest.models.UserTag
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

open class UserTagApi : ApiClient {

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
     * 自分にタグを追加
     * 自分に新しくタグを追加します。
     * @param postUserTagRequest  (optional)
     * @return UserTag
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun addMyUserTag(postUserTagRequest: PostUserTagRequest? = null): HttpResponse<UserTag> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = postUserTagRequest

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.POST,
            "/users/me/tags",
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
     * ユーザーにタグを追加
     * 指定したユーザーに指定したタグを追加します。 Webhookユーザーにタグを追加することは出来ません。
     * @param userId ユーザーUUID
     * @param postUserTagRequest  (optional)
     * @return UserTag
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun addUserTag(
        userId: kotlin.String,
        postUserTagRequest: PostUserTagRequest? = null
    ): HttpResponse<UserTag> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = postUserTagRequest

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.POST,
            "/users/{userId}/tags".replace("{" + "userId" + "}", "$userId"),
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
     * 自分のタグを編集
     * 自分の指定したタグの状態を変更します。
     * @param tagId タグUUID
     * @param patchUserTagRequest  (optional)
     * @return void
     */
    open suspend fun editMyUserTag(
        tagId: kotlin.String,
        patchUserTagRequest: PatchUserTagRequest? = null
    ): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = patchUserTagRequest

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.PATCH,
            "/users/me/tags/{tagId}".replace("{" + "tagId" + "}", "$tagId"),
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
     * ユーザーのタグを編集
     * 指定したユーザーの指定したタグの状態を変更します。 他人の状態は変更できません。
     * @param userId ユーザーUUID
     * @param tagId タグUUID
     * @param patchUserTagRequest  (optional)
     * @return void
     */
    open suspend fun editUserTag(
        userId: kotlin.String,
        tagId: kotlin.String,
        patchUserTagRequest: PatchUserTagRequest? = null
    ): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = patchUserTagRequest

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.PATCH,
            "/users/{userId}/tags/{tagId}".replace("{" + "userId" + "}", "$userId")
                .replace("{" + "tagId" + "}", "$tagId"),
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
     * 自分のタグリストを取得
     * 自分に付けられているタグの配列を取得します。
     * @return kotlin.collections.List<UserTag>
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getMyUserTags(): HttpResponse<kotlin.collections.List<UserTag>> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/users/me/tags",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap<GetMyUserTagsResponse>().map { value }
    }

    @Serializable(GetMyUserTagsResponse.Companion::class)
    private class GetMyUserTagsResponse(val value: List<UserTag>) {
        companion object : KSerializer<GetMyUserTagsResponse> {
            private val serializer: KSerializer<List<UserTag>> = serializer<List<UserTag>>()
            override val descriptor = serializer.descriptor
            override fun serialize(encoder: Encoder, value: GetMyUserTagsResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = GetMyUserTagsResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * タグ情報を取得
     * 指定したタグの情報を取得します。
     * @param tagId タグUUID
     * @return Tag
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getTag(tagId: kotlin.String): HttpResponse<Tag> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/tags/{tagId}".replace("{" + "tagId" + "}", "$tagId"),
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
     * ユーザーのタグリストを取得
     * 指定したユーザーのタグリストを取得します。
     * @param userId ユーザーUUID
     * @return kotlin.collections.List<UserTag>
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getUserTags(userId: kotlin.String): HttpResponse<kotlin.collections.List<UserTag>> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/users/{userId}/tags".replace("{" + "userId" + "}", "$userId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap<GetUserTagsResponse>().map { value }
    }

    @Serializable(GetUserTagsResponse.Companion::class)
    private class GetUserTagsResponse(val value: List<UserTag>) {
        companion object : KSerializer<GetUserTagsResponse> {
            private val serializer: KSerializer<List<UserTag>> = serializer<List<UserTag>>()
            override val descriptor = serializer.descriptor
            override fun serialize(encoder: Encoder, value: GetUserTagsResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = GetUserTagsResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * 自分からタグを削除します
     * 既に存在しないタグを削除しようとした場合は204を返します。
     * @param tagId タグUUID
     * @return void
     */
    open suspend fun removeMyUserTag(tagId: kotlin.String): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.DELETE,
            "/users/me/tags/{tagId}".replace("{" + "tagId" + "}", "$tagId"),
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
     * ユーザーからタグを削除します
     * 既に存在しないタグを削除しようとした場合は204を返します。
     * @param userId ユーザーUUID
     * @param tagId タグUUID
     * @return void
     */
    open suspend fun removeUserTag(userId: kotlin.String, tagId: kotlin.String): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.DELETE,
            "/users/{userId}/tags/{tagId}".replace("{" + "userId" + "}", "$userId")
                .replace("{" + "tagId" + "}", "$tagId"),
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
