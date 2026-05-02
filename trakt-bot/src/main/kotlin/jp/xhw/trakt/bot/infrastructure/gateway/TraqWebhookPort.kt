package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.bot.port.WebhookPort
import jp.xhw.trakt.rest.apis.WebhookApi
import jp.xhw.trakt.rest.models.PatchWebhookRequest
import jp.xhw.trakt.rest.models.PostWebhookRequest
import kotlin.time.Instant

internal class TraqWebhookPort(
    private val apiGateway: TraqApiGateway,
) : WebhookPort {
    override suspend fun fetchWebhooks(all: Boolean): List<Webhook> =
        apiGateway.webhookApi
            .getWebhooks(all)
            .bodyOrThrow(operation = "fetchWebhooks(all=$all)")
            .map { it.toModel() }

    override suspend fun createWebhook(
        name: String,
        description: String,
        channelId: ChannelId,
        secret: String,
    ): Webhook =
        apiGateway.webhookApi
            .createWebhook(
                PostWebhookRequest(
                    name = name,
                    description = description,
                    channelId = channelId.value,
                    secret = secret,
                ),
            ).bodyOrThrow(operation = "createWebhook(name=$name, channelId=${channelId.value})")
            .toModel()

    override suspend fun fetchWebhook(webhookId: WebhookId): Webhook =
        apiGateway.webhookApi
            .getWebhook(webhookId.value)
            .bodyOrThrow(operation = "fetchWebhook(webhookId=${webhookId.value})")
            .toModel()

    override suspend fun editWebhook(
        webhookId: WebhookId,
        name: String?,
        description: String?,
        channelId: ChannelId?,
        secret: String?,
        ownerId: UserId?,
    ) {
        apiGateway.webhookApi
            .editWebhook(
                webhookId = webhookId.value,
                patchWebhookRequest =
                    PatchWebhookRequest(
                        name = name,
                        description = description,
                        channelId = channelId?.value,
                        secret = secret,
                        ownerId = ownerId?.value,
                    ),
            ).requireSuccess(operation = "editWebhook(webhookId=${webhookId.value})")
    }

    override suspend fun deleteWebhook(webhookId: WebhookId) {
        apiGateway.webhookApi
            .deleteWebhook(webhookId.value)
            .requireSuccess(operation = "deleteWebhook(webhookId=${webhookId.value})")
    }

    override suspend fun postWebhook(
        webhookId: WebhookId,
        content: String,
        signature: String?,
        channelId: ChannelId?,
        embed: Boolean,
    ) {
        apiGateway.webhookApi
            .postWebhook(
                webhookId = webhookId.value,
                xTRAQSignature = signature,
                xTRAQChannelId = channelId?.value?.toString(),
                embed = if (embed) 1 else 0,
                body = content,
            ).requireSuccess(operation = "postWebhook(webhookId=${webhookId.value})")
    }

    override suspend fun fetchWebhookMessages(
        webhookId: WebhookId,
        limit: Int?,
        offset: Int,
        since: Instant?,
        until: Instant?,
        inclusive: Boolean,
        order: SortDirection,
    ): List<Message> =
        apiGateway.webhookApi
            .getWebhookMessages(
                webhookId = webhookId.value,
                limit = limit,
                offset = offset,
                since = since,
                until = until,
                inclusive = inclusive,
                order = order.toWebhookMessageOrder(),
            ).bodyOrThrow(operation = "fetchWebhookMessages(webhookId=${webhookId.value})")
            .map { it.toModel() }

    override suspend fun deleteWebhookMessage(
        webhookId: WebhookId,
        messageId: MessageId,
    ) {
        apiGateway.webhookApi
            .deleteWebhookMessage(webhookId.value, messageId.value)
            .requireSuccess(operation = "deleteWebhookMessage(webhookId=${webhookId.value}, messageId=${messageId.value})")
    }

    override suspend fun downloadWebhookIcon(webhookId: WebhookId): ByteArray =
        apiGateway.webhookApi
            .getWebhookIcon(webhookId.value)
            .bodyOrThrow(operation = "downloadWebhookIcon(webhookId=${webhookId.value})")
            .value

    override suspend fun changeWebhookIcon(
        webhookId: WebhookId,
        file: ByteArray,
        fileName: String,
        contentType: String?,
    ) {
        apiGateway.webhookApi
            .changeWebhookIcon(webhookId.value, file.toFilePart(fileName, contentType))
            .requireSuccess(operation = "changeWebhookIcon(webhookId=${webhookId.value}, fileName=$fileName)")
    }
}

private fun SortDirection.toWebhookMessageOrder(): WebhookApi.OrderGetWebhookMessages =
    when (this) {
        SortDirection.ASCENDING -> WebhookApi.OrderGetWebhookMessages.ASC
        SortDirection.DESCENDING -> WebhookApi.OrderGetWebhookMessages.DESC
    }
