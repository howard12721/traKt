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


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 自分のユーザー情報変更リクエスト
 *
 * @param displayName 新しい表示名
 * @param twitterId TwitterID
 * @param bio 自己紹介(biography)
 * @param homeChannel ホームチャンネルのUUID `00000000-0000-0000-0000-000000000000`を指定すると、ホームチャンネルが`null`に設定されます
 */
@Serializable

data class PatchMeRequest(

    /* 新しい表示名 */
    @SerialName(value = "displayName") val displayName: kotlin.String? = null,

    /* TwitterID */
    @SerialName(value = "twitterId") val twitterId: kotlin.String? = null,

    /* 自己紹介(biography) */
    @SerialName(value = "bio") val bio: kotlin.String? = null,

    /* ホームチャンネルのUUID `00000000-0000-0000-0000-000000000000`を指定すると、ホームチャンネルが`null`に設定されます */
    @SerialName(value = "homeChannel") val homeChannel: kotlin.String? = null

) {


}

