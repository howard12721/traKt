package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** Webhook ID。 */
@JvmInline
value class WebhookId(
    val value: Uuid,
)

/** Webhook を参照するためのハンドル。 */
@JvmInline
value class WebhookHandle internal constructor(
    val id: WebhookId,
)

/** Webhook 情報。 */
@ConsistentCopyVisibility
data class Webhook internal constructor(
    val id: WebhookId,
    val botUserId: UserId,
    val displayName: String,
    val description: String,
    val isSecure: Boolean,
    val channelId: ChannelId,
    val ownerId: UserId,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    val handle: WebhookHandle
        get() = WebhookHandle(id)

    val botUser: UserHandle
        get() = UserHandle(botUserId)

    val channel: ChannelHandle
        get() = ChannelHandle(channelId)

    val owner: UserHandle
        get() = UserHandle(ownerId)
}

/**
 * [WebhookId] から Webhook ハンドルを作成します。
 *
 * @param id Webhook ID
 * @return Webhook ハンドル
 */
fun webhook(id: WebhookId) = WebhookHandle(id)

/**
 * UUID から Webhook ハンドルを作成します。
 *
 * @param id Webhook ID
 * @return Webhook ハンドル
 */
fun webhook(id: Uuid) = webhook(WebhookId(id))

/**
 * UUID 文字列から Webhook ハンドルを作成します。
 *
 * @param id Webhook ID
 * @return Webhook ハンドル
 */
fun webhook(id: String) = webhook(Uuid.parse(id))
