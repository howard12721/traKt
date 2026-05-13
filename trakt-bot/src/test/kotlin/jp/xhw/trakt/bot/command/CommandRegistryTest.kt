package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.bot.port.ChannelPort
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
    fun tokenizesMentionJsonAsSingleToken() {
        val result = CommandTokenizer.tokenize("""user !{"type":"user","raw":"@BOT_127","id":"019e145d-e5ca-7cdc-ba1b-92bcc98a2926"} tail""")

        assertIs<TokenizeResult.Success>(result)
        assertEquals(
            listOf(
                "user",
                """!{"type":"user","raw":"@BOT_127","id":"019e145d-e5ca-7cdc-ba1b-92bcc98a2926"}""",
                "tail",
            ),
            result.tokens.map { it.value },
        )
    }

    @Test
    fun mentionTriggerIsResolvedBeforePrefixTrigger() =
        runBlocking {
            val botUserId = "019e145d-e5ca-7cdc-ba1b-92bcc98a2926"
            var handled = ""
            val registry =
                registry(prefix = "!", botUserId = botUserId) {
                    command("help") {
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
                    command("admin") {
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
                    command("user") {
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
                    command("say") {
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
    fun helpCommandIsNotGenerated() =
        runBlocking {
            val replies = mutableListOf<String>()
            val registry =
                registry {
                    command("say") {
                        greedyString("text") {
                            executes {}
                        }
                    }
                }

            registry.handle(testContext(replies), event("!help"))

            assertEquals(emptyList(), replies)
        }

    @Test
    fun userArgumentResolvesUuidMentionAndName() =
        runBlocking {
            val resolved = mutableListOf<UserId>()
            val registry =
                registry {
                    command("user") {
                        user("user") {
                            executes { command ->
                                resolved += command.args.user("user").id
                            }
                        }
                    }
                }
            val userId = UserId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2940"))
            val context = testContext(users = listOf(basicUser(userId, "BOT_127")))

            registry.preloadArgumentCaches(context)
            registry.handle(context, event("!user ${userId.value}"))
            registry.handle(context, event("""!user !{"type":"user","raw":"@BOT_127","id":"${userId.value}"}"""))
            registry.handle(context, event("!user BOT_127"))

            assertEquals(listOf(userId, userId, userId), resolved)
        }

    @Test
    fun channelArgumentResolvesUuidMentionAndPath() =
        runBlocking {
            val resolved = mutableListOf<ChannelId>()
            val registry =
                registry {
                    command("channel") {
                        channel("channel") {
                            executes { command ->
                                resolved += command.args.channel("channel").id
                            }
                        }
                    }
                }
            val channelId = ChannelId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2941"))
            val context = testContext(channels = listOf(channel(channelId, "times/25/howard127/---/temp")))

            registry.handle(context, event("!channel ${channelId.value}"))
            registry.handle(
                context,
                event("""!channel !{"type":"channel","raw":"#times/25/howard127/---/temp","id":"${channelId.value}"}"""),
            )
            registry.handle(context, event("!channel times/25/howard127/---/temp"))

            assertEquals(listOf(channelId, channelId, channelId), resolved)
        }

    @Test
    fun groupAndMessageArgumentsResolveSupportedForms() =
        runBlocking {
            val groups = mutableListOf<GroupId>()
            val messages = mutableListOf<MessageId>()
            val registry =
                registry {
                    command("refs") {
                        group("group") {
                            message("message") {
                                executes { command ->
                                    groups += command.args.group("group").id
                                    messages += command.args.message("message").id
                                }
                            }
                        }
                    }
                }
            val groupId = GroupId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2942"))
            val messageId = MessageId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2943"))

            registry.handle(
                testContext(),
                event("""!refs !{"type":"group","raw":"@howards","id":"${groupId.value}"} https://q.trap.jp/messages/${messageId.value}"""),
            )
            registry.handle(testContext(), event("!refs ${groupId.value} ${messageId.value}"))

            assertEquals(listOf(groupId, groupId), groups)
            assertEquals(listOf(messageId, messageId), messages)
        }

    private fun registry(
        prefix: String = "!",
        botUserId: String? = null,
        block: CommandRegistryBuilder.() -> Unit,
    ): CommandRegistry =
        CommandRegistry(CommandOptions(prefix = prefix, botUserIdProvider = { botUserId })).also { registry ->
            CommandRegistryBuilder(registry).block()
        }

    private fun event(content: String): BotMessageCreated =
        BotMessageCreated(
            occurredAt = Instant.parse("2026-05-12T00:00:00Z"),
            message =
                Message.Detail(
                    id = MessageId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2927")),
                    author = User.Ref(UserId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2928"))),
                    channel = Channel.Ref(ChannelId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2929"))),
                    content = content,
                    createdAt = Instant.parse("2026-05-12T00:00:00Z"),
                    updatedAt = Instant.parse("2026-05-12T00:00:00Z"),
                    isPinned = false,
                    stamps = emptyList(),
                    threadId = null,
                ),
        )

    private fun testContext(
        replies: MutableList<String> = mutableListOf(),
        users: List<User.Basic> = emptyList(),
        channels: List<Channel.Detail> = emptyList(),
    ): BotContext {
        val context =
            BotContext(
                botId = null,
                origin = "q.trap.jp",
                botPort = fakePort(),
                selfPort = fakePort(),
                channelPort = fakeChannelPort(replies, channels),
                messagePort = fakePort(),
                userPort = fakeUserPort(users),
                stampPort = fakePort(),
                groupPort = fakePort(),
                filePort = fakePort(),
            )
        context.currentUserId = UserId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2926"))
        return context
    }

    private fun fakeChannelPort(
        replies: MutableList<String>,
        channels: List<Channel.Detail>,
    ): ChannelPort =
        Proxy.newProxyInstance(
            ChannelPort::class.java.classLoader,
            arrayOf(ChannelPort::class.java),
        ) { _, method, args ->
            if (method.name.startsWith("sendMessage")) {
                replies += args?.get(1) as String
                val channelId =
                    when (val rawChannelId = args[0]) {
                        is ChannelId -> rawChannelId
                        is Uuid -> ChannelId(rawChannelId)
                        else -> error("Unexpected channel id argument: $rawChannelId")
                    }
                return@newProxyInstance message(channelId, args[1] as String)
            }
            if (method.name == "fetchChannels") {
                val path = args?.getOrNull(1) as? String
                return@newProxyInstance ChannelDirectory(
                    publicChannels = channels.filter { path == null || it.name == path.substringAfterLast("/") },
                    directMessageChannels = emptyList(),
                )
            }
            when (method.returnType) {
                java.lang.Boolean.TYPE -> false
                java.lang.Integer.TYPE -> 0
                java.lang.Long.TYPE -> 0L
                java.lang.Void.TYPE -> Unit
                else -> null
            }
        } as ChannelPort

    private fun fakeUserPort(users: List<User.Basic>): UserPort =
        Proxy.newProxyInstance(
            UserPort::class.java.classLoader,
            arrayOf(UserPort::class.java),
        ) { _, method, args ->
            if (method.name == "fetchUsers") {
                val name = args?.getOrNull(1) as? String
                return@newProxyInstance users.filter { name == null || it.name == name }
            }
            when (method.returnType) {
                java.lang.Boolean.TYPE -> false
                java.lang.Integer.TYPE -> 0
                java.lang.Long.TYPE -> 0L
                java.lang.Void.TYPE -> Unit
                else -> null
            }
        } as UserPort

    private fun basicUser(
        id: UserId,
        name: String,
    ): User.Basic =
        User.Basic(
            id = id,
            name = name,
            displayName = name,
            iconFile = File.Ref(FileId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2944"))),
            isBot = true,
            state = UserState.ACTIVE,
            updatedAt = Instant.parse("2026-05-12T00:00:00Z"),
        )

    private fun channel(
        id: ChannelId,
        path: String,
    ): Channel.Detail =
        Channel.Detail(
            id = id,
            parent = null,
            name = path.substringAfterLast("/"),
            isArchived = false,
            isForcedNotified = false,
            topic = ChannelTopic(""),
            children = emptyList(),
        )

    private fun message(
        channelId: ChannelId,
        content: String,
    ): Message =
        Message.Detail(
            id = MessageId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2930")),
            author = User.Ref(UserId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2928"))),
            channel = Channel.Ref(channelId),
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
