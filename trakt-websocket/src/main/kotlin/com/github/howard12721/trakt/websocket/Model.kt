package com.github.howard12721.trakt.websocket

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class UserGroupMember(
    val groupId: Uuid,
    val userId: Uuid,
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class RoledUserGroupMember(
    val groupId: Uuid,
    val userId: Uuid,
    val role: String
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class User(
    val id: Uuid,
    val name: String,
    val displayName: String,
    val iconId: Uuid,
    val bot: Boolean,
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class Embedded(
    val raw: String,
    val type: String,
    val id: Uuid,
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class DeletedMessage(
    val id: Uuid,
    val channelId: Uuid,
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class DeletedDirectMessage(
    val id: Uuid,
    val userId: Uuid,
    val channelId: Uuid,
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class ReactionStamp(
    val stampId: Uuid,
    val userId: Uuid,
    val count: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class UserGroup(
    val id: Uuid,
    val name: String,
    val description: String,
    val type: String,
    val icon: Uuid,
    val admins: List<UserGroupMember>,
    val members: List<RoledUserGroupMember>,
    val createdAt: Instant,
    val updatedAt: Instant,
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
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
@OptIn(ExperimentalUuidApi::class)
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
