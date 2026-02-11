package jp.xhw.trakt.bot.model

import kotlin.uuid.Uuid

data class MessageId(
    val value: Uuid,
)

data class Message(
    val id: MessageId,
    val channel: Channel,
    val author: User,
) {
    val channelId: ChannelId
        get() = channel.id
}

data class EmbeddedItem(
    val raw: String,
    val type: String,
    val id: Uuid,
)
