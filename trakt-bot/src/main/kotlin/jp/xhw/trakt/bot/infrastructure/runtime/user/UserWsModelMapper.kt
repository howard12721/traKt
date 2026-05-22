package jp.xhw.trakt.bot.infrastructure.runtime.user

import jp.xhw.trakt.bot.model.*
import kotlin.uuid.Uuid
import jp.xhw.trakt.websocket.user.ChannelCreated as WsChannelCreated
import jp.xhw.trakt.websocket.user.ChannelDeleted as WsChannelDeleted
import jp.xhw.trakt.websocket.user.ChannelStared as WsChannelStared
import jp.xhw.trakt.websocket.user.ChannelSubscribersChanged as WsChannelSubscribersChanged
import jp.xhw.trakt.websocket.user.ChannelUnstared as WsChannelUnstared
import jp.xhw.trakt.websocket.user.ChannelUpdated as WsChannelUpdated
import jp.xhw.trakt.websocket.user.ChannelViewer as WsChannelViewer
import jp.xhw.trakt.websocket.user.ChannelViewersChanged as WsChannelViewersChanged
import jp.xhw.trakt.websocket.user.ClipFolderCreated as WsClipFolderCreated
import jp.xhw.trakt.websocket.user.ClipFolderDeleted as WsClipFolderDeleted
import jp.xhw.trakt.websocket.user.ClipFolderMessageAdded as WsClipFolderMessageAdded
import jp.xhw.trakt.websocket.user.ClipFolderMessageDeleted as WsClipFolderMessageDeleted
import jp.xhw.trakt.websocket.user.ClipFolderUpdated as WsClipFolderUpdated
import jp.xhw.trakt.websocket.user.MessageCreated as WsMessageCreated
import jp.xhw.trakt.websocket.user.MessageDeleted as WsMessageDeleted
import jp.xhw.trakt.websocket.user.MessagePinned as WsMessagePinned
import jp.xhw.trakt.websocket.user.MessageRead as WsMessageRead
import jp.xhw.trakt.websocket.user.MessageStamped as WsMessageStamped
import jp.xhw.trakt.websocket.user.MessageUnpinned as WsMessageUnpinned
import jp.xhw.trakt.websocket.user.MessageUnstamped as WsMessageUnstamped
import jp.xhw.trakt.websocket.user.MessageUpdated as WsMessageUpdated
import jp.xhw.trakt.websocket.user.QallParticipant as WsQallParticipant
import jp.xhw.trakt.websocket.user.QallRoomStateChanged as WsQallRoomStateChanged
import jp.xhw.trakt.websocket.user.QallRoomWithParticipants as WsQallRoomWithParticipants
import jp.xhw.trakt.websocket.user.QallSoundboardItemCreated as WsQallSoundboardItemCreated
import jp.xhw.trakt.websocket.user.QallSoundboardItemDeleted as WsQallSoundboardItemDeleted
import jp.xhw.trakt.websocket.user.StampCreated as WsStampCreated
import jp.xhw.trakt.websocket.user.StampDeleted as WsStampDeleted
import jp.xhw.trakt.websocket.user.StampPaletteCreated as WsStampPaletteCreated
import jp.xhw.trakt.websocket.user.StampPaletteDeleted as WsStampPaletteDeleted
import jp.xhw.trakt.websocket.user.StampPaletteUpdated as WsStampPaletteUpdated
import jp.xhw.trakt.websocket.user.StampUpdated as WsStampUpdated
import jp.xhw.trakt.websocket.user.UserEvent as WebSocketEvent
import jp.xhw.trakt.websocket.user.UserGroupCreated as WsUserGroupCreated
import jp.xhw.trakt.websocket.user.UserGroupDeleted as WsUserGroupDeleted
import jp.xhw.trakt.websocket.user.UserGroupUpdated as WsUserGroupUpdated
import jp.xhw.trakt.websocket.user.UserIconUpdated as WsUserIconUpdated
import jp.xhw.trakt.websocket.user.UserJoined as WsUserJoined
import jp.xhw.trakt.websocket.user.UserOffline as WsUserOffline
import jp.xhw.trakt.websocket.user.UserOnline as WsUserOnline
import jp.xhw.trakt.websocket.user.UserTagsUpdated as WsUserTagsUpdated
import jp.xhw.trakt.websocket.user.UserUpdated as WsUserUpdated
import jp.xhw.trakt.websocket.user.UserViewStateChanged as WsUserViewStateChanged
import jp.xhw.trakt.websocket.user.UserWebRtcStateChanged as WsUserWebRtcStateChanged
import jp.xhw.trakt.websocket.user.ViewState as WsViewState
import jp.xhw.trakt.websocket.user.WebRtcSession as WsWebRtcSession

