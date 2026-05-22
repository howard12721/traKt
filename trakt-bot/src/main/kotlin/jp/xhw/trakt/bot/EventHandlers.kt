package jp.xhw.trakt.bot

import jp.xhw.trakt.bot.context.bot.BotContext
import jp.xhw.trakt.bot.context.user.UserContext
import jp.xhw.trakt.bot.infrastructure.runtime.SelfTraktClientBuilder
import jp.xhw.trakt.bot.infrastructure.runtime.TraktClientBuilder
import jp.xhw.trakt.bot.model.BotEvents
import jp.xhw.trakt.bot.model.Disposed
import jp.xhw.trakt.bot.model.Initialized
import jp.xhw.trakt.bot.model.UserEvents
import kotlin.jvm.JvmName

@JvmName("onBotInitialized")
fun TraktClientBuilder.onInitialized(handler: suspend BotContext.(Initialized) -> Unit) {
    on<Initialized>(handler)
}

@JvmName("onBotDisposed")
fun TraktClientBuilder.onDisposed(handler: suspend BotContext.(Disposed) -> Unit) {
    on<Disposed>(handler)
}

@JvmName("onBotMessageCreated")
fun TraktClientBuilder.onMessageCreated(handler: suspend BotContext.(BotEvents.MessageCreated) -> Unit) {
    on<BotEvents.MessageCreated>(handler)
}

@JvmName("onBotMessageUpdated")
fun TraktClientBuilder.onMessageUpdated(handler: suspend BotContext.(BotEvents.MessageUpdated) -> Unit) {
    on<BotEvents.MessageUpdated>(handler)
}

@JvmName("onBotDirectMessageCreated")
fun TraktClientBuilder.onDirectMessageCreated(handler: suspend BotContext.(BotEvents.DirectMessageCreated) -> Unit) {
    on<BotEvents.DirectMessageCreated>(handler)
}

@JvmName("onBotDirectMessageUpdated")
fun TraktClientBuilder.onDirectMessageUpdated(handler: suspend BotContext.(BotEvents.DirectMessageUpdated) -> Unit) {
    on<BotEvents.DirectMessageUpdated>(handler)
}

@JvmName("onBotMessageDeleted")
fun TraktClientBuilder.onMessageDeleted(handler: suspend BotContext.(BotEvents.MessageDeleted) -> Unit) {
    on<BotEvents.MessageDeleted>(handler)
}

@JvmName("onBotDirectMessageDeleted")
fun TraktClientBuilder.onDirectMessageDeleted(handler: suspend BotContext.(BotEvents.DirectMessageDeleted) -> Unit) {
    on<BotEvents.DirectMessageDeleted>(handler)
}

fun TraktClientBuilder.onJoinedChannel(handler: suspend BotContext.(BotEvents.JoinedChannel) -> Unit) {
    on<BotEvents.JoinedChannel>(handler)
}

fun TraktClientBuilder.onLeftChannel(handler: suspend BotContext.(BotEvents.LeftChannel) -> Unit) {
    on<BotEvents.LeftChannel>(handler)
}

@JvmName("onBotChannelCreated")
fun TraktClientBuilder.onChannelCreated(handler: suspend BotContext.(BotEvents.ChannelCreated) -> Unit) {
    on<BotEvents.ChannelCreated>(handler)
}

fun TraktClientBuilder.onChannelTopicChanged(handler: suspend BotContext.(BotEvents.ChannelTopicChanged) -> Unit) {
    on<BotEvents.ChannelTopicChanged>(handler)
}

fun TraktClientBuilder.onUserCreated(handler: suspend BotContext.(BotEvents.UserCreated) -> Unit) {
    on<BotEvents.UserCreated>(handler)
}

fun TraktClientBuilder.onUserActivated(handler: suspend BotContext.(BotEvents.UserActivated) -> Unit) {
    on<BotEvents.UserActivated>(handler)
}

@JvmName("onBotStampCreated")
fun TraktClientBuilder.onStampCreated(handler: suspend BotContext.(BotEvents.StampCreated) -> Unit) {
    on<BotEvents.StampCreated>(handler)
}

fun TraktClientBuilder.onTagAdded(handler: suspend BotContext.(BotEvents.TagAdded) -> Unit) {
    on<BotEvents.TagAdded>(handler)
}

fun TraktClientBuilder.onTagRemoved(handler: suspend BotContext.(BotEvents.TagRemoved) -> Unit) {
    on<BotEvents.TagRemoved>(handler)
}

