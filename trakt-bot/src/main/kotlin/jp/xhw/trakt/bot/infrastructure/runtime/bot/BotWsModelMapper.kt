package jp.xhw.trakt.bot.infrastructure.runtime.bot

import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.websocket.bot.Channel as WsChannel
import jp.xhw.trakt.websocket.bot.ChannelCreated as WsChannelCreated
import jp.xhw.trakt.websocket.bot.ChannelTopicChanged as WsChannelTopicChanged
import jp.xhw.trakt.websocket.bot.DirectMessageCreated as WsDirectMessageCreated
import jp.xhw.trakt.websocket.bot.DirectMessageDeleted as WsDirectMessageDeleted
import jp.xhw.trakt.websocket.bot.DirectMessageUpdated as WsDirectMessageUpdated
import jp.xhw.trakt.websocket.bot.BotEvent as WebSocketEvent
import jp.xhw.trakt.websocket.bot.Joined as WsJoined
import jp.xhw.trakt.websocket.bot.Left as WsLeft
import jp.xhw.trakt.websocket.bot.Message as WsMessage
import jp.xhw.trakt.websocket.bot.MessageCreated as WsMessageCreated
import jp.xhw.trakt.websocket.bot.MessageDeleted as WsMessageDeleted
import jp.xhw.trakt.websocket.bot.MessageUpdated as WsMessageUpdated
import jp.xhw.trakt.websocket.bot.StampCreated as WsStampCreated
import jp.xhw.trakt.websocket.bot.TagAdded as WsTagAdded
import jp.xhw.trakt.websocket.bot.TagRemoved as WsTagRemoved
import jp.xhw.trakt.websocket.bot.User as WsUser
import jp.xhw.trakt.websocket.bot.UserActivated as WsUserActivated
import jp.xhw.trakt.websocket.bot.UserCreated as WsUserCreated

internal fun WebSocketEvent.toEventOrNull(): BotEvent? =
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
                channel = channel.toModel(),
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

private fun WsMessage.id(): MessageId = MessageId(id)

private fun WsMessage.toModel(): Message =
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

private fun WsUser.id(): UserId = UserId(id)

private fun WsUser.toModel(): User.Minimal =
    User.Minimal(
        id = id(),
        name = name,
        displayName = displayName,
        iconFileId = FileId(iconId),
        isBot = bot,
    )

private fun WsChannel.id(): ChannelId = ChannelId(id)

private fun WsChannel.toModel(): Channel.Meta =
    Channel.Meta(
        id = id(),
        parentId = ChannelId(parentId),
        name = name,
        creator = creator.toModel(),
        path = ChannelPath(path),
    )
