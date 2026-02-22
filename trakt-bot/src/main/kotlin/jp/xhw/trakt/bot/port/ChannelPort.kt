package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant

internal interface ChannelPort {
    suspend fun fetchChannels(
        includeDirectMessages: Boolean = false,
        path: String? = null,
    ): ChannelDirectory

    suspend fun fetchChannel(channelId: ChannelId): Channel.Detail

    suspend fun fetchChannelPath(channelId: ChannelId): ChannelPath

    suspend fun fetchChannelTopic(channelId: ChannelId): String

    suspend fun setChannelTopic(
        channelId: ChannelId,
        topic: String,
    )

    suspend fun fetchSubscribers(channelId: ChannelId): List<UserId>

    suspend fun setSubscribers(
        channelId: ChannelId,
        subscribers: List<UserId>,
    )

    suspend fun fetchChannelPins(channelId: ChannelId): List<Pin>

    suspend fun fetchChannelStats(channelId: ChannelId): ChannelStats

    suspend fun fetchMessages(
        channelId: ChannelId,
        limit: Int? = null,
        offset: Int = 0,
        since: Instant? = null,
        until: Instant? = null,
        inclusive: Boolean = false,
        order: SortDirection = SortDirection.DESCENDING,
    ): List<Message>

    suspend fun sendMessage(
        channelId: ChannelId,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): Message
}
