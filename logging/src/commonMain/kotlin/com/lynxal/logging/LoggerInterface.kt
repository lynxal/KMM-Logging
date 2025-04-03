package com.lynxal.logging

interface LoggerInterface {
    /**
     * Sets the minimum log level that will be logged.
     */
    var minLevel: LogLevel

    /**
     * Retrieves the extras being used by the LoggerInstance. At this point we support only an optional tag property.
     */
    val extras: LoggerExtras

    /**
     * Prints a message using [LogLevel.Verbose]
     */
    fun verbose(loggerExtras: LoggerExtras = extras, details: LogDetails.Builder.() -> Unit)

    /**
     * A helper function simplifying the logger interface, prints a message using [LogLevel.Verbose]
     */
    fun verbose(message: String, cause: Throwable? = null) = verbose {
        this.message = message
        this.cause = cause
    }

    /**
     * Prints a message using [LogLevel.Debug]
     */
    fun debug(loggerExtras: LoggerExtras = extras, details: LogDetails.Builder.() -> Unit)

    /**
     * A helper function simplifying the logger interface, prints a message using [LogLevel.Debug]
     */
    fun debug(message: String, cause: Throwable? = null) = debug {
        this.message = message
        this.cause = cause
    }

    /**
     * Prints a message using [LogLevel.Info]
     */
    fun info(loggerExtras: LoggerExtras = extras, details: LogDetails.Builder.() -> Unit)

    /**
     * A helper function simplifying the logger interface, prints a message using [LogLevel.Info]
     */
    fun info(message: String, cause: Throwable? = null) = info {
        this.message = message
        this.cause = cause
    }

    /**
     * Prints a message using [LogLevel.Warning]
     */
    fun warning(loggerExtras: LoggerExtras = extras, details: LogDetails.Builder.() -> Unit)

    /**
     * A helper function simplifying the logger interface, prints a message using [LogLevel.Warning]
     */
    fun warning(message: String, cause: Throwable? = null) = warning {
        this.message = message
        this.cause = cause
    }

    /**
     * Prints a message using [LogLevel.Error]
     */
    fun error(loggerExtras: LoggerExtras = extras, details: LogDetails.Builder.() -> Unit)

    /**
     * A helper function simplifying the logger interface, prints a message using [LogLevel.Error]
     */
    fun error(message: String, cause: Throwable? = null) = error {
        this.message = message
        this.cause = cause
    }

    /**
     * This method will set the optional logging tag.
     * You might want to create a tagged logger instance to track activity of specific module or feature.
     */
    fun tag(tag: String): LoggerInterface

    /**
     * Adds a logger implementation, you can use [DebugLoggerImplementation] or provide a custom implementation by implementing [LoggerImplementation]
     */
    fun add(loggerImplementation: LoggerImplementation)
}