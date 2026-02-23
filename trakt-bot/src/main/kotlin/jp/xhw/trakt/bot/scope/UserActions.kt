package jp.xhw.trakt.bot.scope

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
context(scope: BotScope)
suspend fun fetchUser(userId: UserId): User.Detail = scope.context.userPort.fetchUser(userId)

/**
 * ユーザー詳細を取得します。
 *
 * @param userId 取得対象ユーザーID(UUID)
 * @return ユーザー詳細情報
 */
context(scope: BotScope)
suspend fun fetchUser(userId: Uuid): User.Detail = fetchUser(UserId(userId))

/**
 * このハンドルが指すユーザーを取得します。
 *
 * @return ユーザー情報
 */
context(scope: BotScope)
suspend fun UserHandle.resolve(): User = scope.context.userPort.fetchUser(id)

/**
 * ユーザー情報を再取得します。
 *
 * @return ユーザー情報
 */
context(scope: BotScope)
suspend fun User.Minimal.resolve(): User = handle.resolve()

/**
 * ユーザー情報を再取得します。
 *
 * @return ユーザー情報
 */
context(scope: BotScope)
suspend fun User.Basic.resolve(): User = handle.resolve()

/**
 * ユーザー詳細情報を再取得します。
 *
 * @return ユーザー情報
 */
context(scope: BotScope)
suspend fun User.Detail.refresh(): User = handle.resolve()

/**
 * ユーザー一覧を取得します。
 *
 * @param includeSuspended `true` の場合は非アクティブユーザーも含めます
 * @param name 指定時は名前一致ユーザーのみ取得します
 * @return ユーザー基本情報の一覧
 */
context(scope: BotScope)
suspend fun fetchUsers(
    includeSuspended: Boolean = false,
    name: String? = null,
): List<User.Basic> = scope.context.userPort.fetchUsers(includeSuspended, name)

/**
 * ユーザーアイコンをダウンロードします。
 *
 * @return アイコン画像のバイト列
 */
context(scope: BotScope)
suspend fun UserHandle.downloadIcon(): ByteArray = scope.context.userPort.fetchUserIcon(id)

/**
 * 指定ユーザーとの DM チャンネル情報を取得します。
 *
 * @return DM チャンネル
 */
context(scope: BotScope)
suspend fun UserHandle.fetchDirectMessageChannel(): Channel.DirectMessage = scope.context.userPort.fetchDirectMessageChannel(id)

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
context(scope: BotScope)
suspend fun UserHandle.fetchDirectMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message> =
    scope.context.messagePort.fetchDirectMessages(
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
context(scope: BotScope)
suspend fun UserHandle.sendDirectMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message = scope.context.messagePort.sendDirectMessage(id, content, embed, nonce)

/**
 * 指定ユーザーへダイレクトメッセージを送信します。
 *
 * @param content 送信本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 * @return 送信されたメッセージ
 */
context(scope: BotScope)
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
context(scope: BotScope)
suspend fun UserHandle.fetchTags(): List<UserTag> = scope.context.userPort.fetchUserTags(id)

/**
 * ユーザーへタグを追加します。
 *
 * @param tag 追加するタグ文字列
 * @return 追加後のタグ情報
 */
context(scope: BotScope)
suspend fun UserHandle.tag(tag: String): UserTag = scope.context.userPort.addUserTag(id, tag)

/**
 * ユーザーからタグを削除します。
 *
 * @param tagId 削除するタグID
 */
context(scope: BotScope)
suspend fun UserHandle.untag(tagId: UserTagId) = scope.context.userPort.removeUserTag(id, tagId)

// --- Stats ---

/**
 * ユーザー統計情報を取得します。
 *
 * @return ユーザー統計情報
 */
context(scope: BotScope)
suspend fun UserHandle.fetchStats(): UserStats = scope.context.userPort.fetchUserStats(id)

// --- User convenience extensions ---

/**
 * ユーザーアイコンをダウンロードします。
 *
 * @return アイコン画像のバイト列
 */
context(scope: BotScope)
suspend fun User.downloadIcon(): ByteArray = handle.downloadIcon()

/**
 * 指定ユーザーとの DM チャンネル情報を取得します。
 *
 * @return DM チャンネル
 */
context(scope: BotScope)
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
context(scope: BotScope)
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
context(scope: BotScope)
suspend fun User.fetchTags(): List<UserTag> = handle.fetchTags()

/**
 * ユーザーへタグを追加します。
 *
 * @param tag 追加するタグ文字列
 * @return 追加後のタグ情報
 */
context(scope: BotScope)
suspend fun User.tag(tag: String): UserTag = handle.tag(tag)

/**
 * ユーザーからタグを削除します。
 *
 * @param tagId 削除するタグID
 */
context(scope: BotScope)
suspend fun User.untag(tagId: UserTagId) = handle.untag(tagId)

/**
 * ユーザー統計情報を取得します。
 *
 * @return ユーザー統計情報
 */
context(scope: BotScope)
suspend fun User.fetchStats(): UserStats = handle.fetchStats()
