package jp.xhw.trakt.bot.scope.bot

import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupHandle
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember
import jp.xhw.trakt.bot.model.User
import jp.xhw.trakt.bot.model.UserHandle
import jp.xhw.trakt.bot.model.UserId

/**
 * グループ詳細を取得します。
 *
 * @param groupId 取得対象グループID
 * @return グループ詳細情報
 */
context(ctx: BotContext)
suspend fun fetchGroup(groupId: GroupId): Group = ctx.groupPort.fetchGroup(groupId)

/**
 * このハンドルが指すグループ詳細を取得します。
 *
 * @return グループ詳細情報
 */
context(ctx: BotContext)
suspend fun GroupHandle.resolve(): Group = ctx.groupPort.fetchGroup(id)

/**
 * グループ一覧を取得します。
 *
 * @return グループ一覧
 */
context(ctx: BotContext)
suspend fun fetchGroups(): List<Group> = ctx.groupPort.fetchGroups()

/**
 * グループメンバー一覧を取得します。
 *
 * @return グループメンバー一覧
 */
context(ctx: BotContext)
suspend fun GroupHandle.fetchMembers(): List<GroupMember> = ctx.groupPort.fetchMembers(id)

/**
 * グループへメンバーを追加します。
 *
 * @param userId 追加するユーザーID
 * @param role 追加時に設定するロール
 */
context(ctx: BotContext)
suspend fun GroupHandle.addMember(
    userId: UserId,
    role: String = "",
) {
    ctx.groupPort.addMember(id, userId, role)
}

/**
 * グループへメンバーを追加します。
 *
 * @param user 追加するユーザー
 * @param role 追加時に設定するロール
 */
context(ctx: BotContext)
suspend fun GroupHandle.addMember(
    user: UserHandle,
    role: String = "",
) = addMember(user.id, role)

/**
 * グループへメンバーを追加します。
 *
 * @param user 追加するユーザー
 * @param role 追加時に設定するロール
 */
context(ctx: BotContext)
suspend fun GroupHandle.addMember(
    user: User,
    role: String = "",
) = addMember(user.id, role)

/**
 * グループへ管理者を追加します。
 *
 * @param userId 追加するユーザーID
 */
context(ctx: BotContext)
suspend fun GroupHandle.addAdmin(userId: UserId) {
    ctx.groupPort.addAdmin(id, userId)
}

/**
 * グループへ管理者を追加します。
 *
 * @param user 追加するユーザー
 */
context(ctx: BotContext)
suspend fun GroupHandle.addAdmin(user: UserHandle) = addAdmin(user.id)

/**
 * グループへ管理者を追加します。
 *
 * @param user 追加するユーザー
 */
context(ctx: BotContext)
suspend fun GroupHandle.addAdmin(user: User) = addAdmin(user.id)

/**
 * グループ情報を再取得します。
 *
 * @return 最新のグループ情報
 */
context(ctx: BotContext)
suspend fun Group.refresh(): Group = handle.resolve()

/**
 * グループへメンバーを追加します。
 *
 * @param userId 追加するユーザーID
 * @param role 追加時に設定するロール
 */
context(ctx: BotContext)
suspend fun Group.addMember(
    userId: UserId,
    role: String = "",
) = handle.addMember(userId, role)

/**
 * グループへメンバーを追加します。
 *
 * @param user 追加するユーザー
 * @param role 追加時に設定するロール
 */
context(ctx: BotContext)
suspend fun Group.addMember(
    user: UserHandle,
    role: String = "",
) = handle.addMember(user, role)

/**
 * グループへメンバーを追加します。
 *
 * @param user 追加するユーザー
 * @param role 追加時に設定するロール
 */
context(ctx: BotContext)
suspend fun Group.addMember(
    user: User,
    role: String = "",
) = handle.addMember(user, role)

/**
 * グループへ管理者を追加します。
 *
 * @param userId 追加するユーザーID
 */
context(ctx: BotContext)
suspend fun Group.addAdmin(userId: UserId) = handle.addAdmin(userId)

/**
 * グループへ管理者を追加します。
 *
 * @param user 追加するユーザー
 */
context(ctx: BotContext)
suspend fun Group.addAdmin(user: UserHandle) = handle.addAdmin(user)

/**
 * グループへ管理者を追加します。
 *
 * @param user 追加するユーザー
 */
context(ctx: BotContext)
suspend fun Group.addAdmin(user: User) = handle.addAdmin(user)
