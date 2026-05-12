package jp.xhw.trakt.bot.command

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

// コマンドの本体
internal data class CommandInput(
    val body: String,
)

// コマンドのprefixを取り除いて本体だけを返すヘルパー
internal object CommandTriggerParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(
        content: String,
        options: CommandOptions,
    ): CommandInput? = parseMention(content, options) ?: parsePrefix(content, options)

    private fun parsePrefix(
        content: String,
        options: CommandOptions,
    ): CommandInput? {
        if (!content.startsWith(options.prefix)) {
            return null
        }
        return CommandInput(content.removePrefix(options.prefix).trim())
    }

    private fun parseMention(
        content: String,
        options: CommandOptions,
    ): CommandInput? {
        val botUserId = options.botUserIdProvider() ?: return null
        if (!content.startsWith("!{")) {
            return null
        }

        val jsonEnd = findJsonEnd(content, start = 1) ?: return null
        val mention =
            runCatching {
                json.parseToJsonElement(content.substring(1, jsonEnd + 1)) as? JsonObject
            }.getOrNull() ?: return null

        val type = mention["type"]?.jsonPrimitive?.content
        val id = mention["id"]?.jsonPrimitive?.content
        if (type != "user" || id != botUserId) {
            return null
        }

        return CommandInput(content.substring(jsonEnd + 1).trim())
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
}
