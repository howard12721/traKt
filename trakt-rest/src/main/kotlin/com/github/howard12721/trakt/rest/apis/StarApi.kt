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
import com.github.howard12721.trakt.rest.models.PostStarRequest
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

open class StarApi : ApiClient {

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
     * チャンネルをスターに追加
     * 指定したチャンネルをスターチャンネルに追加します。 スター済みのチャンネルIDを指定した場合、204を返します。 不正なチャンネルIDを指定した場合、400を返します。
     * @param postStarRequest  (optional)
     * @return void
     */
    open suspend fun addMyStar(postStarRequest: PostStarRequest? = null): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = postStarRequest

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.POST,
            "/users/me/stars",
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
     * スターチャンネルリストを取得
     * 自分がスターしているチャンネルのUUIDの配列を取得します。
     * @return kotlin.collections.List<kotlin.String>
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getMyStars(): HttpResponse<kotlin.collections.List<kotlin.String>> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/users/me/stars",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap<GetMyStarsResponse>().map { value }
    }

    @Serializable(GetMyStarsResponse.Companion::class)
    private class GetMyStarsResponse(val value: List<kotlin.String>) {
        companion object : KSerializer<GetMyStarsResponse> {
            private val serializer: KSerializer<List<kotlin.String>> = serializer<List<kotlin.String>>()
            override val descriptor = serializer.descriptor
            override fun serialize(encoder: Encoder, value: GetMyStarsResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = GetMyStarsResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * チャンネルをスターから削除します
     * 既にスターから削除されているチャンネルを指定した場合は204を返します。
     * @param channelId チャンネルUUID
     * @return void
     */
    open suspend fun removeMyStar(channelId: kotlin.String): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.DELETE,
            "/users/me/stars/{channelId}".replace("{" + "channelId" + "}", "$channelId"),
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
