package com.github.howard12721.trakt.websocket

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class WebSocketClientData(
    val origin: String = "wss://q.trap.jp",
    val client: HttpClient = HttpClient(CIO) {
        install(WebSockets)
    },
    val eventFlow: MutableSharedFlow<Event> = MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class Request(val type: String, val reqId: Uuid, val body: JsonElement)

class WebSocketClient(
    private val token: String,
    private val data: WebSocketClientData = WebSocketClientData(),
) : CoroutineScope {
    companion object {
        private const val TRAQ_GATEWAY_PATH = "/api/v3/bots/ws"
    }

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Default

    val events: SharedFlow<Event> = data.eventFlow.asSharedFlow()

    val logger: Logger by lazy { LoggerFactory.getLogger(WebSocketClient::class.java) }

    inline fun <reified T : Event> on(
        scope: CoroutineScope = this,
        crossinline block: suspend T.() -> Unit
    ): Job = events
        .buffer(Channel.UNLIMITED)
        .filterIsInstance<T>()
        .onEach {
            launch {
                runCatching { it.block() }
                    .onFailure { error -> logger.error("Error processing event", error) }
            }
        }
        .launchIn(scope)

    private lateinit var session: DefaultClientWebSocketSession

    suspend fun start() {
        session = data.client.webSocketSession {
            url("${data.origin}$TRAQ_GATEWAY_PATH")
            header("Authorization", "Bearer $token")
        }

        if (session.isActive) {
            logger.info("WebSocket session established successfully")
        } else {
            logger.error("Failed to establish WebSocket session")
            throw WebSocketException("Failed to establish WebSocket session")
        }

        withContext(Dispatchers.Default) {
            processMessages()
        }
    }

    suspend fun stop() {
        logger.info("Stopping WebSocket client...")

        if (::session.isInitialized && session.isActive) {
            try {
                data.eventFlow.emit(Event.Close)
                session.close(CloseReason(CloseReason.Codes.NORMAL, "Client requested disconnect"))
                logger.info("WebSocket session closed successfully")
            } catch (e: Exception) {
                logger.error("Error while closing WebSocket session", e)
            }
        }

        delay(1500)

        coroutineContext.cancelChildren()

        logger.info("WebSocket client stopped")
    }

    suspend fun sendCommand(command: String) {
        if (!session.isActive) {
            logger.warn("WebSocket session is not active. Cannot send command: $command")
        }
        session.send(command)
    }

    private suspend fun processMessages() {
        while (session.isActive) {
            try {
                session.incoming.receiveAsFlow()
                    .buffer(Channel.UNLIMITED)
                    .collect { frame ->
                        when (frame) {
                            is Frame.Text -> handleTextFrame(frame)
                            else -> { //ignore other frame types
                                logger.info("Received non-text frame: {}", frame)
                            }
                        }
                    }
            } catch (e: Exception) {
                logger.error("Error processing WebSocket messages", e)
            }
        }
    }

    private suspend fun handleTextFrame(frame: Frame.Text) {
        val frameData = frame.data.decodeToString()
        runCatching {
            val request = Json.decodeFromString<Request>(frameData)
            logger.info("Received frame: $frameData")
            val event = Event.decodeEvent(request.type, request.body)
            data.eventFlow.emit(event)
        }.onFailure { error ->
            logger.error("Failed to process frame: $frameData", error)
        }
    }
}
