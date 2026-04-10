package com.paxus.pay.poslinkui.demo.entry

import android.os.Bundle
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.SecurityEntry
import com.paxus.pay.poslinkui.demo.viewmodel.EntryUiState

/**
 * CLSS 四灯是否在 ActionBar 显示。
 *
 * `golive/v1.03.00` [InputAccountFragment]：`ClssLightsViewStatusManager.activate(enableContactlessLight && enableTap)`。
 * 多数演示/回归 Intent **不会**带 [EntryExtraData.PARAM_ENABLE_CONTACTLESS_LIGHT]；若按缺省 `false` 则灯永远不出现。
 * 因此：**未显式传入该参数时视为开启**；仅当宿主显式传 `false` 时隐藏。
 */
object EntryActionBarPolicy {

    /**
     * Whether the four CLSS indicators should be visible in [com.paxus.pay.poslinkui.demo.utils.EntryActivityActionBar].
     */
    fun shouldShowClssLights(state: EntryUiState): Boolean {
        if (!state.categories.contains(SecurityEntry.CATEGORY)) return false
        if (state.entryAction != SecurityEntry.ACTION_INPUT_ACCOUNT) return false
        val ex = state.extras
        val enableTap = ex.readBooleanExtra(false, EntryExtraData.PARAM_ENABLE_TAP, "enableTap", "enableTAP")
        if (!enableTap) return false
        val enableLight = if (ex.hasEnableContactlessLightKey()) {
            ex.readBooleanExtra(
                false,
                EntryExtraData.PARAM_ENABLE_CONTACTLESS_LIGHT,
                "enableContactlessLight"
            )
        } else {
            true
        }
        return enableLight
    }

    private fun Bundle.hasEnableContactlessLightKey(): Boolean {
        if (containsKey(EntryExtraData.PARAM_ENABLE_CONTACTLESS_LIGHT)) return true
        return keySet().any { it.equals("enableContactlessLight", ignoreCase = true) }
    }

    private fun Bundle.readBooleanExtra(default: Boolean, vararg keys: String): Boolean {
        for (key in keys) {
            val raw = getCompatValue(key) ?: continue
            return when (raw) {
                is Boolean -> raw
                is Number -> raw.toInt() != 0
                is String -> {
                    val n = raw.trim()
                    n.equals("true", ignoreCase = true) ||
                        n == "1" ||
                        n.equals("yes", ignoreCase = true) ||
                        n.equals("y", ignoreCase = true)
                }
                else -> default
            }
        }
        return default
    }

    private fun Bundle.getCompatValue(key: String): Any? {
        if (containsKey(key)) return get(key)
        val actualKey = keySet().firstOrNull { it.equals(key, ignoreCase = true) } ?: return null
        return get(actualKey)
    }
}
