package jp.xhw.trakt.bot

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.context.user.UserContext
import jp.xhw.trakt.bot.infrastructure.client.BotEventRegistrar
import jp.xhw.trakt.bot.infrastructure.client.SelfEventRegistrar
import jp.xhw.trakt.bot.model.BotEvents
import jp.xhw.trakt.bot.model.Disposed
import jp.xhw.trakt.bot.model.Initialized
import jp.xhw.trakt.bot.model.UserEvents
import kotlin.jvm.JvmName

@JvmName("onBotInitialized")
fun BotEventRegistrar.onInitialized(handler: suspend BotContext.(Initialized) -> Unit) {
    on<Initialized>(handler)
}

@JvmName("onBotDisposed")
fun BotEventRegistrar.onDisposed(handler: suspend BotContext.(Disposed) -> Unit) {
    on<Disposed>(handler)
}

@JvmName("onBotMessageCreated")
fun BotEventRegistrar.onMessageCreated(handler: suspend BotContext.(BotEvents.MessageCreated) -> Unit) {
    on<BotEvents.MessageCreated>(handler)
}

@JvmName("onBotMessageUpdated")
fun BotEventRegistrar.onMessageUpdated(handler: suspend BotContext.(BotEvents.MessageUpdated) -> Unit) {
    on<BotEvents.MessageUpdated>(handler)
}

@JvmName("onBotDirectMessageCreated")
fun BotEventRegistrar.onDirectMessageCreated(handler: suspend BotContext.(BotEvents.DirectMessageCreated) -> Unit) {
    on<BotEvents.DirectMessageCreated>(handler)
}

@JvmName("onBotDirectMessageUpdated")
fun BotEventRegistrar.onDirectMessageUpdated(handler: suspend BotContext.(BotEvents.DirectMessageUpdated) -> Unit) {
    on<BotEvents.DirectMessageUpdated>(handler)
}

@JvmName("onBotMessageDeleted")
fun BotEventRegistrar.onMessageDeleted(handler: suspend BotContext.(BotEvents.MessageDeleted) -> Unit) {
    on<BotEvents.MessageDeleted>(handler)
}

@JvmName("onBotDirectMessageDeleted")
fun BotEventRegistrar.onDirectMessageDeleted(handler: suspend BotContext.(BotEvents.DirectMessageDeleted) -> Unit) {
    on<BotEvents.DirectMessageDeleted>(handler)
}

fun BotEventRegistrar.onJoinedChannel(handler: suspend BotContext.(BotEvents.JoinedChannel) -> Unit) {
    on<BotEvents.JoinedChannel>(handler)
}

fun BotEventRegistrar.onLeftChannel(handler: suspend BotContext.(BotEvents.LeftChannel) -> Unit) {
    on<BotEvents.LeftChannel>(handler)
}

@JvmName("onBotChannelCreated")
fun BotEventRegistrar.onChannelCreated(handler: suspend BotContext.(BotEvents.ChannelCreated) -> Unit) {
    on<BotEvents.ChannelCreated>(handler)
}

fun BotEventRegistrar.onChannelTopicChanged(handler: suspend BotContext.(BotEvents.ChannelTopicChanged) -> Unit) {
    on<BotEvents.ChannelTopicChanged>(handler)
}

fun BotEventRegistrar.onUserCreated(handler: suspend BotContext.(BotEvents.UserCreated) -> Unit) {
    on<BotEvents.UserCreated>(handler)
}

fun BotEventRegistrar.onUserActivated(handler: suspend BotContext.(BotEvents.UserActivated) -> Unit) {
    on<BotEvents.UserActivated>(handler)
}

@JvmName("onBotStampCreated")
fun BotEventRegistrar.onStampCreated(handler: suspend BotContext.(BotEvents.StampCreated) -> Unit) {
    on<BotEvents.StampCreated>(handler)
}

fun BotEventRegistrar.onTagAdded(handler: suspend BotContext.(BotEvents.TagAdded) -> Unit) {
    on<BotEvents.TagAdded>(handler)
}

fun BotEventRegistrar.onTagRemoved(handler: suspend BotContext.(BotEvents.TagRemoved) -> Unit) {
    on<BotEvents.TagRemoved>(handler)
}

@JvmName("onUserInitialized")
fun SelfEventRegistrar.onInitialized(handler: suspend UserContext.(Initialized) -> Unit) {
    on<Initialized>(handler)
}

