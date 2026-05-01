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
            UserJoined(userId = id.userId())
        }

        is WsUserUpdated -> {
            UserUpdated(userId = id.userId())
        }

        is WsUserTagsUpdated -> {
            UserTagsUpdated(userId = id.userId(), tagId = UserTagId(tagId))
        }

        is WsUserIconUpdated -> {
            UserIconUpdated(userId = id.userId())
        }

        is WsUserWebRtcStateChanged -> {
            UserWebRtcStateChanged(
                userId = userId.userId(),
                channelId = channelId.channelId(),
                sessions = sessions.map { it.toModel() },
            )
        }

        is WsUserViewStateChanged -> {
            UserViewStateChanged(viewStates = viewStates.map { it.toModel() })
        }

        is WsUserOnline -> {
            UserOnline(userId = id.userId())
        }

        is WsUserOffline -> {
            UserOffline(userId = id.userId())
        }

        is WsUserGroupCreated -> {
            UserGroupCreated(groupId = id.groupId())
        }

        is WsUserGroupUpdated -> {
            UserGroupUpdated(groupId = id.groupId())
        }

        is WsUserGroupDeleted -> {
            UserGroupDeleted(groupId = id.groupId())
        }

        is WsChannelCreated -> {
            UserChannelCreated(channelId = id.channelId(), dmUserId = dmUserId?.userId())
        }

        is WsChannelUpdated -> {
            UserChannelUpdated(channelId = id.channelId(), dmUserId = dmUserId?.userId())
        }

        is WsChannelDeleted -> {
            UserChannelDeleted(channelId = id.channelId(), dmUserId = dmUserId?.userId())
        }

        is WsChannelStared -> {
            UserChannelStared(channelId = id.channelId())
        }

        is WsChannelUnstared -> {
            UserChannelUnstared(channelId = id.channelId())
        }

        is WsChannelViewersChanged -> {
            UserChannelViewersChanged(
                channelId = id.channelId(),
                viewers = viewers.map { it.toModel() },
            )
        }

        is WsChannelSubscribersChanged -> {
            UserChannelSubscribersChanged(channelId = id.channelId())
        }

        is WsMessageCreated -> {
            UserMessageCreated(messageId = id.messageId(), isCiting = isCiting)
        }

        is WsMessageUpdated -> {
            UserMessageUpdated(messageId = id.messageId())
        }

        is WsMessageDeleted -> {
            UserMessageDeleted(messageId = id.messageId())
        }

        is WsMessageStamped -> {
            UserMessageStamped(
                messageId = messageId.messageId(),
                userId = userId.userId(),
                stampId = stampId.stampId(),
                count = count,
                createdAt = createdAt,
            )
        }

        is WsMessageUnstamped -> {
            UserMessageUnstamped(
                messageId = messageId.messageId(),
                userId = userId.userId(),
                stampId = stampId.stampId(),
            )
        }

        is WsMessagePinned -> {
            UserMessagePinned(messageId = messageId.messageId(), channelId = channelId.channelId())
        }

        is WsMessageUnpinned -> {
            UserMessageUnpinned(
                messageId = messageId.messageId(),
                channelId = channelId.channelId(),
            )
        }

        is WsMessageRead -> {
            UserMessageRead(messageId = id.messageId())
        }

        is WsStampCreated -> {
            UserStampCreated(stampId = id.stampId())
        }

        is WsStampUpdated -> {
            UserStampUpdated(stampId = id.stampId())
        }

        is WsStampDeleted -> {
            UserStampDeleted(stampId = id.stampId())
        }

        is WsStampPaletteCreated -> {
            UserStampPaletteCreated(stampPaletteId = id.stampPaletteId())
        }

        is WsStampPaletteUpdated -> {
            UserStampPaletteUpdated(stampPaletteId = id.stampPaletteId())
        }

        is WsStampPaletteDeleted -> {
            UserStampPaletteDeleted(stampPaletteId = id.stampPaletteId())
        }

        is WsClipFolderCreated -> {
            ClipFolderCreated(clipFolderId = id.clipFolderId())
        }

        is WsClipFolderUpdated -> {
            ClipFolderUpdated(clipFolderId = id.clipFolderId())
        }

        is WsClipFolderDeleted -> {
            ClipFolderDeleted(clipFolderId = id.clipFolderId())
        }

        is WsClipFolderMessageDeleted -> {
            ClipFolderMessageDeleted(
                clipFolderId = folderId.clipFolderId(),
                messageId = messageId.messageId(),
            )
        }

        is WsClipFolderMessageAdded -> {
            ClipFolderMessageAdded(
                clipFolderId = folderId.clipFolderId(),
                messageId = messageId.messageId(),
            )
        }

        is WsQallRoomStateChanged -> {
            QallRoomStateChanged(roomStates = roomStates.map { it.toModel() })
        }

        is WsQallSoundboardItemCreated -> {
            QallSoundboardItemCreated(
                soundId = soundId.qallSoundId(),
                name = name,
                creatorId = creatorId.userId(),
            )
        }

        is WsQallSoundboardItemDeleted -> {
            QallSoundboardItemDeleted(soundId = soundId.qallSoundId())
        }
    }

private fun Uuid.userId(): UserId = UserId(this)

private fun Uuid.groupId(): GroupId = GroupId(this)

private fun Uuid.channelId(): ChannelId = ChannelId(this)

private fun Uuid.messageId(): MessageId = MessageId(this)

private fun Uuid.stampId(): StampId = StampId(this)

private fun Uuid.stampPaletteId(): StampPaletteId = StampPaletteId(this)

private fun Uuid.clipFolderId(): ClipFolderId = ClipFolderId(this)

private fun Uuid.qallSoundId(): QallSoundId = QallSoundId(this)

private fun Uuid.qallRoomId(): QallRoomId = QallRoomId(this)

private fun WsWebRtcSession.toModel(): WebRtcSession =
    WebRtcSession(
        state = state,
        sessionId = sessionId,
    )

private fun WsViewState.toModel(): UserChannelViewState =
    UserChannelViewState(
        key = key,
        channelId = channelId.channelId(),
        state = state.toChannelViewState(),
    )

private fun WsChannelViewer.toModel(): ChannelViewer =
    ChannelViewer(
        userId = userId.userId(),
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
        roomId = roomId.qallRoomId(),
    )

private fun String.toChannelViewState(): ChannelViewState =
    when (lowercase()) {
        "stale_viewing" -> ChannelViewState.STALE_VIEWING
        "monitoring" -> ChannelViewState.MONITORING
        "editing" -> ChannelViewState.EDITING
        else -> ChannelViewState.NONE
    }
