package jp.xhw.trakt.websocket.bot

import jp.xhw.trakt.websocket.WsEventDecoder
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.time.Instant
import kotlin.uuid.Uuid

sealed interface BotEvent {
    companion object {
        val decoder: WsEventDecoder<BotEvent> =
            WsEventDecoder { frameData ->
                val envelope = Json.decodeFromString<BotEventEnvelope>(frameData)
                decodeEventOrNull(envelope.type, envelope.body)
            }

        fun decodeEventOrNull(
            type: String,
            body: JsonElement,
        ): BotEvent? =
            when (type) {
                "PING" -> Json.decodeFromJsonElement<Ping>(body)
                "JOINED" -> Json.decodeFromJsonElement<Joined>(body)
                "LEFT" -> Json.decodeFromJsonElement<Left>(body)
                "MESSAGE_CREATED" -> Json.decodeFromJsonElement<MessageCreated>(body)
                "MESSAGE_DELETED" -> Json.decodeFromJsonElement<MessageDeleted>(body)
                "MESSAGE_UPDATED" -> Json.decodeFromJsonElement<MessageUpdated>(body)
                "DIRECT_MESSAGE_CREATED" -> Json.decodeFromJsonElement<DirectMessageCreated>(body)
                "DIRECT_MESSAGE_DELETED" -> Json.decodeFromJsonElement<DirectMessageDeleted>(body)
                "DIRECT_MESSAGE_UPDATED" -> Json.decodeFromJsonElement<DirectMessageUpdated>(body)
                "BOT_MESSAGE_STAMPS_UPDATED" -> Json.decodeFromJsonElement<BotMessageStampsUpdated>(body)
                "CHANNEL_CREATED" -> Json.decodeFromJsonElement<ChannelCreated>(body)
                "CHANNEL_TOPIC_CHANGED" -> Json.decodeFromJsonElement<ChannelTopicChanged>(body)
                "USER_CREATED" -> Json.decodeFromJsonElement<UserCreated>(body)
                "USER_ACTIVATED" -> Json.decodeFromJsonElement<UserActivated>(body)
                "USER_GROUP_CREATED" -> Json.decodeFromJsonElement<UserGroupCreated>(body)
                "USER_GROUP_UPDATED" -> Json.decodeFromJsonElement<UserGroupUpdated>(body)
                "USER_GROUP_DELETED" -> Json.decodeFromJsonElement<UserGroupDeleted>(body)
                "USER_GROUP_MEMBER_ADDED" -> Json.decodeFromJsonElement<UserGroupMemberAdded>(body)
                "USER_GROUP_MEMBER_UPDATED" -> Json.decodeFromJsonElement<UserGroupMemberUpdated>(body)
                "USER_GROUP_MEMBER_REMOVED" -> Json.decodeFromJsonElement<UserGroupMemberRemoved>(body)
                "USER_GROUP_ADMIN_ADDED" -> Json.decodeFromJsonElement<UserGroupAdminAdded>(body)
                "USER_GROUP_ADMIN_REMOVED" -> Json.decodeFromJsonElement<UserGroupAdminRemoved>(body)
                "STAMP_CREATED" -> Json.decodeFromJsonElement<StampCreated>(body)
                "TAG_ADDED" -> Json.decodeFromJsonElement<TagAdded>(body)
                "TAG_REMOVED" -> Json.decodeFromJsonElement<TagRemoved>(body)
                else -> null
            }
    }
}

@Serializable
private data class BotEventEnvelope(
    val type: String,
    val reqId: Uuid,
    val body: JsonElement,
)

@Serializable
data class Ping(
    val eventTime: Instant,
) : BotEvent

@Serializable
data class Joined(
    val eventTime: Instant,
    val channel: Channel,
) : BotEvent

@Serializable
data class Left(
    val eventTime: Instant,
    val channel: Channel,
) : BotEvent

@Serializable
data class MessageCreated(
    val eventTime: Instant,
    val message: Message,
) : BotEvent

@Serializable
data class MessageDeleted(
    val eventTime: Instant,
    val message: DeletedMessage,
) : BotEvent

@Serializable
data class MessageUpdated(
    val eventTime: Instant,
    val message: Message,
) : BotEvent

@Serializable
data class DirectMessageCreated(
    val eventTime: Instant,
    val message: Message,
) : BotEvent

@Serializable
data class DirectMessageDeleted(
    val eventTime: Instant,
    val message: DeletedDirectMessage,
) : BotEvent

@Serializable
data class DirectMessageUpdated(
    val eventTime: Instant,
    val message: Message,
) : BotEvent

@Serializable
data class BotMessageStampsUpdated(
    val eventTime: Instant,
    val messageId: Uuid,
    val stamps: List<ReactionStamp>,
) : BotEvent

@Serializable
data class ChannelCreated(
    val eventTime: Instant,
    val channel: Channel,
) : BotEvent

@Serializable
data class ChannelTopicChanged(
    val eventTime: Instant,
    val channel: Channel,
    val topic: String,
    val updater: User,
) : BotEvent

@Serializable
data class UserCreated(
    val eventTime: Instant,
    val user: User,
) : BotEvent

@Serializable
data class UserActivated(
    val eventTime: Instant,
    val user: User,
) : BotEvent

@Serializable
data class UserGroupCreated(
    val eventTime: Instant,
    val group: UserGroup,
) : BotEvent

@Serializable
data class UserGroupUpdated(
    val eventTime: Instant,
    val groupId: Uuid,
) : BotEvent

@Serializable
data class UserGroupDeleted(
    val eventTime: Instant,
    val groupId: Uuid,
) : BotEvent

@Serializable
data class UserGroupMemberAdded(
    val eventTime: Instant,
    val groupMember: UserGroupMember,
) : BotEvent

@Serializable
data class UserGroupMemberUpdated(
    val eventTime: Instant,
    val groupMember: UserGroupMember,
) : BotEvent

@Serializable
data class UserGroupMemberRemoved(
    val eventTime: Instant,
    val groupMember: UserGroupMember,
) : BotEvent

@Serializable
data class UserGroupAdminAdded(
    val eventTime: Instant,
    val member: UserGroupMember,
) : BotEvent

@Serializable
data class UserGroupAdminRemoved(
    val eventTime: Instant,
    val member: UserGroupMember,
) : BotEvent

@Serializable
data class StampCreated(
    val eventTime: Instant,
    val id: Uuid,
    val name: String,
    val fileId: Uuid,
    val creator: User,
) : BotEvent

@Serializable
data class TagAdded(
    val eventTime: Instant,
    val tagId: Uuid,
    val tag: String,
) : BotEvent

@Serializable
data class TagRemoved(
    val eventTime: Instant,
    val tagId: Uuid,
    val tag: String,
) : BotEvent
