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
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

open class AuthenticationApi : ApiClient {

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
     * 外部ログインアカウント一覧を取得
     * 自分に紐付けられている外部ログインアカウント一覧を取得します。
     * @return kotlin.collections.List<ExternalProviderUser>
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getMyExternalAccounts(): HttpResponse<kotlin.collections.List<ExternalProviderUser>> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/users/me/ex-accounts",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap<GetMyExternalAccountsResponse>().map { value }
    }

    @Serializable(GetMyExternalAccountsResponse.Companion::class)
    private class GetMyExternalAccountsResponse(val value: List<ExternalProviderUser>) {
        companion object : KSerializer<GetMyExternalAccountsResponse> {
            private val serializer: KSerializer<List<ExternalProviderUser>> = serializer<List<ExternalProviderUser>>()
            override val descriptor = serializer.descriptor
            override fun serialize(encoder: Encoder, value: GetMyExternalAccountsResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = GetMyExternalAccountsResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * 自分のログインセッションリストを取得
     * 自分のログインセッションのリストを取得します。
     * @return kotlin.collections.List<LoginSession>
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getMySessions(): HttpResponse<kotlin.collections.List<LoginSession>> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/users/me/sessions",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap<GetMySessionsResponse>().map { value }
    }

    @Serializable(GetMySessionsResponse.Companion::class)
    private class GetMySessionsResponse(val value: List<LoginSession>) {
        companion object : KSerializer<GetMySessionsResponse> {
            private val serializer: KSerializer<List<LoginSession>> = serializer<List<LoginSession>>()
            override val descriptor = serializer.descriptor
            override fun serialize(encoder: Encoder, value: GetMySessionsResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = GetMySessionsResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * 外部ログインアカウントを紐付ける
     * 自分に外部ログインアカウントを紐付けます。 指定した&#x60;providerName&#x60;がサーバー側で有効である必要があります。 リクエストが受理された場合、外部サービスの認証画面にリダイレクトされ、認証される必要があります。
     * @param postLinkExternalAccount  (optional)
     * @return void
     */
    open suspend fun linkExternalAccount(postLinkExternalAccount: PostLinkExternalAccount? = null): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = postLinkExternalAccount

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.POST,
            "/users/me/ex-accounts/link",
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
     * ログイン
     * ログインします。
     * @param redirect リダイレクト先 (optional)
     * @param postLoginRequest  (optional)
     * @return void
     */
    open suspend fun login(
        redirect: kotlin.String? = null,
        postLoginRequest: PostLoginRequest? = null
    ): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = postLoginRequest

        val localVariableQuery = mutableMapOf<String, List<String>>()
        redirect?.apply { localVariableQuery["redirect"] = listOf("$redirect") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.POST,
            "/login",
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
     * ログアウト
     * ログアウトします。
     * @param redirect リダイレクト先 (optional)
     * @param all 全てのセッションでログアウトするかどうか (optional, default to false)
     * @return void
     */
    open suspend fun logout(redirect: kotlin.String? = null, all: kotlin.Boolean? = false): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        redirect?.apply { localVariableQuery["redirect"] = listOf("$redirect") }
        all?.apply { localVariableQuery["all"] = listOf("$all") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.POST,
            "/logout",
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
     * セッションを無効化
     * 指定した自分のセッションを無効化(ログアウト)します。 既に存在しない・無効化されているセッションを指定した場合も&#x60;204&#x60;を返します。
     * @param sessionId セッションUUID
     * @return void
     */
    open suspend fun revokeMySession(sessionId: kotlin.String): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.DELETE,
            "/users/me/sessions/{sessionId}".replace("{" + "sessionId" + "}", "$sessionId"),
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
     * 外部ログインアカウントの紐付けを解除
     * 自分に紐付けられている外部ログインアカウントの紐付けを解除します。
     * @param postUnlinkExternalAccount  (optional)
     * @return void
     */
    open suspend fun unlinkExternalAccount(postUnlinkExternalAccount: PostUnlinkExternalAccount? = null): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody = postUnlinkExternalAccount

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.POST,
            "/users/me/ex-accounts/unlink",
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


}
