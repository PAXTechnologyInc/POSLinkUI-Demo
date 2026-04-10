package com.paxus.pay.poslinkui.demo.entry.option

import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.OptionEntry
import com.paxus.pay.poslinkui.demo.R

/**
 * Titles for Option Entry (legacy [AOptionEntryFragment.formatTitle], git d8cb92c).
 *
 * [OptionEntry.ACTION_SELECT_CURRENCY]: when [EntryExtraData.PARAM_TITLE_MESSAGE] is non-empty,
 * the old XML screen used it as the main title; Compose shows the same as the primary headline.
 */
object OptionEntryMessageFormatter {

    fun title(action: String?, extras: Bundle, res: Resources): String {
        if (action == null) return hostTitle(extras)
        if (action == OptionEntry.ACTION_SELECT_CURRENCY) {
            val tm = extras.getString(EntryExtraData.PARAM_TITLE_MESSAGE)
            if (!TextUtils.isEmpty(tm)) return tm.orEmpty()
        }
        return when (action) {
            OptionEntry.ACTION_SELECT_ORIG_CURRENCY -> res.getString(R.string.select_orig_currency)
            OptionEntry.ACTION_SELECT_MERCHANT -> res.getString(R.string.select_merchant)
            OptionEntry.ACTION_SELECT_LANGUAGE -> res.getString(R.string.select_language)
            OptionEntry.ACTION_SELECT_TIP_AMOUNT -> res.getString(R.string.select_tip_amount)
            OptionEntry.ACTION_SELECT_CASHBACK_AMOUNT -> res.getString(R.string.select_cashback_amount)
            OptionEntry.ACTION_SELECT_INSTALLMENT_PLAN -> res.getString(R.string.select_installment_plan)
            OptionEntry.ACTION_SELECT_TRANS_FOR_ADJUST -> res.getString(R.string.select_trans_for_adjust)
            OptionEntry.ACTION_SELECT_ACCOUNT_TYPE -> res.getString(R.string.select_account_type)
            OptionEntry.ACTION_SELECT_REPORT_TYPE -> res.getString(R.string.select_report_type)
            OptionEntry.ACTION_SELECT_EDC_GROUP -> res.getString(R.string.select_edc_type)
            OptionEntry.ACTION_SELECT_BATCH_TYPE -> res.getString(R.string.select_batch_menu)
            OptionEntry.ACTION_SELECT_SEARCH_CRITERIA -> res.getString(R.string.select_search_type)
            OptionEntry.ACTION_SELECT_EDC_TYPE -> res.getString(R.string.select_edc_type)
            OptionEntry.ACTION_SELECT_TRANS_TYPE -> res.getString(R.string.select_trans_type)
            OptionEntry.ACTION_SELECT_CARD_TYPE -> res.getString(R.string.select_card_type)
            OptionEntry.ACTION_SELECT_DUPLICATE_OVERRIDE -> res.getString(R.string.select_duplicate_override)
            OptionEntry.ACTION_SELECT_TAX_REASON -> res.getString(R.string.select_tax_reason)
            OptionEntry.ACTION_SELECT_MOTO_TYPE -> res.getString(R.string.select_moto_type)
            OptionEntry.ACTION_SELECT_REFUND_REASON -> res.getString(R.string.select_refund_reason)
            OptionEntry.ACTION_SELECT_SUB_TRANS_TYPE -> res.getString(R.string.select_sub_trans_type)
            OptionEntry.ACTION_SELECT_AID -> res.getString(R.string.select_emv_aid)
            OptionEntry.ACTION_SELECT_BY_PASS -> res.getString(R.string.select_bypass_reason)
            OptionEntry.ACTION_SELECT_EBT_TYPE -> res.getString(R.string.select_ebt_type)
            OptionEntry.ACTION_SELECT_COF_INITIATOR -> res.getString(R.string.select_cof_initiator)
            OptionEntry.ACTION_SELECT_CASH_DISCOUNT -> res.getString(R.string.select_cash_discount)
            OptionEntry.ACTION_SELECT_BATCH_REPORT_TYPE -> res.getString(R.string.select_batch_report_type)
            OptionEntry.ACTION_SELECT_CURRENCY -> res.getString(R.string.select_currency)
            else -> hostTitle(extras)
        }
    }

    private fun hostTitle(extras: Bundle): String {
        val t = extras.getString(EntryExtraData.PARAM_TITLE)
        if (!TextUtils.isEmpty(t)) return t.orEmpty()
        val m = extras.getString(EntryExtraData.PARAM_MESSAGE)
        if (!TextUtils.isEmpty(m)) return m.orEmpty()
        return ""
    }
}
