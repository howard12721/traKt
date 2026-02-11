package jp.xhw.trakt.bot.runtime

import jp.xhw.trakt.bot.event.ChannelCreated
import jp.xhw.trakt.bot.event.ChannelTopicChanged
import jp.xhw.trakt.bot.event.DirectMessageCreated
import jp.xhw.trakt.bot.event.DirectMessageDeleted
import jp.xhw.trakt.bot.event.DirectMessageUpdated
import jp.xhw.trakt.bot.event.Event
import jp.xhw.trakt.bot.event.Joined
import jp.xhw.trakt.bot.event.Left
import jp.xhw.trakt.bot.event.MessageCreated
import jp.xhw.trakt.bot.event.MessageDeleted
import jp.xhw.trakt.bot.event.MessageUpdated
import jp.xhw.trakt.bot.event.StampCreated
import jp.xhw.trakt.bot.event.TagAdded
import jp.xhw.trakt.bot.event.TagRemoved
import jp.xhw.trakt.bot.event.UserActivated
import jp.xhw.trakt.bot.event.UserCreated
import jp.xhw.trakt.bot.model.Channel
import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.ChannelSnapshot
import jp.xhw.trakt.bot.model.EmbeddedItem
import jp.xhw.trakt.bot.model.File
import jp.xhw.trakt.bot.model.FileId
import jp.xhw.trakt.bot.model.Message
import jp.xhw.trakt.bot.model.MessageId
import jp.xhw.trakt.bot.model.MessageSnapshot
import jp.xhw.trakt.bot.model.Stamp
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.StampSnapshot
import jp.xhw.trakt.bot.model.User
import jp.xhw.trakt.bot.model.UserId
import jp.xhw.trakt.bot.model.UserSnapshot
import jp.xhw.trakt.bot.model.UserTagId
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
            MessageCreated(occurredAt = eventTime, message = message.toSnapshot())
        }

        is WsMessageUpdated -> {
            MessageUpdated(occurredAt = eventTime, message = message.toSnapshot())
        }

        is WsDirectMessageCreated -> {
            DirectMessageCreated(occurredAt = eventTime, message = message.toSnapshot())
        }

        is WsDirectMessageUpdated -> {
            DirectMessageUpdated(occurredAt = eventTime, message = message.toSnapshot())
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
            Joined(occurredAt = eventTime, channel = Channel(ChannelId(channel.id)))
        }

        is WsLeft -> {
            Left(occurredAt = eventTime, channel = Channel(ChannelId(channel.id)))
        }

        is WsChannelCreated -> {
            ChannelCreated(
                occurredAt = eventTime,
                channel = channel.toSnapshot(),
            )
        }

        is WsChannelTopicChanged -> {
            ChannelTopicChanged(
                occurredAt = eventTime,
                channel = Channel(ChannelId(channel.id)),
                topic = topic,
                updater = updater.toHandle(),
            )
        }

        is WsUserCreated -> {
            UserCreated(
                occurredAt = eventTime,
                user = user.toSnapshot(),
            )
        }

        is WsUserActivated -> {
            UserActivated(
                occurredAt = eventTime,
                user = user.toSnapshot(),
            )
        }

        is WsStampCreated -> {
            StampCreated(
                occurredAt = eventTime,
                stamp =
                    StampSnapshot(
                        stamp = Stamp(StampId(id)),
                        name = name,
                        creator = creator.toHandle(),
                        file = File(FileId(fileId)),
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

private fun jp.xhw.trakt.websocket.Message.toHandle(): Message =
    Message(
        id = MessageId(id),
        channel = Channel(ChannelId(channelId)),
        author = user.toHandle(),
    )

private fun jp.xhw.trakt.websocket.Message.toSnapshot(): MessageSnapshot =
    MessageSnapshot(
        handle = toHandle(),
        content = text,
        createdAt = createdAt,
        updatedAt = updatedAt,
        plainText = plainText,
        embedded =
            embedded.map { embeddedItem ->
                EmbeddedItem(
                    raw = embeddedItem.raw,
                    type = embeddedItem.type,
                    id = embeddedItem.id,
                )
            },
    )

private fun jp.xhw.trakt.websocket.User.toHandle(): User = User(UserId(id))

private fun jp.xhw.trakt.websocket.User.toSnapshot(): UserSnapshot =
    UserSnapshot(
        user = toHandle(),
        name = name,
        displayName = displayName,
        iconFileId = FileId(iconId),
        isBot = bot,
    )

private fun jp.xhw.trakt.websocket.Channel.toSnapshot(): ChannelSnapshot =
    ChannelSnapshot(
        channel = Channel(ChannelId(id)),
        name = name,
        path = path,
        parent = Channel(ChannelId(parentId)),
        creator = creator.toHandle(),
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
