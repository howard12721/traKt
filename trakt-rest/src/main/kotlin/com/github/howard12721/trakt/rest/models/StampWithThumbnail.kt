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
 * スタンプ情報とサムネイルの有無
 *
 * @param id スタンプUUID
 * @param name スタンプ名
 * @param creatorId 作成者UUID
 * @param createdAt 作成日時
 * @param updatedAt 更新日時
 * @param fileId ファイルUUID
 * @param isUnicode Unicode絵文字か
 * @param hasThumbnail サムネイルの有無
 */
@Serializable

data class StampWithThumbnail(

    /* スタンプUUID */
    @SerialName(value = "id") @Required val id: kotlin.String,

    /* スタンプ名 */
    @SerialName(value = "name") @Required val name: kotlin.String,

    /* 作成者UUID */
    @SerialName(value = "creatorId") @Required val creatorId: kotlin.String,

    /* 作成日時 */
    @SerialName(value = "createdAt") @Required val createdAt: kotlinx.datetime.Instant,

    /* 更新日時 */
    @SerialName(value = "updatedAt") @Required val updatedAt: kotlinx.datetime.Instant,

    /* ファイルUUID */
    @SerialName(value = "fileId") @Required val fileId: kotlin.String,

    /* Unicode絵文字か */
    @SerialName(value = "isUnicode") @Required val isUnicode: kotlin.Boolean,

    /* サムネイルの有無 */
    @SerialName(value = "hasThumbnail") @Required val hasThumbnail: kotlin.Boolean

) {


}

