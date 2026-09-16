package com.lynxal.logging

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Regression cover for lynxal/KMM-Logging#5, seen downstream as
 * lynxal/canvas_control_mobile#917.
 *
 * The sinks used to live in a plain mutable set: add() mutated it while log() was iterating it,
 * so registering a sink concurrently with an in-flight log call threw
 * ConcurrentModificationException and killed the host app at startup. The sinks are now an
 * immutable snapshot swapped atomically, so a log call always iterates the set as it was when
 * the call started. Registering a sink from inside a sink's own log() reproduces the old crash
 * deterministically on a single thread — but only when another sink follows the registering one,
 * because the mutable set's iterator checked for modification on the next next() call, so a
 * registration by the last sink in the set escaped the check.
 */
class LoggerInterfaceImplTest {

    private class RecordingSink(private val onLog: (() -> Unit)? = null) : LoggerImplementation {
        val messages = mutableListOf<String>()

        override fun log(logDetails: LogDetails, loggerExtras: LoggerExtras) {
            messages.add(logDetails.message)
            onLog?.invoke()
        }
    }

    @Test
    fun addingASinkWhileALogCallIsIteratingDoesNotBreakTheLogCall() {
        val logger = LoggerInterfaceImpl()
        val lateSink = RecordingSink()
        var registered = false
        val registeringSink = RecordingSink(onLog = {
            if (!registered) {
                registered = true
                logger.add(lateSink)
            }
        })
        val followerSink = RecordingSink()
        logger.add(registeringSink)
        // The iteration must continue past the registration point to hit the old crash.
        logger.add(followerSink)

        // Threw ConcurrentModificationException before the fix.
        logger.debug("first")

        assertEquals(
            listOf("first"), registeringSink.messages,
            "the already-registered sink lost the line that raced the registration"
        )
        assertEquals(
            listOf("first"), followerSink.messages,
            "the sink registered after the registration point lost the in-flight line"
        )
    }

    @Test
    fun aSinkAddedDuringALogCallReceivesTheFollowingLines() {
        val logger = LoggerInterfaceImpl()
        val lateSink = RecordingSink()
        var registered = false
        logger.add(RecordingSink(onLog = {
            if (!registered) {
                registered = true
                logger.add(lateSink)
            }
        }))

        logger.debug("during registration")
        logger.debug("after registration")

        assertEquals(
            listOf("after registration"), lateSink.messages,
            "a sink added mid-call must start receiving with the next line, and only the next line"
        )
    }

    @Test
    fun aSinkAddedTwiceReceivesEachLineOnce() {
        val logger = LoggerInterfaceImpl()
        val sink = RecordingSink()
        logger.add(sink)
        logger.add(sink)

        logger.debug("only once")

        assertEquals(
            listOf("only once"), sink.messages,
            "set semantics were lost: one registration must mean one delivery per line"
        )
    }
}
