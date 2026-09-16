package com.lynxal.logging

import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/**
 * Logger class purely written in kotlin. It's using the same approach as used in Timber.
 * If you wish to have a basic Logging functionality plant a DebugTree, otherwise
 * provide a custom tree implementation.
 */
@OptIn(ExperimentalAtomicApi::class)
open class LoggerInterfaceImpl : LoggerInterface {
    // Immutable snapshot swapped atomically: add() may run on any thread while log() iterates,
    // and a mutable set here throws ConcurrentModificationException (KMM-Logging issue #5).
    private val loggerImplementations: AtomicReference<Set<LoggerImplementation>> =
        AtomicReference(emptySet())
    override var minLevel: LogLevel = LogLevel.Debug

    override val extras: LoggerExtras = LoggerExtras()
    override fun verbose(loggerExtras: LoggerExtras, details: LogDetails.Builder.() -> Unit) =
        log(LogDetails.Builder().apply {
            details()
        }.build(LogLevel.Verbose), loggerExtras)

    override fun debug(loggerExtras: LoggerExtras, details: LogDetails.Builder.() -> Unit) =
        log(LogDetails.Builder().apply {
            details()
        }.build(LogLevel.Debug), loggerExtras)

    override fun info(loggerExtras: LoggerExtras, details: LogDetails.Builder.() -> Unit) =
        log(LogDetails.Builder().apply {
            details()
        }.build(LogLevel.Info), loggerExtras)

    override fun warning(loggerExtras: LoggerExtras, details: LogDetails.Builder.() -> Unit) =
        log(LogDetails.Builder().apply {
            details()
        }.build(LogLevel.Warning), loggerExtras)

    override fun error(loggerExtras: LoggerExtras, details: LogDetails.Builder.() -> Unit) =
        log(LogDetails.Builder().apply {
            details()
        }.build(LogLevel.Error), loggerExtras)


    override fun tag(tag: String): LoggerInterface {
        return LoggerWrapper(this, extras.copy(tag = tag))
    }

    override fun add(loggerImplementation: LoggerImplementation) {
        // Retry only when another thread swapped the set between the load and the compare-and-set.
        // Same contract as the stdlib's AtomicReference.update, which needs a newer Kotlin.
        do {
            val current = loggerImplementations.load()
            val updated = current + loggerImplementation
        } while (!loggerImplementations.compareAndSet(current, updated))
    }

    private fun log(logDetails: LogDetails, loggerExtras: LoggerExtras) {
        if (logDetails.logLevel.level >= minLevel.level) {
            loggerImplementations.load().forEach {
                it.log(
                    logDetails = logDetails, loggerExtras = loggerExtras
                )
            }
        }
    }
}

val Logger: LoggerInterface by lazy { LoggerInterfaceImpl() }
