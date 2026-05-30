package jp.xhw.trakt.bot.infrastructure.runtime

import jp.xhw.trakt.bot.context.ClientContext
import jp.xhw.trakt.bot.dsl.TraktDsl
import kotlin.reflect.KClass
import kotlin.time.Duration

@TraktDsl
class EventRegistrar<C : ClientContext, E : Any> internal constructor(
    internal val context: C,
    private val dispatcher: EventDispatcher<C, E>,
) {
    internal val scheduledTasks = mutableListOf<ScheduledTask<C>>()

    inline fun <reified T : E> on(noinline handler: suspend C.(T) -> Unit) {
        on(T::class, handler)
    }

    @PublishedApi
    internal fun <T : E> on(
        eventClass: KClass<T>,
        handler: suspend C.(T) -> Unit,
    ) {
        dispatcher.on(eventClass, context, handler)
    }

    fun every(
        interval: Duration,
        initialDelay: Duration = interval,
        block: suspend C.() -> Unit,
    ) {
        require(interval > Duration.ZERO) { "interval must be positive" }
        require(initialDelay >= Duration.ZERO) { "initialDelay must be zero or positive" }
        scheduledTasks += ScheduledTask(interval, initialDelay, block)
    }
}

internal data class ScheduledTask<C : ClientContext>(
    val interval: Duration,
    val initialDelay: Duration,
    val block: suspend C.() -> Unit,
)
