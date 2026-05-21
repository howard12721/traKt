package jp.xhw.trakt.bot.infrastructure

import jp.xhw.trakt.core.logging.Logger as SharedLogger
import jp.xhw.trakt.core.logging.LoggerFactory as SharedLoggerFactory

internal typealias Logger = SharedLogger

internal object LoggerFactory {
    fun getLogger(name: String): Logger = SharedLoggerFactory.getLogger(name)
}
