package jp.xhw.trakt.bot

import jp.xhw.trakt.rest.apis.*
import jp.xhw.trakt.websocket.WebSocketClient

class TraktClient(
    private val token: String,
) {
    val activityApi: ActivityApi = ActivityApi()
    val authenticationApi: AuthenticationApi = AuthenticationApi()
    val botApi: BotApi = BotApi()
    val channelApi: ChannelApi = ChannelApi()
    val clipApi: ClipApi = ClipApi()
    val fileApi: FileApi = FileApi()
    val groupApi: GroupApi = GroupApi()
    val meApi: MeApi = MeApi()
    val messageApi: MessageApi = MessageApi()
    val notificationsApi: NotificationApi = NotificationApi()
    val oauth2Api: Oauth2Api = Oauth2Api()
    val ogpApi: OgpApi = OgpApi()
    val pinApi: PinApi = PinApi()
    val publicApi: PublicApi = PublicApi()
    val stampApi: StampApi = StampApi()
    val starApi: StarApi = StarApi()
    val userApi: UserApi = UserApi()
    val userTagApi: UserTagApi = UserTagApi()
    val webhookApi: WebhookApi = WebhookApi()
    val webrtcApi: WebrtcApi = WebrtcApi()
    val websocketClient: WebSocketClient = WebSocketClient(token)

    init {
        this.activityApi.setBearerToken(token)
        this.authenticationApi.setBearerToken(token)
        this.botApi.setBearerToken(token)
        this.channelApi.setBearerToken(token)
        this.clipApi.setBearerToken(token)
        this.fileApi.setBearerToken(token)
        this.groupApi.setBearerToken(token)
        this.meApi.setBearerToken(token)
        this.messageApi.setBearerToken(token)
        this.notificationsApi.setBearerToken(token)
        this.oauth2Api.setBearerToken(token)
        this.ogpApi.setBearerToken(token)
        this.pinApi.setBearerToken(token)
        this.publicApi.setBearerToken(token)
        this.stampApi.setBearerToken(token)
        this.starApi.setBearerToken(token)
        this.userApi.setBearerToken(token)
        this.userTagApi.setBearerToken(token)
        this.webhookApi.setBearerToken(token)
        this.webrtcApi.setBearerToken(token)
    }
}
