package jp.xhw.trakt.bot.context.base

import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember
import jp.xhw.trakt.bot.model.UserId

/**
 * グループ詳細を取得します。
 *
 * @param groupId 取得対象グループID
 * @return グループ詳細情報
 */
context(ctx: BaseContext)
suspend fun fetchGroup(groupId: GroupId): Group.Detail = ctx.groupPort.fetchGroup(groupId)

/**
 * グループ詳細を取得します。存在しない場合は `null` を返します。
 *
 * @param groupId 取得対象グループID
 * @return グループ詳細情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun fetchGroupOrNull(groupId: GroupId): Group.Detail? = ctx.groupPort.fetchGroupOrNull(groupId)

/**
 * グループ情報を取得します。
 *
 * @return 最新のグループ情報
 */
context(ctx: BaseContext)
suspend fun Group.fetch(): Group.Detail = ctx.groupPort.fetchGroup(id)

/**
 * グループ一覧を取得します。
 *
 * @return グループ一覧
 */
context(ctx: BaseContext)
suspend fun fetchGroups(): List<Group.Detail> = ctx.groupPort.fetchGroups()

/**
 * グループメンバー一覧を取得します。
 *
 * @return グループメンバー一覧
 */
context(ctx: BaseContext)
suspend fun Group.fetchMembers(): List<GroupMember> = ctx.groupPort.fetchMembers(id)

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
    ctx.groupPort.addMember(id, userId, role)
}

/**
 * グループへ管理者を追加します。
 *
 * @param userId 追加するユーザーID
 */
context(ctx: BaseContext)
suspend fun Group.addAdmin(userId: UserId) {
    ctx.groupPort.addAdmin(id, userId)
}
