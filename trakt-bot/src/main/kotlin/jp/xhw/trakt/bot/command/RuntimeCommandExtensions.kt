package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.infrastructure.runtime.TraktClientBuilder
import jp.xhw.trakt.bot.model.BotMessageCreated
import jp.xhw.trakt.bot.model.Initialized

/**
 * [BotMessageCreated] イベントを処理するコマンドツリーを登録します。
 *
 * メンショントリガーは runtime 起動時に取得した Bot ユーザーIDを使って判定します。
 */
fun TraktClientBuilder.commands(
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

    on<Initialized> {
        registry.preloadArgumentCaches(this)
    }

    on<BotMessageCreated> { event ->
        registry.handle(this, event)
    }
}
