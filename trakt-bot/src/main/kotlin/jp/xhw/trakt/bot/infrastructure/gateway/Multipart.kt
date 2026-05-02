package jp.xhw.trakt.bot.infrastructure.gateway

import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.core.*

internal fun ByteArray.toFilePart(
    fileName: String,
    contentType: String? = null,
): FormPart<InputProvider> =
    FormPart(
        key = "file",
        value =
            InputProvider {
                buildPacket {
                    writeFully(this@toFilePart)
                }
            },
        headers =
            Headers.build {
                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                contentType?.let { append(HttpHeaders.ContentType, it) }
            },
    )
