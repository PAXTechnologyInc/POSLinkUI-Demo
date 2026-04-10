package com.paxus.pay.poslinkui.demo.utils

import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Author: Elaine Xie
 * Date: 2026/1/22
 * Desc:
 */
@Singleton
class DateUtils @Inject constructor() {
    fun isValidateDate(input: String?): Boolean {
        if (input == null) return false

        try {
            val monthDay = input.substring(0, 4)
            val sdf = SimpleDateFormat("MMdd", Locale.US)
            sdf.setLenient(false)
            sdf.parse(monthDay)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun isValidateTime(input: String?): Boolean {
        if (input == null) return false
        //  HHMMSS，HH: 00-23, MM: 00-59, SS: 00-59
        return input.matches("([01][0-9]|2[0-3])[0-5][0-9][0-5][0-9]".toRegex())
    }
}
