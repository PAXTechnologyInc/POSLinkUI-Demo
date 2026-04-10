package com.paxus.pay.poslinkui.demo.utils

import android.annotation.SuppressLint
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Stack

class InterfaceHistory @Inject constructor() {
    private val history: Stack<InterfaceRecord?>

    init {
        history = Stack<InterfaceRecord?>()
        Logger.d("Interface History Initialized.")
    }

    fun add(interfaceID: String, interfaceAction: String) {
        val newInterfaceRecord =
            InterfaceRecord(interfaceID, interfaceAction, System.currentTimeMillis())
        Logger.d("New Interface: " + newInterfaceRecord)
        history.push(newInterfaceRecord)
    }

    fun validate(intendedInterfaceID: String?, intendedInterfaceAction: String?): Boolean {
        if (history.empty()) return true

        val top = history.peek() ?: return true
        val isInterfaceIDValid =
            intendedInterfaceID != null && top.interfaceID == intendedInterfaceID
        val isInterfaceActionValid =
            intendedInterfaceAction != null && top.interfaceAction == intendedInterfaceAction
        if (isInterfaceIDValid && isInterfaceActionValid) top.accept()
        print()
        return isInterfaceIDValid && isInterfaceActionValid
    }

    private fun print() {
        val historyString = StringBuilder(javaClass.getSimpleName() + "\n")
        val iterator = history.listIterator(history.size)
        while (iterator.hasPrevious()) historyString.append(iterator.previous().toString())
        Logger.d(historyString.toString())
    }

    private inner class InterfaceRecord(
        var interfaceID: String,
        var interfaceAction: String,
        var initTimeMillis: Long
    ) {
        private var isAccepted = false

        fun accept() {
            this.isAccepted = true
        }

        @SuppressLint("SimpleDateFormat")
        override fun toString(): String {
            return SimpleDateFormat("HH:mm:ss:SSS").format(Date(this.initTimeMillis)) +
                    ":  " + this.interfaceAction + (if (this.isAccepted) " (Accepted)" else "") + "\t\t" + this.interfaceID + "\n"
        }
    }
}
