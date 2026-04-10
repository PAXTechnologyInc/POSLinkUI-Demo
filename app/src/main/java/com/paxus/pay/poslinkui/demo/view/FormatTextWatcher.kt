package com.paxus.pay.poslinkui.demo.view

import android.text.Editable
import android.text.TextWatcher

/**
 * TextWatcher for input format
 */
class FormatTextWatcher(private val format: String?) : TextWatcher {
    var editing: Boolean = false
    var deleteIndex: Int = -1

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        val needDelete = count == 1 && after == 0 && (s[start] !in '0'..'9')
        if (needDelete) {
            deleteIndex = start
        } else {
            deleteIndex = -1
        }
    }

    override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
        if (charSequence == null) return
        /* Otherwise no-op: formatting runs in [afterTextChanged] to avoid fighting IME edits. */
    }

    override fun afterTextChanged(editable: Editable) {
        if (!editing) {
            editing = true
            if (deleteIndex > 0) {
                editable.delete(deleteIndex - 1, deleteIndex)
            }
            val digitsOnly = editable.toString().replace("[^0-9]".toRegex(), "")
            val formatted = formatDigitsByType(StringBuilder(digitsOnly))
            editable.replace(0, editable.length, formatted)
            editing = false
        }
    }

    private fun formatDigitsByType(value: StringBuilder): StringBuilder = when (format) {
        FORMAT_DATE, FORMAT_EXPIRY -> formatDateOrExpiry(value)
        FORMAT_TIME -> formatTime(value)
        FORMAT_PHONE -> formatPhone(value)
        FORMAT_SSN -> formatSsn(value)
        else -> value
    }

    private fun formatDateOrExpiry(value: StringBuilder): StringBuilder {
        if (value.length >= 2) { value.insert(2, "/") }
        if (value.length >= 5) { value.insert(5, "/") }
        return value
    }

    private fun formatTime(value: StringBuilder): StringBuilder {
        if (value.length >= 2) { value.insert(2, ":") }
        if (value.length >= 5) { value.insert(5, ":") }
        return value
    }

    private fun formatPhone(value: StringBuilder): StringBuilder {
        if (value.isNotEmpty()) { value.insert(0, "(") }
        if (value.length >= 4) { value.insert(4, ")") }
        if (value.length >= 8) { value.insert(8, "-") }
        return value
    }

    private fun formatSsn(value: StringBuilder): StringBuilder {
        if (value.length >= 3) { value.insert(3, "-") }
        if (value.length >= 6) { value.insert(6, "-") }
        return value
    }

    companion object {
        private const val FORMAT_DATE = "MM/DD/YYYY"
        private const val FORMAT_TIME = "HH:MM:SS"
        private const val FORMAT_PHONE = "(XXX)XXX-XXXX"
        private const val FORMAT_SSN = "XXX-XX-XXXX"
        private const val FORMAT_EXPIRY = "MM/YY"
    }
}