package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.dsl.TraktDsl
import jp.xhw.trakt.bot.model.Channel
import jp.xhw.trakt.bot.model.ChannelDirectory
import jp.xhw.trakt.bot.model.ChannelId
import jp.xhw.trakt.bot.model.ChannelPinSnapshot
import jp.xhw.trakt.bot.model.ChannelSnapshot
import jp.xhw.trakt.bot.model.CreateUserRequest
import jp.xhw.trakt.bot.model.DirectMessageChannelSnapshot
import jp.xhw.trakt.bot.model.EditChannelRequest
import jp.xhw.trakt.bot.model.EditUserRequest
import jp.xhw.trakt.bot.model.Message
import jp.xhw.trakt.bot.model.MessageClipSnapshot
import jp.xhw.trakt.bot.model.MessageHistoryQuery
import jp.xhw.trakt.bot.model.MessageId
import jp.xhw.trakt.bot.model.MessagePinSnapshot
import jp.xhw.trakt.bot.model.MessageSearchQuery
import jp.xhw.trakt.bot.model.MessageSearchResult
import jp.xhw.trakt.bot.model.MessageSnapshot
import jp.xhw.trakt.bot.model.MessageStampSnapshot
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.User
import jp.xhw.trakt.bot.model.UserIconUpload
import jp.xhw.trakt.bot.model.UserId
import jp.xhw.trakt.bot.model.UserSnapshot
import jp.xhw.trakt.bot.model.UserState
import jp.xhw.trakt.bot.model.UserStatsSnapshot
import jp.xhw.trakt.bot.model.UserTagId
import jp.xhw.trakt.bot.model.UserTagSnapshot
import jp.xhw.trakt.bot.port.BotRuntimeContext
import kotlin.uuid.Uuid

