package com.paxus.pay.poslinkui.demo.utils;

import com.pax.us.pay.ui.constant.entry.enumeration.AccountType;
import com.paxus.pay.poslinkui.demo.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Subsequent textual content for internationalization options can be added here.
 */
public class SelectOptionContent {
    public static final Map<String, Integer> SELECT_OPTION_MAP = new HashMap<>();

    static {
        SELECT_OPTION_MAP.put(AccountType.SAVING, R.string.acc_type_saving);
        SELECT_OPTION_MAP.put(AccountType.CHECKING, R.string.acc_type_checking);
    }

}
