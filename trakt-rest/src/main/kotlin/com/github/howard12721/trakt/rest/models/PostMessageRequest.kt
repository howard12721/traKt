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

package com.github.howard12721.trakt.rest.models


import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * メッセージ投稿リクエスト
 *
 * @param content メッセージ本文
 * @param embed メンション・チャンネルリンクを自動埋め込みするか
 */
@Serializable

data class PostMessageRequest(

    /* メッセージ本文 */
    @SerialName(value = "content") @Required val content: kotlin.String,

    /* メンション・チャンネルリンクを自動埋め込みするか */
    @SerialName(value = "embed") val embed: kotlin.Boolean? = false

) {


}

