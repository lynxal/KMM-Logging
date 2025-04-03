package io.cnvs.example

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.lynxal.logging.DebugLoggerImplementation
import com.lynxal.logging.Logger
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    Logger.add(DebugLoggerImplementation())

    ComposeViewport(document.body!!) {
        App()
    }
}