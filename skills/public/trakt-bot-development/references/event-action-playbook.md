# Event to Action Playbook

## Common imports

```kotlin
import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.bot.scope.*
```

## Event selection matrix

| Event | Trigger meaning | Typical actions | Caveats |
|---|---|---|---|
| `MessageCreated` | public message posted | `reply`, `sendMessage`, `stamp` | best default for command bots |
| `MessageUpdated` | public message edited | `sendMessage`, `update`, `stamp` | use when edits matter |
| `DirectMessageCreated` | DM posted | `reply`, `sendDirectMessage` | private workflows |
| `DirectMessageUpdated` | DM edited | `sendDirectMessage` | updated DM text in `message.content` |
| `MessageDeleted` | public message deleted | `fetchChannel(...).sendMessage(...)` | message text is unavailable |
| `DirectMessageDeleted` | DM deleted | `fetchChannel(...)`, `sendMessage(...)` | message text is unavailable |
| `BotJoinedChannel` | bot added to channel | `event.channel.sendMessage(...)` | good onboarding hook |
| `BotLeftChannel` | bot removed from channel | logging / external side effects | cannot post to removed channel |
| `ChannelCreated` | channel created | `event.channel.handle.sendMessage(...)` | metadata event |
| `ChannelTopicChanged` | topic changed | `sendMessage`, `setTopic` | includes `updaterId` |
| `UserCreated` | new user created | `sendDirectMessage`, `tag` | start onboarding flows |
| `UserActivated` | user activated | `sendDirectMessage`, `tag` | often follows user creation |
| `StampCreated` | new stamp added | `sendMessage`, `fetchStamp` | stamp metadata available |
| `TagAdded` | tag dictionary changed | `fetchUsers`, `tag`, `untag` | event payload has tag id/text only |
| `TagRemoved` | tag dictionary changed | `fetchUsers`, `untag` | event payload has tag id/text only |

## High-signal handler patterns

### Command bot on public channels

```kotlin
on<MessageCreated> { event ->
    when (event.message.content.trim()) {
        "ping" -> event.message.reply("pong")
        "help" -> event.message.channel.sendMessage("commands: ping, help")
    }
}
```

### Edited-message watcher

```kotlin
on<MessageUpdated> { event ->
    if ("urgent" in event.message.content) {
        event.message.channel.sendMessage("message updated with urgent keyword")
    }
}
```

### Deletion audit (ID-only payload)

```kotlin
on<MessageDeleted> { event ->
    val channel = fetchChannel(event.channelId)
    channel.sendMessage("message deleted: ${event.messageId.value}")
}
```

### Join-channel onboarding

```kotlin
on<BotJoinedChannel> { event ->
    event.channel.sendMessage("hello, I am online")
}
```

## Selection rules

- Need message text and author/channel handles: choose `MessageCreated`, `MessageUpdated`, `DirectMessageCreated`, or `DirectMessageUpdated`.
- Need only deletion signal: choose `MessageDeleted` or `DirectMessageDeleted`.
- Need channel lifecycle reaction: choose `BotJoinedChannel`, `BotLeftChannel`, `ChannelCreated`, or `ChannelTopicChanged`.
- Need user/stamp/tag lifecycle reaction: choose `UserCreated`, `UserActivated`, `StampCreated`, `TagAdded`, or `TagRemoved`.
