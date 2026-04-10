package com.paxus.pay.poslinkui.demo.entry.confirmation

import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import com.pax.us.pay.ui.constant.entry.ConfirmationEntry
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import com.pax.us.pay.ui.constant.entry.enumeration.PrintStatusType
import com.pax.us.pay.ui.constant.status.StatusData
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.EntryGapActions
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils

/**
 * Reproduces legacy [AConfirmationFragment] + per-action [formatMessage] / button behavior for Compose routing.
 *
 * @param message Resolved body text for [ConfirmationScreen].
 * @param positiveText When non-null, overrides [EntryExtraData.PARAM_OPTIONS] index 0 and default accept string.
 * @param negativeText When non-null, used as cancel label. When [hideNegativeButton] is true, cancel is hidden.
 * @param hideNegativeButton Legacy [ConfirmCardProcessResultFragment] (single YES + timeout).
 */
data class ConfirmationPresentation(
    val message: String,
    val positiveText: String? = null,
    val negativeText: String? = null,
    val hideNegativeButton: Boolean = false,
    val autoConfirmAfterMs: Long? = null
)

/**
 * Builds confirmation UI text and overrides from [Intent] extras, matching pre-Compose Fragment logic (git d8cb92c).
 */
object ConfirmationMessageFormatter {

    private const val DEFAULT_CARD_PROCESS_TIMEOUT_MS = 30_000L

    private fun presentationFromParamOrStringRes(
        paramMessage: String?,
        res: Resources,
        stringRes: Int
    ): ConfirmationPresentation = ConfirmationPresentation(
        message = if (!TextUtils.isEmpty(paramMessage)) paramMessage.orEmpty() else res.getString(stringRes)
    )

