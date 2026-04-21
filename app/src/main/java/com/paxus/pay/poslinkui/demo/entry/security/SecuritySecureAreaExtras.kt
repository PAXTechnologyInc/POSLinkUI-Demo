package com.paxus.pay.poslinkui.demo.entry.security

import android.os.Bundle
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils

@Suppress("DEPRECATION")
internal fun Bundle.getCompatValue(key: String): Any? {
    if (containsKey(key)) return get(key)
    val actualKey = keySet().firstOrNull { it.equals(key, ignoreCase = true) } ?: return null
    return get(actualKey)
}

internal fun Bundle.getStringCompat(primaryKey: String, vararg aliases: String): String? {
    val keys = buildList {
        add(primaryKey)
        addAll(aliases)
    }
    for (key in keys) {
        val raw = getCompatValue(key) ?: continue
        if (raw is String) return raw
    }
    return null
}

internal fun Bundle.getBooleanCompat(primaryKey: String, default: Boolean, vararg aliases: String): Boolean {
    val keys = buildList {
        add(primaryKey)
        addAll(aliases)
    }
    for (key in keys) {
        val raw = getCompatValue(key) ?: continue
        return when (raw) {
            is Boolean -> raw
            is Number -> raw.toInt() != 0
            is String -> {
                val normalized = raw.trim()
                normalized.equals("true", ignoreCase = true) ||
                    normalized == "1" ||
                    normalized.equals("yes", ignoreCase = true) ||
                    normalized.equals("y", ignoreCase = true)
            }
            else -> default
        }
    }
    return default
}

internal fun isCreditSaleTransType(extras: Bundle): Boolean {
    val raw = extras.getStringCompat(
        EntryExtraData.PARAM_TRANS_TYPE,
        "transType",
        "transactionType"
    ).orEmpty()
    val normalized = raw
        .trim()
        .uppercase()
        .replace("\\", "")
        .replace(Regex("[\\s-]+"), "_")
    return normalized == "CREDIT_SALE"
}

internal fun resolveInputAccountTotalAmount(extras: Bundle): String? {
    val totalAmount = when (val raw = extras.getCompatValue(EntryExtraData.PARAM_TOTAL_AMOUNT)
        ?: extras.getCompatValue("totalAmount")) {
        is Long -> raw
        is Int -> raw.toLong()
        is String -> raw.toLongOrNull()
        else -> null
    } ?: return null
    val currency = (extras.getStringCompat(EntryExtraData.PARAM_CURRENCY, "currency")
        ?: CurrencyType.USD)
    return CurrencyUtils.convert(totalAmount, currency)
}

internal fun canBypassPin(extras: Bundle): Boolean {
    val pinRange = extras.getStringCompat(EntryExtraData.PARAM_PIN_RANGE, "pinRange").orEmpty()
    return pinRange.startsWith("0,")
}

internal fun isLikelyInputAccountCreditSalePrompt(
    message: String,
    totalAmountText: String?,
    enableInsert: Boolean,
    enableTap: Boolean,
    enableSwipe: Boolean,
    enableManual: Boolean
): Boolean {
    if (totalAmountText == null) return false
    if (!message.contains("card number", ignoreCase = true)) return false
    return listOf(enableInsert, enableTap, enableSwipe, enableManual).all { it }
}

internal fun isInputAccountCreditSaleMainCase(
    extras: Bundle,
    enableInsert: Boolean,
    enableTap: Boolean,
    enableSwipe: Boolean,
    enableManual: Boolean
): Boolean {
    if (!isCreditSaleTransType(extras)) return false
    return listOf(enableInsert, enableTap, enableSwipe, enableManual).all { it }
}
