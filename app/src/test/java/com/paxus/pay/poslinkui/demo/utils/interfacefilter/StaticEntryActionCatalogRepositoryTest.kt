package com.paxus.pay.poslinkui.demo.utils.interfacefilter

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

class StaticEntryActionCatalogRepositoryTest {
    private val repository = StaticEntryActionCatalogRepository()

    @Test
    fun getCategories_shouldReturnNonEmptyCatalog() {
        val categories = repository.getCategories()
        assertFalse(categories.isEmpty())
    }

    @Test
    fun knownAction_shouldResolveEntryActionMetadata() {
        val action = repository.getEntryAction("com.pax.us.pay.action.ENTER_AMOUNT")
        assertNotNull(action)
    }
}
