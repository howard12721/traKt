package jp.xhw.trakt.bot.infrastructure.runtime.mapper

import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.websocket.ChannelCreated as WsChannelCreated
import jp.xhw.trakt.websocket.ChannelTopicChanged as WsChannelTopicChanged
import jp.xhw.trakt.websocket.DirectMessageCreated as WsDirectMessageCreated
import jp.xhw.trakt.websocket.DirectMessageDeleted as WsDirectMessageDeleted
import jp.xhw.trakt.websocket.DirectMessageUpdated as WsDirectMessageUpdated
import jp.xhw.trakt.websocket.Event as WebSocketEvent
import jp.xhw.trakt.websocket.Joined as WsJoined
import jp.xhw.trakt.websocket.Left as WsLeft
import jp.xhw.trakt.websocket.MessageCreated as WsMessageCreated
import jp.xhw.trakt.websocket.MessageDeleted as WsMessageDeleted
import jp.xhw.trakt.websocket.MessageUpdated as WsMessageUpdated
import jp.xhw.trakt.websocket.StampCreated as WsStampCreated
import jp.xhw.trakt.websocket.TagAdded as WsTagAdded
import jp.xhw.trakt.websocket.TagRemoved as WsTagRemoved
import jp.xhw.trakt.websocket.UserActivated as WsUserActivated
import jp.xhw.trakt.websocket.UserCreated as WsUserCreated

internal fun WebSocketEvent.toEventOrNull(): Event? =
    when (this) {
        is WsMessageCreated -> {
            MessageCreated(occurredAt = eventTime, message = message.toModel())
        }

        is WsMessageUpdated -> {
            MessageUpdated(occurredAt = eventTime, message = message.toModel())
        }

        is WsDirectMessageCreated -> {
            DirectMessageCreated(occurredAt = eventTime, message = message.toModel())
        }

        is WsDirectMessageUpdated -> {
            DirectMessageUpdated(occurredAt = eventTime, message = message.toModel())
        }

        is WsMessageDeleted -> {
            MessageDeleted(
                occurredAt = eventTime,
                messageId = MessageId(message.id),
                channelId = ChannelId(message.channelId),
            )
        }

        is WsDirectMessageDeleted -> {
            DirectMessageDeleted(
                occurredAt = eventTime,
                messageId = MessageId(message.id),
                channelId = ChannelId(message.channelId),
            )
        }

        is WsJoined -> {
            BotJoinedChannel(occurredAt = eventTime, channelId = channel.id())
        }

        is WsLeft -> {
            BotLeftChannel(occurredAt = eventTime, channelId = channel.id())
        }

        is WsChannelCreated -> {
            ChannelCreated(
                occurredAt = eventTime,
                channel = channel.toModel(),
            )
        }

        is WsChannelTopicChanged -> {
            ChannelTopicChanged(
                occurredAt = eventTime,
                channelId = channel.id(),
                topic = topic,
                updaterId = updater.id(),
            )
        }

        is WsUserCreated -> {
            UserCreated(
                occurredAt = eventTime,
                user = user.toModel(),
            )
        }

        is WsUserActivated -> {
            UserActivated(
                occurredAt = eventTime,
                user = user.toModel(),
            )
        }

        is WsStampCreated -> {
            StampCreated(
                occurredAt = eventTime,
                stamp =
                    Stamp.Basic(
                        id = StampId(id),
                        name = name,
                        creatorId = creator.id(),
                        fileId = FileId(fileId),
                    ),
            )
        }

        is WsTagAdded -> {
            TagAdded(
                occurredAt = eventTime,
                tagId = UserTagId(tagId),
                tag = tag,
            )
        }

        is WsTagRemoved -> {
            TagRemoved(
                occurredAt = eventTime,
                tagId = UserTagId(tagId),
                tag = tag,
            )
        }

        else -> {
            null
        }
    }

private fun jp.xhw.trakt.websocket.Message.id(): MessageId = MessageId(id)

private fun jp.xhw.trakt.websocket.Message.toModel(): Message =
    Message(
        id = id(),
        authorId = user.id(),
        channelId = ChannelId(this.channelId),
        content = text,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isPinned = false,
        stamps = emptyList(),
        threadId = null,
    )

private fun jp.xhw.trakt.websocket.User.id(): UserId = UserId(id)

private fun jp.xhw.trakt.websocket.User.toModel(): User.Minimal =
    User.Minimal(
        id = id(),
        name = name,
        displayName = displayName,
        iconFileId = FileId(iconId),
        isBot = bot,
    )

private fun jp.xhw.trakt.websocket.Channel.id(): ChannelId = ChannelId(id)

private fun jp.xhw.trakt.websocket.Channel.toModel(): Channel.Meta =
    Channel.Meta(
        id = id(),
        parentId = ChannelId(parentId),
        name = name,
        creator = creator.toModel(),
        path = ChannelPath(path),
    )
