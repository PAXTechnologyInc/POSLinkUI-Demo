package com.paxus.pay.poslinkui.demo.entry.security

import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.SecurityEntry
import com.pax.us.pay.ui.constant.entry.enumeration.AdminPasswordType
import com.pax.us.pay.ui.constant.entry.enumeration.PanStyles
import com.pax.us.pay.ui.constant.entry.enumeration.PinStyles
import com.pax.us.pay.ui.constant.entry.enumeration.VCodeName
import com.paxus.pay.poslinkui.demo.R

/**
 * Security prompts from legacy `ASecurityFragment` / `PINFragment` / `AdministratorPasswordFragment` (git d8cb92c).
 */
object SecurityMessageFormatter {

    fun prompt(action: String?, extras: Bundle, res: Resources): String {
        if (action == null) return hostMessage(extras)
        return when (action) {
            SecurityEntry.ACTION_ENTER_PIN -> pinPrompt(extras, res)
            SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS ->
                res.getString(R.string.prompt_input_all_digit)
            SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS ->
                res.getString(R.string.prompt_input_4digit)
            SecurityEntry.ACTION_INPUT_ACCOUNT -> inputAccountPrompt(extras, res)
            SecurityEntry.ACTION_MANAGE_INPUT_ACCOUNT ->
                res.getString(R.string.hint_enter_account)
            SecurityEntry.ACTION_ENTER_ADMINISTRATION_PASSWORD -> adminPasswordPrompt(extras, res)
            else -> {
                if (isVcodeSecurityAction(action)) {
                    vcodePrompt(extras, res)
                } else {
                    hostMessage(extras)
                }
            }
        }
    }

    private fun hostMessage(extras: Bundle): String {
        val m = extras.getString(EntryExtraData.PARAM_MESSAGE)
        if (!TextUtils.isEmpty(m)) return m.orEmpty()
        return extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    }

    private fun pinPrompt(extras: Bundle, res: Resources): String {
        val pinStyle = extras.getString(EntryExtraData.PARAM_PIN_STYLES, PinStyles.NORMAL)
        val isOnline = extras.getBoolean(EntryExtraData.PARAM_IS_ONLINE_PIN, true)
        return when (pinStyle) {
            PinStyles.RETRY -> res.getString(R.string.pls_input_pin_again)
            PinStyles.LAST -> res.getString(R.string.pls_input_pin_last)
            else -> res.getString(
                if (isOnline) R.string.prompt_pin else R.string.prompt_offline_pin
            )
        }
    }

    private fun vcodePrompt(extras: Bundle, res: Resources): String {
        val vcodeName = extras.getString(EntryExtraData.PARAM_VCODE_NAME)
        if (TextUtils.isEmpty(vcodeName)) return res.getString(R.string.pls_input_vcode)
        return when (vcodeName) {
            VCodeName.CVV2 -> res.getString(R.string.pls_input_cvv2)
            VCodeName.CAV2 -> res.getString(R.string.pls_input_cav2)
            VCodeName.CID -> res.getString(R.string.pls_input_cid)
            else -> vcodeName.orEmpty()
        }
    }

    private fun inputAccountPrompt(extras: Bundle, res: Resources): String {
        val panStyle = extras.getString(EntryExtraData.PARAM_PAN_STYLES, PanStyles.NORMAL)
        return if (PanStyles.NEW_PAN == panStyle) {
            res.getString(R.string.enter_new_account)
        } else {
            res.getString(R.string.hint_enter_account)
        }
    }

    private fun adminPasswordPrompt(extras: Bundle, res: Resources): String {
        val merchantName = extras.getString(EntryExtraData.PARAM_MERCHANT_NAME).orEmpty()
        val adminRole = extras.getString(EntryExtraData.PARAM_ADMIN_PASSWORD_TYPE)
        if (AdminPasswordType.MANAGER == adminRole) {
            return res.getString(R.string.prompt_manager_pwd)
        }
        if (AdminPasswordType.OPERATOR == adminRole) {
            return if (merchantName.isNotEmpty()) {
                res.getString(R.string.prompt_merchant_pwd, merchantName)
            } else {
                res.getString(R.string.prompt_operator_pwd)
            }
        }
        if (merchantName.isNotEmpty()) {
            return res.getString(R.string.prompt_merchant_pwd, merchantName)
        }
        return res.getString(R.string.enter_pwd)
    }
}
