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
 * BOTのトークン情報
 *
 * @param verificationToken Verification Token
 * @param accessToken BOTアクセストークン
 */
@Serializable

data class BotTokens(

    /* Verification Token */
    @SerialName(value = "verificationToken") @Required val verificationToken: kotlin.String,

    /* BOTアクセストークン */
    @SerialName(value = "accessToken") @Required val accessToken: kotlin.String

) {


}

