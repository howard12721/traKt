package jp.xhw.trakt.bot.command

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.uuid.Uuid

internal data class CommandMention(
    val type: String,
    val raw: String?,
    val id: Uuid,
)

internal data class ParsedCommandMention(
    val mention: CommandMention,
    val start: Int,
    val endExclusive: Int,
    val token: String,
)

internal object CommandMentionParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parseToken(token: String): CommandMention? = parseAt(token, start = 0)?.takeIf { it.endExclusive == token.length }?.mention

    fun parseId(
        token: String,
        type: String,
    ): Uuid? = parseToken(token)?.takeIf { it.type == type }?.id

    fun parseAt(
        content: String,
        start: Int,
    ): ParsedCommandMention? {
        if (!content.startsWith("!{", startIndex = start)) {
            return null
        }

        val jsonEnd = findJsonEnd(content, start = start + 1) ?: return null
        val token = content.substring(start, jsonEnd + 1)
        val mention =
            runCatching {
                json.parseToJsonElement(token.removePrefix("!")) as? JsonObject
            }.getOrNull() ?: return null

        val type = mention["type"]?.jsonPrimitive?.content ?: return null
        val id = mention["id"]?.jsonPrimitive?.content?.let(::parseUuidOrNull) ?: return null
        val raw = mention["raw"]?.jsonPrimitive?.content

        return ParsedCommandMention(
            mention = CommandMention(type = type, raw = raw, id = id),
            start = start,
            endExclusive = jsonEnd + 1,
            token = token,
        )
    }

    private fun findJsonEnd(
        content: String,
        start: Int,
    ): Int? {
        var index = start
        var depth = 0
        var inString = false
        var escaped = false

        while (index < content.length) {
            val char = content[index]
            when {
                escaped -> {
                    escaped = false
                }

                inString && char == '\\' -> {
                    escaped = true
                }

                char == '"' -> {
                    inString = !inString
                }

                !inString && char == '{' -> {
                    depth++
                }

                !inString && char == '}' -> {
                    depth--
                    if (depth == 0) {
                        return index
                    }
                }
            }
            index++
        }

        return null
    }

    private fun parseUuidOrNull(value: String): Uuid? =
        runCatching {
            Uuid.parse(value)
        }.getOrNull()
}
