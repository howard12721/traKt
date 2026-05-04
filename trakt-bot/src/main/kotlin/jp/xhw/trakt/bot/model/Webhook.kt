package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** Webhook ID。 */
@JvmInline
value class WebhookId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): WebhookId = parse(value)

        fun parse(value: String): WebhookId = WebhookId(Uuid.parse(value))
    }
}

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
    val botUser: UserId
        get() = botUserId

    val channel: ChannelId
        get() = channelId

    val owner: UserId
        get() = ownerId
}
