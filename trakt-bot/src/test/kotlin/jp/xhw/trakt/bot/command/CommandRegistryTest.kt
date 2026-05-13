package jp.xhw.trakt.bot.command

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.bot.port.ChannelPort
import jp.xhw.trakt.bot.port.GroupPort
import jp.xhw.trakt.bot.port.MessagePort
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
        val result =
            CommandTokenizer.tokenize("""user !{"type":"user","raw":"@BOT_127","id":"019e145d-e5ca-7cdc-ba1b-92bcc98a2926"} tail""")

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
    fun literalNodeWithoutExecutorDoesNotShadowArgumentNode() =
        runBlocking {
            var handled = ""
            val registry =
                registry {
                    command("user") {
                        literal("me") {
                        }
                        string("name") {
                            executes { command ->
                                handled = command.args.string("name")
                            }
                        }
                    }
                }

            registry.handle(testContext(), event("!user me"))

            assertEquals("me", handled)
        }

    @Test
    fun missingArgumentsShowExecutableDescendantUsage() =
        runBlocking {
            val replies = mutableListOf<String>()
            val registry =
                registry {
                    command("admin") {
                        literal("ban") {
                            literal("dry-run") {
                            }
                            string("name") {
                                executes {}
                            }
                        }
                    }
                }

            registry.handle(testContext(replies), event("!admin ban"))

            assertEquals(
                listOf("引数が不足しています。 Usage: admin ban <name>\nhttps://q.trap.jp/messages/019e145d-e5ca-7cdc-ba1b-92bcc98a2927"),
                replies,
            )
        }

    @Test
    fun commandWithoutExecutorIsIgnored() =
        runBlocking {
            val replies = mutableListOf<String>()
            val registry =
                registry {
                    command("noop") {
                        literal("child") {
                        }
                    }
                }

            registry.handle(testContext(replies), event("!noop child"))

            assertEquals(emptyList(), replies)
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
            val resolved = mutableListOf<String>()
            val registry =
                registry {
                    command("user") {
                        user("user") {
                            executes { command ->
                                resolved += command.args.user("user").name
                            }
                        }
                    }
                }
            val userId = UserId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2940"))
            val context = testContext(users = listOf(userDetail(userId, "BOT_127")))

            registry.preloadArgumentCaches(context)
            registry.handle(context, event("!user ${userId.value}"))
            registry.handle(context, event("""!user !{"type":"user","raw":"@BOT_127","id":"${userId.value}"}"""))
            registry.handle(context, event("!user BOT_127"))

            assertEquals(listOf("BOT_127", "BOT_127", "BOT_127"), resolved)
        }

    @Test
    fun channelArgumentResolvesUuidMentionAndPath() =
        runBlocking {
            val resolved = mutableListOf<String>()
            val registry =
                registry {
                    command("channel") {
                        channel("channel") {
                            executes { command ->
                                resolved += command.args.channel("channel").name
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

            assertEquals(listOf("temp", "temp", "temp"), resolved)
        }

    @Test
    fun groupAndMessageArgumentsResolveSupportedForms() =
        runBlocking {
            val groups = mutableListOf<String>()
            val messages = mutableListOf<String>()
            val registry =
                registry {
                    command("refs") {
                        group("group") {
                            message("message") {
                                executes { command ->
                                    groups += command.args.group("group").name
                                    messages += command.args.message("message").content
                                }
                            }
                        }
                    }
                }
            val groupId = GroupId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2942"))
            val messageId = MessageId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2943"))
            val context =
                testContext(groups = listOf(group(groupId)), messages = listOf(message(messageId, content = "hello")))

            registry.handle(
                context,
                event("""!refs !{"type":"group","raw":"@howards","id":"${groupId.value}"} https://q.trap.jp/messages/${messageId.value}"""),
            )
            registry.handle(context, event("!refs ${groupId.value} ${messageId.value}"))

            assertEquals(listOf("howards", "howards"), groups)
            assertEquals(listOf("hello", "hello"), messages)
        }

    @Test
    fun idArgumentsRejectExistingUuidForMissingEntity() =
        runBlocking {
            val replies = mutableListOf<String>()
            var handled = false
            val registry =
                registry {
                    command("user") {
                        user("user") {
                            executes {
                                handled = true
                            }
                        }
                    }
                }

            registry.handle(
                testContext(replies = replies),
                event("!user 019e145d-e5ca-7cdc-ba1b-92bcc98a2940"),
            )

            assertEquals(false, handled)
            assertEquals(
                listOf("<user>が有効なuserではありません。\nhttps://q.trap.jp/messages/019e145d-e5ca-7cdc-ba1b-92bcc98a2927"),
                replies,
            )
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
        users: List<User.Detail> = emptyList(),
        channels: List<Channel.Detail> = emptyList(),
        groups: List<Group.Detail> = emptyList(),
        messages: List<Message.Detail> = emptyList(),
    ): BotContext {
        val context =
            BotContext(
                botId = null,
                origin = "q.trap.jp",
                botPort = fakePort(),
                selfPort = fakePort(),
                channelPort = fakeChannelPort(replies, channels),
                messagePort = fakeMessagePort(messages),
                userPort = fakeUserPort(users),
                stampPort = fakePort(),
                groupPort = fakeGroupPort(groups),
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
            if (method.name.startsWith("fetchChannels")) {
                val path = args?.getOrNull(1) as? String
                return@newProxyInstance ChannelDirectory(
                    publicChannels = channels.filter { path == null || it.name == path.substringAfterLast("/") },
                    directMessageChannels = emptyList(),
                )
            }
            if (method.name.startsWith("fetchChannelOrNull")) {
                val channelId = channelId(args?.get(0))
                return@newProxyInstance channels.firstOrNull { it.id == channelId }
            }
            when (method.returnType) {
                java.lang.Boolean.TYPE -> false
                java.lang.Integer.TYPE -> 0
                java.lang.Long.TYPE -> 0L
                java.lang.Void.TYPE -> Unit
                else -> null
            }
        } as ChannelPort

    private fun fakeUserPort(users: List<User.Detail>): UserPort =
        Proxy.newProxyInstance(
            UserPort::class.java.classLoader,
            arrayOf(UserPort::class.java),
        ) { _, method, args ->
            if (method.name.startsWith("fetchUsers")) {
                val name = args?.getOrNull(1) as? String
                return@newProxyInstance users.filter { name == null || it.name == name }.map { it.toBasic() }
            }
            if (method.name.startsWith("fetchUserOrNull")) {
                val userId = userId(args?.get(0))
                return@newProxyInstance users.firstOrNull { it.id == userId }
            }
            when (method.returnType) {
                java.lang.Boolean.TYPE -> false
                java.lang.Integer.TYPE -> 0
                java.lang.Long.TYPE -> 0L
                java.lang.Void.TYPE -> Unit
                else -> null
            }
        } as UserPort

    private fun fakeGroupPort(groups: List<Group.Detail>): GroupPort =
        Proxy.newProxyInstance(
            GroupPort::class.java.classLoader,
            arrayOf(GroupPort::class.java),
        ) { _, method, args ->
            if (method.name.startsWith("fetchGroupOrNull")) {
                val groupId = groupId(args?.get(0))
                return@newProxyInstance groups.firstOrNull { it.id == groupId }
            }
            when (method.returnType) {
                java.lang.Boolean.TYPE -> false
                java.lang.Integer.TYPE -> 0
                java.lang.Long.TYPE -> 0L
                java.lang.Void.TYPE -> Unit
                else -> null
            }
        } as GroupPort

    private fun fakeMessagePort(messages: List<Message.Detail>): MessagePort =
        Proxy.newProxyInstance(
            MessagePort::class.java.classLoader,
            arrayOf(MessagePort::class.java),
        ) { _, method, args ->
            if (method.name.startsWith("fetchMessageOrNull")) {
                val messageId = messageId(args?.get(0))
                return@newProxyInstance messages.firstOrNull { it.id == messageId }
            }
            when (method.returnType) {
                java.lang.Boolean.TYPE -> false
                java.lang.Integer.TYPE -> 0
                java.lang.Long.TYPE -> 0L
                java.lang.Void.TYPE -> Unit
                else -> null
            }
        } as MessagePort

    private fun userDetail(
        id: UserId,
        name: String,
    ): User.Detail =
        User.Detail(
            id = id,
            name = name,
            displayName = name,
            iconFile = File.Ref(FileId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2944"))),
            isBot = true,
            state = UserState.ACTIVE,
            updatedAt = Instant.parse("2026-05-12T00:00:00Z"),
            twitterId = "",
            lastOnline = null,
            tags = emptyList(),
            groups = emptyList(),
            bio = "",
            homeChannel = null,
        )

    private fun User.Detail.toBasic(): User.Basic =
        User.Basic(
            id = id,
            name = name,
            displayName = displayName,
            iconFile = iconFile,
            isBot = isBot,
            state = state,
            updatedAt = updatedAt,
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

    private fun message(
        id: MessageId,
        content: String = "",
    ): Message.Detail =
        Message.Detail(
            id = id,
            author = User.Ref(UserId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2928"))),
            channel = Channel.Ref(ChannelId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2929"))),
            content = content,
            createdAt = Instant.parse("2026-05-12T00:00:00Z"),
            updatedAt = Instant.parse("2026-05-12T00:00:00Z"),
            isPinned = false,
            stamps = emptyList(),
            threadId = null,
        )

    private fun group(id: GroupId): Group.Detail =
        Group.Detail(
            id = id,
            name = "howards",
            description = null,
            type = "",
            iconFile = File.Ref(FileId(Uuid.parse("019e145d-e5ca-7cdc-ba1b-92bcc98a2945"))),
            members = emptyList(),
            createdAt = Instant.parse("2026-05-12T00:00:00Z"),
            updatedAt = Instant.parse("2026-05-12T00:00:00Z"),
            adminUsers = emptyList(),
        )

    private fun userId(value: Any?): UserId =
        when (value) {
            is UserId -> value
            is Uuid -> UserId(value)
            else -> error("Unexpected user id argument: $value")
        }

    private fun channelId(value: Any?): ChannelId =
        when (value) {
            is ChannelId -> value
            is Uuid -> ChannelId(value)
            else -> error("Unexpected channel id argument: $value")
        }

    private fun groupId(value: Any?): GroupId =
        when (value) {
            is GroupId -> value
            is Uuid -> GroupId(value)
            else -> error("Unexpected group id argument: $value")
        }

    private fun messageId(value: Any?): MessageId =
        when (value) {
            is MessageId -> value
            is Uuid -> MessageId(value)
            else -> error("Unexpected message id argument: $value")
        }

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
