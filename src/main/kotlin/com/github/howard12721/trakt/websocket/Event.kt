package com.github.howard12721.trakt.websocket

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal sealed interface Event {

    object Unknown : Event

    object Close : Event

    companion object {
        fun decodeEvent(type: String, body: JsonElement): Event = when (type) {
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

            else -> Unknown
        }
    }
}

@Serializable
internal data class Ping(val eventTime: Instant) : Event

@Serializable
internal data class Joined(val eventTime: Instant, val channel: Channel) : Event

@Serializable
internal data class Left(val eventTime: Instant, val channel: Channel) : Event

@Serializable
internal data class MessageCreated(val eventTime: Instant, val message: Message) : Event

@Serializable
internal data class MessageDeleted(val eventTime: Instant, val message: DeletedMessage) : Event

@Serializable
internal data class MessageUpdated(val eventTime: Instant, val message: Message) : Event

@Serializable
internal data class DirectMessageCreated(val eventTime: Instant, val message: Message) : Event

@Serializable
internal data class DirectMessageDeleted(val eventTime: Instant, val message: DeletedDirectMessage) : Event

@Serializable
internal data class DirectMessageUpdated(val eventTime: Instant, val message: Message) : Event

@Serializable
@OptIn(ExperimentalUuidApi::class)
internal data class BotMessageStampsUpdated(
    val eventTime: Instant,
    val messageId: Uuid,
    val stamps: List<ReactionStamp>
) : Event

@Serializable
internal data class ChannelCreated(val eventTime: Instant, val channel: Channel) : Event

@Serializable
internal data class ChannelTopicChanged(
    val eventTime: Instant,
    val channel: Channel,
    val topic: String,
    val updater: User
) : Event

@Serializable
internal data class UserCreated(val eventTime: Instant, val user: User) : Event

@Serializable
internal data class UserActivated(val eventTime: Instant, val user: User) : Event

@Serializable
internal data class UserGroupCreated(val eventTime: Instant, val group: UserGroup) : Event

@Serializable
@OptIn(ExperimentalUuidApi::class)
internal data class UserGroupUpdated(val eventTime: Instant, val groupId: Uuid) : Event

@Serializable
@OptIn(ExperimentalUuidApi::class)
internal data class UserGroupDeleted(val eventTime: Instant, val groupId: Uuid) : Event

@Serializable
internal data class UserGroupMemberAdded(val eventTime: Instant, val groupMember: UserGroupMember) : Event

@Serializable
internal data class UserGroupMemberUpdated(val eventTime: Instant, val groupMember: UserGroupMember) : Event

@Serializable
internal data class UserGroupMemberRemoved(val eventTime: Instant, val groupMember: UserGroupMember) : Event

@Serializable
internal data class UserGroupAdminAdded(val eventTime: Instant, val userGroup: UserGroupMember) : Event

@Serializable
internal data class UserGroupAdminRemoved(val eventTime: Instant, val userGroup: UserGroupMember) : Event

@Serializable
@OptIn(ExperimentalUuidApi::class)
internal data class StampCreated(
    val eventTime: Instant,
    val id: Uuid,
    val name: String,
    val fileId: Uuid,
    val creator: User
) : Event

@Serializable
@OptIn(ExperimentalUuidApi::class)
internal data class TagAdded(val eventTime: Instant, val tagId: Uuid, val tag: String) : Event

@Serializable
@OptIn(ExperimentalUuidApi::class)
internal data class TagRemoved(val eventTime: Instant, val tagId: Uuid, val tag: String) : Event
