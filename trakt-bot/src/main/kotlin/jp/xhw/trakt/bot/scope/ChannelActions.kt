package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.*
import kotlin.uuid.Uuid

// --- Fetch ---

context(scope: BotScope)
suspend fun fetchChannel(channelId: ChannelId): Channel.Detail = scope.context.channelPort.fetchChannel(channelId)

context(scope: BotScope)
suspend fun fetchChannel(channelId: Uuid): Channel.Detail = fetchChannel(ChannelId(channelId))

context(scope: BotScope)
suspend fun ChannelHandle.resolve(): Channel.Detail = scope.context.channelPort.fetchChannel(id)

context(scope: BotScope)
suspend fun Channel.resolve(): Channel.Detail = this as? Channel.Detail ?: handle.resolve()

context(scope: BotScope)
suspend fun Channel.Detail.refresh(): Channel.Detail = handle.resolve()

context(scope: BotScope)
suspend fun fetchChannels(
    includeDirectMessages: Boolean = false,
    path: String? = null,
): ChannelDirectory = scope.context.channelPort.fetchChannels(includeDirectMessages, path)

context(scope: BotScope)
suspend fun ChannelHandle.fetchPath(): ChannelPath = scope.context.channelPort.fetchChannelPath(id)

context(scope: BotScope)
suspend fun ChannelHandle.fetchTopic(): String = scope.context.channelPort.fetchChannelTopic(id)

context(scope: BotScope)
suspend fun ChannelHandle.setTopic(topic: String) {
    scope.context.channelPort.setChannelTopic(id, topic)
}

context(scope: BotScope)
suspend fun ChannelHandle.fetchSubscribers(): List<UserId> = scope.context.channelPort.fetchSubscribers(id)

context(scope: BotScope)
suspend fun ChannelHandle.setSubscribers(subscribers: List<UserId>) {
    scope.context.channelPort.setSubscribers(id, subscribers)
}

context(scope: BotScope)
suspend fun ChannelHandle.fetchPins(): List<Pin> = scope.context.channelPort.fetchChannelPins(id)

context(scope: BotScope)
suspend fun ChannelHandle.fetchStats(): ChannelStats = scope.context.channelPort.fetchChannelStats(id)

context(scope: BotScope)
suspend fun ChannelHandle.fetchMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: kotlin.time.Instant? = null,
    until: kotlin.time.Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> = scope.context.channelPort.fetchMessages(id, limit, offset, since, until, inclusive, order)

// --- Send ---

context(scope: BotScope)
suspend fun ChannelHandle.sendMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = scope.context.channelPort.sendMessage(id, content, embed, nonce)

context(scope: BotScope)
suspend fun Channel.sendMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = handle.sendMessage(content, embed, nonce)
