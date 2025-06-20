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
 * @param id BOT UUID
 * @param botUserId BOTユーザーUUID
 * @param description 説明
 * @param developerId BOT開発者UUID
 * @param subscribeEvents BOTが購読しているイベントの配列
 * @param mode
 * @param state
 * @param createdAt 作成日時
 * @param updatedAt 更新日時
 * @param tokens
 * @param endpoint BOTサーバーエンドポイント
 * @param privileged 特権BOTかどうか
 * @param channels BOTが参加しているチャンネルのUUID配列
 */
@Serializable

data class GetBot200Response(

    /* BOT UUID */
    @SerialName(value = "id") @Required val id: kotlin.String,

    /* BOTユーザーUUID */
    @SerialName(value = "botUserId") @Required val botUserId: kotlin.String,

    /* 説明 */
    @SerialName(value = "description") @Required val description: kotlin.String,

    /* BOT開発者UUID */
    @SerialName(value = "developerId") @Required val developerId: kotlin.String,

    /* BOTが購読しているイベントの配列 */
    @SerialName(value = "subscribeEvents") @Required val subscribeEvents: kotlin.collections.List<kotlin.String>,

    @SerialName(value = "mode") @Required val mode: BotMode,

    @SerialName(value = "state") @Required val state: BotState,

    /* 作成日時 */
    @SerialName(value = "createdAt") @Required val createdAt: kotlinx.datetime.Instant,

    /* 更新日時 */
    @SerialName(value = "updatedAt") @Required val updatedAt: kotlinx.datetime.Instant,

    @SerialName(value = "tokens") @Required val tokens: BotTokens,

    /* BOTサーバーエンドポイント */
    @SerialName(value = "endpoint") @Required val endpoint: kotlin.String,

    /* 特権BOTかどうか */
    @SerialName(value = "privileged") @Required val privileged: kotlin.Boolean,

    /* BOTが参加しているチャンネルのUUID配列 */
    @SerialName(value = "channels") @Required val channels: kotlin.collections.List<kotlin.String>

) {


}

