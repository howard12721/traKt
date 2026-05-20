package jp.xhw.trakt.bot.context.base

import jp.xhw.trakt.bot.model.Group
import jp.xhw.trakt.bot.model.GroupId
import jp.xhw.trakt.bot.model.GroupMember
import jp.xhw.trakt.bot.model.UserId

// --- Create ---

/**
 * ユーザーグループを作成します。
 *
 * @param name グループ名
 * @param description グループ説明
 * @param type グループタイプ
 * @return 作成されたグループ詳細
 */
context(ctx: BaseContext)
suspend fun createGroup(
    name: String,
    description: String,
    type: String,
): Group.Detail = ctx.groupPort.createGroup(name, description, type)

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

/**
 * グループ情報を更新します。
 *
 * @param name 新しいグループ名。`null` の場合は変更しません
 * @param description 新しい説明。`null` の場合は変更しません
 */
context(ctx: BaseContext)
suspend fun Group.update(
    name: String? = null,
    description: String? = null,
) {
    ctx.groupPort.editGroup(id, name, description)
}

/** グループを削除します。 */
context(ctx: BaseContext)
suspend fun Group.delete() {
    ctx.groupPort.deleteGroup(id)
}

/**
 * グループのアイコンを変更します。
 *
 * @param file アイコン画像のバイト列
 * @param fileName ファイル名
 * @param contentType MIME タイプ。`null` の場合はサーバー側判定
 */
context(ctx: BaseContext)
suspend fun Group.changeIcon(
    file: ByteArray,
    fileName: String,
    contentType: String? = null,
) {
    ctx.groupPort.changeGroupIcon(id, file, fileName, contentType)
}

/**
 * グループからメンバーを削除します。
 *
 * @param userId 削除するユーザーID
 */
context(ctx: BaseContext)
suspend fun Group.removeMember(userId: UserId) {
    ctx.groupPort.removeMember(id, userId)
}

/**
 * グループメンバーのロールを編集します。
 *
 * @param userId 編集するユーザーID
 * @param role 新しいロール
 */
context(ctx: BaseContext)
suspend fun Group.editMember(
    userId: UserId,
    role: String,
) {
    ctx.groupPort.editMember(id, userId, role)
}

/** グループから全てのメンバーを削除します。 */
context(ctx: BaseContext)
suspend fun Group.removeAllMembers() {
    ctx.groupPort.removeAllMembers(id)
}
