package jp.xhw.trakt.bot.context.base

import jp.xhw.trakt.bot.model.*
import kotlin.time.Instant

// --- Fetch ---

/**
 * ユーザー詳細を取得します。
 *
 * @param userId 取得対象ユーザーID
 * @return ユーザー詳細情報
 */
context(ctx: BaseContext)
suspend fun fetchUser(userId: UserId): User.Detail = ctx.userPort.fetchUser(userId)

/**
 * ユーザー詳細を取得します。存在しない場合は `null` を返します。
 *
 * @param userId 取得対象ユーザーID
 * @return ユーザー詳細情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun fetchUserOrNull(userId: UserId): User.Detail? = ctx.userPort.fetchUserOrNull(userId)

/**
 * ユーザー詳細情報を取得します。
 *
 * @return ユーザー情報
 */
context(ctx: BaseContext)
suspend fun User.fetch(): User.Detail = ctx.userPort.fetchUser(id)

/**
 * ユーザー一覧を取得します。
 *
 * @param includeSuspended `true` の場合は非アクティブユーザーも含めます
 * @param name 指定時は名前一致ユーザーのみ取得します
 * @return ユーザー基本情報の一覧
 */
context(ctx: BaseContext)
suspend fun fetchUsers(
    includeSuspended: Boolean = false,
    name: String? = null,
): List<User.Basic> = ctx.userPort.fetchUsers(includeSuspended, name)

/**
 * ユーザーアイコンをダウンロードします。
 *
 * @return アイコン画像のバイト列
 */
context(ctx: BaseContext)
suspend fun User.downloadIcon(): ByteArray = ctx.userPort.fetchUserIcon(id)

/**
 * 指定ユーザーとの DM チャンネル情報を取得します。
 *
 * @return DM チャンネル
 */
context(ctx: BaseContext)
suspend fun User.fetchDirectMessageChannel(): Channel.DirectMessage = ctx.userPort.fetchDirectMessageChannel(id)

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
context(ctx: BaseContext)
suspend fun User.fetchDirectMessages(
    limit: Int? = null,
    offset: Int = 0,
    since: Instant? = null,
    until: Instant? = null,
    inclusive: Boolean = false,
    order: SortDirection = SortDirection.DESCENDING,
): List<Message.Detail> = ctx.messagePort.fetchDirectMessages(id, limit, offset, since, until, inclusive, order)

/**
 * 指定ユーザーへダイレクトメッセージを送信します。
 *
 * @param content 送信本文
 * @param embed `true` の場合に埋め込み展開を有効化します
 * @param nonce 重複送信防止に使う任意文字列
 * @return 送信されたメッセージ
 */
context(ctx: BaseContext)
suspend fun User.sendDirectMessage(
    content: String,
    embed: Boolean = false,
    nonce: String? = null,
): Message.Detail = ctx.messagePort.sendDirectMessage(id, content, embed, nonce)

// --- Tags ---

/**
 * ユーザータグ一覧を取得します。
 *
 * @return ユーザータグ一覧
 */
context(ctx: BaseContext)
suspend fun User.fetchTags(): List<UserTag.Detail> = ctx.userPort.fetchUserTags(id)

/**
 * ユーザーへタグを追加します。
 *
 * @param tag 追加するタグ文字列
 * @return 追加後のタグ情報
 */
context(ctx: BaseContext)
suspend fun User.tag(tag: String): UserTag.Detail = ctx.userPort.addUserTag(id, tag)

/**
 * ユーザーからタグを削除します。
 *
 * @param tagId 削除するタグID
 */
context(ctx: BaseContext)
suspend fun User.untag(tagId: UserTagId) = ctx.userPort.removeUserTag(id, tagId)

// --- Stats ---

/**
 * ユーザー統計情報を取得します。
 *
 * @return ユーザー統計情報
 */
context(ctx: BaseContext)
suspend fun User.fetchStats(): UserStats = ctx.userPort.fetchUserStats(id)

/**
 * 名前からユーザーを取得します。
 *
 * @param name 取得するユーザーの名前
 * @return ユーザー情報。見つからない場合は `null`
 */
context(ctx: BaseContext)
suspend fun fetchUserByName(name: String): User.Detail? = fetchUsers(name = name).firstOrNull()?.fetch()
