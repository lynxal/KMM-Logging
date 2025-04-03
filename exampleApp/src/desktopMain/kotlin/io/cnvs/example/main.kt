package io.cnvs.example

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.lynxal.logging.DebugLoggerImplementation
import com.lynxal.logging.Logger

fun main() = application {
    Logger.add(DebugLoggerImplementation())

    Window(
        onCloseRequest = ::exitApplication,
        title = "ExampleApp",
    ) {
        App()
    }
}