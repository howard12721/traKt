package jp.xhw.trakt.bot.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant
import kotlin.uuid.Uuid

class ModelIdentityTest {
    private val now = Instant.fromEpochMilliseconds(0)

    @Test
    fun channelIdentityIsBasedOnId() {
        val id = ChannelId(uuid(1))
        val ref = Channel.Ref(id)
        val directMessage = Channel.DirectMessage(id = id, user = User.Ref(UserId(uuid(2))))
        val meta =
            Channel.Meta(
                id = id,
                parent = null,
                name = "general",
                creator = userMinimal(UserId(uuid(3))),
                path = ChannelPath("general"),
            )
        val detail =
            Channel.Detail(
                id = id,
                parent = null,
                name = "general",
                isArchived = false,
                isForcedNotified = false,
                topic = ChannelTopic("topic"),
                children = emptyList(),
            )

        assertSameIdentity(ref, detail)
        assertSameIdentity(directMessage, detail)
        assertSameIdentity(meta, detail)
    }

    @Test
    fun userIdentityIsBasedOnId() {
        val id = UserId(uuid(1))
        val ref = User.Ref(id)
        val minimal = userMinimal(id)
        val basic =
            User.Basic(
                id = id,
                name = "howard127",
                displayName = "howard",
                iconFile = File.Ref(FileId(uuid(2))),
                isBot = false,
                state = UserState.ACTIVE,
                updatedAt = now,
            )
        val detail =
            User.Detail(
                id = id,
                name = "howard127",
                displayName = "howard",
                iconFile = File.Ref(FileId(uuid(2))),
                isBot = false,
                state = UserState.ACTIVE,
                updatedAt = now,
                twitterId = "",
                lastOnline = null,
                tags = emptyList(),
                groups = emptyList(),
                bio = "",
                homeChannel = null,
            )

        assertSameIdentity(ref, detail)
        assertSameIdentity(minimal, detail)
        assertSameIdentity(basic, detail)
    }

    @Test
    fun messageIdentityIsBasedOnId() {
        val id = MessageId(uuid(1))
        val ref = Message.Ref(id)
        val detail =
            Message.Detail(
                id = id,
                author = User.Ref(UserId(uuid(2))),
                channel = Channel.Ref(ChannelId(uuid(3))),
                content = "hello",
                createdAt = now,
                updatedAt = now,
                isPinned = false,
                stamps = emptyList(),
                threadId = null,
            )

        assertSameIdentity(ref, detail)
    }

    @Test
    fun remainingEntityIdentitiesAreBasedOnId() {
        assertSameIdentity(Stamp.Ref(StampId(uuid(1))), stampBasic(StampId(uuid(1))))
        assertSameIdentity(stampBasic(StampId(uuid(1))), stampDetail(StampId(uuid(1))))
        assertSameIdentity(Group.Ref(GroupId(uuid(2))), groupDetail(GroupId(uuid(2))))
        assertSameIdentity(File.Ref(FileId(uuid(3))), fileMeta(FileId(uuid(3))))
        assertSameIdentity(ClipFolder.Ref(ClipFolderId(uuid(4))), clipFolderDetail(ClipFolderId(uuid(4))))
        assertSameIdentity(Webhook.Ref(WebhookId(uuid(5))), webhookDetail(WebhookId(uuid(5))))
        assertSameIdentity(ManagedBot.Ref(BotId(uuid(6))), managedBotBasic(BotId(uuid(6))))
        assertSameIdentity(managedBotBasic(BotId(uuid(6))), managedBotDetail(BotId(uuid(6))))
        assertSameIdentity(LoginSession.Ref(LoginSessionId(uuid(7))), LoginSession.Detail(LoginSessionId(uuid(7)), now))
        assertSameIdentity(
            ActiveOAuth2Token.Ref(OAuth2TokenId(uuid(8))),
            ActiveOAuth2Token.Detail(OAuth2TokenId(uuid(8)), clientId = "client", scopes = emptyList(), issuedAt = now),
        )
        assertSameIdentity(UserTag.Ref(UserTagId(uuid(9))), userTagDetail(UserTagId(uuid(9))))
        assertSameIdentity(StampPalette.Ref(StampPaletteId(uuid(10))), StampPalette.Ref(StampPaletteId(uuid(10))))
        assertSameIdentity(QallSound.Ref(QallSoundId(uuid(11))), QallSound.Ref(QallSoundId(uuid(11))))
        assertSameIdentity(QallRoom.Ref(QallRoomId(uuid(12))), QallRoom.Ref(QallRoomId(uuid(12))))
    }

