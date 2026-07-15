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
        private val json = Json { ignoreUnknownKeys = true }

        val decoder: WsEventDecoder<UserEvent> =
            WsEventDecoder { frameData ->
                val envelope = json.decodeFromString<UserEventEnvelope>(frameData)
                decodeEventOrNull(envelope.type, envelope.body)
            }

        fun decodeEventOrNull(
            type: String,
            body: JsonElement,
        ): UserEvent? =
            when (type) {
                "USER_JOINED" -> json.decodeFromJsonElement<UserJoined>(body)
                "USER_UPDATED" -> json.decodeFromJsonElement<UserUpdated>(body)
                "USER_TAGS_UPDATED" -> json.decodeFromJsonElement<UserTagsUpdated>(body)
                "USER_ICON_UPDATED" -> json.decodeFromJsonElement<UserIconUpdated>(body)
                "USER_WEBRTC_STATE_CHANGED" -> json.decodeFromJsonElement<UserWebRtcStateChanged>(body)
                "USER_VIEWSTATE_CHANGED" -> json.decodeFromJsonElement<UserViewStateChanged>(body)
                "USER_ONLINE" -> json.decodeFromJsonElement<UserOnline>(body)
                "USER_OFFLINE" -> json.decodeFromJsonElement<UserOffline>(body)
                "USER_GROUP_CREATED" -> json.decodeFromJsonElement<UserGroupCreated>(body)
                "USER_GROUP_UPDATED" -> json.decodeFromJsonElement<UserGroupUpdated>(body)
                "USER_GROUP_DELETED" -> json.decodeFromJsonElement<UserGroupDeleted>(body)
                "CHANNEL_CREATED" -> json.decodeFromJsonElement<ChannelCreated>(body)
                "CHANNEL_UPDATED" -> json.decodeFromJsonElement<ChannelUpdated>(body)
                "CHANNEL_DELETED" -> json.decodeFromJsonElement<ChannelDeleted>(body)
                "CHANNEL_STARED" -> json.decodeFromJsonElement<ChannelStared>(body)
                "CHANNEL_UNSTARED" -> json.decodeFromJsonElement<ChannelUnstared>(body)
                "CHANNEL_VIEWERS_CHANGED" -> json.decodeFromJsonElement<ChannelViewersChanged>(body)
                "CHANNEL_SUBSCRIBERS_CHANGED" -> json.decodeFromJsonElement<ChannelSubscribersChanged>(body)
                "MESSAGE_CREATED" -> json.decodeFromJsonElement<MessageCreated>(body)
                "MESSAGE_UPDATED" -> json.decodeFromJsonElement<MessageUpdated>(body)
                "MESSAGE_DELETED" -> json.decodeFromJsonElement<MessageDeleted>(body)
                "MESSAGE_STAMPED" -> json.decodeFromJsonElement<MessageStamped>(body)
                "MESSAGE_UNSTAMPED" -> json.decodeFromJsonElement<MessageUnstamped>(body)
                "MESSAGE_PINNED" -> json.decodeFromJsonElement<MessagePinned>(body)
                "MESSAGE_UNPINNED" -> json.decodeFromJsonElement<MessageUnpinned>(body)
                "MESSAGE_READ" -> json.decodeFromJsonElement<MessageRead>(body)
                "STAMP_CREATED" -> json.decodeFromJsonElement<StampCreated>(body)
                "STAMP_UPDATED" -> json.decodeFromJsonElement<StampUpdated>(body)
                "STAMP_DELETED" -> json.decodeFromJsonElement<StampDeleted>(body)
                "STAMP_PALETTE_CREATED" -> json.decodeFromJsonElement<StampPaletteCreated>(body)
                "STAMP_PALETTE_UPDATED" -> json.decodeFromJsonElement<StampPaletteUpdated>(body)
                "STAMP_PALETTE_DELETED" -> json.decodeFromJsonElement<StampPaletteDeleted>(body)
                "CLIP_FOLDER_CREATED" -> json.decodeFromJsonElement<ClipFolderCreated>(body)
                "CLIP_FOLDER_UPDATED" -> json.decodeFromJsonElement<ClipFolderUpdated>(body)
                "CLIP_FOLDER_DELETED" -> json.decodeFromJsonElement<ClipFolderDeleted>(body)
                "CLIP_FOLDER_MESSAGE_DELETED" -> json.decodeFromJsonElement<ClipFolderMessageDeleted>(body)
                "CLIP_FOLDER_MESSAGE_ADDED" -> json.decodeFromJsonElement<ClipFolderMessageAdded>(body)
                "QALL_ROOM_STATE_CHANGED" -> json.decodeFromJsonElement<QallRoomStateChanged>(body)
                "QALL_SOUNDBOARD_ITEM_CREATED" -> json.decodeFromJsonElement<QallSoundboardItemCreated>(body)
                "QALL_SOUNDBOARD_ITEM_DELETED" -> json.decodeFromJsonElement<QallSoundboardItemDeleted>(body)
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
