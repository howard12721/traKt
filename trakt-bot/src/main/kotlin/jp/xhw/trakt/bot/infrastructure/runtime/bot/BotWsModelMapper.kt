package jp.xhw.trakt.bot.infrastructure.runtime.bot

import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.websocket.bot.BotEvent as WebSocketEvent
import jp.xhw.trakt.websocket.bot.Channel as WsChannel
import jp.xhw.trakt.websocket.bot.ChannelCreated as WsChannelCreated
import jp.xhw.trakt.websocket.bot.ChannelTopicChanged as WsChannelTopicChanged
import jp.xhw.trakt.websocket.bot.DirectMessageCreated as WsDirectMessageCreated
import jp.xhw.trakt.websocket.bot.DirectMessageDeleted as WsDirectMessageDeleted
import jp.xhw.trakt.websocket.bot.DirectMessageUpdated as WsDirectMessageUpdated
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
            BotEvents.MessageCreated(occurredAt = eventTime, message = message.toModel())
        }

        is WsMessageUpdated -> {
            BotEvents.MessageUpdated(occurredAt = eventTime, message = message.toModel())
        }

        is WsDirectMessageCreated -> {
            BotEvents.DirectMessageCreated(occurredAt = eventTime, message = message.toModel())
        }

        is WsDirectMessageUpdated -> {
            BotEvents.DirectMessageUpdated(occurredAt = eventTime, message = message.toModel())
        }

        is WsMessageDeleted -> {
            BotEvents.MessageDeleted(
                occurredAt = eventTime,
                message = Message.Ref(MessageId(message.id)),
                channel = Channel.Ref(ChannelId(message.channelId)),
            )
        }

        is WsDirectMessageDeleted -> {
            BotEvents.DirectMessageDeleted(
                occurredAt = eventTime,
                message = Message.Ref(MessageId(message.id)),
                channel = Channel.Ref(ChannelId(message.channelId)),
            )
        }

        is WsJoined -> {
            BotEvents.JoinedChannel(occurredAt = eventTime, channel = Channel.Ref(channel.id()))
        }

        is WsLeft -> {
            BotEvents.LeftChannel(occurredAt = eventTime, channel = Channel.Ref(channel.id()))
        }

        is WsChannelCreated -> {
            BotEvents.ChannelCreated(
                occurredAt = eventTime,
                channel = channel.toModel(),
            )
        }

        is WsChannelTopicChanged -> {
            BotEvents.ChannelTopicChanged(
                occurredAt = eventTime,
                channel = channel.toModel(),
                topic = topic,
                updater = User.Ref(updater.id()),
            )
        }

        is WsUserCreated -> {
            BotEvents.UserCreated(
                occurredAt = eventTime,
                user = user.toModel(),
            )
        }

        is WsUserActivated -> {
            BotEvents.UserActivated(
                occurredAt = eventTime,
                user = user.toModel(),
            )
        }

        is WsStampCreated -> {
            BotEvents.StampCreated(
                occurredAt = eventTime,
                stamp =
                    Stamp.Basic(
                        id = StampId(id),
                        name = name,
                        creator = User.Ref(creator.id()),
                        file = File.Ref(FileId(fileId)),
                    ),
            )
        }

        is WsTagAdded -> {
            BotEvents.TagAdded(
                occurredAt = eventTime,
                userTag = UserTag.Ref(UserTagId(tagId)),
                tag = tag,
            )
        }

        is WsTagRemoved -> {
            BotEvents.TagRemoved(
                occurredAt = eventTime,
                userTag = UserTag.Ref(UserTagId(tagId)),
                tag = tag,
            )
        }

        else -> {
            null
        }
    }

private fun WsMessage.id(): MessageId = MessageId(id)

private fun WsMessage.toModel(): Message.Detail =
    Message.Detail(
        id = id(),
        author = User.Ref(user.id()),
        channel = Channel.Ref(ChannelId(this.channelId)),
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
        iconFile = File.Ref(FileId(iconId)),
        isBot = bot,
    )

private fun WsChannel.id(): ChannelId = ChannelId(id)

private fun WsChannel.toModel(): Channel.Meta =
    Channel.Meta(
        id = id(),
        parent = Channel.Ref(ChannelId(parentId)),
        name = name,
        creator = creator.toModel(),
        path = ChannelPath(path),
    )
