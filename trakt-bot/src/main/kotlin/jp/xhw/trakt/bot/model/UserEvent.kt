package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

sealed interface UserEvent : Event

/** User token のイベントであることを型名から判別しやすい共通型。 */
typealias UserDomainEvent = UserEvent

@JvmInline
value class StampPaletteId(
    val value: Uuid,
)

@JvmInline
value class ClipFolderId(
    val value: Uuid,
)

@JvmInline
value class QallSoundId(
    val value: Uuid,
)

@JvmInline
value class QallRoomId(
    val value: Uuid,
)

@ConsistentCopyVisibility
data class WebRtcSession internal constructor(
    val state: String,
    val sessionId: String,
)

@ConsistentCopyVisibility
data class UserChannelViewState internal constructor(
    val key: String,
    val channelId: ChannelId,
    val state: ChannelViewState,
) {
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

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
    val roomId: QallRoomId,
)

@ConsistentCopyVisibility
data class UserJoined internal constructor(
    val userId: UserId,
) : UserEvent {
    val user: UserHandle
        get() = UserHandle(userId)
}

@ConsistentCopyVisibility
data class UserUpdated internal constructor(
    val userId: UserId,
) : UserEvent {
    val user: UserHandle
        get() = UserHandle(userId)
}

@ConsistentCopyVisibility
data class UserTagsUpdated internal constructor(
    val userId: UserId,
    val tagId: UserTagId,
) : UserEvent {
    val user: UserHandle
        get() = UserHandle(userId)
}

@ConsistentCopyVisibility
data class UserIconUpdated internal constructor(
    val userId: UserId,
) : UserEvent {
    val user: UserHandle
        get() = UserHandle(userId)
}

@ConsistentCopyVisibility
data class UserWebRtcStateChanged internal constructor(
    val userId: UserId,
    val channelId: ChannelId,
    val sessions: List<WebRtcSession>,
) : UserEvent {
    val user: UserHandle
        get() = UserHandle(userId)

    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

@ConsistentCopyVisibility
data class UserViewStateChanged internal constructor(
    val viewStates: List<UserChannelViewState>,
) : UserEvent

@ConsistentCopyVisibility
data class UserOnline internal constructor(
    val userId: UserId,
) : UserEvent {
    val user: UserHandle
        get() = UserHandle(userId)
}

@ConsistentCopyVisibility
data class UserOffline internal constructor(
    val userId: UserId,
) : UserEvent {
    val user: UserHandle
        get() = UserHandle(userId)
}

@ConsistentCopyVisibility
data class UserGroupCreated internal constructor(
    val groupId: GroupId,
) : UserEvent {
    val group: GroupHandle
        get() = GroupHandle(groupId)
}

@ConsistentCopyVisibility
data class UserGroupUpdated internal constructor(
    val groupId: GroupId,
) : UserEvent {
    val group: GroupHandle
        get() = GroupHandle(groupId)
}

@ConsistentCopyVisibility
data class UserGroupDeleted internal constructor(
    val groupId: GroupId,
) : UserEvent {
    val group: GroupHandle
        get() = GroupHandle(groupId)
}

@ConsistentCopyVisibility
data class UserChannelCreated internal constructor(
    val channelId: ChannelId,
    val dmUserId: UserId?,
) : UserEvent {
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)

    val dmUser: UserHandle?
        get() = dmUserId?.let { UserHandle(it) }
}

@ConsistentCopyVisibility
data class UserChannelUpdated internal constructor(
    val channelId: ChannelId,
    val dmUserId: UserId?,
) : UserEvent {
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)

    val dmUser: UserHandle?
        get() = dmUserId?.let { UserHandle(it) }
}

@ConsistentCopyVisibility
data class UserChannelDeleted internal constructor(
    val channelId: ChannelId,
    val dmUserId: UserId?,
) : UserEvent {
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)

    val dmUser: UserHandle?
        get() = dmUserId?.let { UserHandle(it) }
}

