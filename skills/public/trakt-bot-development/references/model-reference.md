# Model Reference

## Event hierarchy

- `Event`: base interface with `occurredAt`.
- `MessageEvent`: subtype of `Event` with `message: Message`.

## Event payload reference

| Event | Payload highlights | Notes |
|---|---|---|
| `MessageCreated` | `message` | public channel post |
| `MessageUpdated` | `message` | public channel edit |
| `DirectMessageCreated` | `message` | DM post |
| `DirectMessageUpdated` | `message` | DM edit |
| `MessageDeleted` | `messageId`, `channelId` | IDs only; original text unavailable |
| `DirectMessageDeleted` | `messageId`, `channelId` | IDs only; original text unavailable |
| `BotJoinedChannel` | `channelId`, `channel` handle | use `event.channel.sendMessage(...)` |
| `BotLeftChannel` | `channelId`, `channel` handle | bot is already out of channel |
| `ChannelCreated` | `channel: Channel.Meta` | includes creator and path |
| `ChannelTopicChanged` | `channel`, `topic`, `updaterId`, `updater` handle | topic update metadata |
| `UserCreated` | `user: User.Minimal` | lightweight user data |
| `UserActivated` | `user: User.Minimal` | activation signal |
| `StampCreated` | `stamp: Stamp.Basic` | includes creator/file IDs |
| `TagAdded` | `tagId`, `tag` | user tag dictionary changed |
| `TagRemoved` | `tagId`, `tag` | user tag dictionary changed |

## Message model

Core fields:

- IDs: `id`, `authorId`, `channelId`
- text/meta: `content`, `createdAt`, `updatedAt`, `nonce`
- state: `isPinned`, `stamps`, `threadId`

Convenience handles:

- `message.handle` -> `MessageHandle`
- `message.author` -> `UserHandle`
- `message.channel` -> `ChannelHandle`

## Channel model

- `Channel.DirectMessage`: DM channel with `userId`.
- `Channel.Meta`: lightweight public channel snapshot (event payload).
- `Channel.Detail`: full channel state from API.
- `ChannelDirectory`: result of `fetchChannels(...)`.

Convenience handles:

- `channel.handle`
- `Channel.PublicChannel.parent`
- `Channel.Detail.children`

## User model

- `User.Minimal`: event payload user.
- `User.Basic`: list-oriented user with state.
- `User.Detail`: full user details (`tags`, `groupIds`, `homeChannelId`, etc.).
- `Bot`: mapping between `botId` and `userId`.

Convenience handles:

- `user.handle`
- `user.iconFile`
- `User.Detail.groups`
- `User.Detail.homeChannelHandle`

## File / Group / Stamp model

- File: `FileMeta`, `FileHandle`, `FileId`
- Group: `Group`, `GroupHandle`, `GroupMember`, `GroupId`
- Stamp: `Stamp.Basic`, `Stamp.Detail`, `StampHandle`, `StampId`

## Handle vs entity rules

- Use handles (`*Handle`) when you only need ID-based operations.
- Resolve to entities when you need loaded fields from API.
- Prefer these conversions:
  - entity -> handle: `entity.handle`
  - handle -> entity: `handle.resolve()`
  - refresh stale detail: `detail.refresh()`

## Search model

`searchMessages(...)` returns `SearchResult(totalHits, hits)` and supports:

- text/time filters: `word`, `after`, `before`
- relation filters: `inChannel`, `to`, `from`, `citation`, `bot`
- content filters: `hasUrl`, `hasAttachments`, `hasImage`, `hasVideo`, `hasAudio`
- paging/sort: `limit`, `offset`, `sort`

For callable APIs, always use `action-reference.md` as the action source of truth.
