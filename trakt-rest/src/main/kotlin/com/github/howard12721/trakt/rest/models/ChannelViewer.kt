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
 * チャンネル閲覧者情報
 *
 * @param userId ユーザーUUID
 * @param state
 * @param updatedAt 更新日時
 */
@Serializable

data class ChannelViewer(

    /* ユーザーUUID */
    @SerialName(value = "userId") @Required val userId: kotlin.String,

    @SerialName(value = "state") @Required val state: ChannelViewState,

    /* 更新日時 */
    @SerialName(value = "updatedAt") @Required val updatedAt: kotlinx.datetime.Instant

) {


}

