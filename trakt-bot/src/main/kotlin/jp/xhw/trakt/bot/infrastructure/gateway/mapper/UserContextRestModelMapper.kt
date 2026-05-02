package jp.xhw.trakt.bot.infrastructure.gateway.mapper

import jp.xhw.trakt.bot.model.*
import jp.xhw.trakt.rest.models.ActiveOAuth2Token as RestActiveOAuth2Token
import jp.xhw.trakt.rest.models.Bot as RestBot
import jp.xhw.trakt.rest.models.BotDetail as RestBotDetail
import jp.xhw.trakt.rest.models.BotEventLog as RestBotEventLog
import jp.xhw.trakt.rest.models.BotEventResult as RestBotEventResult
import jp.xhw.trakt.rest.models.BotMode as RestBotMode
import jp.xhw.trakt.rest.models.BotState as RestBotState
import jp.xhw.trakt.rest.models.BotTokens as RestBotTokens
import jp.xhw.trakt.rest.models.ChannelSubscribeLevel as RestChannelSubscribeLevel
import jp.xhw.trakt.rest.models.ClipFolder as RestClipFolder
import jp.xhw.trakt.rest.models.ClippedMessage as RestClippedMessage
import jp.xhw.trakt.rest.models.ExternalProviderUser as RestExternalProviderUser
import jp.xhw.trakt.rest.models.GetBot200Response as RestGetBotResponse
import jp.xhw.trakt.rest.models.GetMyStampRecommendations200ResponseInner as RestStampRecommendation
import jp.xhw.trakt.rest.models.LoginSession as RestLoginSession
import jp.xhw.trakt.rest.models.MessageClip as RestMessageClip
import jp.xhw.trakt.rest.models.MyChannelViewState as RestMyChannelViewState
import jp.xhw.trakt.rest.models.MyUserDetail as RestMyUserDetail
import jp.xhw.trakt.rest.models.OAuth2Scope as RestOAuth2Scope
import jp.xhw.trakt.rest.models.StampHistoryEntry as RestStampHistoryEntry
import jp.xhw.trakt.rest.models.UnreadChannel as RestUnreadChannel
import jp.xhw.trakt.rest.models.UserPermission as RestUserPermission
import jp.xhw.trakt.rest.models.UserSettings as RestUserSettings
import jp.xhw.trakt.rest.models.UserSubscribeState as RestUserSubscribeState
import jp.xhw.trakt.rest.models.Webhook as RestWebhook

internal fun RestMyUserDetail.toCurrentUser(): CurrentUser =
    CurrentUser(
        id = UserId(id),
        name = name,
        displayName = displayName,
        iconFileId = FileId(iconFileId),
        isBot = bot,
        state = state.toModel(),
        updatedAt = updatedAt,
        twitterId = twitterId,
        lastOnline = lastOnline,
        tags = tags.map { it.toModel() },
        groupIds = groups.map { GroupId(it) },
        bio = bio,
        homeChannelId = homeChannel?.let { ChannelId(it) },
        permissions = permissions.map { it.toModel() },
    )

internal fun RestUserPermission.toModel(): UserPermission = UserPermission(value)

internal fun RestOAuth2Scope.toModel(): OAuth2Scope = OAuth2Scope(value)

internal fun RestChannelSubscribeLevel.toModel(): ChannelSubscriptionLevel =
    when (this) {
        RestChannelSubscribeLevel.none -> ChannelSubscriptionLevel.NONE
        RestChannelSubscribeLevel.subscribed -> ChannelSubscriptionLevel.SUBSCRIBED
        RestChannelSubscribeLevel.notified -> ChannelSubscriptionLevel.NOTIFIED
    }

internal fun ChannelSubscriptionLevel.toApiModel(): RestChannelSubscribeLevel =
    when (this) {
        ChannelSubscriptionLevel.NONE -> RestChannelSubscribeLevel.none
        ChannelSubscriptionLevel.SUBSCRIBED -> RestChannelSubscribeLevel.subscribed
        ChannelSubscriptionLevel.NOTIFIED -> RestChannelSubscribeLevel.notified
    }

internal fun RestUserSubscribeState.toModel(): ChannelSubscription =
    ChannelSubscription(
        channelId = ChannelId(channelId),
        level = level.toModel(),
    )

internal fun RestUnreadChannel.toModel(): UnreadChannel =
    UnreadChannel(
        channelId = ChannelId(channelId),
        count = count,
        noticeable = noticeable,
        since = since,
        updatedAt = updatedAt,
        oldestMessageId = MessageId(oldestMessageId),
    )

internal fun RestMyChannelViewState.toModel(): MyChannelViewState =
    MyChannelViewState(
        key = key,
        channelId = ChannelId(channelId),
        state = state.toModel(),
    )

