package jp.xhw.trakt.bot.infrastructure.runtime

import jp.xhw.trakt.bot.context.RuntimeContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/** domain event を登録された handler へ配送します。 */
internal class RuleRegistry<R : RuntimeContext, E : Any> {
    private val logger = LoggerFactory.getLogger(RuleRegistry::class.java)

    private val installers =
        mutableListOf<
            (
                eventSource: Flow<E>,
                scope: CoroutineScope,
            ) -> Job,
        >()

    fun <T : E> on(
        eventClass: KClass<T>,
        context: R,
        handler: suspend R.(T) -> Unit,
    ) {
        installers += { eventSource, scope ->
            eventSource
                .onEach { event ->
                    if (!eventClass.isInstance(event)) {
                        return@onEach
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
                }.launchIn(scope)
        }
    }

    fun install(
        eventSource: Flow<E>,
        scope: CoroutineScope,
    ): List<Job> = installers.map { installer -> installer(eventSource, scope) }
}
