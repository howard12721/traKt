package jp.xhw.trakt.websocket

import jp.xhw.trakt.core.logging.Logger as SharedLogger
import jp.xhw.trakt.core.logging.LoggerFactory as SharedLoggerFactory

internal typealias Logger = SharedLogger

internal object LoggerFactory {
    fun getLogger(
        name: String,
        debugEnabled: Boolean = false,
    ): Logger = SharedLoggerFactory.getLogger(name, debugEnabled)
}
