package jp.xhw.trakt.bot.gateway

import jp.xhw.trakt.bot.model.Channel
import jp.xhw.trakt.bot.model.ChannelDirectory
import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.ChannelPinSnapshot
import jp.xhw.trakt.bot.model.ChannelSnapshot
import jp.xhw.trakt.bot.model.DirectMessageChannelSnapshot
import jp.xhw.trakt.bot.model.File
import jp.xhw.trakt.bot.model.FileId
import jp.xhw.trakt.bot.model.FileSnapshot
import jp.xhw.trakt.bot.model.Message
import jp.xhw.trakt.bot.model.MessageClipSnapshot
import jp.xhw.trakt.bot.model.MessageId
import jp.xhw.trakt.bot.model.MessagePinSnapshot
import jp.xhw.trakt.bot.model.MessageSearchResult
import jp.xhw.trakt.bot.model.MessageSnapshot
import jp.xhw.trakt.bot.model.MessageStampSnapshot
import jp.xhw.trakt.bot.model.Stamp
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.StampSnapshot
import jp.xhw.trakt.bot.model.User
import jp.xhw.trakt.bot.model.UserId
import jp.xhw.trakt.bot.model.UserSnapshot
import jp.xhw.trakt.bot.model.UserStampStatsSnapshot
import jp.xhw.trakt.bot.model.UserState
import jp.xhw.trakt.bot.model.UserStatsSnapshot
import jp.xhw.trakt.bot.model.UserTagId
import jp.xhw.trakt.bot.model.UserTagSnapshot
import jp.xhw.trakt.rest.models.Channel as RestChannel
import jp.xhw.trakt.rest.models.ChannelList as RestChannelList
import jp.xhw.trakt.rest.models.DMChannel as RestDMChannel
import jp.xhw.trakt.rest.models.FileInfo as RestFileInfo
import jp.xhw.trakt.rest.models.Message as RestMessage
import jp.xhw.trakt.rest.models.MessageClip as RestMessageClip
import jp.xhw.trakt.rest.models.MessagePin as RestMessagePin
import jp.xhw.trakt.rest.models.MessageSearchResult as RestMessageSearchResult
import jp.xhw.trakt.rest.models.MessageStamp as RestMessageStamp
import jp.xhw.trakt.rest.models.Pin as RestPin
import jp.xhw.trakt.rest.models.Stamp as RestStamp
import jp.xhw.trakt.rest.models.StampWithThumbnail as RestStampWithThumbnail
import jp.xhw.trakt.rest.models.User as RestUser
import jp.xhw.trakt.rest.models.UserAccountState as RestUserAccountState
import jp.xhw.trakt.rest.models.UserDetail as RestUserDetail
import jp.xhw.trakt.rest.models.UserStats as RestUserStats
import jp.xhw.trakt.rest.models.UserStatsStamp as RestUserStatsStamp
import jp.xhw.trakt.rest.models.UserTag as RestUserTag

internal fun RestChannel.toHandle(): Channel = Channel(ChannelId(id))

internal fun RestChannel.toSnapshot(path: String? = null): ChannelSnapshot =
    ChannelSnapshot(
        channel = toHandle(),
        name = name,
        path = path,
        parent = parentId?.let { Channel(ChannelId(it)) },
        topic = topic,
        archived = archived,
        force = force,
        children = children.map { childId -> Channel(ChannelId(childId)) },
    )

internal fun RestDMChannel.toSnapshot(): DirectMessageChannelSnapshot =
    DirectMessageChannelSnapshot(
        channel = Channel(ChannelId(id)),
        user = User(UserId(userId)),
    )

internal fun RestChannelList.toDirectory(): ChannelDirectory =
    ChannelDirectory(
        publicChannels = `public`.map(RestChannel::toSnapshot),
        directMessageChannels = dm.orEmpty().map(RestDMChannel::toSnapshot),
    )

internal fun RestMessage.toHandle(): Message =
    Message(
        id = MessageId(id),
        channel = Channel(ChannelId(channelId)),
        author = User(UserId(userId)),
    )