    private fun confirmBalancePresentation(extras: Bundle, res: Resources): ConfirmationPresentation {
        val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, "USD")
        val balance = extras.getLong(EntryExtraData.PARAM_BALANCE, 0L)
        return ConfirmationPresentation(
            message = res.getString(
                R.string.confirm_balance_message,
                CurrencyUtils.convert(balance, currency)
            )
        )
    }

    private fun confirmCashPaymentPresentation(
        paramMessage: String?,
        extras: Bundle,
        res: Resources
    ): ConfirmationPresentation {
        val totalAmount = extras.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT)
        val currency = extras.getString(EntryExtraData.PARAM_CURRENCY)
        return ConfirmationPresentation(
            message = if (paramMessage != null) {
                paramMessage
            } else {
                res.getString(
                    R.string.confirm_cash_payment_message,
                    CurrencyUtils.convert(totalAmount, currency)
                )
            }
        )
    }

    private fun confirmTotalAmountPresentation(extras: Bundle, res: Resources): ConfirmationPresentation {
        val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, "USD")
        val totalAmount = extras.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT, 0L)
        return ConfirmationPresentation(
            message = res.getString(
                R.string.confirm_total_amount_message,
                CurrencyUtils.convert(totalAmount, currency)
            )
        )
    }

    private fun confirmCardProcessResultPresentation(
        paramMessage: String?,
        extras: Bundle
    ): ConfirmationPresentation {
        val timeout = extras.getLong(EntryExtraData.PARAM_TIMEOUT, DEFAULT_CARD_PROCESS_TIMEOUT_MS)
        return ConfirmationPresentation(
            message = paramMessage.orEmpty(),
            hideNegativeButton = true,
            autoConfirmAfterMs = timeout
        )
    }

    fun build(action: String?, extras: Bundle, res: Resources): ConfirmationPresentation {
        val paramMessage = extras.getString(EntryExtraData.PARAM_MESSAGE)
        return presentationForConfirmActionsBatchAndAmounts(action, paramMessage, extras, res)
            ?: presentationForConfirmActionsUploadAndChecks(action, paramMessage, extras, res)
            ?: ConfirmationPresentation(message = paramMessage.orEmpty())
    }

    /**
     * First half of confirmation actions (batch/amount/print-related) to keep each `when` within branch limits.
     */
    private fun presentationForConfirmActionsBatchAndAmounts(
        action: String?,
        paramMessage: String?,
        extras: Bundle,
        res: Resources
    ): ConfirmationPresentation? = when (action) {
        ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.confirm_batch_close)
        ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE_WITH_INCOMPLETE_TRANSACTION ->
            presentationFromParamOrStringRes(
                paramMessage,
                res,
                R.string.confirm_batch_close_with_incomplete_transaction
            )
        ConfirmationEntry.ACTION_CONFIRM_BATCH_FOR_APPLICATION_UPDATE ->
            presentationFromParamOrStringRes(
                paramMessage,
                res,
                R.string.confirm_application_update_with_batch_close
            )
        EntryGapActions.ACTION_CONFIRM_DEBIT_TRANS ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.confirm_debit_trans)
        ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY_OFFLINE ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.confirm_online_retry_offline)
        ConfirmationEntry.ACTION_CONFIRM_TAX_AMOUNT ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.enter_tax_amount_again)
        ConfirmationEntry.ACTION_CONFIRM_UNTIPPED ->
            ConfirmationPresentation(message = res.getString(R.string.confirm_untipped_close))
        ConfirmationEntry.ACTION_CONFIRM_BALANCE -> confirmBalancePresentation(extras, res)
        ConfirmationEntry.ACTION_CONFIRM_CASH_PAYMENT ->
            confirmCashPaymentPresentation(paramMessage, extras, res)
        ConfirmationEntry.ACTION_CONFIRM_DCC -> buildDccPresentation(extras)
        ConfirmationEntry.ACTION_CONFIRM_PRINT_FAILED_TRANS ->
            ConfirmationPresentation(message = res.getString(R.string.confirm_print_failed_trans))
        ConfirmationEntry.ACTION_CONFIRM_PRINT_FPS ->
            ConfirmationPresentation(message = res.getString(R.string.confirm_print_fps))
        ConfirmationEntry.ACTION_CONFIRM_PRINTER_STATUS ->
            ConfirmationPresentation(message = printerStatusMessage(extras, res))
        ConfirmationEntry.ACTION_CONFIRM_RECEIPT_SIGNATURE ->
            ConfirmationPresentation(message = res.getString(R.string.confirm_receipt_signature))
        ConfirmationEntry.ACTION_CONFIRM_SIGNATURE_MATCH ->
            ConfirmationPresentation(message = res.getString(R.string.confirm_signature_match))
        ConfirmationEntry.ACTION_CONFIRM_TOTAL_AMOUNT -> confirmTotalAmountPresentation(extras, res)
        else -> null
    }

    /**
     * Remaining confirmation actions (upload, checks, partial approval, etc.).
     */
    private fun presentationForConfirmActionsUploadAndChecks(
        action: String?,
        paramMessage: String?,
        extras: Bundle,
        res: Resources
    ): ConfirmationPresentation? = when (action) {
        ConfirmationEntry.ACTION_CONFIRM_UPLOAD_RETRY ->
            ConfirmationPresentation(message = res.getString(R.string.confirm_upload_retry))
        ConfirmationEntry.ACTION_CONFIRM_UPLOAD_TRANS ->
            ConfirmationPresentation(message = res.getString(R.string.confirm_upload_trans))
        ConfirmationEntry.ACTION_CHECK_CARD_PRESENT ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.check_card_present)
        ConfirmationEntry.ACTION_CHECK_DEACTIVATE_WARN ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.confirm_deactivate_warn)
        ConfirmationEntry.ACTION_CONFIRM_ADJUST_TIP ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.confirm_adjust_tip)
        ConfirmationEntry.ACTION_CONFIRM_CARD_ENTRY_RETRY ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.confirm_card_entry_retry)
        ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT ->
            confirmCardProcessResultPresentation(paramMessage, extras)
        ConfirmationEntry.ACTION_CONFIRM_DELETE_SF ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.confirm_delete_sf)
        ConfirmationEntry.ACTION_CONFIRM_DUPLICATE_TRANS ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.prompt_confirm_dup_transaction)
        ConfirmationEntry.ACTION_CONFIRM_MERCHANT_SCOPE ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.confirm_merchant_scope)
        ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.confirm_online_retry)
        ConfirmationEntry.ACTION_CONFIRM_PRINT_CUSTOMER_COPY ->
            presentationFromParamOrStringRes(paramMessage, res, R.string.confirm_print_customer_copy)
        ConfirmationEntry.ACTION_CONFIRM_UNIFIED_MESSAGE ->
            ConfirmationPresentation(message = paramMessage.orEmpty())
        ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL ->
            buildPartialApprovalPresentation(extras, res, reverse = true)
        ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL ->
            buildPartialApprovalPresentation(extras, res, reverse = false)
        else -> null
    }

    private fun buildPartialApprovalPresentation(
        extras: Bundle,
        res: Resources,
        reverse: Boolean
    ): ConfirmationPresentation {
        val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
        val approvedAmt = extras.getLong(EntryExtraData.PARAM_APPROVED_AMOUNT)
        val total = extras.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT)
        val due = total - approvedAmt
        val approvedStr = CurrencyUtils.convert(approvedAmt, currency)
        val dueStr = CurrencyUtils.convert(due, currency)
        val msg = if (reverse) {
            res.getString(R.string.select_reverse_partial, approvedStr, dueStr)
        } else {
            res.getString(R.string.select_supplement_partial, approvedStr, dueStr)
        }
        return ConfirmationPresentation(message = msg)
    }

    private fun printerStatusMessage(extras: Bundle, res: Resources): String {
        val printStatus = extras.getString(EntryExtraData.PARAM_PRINT_STATUS)
        return when (printStatus) {
            PrintStatusType.PRINTER_OUT_OF_PAPER -> res.getString(R.string.prompt_printer_out_of_paper)
            PrintStatusType.PRINTER_HOT -> res.getString(R.string.confirm_printer_over_hot)
            PrintStatusType.PRINTER_VOLTAGE_TOO_LOW -> res.getString(R.string.confirm_printer_voltage_low)
            else -> res.getString(R.string.confirm_printer_status)
        }
    }

    private fun buildDccPresentation(extras: Bundle): ConfirmationPresentation {
        val amountMessage = extras.getString(EntryExtraData.PARAM_AMOUNT_MESSAGE)
        val exchangeRate = extras.getString(EntryExtraData.PARAM_EXCHANGE_RATE)
        val currencyAlfCode = extras.getString(EntryExtraData.PARAM_CURRENCY_ALPHA_CODE)
        val margin = extras.getString(EntryExtraData.PARAM_MARGIN)
        val foreignAmountMessage = extras.getString(EntryExtraData.PARAM_FOREIGN_AMOUNT_MESSAGE)
        val confirmWithCurrency = extras.getBoolean(EntryExtraData.PARAM_CONFIRM_WITH_CURRENCY)
        val contentMsg = StringBuilder()
        if (!TextUtils.isEmpty(amountMessage)) {
            contentMsg.append("USD ").append(amountMessage).append("\n")
        }
        if (!TextUtils.isEmpty(exchangeRate) && !TextUtils.isEmpty(currencyAlfCode)) {
            contentMsg.append("1 USD = ").append(exchangeRate).append(" ").append(currencyAlfCode)
                .append("\n")
        }
        if (!TextUtils.isEmpty(margin)) {
            contentMsg.append("Int'l Margin ").append(margin).append("%\n")
        }
        if (!TextUtils.isEmpty(foreignAmountMessage) && !TextUtils.isEmpty(currencyAlfCode)) {
            contentMsg.append(currencyAlfCode).append(" ").append(foreignAmountMessage)
        }
        val pos = if (confirmWithCurrency && !TextUtils.isEmpty(currencyAlfCode)) "USD" else "Agree"
        val neg = if (confirmWithCurrency && !TextUtils.isEmpty(currencyAlfCode)) {
            currencyAlfCode.orEmpty()
        } else {
            "Cancel"
        }
        return ConfirmationPresentation(
            message = contentMsg.toString(),
            positiveText = pos,
            negativeText = neg
        )
    }

    /**
     * Secondary display line for partial-approval flows (legacy [ReversePartialApprovalFragment] / [SupplementPartialApprovalFragment]).
     */
    fun partialApprovalStatusTitle(extras: Bundle): String =
        extras.getString(StatusData.PARAM_MSG_PRIMARY, "")
}
