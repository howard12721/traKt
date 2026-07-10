package jp.xhw.trakt.bot.infrastructure.gateway

import io.ktor.client.HttpClient
import jp.xhw.trakt.bot.model.StampType
import jp.xhw.trakt.rest.apis.StampApi
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TraqStampPortTest {
    @Test
    fun selectsCompatibleGetStampsParameters() =
        runBlocking {
            val actualRequests = mutableListOf<GetStampsRequest>()
            val httpClient = HttpClient()
            val stampApi =
                object : StampApi("https://example.invalid", httpClient) {
                    override suspend fun getStamps(
                        includeUnicode: Boolean?,
                        type: TypeGetStamps?,
                    ): Nothing {
                        actualRequests += GetStampsRequest(includeUnicode, type)
                        throw RequestCaptured()
                    }
                }

            try {
                assertFailsWith<RequestCaptured> {
                    TraqStampPort(stampApi).fetchStamps(StampType.ORIGINAL)
                }
                assertFailsWith<RequestCaptured> {
                    TraqStampPort(stampApi).fetchStamps(null)
                }
            } finally {
                httpClient.close()
            }

            assertEquals(
                listOf(
                    GetStampsRequest(includeUnicode = null, type = StampApi.TypeGetStamps.ORIGINAL),
                    GetStampsRequest(includeUnicode = true, type = null),
                ),
                actualRequests,
            )
        }

    private data class GetStampsRequest(
        val includeUnicode: Boolean?,
        val type: StampApi.TypeGetStamps?,
    )

    private class RequestCaptured : RuntimeException()
}
