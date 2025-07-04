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
 *
 *
 * @param userId ユーザーID
 * @param canPublish 発言権限
 */
@Serializable

data class QallParticipantRequestUsersInner(

    /* ユーザーID */
    @SerialName(value = "userId") val userId: kotlin.String? = null,

    /* 発言権限 */
    @SerialName(value = "canPublish") val canPublish: kotlin.Boolean? = null

) {


}

