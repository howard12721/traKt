package jp.xhw.trakt.bot.infrastructure.gateway

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import jp.xhw.trakt.core.client.createHttpClient
import jp.xhw.trakt.rest.apis.*
import jp.xhw.trakt.rest.infrastructure.ApiClient
import jp.xhw.trakt.websocket.WebSocketClient
import jp.xhw.trakt.websocket.WebSocketClientConfig
import jp.xhw.trakt.websocket.bot.BotEvent
import jp.xhw.trakt.websocket.user.UserEvent
import kotlinx.serialization.json.Json

internal class TraqApiGateway(
    private val token: String,
    private val origin: String = "q.trap.jp",
    private val debugMode: Boolean = false,
) : AutoCloseable {
    val serializer =
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }

    val httpClient =
        createHttpClient(debugMode) {
            install(ContentNegotiation) {
                json(serializer)
            }
        }

    val botWs: WebSocketClient<BotEvent> =
        WebSocketClient(
            token,
            WebSocketClientConfig(
                origin = "wss://$origin",
                debugMode = debugMode,
                eventDecoders = listOf(BotEvent.decoder),
            ),
        )

    val userWs: WebSocketClient<UserEvent> =
        WebSocketClient(
            token,
            WebSocketClientConfig(
                origin = "wss://$origin",
                path = "/api/v3/ws",
                debugMode = debugMode,
                eventDecoders = listOf(UserEvent.decoder),
            ),
        )

    val ws: WebSocketClient<BotEvent>
        get() = botWs

    val activityApi = api(::ActivityApi)
    val botApi = api(::BotApi)
    val channelApi = api(::ChannelApi)
    val clipApi = api(::ClipApi)
    val fileApi = api(::FileApi)
    val groupApi = api(::GroupApi)
    val meApi = api(::MeApi)
    val messageApi = api(::MessageApi)
    val notificationApi = api(::NotificationApi)
    val ogpApi = api(::OgpApi)
    val pinApi = api(::PinApi)
    val publicApi = api(::PublicApi)
    val stampApi = api(::StampApi)
    val starApi = api(::StarApi)
    val userApi = api(::UserApi)
    val userTagApi = api(::UserTagApi)
    val webhookApi = api(::WebhookApi)

    private fun <T : ApiClient> api(
        factory: (
            String,
            HttpClient,
        ) -> T,
    ): T = factory("https://$origin/api/v3", httpClient).also { it.setBearerToken(token) }

    override fun close() {
        botWs.close()
        userWs.close()
        httpClient.close()
    }
}
