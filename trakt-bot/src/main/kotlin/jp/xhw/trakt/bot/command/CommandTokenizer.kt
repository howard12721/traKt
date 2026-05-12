package jp.xhw.trakt.bot.command

internal data class CommandToken(
    val value: String,
    val start: Int,
    val end: Int,
)

internal sealed interface TokenizeResult {
    data class Success(
        val tokens: List<CommandToken>,
    ) : TokenizeResult

    data class Failure(
        val message: String,
    ) : TokenizeResult
}

internal object CommandTokenizer {
    fun tokenize(input: String): TokenizeResult {
        val tokens = mutableListOf<CommandToken>()
        var index = 0

        while (index < input.length) {
            while (index < input.length && input[index].isWhitespace()) {
                index++
            }
            if (index >= input.length) {
                break
            }

            val start = index
            val value = StringBuilder()
            var quote: Char? = null

            while (index < input.length) {
                val char = input[index]
                when {
                    quote == null && char.isWhitespace() -> {
                        break
                    }

                    char == '\\' -> {
                        index++
                        if (index >= input.length) {
                            value.append('\\')
                        } else {
                            value.append(input[index])
                        }
                    }

                    quote != null && char == quote -> {
                        quote = null
                    }

                    quote == null && (char == '"' || char == '\'') -> {
                        quote = char
                    }

                    else -> {
                        value.append(char)
                    }
                }
                index++
            }

            if (quote != null) {
                return TokenizeResult.Failure("Unclosed quote: $quote")
            }

            tokens += CommandToken(value.toString(), start, index)
        }

        return TokenizeResult.Success(tokens)
    }
}
