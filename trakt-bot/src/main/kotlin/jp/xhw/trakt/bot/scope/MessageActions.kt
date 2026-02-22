package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant
import kotlin.uuid.Uuid

// --- Fetch ---

context(scope: BotScope)
suspend fun fetchMessage(messageId: MessageId): Message = scope.context.messagePort.fetchMessage(messageId)

context(scope: BotScope)
suspend fun fetchMessage(messageId: Uuid): Message = fetchMessage(MessageId(messageId))

context(scope: BotScope)
suspend fun MessageHandle.resolve(): Message = scope.context.messagePort.fetchMessage(id)

context(scope: BotScope)
suspend fun MessageHandle.fetchStamps(): List<MessageStamp> = scope.context.messagePort.fetchStamps(id)

context(scope: BotScope)
suspend fun MessageHandle.fetchPinInfo(): PinInfo = scope.context.messagePort.fetchPinInfo(id)

// --- Edit / Delete ---

context(scope: BotScope)
suspend fun MessageHandle.update(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
) = scope.context.messagePort.editMessage(id, content, embed, nonce)

context(scope: BotScope)
suspend fun MessageHandle.delete() = scope.context.messagePort.deleteMessage(id)

// --- Stamps ---

context(scope: BotScope)
suspend fun MessageHandle.stamp(
    stampId: StampId,
    count: Int = 1,
) = scope.context.messagePort.addStamp(id, stampId, count)

context(scope: BotScope)
suspend fun MessageHandle.unstamp(stampId: StampId) = scope.context.messagePort.removeStamp(id, stampId)

// --- Pins ---

context(scope: BotScope)
suspend fun MessageHandle.pin(): PinInfo = scope.context.messagePort.createPin(id)

context(scope: BotScope)
suspend fun MessageHandle.unpin() = scope.context.messagePort.removePin(id)

// --- Search ---

context(scope: BotScope)
suspend fun searchMessages(
    word: String? = null,
    after: Instant? = null,
    before: Instant? = null,
    inChannel: ChannelId? = null,
    to: List<UserId>? = null,
    from: List<UserId>? = null,
    citation: MessageId? = null,
    bot: Boolean? = null,
    hasUrl: Boolean? = null,
    hasAttachments: Boolean? = null,
    hasImage: Boolean? = null,
    hasVideo: Boolean? = null,
    hasAudio: Boolean? = null,
    limit: Int? = null,
    offset: Int? = null,
    sort: SortDirection = SortDirection.DESCENDING,
): SearchResult =
    scope.context.messagePort.searchMessages(
        word,
        after,
        before,
        inChannel,
        to,
        from,
        citation,
        bot,
        hasUrl,
        hasAttachments,
        hasImage,
        hasVideo,
        hasAudio,
        limit,
        offset,
        sort,
    )

// --- Reply ---

context(scope: BotScope)
suspend fun MessageHandle.reply(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = resolve().channel.sendMessage(content + "\n${url()}", embed, nonce)

// --- Message convenience extensions ---

context(scope: BotScope)
suspend fun Message.refresh(): Message = handle.resolve()

context(scope: BotScope)
suspend fun Message.fetchStamps(): List<MessageStamp> = handle.fetchStamps()

context(scope: BotScope)
suspend fun Message.fetchPinInfo(): PinInfo = handle.fetchPinInfo()

context(scope: BotScope)
suspend fun Message.update(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
) = handle.update(content, embed, nonce)

context(scope: BotScope)
suspend fun Message.delete() = handle.delete()

context(scope: BotScope)
suspend fun Message.stamp(
    stampId: StampId,
    count: Int = 1,
) = handle.stamp(stampId, count)

context(scope: BotScope)
suspend fun Message.unstamp(stampId: StampId) = handle.unstamp(stampId)

context(scope: BotScope)
suspend fun Message.pin(): PinInfo = handle.pin()

context(scope: BotScope)
suspend fun Message.unpin() = handle.unpin()

context(scope: BotScope)
suspend fun Message.reply(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = channel.sendMessage(content + "\n${url()}", embed, nonce)

context(scope: BotScope)
private fun MessageHandle.url(): String = "https://${scope.context.origin}/messages/${id.value}"

context(scope: BotScope)
private fun Message.url(): String = handle.url()
