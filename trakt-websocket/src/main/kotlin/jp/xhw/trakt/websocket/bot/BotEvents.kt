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
        private val json = Json { ignoreUnknownKeys = true }

        val decoder: WsEventDecoder<BotEvent> =
            WsEventDecoder { frameData ->
                val envelope = json.decodeFromString<BotEventEnvelope>(frameData)
                decodeEventOrNull(envelope.type, envelope.body)
            }

        fun decodeEventOrNull(
            type: String,
            body: JsonElement,
        ): BotEvent? =
            when (type) {
                "PING" -> json.decodeFromJsonElement<Ping>(body)
                "JOINED" -> json.decodeFromJsonElement<Joined>(body)
                "LEFT" -> json.decodeFromJsonElement<Left>(body)
                "MESSAGE_CREATED" -> json.decodeFromJsonElement<MessageCreated>(body)
                "MESSAGE_DELETED" -> json.decodeFromJsonElement<MessageDeleted>(body)
                "MESSAGE_UPDATED" -> json.decodeFromJsonElement<MessageUpdated>(body)
                "DIRECT_MESSAGE_CREATED" -> json.decodeFromJsonElement<DirectMessageCreated>(body)
                "DIRECT_MESSAGE_DELETED" -> json.decodeFromJsonElement<DirectMessageDeleted>(body)
                "DIRECT_MESSAGE_UPDATED" -> json.decodeFromJsonElement<DirectMessageUpdated>(body)
                "BOT_MESSAGE_STAMPS_UPDATED" -> json.decodeFromJsonElement<BotMessageStampsUpdated>(body)
                "CHANNEL_CREATED" -> json.decodeFromJsonElement<ChannelCreated>(body)
                "CHANNEL_TOPIC_CHANGED" -> json.decodeFromJsonElement<ChannelTopicChanged>(body)
                "USER_CREATED" -> json.decodeFromJsonElement<UserCreated>(body)
                "USER_ACTIVATED" -> json.decodeFromJsonElement<UserActivated>(body)
                "USER_GROUP_CREATED" -> json.decodeFromJsonElement<UserGroupCreated>(body)
                "USER_GROUP_UPDATED" -> json.decodeFromJsonElement<UserGroupUpdated>(body)
                "USER_GROUP_DELETED" -> json.decodeFromJsonElement<UserGroupDeleted>(body)
                "USER_GROUP_MEMBER_ADDED" -> json.decodeFromJsonElement<UserGroupMemberAdded>(body)
                "USER_GROUP_MEMBER_UPDATED" -> json.decodeFromJsonElement<UserGroupMemberUpdated>(body)
                "USER_GROUP_MEMBER_REMOVED" -> json.decodeFromJsonElement<UserGroupMemberRemoved>(body)
                "USER_GROUP_ADMIN_ADDED" -> json.decodeFromJsonElement<UserGroupAdminAdded>(body)
                "USER_GROUP_ADMIN_REMOVED" -> json.decodeFromJsonElement<UserGroupAdminRemoved>(body)
                "STAMP_CREATED" -> json.decodeFromJsonElement<StampCreated>(body)
                "TAG_ADDED" -> json.decodeFromJsonElement<TagAdded>(body)
                "TAG_REMOVED" -> json.decodeFromJsonElement<TagRemoved>(body)
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
    val groupMember: UserGroupMember,
) : BotEvent

@Serializable
data class UserGroupAdminRemoved(
    val eventTime: Instant,
    val groupMember: UserGroupMember,
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
