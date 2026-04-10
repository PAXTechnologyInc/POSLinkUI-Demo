package com.paxus.pay.poslinkui.demo.entry.compose

import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.PoslinkEntry
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Poslink Entry screens that collect a typed line use [EntryRequest.PARAM_INPUT_VALUE] in the
 * `sendNext` bundle (see `PoslinkEntryRoute` branches for [PoslinkEntry.ACTION_INPUT_TEXT] and
 * [PoslinkEntry.ACTION_SHOW_INPUT_TEXT_BOX]). [PoslinkEntry.ACTION_SHOW_TEXT_BOX] uses button keys instead.
 */
class PoslinkInputValueResponseContractTest {

    @Test
    fun paramInputValue_isNonBlankForHostContract() {
        assertTrue(EntryRequest.PARAM_INPUT_VALUE.isNotBlank())
    }

    @Test
    fun poslinkActions_documentedAsUsingInputValue_areDefined() {
        val actions = listOf(
            PoslinkEntry.ACTION_INPUT_TEXT,
            PoslinkEntry.ACTION_SHOW_INPUT_TEXT_BOX
        )
        actions.forEach { assertTrue(it.isNotBlank()) }
    }
}
