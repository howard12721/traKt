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
 * クリップフォルダ情報編集リクエスト
 *
 * @param name フォルダ名
 * @param description 説明
 */
@Serializable

data class PatchClipFolderRequest(

    /* フォルダ名 */
    @SerialName(value = "name") val name: kotlin.String? = null,

    /* 説明 */
    @SerialName(value = "description") val description: kotlin.String? = null

) {


}

