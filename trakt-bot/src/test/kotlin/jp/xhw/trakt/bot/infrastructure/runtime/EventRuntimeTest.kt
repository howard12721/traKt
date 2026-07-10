package jp.xhw.trakt.bot.infrastructure.runtime

import jp.xhw.trakt.bot.context.ClientContext
import jp.xhw.trakt.bot.infrastructure.client.BaseRuntime
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.milliseconds

class EventRuntimeTest {
    @Test
    fun externalStopAndRunFinallyCloseOnce() =
        runBlocking {
            val fixture = fixture()
            val runJob = launch { fixture.runtime.run() }
            fixture.lifecycle.started.await()

            fixture.runtime.stop()
            withTimeout(1_000.milliseconds) {
                runJob.join()
            }

            assertEquals(1, fixture.lifecycle.stopCount)
            assertEquals(1, fixture.closeCount())
            assertFailsWith<IllegalStateException> {
                fixture.runtime.start()
            }
            Unit
        }

    @Test
    fun cancellingRunStillClosesOnce() =
        runBlocking {
            val fixture = fixture()
            val runJob = launch { fixture.runtime.run() }
            fixture.lifecycle.started.await()

            runJob.cancelAndJoin()

            assertEquals(1, fixture.lifecycle.stopCount)
            assertEquals(1, fixture.closeCount())
        }

    private fun fixture(): Fixture {
        var closeCount = 0
        val lifecycle = TestLifecycle()
        val base =
            BaseRuntime<TestContext, Any>(
                context = TestContext,
                coroutineContext = Dispatchers.Default,
                eventSource = emptyFlow<Any>(),
                eventMapper = { it },
                lifecycle = lifecycle,
                onClose = { closeCount++ },
            )
        return Fixture(
            runtime = base.buildEventRuntime {},
            lifecycle = lifecycle,
            closeCount = { closeCount },
        )
    }

    private class TestLifecycle : Lifecycle {
        val started = CompletableDeferred<Unit>()
        var stopCount = 0

        override suspend fun start() {
            started.complete(Unit)
            awaitCancellation()
        }

        override suspend fun awaitStarted() {
            started.await()
        }

        override suspend fun stop() {
            yield()
            stopCount++
        }
    }

    private data class Fixture(
        val runtime: EventRuntime<TestContext, Any>,
        val lifecycle: TestLifecycle,
        val closeCount: () -> Int,
    )

    private data object TestContext : ClientContext
}
