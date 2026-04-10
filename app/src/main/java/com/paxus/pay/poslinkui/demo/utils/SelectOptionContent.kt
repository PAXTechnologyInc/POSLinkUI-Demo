package com.paxus.pay.poslinkui.demo.utils

import com.pax.us.pay.ui.constant.entry.enumeration.AccountType
import com.paxus.pay.poslinkui.demo.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Subsequent textual content for internationalization options can be added here.
 */
@Singleton
class SelectOptionContent @Inject constructor() {
    val SELECT_OPTION_MAP: MutableMap<String?, Int?> = HashMap<String?, Int?>()

    init {
        SELECT_OPTION_MAP.put(AccountType.SAVING, R.string.acc_type_saving)
        SELECT_OPTION_MAP.put(AccountType.CHECKING, R.string.acc_type_checking)
    }
}
