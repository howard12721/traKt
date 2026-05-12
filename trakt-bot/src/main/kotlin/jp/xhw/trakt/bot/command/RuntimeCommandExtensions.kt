package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.infrastructure.runtime.TraktClient
import jp.xhw.trakt.bot.model.BotMessageCreated

/**
 * Registers a command tree that handles [BotMessageCreated] events.
 *
 * Mention triggers use the Bot user ID fetched during runtime startup.
 */
fun TraktClient.commands(
    prefix: String,
    replyOnError: Boolean = true,
    helpDescription: String = "ヘルプを表示します",
    block: CommandRegistryBuilder.() -> Unit,
) {
    require(prefix.isNotEmpty()) { "Command prefix must not be empty" }

    val registry =
        CommandRegistry(
            CommandOptions(
                prefix = prefix,
                botUserIdProvider = { context.currentUserId?.value?.toString() },
                replyOnError = replyOnError,
                helpDescription = helpDescription,
            ),
        )
    CommandRegistryBuilder(registry).block()
    registry.installHelpIfAbsent()

    on<BotMessageCreated> { event ->
        registry.handle(this, event)
    }
}
