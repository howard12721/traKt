package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.*

internal interface SelfPort {
    suspend fun fetchMe(): CurrentUser

    suspend fun editMe(
        displayName: String? = null,
        twitterId: String? = null,
        bio: String? = null,
        homeChannelId: ChannelId? = null,
    )

    suspend fun downloadMyIcon(): ByteArray

    suspend fun changeMyIcon(
        file: ByteArray,
        fileName: String,
        contentType: String? = null,
    )

    suspend fun changeMyPassword(
        currentPassword: String,
        newPassword: String,
    )

    suspend fun fetchMySettings(): UserSettings

    suspend fun isNotifyCitationEnabled(): Boolean

    suspend fun setNotifyCitation(enabled: Boolean)

    suspend fun fetchMyTags(): List<UserTag>

    suspend fun addMyTag(tag: String): UserTag

    suspend fun editMyTag(
        tagId: UserTagId,
        isLocked: Boolean,
    )

    suspend fun removeMyTag(tagId: UserTagId)

    suspend fun fetchMyStars(): List<ChannelId>

    suspend fun addMyStar(channelId: ChannelId)

    suspend fun removeMyStar(channelId: ChannelId)

    suspend fun fetchMySubscriptions(): List<ChannelSubscription>

    suspend fun setMySubscription(
        channelId: ChannelId,
        level: ChannelSubscriptionLevel,
    )

    suspend fun fetchMyUnreadChannels(): List<UnreadChannel>

    suspend fun readChannel(channelId: ChannelId)

    suspend fun fetchMyViewStates(): List<MyChannelViewState>

    suspend fun registerFCMDevice(token: String)

    suspend fun fetchMySessions(): List<LoginSession>

    suspend fun revokeMySession(sessionId: LoginSessionId)

    suspend fun fetchMyTokens(): List<ActiveOAuth2Token>

    suspend fun revokeMyToken(tokenId: OAuth2TokenId)

    suspend fun fetchMyExternalAccounts(): List<ExternalAccount>

    suspend fun linkExternalAccount(providerName: String)

    suspend fun unlinkExternalAccount(providerName: String)

    suspend fun fetchMyStampHistory(limit: Int = 100): List<StampHistoryEntry>

    suspend fun fetchMyStampRecommendations(limit: Int = 100): List<StampRecommendation>

    suspend fun fetchMyQRCode(): ByteArray
}
