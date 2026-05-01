package jp.xhw.trakt.bot.scope.bot

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant
import kotlin.uuid.Uuid

// --- Fetch ---

/**
 * ユーザー詳細を取得します。
 *
 * @param userId 取得対象ユーザーID
 * @return ユーザー詳細情報
 */
context(ctx: BotContext)
suspend fun fetchUser(userId: UserId): User.Detail = ctx.userPort.fetchUser(userId)

/**
 * ユーザー詳細を取得します。
 *
 * @param userId 取得対象ユーザーID(UUID)
 * @return ユーザー詳細情報
 */
context(ctx: BotContext)
suspend fun fetchUser(userId: Uuid): User.Detail = fetchUser(UserId(userId))

/**
 * このハンドルが指すユーザーを取得します。
 *
 * @return ユーザー情報
 */
context(ctx: BotContext)
suspend fun UserHandle.resolve(): User = ctx.userPort.fetchUser(id)

/**
 * ユーザー情報を再取得します。
 *
 * @return ユーザー情報
 */
context(ctx: BotContext)
suspend fun User.Minimal.resolve(): User = handle.resolve()

/**
 * ユーザー情報を再取得します。
 *
 * @return ユーザー情報
 */
context(ctx: BotContext)
suspend fun User.Basic.resolve(): User = handle.resolve()

/**
 * ユーザー詳細情報を再取得します。
 *
 * @return ユーザー情報
 */
context(ctx: BotContext)
suspend fun User.Detail.refresh(): User = handle.resolve()

/**
 * ユーザー一覧を取得します。
 *
 * @param includeSuspended `true` の場合は非アクティブユーザーも含めます
 * @param name 指定時は名前一致ユーザーのみ取得します
 * @return ユーザー基本情報の一覧
 */
context(ctx: BotContext)
suspend fun fetchUsers(
    includeSuspended: Boolean = false,
    name: String? = null,
): List<User.Basic> = ctx.userPort.fetchUsers(includeSuspended, name)

/**
 * ユーザーアイコンをダウンロードします。
 *
 * @return アイコン画像のバイト列
 */
context(ctx: BotContext)
suspend fun UserHandle.downloadIcon(): ByteArray = ctx.userPort.fetchUserIcon(id)

/**
 * 指定ユーザーとの DM チャンネル情報を取得します。
 *
 * @return DM チャンネル
 */
context(ctx: BotContext)
suspend fun UserHandle.fetchDirectMessageChannel(): Channel.DirectMessage = ctx.userPort.fetchDirectMessageChannel(id)

/**
 * 指定ユーザーとの DM メッセージを取得します。
 *
 * @param limit 取得件数上限。`null` の場合はサーバーデフォルト
 * @param offset 取得開始位置
 * @param since 指定時刻以降のメッセージに絞り込みます
 * @param until 指定時刻以前のメッセージに絞り込みます
 * @param inclusive `true` の場合は境界時刻と一致するメッセージを含めます
 * @param order 並び順
 * @return DM メッセージ一覧
 */
context(ctx: BotContext)
suspend fun UserHandle.fetchDirectMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> =
    ctx.messagePort.fetchDirectMessages(
        id,
        limit,
        offset,
        since,
        until,
        inclusive,
        order,
    )

/**
 * 指定ユーザーへダイレクトメッセージを送信します。
 *
 * @param content 送信本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 * @return 送信されたメッセージ
 */
context(ctx: BotContext)
suspend fun UserHandle.sendDirectMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = ctx.messagePort.sendDirectMessage(id, content, embed, nonce)

/**
 * 指定ユーザーへダイレクトメッセージを送信します。
 *
 * @param content 送信本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 * @return 送信されたメッセージ
 */
context(ctx: BotContext)
suspend fun User.sendDirectMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = handle.sendDirectMessage(content, embed, nonce)

// --- Tags ---

/**
 * ユーザータグ一覧を取得します。
 *
 * @return ユーザータグ一覧
 */
context(ctx: BotContext)
suspend fun UserHandle.fetchTags(): List<UserTag> = ctx.userPort.fetchUserTags(id)

/**
 * ユーザーへタグを追加します。
 *
 * @param tag 追加するタグ文字列
 * @return 追加後のタグ情報
 */
context(ctx: BotContext)
suspend fun UserHandle.tag(tag: String): UserTag = ctx.userPort.addUserTag(id, tag)

/**
 * ユーザーからタグを削除します。
 *
 * @param tagId 削除するタグID
 */
context(ctx: BotContext)
suspend fun UserHandle.untag(tagId: UserTagId) = ctx.userPort.removeUserTag(id, tagId)

// --- Stats ---

/**
 * ユーザー統計情報を取得します。
 *
 * @return ユーザー統計情報
 */
context(ctx: BotContext)
suspend fun UserHandle.fetchStats(): UserStats = ctx.userPort.fetchUserStats(id)

// --- User convenience extensions ---

/**
 * ユーザーアイコンをダウンロードします。
 *
 * @return アイコン画像のバイト列
 */
context(ctx: BotContext)
suspend fun User.downloadIcon(): ByteArray = handle.downloadIcon()

/**
 * 指定ユーザーとの DM チャンネル情報を取得します。
 *
 * @return DM チャンネル
 */
context(ctx: BotContext)
suspend fun User.fetchDirectMessageChannel(): Channel.DirectMessage = handle.fetchDirectMessageChannel()

/**
 * 指定ユーザーとの DM メッセージを取得します。
 *
 * @param limit 取得件数上限。`null` の場合はサーバーデフォルト
 * @param offset 取得開始位置
 * @param since 指定時刻以降のメッセージに絞り込みます
 * @param until 指定時刻以前のメッセージに絞り込みます
 * @param inclusive `true` の場合は境界時刻と一致するメッセージを含めます
 * @param order 並び順
 * @return DM メッセージ一覧
 */
context(ctx: BotContext)
suspend fun User.fetchDirectMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> = handle.fetchDirectMessages(limit, offset, since, until, inclusive, order)

/**
 * ユーザータグ一覧を取得します。
 *
 * @return ユーザータグ一覧
 */
context(ctx: BotContext)
suspend fun User.fetchTags(): List<UserTag> = handle.fetchTags()

/**
 * ユーザーへタグを追加します。
 *
 * @param tag 追加するタグ文字列
 * @return 追加後のタグ情報
 */
context(ctx: BotContext)
suspend fun User.tag(tag: String): UserTag = handle.tag(tag)

/**
 * ユーザーからタグを削除します。
 *
 * @param tagId 削除するタグID
 */
context(ctx: BotContext)
suspend fun User.untag(tagId: UserTagId) = handle.untag(tagId)

/**
 * ユーザー統計情報を取得します。
 *
 * @return ユーザー統計情報
 */
context(ctx: BotContext)
suspend fun User.fetchStats(): UserStats = handle.fetchStats()
