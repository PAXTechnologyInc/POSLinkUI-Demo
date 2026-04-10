package com.paxus.pay.poslinkui.demo.utils

import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Toast(@Suppress("UNUSED_PARAMETER") private val activity: FragmentActivity) {
    enum class TYPE {
        SUCCESS, FAILURE
    }

    fun show(message: String?, type: TYPE?) {
        if (message.isNullOrEmpty()) return
        ToastCenter.show(message, type ?: TYPE.SUCCESS)
    }

    companion object {
        private const val DURATION_DEFAULT: Long = 2500

        fun durationMs(): Long = DURATION_DEFAULT
    }
}

data class ToastMessage(
    val message: String,
    val type: Toast.TYPE
)

object ToastCenter {
    private val eventsFlow = MutableSharedFlow<ToastMessage>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val events = eventsFlow.asSharedFlow()

    fun show(message: String, type: Toast.TYPE) {
        eventsFlow.tryEmit(ToastMessage(message = message, type = type))
    }
}
