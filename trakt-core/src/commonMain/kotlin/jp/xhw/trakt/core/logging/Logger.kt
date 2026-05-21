package jp.xhw.trakt.core.logging

class Logger(
    private val name: String,
    private val debugEnabled: Boolean = false,
) {
    fun debug(
        message: String,
        vararg args: Any?,
    ) {
        log(LogLevel.DEBUG, message, args)
    }

    fun info(
        message: String,
        vararg args: Any?,
    ) {
        log(LogLevel.INFO, message, args)
    }

    fun warn(
        message: String,
        vararg args: Any?,
    ) {
        log(LogLevel.WARN, message, args)
    }

    fun error(
        message: String,
        vararg args: Any?,
    ) {
        log(LogLevel.ERROR, message, args)
    }

    private fun log(
        level: LogLevel,
        message: String,
        args: Array<out Any?>,
    ) {
        if (level == LogLevel.DEBUG && !debugEnabled) {
            return
        }

        if (level != LogLevel.DEBUG || !debugEnabled) {
            if (!platformIsLoggable(level, name)) {
                return
            }
        }

        val throwable = args.lastOrNull() as? Throwable
        val values = if (throwable == null) args.asList() else args.dropLast(1)
        platformLog(level, name, formatMessage(message, values), throwable)
    }

    private fun formatMessage(
        template: String,
        args: List<Any?>,
    ): String {
        if (args.isEmpty()) {
            return template
        }

        val builder = StringBuilder()
        var cursor = 0
        var argIndex = 0
        while (cursor < template.length) {
            val placeholder = template.indexOf("{}", startIndex = cursor)
            if (placeholder < 0 || argIndex >= args.size) {
                builder.append(template, cursor, template.length)
                break
            }
            builder.append(template, cursor, placeholder)
            builder.append(args[argIndex++])
            cursor = placeholder + 2
        }

        if (argIndex < args.size) {
            builder.append(" [")
            args.drop(argIndex).joinTo(builder)
            builder.append("]")
        }

        return builder.toString()
    }
}

object LoggerFactory {
    fun getLogger(
        name: String,
        debugEnabled: Boolean = false,
    ): Logger = Logger(name, debugEnabled)
}

internal enum class LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR,
}

internal expect fun platformIsLoggable(
    level: LogLevel,
    loggerName: String,
): Boolean

internal expect fun platformLog(
    level: LogLevel,
    loggerName: String,
    message: String,
    throwable: Throwable?,
)
