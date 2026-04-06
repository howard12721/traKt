package jp.xhw.trakt.websocket

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val DEFAULT_EVENT_BUFFER_CAPACITY = 256
private const val DEFAULT_RECONNECT_DELAY_MILLIS = 5_000L

data class WebSocketClientConfig(
    val origin: String = "wss://q.trap.jp",
    val client: HttpClient =
        HttpClient(CIO) {
            install(WebSockets)
        },
    val eventFlow: MutableSharedFlow<Event> =
        MutableSharedFlow(
            replay = 0,
            extraBufferCapacity = DEFAULT_EVENT_BUFFER_CAPACITY,
            onBufferOverflow = BufferOverflow.SUSPEND,
        ),
    val reconnectDelayMillis: Long = DEFAULT_RECONNECT_DELAY_MILLIS,
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class GatewayEventEnvelope(
    val type: String,
    val reqId: Uuid,
    val body: JsonElement,
)

class WebSocketClient(
    private val token: String,
    private val config: WebSocketClientConfig = WebSocketClientConfig(),
) : CoroutineScope {
    companion object {
        private const val TRAQ_GATEWAY_PATH = "/api/v3/bots/ws"
    }

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Default

    val events: SharedFlow<Event> = config.eventFlow.asSharedFlow()

    val logger: Logger by lazy { LoggerFactory.getLogger(WebSocketClient::class.java) }

    inline fun <reified T : Event> on(
        scope: CoroutineScope = this,
        crossinline block: suspend T.() -> Unit,
    ): Job =
        events
            .filterIsInstance<T>()
            .onEach { event ->
                scope.launch {
                    try {
                        event.block()
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        logger.error("Error processing event", e)
                    }
                }
            }.launchIn(scope)

    @Volatile
    private var session: DefaultClientWebSocketSession? = null

    @Volatile
    private var started = false

    @Volatile
    private var stopping = false

    suspend fun start() {
        check(!started) { "WebSocket client is already started" }
        started = true
        stopping = false

        try {
            while (currentCoroutineContext().isActive && !stopping) {
                val openedSession =
                    try {
                        openSession()
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        if (stopping) {
                            break
                        }
                        logger.error("Failed to establish WebSocket session", e)
                        waitForReconnect()
                        continue
                    }

                try {
                    withContext(Dispatchers.Default) {
                        processMessages(openedSession)
                    }

                    if (!stopping) {
                        logger.warn("WebSocket session closed. Scheduling reconnect")
                        waitForReconnect()
                    }
                } finally {
                    session = null
                }
            }
        } finally {
            session = null
            started = false
        }
    }

    suspend fun stop() {
        logger.info("Stopping WebSocket client...")
        stopping = true

        val currentSession = session
        if (currentSession?.isActive == true) {
            try {
                config.eventFlow.tryEmit(Event.Close)
                currentSession.close(CloseReason(CloseReason.Codes.NORMAL, "Client requested disconnect"))
                logger.info("WebSocket session closed successfully")
            } catch (e: Exception) {
                logger.error("Error while closing WebSocket session", e)
            }
        }

        session = null
        coroutineContext.cancelChildren()
        logger.info("WebSocket client stopped")
    }

    fun close() {
        stopping = true
        coroutineContext.cancelChildren()
        runCatching { config.client.close() }
            .onFailure { error -> logger.error("Error while closing WebSocket HttpClient", error) }
    }

    suspend fun sendCommand(command: String) {
        val currentSession = session
        if (currentSession?.isActive != true) {
            logger.warn("WebSocket session is not active. Cannot send command: $command")
            return
        }
        currentSession.send(command)
    }

    private suspend fun openSession(): DefaultClientWebSocketSession {
        val openedSession =
            config.client.webSocketSession {
                url("${config.origin}$TRAQ_GATEWAY_PATH")
                header("Authorization", "Bearer $token")
            }

        if (!openedSession.isActive) {
            session = null
            throw WebSocketException("Failed to establish WebSocket session")
        }

        session = openedSession
        logger.info("WebSocket session established successfully")
        return openedSession
    }

    private suspend fun waitForReconnect() {
        if (stopping) {
            return
        }

        logger.info("Retrying WebSocket connection in {} ms", config.reconnectDelayMillis)
        delay(config.reconnectDelayMillis)
    }

    private suspend fun processMessages(session: DefaultClientWebSocketSession) {
        try {
            session.incoming
                .receiveAsFlow()
                .collect { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            handleTextFrame(frame)
                        }

                        else -> { // ignore other frame types
                            logger.info("Received non-text frame: {}", frame)
                        }
                    }
                }
            logger.info("WebSocket incoming channel closed")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.error("Error processing WebSocket messages", e)
        }
    }

    private suspend fun handleTextFrame(frame: Frame.Text) {
        val frameData = frame.data.decodeToString()
        runCatching {
            val envelope = Json.decodeFromString<GatewayEventEnvelope>(frameData)
            logger.info("Received frame: $frameData")
            val event = Event.decodeEvent(envelope.type, envelope.body)
            config.eventFlow.emit(event)
        }.onFailure { error ->
            logger.error("Failed to process frame: $frameData", error)
        }
    }
}
