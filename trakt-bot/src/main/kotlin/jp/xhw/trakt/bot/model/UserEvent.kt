package jp.xhw.trakt.bot.model

import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlin.uuid.Uuid

sealed interface UserEvent : Event

@JvmInline
value class StampPaletteId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): StampPaletteId = parse(value)

        fun parse(value: String): StampPaletteId = StampPaletteId(Uuid.parse(value))
    }
}

/** スタンプパレット。 */
sealed interface StampPalette {
    val id: StampPaletteId

    /** ID のみを持つスタンプパレット参照。 */
    @JvmInline
    value class Ref(
        override val id: StampPaletteId,
    ) : StampPalette
}

@JvmInline
value class ClipFolderId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): ClipFolderId = parse(value)

        fun parse(value: String): ClipFolderId = ClipFolderId(Uuid.parse(value))
    }
}

@JvmInline
value class QallSoundId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): QallSoundId = parse(value)

        fun parse(value: String): QallSoundId = QallSoundId(Uuid.parse(value))
    }
}

/** Qall サウンド。 */
sealed interface QallSound {
    val id: QallSoundId

    /** ID のみを持つ Qall サウンド参照。 */
    @JvmInline
    value class Ref(
        override val id: QallSoundId,
    ) : QallSound
}

@JvmInline
value class QallRoomId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): QallRoomId = parse(value)

        fun parse(value: String): QallRoomId = QallRoomId(Uuid.parse(value))
    }
}

/** Qall ルーム。 */
sealed interface QallRoom {
    val id: QallRoomId

    /** ID のみを持つ Qall ルーム参照。 */
    @JvmInline
    value class Ref(
        override val id: QallRoomId,
    ) : QallRoom
}

@ConsistentCopyVisibility
data class WebRtcSession internal constructor(
    val state: String,
    val sessionId: String,
)

@ConsistentCopyVisibility
data class UserChannelViewState internal constructor(
    val key: String,
    val channel: Channel.Ref,
    val state: ChannelViewState,
)

@ConsistentCopyVisibility
data class QallParticipant internal constructor(
    val attributes: Map<String, String>?,
    val canPublish: Boolean,
    val identity: String,
    val joinedAt: Instant,
    val name: String,
)

@ConsistentCopyVisibility
data class QallRoomWithParticipants internal constructor(
    val isWebinar: Boolean,
    val metadata: String?,
    val participants: List<QallParticipant>,
    val room: QallRoom.Ref,
)

@ConsistentCopyVisibility
data class UserJoined internal constructor(
    val user: User.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserUpdated internal constructor(
    val user: User.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserTagsUpdated internal constructor(
    val user: User.Ref,
    val tag: UserTag.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserIconUpdated internal constructor(
    val user: User.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserWebRtcStateChanged internal constructor(
    val user: User.Ref,
    val channel: Channel.Ref,
    val sessions: List<WebRtcSession>,
) : UserEvent

@ConsistentCopyVisibility
data class UserViewStateChanged internal constructor(
    val viewStates: List<UserChannelViewState>,
) : UserEvent

@ConsistentCopyVisibility
data class UserOnline internal constructor(
    val user: User.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserOffline internal constructor(
    val user: User.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserGroupCreated internal constructor(
    val group: Group.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserGroupUpdated internal constructor(
    val group: Group.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserGroupDeleted internal constructor(
    val group: Group.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserChannelCreated internal constructor(
    val channel: Channel.Ref,
    val dmUser: User.Ref?,
) : UserEvent

@ConsistentCopyVisibility
data class UserChannelUpdated internal constructor(
    val channel: Channel.Ref,
    val dmUser: User.Ref?,
) : UserEvent

@ConsistentCopyVisibility
data class UserChannelDeleted internal constructor(
    val channel: Channel.Ref,
    val dmUser: User.Ref?,
) : UserEvent

@ConsistentCopyVisibility
data class UserChannelStared internal constructor(
    val channel: Channel.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserChannelUnstared internal constructor(
    val channel: Channel.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserChannelViewersChanged internal constructor(
    val channel: Channel.Ref,
    val viewers: List<ChannelViewer>,
) : UserEvent

@ConsistentCopyVisibility
data class UserChannelSubscribersChanged internal constructor(
    val channel: Channel.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserMessageCreated internal constructor(
    val message: Message.Ref,
    val isCiting: Boolean,
) : UserEvent

@ConsistentCopyVisibility
data class UserMessageUpdated internal constructor(
    val message: Message.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserMessageDeleted internal constructor(
    val message: Message.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserMessageStamped internal constructor(
    val message: Message.Ref,
    val user: User.Ref,
    val stamp: Stamp.Ref,
    val count: Int,
    val createdAt: Instant,
) : UserEvent

@ConsistentCopyVisibility
data class UserMessageUnstamped internal constructor(
    val message: Message.Ref,
    val user: User.Ref,
    val stamp: Stamp.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserMessagePinned internal constructor(
    val message: Message.Ref,
    val channel: Channel.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserMessageUnpinned internal constructor(
    val message: Message.Ref,
    val channel: Channel.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserMessageRead internal constructor(
    val message: Message.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserStampCreated internal constructor(
    val stamp: Stamp.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserStampUpdated internal constructor(
    val stamp: Stamp.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserStampDeleted internal constructor(
    val stamp: Stamp.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserStampPaletteCreated internal constructor(
    val stampPalette: StampPalette.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserStampPaletteUpdated internal constructor(
    val stampPalette: StampPalette.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserStampPaletteDeleted internal constructor(
    val stampPalette: StampPalette.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserClipFolderCreated internal constructor(
    val folder: ClipFolder.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserClipFolderUpdated internal constructor(
    val folder: ClipFolder.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserClipFolderDeleted internal constructor(
    val folder: ClipFolder.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserClipFolderMessageDeleted internal constructor(
    val folder: ClipFolder.Ref,
    val message: Message.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserClipFolderMessageAdded internal constructor(
    val folder: ClipFolder.Ref,
    val message: Message.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserQallRoomStateChanged internal constructor(
    val roomStates: List<QallRoomWithParticipants>,
) : UserEvent

@ConsistentCopyVisibility
data class UserQallSoundboardItemCreated internal constructor(
    val sound: QallSound.Ref,
    val name: String,
    val creator: User.Ref,
) : UserEvent

@ConsistentCopyVisibility
data class UserQallSoundboardItemDeleted internal constructor(
    val sound: QallSound.Ref,
) : UserEvent
