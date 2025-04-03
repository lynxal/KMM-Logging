package io.cnvs.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.lynxal.logging.LogLevel
import com.lynxal.logging.Logger
import kmmlogging.exampleapp.generated.resources.Res
import kmmlogging.exampleapp.generated.resources.label_check_log_messages
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    LaunchedEffect(Unit) {
        Logger.minLevel = LogLevel.Verbose

        Logger.verbose("Verbose Message")
        Logger.debug("Debug Message")
        Logger.info("Info Message")
        Logger.warning("Warning Message")
        Logger.error("Error Message")

        Logger.verbose("Verbose Message, with exception", cause = RuntimeException("Exception Message"))
        Logger.debug("Debug Message, with exception", cause = RuntimeException("Exception Message"))
        Logger.info("Info Message, with exception", cause = RuntimeException("Exception Message"))
        Logger.warning("Warning Message, with exception", cause = RuntimeException("Exception Message"))
        Logger.error("Error Message, with exception", cause = RuntimeException("Exception Message"))

        Logger.tag("Logger Tag").verbose("Verbose Message, with tag")
        Logger.tag("Logger Tag").debug("Debug Message, with tag")
        Logger.tag("Logger Tag").info("Info Message, with tag")
        Logger.tag("Logger Tag").warning("Warning Message, with tag")
        Logger.tag("Logger Tag").error("Error Message, with tag")
    }

    MaterialTheme {
        Box(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                text = stringResource(Res.string.label_check_log_messages)
            )
        }
    }
}