@JvmName("onUserDisposed")
fun SelfEventRegistrar.onDisposed(handler: suspend UserContext.(Disposed) -> Unit) {
    on<Disposed>(handler)
}

fun SelfEventRegistrar.onJoined(handler: suspend UserContext.(UserEvents.Joined) -> Unit) {
    on<UserEvents.Joined>(handler)
}

fun SelfEventRegistrar.onUpdated(handler: suspend UserContext.(UserEvents.Updated) -> Unit) {
    on<UserEvents.Updated>(handler)
}

fun SelfEventRegistrar.onTagsUpdated(handler: suspend UserContext.(UserEvents.TagsUpdated) -> Unit) {
    on<UserEvents.TagsUpdated>(handler)
}

fun SelfEventRegistrar.onIconUpdated(handler: suspend UserContext.(UserEvents.IconUpdated) -> Unit) {
    on<UserEvents.IconUpdated>(handler)
}

fun SelfEventRegistrar.onWebRtcStateChanged(handler: suspend UserContext.(UserEvents.WebRtcStateChanged) -> Unit) {
    on<UserEvents.WebRtcStateChanged>(handler)
}

fun SelfEventRegistrar.onViewStateChanged(handler: suspend UserContext.(UserEvents.ViewStateChanged) -> Unit) {
    on<UserEvents.ViewStateChanged>(handler)
}

fun SelfEventRegistrar.onOnline(handler: suspend UserContext.(UserEvents.Online) -> Unit) {
    on<UserEvents.Online>(handler)
}

fun SelfEventRegistrar.onOffline(handler: suspend UserContext.(UserEvents.Offline) -> Unit) {
    on<UserEvents.Offline>(handler)
}

fun SelfEventRegistrar.onGroupCreated(handler: suspend UserContext.(UserEvents.GroupCreated) -> Unit) {
    on<UserEvents.GroupCreated>(handler)
}

fun SelfEventRegistrar.onGroupUpdated(handler: suspend UserContext.(UserEvents.GroupUpdated) -> Unit) {
    on<UserEvents.GroupUpdated>(handler)
}

fun SelfEventRegistrar.onGroupDeleted(handler: suspend UserContext.(UserEvents.GroupDeleted) -> Unit) {
    on<UserEvents.GroupDeleted>(handler)
}

@JvmName("onUserChannelCreated")
fun SelfEventRegistrar.onChannelCreated(handler: suspend UserContext.(UserEvents.ChannelCreated) -> Unit) {
    on<UserEvents.ChannelCreated>(handler)
}

fun SelfEventRegistrar.onChannelUpdated(handler: suspend UserContext.(UserEvents.ChannelUpdated) -> Unit) {
    on<UserEvents.ChannelUpdated>(handler)
}

fun SelfEventRegistrar.onChannelDeleted(handler: suspend UserContext.(UserEvents.ChannelDeleted) -> Unit) {
    on<UserEvents.ChannelDeleted>(handler)
}

fun SelfEventRegistrar.onChannelStared(handler: suspend UserContext.(UserEvents.ChannelStared) -> Unit) {
    on<UserEvents.ChannelStared>(handler)
}

fun SelfEventRegistrar.onChannelUnstared(handler: suspend UserContext.(UserEvents.ChannelUnstared) -> Unit) {
    on<UserEvents.ChannelUnstared>(handler)
}

fun SelfEventRegistrar.onChannelViewersChanged(handler: suspend UserContext.(UserEvents.ChannelViewersChanged) -> Unit) {
    on<UserEvents.ChannelViewersChanged>(handler)
}

fun SelfEventRegistrar.onChannelSubscribersChanged(handler: suspend UserContext.(UserEvents.ChannelSubscribersChanged) -> Unit) {
    on<UserEvents.ChannelSubscribersChanged>(handler)
}

@JvmName("onUserMessageCreated")
fun SelfEventRegistrar.onMessageCreated(handler: suspend UserContext.(UserEvents.MessageCreated) -> Unit) {
    on<UserEvents.MessageCreated>(handler)
}

@JvmName("onUserMessageUpdated")
fun SelfEventRegistrar.onMessageUpdated(handler: suspend UserContext.(UserEvents.MessageUpdated) -> Unit) {
    on<UserEvents.MessageUpdated>(handler)
}

