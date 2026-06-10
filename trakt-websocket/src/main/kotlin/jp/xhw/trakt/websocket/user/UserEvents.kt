package jp.xhw.trakt.websocket.user

import jp.xhw.trakt.websocket.WsEventDecoder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.time.Instant
import kotlin.uuid.Uuid

sealed interface UserEvent {
    companion object {
        val decoder: WsEventDecoder<UserEvent> =
            WsEventDecoder { frameData ->
                val envelope = Json.decodeFromString<UserEventEnvelope>(frameData)
                decodeEventOrNull(envelope.type, envelope.body)
            }

        fun decodeEventOrNull(
            type: String,
            body: JsonElement,
        ): UserEvent? =
            when (type) {
                "USER_JOINED" -> Json.decodeFromJsonElement<UserJoined>(body)
                "USER_UPDATED" -> Json.decodeFromJsonElement<UserUpdated>(body)
                "USER_TAGS_UPDATED" -> Json.decodeFromJsonElement<UserTagsUpdated>(body)
                "USER_ICON_UPDATED" -> Json.decodeFromJsonElement<UserIconUpdated>(body)
                "USER_WEBRTC_STATE_CHANGED" -> Json.decodeFromJsonElement<UserWebRtcStateChanged>(body)
                "USER_VIEWSTATE_CHANGED" -> Json.decodeFromJsonElement<UserViewStateChanged>(body)
                "USER_ONLINE" -> Json.decodeFromJsonElement<UserOnline>(body)
                "USER_OFFLINE" -> Json.decodeFromJsonElement<UserOffline>(body)
                "USER_GROUP_CREATED" -> Json.decodeFromJsonElement<UserGroupCreated>(body)
                "USER_GROUP_UPDATED" -> Json.decodeFromJsonElement<UserGroupUpdated>(body)
                "USER_GROUP_DELETED" -> Json.decodeFromJsonElement<UserGroupDeleted>(body)
                "CHANNEL_CREATED" -> Json.decodeFromJsonElement<ChannelCreated>(body)
                "CHANNEL_UPDATED" -> Json.decodeFromJsonElement<ChannelUpdated>(body)
                "CHANNEL_DELETED" -> Json.decodeFromJsonElement<ChannelDeleted>(body)
                "CHANNEL_STARED" -> Json.decodeFromJsonElement<ChannelStared>(body)
                "CHANNEL_UNSTARED" -> Json.decodeFromJsonElement<ChannelUnstared>(body)
                "CHANNEL_VIEWERS_CHANGED" -> Json.decodeFromJsonElement<ChannelViewersChanged>(body)
                "CHANNEL_SUBSCRIBERS_CHANGED" -> Json.decodeFromJsonElement<ChannelSubscribersChanged>(body)
                "MESSAGE_CREATED" -> Json.decodeFromJsonElement<MessageCreated>(body)
                "MESSAGE_UPDATED" -> Json.decodeFromJsonElement<MessageUpdated>(body)
                "MESSAGE_DELETED" -> Json.decodeFromJsonElement<MessageDeleted>(body)
                "MESSAGE_STAMPED" -> Json.decodeFromJsonElement<MessageStamped>(body)
                "MESSAGE_UNSTAMPED" -> Json.decodeFromJsonElement<MessageUnstamped>(body)
                "MESSAGE_PINNED" -> Json.decodeFromJsonElement<MessagePinned>(body)
                "MESSAGE_UNPINNED" -> Json.decodeFromJsonElement<MessageUnpinned>(body)
                "MESSAGE_READ" -> Json.decodeFromJsonElement<MessageRead>(body)
                "STAMP_CREATED" -> Json.decodeFromJsonElement<StampCreated>(body)
                "STAMP_UPDATED" -> Json.decodeFromJsonElement<StampUpdated>(body)
                "STAMP_DELETED" -> Json.decodeFromJsonElement<StampDeleted>(body)
                "STAMP_PALETTE_CREATED" -> Json.decodeFromJsonElement<StampPaletteCreated>(body)
                "STAMP_PALETTE_UPDATED" -> Json.decodeFromJsonElement<StampPaletteUpdated>(body)
                "STAMP_PALETTE_DELETED" -> Json.decodeFromJsonElement<StampPaletteDeleted>(body)
                "CLIP_FOLDER_CREATED" -> Json.decodeFromJsonElement<ClipFolderCreated>(body)
                "CLIP_FOLDER_UPDATED" -> Json.decodeFromJsonElement<ClipFolderUpdated>(body)
                "CLIP_FOLDER_DELETED" -> Json.decodeFromJsonElement<ClipFolderDeleted>(body)
                "CLIP_FOLDER_MESSAGE_DELETED" -> Json.decodeFromJsonElement<ClipFolderMessageDeleted>(body)
                "CLIP_FOLDER_MESSAGE_ADDED" -> Json.decodeFromJsonElement<ClipFolderMessageAdded>(body)
                "QALL_ROOM_STATE_CHANGED" -> Json.decodeFromJsonElement<QallRoomStateChanged>(body)
                "QALL_SOUNDBOARD_ITEM_CREATED" -> Json.decodeFromJsonElement<QallSoundboardItemCreated>(body)
                "QALL_SOUNDBOARD_ITEM_DELETED" -> Json.decodeFromJsonElement<QallSoundboardItemDeleted>(body)
                else -> null
            }
    }
}

