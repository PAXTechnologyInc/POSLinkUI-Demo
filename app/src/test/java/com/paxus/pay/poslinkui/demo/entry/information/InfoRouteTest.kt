package com.paxus.pay.poslinkui.demo.entry.information

import com.paxus.pay.poslinkui.demo.entry.compose.resolveVisaInstallmentEndContent
import org.junit.Assert.assertEquals
import org.junit.Test

class InfoRouteTest {

    @Test
    fun resolveVisaInstallmentEndContent_prefersMessage() {
        assertEquals("message", resolveVisaInstallmentEndContent(message = "message", title = "title"))
    }

    @Test
    fun resolveVisaInstallmentEndContent_fallsBackToTitle() {
        assertEquals("title", resolveVisaInstallmentEndContent(message = null, title = "title"))
    }
}
