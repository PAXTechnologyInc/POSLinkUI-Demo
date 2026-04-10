package com.paxus.pay.poslinkui.demo.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EntryActionStateStoreContractTest {
    private val store: EntryActionStateStore = InMemoryEntryActionStateStore()

    @Test
    fun set_thenGet_shouldReturnPersistedValue() {
        store.set(".TEXT.ENTER_AMOUNT", true)
        assertTrue(store.contains(".TEXT.ENTER_AMOUNT"))
        assertEquals(true, store.get(".TEXT.ENTER_AMOUNT"))
    }

    @Test
    fun blankAlias_shouldBeIgnored() {
        store.set("", true)
        assertFalse(store.contains(""))
        assertNull(store.get(""))
    }

    private class InMemoryEntryActionStateStore : EntryActionStateStore {
        private val map = linkedMapOf<String, Boolean>()

        override fun contains(alias: String?): Boolean {
            if (alias.isNullOrBlank()) return false
            return map.containsKey(alias)
        }

        override fun get(alias: String?): Boolean? {
            if (alias.isNullOrBlank()) return null
            return map[alias]
        }

        override fun set(alias: String?, enabled: Boolean) {
            if (alias.isNullOrBlank()) return
            map[alias] = enabled
        }
    }
}