internal fun WebSocketEvent.toEventOrNull(): UserEvent? =
    when (this) {
        is WsUserJoined -> {
            UserEvents.Joined(user = id.userRef())
        }

        is WsUserUpdated -> {
            UserEvents.Updated(user = id.userRef())
        }

        is WsUserTagsUpdated -> {
            UserEvents.TagsUpdated(user = id.userRef(), tag = UserTag.Ref(UserTagId(tagId)))
        }

        is WsUserIconUpdated -> {
            UserEvents.IconUpdated(user = id.userRef())
        }

        is WsUserWebRtcStateChanged -> {
            UserEvents.WebRtcStateChanged(
                user = userId.userRef(),
                channel = channelId.channelRef(),
                sessions = sessions.map { it.toModel() },
            )
        }

        is WsUserViewStateChanged -> {
            UserEvents.ViewStateChanged(viewStates = viewStates.map { it.toModel() })
        }

        is WsUserOnline -> {
            UserEvents.Online(user = id.userRef())
        }

        is WsUserOffline -> {
            UserEvents.Offline(user = id.userRef())
        }

        is WsUserGroupCreated -> {
            UserEvents.GroupCreated(group = id.groupRef())
        }

        is WsUserGroupUpdated -> {
            UserEvents.GroupUpdated(group = id.groupRef())
        }

        is WsUserGroupDeleted -> {
            UserEvents.GroupDeleted(group = id.groupRef())
        }

        is WsChannelCreated -> {
            UserEvents.ChannelCreated(channel = id.channelRef(), dmUser = dmUserId?.userRef())
        }

        is WsChannelUpdated -> {
            UserEvents.ChannelUpdated(channel = id.channelRef(), dmUser = dmUserId?.userRef())
        }

        is WsChannelDeleted -> {
            UserEvents.ChannelDeleted(channel = id.channelRef(), dmUser = dmUserId?.userRef())
        }

        is WsChannelStared -> {
            UserEvents.ChannelStared(channel = id.channelRef())
        }

        is WsChannelUnstared -> {
            UserEvents.ChannelUnstared(channel = id.channelRef())
        }

        is WsChannelViewersChanged -> {
            UserEvents.ChannelViewersChanged(
                channel = id.channelRef(),
                viewers = viewers.map { it.toModel() },
            )
        }

        is WsChannelSubscribersChanged -> {
            UserEvents.ChannelSubscribersChanged(channel = id.channelRef())
        }

        is WsMessageCreated -> {
            UserEvents.MessageCreated(message = id.messageRef(), isCiting = isCiting)
        }

        is WsMessageUpdated -> {
            UserEvents.MessageUpdated(message = id.messageRef())
        }

        is WsMessageDeleted -> {
            UserEvents.MessageDeleted(message = id.messageRef())
        }

        is WsMessageStamped -> {
            UserEvents.MessageStamped(
                message = messageId.messageRef(),
                user = userId.userRef(),
                stamp = stampId.stampRef(),
                count = count,
                createdAt = createdAt,
            )
        }

        is WsMessageUnstamped -> {
            UserEvents.MessageUnstamped(
                message = messageId.messageRef(),
                user = userId.userRef(),
                stamp = stampId.stampRef(),
            )
        }

        is WsMessagePinned -> {
            UserEvents.MessagePinned(
                message = messageId.messageRef(),
                channel = channelId.channelRef(),
            )
        }

        is WsMessageUnpinned -> {
            UserEvents.MessageUnpinned(
                message = messageId.messageRef(),
                channel = channelId.channelRef(),
            )
        }

        is WsMessageRead -> {
            UserEvents.MessageRead(message = id.messageRef())
        }

        is WsStampCreated -> {
            UserEvents.StampCreated(stamp = id.stampRef())
        }

        is WsStampUpdated -> {
            UserEvents.StampUpdated(stamp = id.stampRef())
        }

        is WsStampDeleted -> {
            UserEvents.StampDeleted(stamp = id.stampRef())
        }

        is WsStampPaletteCreated -> {
            UserEvents.StampPaletteCreated(stampPalette = id.stampPaletteRef())
        }

        is WsStampPaletteUpdated -> {
            UserEvents.StampPaletteUpdated(stampPalette = id.stampPaletteRef())
        }

        is WsStampPaletteDeleted -> {
            UserEvents.StampPaletteDeleted(stampPalette = id.stampPaletteRef())
        }

        is WsClipFolderCreated -> {
            UserEvents.ClipFolderCreated(folder = id.clipFolderRef())
        }

        is WsClipFolderUpdated -> {
            UserEvents.ClipFolderUpdated(folder = id.clipFolderRef())
        }

        is WsClipFolderDeleted -> {
            UserEvents.ClipFolderDeleted(folder = id.clipFolderRef())
        }

        is WsClipFolderMessageDeleted -> {
            UserEvents.ClipFolderMessageDeleted(
                folder = folderId.clipFolderRef(),
                message = messageId.messageRef(),
            )
        }

        is WsClipFolderMessageAdded -> {
            UserEvents.ClipFolderMessageAdded(
                folder = folderId.clipFolderRef(),
                message = messageId.messageRef(),
            )
        }

        is WsQallRoomStateChanged -> {
            UserEvents.QallRoomStateChanged(roomStates = roomStates.map { it.toModel() })
        }

        is WsQallSoundboardItemCreated -> {
            UserEvents.QallSoundboardItemCreated(
                sound = soundId.qallSoundRef(),
                name = name,
                creator = creatorId.userRef(),
            )
        }

        is WsQallSoundboardItemDeleted -> {
            UserEvents.QallSoundboardItemDeleted(sound = soundId.qallSoundRef())
        }
    }