@TraktDsl
class BotScope internal constructor(
    private val context: BotRuntimeContext,
) {
    suspend fun getChannel(channelId: ChannelId): Channel? = context.channelPort.getChannel(channelId)

    suspend fun getChannel(channelId: Uuid): Channel? = getChannel(ChannelId(channelId))

    suspend fun Channel.getSnapshot(): ChannelSnapshot? = context.channelPort.getSnapshot(id)

    suspend fun listChannels(
        includeDirectMessage: Boolean = false,
        path: String? = null,
    ): ChannelDirectory = context.channelPort.listChannels(includeDirectMessage, path)

    suspend fun createChannel(
        name: String,
        parent: Channel? = null,
    ): Channel = context.channelPort.createChannel(name, parent)

    suspend fun editChannel(
        channel: Channel,
        request: EditChannelRequest,
    ) {
        context.channelPort.editChannel(channel.id, request)
    }

    suspend fun editChannel(
        channel: Channel,
        name: String? = null,
        archived: Boolean? = null,
        force: Boolean? = null,
        parent: Channel? = null,
    ) {
        editChannel(
            channel = channel,
            request =
                EditChannelRequest(
                    name = name,
                    archived = archived,
                    force = force,
                    parent = parent,
                ),
        )
    }

    suspend fun Channel.getPath(): String? = context.channelPort.getPath(id)

    suspend fun Channel.getTopic(): String? = context.channelPort.getTopic(id)

    suspend fun Channel.setTopic(topic: String) {
        context.channelPort.setTopic(id, topic)
    }

    suspend fun Channel.getSubscribers(): List<User> = context.channelPort.getSubscribers(id)

    suspend fun Channel.getMessages(query: MessageHistoryQuery = MessageHistoryQuery()): List<MessageSnapshot> =
        context.channelPort.getMessages(id, query)

    suspend fun Channel.getPins(): List<ChannelPinSnapshot> = context.channelPort.getPins(id)

    suspend fun postMessage(
        channel: Channel,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot = context.channelPort.postMessage(channel.id, content, embed, nonce)

    suspend fun Channel.sendMessage(
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot = postMessage(this, content, embed, nonce)

    suspend fun getMessage(messageId: MessageId): Message? = context.messagePort.getMessage(messageId)

    suspend fun getMessage(messageId: Uuid): Message? = getMessage(MessageId(messageId))

    suspend fun Message.getSnapshot(): MessageSnapshot? = context.messagePort.getSnapshot(id)

    suspend fun Message.getContent(): String? = getSnapshot()?.content

    suspend fun Message.getStamps(): List<MessageStampSnapshot> = context.messagePort.getStamps(id)

    suspend fun Message.getClips(): List<MessageClipSnapshot> = context.messagePort.getClips(id)

    suspend fun Message.pin(): MessagePinSnapshot = context.messagePort.createPin(id)

    suspend fun Message.getPin(): MessagePinSnapshot? = context.messagePort.getPin(id)

    suspend fun Message.unpin() {
        context.messagePort.removePin(id)
    }

    suspend fun editMessage(
        message: Message,
        content: String,
        embed: Boolean = false,
    ) {
        context.messagePort.editMessage(message.id, content, embed)
    }

    suspend fun deleteMessage(message: Message) {
        context.messagePort.deleteMessage(message.id)
    }

    suspend fun addStamp(
        message: Message,
        stampId: StampId,
        count: Int = 1,
    ) {
        context.messagePort.addStamp(message.id, stampId, count)
    }

    suspend fun removeStamp(
        message: Message,
        stampId: StampId,
    ) {
        context.messagePort.removeStamp(message.id, stampId)
    }

    suspend fun listChannelMessages(
        channel: Channel,
        query: MessageHistoryQuery = MessageHistoryQuery(),
    ): List<MessageSnapshot> = context.messagePort.getChannelMessages(channel.id, query)

    suspend fun listDirectMessages(
        user: User,
        query: MessageHistoryQuery = MessageHistoryQuery(),
    ): List<MessageSnapshot> = context.messagePort.getDirectMessages(user.id, query)

    suspend fun postDirectMessage(
        user: User,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot = context.messagePort.postDirectMessage(user.id, content, embed, nonce)

    suspend fun searchMessages(query: MessageSearchQuery = MessageSearchQuery()): MessageSearchResult =
        context.messagePort.searchMessages(query)

    suspend fun Message.update(
        content: String,
        embed: Boolean = false,
    ) {
        editMessage(this, content, embed)
    }

    suspend fun Message.remove() {
        deleteMessage(this)
    }

    suspend fun Message.stamp(
        stampId: StampId,
        count: Int = 1,
    ) {
        addStamp(this, stampId, count)
    }

    suspend fun Message.unstamp(stampId: StampId) {
        removeStamp(this, stampId)
    }

    suspend fun MessageSnapshot.getStamps(): List<MessageStampSnapshot> = handle.getStamps()

    suspend fun MessageSnapshot.getPin(): MessagePinSnapshot? = handle.getPin()

    suspend fun MessageSnapshot.update(
        content: String,
        embed: Boolean = false,
    ) {
        handle.update(content, embed)
    }

    suspend fun MessageSnapshot.remove() {
        handle.remove()
    }

    suspend fun MessageSnapshot.stamp(
        stampId: StampId,
        count: Int = 1,
    ) {
        handle.stamp(stampId, count)
    }

    suspend fun MessageSnapshot.unstamp(stampId: StampId) {
        handle.unstamp(stampId)
    }

    suspend fun MessageSnapshot.pin(): MessagePinSnapshot = handle.pin()

    suspend fun MessageSnapshot.unpin() {
        handle.unpin()
    }

    suspend fun MessageSnapshot.sendReply(
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot = reply(handle, content, embed, nonce)

    suspend fun reply(
        message: Message,
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot = postMessage(message.channel, content, embed, nonce)

    suspend fun Message.sendReply(
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot = reply(this, content, embed, nonce)

    suspend fun getUser(userId: UserId): User? = context.userPort.getUser(userId)

    suspend fun getUser(userId: Uuid): User? = getUser(UserId(userId))

    suspend fun User.getSnapshot(): UserSnapshot? = context.userPort.getSnapshot(id)

    suspend fun listUsers(
        includeSuspended: Boolean = false,
        name: String? = null,
    ): List<UserSnapshot> = context.userPort.listUsers(includeSuspended, name)

    suspend fun createUser(request: CreateUserRequest): UserSnapshot = context.userPort.createUser(request)

    suspend fun createUser(
        name: String,
        password: String? = null,
    ): UserSnapshot = createUser(CreateUserRequest(name = name, password = password))

    suspend fun editUser(
        user: User,
        request: EditUserRequest,
    ) {
        context.userPort.editUser(user.id, request)
    }

    suspend fun editUser(
        user: User,
        displayName: String? = null,
        twitterId: String? = null,
        state: UserState? = null,
        role: String? = null,
    ) {
        editUser(
            user = user,
            request =
                EditUserRequest(
                    displayName = displayName,
                    twitterId = twitterId,
                    state = state,
                    role = role,
                ),
        )
    }

    suspend fun changeUserPassword(
        user: User,
        newPassword: String,
    ) {
        context.userPort.changePassword(user.id, newPassword)
    }

    suspend fun changeUserIcon(
        user: User,
        icon: UserIconUpload,
    ) {
        context.userPort.changeIcon(user.id, icon)
    }

    suspend fun User.getIcon(): ByteArray? = context.userPort.getIcon(id)

    suspend fun User.getDirectMessageChannelSnapshot(): DirectMessageChannelSnapshot? =
        context.userPort.getDirectMessageChannel(id)

    suspend fun User.getDirectMessageChannel(): Channel? = getDirectMessageChannelSnapshot()?.channel

    suspend fun User.getDirectMessages(query: MessageHistoryQuery = MessageHistoryQuery()): List<MessageSnapshot> =
        context.userPort.getDirectMessages(id, query)

    suspend fun User.sendMessage(
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot = sendDirectMessage(content, embed, nonce)

    suspend fun User.sendDirectMessage(
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot = postDirectMessage(this, content, embed, nonce)

    suspend fun UserSnapshot.sendDirectMessage(
        content: String,
        embed: Boolean = false,
        nonce: String? = null,
    ): MessageSnapshot = user.sendDirectMessage(content, embed, nonce)

    suspend fun User.getTags(): List<UserTagSnapshot> = context.userPort.getTags(id)

    suspend fun addTag(
        user: User,
        tag: String,
    ): UserTagSnapshot = context.userPort.addTag(user.id, tag)

    suspend fun editTag(
        user: User,
        tagId: UserTagId,
        isLocked: Boolean,
    ) {
        context.userPort.editTag(user.id, tagId, isLocked)
    }

    suspend fun removeTag(
        user: User,
        tagId: UserTagId,
    ) {
        context.userPort.removeTag(user.id, tagId)
    }

    suspend fun User.getStats(): UserStatsSnapshot? = context.userPort.getStats(id)
}
