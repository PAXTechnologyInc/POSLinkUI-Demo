package com.paxus.pay.poslinkui.demo.entry.navigation

import com.paxus.pay.poslinkui.demo.entry.compose.buildOptionSubmitBundle
import com.paxus.pay.poslinkui.demo.entry.compose.resolveOptionSubmitIndex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EntryRouteResolverTest {

    @Test
    fun resolveStartRoute_defaults_to_entry_main() {
        assertEquals(TransactionRoute.EntryMain, EntryRouteResolver.resolveStartRoute(false))
    }

    @Test
    fun resolveStartRoute_compose_demo_extra() {
        assertEquals(TransactionRoute.ComposeDemo, EntryRouteResolver.resolveStartRoute(true))
    }

    @Test
    fun entryContentFingerprint_includes_revision() {
        val a = entryContentFingerprint("test.action", 0, null)
        val b = entryContentFingerprint("test.action", 1, null)
        assertTrue(a.contains("|rev=0|"))
        assertTrue(b.contains("|rev=1|"))
    }

    @Test
    fun resolveOptionSubmitIndex_clampsOutOfRangeIndex() {
        assertEquals(1, resolveOptionSubmitIndex(selectedIndex = 99, optionCount = 2))
        assertEquals(0, resolveOptionSubmitIndex(selectedIndex = -3, optionCount = 2))
    }

    @Test
    fun buildOptionSubmitBundle_emptyOptions_returnsNull() {
        assertNull(buildOptionSubmitBundle(selectedIndex = 0, optionCount = 0))
    }
}
