package com.paxus.pay.poslinkui.demo.utils

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.view.Display

object DeviceUtils {

    private val PHYSICAL_KEYBOARD_MODELS = setOf("A80", "A30", "A35", "Aries6", "Aries8")

    fun hasPhysicalKeyboard(): Boolean {
        // For devices which has physical pin pad, do not prompt virtual pin pad
        return Build.MODEL in PHYSICAL_KEYBOARD_MODELS
    }

    fun getSecondDisplay(context: Context): Display? {
        val manager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        return manager.getDisplays()
            .firstOrNull { it.displayId != Display.DEFAULT_DISPLAY }
    }
}
