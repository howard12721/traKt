# Action Reference (Complete)

All actions below are callable from `BotScope` (`context(scope: BotScope)` functions).

Use this file as the action source of truth when generating code.

- If a user asks "what can I call from a handler?", answer from this list.
- If a user asks for an action by model type, pick matching receiver rows (`Message`, `MessageHandle`, `User`, `UserHandle`, etc.).
- Overloads are listed separately when signatures differ.

## Table of contents

1. Bot actions
2. Channel actions
3. Message actions
4. User actions
5. File actions
6. Group actions
7. Stamp actions

## Bot actions

| Action | Purpose | Returns |
|---|---|---|
| `ChannelHandle.join()` | Join the channel as the current bot | `Unit` |
| `ChannelHandle.leave()` | Leave the channel as the current bot | `Unit` |

## Channel actions

| Action | Purpose | Returns |
|---|---|---|
| `fetchChannel(channelId: ChannelId)` | Fetch channel detail by typed ID | `Channel.Detail` |
| `fetchChannel(channelId: Uuid)` | Fetch channel detail by UUID | `Channel.Detail` |
| `ChannelHandle.resolve()` | Resolve handle into channel detail | `Channel.Detail` |
| `Channel.resolve()` | Resolve channel into detail (re-fetch if needed) | `Channel.Detail` |
| `Channel.Detail.refresh()` | Refresh channel detail from API | `Channel.Detail` |
| `fetchChannels(includeDirectMessages, path)` | Fetch channel list with optional filters | `ChannelDirectory` |
| `ChannelHandle.fetchPath()` | Fetch channel path | `ChannelPath` |
| `ChannelHandle.fetchTopic()` | Fetch channel topic | `String` |
| `ChannelHandle.setTopic(topic)` | Update channel topic | `Unit` |
| `ChannelHandle.fetchSubscribers()` | Fetch subscriber user IDs | `List<UserId>` |
| `ChannelHandle.setSubscribers(subscribers)` | Replace subscribers list | `Unit` |
| `ChannelHandle.fetchViewers()` | Fetch channel viewers with states | `List<ChannelViewer>` |
| `ChannelHandle.fetchBots()` | Fetch bots in channel | `List<Bot>` |
| `Channel.fetchSubscribers()` | Fetch subscriber user IDs | `List<UserId>` |
| `Channel.fetchViewers()` | Fetch channel viewers with states | `List<ChannelViewer>` |
| `Channel.fetchBots()` | Fetch bots in channel | `List<Bot>` |
| `ChannelHandle.fetchPins()` | Fetch pinned messages in channel | `List<Pin>` |
| `ChannelHandle.fetchStats()` | Fetch channel stats | `ChannelStats` |
| `ChannelHandle.fetchMessages(limit, offset, since, until, inclusive, order)` | Fetch channel messages | `List<Message>` |
| `ChannelHandle.sendMessage(content, embed, nonce)` | Send message to channel handle | `Message` |
| `Channel.sendMessage(content, embed, nonce)` | Send message to channel entity | `Message` |

## Message actions

| Action | Purpose | Returns |
|---|---|---|
| `fetchMessage(messageId: MessageId)` | Fetch message by typed ID | `Message` |
| `fetchMessage(messageId: Uuid)` | Fetch message by UUID | `Message` |
| `MessageHandle.resolve()` | Resolve handle into message entity | `Message` |
| `MessageHandle.fetchStamps()` | Fetch stamps on message | `List<MessageStamp>` |
| `MessageHandle.fetchPinInfo()` | Fetch pin info for message | `PinInfo` |
| `MessageHandle.update(content, embed, nonce)` | Update message content | `Unit` |
| `MessageHandle.delete()` | Delete message | `Unit` |
| `MessageHandle.stamp(stampId, count)` | Add stamp to message | `Unit` |
| `MessageHandle.unstamp(stampId)` | Remove stamp from message | `Unit` |
| `MessageHandle.pin()` | Pin message | `PinInfo` |
| `MessageHandle.unpin()` | Unpin message | `Unit` |
| `searchMessages(word, after, before, inChannel, to, from, citation, bot, hasUrl, hasAttachments, hasImage, hasVideo, hasAudio, limit, offset, sort)` | Search messages with filters | `SearchResult` |
| `MessageHandle.reply(content, embed, nonce)` | Reply with source message URL appended | `Message` |
| `Message.refresh()` | Refresh message entity | `Message` |
| `Message.fetchStamps()` | Fetch stamps on message | `List<MessageStamp>` |
| `Message.fetchPinInfo()` | Fetch pin info for message | `PinInfo` |
| `Message.update(content, embed, nonce)` | Update message content | `Unit` |
| `Message.delete()` | Delete message | `Unit` |
| `Message.stamp(stampId, count)` | Add stamp to message | `Unit` |
| `Message.unstamp(stampId)` | Remove stamp from message | `Unit` |
| `Message.pin()` | Pin message | `PinInfo` |
| `Message.unpin()` | Unpin message | `Unit` |
| `Message.reply(content, embed, nonce)` | Reply with source message URL appended | `Message` |

