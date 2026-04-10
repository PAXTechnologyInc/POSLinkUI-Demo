package com.paxus.pay.poslinkui.demo.utils.interfacefilter

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry
import com.pax.us.pay.ui.constant.entry.InformationEntry
import com.pax.us.pay.ui.constant.entry.OptionEntry
import com.pax.us.pay.ui.constant.entry.PoslinkEntry
import com.pax.us.pay.ui.constant.entry.SecurityEntry
import com.pax.us.pay.ui.constant.entry.SignatureEntry
import com.pax.us.pay.ui.constant.entry.TextEntry

/**
 * It does not contain states. It is a repository for the list of categories and actions.
 * Do NOT use this directly.
 * Use [EntryActionFilterManager] instead.
 */
internal object EntryActionAndCategoryRepository {
    private val ENTRY_CATEGORY_LIST: MutableList<EntryCategory?> =
        object : ArrayList<EntryCategory?>() {
            init {
                add(EntryCategory(TextEntry.CATEGORY, "Text"))
                add(EntryCategory(SecurityEntry.CATEGORY, "Security"))
                add(EntryCategory(InformationEntry.CATEGORY, "Information"))
                add(EntryCategory(PoslinkEntry.CATEGORY, "POSLink"))
                add(EntryCategory(SignatureEntry.CATEGORY, "Signature"))
                add(EntryCategory(OptionEntry.CATEGORY, "Option"))
                add(EntryCategory(ConfirmationEntry.CATEGORY, "Confirmation"))
            }
        }

    fun getCategories(): MutableList<EntryCategory?> {
        return ArrayList<EntryCategory?>(ENTRY_CATEGORY_LIST)
    }

    private val ENTRY_ACTION_MAP: MutableMap<String?, EntryAction?> =
        HashMap<String?, EntryAction?>().apply {
            seedEntryActionsCatalogPartA()
            seedEntryActionsCatalogPartB()
        }

    fun getEntryAction(action: String?): EntryAction? {
        if (ENTRY_ACTION_MAP.containsKey(action)) return ENTRY_ACTION_MAP.get(action)
        return null
    }

    fun getEntryActionsByCategoryWithDefaultValues(category: String?): MutableList<String?> {
        val entryActions: MutableList<String?> = ArrayList<String?>()
        for (entry in ENTRY_ACTION_MAP.entries) {
            if (entry.value?.category == category) {
                entryActions.add(entry.key)
            }
        }
        return entryActions
    }
}