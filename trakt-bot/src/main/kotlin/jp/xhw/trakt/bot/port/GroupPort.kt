package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember
import jp.xhw.trakt.bot.model.UserId

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

    /**
     * グループへメンバーを追加します。
     *
     * @param groupId 追加対象グループ
     * @param userId 追加するユーザー
     * @param role 追加時に設定するロール
     */
    suspend fun addMember(
        groupId: GroupId,
        userId: UserId,
        role: String,
    )

    /**
     * グループへ管理者を追加します。
     *
     * @param groupId 追加対象グループ
     * @param userId 追加するユーザー
     */
    suspend fun addAdmin(
        groupId: GroupId,
        userId: UserId,
    )
}
