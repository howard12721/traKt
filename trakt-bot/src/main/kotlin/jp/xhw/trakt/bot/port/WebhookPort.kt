package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant

internal interface WebhookPort {
    suspend fun fetchWebhooks(all: Boolean = false): List<Webhook>

    suspend fun createWebhook(
        name: String,
        description: String,
        channelId: ChannelId,
        secret: String,
    ): Webhook

    suspend fun fetchWebhook(webhookId: WebhookId): Webhook

    suspend fun editWebhook(
        webhookId: WebhookId,
        name: String? = null,
        description: String? = null,
        channelId: ChannelId? = null,
        secret: String? = null,
        ownerId: UserId? = null,
    )

    suspend fun deleteWebhook(webhookId: WebhookId)

    suspend fun postWebhook(
        webhookId: WebhookId,
        content: String,
        signature: String? = null,
        channelId: ChannelId? = null,
        embed: Boolean = false,
    )

    suspend fun fetchWebhookMessages(
        webhookId: WebhookId,
        limit: Int? = null,
        offset: Int = 0,
        since: Instant? = null,
        until: Instant? = null,
        inclusive: Boolean = false,
        order: SortDirection = SortDirection.DESCENDING,
    ): List<Message>

    suspend fun deleteWebhookMessage(
        webhookId: WebhookId,
        messageId: MessageId,
    )

    suspend fun downloadWebhookIcon(webhookId: WebhookId): ByteArray

    suspend fun changeWebhookIcon(
        webhookId: WebhookId,
        file: ByteArray,
        fileName: String,
        contentType: String? = null,
    )
}
