package jp.xhw.trakt.core.logging

import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.fprintf
import platform.posix.stderr

internal actual fun platformIsLoggable(
    level: LogLevel,
    loggerName: String,
): Boolean = true

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformLog(
    level: LogLevel,
    loggerName: String,
    message: String,
    throwable: Throwable?,
) {
    val throwableMessage = throwable?.let { ": ${it.stackTraceToString()}" }.orEmpty()
    val line = "${level.name} $loggerName - $message$throwableMessage"
    when (level) {
        LogLevel.DEBUG,
        LogLevel.INFO,
        -> println(line)

        LogLevel.WARN,
        LogLevel.ERROR,
        -> fprintf(stderr, "%s\n", line)
    }
}
