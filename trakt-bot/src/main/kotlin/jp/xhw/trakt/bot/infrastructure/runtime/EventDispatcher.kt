package jp.xhw.trakt.bot.infrastructure.runtime

import jp.xhw.trakt.bot.context.ClientContext
import jp.xhw.trakt.bot.infrastructure.LoggerFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

/** domain event を登録された handler へ配送します。 */
internal class EventDispatcher<C : ClientContext, E : Any> {
    private val logger = LoggerFactory.getLogger("jp.xhw.trakt.bot.infrastructure.runtime.EventDispatcher")

    private val handlers = mutableListOf<suspend (E) -> Unit>()

    fun <T : E> on(
        eventClass: KClass<T>,
        context: C,
        handler: suspend C.(T) -> Unit,
    ) {
        handlers += registeredHandler@{ event ->
            if (!eventClass.isInstance(event)) {
                return@registeredHandler
            }
            @Suppress("UNCHECKED_CAST")
            val typedEvent = event as T
            try {
                context.handler(typedEvent)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logger.error("Error while handling event: {}", eventClass.simpleName, e)
            }
        }
    }

    fun subscribe(
        eventSource: Flow<E>,
        scope: CoroutineScope,
    ): Job? {
        if (handlers.isEmpty()) {
            return null
        }

        val installedHandlers = handlers.toList()
        return eventSource
            .onEach { event ->
                installedHandlers.forEach { handler ->
                    scope.launch {
                        handler(event)
                    }
                }
            }.launchIn(scope)
    }
}
