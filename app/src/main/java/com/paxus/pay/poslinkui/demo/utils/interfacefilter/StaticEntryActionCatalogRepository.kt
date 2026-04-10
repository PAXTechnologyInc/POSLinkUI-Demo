package com.paxus.pay.poslinkui.demo.utils.interfacefilter

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Adapter that keeps the existing static action map while exposing an injectable repository.
 */
@Singleton
class StaticEntryActionCatalogRepository @Inject constructor() : EntryActionCatalogRepository {
    override fun getCategories(): MutableList<EntryCategory?> {
        return EntryActionAndCategoryRepository.getCategories()
    }

    override fun getEntryActionsByCategoryWithDefaultValues(category: String?): MutableList<String?> {
        return EntryActionAndCategoryRepository.getEntryActionsByCategoryWithDefaultValues(category)
    }

    override fun getEntryAction(action: String?): EntryAction? {
        return EntryActionAndCategoryRepository.getEntryAction(action)
    }
}
