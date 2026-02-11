package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.Channel
import jp.xhw.trakt.bot.model.ChannelDirectory
import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.ChannelPinSnapshot
import jp.xhw.trakt.bot.model.ChannelSnapshot
import jp.xhw.trakt.bot.model.EditChannelRequest
import jp.xhw.trakt.bot.model.MessageHistoryQuery
import jp.xhw.trakt.bot.model.MessageSnapshot
import jp.xhw.trakt.bot.model.User

internal interface ChannelPort {
    suspend fun getChannel(channelId: ChannelId): Channel?

    suspend fun getSnapshot(channelId: ChannelId): ChannelSnapshot?

    suspend fun createChannel(
        name: String,
        parent: Channel? = null,
    ): Channel

    suspend fun editChannel(
        channelId: ChannelId,
        request: EditChannelRequest,
    )

    suspend fun getPath(channelId: ChannelId): String?

    suspend fun getTopic(channelId: ChannelId): String?

    suspend fun setTopic(
        channelId: ChannelId,
        topic: String,
    )

    suspend fun listChannels(
        includeDirectMessage: Boolean = false,
        path: String? = null,
    ): ChannelDirectory

    suspend fun getSubscribers(channelId: ChannelId): List<User>

    suspend fun getMessages(
        channelId: ChannelId,
        query: MessageHistoryQuery = MessageHistoryQuery(),
    ): List<MessageSnapshot>

    suspend fun getPins(channelId: ChannelId): List<ChannelPinSnapshot>

    suspend fun postMessage(
        channelId: ChannelId,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot
}