@JvmName("onUserInitialized")
fun SelfTraktClientBuilder.onInitialized(handler: suspend UserContext.(Initialized) -> Unit) {
    on<Initialized>(handler)
}

@JvmName("onUserDisposed")
fun SelfTraktClientBuilder.onDisposed(handler: suspend UserContext.(Disposed) -> Unit) {
    on<Disposed>(handler)
}

fun SelfTraktClientBuilder.onJoined(handler: suspend UserContext.(UserEvents.Joined) -> Unit) {
    on<UserEvents.Joined>(handler)
}

fun SelfTraktClientBuilder.onUpdated(handler: suspend UserContext.(UserEvents.Updated) -> Unit) {
    on<UserEvents.Updated>(handler)
}

fun SelfTraktClientBuilder.onTagsUpdated(handler: suspend UserContext.(UserEvents.TagsUpdated) -> Unit) {
    on<UserEvents.TagsUpdated>(handler)
}

fun SelfTraktClientBuilder.onIconUpdated(handler: suspend UserContext.(UserEvents.IconUpdated) -> Unit) {
    on<UserEvents.IconUpdated>(handler)
}

fun SelfTraktClientBuilder.onWebRtcStateChanged(handler: suspend UserContext.(UserEvents.WebRtcStateChanged) -> Unit) {
    on<UserEvents.WebRtcStateChanged>(handler)
}

fun SelfTraktClientBuilder.onViewStateChanged(handler: suspend UserContext.(UserEvents.ViewStateChanged) -> Unit) {
    on<UserEvents.ViewStateChanged>(handler)
}

fun SelfTraktClientBuilder.onOnline(handler: suspend UserContext.(UserEvents.Online) -> Unit) {
    on<UserEvents.Online>(handler)
}

fun SelfTraktClientBuilder.onOffline(handler: suspend UserContext.(UserEvents.Offline) -> Unit) {
    on<UserEvents.Offline>(handler)
}

fun SelfTraktClientBuilder.onGroupCreated(handler: suspend UserContext.(UserEvents.GroupCreated) -> Unit) {
    on<UserEvents.GroupCreated>(handler)
}

fun SelfTraktClientBuilder.onGroupUpdated(handler: suspend UserContext.(UserEvents.GroupUpdated) -> Unit) {
    on<UserEvents.GroupUpdated>(handler)
}

fun SelfTraktClientBuilder.onGroupDeleted(handler: suspend UserContext.(UserEvents.GroupDeleted) -> Unit) {
    on<UserEvents.GroupDeleted>(handler)
}

@JvmName("onUserChannelCreated")
fun SelfTraktClientBuilder.onChannelCreated(handler: suspend UserContext.(UserEvents.ChannelCreated) -> Unit) {
    on<UserEvents.ChannelCreated>(handler)
}

fun SelfTraktClientBuilder.onChannelUpdated(handler: suspend UserContext.(UserEvents.ChannelUpdated) -> Unit) {
    on<UserEvents.ChannelUpdated>(handler)
}

fun SelfTraktClientBuilder.onChannelDeleted(handler: suspend UserContext.(UserEvents.ChannelDeleted) -> Unit) {
    on<UserEvents.ChannelDeleted>(handler)
}

fun SelfTraktClientBuilder.onChannelStared(handler: suspend UserContext.(UserEvents.ChannelStared) -> Unit) {
    on<UserEvents.ChannelStared>(handler)
}

fun SelfTraktClientBuilder.onChannelUnstared(handler: suspend UserContext.(UserEvents.ChannelUnstared) -> Unit) {
    on<UserEvents.ChannelUnstared>(handler)
}

fun SelfTraktClientBuilder.onChannelViewersChanged(handler: suspend UserContext.(UserEvents.ChannelViewersChanged) -> Unit) {
    on<UserEvents.ChannelViewersChanged>(handler)
}

fun SelfTraktClientBuilder.onChannelSubscribersChanged(handler: suspend UserContext.(UserEvents.ChannelSubscribersChanged) -> Unit) {
    on<UserEvents.ChannelSubscribersChanged>(handler)
}

@JvmName("onUserMessageCreated")
fun SelfTraktClientBuilder.onMessageCreated(handler: suspend UserContext.(UserEvents.MessageCreated) -> Unit) {
    on<UserEvents.MessageCreated>(handler)
}

@JvmName("onUserMessageUpdated")
fun SelfTraktClientBuilder.onMessageUpdated(handler: suspend UserContext.(UserEvents.MessageUpdated) -> Unit) {
    on<UserEvents.MessageUpdated>(handler)
}

