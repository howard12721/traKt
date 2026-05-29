package jp.xhw.trakt.bot.model

internal fun sameChannelId(
    self: Channel,
    other: Any?,
): Boolean = other is Channel && self.id == other.id

internal fun sameUserId(
    self: User,
    other: Any?,
): Boolean = other is User && self.id == other.id

internal fun sameUserTagId(
    self: UserTag,
    other: Any?,
): Boolean = other is UserTag && self.id == other.id

internal fun sameMessageId(
    self: Message,
    other: Any?,
): Boolean = other is Message && self.id == other.id

internal fun sameStampId(
    self: Stamp,
    other: Any?,
): Boolean = other is Stamp && self.id == other.id

internal fun sameGroupId(
    self: Group,
    other: Any?,
): Boolean = other is Group && self.id == other.id

internal fun sameFileId(
    self: File,
    other: Any?,
): Boolean = other is File && self.id == other.id

internal fun sameClipFolderId(
    self: ClipFolder,
    other: Any?,
): Boolean = other is ClipFolder && self.id == other.id

internal fun sameWebhookId(
    self: Webhook,
    other: Any?,
): Boolean = other is Webhook && self.id == other.id

internal fun sameManagedBotId(
    self: ManagedBot,
    other: Any?,
): Boolean = other is ManagedBot && self.id == other.id

internal fun sameLoginSessionId(
    self: LoginSession,
    other: Any?,
): Boolean = other is LoginSession && self.id == other.id

internal fun sameActiveOAuth2TokenId(
    self: ActiveOAuth2Token,
    other: Any?,
): Boolean = other is ActiveOAuth2Token && self.id == other.id

internal fun sameStampPaletteId(
    self: StampPalette,
    other: Any?,
): Boolean = other is StampPalette && self.id == other.id

internal fun sameQallSoundId(
    self: QallSound,
    other: Any?,
): Boolean = other is QallSound && self.id == other.id

internal fun sameQallRoomId(
    self: QallRoom,
    other: Any?,
): Boolean = other is QallRoom && self.id == other.id
