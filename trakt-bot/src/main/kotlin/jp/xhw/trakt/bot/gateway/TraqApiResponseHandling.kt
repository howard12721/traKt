package jp.xhw.trakt.bot.gateway

import io.ktor.client.statement.bodyAsText
import jp.xhw.trakt.rest.infrastructure.HttpResponse

internal class TraqApiException(
    val operation: String,
    val status: Int,
    val responseBody: String?,
) : RuntimeException(buildMessage(operation, status, responseBody)) {
    companion object {
        private const val MAX_BODY_PREVIEW_LENGTH = 512

        private fun buildMessage(
            operation: String,
            status: Int,
            responseBody: String?,
        ): String {
            val preview =
                responseBody
                    ?.takeIf { it.isNotBlank() }
                    ?.take(MAX_BODY_PREVIEW_LENGTH)
            return if (preview == null) {
                "traQ API call failed: operation=$operation status=$status"
            } else {
                "traQ API call failed: operation=$operation status=$status body=$preview"
            }
        }
    }
}

internal suspend fun HttpResponse<*>.requireSuccess(operation: String) {
    if (success) {
        return
    }
    throw toApiException(operation)
}

internal suspend fun <T : Any> HttpResponse<T>.bodyOrThrow(operation: String): T {
    requireSuccess(operation)
    return body()
}

internal suspend fun HttpResponse<*>.toApiException(operation: String): TraqApiException {
    val bodyPreview = runCatching { response.bodyAsText() }.getOrNull()
    return TraqApiException(operation = operation, status = status, responseBody = bodyPreview)
}
