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
 *
 *
 * @param roomId ルームのID
 * @param participants
 * @param isWebinar ウェビナールームかどうか
 * @param metadata ルームに関連付けられたカスタム属性
 */
@Serializable

data class QallRoomStateChangedEventRoomStatesInner(

    /* ルームのID */
    @SerialName(value = "roomId") @Required val roomId: kotlin.String,

    @SerialName(value = "participants") @Required val participants: kotlin.collections.List<QallRoomStateChangedEventRoomStatesInnerParticipantsInner>,

    /* ウェビナールームかどうか */
    @SerialName(value = "isWebinar") @Required val isWebinar: kotlin.Boolean,

    /* ルームに関連付けられたカスタム属性 */
    @SerialName(value = "metadata") val metadata: kotlin.String? = null

) {


}