private fun Uuid.userRef(): User.Ref = User.Ref(UserId(this))

private fun Uuid.groupRef(): Group.Ref = Group.Ref(GroupId(this))

private fun Uuid.channelRef(): Channel.Ref = Channel.Ref(ChannelId(this))

private fun Uuid.messageRef(): Message.Ref = Message.Ref(MessageId(this))

private fun Uuid.stampRef(): Stamp.Ref = Stamp.Ref(StampId(this))

private fun Uuid.stampPaletteRef(): StampPalette.Ref = StampPalette.Ref(StampPaletteId(this))

private fun Uuid.clipFolderRef(): ClipFolder.Ref = ClipFolder.Ref(ClipFolderId(this))

private fun Uuid.qallSoundRef(): QallSound.Ref = QallSound.Ref(QallSoundId(this))

private fun Uuid.qallRoomRef(): QallRoom.Ref = QallRoom.Ref(QallRoomId(this))

private fun WsWebRtcSession.toModel(): WebRtcSession =
    WebRtcSession(
        state = state,
        sessionId = sessionId,
    )

private fun WsViewState.toModel(): UserChannelViewState =
    UserChannelViewState(
        key = key,
        channel = channelId.channelRef(),
        state = state.toChannelViewState(),
    )

private fun WsChannelViewer.toModel(): ChannelViewer =
    ChannelViewer(
        user = userId.userRef(),
        state = state.toChannelViewState(),
        updatedAt = updatedAt,
    )

private fun WsQallParticipant.toModel(): QallParticipant =
    QallParticipant(
        attributes = attributes,
        canPublish = canPublish,
        identity = identity,
        joinedAt = joinedAt,
        name = name,
    )

private fun WsQallRoomWithParticipants.toModel(): QallRoomWithParticipants =
    QallRoomWithParticipants(
        isWebinar = isWebinar,
        metadata = metadata,
        participants = participants.map { it.toModel() },
        room = roomId.qallRoomRef(),
    )

private fun String.toChannelViewState(): ChannelViewState =
    when (lowercase()) {
        "stale_viewing" -> ChannelViewState.STALE_VIEWING
        "monitoring" -> ChannelViewState.MONITORING
        "editing" -> ChannelViewState.EDITING
        else -> ChannelViewState.NONE
    }
