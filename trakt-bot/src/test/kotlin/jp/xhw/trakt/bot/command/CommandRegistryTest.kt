package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.model.BotMessageCreated
import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.Message
import jp.xhw.trakt.bot.model.MessageId
import jp.xhw.trakt.bot.model.UserId
import jp.xhw.trakt.bot.port.BotPort
import jp.xhw.trakt.bot.port.ChannelPort
import jp.xhw.trakt.bot.port.FilePort
import jp.xhw.trakt.bot.port.GroupPort
import jp.xhw.trakt.bot.port.MessagePort
import jp.xhw.trakt.bot.port.StampPort
import jp.xhw.trakt.bot.port.UserPort
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Proxy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Instant
import kotlin.uuid.Uuid

class CommandRegistryTest {
    @Test
    fun tokenizesQuotedArguments() {
        val result = CommandTokenizer.tokenize("""say "hello world" 'again' escaped\ value""")

        assertIs<TokenizeResult.Success>(result)
        assertEquals(listOf("say", "hello world", "again", "escaped value"), result.tokens.map { it.value })
    }

    @Test
    fun mentionTriggerIsResolvedBeforePrefixTrigger() =
        runBlocking {
            val botUserId = "019e145d-e5ca-7cdc-ba1b-92bcc98a2926"
            var handled = ""
            val registry =
                registry(prefix = "!", botUserId = botUserId) {
                    command("help", description = "help") {
                        executes {
                            handled = it.commandName
                        }
                    }
                }

            registry.handle(
                testContext(),
                event("""!{"type":"user","raw":"@BOT_127","id":"$botUserId"} help"""),
            )

            assertEquals("help", handled)
        }

    @Test
    fun matchesSubLiteralCommand() =
        runBlocking {
            var handled = ""
            val registry =
                registry {
                    command("admin", description = "admin") {
                        literal("ban") {
                            string("name") {
                                executes { command ->
                                    handled = command.args.string("name")
                                }
                            }
                        }
                    }
                }

            registry.handle(testContext(), event("!admin ban alice"))

            assertEquals("alice", handled)
        }

    @Test
    fun literalNodeHasPriorityOverArgumentNode() =
        runBlocking {
            var handled = ""
            val registry =
                registry {
                    command("user", description = "user") {
                        string("name") {
                            executes {
                                handled = "argument"
                            }
                        }
                        literal("me") {
                            executes {
                                handled = "literal"
                            }
                        }
                    }
                }

            registry.handle(testContext(), event("!user me"))

            assertEquals("literal", handled)
        }

    @Test
    fun greedyStringConsumesRemainingInput() =
        runBlocking {
            var handled = ""
            val registry =
                registry {
                    command("say", description = "say") {
                        greedyString("text") {
                            executes { command ->
                                handled = command.args.string("text")
                            }
                        }
                    }
                }

            registry.handle(testContext(), event("!say hello   world"))

            assertEquals("hello   world", handled)
        }

    @Test
    fun generatedHelpListsCommands() =
        runBlocking {
            val replies = mutableListOf<String>()
            val registry =
                registry {
                    command("say", description = "say something") {
                        greedyString("text") {
                            executes {}
                        }
                    }
                }

            registry.handle(testContext(replies), event("!help"))

            assertEquals(
                """
                Available commands:
                !say <text> - say something
                !help - ヘルプを表示します
                !help <command> - ヘルプを表示します
                """.trimIndent(),
                replies.single(),
            )
        }

    @Test
    fun generatedHelpShowsCommandSyntax() =
        runBlocking {
            val replies = mutableListOf<String>()
            val registry =
                registry {
                    command("admin", description = "admin commands") {
                        literal("ban") {
                            userId("user") {
                                executes {}
                            }
                        }
                        literal("unban") {
                            userId("user") {
                                executes {}
                            }
                        }
                    }
                }

            registry.handle(testContext(replies), event("!help admin"))

            assertEquals(
                """
                Usage:
                !admin ban <user>
                !admin unban <user>

                admin commands
                """.trimIndent(),
                replies.single(),
            )
        }

    private fun registry(
        prefix: String = "!",
        botUserId: String? = null,
        block: CommandRegistryBuilder.() -> Unit,
    ): CommandRegistry =
        CommandRegistry(CommandOptions(prefix = prefix, botUserIdProvider = { botUserId })).also { registry ->
            CommandRegistryBuilder(registry).block()
            registry.installHelpIfAbsent()
        }

    private fun event(content: String): BotMessageCreated =
        BotMessageCreated(
            occurredAt = Instant.parse("2026-05-12T00:00:00Z"),
            message =
                Message(
                    id = MessageId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2927")),
                    authorId = UserId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2928")),
                    channelId = ChannelId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2929")),
                    content = content,
                    createdAt = Instant.parse("2026-05-12T00:00:00Z"),
                    updatedAt = Instant.parse("2026-05-12T00:00:00Z"),
                    isPinned = false,
                    stamps = emptyList(),
                    threadId = null,
                ),
        )

    private fun testContext(replies: MutableList<String> = mutableListOf()): BotContext {
        val context =
            BotContext(
            botId = null,
            origin = "q.trap.jp",
            botPort = fakePort(),
            selfPort = fakePort(),
            channelPort = fakeChannelPort(replies),
            messagePort = fakePort(),
            userPort = fakePort(),
            stampPort = fakePort(),
            groupPort = fakePort(),
            filePort = fakePort(),
        )
        context.currentUserId = UserId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2926"))
        return context
    }

    private fun fakeChannelPort(replies: MutableList<String>): ChannelPort =
        Proxy.newProxyInstance(ChannelPort::class.java.classLoader, arrayOf(ChannelPort::class.java)) { _, method, args ->
            if (method.name.startsWith("sendMessage")) {
                replies += args?.get(1) as String
                return@newProxyInstance message(ChannelId(args[0] as Uuid), args[1] as String)
            }
            when (method.returnType) {
                java.lang.Boolean.TYPE -> false
                java.lang.Integer.TYPE -> 0
                java.lang.Long.TYPE -> 0L
                java.lang.Void.TYPE -> Unit
                else -> null
            }
        } as ChannelPort

    private fun message(
        channelId: ChannelId,
        content: String,
    ): Message =
        Message(
            id = MessageId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2930")),
            authorId = UserId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2928")),
            channelId = channelId,
            content = content,
            createdAt = Instant.parse("2026-05-12T00:00:00Z"),
            updatedAt = Instant.parse("2026-05-12T00:00:00Z"),
            isPinned = false,
            stamps = emptyList(),
            threadId = null,
        )

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> fakePort(): T =
        Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, method, _ ->
            when (method.returnType) {
                java.lang.Boolean.TYPE -> false
                java.lang.Integer.TYPE -> 0
                java.lang.Long.TYPE -> 0L
                java.lang.Void.TYPE -> Unit
                else -> null
            }
        } as T
}
