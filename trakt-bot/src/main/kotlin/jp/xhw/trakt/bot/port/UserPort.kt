package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.*

internal interface UserPort {
    /**
     * ユーザー詳細を取得します。
     *
     * @param userId 取得対象ユーザー
     * @return ユーザー詳細
     */
    suspend fun fetchUser(userId: UserId): User.Detail

    /**
     * ユーザー一覧を取得します。
     *
     * @param includeSuspended 非アクティブユーザーを含めるかどうか
     * @param name 指定時は名前一致ユーザーのみ取得
     * @return ユーザー基本情報一覧
     */
    suspend fun fetchUsers(
        includeSuspended: Boolean = false,
        name: String? = null,
    ): List<User.Basic>

    /**
     * ユーザーアイコンを取得します。
     *
     * @param userId 取得対象ユーザー
     * @return アイコン画像のバイト列
     */
    suspend fun fetchUserIcon(userId: UserId): ByteArray

    /**
     * 指定ユーザーとの DM チャンネルを取得します。
     *
     * @param userId 対象ユーザー
     * @return DM チャンネル
     */
    suspend fun fetchDirectMessageChannel(userId: UserId): Channel.DirectMessage

    /**
     * ユーザー統計情報を取得します。
     *
     * @param userId 取得対象ユーザー
     * @return ユーザー統計
     */
    suspend fun fetchUserStats(userId: UserId): UserStats

    /**
     * ユーザータグ一覧を取得します。
     *
     * @param userId 取得対象ユーザー
     * @return ユーザータグ一覧
     */
    suspend fun fetchUserTags(userId: UserId): List<UserTag>

    /**
     * ユーザーへタグを追加します。
     *
     * @param userId 追加対象ユーザー
     * @param tag 追加するタグ
     * @return 追加後のタグ情報
     */
    suspend fun addUserTag(
        userId: UserId,
        tag: String,
    ): UserTag

    /**
     * ユーザーからタグを削除します。
     *
     * @param userId 削除対象ユーザー
     * @param tagId 削除するタグID
     */
    suspend fun removeUserTag(
        userId: UserId,
        tagId: UserTagId,
    )
}