@JvmName("onUserMessageDeleted")
fun SelfTraktClientBuilder.onMessageDeleted(handler: suspend UserContext.(UserEvents.MessageDeleted) -> Unit) {
    on<UserEvents.MessageDeleted>(handler)
}

fun SelfTraktClientBuilder.onMessageStamped(handler: suspend UserContext.(UserEvents.MessageStamped) -> Unit) {
    on<UserEvents.MessageStamped>(handler)
}

fun SelfTraktClientBuilder.onMessageUnstamped(handler: suspend UserContext.(UserEvents.MessageUnstamped) -> Unit) {
    on<UserEvents.MessageUnstamped>(handler)
}

fun SelfTraktClientBuilder.onMessagePinned(handler: suspend UserContext.(UserEvents.MessagePinned) -> Unit) {
    on<UserEvents.MessagePinned>(handler)
}

fun SelfTraktClientBuilder.onMessageUnpinned(handler: suspend UserContext.(UserEvents.MessageUnpinned) -> Unit) {
    on<UserEvents.MessageUnpinned>(handler)
}

fun SelfTraktClientBuilder.onMessageRead(handler: suspend UserContext.(UserEvents.MessageRead) -> Unit) {
    on<UserEvents.MessageRead>(handler)
}

@JvmName("onUserStampCreated")
fun SelfTraktClientBuilder.onStampCreated(handler: suspend UserContext.(UserEvents.StampCreated) -> Unit) {
    on<UserEvents.StampCreated>(handler)
}

fun SelfTraktClientBuilder.onStampUpdated(handler: suspend UserContext.(UserEvents.StampUpdated) -> Unit) {
    on<UserEvents.StampUpdated>(handler)
}

fun SelfTraktClientBuilder.onStampDeleted(handler: suspend UserContext.(UserEvents.StampDeleted) -> Unit) {
    on<UserEvents.StampDeleted>(handler)
}

fun SelfTraktClientBuilder.onStampPaletteCreated(handler: suspend UserContext.(UserEvents.StampPaletteCreated) -> Unit) {
    on<UserEvents.StampPaletteCreated>(handler)
}

fun SelfTraktClientBuilder.onStampPaletteUpdated(handler: suspend UserContext.(UserEvents.StampPaletteUpdated) -> Unit) {
    on<UserEvents.StampPaletteUpdated>(handler)
}

fun SelfTraktClientBuilder.onStampPaletteDeleted(handler: suspend UserContext.(UserEvents.StampPaletteDeleted) -> Unit) {
    on<UserEvents.StampPaletteDeleted>(handler)
}

fun SelfTraktClientBuilder.onClipFolderCreated(handler: suspend UserContext.(UserEvents.ClipFolderCreated) -> Unit) {
    on<UserEvents.ClipFolderCreated>(handler)
}

fun SelfTraktClientBuilder.onClipFolderUpdated(handler: suspend UserContext.(UserEvents.ClipFolderUpdated) -> Unit) {
    on<UserEvents.ClipFolderUpdated>(handler)
}

fun SelfTraktClientBuilder.onClipFolderDeleted(handler: suspend UserContext.(UserEvents.ClipFolderDeleted) -> Unit) {
    on<UserEvents.ClipFolderDeleted>(handler)
}

fun SelfTraktClientBuilder.onClipFolderMessageDeleted(handler: suspend UserContext.(UserEvents.ClipFolderMessageDeleted) -> Unit) {
    on<UserEvents.ClipFolderMessageDeleted>(handler)
}

fun SelfTraktClientBuilder.onClipFolderMessageAdded(handler: suspend UserContext.(UserEvents.ClipFolderMessageAdded) -> Unit) {
    on<UserEvents.ClipFolderMessageAdded>(handler)
}

fun SelfTraktClientBuilder.onQallRoomStateChanged(handler: suspend UserContext.(UserEvents.QallRoomStateChanged) -> Unit) {
    on<UserEvents.QallRoomStateChanged>(handler)
}

fun SelfTraktClientBuilder.onQallSoundboardItemCreated(handler: suspend UserContext.(UserEvents.QallSoundboardItemCreated) -> Unit) {
    on<UserEvents.QallSoundboardItemCreated>(handler)
}

fun SelfTraktClientBuilder.onQallSoundboardItemDeleted(handler: suspend UserContext.(UserEvents.QallSoundboardItemDeleted) -> Unit) {
    on<UserEvents.QallSoundboardItemDeleted>(handler)
}