    private fun assertSameIdentity(
        left: Any,
        right: Any,
    ) {
        assertEquals(left, right)
        assertEquals(right, left)
        assertEquals(left.hashCode(), right.hashCode())
        assertEquals(1, setOf(left, right).size)
    }

    private fun stampDetail(id: StampId) =
        Stamp.Detail(
            id = id,
            name = "stamp",
            creator = User.Ref(UserId(uuid(101))),
            file = File.Ref(FileId(uuid(102))),
            createdAt = now,
            updatedAt = now,
            isUnicode = false,
        )

    private fun stampBasic(id: StampId) =
        Stamp.Basic(
            id = id,
            name = "stamp",
            creator = User.Ref(UserId(uuid(101))),
            file = File.Ref(FileId(uuid(102))),
        )

    private fun groupDetail(id: GroupId) =
        Group.Detail(
            id = id,
            name = "group",
            description = null,
            type = "team",
            iconFile = File.Ref(FileId(uuid(103))),
            members = emptyList(),
            createdAt = now,
            updatedAt = now,
            adminUsers = emptyList(),
        )

    private fun fileMeta(id: FileId) =
        FileMeta(
            id = id,
            name = "file.txt",
            mime = "text/plain",
            size = 1,
            md5 = "md5",
            isAnimatedImage = false,
            createdAt = now,
            channel = null,
            uploader = null,
        )

    private fun clipFolderDetail(id: ClipFolderId) =
        ClipFolder.Detail(
            id = id,
            name = "clips",
            description = "",
            owner = User.Ref(UserId(uuid(104))),
            createdAt = now,
        )

    private fun webhookDetail(id: WebhookId) =
        Webhook.Detail(
            id = id,
            botUser = User.Ref(UserId(uuid(105))),
            displayName = "webhook",
            description = "",
            isSecure = true,
            channel = Channel.Ref(ChannelId(uuid(106))),
            owner = User.Ref(UserId(uuid(107))),
            createdAt = now,
            updatedAt = now,
        )

    private fun managedBotDetail(id: BotId) =
        ManagedBot.Detail(
            id = id,
            botUser = User.Ref(UserId(uuid(108))),
            description = "",
            developer = User.Ref(UserId(uuid(109))),
            subscribeEvents = emptyList(),
            mode = ManagedBotMode.WEBSOCKET,
            state = ManagedBotState.ACTIVE,
            createdAt = now,
            updatedAt = now,
            tokens = BotTokens(verificationToken = "verification", accessToken = "access"),
            endpoint = "",
            privileged = false,
            channels = emptyList(),
        )

    private fun managedBotBasic(id: BotId) =
        ManagedBot.Basic(
            id = id,
            botUser = User.Ref(UserId(uuid(108))),
            description = "",
            developer = User.Ref(UserId(uuid(109))),
            subscribeEvents = emptyList(),
            mode = ManagedBotMode.WEBSOCKET,
            state = ManagedBotState.ACTIVE,
            createdAt = now,
            updatedAt = now,
        )

    private fun userTagDetail(id: UserTagId) =
        UserTag.Detail(
            id = id,
            tag = "tag",
            isLocked = false,
            createdAt = now,
            updatedAt = now,
        )

    private fun userMinimal(id: UserId) =
        User.Minimal(
            id = id,
            name = "howard127",
            displayName = "howard",
            iconFile = File.Ref(FileId(uuid(2))),
            isBot = false,
        )

    private fun uuid(seed: Int): Uuid = Uuid.parse("00000000-0000-0000-0000-${seed.toString().padStart(12, '0')}")
}
