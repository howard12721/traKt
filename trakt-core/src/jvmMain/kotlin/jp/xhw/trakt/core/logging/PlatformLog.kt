package jp.xhw.trakt.core.logging

import java.util.logging.Level
import java.util.logging.Logger

internal actual fun platformIsLoggable(
    level: LogLevel,
    loggerName: String,
): Boolean = Logger.getLogger(loggerName).isLoggable(level.toJavaLevel())

internal actual fun platformLog(
    level: LogLevel,
    loggerName: String,
    message: String,
    throwable: Throwable?,
) {
    Logger.getLogger(loggerName).log(level.toJavaLevel(), message, throwable)
}

private fun LogLevel.toJavaLevel(): Level =
    when (this) {
        LogLevel.DEBUG -> Level.FINE
        LogLevel.INFO -> Level.INFO
        LogLevel.WARN -> Level.WARNING
        LogLevel.ERROR -> Level.SEVERE
    }
