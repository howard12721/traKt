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
import com.github.howard12721.trakt.rest.models.Ogp
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.json.Json

open class OgpApi : ApiClient {

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
     * OGP情報のキャッシュを削除
     * 指定されたURLのOGP情報のキャッシュを削除します。
     * @param url OGPのキャッシュを削除したいURL
     * @return void
     */
    open suspend fun deleteOgpCache(url: kotlin.String): HttpResponse<Unit> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        url?.apply { localVariableQuery["url"] = listOf("$url") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.DELETE,
            "/ogp/cache",
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
     * OGP情報を取得
     * 指定されたURLのOGP情報を取得します。 指定されたURLに対するOGP情報が見つからなかった場合、typeがemptyに設定された空のOGP情報を返します。
     * @param url OGPを取得したいURL
     * @return Ogp
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getOgp(url: kotlin.String): HttpResponse<Ogp> {

        val localVariableAuthNames = listOf<String>("OAuth2", "bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        url?.apply { localVariableQuery["url"] = listOf("$url") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/ogp",
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
