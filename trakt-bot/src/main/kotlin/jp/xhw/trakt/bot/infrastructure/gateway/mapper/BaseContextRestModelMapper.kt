package jp.xhw.trakt.bot.infrastructure.gateway.mapper

import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.rest.models.Channel as RestChannel
import jp.xhw.trakt.rest.models.ChannelList as RestChannelList
import jp.xhw.trakt.rest.models.ChannelViewState as RestChannelViewState
import jp.xhw.trakt.rest.models.ChannelViewer as RestChannelViewer
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
        parent = parentId?.let { Channel.Ref(ChannelId(it)) },
        name = name,
        isArchived = archived,
        isForcedNotified = force,
        topic = ChannelTopic(topic),
        children = children.map { Channel.Ref(ChannelId(it)) },
    )

internal fun RestChannelList.toDirectory(): ChannelDirectory =
    ChannelDirectory(
        publicChannels = `public`.map { it.toDetail() },
        directMessageChannels = dm.orEmpty().map { it.toModel() },
    )

internal fun RestDMChannel.toModel(): Channel.DirectMessage =
    Channel.DirectMessage(
        id = ChannelId(id),
        user = User.Ref(UserId(userId)),
    )

// ── Channel Viewer ──

internal fun RestChannelViewer.toViewerModel(): ChannelViewer =
    ChannelViewer(
        user = User.Ref(UserId(userId)),
        state = state.toModel(),
        updatedAt = updatedAt,
    )

internal fun RestChannelViewState.toModel(): ChannelViewState =
    when (this) {
        RestChannelViewState.NONE -> ChannelViewState.NONE
        RestChannelViewState.STALE_VIEWING -> ChannelViewState.STALE_VIEWING
        RestChannelViewState.MONITORING -> ChannelViewState.MONITORING
        RestChannelViewState.EDITING -> ChannelViewState.EDITING
    }

// ── Message ──

internal fun RestMessage.toModel(): Message.Detail =
    Message.Detail(
        id = MessageId(id),
        author = User.Ref(UserId(userId)),
        channel = Channel.Ref(ChannelId(channelId)),
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
        user = User.Ref(UserId(userId)),
        stamp = Stamp.Ref(StampId(stampId)),
        count = count,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

internal fun RestMessagePin.toModel(): PinInfo =
    PinInfo(
        pinner = User.Ref(UserId(userId)),
        pinnedAt = pinnedAt,
    )

internal fun RestPin.toModel(): Pin =
    Pin(
        pinner = User.Ref(UserId(userId)),
        pinnedAt = pinnedAt,
        message = message.toModel(),
    )

// ── User ──

internal fun RestUserDetail.toModel(): User.Detail =
    User.Detail(
        id = UserId(id),
        name = name,
        displayName = displayName,
        iconFile = File.Ref(FileId(iconFileId)),
        isBot = bot,
        state = state.toModel(),
        updatedAt = updatedAt,
        twitterId = twitterId,
        lastOnline = lastOnline,
        tags = tags.map { it.toModel() },
        groups = groups.map { Group.Ref(GroupId(it)) },
        bio = bio,
        homeChannel = homeChannel?.let { Channel.Ref(ChannelId(it)) },
    )

internal fun RestUser.toModel(): User.Basic =
    User.Basic(
        id = UserId(id),
        name = name,
        displayName = displayName,
        iconFile = File.Ref(FileId(iconFileId)),
        isBot = bot,
        state = state.toModel(),
        updatedAt = updatedAt,
    )

internal fun RestUserTag.toModel(): UserTag.Detail =
    UserTag.Detail(
        id = UserTagId(tagId),
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
        creator = User.Ref(UserId(creatorId)),
        file = File.Ref(FileId(fileId)),
        createdAt = createdAt,
        updatedAt = updatedAt,
        isUnicode = isUnicode,
    )

internal fun RestStampWithThumbnail.toModel(): Stamp.Detail =
    Stamp.Detail(
        id = StampId(id),
        name = name,
        creator = User.Ref(UserId(creatorId)),
        file = File.Ref(FileId(fileId)),
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
        channel = channelId?.let { Channel.Ref(ChannelId(it)) },
        uploader = uploaderId?.let { User.Ref(UserId(it)) },
    )

// ── Group ──

internal fun RestUserGroup.toModel(): Group.Detail =
    Group.Detail(
        id = GroupId(id),
        name = name,
        description = description,
        type = type,
        iconFile = File.Ref(FileId(icon)),
        members = members.map { it.toModel() },
        createdAt = createdAt,
        updatedAt = updatedAt,
        adminUsers = admins.map { User.Ref(UserId(it)) },
    )

internal fun RestUserGroupMember.toModel(): GroupMember =
    GroupMember(
        user = User.Ref(UserId(id)),
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
        stamp = Stamp.Ref(StampId(id)),
        count = count,
        total = total,
    )
