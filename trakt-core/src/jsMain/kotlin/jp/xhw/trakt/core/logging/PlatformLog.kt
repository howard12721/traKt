package jp.xhw.trakt.core.logging

internal actual fun platformIsLoggable(
    level: LogLevel,
    loggerName: String,
): Boolean = true

internal actual fun platformLog(
    level: LogLevel,
    loggerName: String,
    message: String,
    throwable: Throwable?,
) {
    val formatted = "[$loggerName] $message"
    when (level) {
        LogLevel.DEBUG -> console.log(formatted, throwable ?: "")
        LogLevel.INFO -> console.info(formatted, throwable ?: "")
        LogLevel.WARN -> console.warn(formatted, throwable ?: "")
        LogLevel.ERROR -> console.error(formatted, throwable ?: "")
    }
}
