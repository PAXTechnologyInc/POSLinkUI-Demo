package com.paxus.pay.poslinkui.demo.entry.confirmation

import com.paxus.pay.poslinkui.demo.entry.compose.resolveConfirmationSubmitValue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ConfirmationRouteTest {

    @Test
    fun resolveConfirmationSubmitValue_confirmBranch_returnsTrue() {
        assertTrue(resolveConfirmationSubmitValue(confirmed = true))
    }

    @Test
    fun resolveConfirmationSubmitValue_cancelBranch_returnsFalse() {
        assertFalse(resolveConfirmationSubmitValue(confirmed = false))
    }
}