internal fun RestUserSettings.toModel(): UserSettings =
    UserSettings(
        userId = UserId(id),
        notifyCitation = notifyCitation,
    )

internal fun RestLoginSession.toModel(): LoginSession =
    LoginSession(
        id = LoginSessionId(id),
        issuedAt = issuedAt,
    )

internal fun RestActiveOAuth2Token.toModel(): ActiveOAuth2Token =
    ActiveOAuth2Token(
        id = OAuth2TokenId(id),
        clientId = clientId,
        scopes = scopes.map { it.toModel() },
        issuedAt = issuedAt,
    )

internal fun RestExternalProviderUser.toModel(): ExternalAccount =
    ExternalAccount(
        providerName = providerName,
        linkedAt = linkedAt,
        externalName = externalName,
    )

internal fun RestStampHistoryEntry.toModel(): StampHistoryEntry =
    StampHistoryEntry(
        stampId = StampId(stampId),
        datetime = datetime,
    )

internal fun RestStampRecommendation.toModel(): StampRecommendation =
    StampRecommendation(
        stampId = StampId(stampId),
        score = score,
    )

internal fun RestClipFolder.toModel(): ClipFolder =
    ClipFolder(
        id = ClipFolderId(id),
        name = name,
        description = description,
        ownerId = UserId(ownerId),
        createdAt = createdAt,
    )

internal fun RestClippedMessage.toModel(): ClippedMessage =
    ClippedMessage(
        message = message.toModel(),
        clippedAt = clippedAt,
    )

internal fun RestMessageClip.toModel(): MessageClip =
    MessageClip(
        folderId = ClipFolderId(folderId),
        clippedAt = clippedAt,
    )

internal fun RestWebhook.toModel(): Webhook =
    Webhook(
        id = WebhookId(id),
        botUserId = UserId(botUserId),
        displayName = displayName,
        description = description,
        isSecure = secure,
        channelId = ChannelId(channelId),
        ownerId = UserId(ownerId),
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

internal fun RestBot.toManagedModel(): ManagedBot.Basic =
    ManagedBot.Basic(
        id = BotId(id),
        botUserId = UserId(botUserId),
        description = description,
        developerId = UserId(developerId),
        subscribeEvents = subscribeEvents,
        mode = mode.toModel(),
        state = state.toModel(),
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

internal fun RestBotDetail.toManagedModel(): ManagedBot.Detail =
    ManagedBot.Detail(
        id = BotId(id),
        botUserId = UserId(botUserId),
        description = description,
        developerId = UserId(developerId),
        subscribeEvents = subscribeEvents,
        mode = mode.toModel(),
        state = state.toModel(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        tokens = tokens.toModel(),
        endpoint = endpoint,
        privileged = privileged,
        channelIds = channels.map { ChannelId(it) },
    )

internal fun RestGetBotResponse.toManagedModel(): ManagedBot.Detail =
    ManagedBot.Detail(
        id = BotId(id),
        botUserId = UserId(botUserId),
        description = description,
        developerId = UserId(developerId),
        subscribeEvents = subscribeEvents,
        mode = mode.toModel(),
        state = state.toModel(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        tokens = tokens.toModel(),
        endpoint = endpoint,
        privileged = privileged,
        channelIds = channels.map { ChannelId(it) },
    )

internal fun RestBotMode.toModel(): ManagedBotMode =
    when (this) {
        RestBotMode.HTTP -> ManagedBotMode.HTTP
        RestBotMode.WebSocket -> ManagedBotMode.WEBSOCKET
    }

internal fun ManagedBotMode.toApiModel(): RestBotMode =
    when (this) {
        ManagedBotMode.HTTP -> RestBotMode.HTTP
        ManagedBotMode.WEBSOCKET -> RestBotMode.WebSocket
    }

internal fun RestBotState.toModel(): ManagedBotState =
    when (this) {
        RestBotState.deactivated -> ManagedBotState.DEACTIVATED
        RestBotState.active -> ManagedBotState.ACTIVE
        RestBotState.suspended -> ManagedBotState.SUSPENDED
    }

internal fun RestBotTokens.toModel(): BotTokens =
    BotTokens(
        verificationToken = verificationToken,
        accessToken = accessToken,
    )

internal fun RestBotEventResult.toModel(): BotEventResult =
    when (this) {
        RestBotEventResult.OK -> BotEventResult.OK
        RestBotEventResult.NG -> BotEventResult.NG
        RestBotEventResult.NetworkError -> BotEventResult.NETWORK_ERROR
        RestBotEventResult.Dropped -> BotEventResult.DROPPED
    }

internal fun RestBotEventLog.toModel(): BotEventLog =
    BotEventLog(
        botId = BotId(botId),
        requestId = requestId.toString(),
        event = event,
        result = result?.toModel(),
        code = code,
        datetime = datetime,
    )