@Serializable
private data class UserEventEnvelope(
    val type: String,
    val body: JsonElement,
)

@Serializable
data class UserJoined(
    val id: Uuid,
) : UserEvent

@Serializable
data class UserUpdated(
    val id: Uuid,
) : UserEvent

@Serializable
data class UserTagsUpdated(
    val id: Uuid,
    @SerialName("tag_id")
    val tagId: Uuid,
) : UserEvent

@Serializable
data class UserIconUpdated(
    val id: Uuid,
) : UserEvent

@Serializable
data class UserWebRtcStateChanged(
    @SerialName("user_id")
    val userId: Uuid,
    @SerialName("channel_id")
    val channelId: Uuid,
    val sessions: List<WebRtcSession>,
) : UserEvent

@Serializable
data class UserViewStateChanged(
    @SerialName("view_states")
    val viewStates: List<ViewState>,
) : UserEvent

@Serializable
data class UserOnline(
    val id: Uuid,
) : UserEvent

@Serializable
data class UserOffline(
    val id: Uuid,
) : UserEvent

@Serializable
data class UserGroupCreated(
    val id: Uuid,
) : UserEvent

@Serializable
data class UserGroupUpdated(
    val id: Uuid,
) : UserEvent

@Serializable
data class UserGroupDeleted(
    val id: Uuid,
) : UserEvent

@Serializable
data class ChannelCreated(
    val id: Uuid,
    @SerialName("dm_user_id")
    val dmUserId: Uuid? = null,
) : UserEvent

@Serializable
data class ChannelUpdated(
    val id: Uuid,
    @SerialName("dm_user_id")
    val dmUserId: Uuid? = null,
) : UserEvent

@Serializable
data class ChannelDeleted(
    val id: Uuid,
    @SerialName("dm_user_id")
    val dmUserId: Uuid? = null,
) : UserEvent

@Serializable
data class ChannelStared(
    val id: Uuid,
) : UserEvent

@Serializable
data class ChannelUnstared(
    val id: Uuid,
) : UserEvent

@Serializable
data class ChannelViewersChanged(
    val id: Uuid,
    val viewers: List<ChannelViewer>,
) : UserEvent

@Serializable
data class ChannelSubscribersChanged(
    val id: Uuid,
) : UserEvent

@Serializable
data class MessageCreated(
    val id: Uuid,
    @SerialName("is_citing")
    val isCiting: Boolean,
) : UserEvent

@Serializable
data class MessageUpdated(
    val id: Uuid,
) : UserEvent

@Serializable
data class MessageDeleted(
    val id: Uuid,
) : UserEvent

@Serializable
data class MessageStamped(
    @SerialName("message_id")
    val messageId: Uuid,
    @SerialName("user_id")
    val userId: Uuid,
    @SerialName("stamp_id")
    val stampId: Uuid,
    val count: Int,
    @SerialName("created_at")
    val createdAt: Instant,
) : UserEvent

@Serializable
data class MessageUnstamped(
    @SerialName("message_id")
    val messageId: Uuid,
    @SerialName("user_id")
    val userId: Uuid,
    @SerialName("stamp_id")
    val stampId: Uuid,
) : UserEvent

@Serializable
data class MessagePinned(
    @SerialName("message_id")
    val messageId: Uuid,
    @SerialName("channel_id")
    val channelId: Uuid,
) : UserEvent

@Serializable
data class MessageUnpinned(
    @SerialName("message_id")
    val messageId: Uuid,
    @SerialName("channel_id")
    val channelId: Uuid,
) : UserEvent

@Serializable
data class MessageRead(
    val id: Uuid,
) : UserEvent

@Serializable
data class StampCreated(
    val id: Uuid,
) : UserEvent

@Serializable
data class StampUpdated(
    val id: Uuid,
) : UserEvent

@Serializable
data class StampDeleted(
    val id: Uuid,
) : UserEvent

@Serializable
data class StampPaletteCreated(
    val id: Uuid,
) : UserEvent

@Serializable
data class StampPaletteUpdated(
    val id: Uuid,
) : UserEvent

@Serializable
data class StampPaletteDeleted(
    val id: Uuid,
) : UserEvent

@Serializable
data class ClipFolderCreated(
    val id: Uuid,
) : UserEvent

@Serializable
data class ClipFolderUpdated(
    val id: Uuid,
) : UserEvent

@Serializable
data class ClipFolderDeleted(
    val id: Uuid,
) : UserEvent

@Serializable
data class ClipFolderMessageDeleted(
    @SerialName("folder_id")
    val folderId: Uuid,
    @SerialName("message_id")
    val messageId: Uuid,
) : UserEvent

@Serializable
data class ClipFolderMessageAdded(
    @SerialName("folder_id")
    val folderId: Uuid,
    @SerialName("message_id")
    val messageId: Uuid,
) : UserEvent

@Serializable
data class QallRoomStateChanged(
    val roomStates: List<QallRoomWithParticipants>,
) : UserEvent

@Serializable
data class QallSoundboardItemCreated(
    @SerialName("sound_id")
    val soundId: Uuid,
    val name: String,
    @SerialName("creator_id")
    val creatorId: Uuid,
) : UserEvent

@Serializable
data class QallSoundboardItemDeleted(
    @SerialName("sound_id")
    val soundId: Uuid,
) : UserEvent
