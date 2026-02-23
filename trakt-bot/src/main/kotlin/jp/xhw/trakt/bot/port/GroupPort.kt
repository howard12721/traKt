package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember

internal interface GroupPort {
    /**
     * グループ詳細を取得します。
     *
     * @param groupId 取得対象グループ
     * @return グループ詳細
     */
    suspend fun fetchGroup(groupId: GroupId): Group

    /**
     * グループ一覧を取得します。
     *
     * @return グループ一覧
     */
    suspend fun fetchGroups(): List<Group>

    /**
     * グループメンバー一覧を取得します。
     *
     * @param groupId 取得対象グループ
     * @return メンバー一覧
     */
    suspend fun fetchMembers(groupId: GroupId): List<GroupMember>
}
