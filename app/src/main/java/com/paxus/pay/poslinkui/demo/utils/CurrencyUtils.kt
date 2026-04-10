package com.paxus.pay.poslinkui.demo.utils

import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import java.text.DecimalFormat

/**
 * Created by Yanina.Yang on 5/3/2022.
 * 
 * Utils for parse Amount
 */
object CurrencyUtils {
    val CURRENCY_SYMBOL_MAP: MutableMap<String?, String?> = hashMapOf(
        CurrencyType.CAD to "$",
        CurrencyType.EUR to "�",
        CurrencyType.GBP to "�",
        CurrencyType.USD to "$",
        CurrencyType.POINT to ""
    )

    fun convert(amount: Long, currencyType: String?): String {
        if (CurrencyType.POINT == currencyType) {
            //For POINT, don't nee prefix "USD".
            return DecimalFormat(",###").format(amount)
        }
        var currencySymbol = "$"
        if (CurrencyType.EUR == currencyType) {
            currencySymbol = "�"
        }
        return currencySymbol + DecimalFormat("0.00").format(amount / 100.0)
    }

    fun parse(formatterAmount: String?): Long {
        if (formatterAmount == null || formatterAmount.isEmpty()) {
            return 0
        }
        try {
            return formatterAmount.replace("[^0-9]".toRegex(), "").toLong()
        } catch (e: NumberFormatException) {
            return 0
        }
    }
}
