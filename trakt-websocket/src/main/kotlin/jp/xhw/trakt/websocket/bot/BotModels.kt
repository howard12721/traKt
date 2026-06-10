package jp.xhw.trakt.websocket.bot

import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class UserGroupMember(
    val groupId: Uuid,
    val userId: Uuid,
)

@Serializable
data class UserGroupMemberWithRole(
    val groupId: Uuid,
    val userId: Uuid,
    val role: String,
)

@Serializable
data class User(
    val id: Uuid,
    val name: String,
    val displayName: String,
    val iconId: Uuid,
    val bot: Boolean,
)

@Serializable
data class Embedded(
    val raw: String,
    val type: String,
    val id: Uuid,
)

@Serializable
data class DeletedMessage(
    val id: Uuid,
    val channelId: Uuid,
)

@Serializable
data class DeletedDirectMessage(
    val id: Uuid,
    val userId: Uuid,
    val channelId: Uuid,
)

@Serializable
data class ReactionStamp(
    val stampId: Uuid,
    val userId: Uuid,
    val count: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
)

@Serializable
data class UserGroup(
    val id: Uuid,
    val name: String,
    val description: String,
    val type: String,
    val icon: Uuid,
    val admins: List<UserGroupMember>,
    val members: List<UserGroupMemberWithRole>,
    val createdAt: Instant,
    val updatedAt: Instant,
)

@Serializable
data class Channel(
    val id: Uuid,
    val name: String,
    val path: String,
    val parentId: Uuid,
    val creator: User,
    val createdAt: Instant,
    val updatedAt: Instant,
)

@Serializable
data class Message(
    val id: Uuid,
    val user: User,
    val channelId: Uuid,
    val text: String,
    val plainText: String,
    val embedded: List<Embedded>,
    val createdAt: Instant,
    val updatedAt: Instant,
)
