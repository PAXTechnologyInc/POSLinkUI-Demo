package com.paxus.pay.poslinkui.demo.utils

import android.content.Intent
import com.orhanobut.logger.Logger as OrhanLogger

/**
 * Logger, but more pretty, simple and powerful
 */
object Logger {
    fun d(message: String, vararg args: Any?) {
        OrhanLogger.d(message, *args)
    }

    fun d(`object`: Any?) {
        OrhanLogger.d(`object`)
    }

    fun e(message: String, vararg args: Any?) {
        OrhanLogger.e(message, *args)
    }

    fun e(throwable: Throwable?) {
        val msg = throwable?.message.orEmpty()
        OrhanLogger.e(throwable, msg)
    }

    fun e(throwable: Throwable?, message: String, vararg args: Any?) {
        OrhanLogger.e(throwable, message, *args)
    }

    fun i(message: String, vararg args: Any?) {
        OrhanLogger.i(message, *args)
    }

    fun v(message: String, vararg args: Any?) {
        OrhanLogger.v(message, *args)
    }

    fun w(message: String, vararg args: Any?) {
        OrhanLogger.w(message, *args)
    }

    /**
     * Tip: Use this for exceptional situations to log
     * ie: Unexpected errors etc
     */
    fun wtf(message: String, vararg args: Any?) {
        OrhanLogger.wtf(message, *args)
    }

    /**
     * Formats the given json content and print it
     */
    fun json(json: String?) {
        OrhanLogger.json(json)
    }

    /**
     * Formats the given xml content and print it
     */
    fun xml(xml: String?) {
        OrhanLogger.d(xml)
    }


    fun intent(intent: Intent, vararg headings: String?) {
        val intentBuilder = StringBuilder()

        for (heading in headings) {
            intentBuilder.append(heading).append(System.lineSeparator())
        }

        intentBuilder.append("Intent: ").append(intent.action).append(System.lineSeparator())
        val extras = intent.extras
        if (extras != null) {
            for (key in extras.keySet()) {
                intentBuilder.append(key).append(": ")
                val raw = extras.get(key)
                if (raw == null) {
                    intentBuilder.append("null").append(System.lineSeparator())
                    continue
                }
                val isArray = raw.javaClass.isArray
                intentBuilder.append(
                    if (isArray) stringifyArray(raw) else raw
                )
                intentBuilder.append(System.lineSeparator())
            }
        }
        i(intentBuilder.toString())
    }

    private fun stringifyArray(array: Any): String {
        val kind = array.javaClass.simpleName
        return when (kind) {
            "short[]" -> (array as ShortArray).contentToString()
            "Object[]", "String[]" -> (array as Array<String?>).contentToString()
            else -> "UNABLE TO LOG"
        }
    }
}