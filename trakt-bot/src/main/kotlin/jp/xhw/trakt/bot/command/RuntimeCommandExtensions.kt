package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.infrastructure.client.BotEventRegistrar
import jp.xhw.trakt.bot.onInitialized
import jp.xhw.trakt.bot.onMessageCreated

/**
 * Bot のメッセージ作成イベントを処理するコマンドツリーを登録します。
 *
 * メンショントリガーは runtime 起動時に取得した Bot ユーザーIDを使って判定します。
 */
fun BotEventRegistrar.commands(
    prefix: String,
    replyOnError: Boolean = true,
    errorMessages: CommandErrorMessages = DefaultCommandErrorMessages,
    block: CommandRegistryBuilder.() -> Unit,
) {
    require(prefix.isNotEmpty()) { "Command prefix must not be empty" }

    val registry =
        CommandRegistry(
            CommandOptions(
                prefix = prefix,
                botUserIdProvider = { context.currentUserId?.value?.toString() },
                replyOnError = replyOnError,
                errorMessages = errorMessages,
            ),
        )
    CommandRegistryBuilder(registry).block()

    onInitialized {
        registry.preloadArgumentCaches(this)
    }

    onMessageCreated { event ->
        registry.handle(this, event)
    }
}
