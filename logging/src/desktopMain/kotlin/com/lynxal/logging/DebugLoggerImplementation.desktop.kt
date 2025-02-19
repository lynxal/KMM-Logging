package com.lynxal.logging

import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual open class DebugLoggerImplementation : LoggerImplementation {
    actual override fun log(logDetails: LogDetails, loggerExtras: LoggerExtras) {
        println(formatMessage(logDetails, loggerExtras))
    }

    private fun formatMessage(logDetails: LogDetails, loggerExtras: LoggerExtras) : String {
        // Everything after this is in red
        val red = "\u001b[31m"
        // Resets previous color codes
        val reset = "\u001b[0m"

        val messageBuilder = StringBuilder()

        if (logDetails.logLevel == LogLevel.Error) {
            messageBuilder.append(red)
        }


        messageBuilder.append(loggerExtras.tag + " / ")
        messageBuilder.append(logDetails.timestamp.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET) + " / ")
        messageBuilder.append("[${logDetails.logLevel.name}] ")
        messageBuilder.append(logDetails.message)


        if (logDetails.payload.isNotEmpty()) {
            messageBuilder.append("\n")
            messageBuilder.append("Arguments {${
                logDetails.payload.entries.joinToString {
                    "${it.key}: ${it.value}"
                }
            }}")
        }

        if (logDetails.cause != null) {
            messageBuilder.append("\n")
            messageBuilder.append(logDetails.cause.message)
            messageBuilder.append("\n")
            messageBuilder.append(logDetails.cause.cause)
        }

        if (logDetails.logLevel == LogLevel.Error) {
            messageBuilder.append(reset)
        }

        return messageBuilder.toString()
    }
}
