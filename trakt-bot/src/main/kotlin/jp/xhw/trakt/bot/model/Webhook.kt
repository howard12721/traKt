package jp.xhw.trakt.bot.model

import kotlin.jvm.JvmInline
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

/** Webhook。 */
sealed interface Webhook {
    val id: WebhookId

    /** ID のみを持つ Webhook 参照。 */
    class Ref(
        override val id: WebhookId,
    ) : Webhook {
        override fun equals(other: Any?): Boolean = sameWebhookId(this, other)

        override fun hashCode(): Int = id.hashCode()
    }

    /** Webhook 情報。 */
    @ConsistentCopyVisibility
    data class Detail internal constructor(
        override val id: WebhookId,
        val botUser: User.Ref,
        val displayName: String,
        val description: String,
        val isSecure: Boolean,
        val channel: Channel.Ref,
        val owner: User.Ref,
        val createdAt: Instant,
        val updatedAt: Instant,
    ) : Webhook {
        override fun equals(other: Any?): Boolean = sameWebhookId(this, other)

        override fun hashCode(): Int = id.hashCode()
    }
}