internal fun RestMessage.toSnapshot(): MessageSnapshot =
    MessageSnapshot(
        handle = toHandle(),
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        pinned = pinned,
        stamps = stamps.map(RestMessageStamp::toSnapshot),
        threadId = threadId?.let(::MessageId),
        nonce = nonce,
    )

internal fun RestMessageClip.toSnapshot(): MessageClipSnapshot =
    MessageClipSnapshot(
        folderId = folderId,
        clippedAt = clippedAt,
    )

internal fun RestMessagePin.toSnapshot(): MessagePinSnapshot =
    MessagePinSnapshot(
        pinnedBy = User(UserId(userId)),
        pinnedAt = pinnedAt,
    )

internal fun RestMessageStamp.toSnapshot(): MessageStampSnapshot =
    MessageStampSnapshot(
        userId = UserId(userId),
        stampId = StampId(stampId),
        count = count,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

internal fun RestMessageSearchResult.toResult(): MessageSearchResult =
    MessageSearchResult(
        totalHits = totalHits,
        hits = hits.map(RestMessage::toSnapshot),
    )

internal fun RestPin.toSnapshot(): ChannelPinSnapshot =
    ChannelPinSnapshot(
        pinnedBy = User(UserId(userId)),
        pinnedAt = pinnedAt,
        message = message.toSnapshot(),
    )

internal fun RestUser.toHandle(): User = User(UserId(id))

internal fun RestUser.toSnapshot(): UserSnapshot =
    UserSnapshot(
        user = toHandle(),
        name = name,
        displayName = displayName,
        iconFileId = FileId(iconFileId),
        isBot = bot,
        state = state.toModel(),
        updatedAt = updatedAt,
    )

internal fun RestUserDetail.toSnapshot(): UserSnapshot =
    UserSnapshot(
        user = User(UserId(id)),
        name = name,
        displayName = displayName,
        iconFileId = FileId(iconFileId),
        isBot = bot,
        state = state.toModel(),
        twitterId = twitterId,
        lastOnline = lastOnline,
        updatedAt = updatedAt,
        tags = tags.map(RestUserTag::toSnapshot),
        groups = groups,
        bio = bio,
        homeChannel = homeChannel?.let { Channel(ChannelId(it)) },
    )

internal fun RestUserTag.toSnapshot(): UserTagSnapshot =
    UserTagSnapshot(
        tagId = UserTagId(tagId),
        tag = tag,
        isLocked = isLocked,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

internal fun RestUserStats.toSnapshot(): UserStatsSnapshot =
    UserStatsSnapshot(
        totalMessageCount = totalMessageCount,
        stamps = stamps.map(RestUserStatsStamp::toSnapshot),
        datetime = datetime,
    )

internal fun RestUserStatsStamp.toSnapshot(): UserStampStatsSnapshot =
    UserStampStatsSnapshot(
        stampId = StampId(id),
        count = count,
        total = total,
    )

internal fun RestFileInfo.toSnapshot(): FileSnapshot =
    FileSnapshot(
        file = File(FileId(id)),
        name = name,
        mime = mime,
        size = propertySize,
        md5 = md5,
        isAnimatedImage = isAnimatedImage,
        createdAt = createdAt,
        channel = channelId?.let { Channel(ChannelId(it)) },
        uploader = uploaderId?.let { User(UserId(it)) },
    )

internal fun RestStamp.toSnapshot(): StampSnapshot =
    StampSnapshot(
        stamp = Stamp(StampId(id)),
        name = name,
        creator = User(UserId(creatorId)),
        file = File(FileId(fileId)),
        createdAt = createdAt,
        updatedAt = updatedAt,
        isUnicode = isUnicode,
    )

internal fun RestStampWithThumbnail.toSnapshot(): StampSnapshot =
    StampSnapshot(
        stamp = Stamp(StampId(id)),
        name = name,
        creator = User(UserId(creatorId)),
        file = File(FileId(fileId)),
        createdAt = createdAt,
        updatedAt = updatedAt,
        isUnicode = isUnicode,
        hasThumbnail = hasThumbnail,
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
