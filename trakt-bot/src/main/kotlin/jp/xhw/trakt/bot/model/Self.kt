package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** 現在のユーザーが持つ traQ 権限。 */
@JvmInline
value class UserPermission(
    val value: String,
)

/** OAuth2 スコープ。 */
@JvmInline
value class OAuth2Scope(
    val value: String,
)

/** ログインセッションID。 */
@JvmInline
value class LoginSessionId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): LoginSessionId = parse(value)

        fun parse(value: String): LoginSessionId = LoginSessionId(Uuid.parse(value))
    }
}

/** OAuth2 トークンID。 */
@JvmInline
value class OAuth2TokenId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): OAuth2TokenId = parse(value)

        fun parse(value: String): OAuth2TokenId = OAuth2TokenId(Uuid.parse(value))
    }
}

/** 自分自身の詳細情報。 */
class CurrentUser internal constructor(
    override val id: UserId,
    override val name: String,
    override val displayName: String,
    override val iconFileId: FileId,
    override val isBot: Boolean,
    override val state: UserState,
    override val updatedAt: Instant,
    val twitterId: String,
    val lastOnline: Instant?,
    val tags: List<UserTag>,
    val groupIds: List<GroupId>,
    val bio: String,
    val homeChannelId: ChannelId?,
    val permissions: List<UserPermission>,
) : User.StatefulUser {
    val groups: List<GroupId>
        get() = groupIds

    val homeChannel: ChannelId?
        get() = homeChannelId

    val hasAdministrativeAccess: Boolean
        get() =
            permissions.any {
                it.value.startsWith("edit_") ||
                    it.value.startsWith("delete_") ||
                    it.value.startsWith("manage_") ||
                    it.value.startsWith("access_others_")
            }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

/** チャンネル購読レベル。 */
enum class ChannelSubscriptionLevel {
    NONE,
    SUBSCRIBED,
    NOTIFIED,
}

/** 自分のチャンネル購読状態。 */
@ConsistentCopyVisibility
data class ChannelSubscription internal constructor(
    val channelId: ChannelId,
    val level: ChannelSubscriptionLevel,
) {
    val channel: ChannelId
        get() = channelId
}

/** 自分の未読チャンネル情報。 */
@ConsistentCopyVisibility
data class UnreadChannel internal constructor(
    val channelId: ChannelId,
    val count: Int,
    val noticeable: Boolean,
    val since: Instant,
    val updatedAt: Instant,
    val oldestMessageId: MessageId,
) {
    val channel: ChannelId
        get() = channelId

    val oldestMessage: MessageId
        get() = oldestMessageId
}

/** 自分の WebSocket セッションごとの閲覧状態。 */
@ConsistentCopyVisibility
data class MyChannelViewState internal constructor(
    val key: String,
    val channelId: ChannelId,
    val state: ChannelViewState,
) {
    val channel: ChannelId
        get() = channelId
}

/** ユーザー設定。 */
@ConsistentCopyVisibility
data class UserSettings internal constructor(
    val userId: UserId,
    val notifyCitation: Boolean,
) {
    val user: UserId
        get() = userId
}

/** ログインセッション情報。 */
@ConsistentCopyVisibility
data class LoginSession internal constructor(
    val id: LoginSessionId,
    val issuedAt: Instant,
)

/** 有効な OAuth2 トークン情報。 */
@ConsistentCopyVisibility
data class ActiveOAuth2Token internal constructor(
    val id: OAuth2TokenId,
    val clientId: String,
    val scopes: List<OAuth2Scope>,
    val issuedAt: Instant,
)

/** 外部ログインアカウント。 */
@ConsistentCopyVisibility
data class ExternalAccount internal constructor(
    val providerName: String,
    val linkedAt: String,
    val externalName: String,
)

/** スタンプ利用履歴。 */
@ConsistentCopyVisibility
data class StampHistoryEntry internal constructor(
    val stampId: StampId,
    val datetime: Instant,
) {
    val stamp: StampId
        get() = stampId
}

/** スタンプレコメンド。 */
@ConsistentCopyVisibility
data class StampRecommendation internal constructor(
    val stampId: StampId,
    val score: Double,
) {
    val stamp: StampId
        get() = stampId
}
