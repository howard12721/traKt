package jp.xhw.trakt.bot.model

import kotlin.time.Instant

data class ChannelSnapshot(
    val channel: Channel,
    val name: String,
    val path: String? = null,
    val parent: Channel? = null,
    val topic: String? = null,
    val archived: Boolean? = null,
    val force: Boolean? = null,
    val children: List<Channel> = emptyList(),
    val creator: User? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
) {
    val id: ChannelId
        get() = channel.id
}

data class DirectMessageChannelSnapshot(
    val channel: Channel,
    val user: User,
)

data class ChannelDirectory(
    val publicChannels: List<ChannelSnapshot>,
    val directMessageChannels: List<DirectMessageChannelSnapshot> = emptyList(),
)

data class EditChannelRequest(
    val name: String? = null,
    val archived: Boolean? = null,
    val force: Boolean? = null,
    val parent: Channel? = null,
)

data class ChannelPinSnapshot(
    val pinnedBy: User,
    val pinnedAt: Instant,
    val message: MessageSnapshot,
)
