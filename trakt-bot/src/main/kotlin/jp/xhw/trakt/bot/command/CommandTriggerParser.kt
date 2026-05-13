package jp.xhw.trakt.bot.command

/** コマンドトリガーを取り除いた入力本文。 */
internal data class CommandInput(
    val body: String,
)

/** メッセージ本文からコマンド入力を取り出します。 */
internal object CommandTriggerParser {
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
        val parsed = CommandMentionParser.parseAt(content, start = 0) ?: return null
        if (parsed.mention.type != "user" || parsed.mention.id.toString() != botUserId) {
            return null
        }

        return CommandInput(content.substring(parsed.endExclusive).trim())
    }
}
