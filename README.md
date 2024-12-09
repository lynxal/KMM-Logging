# KMMLogging

**KMMLogging** is a lightweight, multiplatform logging library for Kotlin Multiplatform Mobile (KMM) projects. It offers a robust and flexible API for handling logs in both Android and iOS applications.

---

## Features

- Multiplatform support: Android and iOS.
- Easy-to-use API with customizable log levels.
- Tagging support for categorized logging.
- Builder-based pattern for structured log messages.

---

## Installation

Add the following dependency to your shared module's `build.gradle.kts` file:

```kotlin
implementation("com.lynxal.logging:logging:{latest_version}")
```

## Getting Started

### Logger Setup

By default, Logger is not configured, and calls to methods like Logger.debug will not produce any output. To enable logging, you need to provide a LoggerInterface implementation.

For Android and iOS, you can use the built-in DebugLoggerImplementation, which logs messages using the platform’s native logging systems.

### Setting Up the Logger

To enable debug logging, call the following during your app’s initialization:

```kotlin
fun setupLogging() {
    Logger.add(DebugLoggerImplementation())
}
```

You can add multiple logger implementations if needed (for remote logging, analytics...)

### Using the Logger

Once set up, you can use the logger as shown below. The main logging interface is accessible through the Logger variable, which implements LoggerInterface. You can tag log messages globally or create a local tagged logger instance.

```kotlin
// Using global Logger instance
Logger.error("Something went wrong", cause = exception)

// DSL style
Logger.error {
    message("This is a global error log.")
    payload = mapOf("any_key", "any_string_value")
    cause = RuntimeException("Something went wrong")
}

// Tagging directly and logging
Logger.tag("MyGlobalTag").debug {
    message("This is a debug log with a global tag.")
}

// Creating local tagged logger
val localLogger = Logger.tag("my_tag")
localLogger.info("Inspecting object tree...")
```

### Logging Levels and Methods

KMMLogging provides logging methods corresponding to common log levels: verbose, debug, info, warn, and error. Each method shares a unified structure and accepts the following arguments:
1.	LogExtras: Used to set additional metadata for the log entry, such as the tag.
2.	LogDetails.Builder: A builder for specifying detailed log properties.

LogDetails.Builder Parameters
•	message: String (default: "")
The main content of the log message.
•	cause: Throwable? (default: null)
Optional exception or error to include in the log.
•	payload: Map<String, String> (default: emptyMap())
Key-value pairs containing additional data for the logger implementation to process.

**Example**
```kotlin
Logger.verbose(LogExtras(tag = "VerboseTag")) {
    message = "Detailed debug information"
    payload = mapOf("userId" to "1234", "action" to "loadData")
}
```

### Key Advantages of Unified Structure
**Consistency**: The unified structure ensures that all log levels follow the same format and behavior.  

**Customizability**: The LogExtras and LogDetails.Builder allow you to enrich your logs with structured data and contextual information.  

**Extensibility**: By passing payload, you can share additional data with the logger interface implementation, enabling advanced processing or analytics.  

# API Reference

LoggerInterface Methods

**add(logger: LoggerInterface)** - Adds a new logger implementation to the Logger.  

**tag(tag: String): LoggerInterface** - Returns a new LoggerInterface instance with the specified tag.  

**Logging Methods** - verbose, debug, info, warn, error, accept a builder function to construct log details.  

# Contributions

Contributions are welcome! If you find issues or have suggestions, feel free to open an issue or submit a pull request.

# License

This project is licensed under the MIT License. See the LICENSE file for details.