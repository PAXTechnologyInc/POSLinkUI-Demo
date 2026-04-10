package com.paxus.pay.poslinkui.demo.utils.interfacefilter

/**
 * Read-only catalog abstraction for Entry categories and action metadata.
 */
interface EntryActionCatalogRepository {
    fun getCategories(): MutableList<EntryCategory?>
    fun getEntryActionsByCategoryWithDefaultValues(category: String?): MutableList<String?>
    fun getEntryAction(action: String?): EntryAction?
}
