package jp.xhw.trakt.bot.command

/** コマンド解析や引数検証に失敗したときの返信文言を生成します。 */
interface CommandErrorMessages {
    fun unclosedQuote(quote: Char): String

    fun missingArguments(usage: String): String

    fun invalidArgument(
        name: String,
        expectedType: String,
    ): String

    fun unexpectedArgument(value: String): String
}

object DefaultCommandErrorMessages : CommandErrorMessages {
    override fun unclosedQuote(quote: Char): String = "引用符が閉じていません。: $quote"

    override fun missingArguments(usage: String): String = "引数が不足しています。 Usage: $usage"

    override fun invalidArgument(
        name: String,
        expectedType: String,
    ): String = "<$name>が有効な${expectedType}ではありません。"

    override fun unexpectedArgument(value: String): String = "予期しない引数: $value"
}
