package com.paxus.pay.poslinkui.demo.entry.compose

import androidx.compose.runtime.compositionLocalOf

/**
 * After [com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel.sendNext], entry screens should disable interactive
 * controls (legacy `EditabilityBlocker`) until the Manager sends [com.pax.us.pay.ui.constant.entry.EntryResponse.ACTION_DECLINED].
 * [EntryResponse.ACTION_ACCEPTED] leaves controls disabled.
 */
val LocalEntryInteractionLocked = compositionLocalOf { false }
