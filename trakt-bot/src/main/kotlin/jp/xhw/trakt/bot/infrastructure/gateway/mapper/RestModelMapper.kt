package jp.xhw.trakt.bot.infrastructure.gateway.mapper

import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.rest.models.Channel as RestChannel
import jp.xhw.trakt.rest.models.ChannelList as RestChannelList
import jp.xhw.trakt.rest.models.DMChannel as RestDMChannel
import jp.xhw.trakt.rest.models.FileInfo as RestFileInfo
import jp.xhw.trakt.rest.models.Message as RestMessage
import jp.xhw.trakt.rest.models.MessagePin as RestMessagePin
import jp.xhw.trakt.rest.models.MessageStamp as RestMessageStamp
import jp.xhw.trakt.rest.models.Pin as RestPin
import jp.xhw.trakt.rest.models.Stamp as RestStamp
import jp.xhw.trakt.rest.models.StampWithThumbnail as RestStampWithThumbnail
import jp.xhw.trakt.rest.models.User as RestUser
import jp.xhw.trakt.rest.models.UserAccountState as RestUserAccountState
import jp.xhw.trakt.rest.models.UserDetail as RestUserDetail
import jp.xhw.trakt.rest.models.UserGroup as RestUserGroup
import jp.xhw.trakt.rest.models.UserGroupMember as RestUserGroupMember
import jp.xhw.trakt.rest.models.UserStats as RestUserStats
import jp.xhw.trakt.rest.models.UserStatsStamp as RestUserStatsStamp
import jp.xhw.trakt.rest.models.UserTag as RestUserTag

// ── Channel ──

internal fun RestChannel.toDetail(): Channel.Detail =
    Channel.Detail(
        id = ChannelId(id),
        parentId = parentId?.let { ChannelId(it) },
        name = name,
        isArchived = archived,
        isForcedNotified = force,
        topic = ChannelTopic(topic),
        childrenIds = children.map { ChannelId(it) },
    )

internal fun RestChannelList.toDirectory(): ChannelDirectory =
    ChannelDirectory(
        publicChannels = `public`.map { it.toDetail() },
        directMessageChannels = dm.orEmpty().map { it.toModel() },
    )

internal fun RestDMChannel.toModel(): Channel.DirectMessage =
    Channel.DirectMessage(
        id = ChannelId(id),
        userId = UserId(userId),
    )

// ── Message ──

internal fun RestMessage.toModel(): Message =
    Message(
        id = MessageId(id),
        authorId = UserId(userId),
        channelId = ChannelId(channelId),
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isPinned = pinned,
        stamps = stamps.map { it.toModel() },
        threadId = threadId,
        nonce = nonce,
    )

internal fun RestMessageStamp.toModel(): MessageStamp =
    MessageStamp(
        userId = UserId(userId),
        stampId = StampId(stampId),
        count = count,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

internal fun RestMessagePin.toModel(): PinInfo =
    PinInfo(
        pinnerId = UserId(userId),
        pinnedAt = pinnedAt,
    )

internal fun RestPin.toModel(): Pin =
    Pin(
        pinnerId = UserId(userId),
        pinnedAt = pinnedAt,
        message = message.toModel(),
    )

// ── User ──

internal fun RestUserDetail.toModel(): User.Detail =
    User.Detail(
        id = UserId(id),
        name = name,
        displayName = displayName,
        iconFileId = FileId(iconFileId),
        isBot = bot,
        state = state.toModel(),
        updatedAt = updatedAt,
        twitterId = twitterId,
        lastOnline = lastOnline,
        tags = tags.map { it.toModel() },
        groupIds = groups.map { GroupId(it) },
        bio = bio,
        homeChannelId = homeChannel?.let { ChannelId(it) },
    )

internal fun RestUser.toModel(): User.Basic =
    User.Basic(
        id = UserId(id),
        name = name,
        displayName = displayName,
        iconFileId = FileId(iconFileId),
        isBot = bot,
        state = state.toModel(),
        updatedAt = updatedAt,
    )

internal fun RestUserTag.toModel(): UserTag =
    UserTag(
        tagId = UserTagId(tagId),
        tag = tag,
        isLocked = isLocked,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

internal fun RestUserAccountState.toModel(): UserState =
    when (this) {
        RestUserAccountState.deactivated -> UserState.DEACTIVATED
        RestUserAccountState.active -> UserState.ACTIVE
        RestUserAccountState.suspended -> UserState.SUSPENDED
    }

internal fun UserState.toApiModel(): RestUserAccountState =
    when (this) {
        UserState.DEACTIVATED -> RestUserAccountState.deactivated
        UserState.ACTIVE -> RestUserAccountState.active
        UserState.SUSPENDED -> RestUserAccountState.suspended
    }

// ── Stamp ──

internal fun RestStamp.toModel(): Stamp.Detail =
    Stamp.Detail(
        id = StampId(id),
        name = name,
        creatorId = UserId(creatorId),
        fileId = FileId(fileId),
        createdAt = createdAt,
        updatedAt = updatedAt,
        isUnicode = isUnicode,
    )

internal fun RestStampWithThumbnail.toModel(): Stamp.Detail =
    Stamp.Detail(
        id = StampId(id),
        name = name,
        creatorId = UserId(creatorId),
        fileId = FileId(fileId),
        createdAt = createdAt,
        updatedAt = updatedAt,
        isUnicode = isUnicode,
    )

// ── File ──

internal fun RestFileInfo.toModel(): FileMeta =
    FileMeta(
        id = FileId(id),
        name = name,
        mime = mime,
        size = propertySize,
        md5 = md5,
        isAnimatedImage = isAnimatedImage,
        createdAt = createdAt,
        channelId = channelId?.let { ChannelId(it) },
        uploaderId = uploaderId?.let { UserId(it) },
    )

// ── Group ──

internal fun RestUserGroup.toModel(): Group =
    Group(
        id = GroupId(id),
        name = name,
        description = description,
        type = type,
        iconFileId = FileId(icon),
        members = members.map { it.toModel() },
        createdAt = createdAt,
        updatedAt = updatedAt,
        admins = admins.map { UserId(it) },
    )

internal fun RestUserGroupMember.toModel(): GroupMember =
    GroupMember(
        userId = UserId(id),
        role = role,
    )

// ── UserStats ──

internal fun RestUserStats.toModel(): UserStats =
    UserStats(
        totalMessageCount = totalMessageCount,
        stamps = stamps.map { it.toModel() },
        datetime = datetime,
    )

internal fun RestUserStatsStamp.toModel(): UserStampStats =
    UserStampStats(
        stampId = StampId(id),
        count = count,
        total = total,
    )
