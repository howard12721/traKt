package jp.xhw.trakt.bot.infrastructure.client

import jp.xhw.trakt.bot.context.ClientContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

class BaseRuntimeTest {
    @Test
    fun closeIsIdempotentAndCancelsOwnedJobs() =
        runBlocking {
            var closeCount = 0
            val client =
                BaseRuntime<TestContext, Any>(
                    context = TestContext,
                    coroutineContext = Dispatchers.Default,
                    onClose = { closeCount++ },
                )
            val started = CompletableDeferred<Unit>()
            val cancelled = CompletableDeferred<Unit>()
            val job =
                client.launchAndExecute {
                    started.complete(Unit)
                    try {
                        awaitCancellation()
                    } finally {
                        cancelled.complete(Unit)
                    }
                }

            started.await()
            client.close()
            client.close()

            withTimeout(1_000.milliseconds) {
                cancelled.await()
            }
            assertEquals(1, closeCount)
            assertTrue(job.isCancelled)
            assertFailsWith<IllegalStateException> {
                client.execute {}
            }
            assertFailsWith<IllegalStateException> {
                client.launchAndExecute {}
            }
            Unit
        }

    @Test
    fun useClosesClientWhenBlockFails() {
        var closeCount = 0
        val client =
            BaseRuntime<TestContext, Any>(
                context = TestContext,
                coroutineContext = Dispatchers.Default,
                onClose = { closeCount++ },
            )

        assertFailsWith<IllegalStateException> {
            client.use {
                error("failed")
            }
        }

        assertEquals(1, closeCount)
        assertTrue(client.isClosed)
    }

    private data object TestContext : ClientContext
}
