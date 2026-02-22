package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupHandle
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember

context(scope: BotScope)
suspend fun fetchGroup(groupId: GroupId): Group = scope.context.groupPort.fetchGroup(groupId)

context(scope: BotScope)
suspend fun GroupHandle.resolve(): Group = scope.context.groupPort.fetchGroup(id)

context(scope: BotScope)
suspend fun fetchGroups(): List<Group> = scope.context.groupPort.fetchGroups()

context(scope: BotScope)
suspend fun GroupHandle.fetchMembers(): List<GroupMember> = scope.context.groupPort.fetchMembers(id)

context(scope: BotScope)
suspend fun Group.refresh(): Group = handle.resolve()
