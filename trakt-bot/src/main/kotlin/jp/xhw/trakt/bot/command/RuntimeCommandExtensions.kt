package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.infrastructure.runtime.TraktClient
import jp.xhw.trakt.bot.model.BotMessageCreated

/**
 * [BotMessageCreated] イベントを処理するコマンドツリーを登録します。
 *
 * メンショントリガーは runtime 起動時に取得した Bot ユーザーIDを使って判定します。
 */
fun TraktClient.commands(
    prefix: String,
    replyOnError: Boolean = true,
    block: CommandRegistryBuilder.() -> Unit,
) {
    require(prefix.isNotEmpty()) { "Command prefix must not be empty" }

    val registry =
        CommandRegistry(
            CommandOptions(
                prefix = prefix,
                botUserIdProvider = { context.currentUserId?.value?.toString() },
                replyOnError = replyOnError,
            ),
        )
    CommandRegistryBuilder(registry).block()

    on<BotMessageCreated> { event ->
        registry.handle(this, event)
    }
}
