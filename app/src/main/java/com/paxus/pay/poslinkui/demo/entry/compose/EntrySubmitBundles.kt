package com.paxus.pay.poslinkui.demo.entry.compose

import android.os.Bundle

/**
 * Shared [Bundle] builders for Entry `sendNext` payloads (option index, confirmation flag, surcharge tri-state).
 *
 * Kept separate from UI routes so routers stay thin and unit tests can depend on stable bundle shape only.
 */
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.EntryRequest

internal fun buildOptionSubmitBundle(selectedIndex: Int, optionCount: Int): Bundle? {
    if (optionCount <= 0) return null
    val safeIndex = resolveOptionSubmitIndex(selectedIndex, optionCount)
    return Bundle().apply { putInt(EntryRequest.PARAM_INDEX, safeIndex) }
}

internal fun resolveOptionSubmitIndex(selectedIndex: Int, optionCount: Int): Int {
    require(optionCount > 0) { "optionCount must be greater than zero" }
    return selectedIndex.coerceIn(0, optionCount - 1)
}

internal fun buildConfirmationSubmitBundle(confirmed: Boolean): Bundle =
    Bundle().apply {
        putBoolean(EntryRequest.PARAM_CONFIRMED, resolveConfirmationSubmitValue(confirmed))
    }

/**
 * Surcharge confirmation tri-state (prototype: YES / NO / BYPASS FEE).
 *
 * Bypass sends [EntryRequest.PARAM_CONFIRMED]=false plus [EntryExtraData.PARAM_ENABLE_BYPASS]=true so the host
 * can distinguish decline from fee bypass when it supplied enable-bypass on entry.
 */
internal enum class SurchargeFeeSubmitChoice {
    Accept,
    Decline,
    Bypass
}

internal fun buildSurchargeFeeSubmitBundle(choice: SurchargeFeeSubmitChoice): Bundle = when (choice) {
    SurchargeFeeSubmitChoice.Accept -> buildConfirmationSubmitBundle(confirmed = true)
    SurchargeFeeSubmitChoice.Decline -> buildConfirmationSubmitBundle(confirmed = false)
    SurchargeFeeSubmitChoice.Bypass -> Bundle().apply {
        putBoolean(EntryRequest.PARAM_CONFIRMED, false)
        putBoolean(EntryExtraData.PARAM_ENABLE_BYPASS, true)
    }
}

internal fun resolveConfirmationSubmitValue(confirmed: Boolean): Boolean = confirmed
