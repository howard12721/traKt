package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.*
import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.bot.port.SelfPort
import jp.xhw.trakt.rest.models.PatchMeRequest
import jp.xhw.trakt.rest.models.PatchUserTagRequest
import jp.xhw.trakt.rest.models.PostLinkExternalAccount
import jp.xhw.trakt.rest.models.PostMyFCMDeviceRequest
import jp.xhw.trakt.rest.models.PostStarRequest
import jp.xhw.trakt.rest.models.PostUnlinkExternalAccount
import jp.xhw.trakt.rest.models.PostUserTagRequest
import jp.xhw.trakt.rest.models.PutChannelSubscribeLevelRequest
import jp.xhw.trakt.rest.models.PutMyPasswordRequest
import jp.xhw.trakt.rest.models.PutNotifyCitationRequest

internal class TraqSelfPort(
    private val apiGateway: TraqApiGateway,
) : SelfPort {
    override suspend fun fetchMe(): CurrentUser =
        apiGateway.meApi
            .getMe()
            .bodyOrThrow(operation = "fetchMe()")
            .toCurrentUser()

    override suspend fun editMe(
        displayName: String?,
        twitterId: String?,
        bio: String?,
        homeChannelId: ChannelId?,
    ) {
        apiGateway.meApi
            .editMe(
                PatchMeRequest(
                    displayName = displayName,
                    twitterId = twitterId,
                    bio = bio,
                    homeChannel = homeChannelId?.value,
                ),
            ).requireSuccess(operation = "editMe()")
    }

    override suspend fun downloadMyIcon(): ByteArray =
        apiGateway.meApi
            .getMyIcon()
            .bodyOrThrow(operation = "downloadMyIcon()")
            .value

    override suspend fun changeMyIcon(
        file: ByteArray,
        fileName: String,
        contentType: String?,
    ) {
        apiGateway.meApi
            .changeMyIcon(file.toFilePart(fileName, contentType))
            .requireSuccess(operation = "changeMyIcon(fileName=$fileName)")
    }

    override suspend fun changeMyPassword(
        currentPassword: String,
        newPassword: String,
    ) {
        apiGateway.meApi
            .changeMyPassword(
                PutMyPasswordRequest(
                    password = currentPassword,
                    newPassword = newPassword,
                ),
            ).requireSuccess(operation = "changeMyPassword()")
    }

    override suspend fun fetchMySettings(): UserSettings =
        apiGateway.meApi
            .getUserSettings()
            .bodyOrThrow(operation = "fetchMySettings()")
            .toModel()

    override suspend fun isNotifyCitationEnabled(): Boolean =
        apiGateway.meApi
            .getMyNotifyCitation()
            .bodyOrThrow(operation = "isNotifyCitationEnabled()")
            .notifyCitation

    override suspend fun setNotifyCitation(enabled: Boolean) {
        apiGateway.meApi
            .changeMyNotifyCitation(PutNotifyCitationRequest(enabled))
            .requireSuccess(operation = "setNotifyCitation(enabled=$enabled)")
    }

    override suspend fun fetchMyTags(): List<UserTag.Detail> =
        apiGateway.meApi
            .getMyUserTags()
            .bodyOrThrow(operation = "fetchMyTags()")
            .map { it.toModel() }

    override suspend fun addMyTag(tag: String): UserTag.Detail =
        apiGateway.meApi
            .addMyUserTag(PostUserTagRequest(tag))
            .bodyOrThrow(operation = "addMyTag(tag=$tag)")
            .toModel()

    override suspend fun editMyTag(
        tagId: UserTagId,
        isLocked: Boolean,
    ) {
        apiGateway.meApi
            .editMyUserTag(tagId.value, PatchUserTagRequest(isLocked))
            .requireSuccess(operation = "editMyTag(tagId=${tagId.value})")
    }

    override suspend fun removeMyTag(tagId: UserTagId) {
        apiGateway.meApi
            .removeMyUserTag(tagId.value)
            .requireSuccess(operation = "removeMyTag(tagId=${tagId.value})")
    }

    override suspend fun fetchMyStars(): List<Channel.Ref> =
        apiGateway.meApi
            .getMyStars()
            .bodyOrThrow(operation = "fetchMyStars()")
            .map { Channel.Ref(ChannelId(it)) }

    override suspend fun addMyStar(channelId: ChannelId) {
        apiGateway.meApi
            .addMyStar(PostStarRequest(channelId.value))
            .requireSuccess(operation = "addMyStar(channelId=${channelId.value})")
    }

    override suspend fun removeMyStar(channelId: ChannelId) {
        apiGateway.meApi
            .removeMyStar(channelId.value)
            .requireSuccess(operation = "removeMyStar(channelId=${channelId.value})")
    }

    override suspend fun fetchMySubscriptions(): List<ChannelSubscription> =
        apiGateway.meApi
            .getMyChannelSubscriptions()
            .bodyOrThrow(operation = "fetchMySubscriptions()")
            .map { it.toModel() }

    override suspend fun setMySubscription(
        channelId: ChannelId,
        level: ChannelSubscriptionLevel,
    ) {
        apiGateway.meApi
            .setChannelSubscribeLevel(
                channelId.value,
                PutChannelSubscribeLevelRequest(level.toApiModel()),
            ).requireSuccess(operation = "setMySubscription(channelId=${channelId.value}, level=$level)")
    }

    override suspend fun fetchMyUnreadChannels(): List<UnreadChannel> =
        apiGateway.meApi
            .getMyUnreadChannels()
            .bodyOrThrow(operation = "fetchMyUnreadChannels()")
            .map { it.toModel() }

    override suspend fun readChannel(channelId: ChannelId) {
        apiGateway.meApi
            .readChannel(channelId.value)
            .requireSuccess(operation = "readChannel(channelId=${channelId.value})")
    }

    override suspend fun fetchMyViewStates(): List<MyChannelViewState> =
        apiGateway.meApi
            .getMyViewStates()
            .bodyOrThrow(operation = "fetchMyViewStates()")
            .map { it.toModel() }

    override suspend fun registerFCMDevice(token: String) {
        apiGateway.meApi
            .registerFCMDevice(PostMyFCMDeviceRequest(token))
            .requireSuccess(operation = "registerFCMDevice()")
    }

    override suspend fun fetchMySessions(): List<LoginSession.Detail> =
        apiGateway.meApi
            .getMySessions()
            .bodyOrThrow(operation = "fetchMySessions()")
            .map { it.toModel() }

    override suspend fun revokeMySession(sessionId: LoginSessionId) {
        apiGateway.meApi
            .revokeMySession(sessionId.value)
            .requireSuccess(operation = "revokeMySession(sessionId=${sessionId.value})")
    }

    override suspend fun fetchMyTokens(): List<ActiveOAuth2Token.Detail> =
        apiGateway.meApi
            .getMyTokens()
            .bodyOrThrow(operation = "fetchMyTokens()")
            .map { it.toModel() }

    override suspend fun revokeMyToken(tokenId: OAuth2TokenId) {
        apiGateway.meApi
            .revokeMyToken(tokenId.value)
            .requireSuccess(operation = "revokeMyToken(tokenId=${tokenId.value})")
    }

    override suspend fun fetchMyExternalAccounts(): List<ExternalAccount> =
        apiGateway.meApi
            .getMyExternalAccounts()
            .bodyOrThrow(operation = "fetchMyExternalAccounts()")
            .map { it.toModel() }

    override suspend fun linkExternalAccount(providerName: String) {
        apiGateway.meApi
            .linkExternalAccount(PostLinkExternalAccount(providerName))
            .requireSuccess(operation = "linkExternalAccount(providerName=$providerName)")
    }

    override suspend fun unlinkExternalAccount(providerName: String) {
        apiGateway.meApi
            .unlinkExternalAccount(PostUnlinkExternalAccount(providerName))
            .requireSuccess(operation = "unlinkExternalAccount(providerName=$providerName)")
    }

    override suspend fun fetchMyStampHistory(limit: Int): List<StampHistoryEntry> =
        apiGateway.meApi
            .getMyStampHistory(limit)
            .bodyOrThrow(operation = "fetchMyStampHistory(limit=$limit)")
            .map { it.toModel() }

    override suspend fun fetchMyStampRecommendations(limit: Int): List<StampRecommendation> =
        apiGateway.meApi
            .getMyStampRecommendations(limit)
            .bodyOrThrow(operation = "fetchMyStampRecommendations(limit=$limit)")
            .map { it.toModel() }

    override suspend fun fetchMyQRCode(): ByteArray =
        apiGateway.meApi
            .getMyQRCode(token = false)
            .bodyOrThrow(operation = "fetchMyQRCode()")
            .value
}