@JvmName("onUserMessageDeleted")
fun SelfEventRegistrar.onMessageDeleted(handler: suspend UserContext.(UserEvents.MessageDeleted) -> Unit) {
    on<UserEvents.MessageDeleted>(handler)
}

fun SelfEventRegistrar.onMessageStamped(handler: suspend UserContext.(UserEvents.MessageStamped) -> Unit) {
    on<UserEvents.MessageStamped>(handler)
}

fun SelfEventRegistrar.onMessageUnstamped(handler: suspend UserContext.(UserEvents.MessageUnstamped) -> Unit) {
    on<UserEvents.MessageUnstamped>(handler)
}

fun SelfEventRegistrar.onMessagePinned(handler: suspend UserContext.(UserEvents.MessagePinned) -> Unit) {
    on<UserEvents.MessagePinned>(handler)
}

fun SelfEventRegistrar.onMessageUnpinned(handler: suspend UserContext.(UserEvents.MessageUnpinned) -> Unit) {
    on<UserEvents.MessageUnpinned>(handler)
}

fun SelfEventRegistrar.onMessageRead(handler: suspend UserContext.(UserEvents.MessageRead) -> Unit) {
    on<UserEvents.MessageRead>(handler)
}

@JvmName("onUserStampCreated")
fun SelfEventRegistrar.onStampCreated(handler: suspend UserContext.(UserEvents.StampCreated) -> Unit) {
    on<UserEvents.StampCreated>(handler)
}

fun SelfEventRegistrar.onStampUpdated(handler: suspend UserContext.(UserEvents.StampUpdated) -> Unit) {
    on<UserEvents.StampUpdated>(handler)
}

fun SelfEventRegistrar.onStampDeleted(handler: suspend UserContext.(UserEvents.StampDeleted) -> Unit) {
    on<UserEvents.StampDeleted>(handler)
}

fun SelfEventRegistrar.onStampPaletteCreated(handler: suspend UserContext.(UserEvents.StampPaletteCreated) -> Unit) {
    on<UserEvents.StampPaletteCreated>(handler)
}

fun SelfEventRegistrar.onStampPaletteUpdated(handler: suspend UserContext.(UserEvents.StampPaletteUpdated) -> Unit) {
    on<UserEvents.StampPaletteUpdated>(handler)
}

fun SelfEventRegistrar.onStampPaletteDeleted(handler: suspend UserContext.(UserEvents.StampPaletteDeleted) -> Unit) {
    on<UserEvents.StampPaletteDeleted>(handler)
}

fun SelfEventRegistrar.onClipFolderCreated(handler: suspend UserContext.(UserEvents.ClipFolderCreated) -> Unit) {
    on<UserEvents.ClipFolderCreated>(handler)
}

fun SelfEventRegistrar.onClipFolderUpdated(handler: suspend UserContext.(UserEvents.ClipFolderUpdated) -> Unit) {
    on<UserEvents.ClipFolderUpdated>(handler)
}

fun SelfEventRegistrar.onClipFolderDeleted(handler: suspend UserContext.(UserEvents.ClipFolderDeleted) -> Unit) {
    on<UserEvents.ClipFolderDeleted>(handler)
}

fun SelfEventRegistrar.onClipFolderMessageDeleted(handler: suspend UserContext.(UserEvents.ClipFolderMessageDeleted) -> Unit) {
    on<UserEvents.ClipFolderMessageDeleted>(handler)
}

fun SelfEventRegistrar.onClipFolderMessageAdded(handler: suspend UserContext.(UserEvents.ClipFolderMessageAdded) -> Unit) {
    on<UserEvents.ClipFolderMessageAdded>(handler)
}

fun SelfEventRegistrar.onQallRoomStateChanged(handler: suspend UserContext.(UserEvents.QallRoomStateChanged) -> Unit) {
    on<UserEvents.QallRoomStateChanged>(handler)
}

fun SelfEventRegistrar.onQallSoundboardItemCreated(handler: suspend UserContext.(UserEvents.QallSoundboardItemCreated) -> Unit) {
    on<UserEvents.QallSoundboardItemCreated>(handler)
}

fun SelfEventRegistrar.onQallSoundboardItemDeleted(handler: suspend UserContext.(UserEvents.QallSoundboardItemDeleted) -> Unit) {
    on<UserEvents.QallSoundboardItemDeleted>(handler)
}
