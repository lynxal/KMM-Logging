package com.lynxal.logging

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class LogDetails(
    val logLevel: LogLevel,
    val message: String,
    val cause: Throwable? = null,
    val payload: Map<String, String> = emptyMap(),
    val timestamp: Instant = Clock.System.now()
) {
    data class Builder internal constructor(
        var message: String = "",
        var cause: Throwable? = null,
        var payload: Map<String, String> = emptyMap()
    ) {
        internal fun build(logLevel: LogLevel) = LogDetails(logLevel, message, cause, payload)
    }
}