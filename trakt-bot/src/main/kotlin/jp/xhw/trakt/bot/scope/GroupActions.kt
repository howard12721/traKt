package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupHandle
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember

/**
 * グループ詳細を取得します。
 *
 * @param groupId 取得対象グループID
 * @return グループ詳細情報
 */
context(scope: BotScope)
suspend fun fetchGroup(groupId: GroupId): Group = scope.context.groupPort.fetchGroup(groupId)

/**
 * このハンドルが指すグループ詳細を取得します。
 *
 * @return グループ詳細情報
 */
context(scope: BotScope)
suspend fun GroupHandle.resolve(): Group = scope.context.groupPort.fetchGroup(id)

/**
 * グループ一覧を取得します。
 *
 * @return グループ一覧
 */
context(scope: BotScope)
suspend fun fetchGroups(): List<Group> = scope.context.groupPort.fetchGroups()

/**
 * グループメンバー一覧を取得します。
 *
 * @return グループメンバー一覧
 */
context(scope: BotScope)
suspend fun GroupHandle.fetchMembers(): List<GroupMember> = scope.context.groupPort.fetchMembers(id)

/**
 * グループ情報を再取得します。
 *
 * @return 最新のグループ情報
 */
context(scope: BotScope)
suspend fun Group.refresh(): Group = handle.resolve()
