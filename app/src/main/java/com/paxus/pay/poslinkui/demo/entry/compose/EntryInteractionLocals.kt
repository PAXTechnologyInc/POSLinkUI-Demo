package com.paxus.pay.poslinkui.demo.entry.compose

import android.view.KeyEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow


/**
 * After [com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel.sendNext], entry screens should disable interactive
 * controls (legacy `EditabilityBlocker`) until the Manager sends [com.pax.us.pay.ui.constant.entry.EntryResponse.ACTION_DECLINED].
 * [EntryResponse.ACTION_ACCEPTED] leaves controls disabled.
 */
val LocalEntryInteractionLocked = compositionLocalOf { false }

/**
 * Hardware key events forwarded from [com.paxus.pay.poslinkui.demo.entry.EntryActivity] so Compose
 * Entry screens can match legacy [com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment] confirm behavior.
 */
val LocalEntryHardwareKeyEvents = compositionLocalOf<Flow<Int>?> { null }

@Composable
fun EntryHardwareConfirmEffect(
    enabled: Boolean = true,
    onConfirm: () -> Unit
) {
    val keyEvents = LocalEntryHardwareKeyEvents.current ?: return
    val latestEnabled = rememberUpdatedState(enabled)
    val latestOnConfirm = rememberUpdatedState(onConfirm)

    LaunchedEffect(keyEvents) {
        keyEvents.collect { keyCode ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && latestEnabled.value) {
                latestOnConfirm.value()
            }
        }
    }
}
