package jp.xhw.trakt.core.client

import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import jp.xhw.trakt.core.logging.LoggerFactory
import io.ktor.client.plugins.logging.Logger as KtorLogger

fun createHttpClient(
    debugMode: Boolean,
    configure: HttpClientConfig<*>.() -> Unit = {},
): HttpClient =
    HttpClient {
        if (debugMode) {
            install(Logging) {
                logger =
                    object : KtorLogger {
                        private val log = LoggerFactory.getLogger("io.ktor.client.HttpClient", debugEnabled = true)

                        override fun log(message: String) {
                            log.debug(message)
                        }
                    }
                level = LogLevel.ALL
            }
        }
        configure()
    }
