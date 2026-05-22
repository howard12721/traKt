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

object UserEvents {
    @ConsistentCopyVisibility
    data class Joined internal constructor(
        val user: User.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class Updated internal constructor(
        val user: User.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class TagsUpdated internal constructor(
        val user: User.Ref,
        val tag: UserTag.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class IconUpdated internal constructor(
        val user: User.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class WebRtcStateChanged internal constructor(
        val user: User.Ref,
        val channel: Channel.Ref,
        val sessions: List<WebRtcSession>,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ViewStateChanged internal constructor(
        val viewStates: List<UserChannelViewState>,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class Online internal constructor(
        val user: User.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class Offline internal constructor(
        val user: User.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class GroupCreated internal constructor(
        val group: Group.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class GroupUpdated internal constructor(
        val group: Group.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class GroupDeleted internal constructor(
        val group: Group.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ChannelCreated internal constructor(
        val channel: Channel.Ref,
        val dmUser: User.Ref?,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ChannelUpdated internal constructor(
        val channel: Channel.Ref,
        val dmUser: User.Ref?,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ChannelDeleted internal constructor(
        val channel: Channel.Ref,
        val dmUser: User.Ref?,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ChannelStared internal constructor(
        val channel: Channel.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ChannelUnstared internal constructor(
        val channel: Channel.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ChannelViewersChanged internal constructor(
        val channel: Channel.Ref,
        val viewers: List<ChannelViewer>,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ChannelSubscribersChanged internal constructor(
        val channel: Channel.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class MessageCreated internal constructor(
        val message: Message.Ref,
        val isCiting: Boolean,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class MessageUpdated internal constructor(
        val message: Message.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class MessageDeleted internal constructor(
        val message: Message.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class MessageStamped internal constructor(
        val message: Message.Ref,
        val user: User.Ref,
        val stamp: Stamp.Ref,
        val count: Int,
        val createdAt: Instant,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class MessageUnstamped internal constructor(
        val message: Message.Ref,
        val user: User.Ref,
        val stamp: Stamp.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class MessagePinned internal constructor(
        val message: Message.Ref,
        val channel: Channel.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class MessageUnpinned internal constructor(
        val message: Message.Ref,
        val channel: Channel.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class MessageRead internal constructor(
        val message: Message.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class StampCreated internal constructor(
        val stamp: Stamp.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class StampUpdated internal constructor(
        val stamp: Stamp.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class StampDeleted internal constructor(
        val stamp: Stamp.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class StampPaletteCreated internal constructor(
        val stampPalette: StampPalette.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class StampPaletteUpdated internal constructor(
        val stampPalette: StampPalette.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class StampPaletteDeleted internal constructor(
        val stampPalette: StampPalette.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ClipFolderCreated internal constructor(
        val folder: ClipFolder.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ClipFolderUpdated internal constructor(
        val folder: ClipFolder.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ClipFolderDeleted internal constructor(
        val folder: ClipFolder.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ClipFolderMessageDeleted internal constructor(
        val folder: ClipFolder.Ref,
        val message: Message.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class ClipFolderMessageAdded internal constructor(
        val folder: ClipFolder.Ref,
        val message: Message.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class QallRoomStateChanged internal constructor(
        val roomStates: List<QallRoomWithParticipants>,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class QallSoundboardItemCreated internal constructor(
        val sound: QallSound.Ref,
        val name: String,
        val creator: User.Ref,
    ) : UserEvent

    @ConsistentCopyVisibility
    data class QallSoundboardItemDeleted internal constructor(
        val sound: QallSound.Ref,
    ) : UserEvent
}

typealias UserJoined = UserEvents.Joined
typealias UserUpdated = UserEvents.Updated
typealias UserTagsUpdated = UserEvents.TagsUpdated
typealias UserIconUpdated = UserEvents.IconUpdated
typealias UserWebRtcStateChanged = UserEvents.WebRtcStateChanged
typealias UserViewStateChanged = UserEvents.ViewStateChanged
typealias UserOnline = UserEvents.Online
typealias UserOffline = UserEvents.Offline
typealias UserGroupCreated = UserEvents.GroupCreated
typealias UserGroupUpdated = UserEvents.GroupUpdated
typealias UserGroupDeleted = UserEvents.GroupDeleted
typealias UserChannelCreated = UserEvents.ChannelCreated
typealias UserChannelUpdated = UserEvents.ChannelUpdated
typealias UserChannelDeleted = UserEvents.ChannelDeleted
typealias UserChannelStared = UserEvents.ChannelStared
typealias UserChannelUnstared = UserEvents.ChannelUnstared
typealias UserChannelViewersChanged = UserEvents.ChannelViewersChanged
typealias UserChannelSubscribersChanged = UserEvents.ChannelSubscribersChanged
typealias UserMessageCreated = UserEvents.MessageCreated
typealias UserMessageUpdated = UserEvents.MessageUpdated
typealias UserMessageDeleted = UserEvents.MessageDeleted
typealias UserMessageStamped = UserEvents.MessageStamped
typealias UserMessageUnstamped = UserEvents.MessageUnstamped
typealias UserMessagePinned = UserEvents.MessagePinned
typealias UserMessageUnpinned = UserEvents.MessageUnpinned
typealias UserMessageRead = UserEvents.MessageRead
typealias UserStampCreated = UserEvents.StampCreated
typealias UserStampUpdated = UserEvents.StampUpdated
typealias UserStampDeleted = UserEvents.StampDeleted
typealias UserStampPaletteCreated = UserEvents.StampPaletteCreated
typealias UserStampPaletteUpdated = UserEvents.StampPaletteUpdated
typealias UserStampPaletteDeleted = UserEvents.StampPaletteDeleted
typealias UserClipFolderCreated = UserEvents.ClipFolderCreated
typealias UserClipFolderUpdated = UserEvents.ClipFolderUpdated
typealias UserClipFolderDeleted = UserEvents.ClipFolderDeleted
typealias UserClipFolderMessageDeleted = UserEvents.ClipFolderMessageDeleted
typealias UserClipFolderMessageAdded = UserEvents.ClipFolderMessageAdded
typealias UserQallRoomStateChanged = UserEvents.QallRoomStateChanged
typealias UserQallSoundboardItemCreated = UserEvents.QallSoundboardItemCreated
typealias UserQallSoundboardItemDeleted = UserEvents.QallSoundboardItemDeleted