## User actions

| Action | Purpose | Returns |
|---|---|---|
| `fetchUser(userId: UserId)` | Fetch user detail by typed ID | `User.Detail` |
| `fetchUser(userId: Uuid)` | Fetch user detail by UUID | `User.Detail` |
| `UserHandle.resolve()` | Resolve handle into user entity | `User` |
| `User.Minimal.resolve()` | Resolve minimal user to full user | `User` |
| `User.Basic.resolve()` | Resolve basic user to full user | `User` |
| `User.Detail.refresh()` | Refresh user detail from API | `User` |
| `fetchUsers(includeSuspended, name)` | Fetch users list | `List<User.Basic>` |
| `UserHandle.downloadIcon()` | Download user icon bytes | `ByteArray` |
| `UserHandle.fetchDirectMessageChannel()` | Fetch DM channel for user | `Channel.DirectMessage` |
| `UserHandle.fetchDirectMessages(limit, offset, since, until, inclusive, order)` | Fetch DM messages with user | `List<Message>` |
| `UserHandle.sendDirectMessage(content, embed, nonce)` | Send DM using user handle | `Message` |
| `User.sendDirectMessage(content, embed, nonce)` | Send DM using user entity | `Message` |
| `UserHandle.fetchTags()` | Fetch user tags | `List<UserTag>` |
| `UserHandle.tag(tag)` | Add user tag | `UserTag` |
| `UserHandle.untag(tagId)` | Remove user tag | `Unit` |
| `UserHandle.fetchStats()` | Fetch user stats | `UserStats` |
| `User.downloadIcon()` | Download user icon bytes | `ByteArray` |
| `User.fetchDirectMessageChannel()` | Fetch DM channel for user | `Channel.DirectMessage` |
| `User.fetchDirectMessages(limit, offset, since, until, inclusive, order)` | Fetch DM messages with user | `List<Message>` |
| `User.fetchTags()` | Fetch user tags | `List<UserTag>` |
| `User.tag(tag)` | Add user tag | `UserTag` |
| `User.untag(tagId)` | Remove user tag | `Unit` |
| `User.fetchStats()` | Fetch user stats | `UserStats` |

## File actions

| Action | Purpose | Returns |
|---|---|---|
| `uploadFile(channelHandle, file, fileName, contentType)` | Upload file to channel | `FileMeta` |
| `FileHandle.fetchMeta()` | Fetch file metadata | `FileMeta` |
| `FileHandle.download()` | Download file bytes | `ByteArray` |
| `FileMeta.download()` | Download file bytes | `ByteArray` |
| `FileHandle.delete()` | Delete file | `Unit` |
| `FileMeta.delete()` | Delete file | `Unit` |
| `FileMeta.refresh()` | Refresh file metadata | `FileMeta` |

## Group actions

| Action | Purpose | Returns |
|---|---|---|
| `fetchGroup(groupId)` | Fetch group by ID | `Group` |
| `GroupHandle.resolve()` | Resolve group handle | `Group` |
| `fetchGroups()` | Fetch all groups | `List<Group>` |
| `GroupHandle.fetchMembers()` | Fetch group members | `List<GroupMember>` |
| `Group.refresh()` | Refresh group data | `Group` |

## Stamp actions

| Action | Purpose | Returns |
|---|---|---|
| `fetchStamp(stampId)` | Fetch stamp detail | `Stamp.Detail` |
| `StampHandle.resolve()` | Resolve stamp handle | `Stamp.Detail` |
| `fetchStamps(type)` | Fetch stamp list | `List<Stamp.Detail>` |
| `Stamp.resolve()` | Resolve stamp entity to detail | `Stamp.Detail` |
| `Stamp.Detail.refresh()` | Refresh stamp detail | `Stamp.Detail` |