@ConsistentCopyVisibility
data class UserChannelStared internal constructor(
    val channelId: ChannelId,
) : UserEvent {
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

@ConsistentCopyVisibility
data class UserChannelUnstared internal constructor(
    val channelId: ChannelId,
) : UserEvent {
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

@ConsistentCopyVisibility
data class UserChannelViewersChanged internal constructor(
    val channelId: ChannelId,
    val viewers: List<ChannelViewer>,
) : UserEvent {
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

@ConsistentCopyVisibility
data class UserChannelSubscribersChanged internal constructor(
    val channelId: ChannelId,
) : UserEvent {
    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

@ConsistentCopyVisibility
data class UserMessageCreated internal constructor(
    val messageId: MessageId,
    val isCiting: Boolean,
) : UserEvent {
    val message: MessageHandle
        get() = MessageHandle(messageId)
}

@ConsistentCopyVisibility
data class UserMessageUpdated internal constructor(
    val messageId: MessageId,
) : UserEvent {
    val message: MessageHandle
        get() = MessageHandle(messageId)
}

@ConsistentCopyVisibility
data class UserMessageDeleted internal constructor(
    val messageId: MessageId,
) : UserEvent {
    val message: MessageHandle
        get() = MessageHandle(messageId)
}

@ConsistentCopyVisibility
data class UserMessageStamped internal constructor(
    val messageId: MessageId,
    val userId: UserId,
    val stampId: StampId,
    val count: Int,
    val createdAt: Instant,
) : UserEvent {
    val message: MessageHandle
        get() = MessageHandle(messageId)

    val user: UserHandle
        get() = UserHandle(userId)

    val stamp: StampHandle
        get() = StampHandle(stampId)
}

@ConsistentCopyVisibility
data class UserMessageUnstamped internal constructor(
    val messageId: MessageId,
    val userId: UserId,
    val stampId: StampId,
) : UserEvent {
    val message: MessageHandle
        get() = MessageHandle(messageId)

    val user: UserHandle
        get() = UserHandle(userId)

    val stamp: StampHandle
        get() = StampHandle(stampId)
}

@ConsistentCopyVisibility
data class UserMessagePinned internal constructor(
    val messageId: MessageId,
    val channelId: ChannelId,
) : UserEvent {
    val message: MessageHandle
        get() = MessageHandle(messageId)

    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

@ConsistentCopyVisibility
data class UserMessageUnpinned internal constructor(
    val messageId: MessageId,
    val channelId: ChannelId,
) : UserEvent {
    val message: MessageHandle
        get() = MessageHandle(messageId)

    val channel: ChannelHandle
        get() = ChannelHandle(channelId)
}

@ConsistentCopyVisibility
data class UserMessageRead internal constructor(
    val messageId: MessageId,
) : UserEvent {
    val message: MessageHandle
        get() = MessageHandle(messageId)
}

@ConsistentCopyVisibility
data class UserStampCreated internal constructor(
    val stampId: StampId,
) : UserEvent {
    val stamp: StampHandle
        get() = StampHandle(stampId)
}

@ConsistentCopyVisibility
data class UserStampUpdated internal constructor(
    val stampId: StampId,
) : UserEvent {
    val stamp: StampHandle
        get() = StampHandle(stampId)
}

@ConsistentCopyVisibility
data class UserStampDeleted internal constructor(
    val stampId: StampId,
) : UserEvent {
    val stamp: StampHandle
        get() = StampHandle(stampId)
}

@ConsistentCopyVisibility
data class UserStampPaletteCreated internal constructor(
    val stampPaletteId: StampPaletteId,
) : UserEvent

@ConsistentCopyVisibility
data class UserStampPaletteUpdated internal constructor(
    val stampPaletteId: StampPaletteId,
) : UserEvent

@ConsistentCopyVisibility
data class UserStampPaletteDeleted internal constructor(
    val stampPaletteId: StampPaletteId,
) : UserEvent

@ConsistentCopyVisibility
data class UserClipFolderCreated internal constructor(
    val clipFolderId: ClipFolderId,
) : UserEvent

@ConsistentCopyVisibility
data class UserClipFolderUpdated internal constructor(
    val clipFolderId: ClipFolderId,
) : UserEvent

@ConsistentCopyVisibility
data class UserClipFolderDeleted internal constructor(
    val clipFolderId: ClipFolderId,
) : UserEvent

@ConsistentCopyVisibility
data class UserClipFolderMessageDeleted internal constructor(
    val clipFolderId: ClipFolderId,
    val messageId: MessageId,
) : UserEvent {
    val message: MessageHandle
        get() = MessageHandle(messageId)
}

@ConsistentCopyVisibility
data class UserClipFolderMessageAdded internal constructor(
    val clipFolderId: ClipFolderId,
    val messageId: MessageId,
) : UserEvent {
    val message: MessageHandle
        get() = MessageHandle(messageId)
}

@ConsistentCopyVisibility
data class UserQallRoomStateChanged internal constructor(
    val roomStates: List<QallRoomWithParticipants>,
) : UserEvent

@ConsistentCopyVisibility
data class UserQallSoundboardItemCreated internal constructor(
    val soundId: QallSoundId,
    val name: String,
    val creatorId: UserId,
) : UserEvent {
    val creator: UserHandle
        get() = UserHandle(creatorId)
}

@ConsistentCopyVisibility
data class UserQallSoundboardItemDeleted internal constructor(
    val soundId: QallSoundId,
) : UserEvent
