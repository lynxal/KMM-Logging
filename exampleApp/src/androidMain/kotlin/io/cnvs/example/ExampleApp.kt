package io.cnvs.example

import android.app.Application
import com.lynxal.logging.DebugLoggerImplementation
import com.lynxal.logging.Logger

class ExampleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Logger.add(DebugLoggerImplementation())
    }
}