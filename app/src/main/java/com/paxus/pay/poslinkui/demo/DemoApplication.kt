package com.paxus.pay.poslinkui.demo

import android.app.Application
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.paxus.pay.poslinkui.demo.utils.FileLogAdapter
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Yanina.Yang on 5/11/2022.
 * 
 * Init log when application started
 */
@HiltAndroidApp
class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //============Init Log===========
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .methodCount(1)
            .methodOffset(6) // (Optional) Hides internal method calls up to offset. Default 0
            .tag("POSLinkUIDemo")
            .build()
        val logDir = getExternalFilesDir(null)?.absolutePath ?: filesDir.absolutePath
        Logger.addLogAdapter(FileLogAdapter(formatStrategy, logDir, "POSLinkUILog"))
    }
}
