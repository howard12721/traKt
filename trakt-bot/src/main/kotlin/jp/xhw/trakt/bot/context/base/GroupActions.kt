package jp.xhw.trakt.bot.context.base

import jp.xhw.trakt.bot.model.*

/**
 * グループ詳細を取得します。
 *
 * @param groupId 取得対象グループID
 * @return グループ詳細情報
 */
context(ctx: BaseContext)
suspend fun fetchGroup(groupId: GroupId): Group = ctx.groupPort.fetchGroup(groupId)

/**
 * この ID が指すグループ詳細を取得します。
 *
 * @return グループ詳細情報
 */
context(ctx: BaseContext)
suspend fun GroupId.fetch(): Group = ctx.groupPort.fetchGroup(this)

/**
 * グループ一覧を取得します。
 *
 * @return グループ一覧
 */
context(ctx: BaseContext)
suspend fun fetchGroups(): List<Group> = ctx.groupPort.fetchGroups()

/**
 * グループメンバー一覧を取得します。
 *
 * @return グループメンバー一覧
 */
context(ctx: BaseContext)
suspend fun GroupId.fetchMembers(): List<GroupMember> = ctx.groupPort.fetchMembers(this)

/**
 * グループへメンバーを追加します。
 *
 * @param userId 追加するユーザーID
 * @param role 追加時に設定するロール
 */
context(ctx: BaseContext)
suspend fun GroupId.addMember(
    userId: UserId,
    role: String = "",
) {
    ctx.groupPort.addMember(this, userId, role)
}

/**
 * グループへメンバーを追加します。
 *
 * @param user 追加するユーザー
 * @param role 追加時に設定するロール
 */
context(ctx: BaseContext)
suspend fun GroupId.addMember(
    user: User,
    role: String = "",
) {
    addMember(user.id, role)
}

/**
 * グループへ管理者を追加します。
 *
 * @param userId 追加するユーザーID
 */
context(ctx: BaseContext)
suspend fun GroupId.addAdmin(userId: UserId) {
    ctx.groupPort.addAdmin(this, userId)
}

/**
 * グループへ管理者を追加します。
 *
 * @param user 追加するユーザー
 */
context(ctx: BaseContext)
suspend fun GroupId.addAdmin(user: User) {
    addAdmin(user.id)
}

/**
 * グループ情報を取得します。
 *
 * @return 最新のグループ情報
 */
context(ctx: BaseContext)
suspend fun Group.fetch(): Group = id.fetch()

/**
 * グループメンバー一覧を取得します。
 *
 * @return グループメンバー一覧
 */
context(ctx: BaseContext)
suspend fun Group.fetchMembers(): List<GroupMember> = id.fetchMembers()

/**
 * グループへメンバーを追加します。
 *
 * @param userId 追加するユーザーID
 * @param role 追加時に設定するロール
 */
context(ctx: BaseContext)
suspend fun Group.addMember(
    userId: UserId,
    role: String = "",
) {
    id.addMember(userId, role)
}

/**
 * グループへメンバーを追加します。
 *
 * @param user 追加するユーザー
 * @param role 追加時に設定するロール
 */
context(ctx: BaseContext)
suspend fun Group.addMember(
    user: User,
    role: String = "",
) {
    id.addMember(user, role)
}

/**
 * グループへ管理者を追加します。
 *
 * @param userId 追加するユーザーID
 */
context(ctx: BaseContext)
suspend fun Group.addAdmin(userId: UserId) {
    id.addAdmin(userId)
}

/**
 * グループへ管理者を追加します。
 *
 * @param user 追加するユーザー
 */
context(ctx: BaseContext)
suspend fun Group.addAdmin(user: User) {
    id.addAdmin(user)
}
