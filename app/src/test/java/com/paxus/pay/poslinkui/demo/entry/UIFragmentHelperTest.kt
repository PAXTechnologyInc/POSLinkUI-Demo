package com.paxus.pay.poslinkui.demo.entry

import org.junit.Assert.assertFalse
import org.junit.Test

class UIFragmentHelperTest {
    @Test
    fun resolveFragment_returnsNullForUnsupportedCategory() {
        val result = EntryActionRegistry.isKnownEntryAction("UNKNOWN_ACTION", setOf("UNKNOWN_CATEGORY"))
        assertFalse(result)
    }
}
