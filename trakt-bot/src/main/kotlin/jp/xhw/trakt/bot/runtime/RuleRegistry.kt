package jp.xhw.trakt.bot.runtime

import jp.xhw.trakt.bot.event.Event
import jp.xhw.trakt.bot.scope.BotScope
import jp.xhw.trakt.websocket.WebSocketClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

internal class RuleRegistry {
    private val logger = LoggerFactory.getLogger(RuleRegistry::class.java)

    private val installers =
        mutableListOf<
            (
                client: WebSocketClient,
                scope: CoroutineScope,
            ) -> Job,
        >()

    fun <T : Event> on(
        eventClass: KClass<T>,
        bot: BotScope,
        handler: suspend BotScope.(T) -> Unit,
    ) {
        installers += { client, scope ->
            client.events
                .onEach { webSocketEvent ->
                    val event = webSocketEvent.toEventOrNull() ?: return@onEach
                    if (!eventClass.isInstance(event)) {
                        return@onEach
                    }
                    @Suppress("UNCHECKED_CAST")
                    val typedEvent = event as T
                    try {
                        bot.handler(typedEvent)
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        logger.error("Error while handling event: {}", eventClass.simpleName, e)
                    }
                }.launchIn(scope)
        }
    }

    fun install(
        client: WebSocketClient,
        scope: CoroutineScope,
    ): List<Job> = installers.map { installer -> installer(client, scope) }
}
