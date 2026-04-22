package com.paxus.pay.poslinkui.demo.entry

import android.content.Intent
import com.pax.us.pay.ui.constant.entry.ConfirmationEntry
import com.pax.us.pay.ui.constant.entry.InformationEntry
import com.pax.us.pay.ui.constant.entry.OptionEntry
import com.pax.us.pay.ui.constant.entry.PoslinkEntry
import com.pax.us.pay.ui.constant.entry.SecurityEntry
import com.pax.us.pay.ui.constant.entry.SignatureEntry
import com.pax.us.pay.ui.constant.entry.TextEntry
import com.paxus.pay.poslinkui.demo.utils.Logger

/**
 * Maps Entry [Intent] (action + categories) to a known route without Fragment types.
 * Replaces the former [UIFragmentHelper] class map for Compose-only Entry.
 */
object EntryActionRegistry {

    /**
     * Text-category actions accepted by [canResolveEntry].
     * Internal for unit tests that assert action → response param contracts.
     */
    internal val textEntryActions: Set<String> = buildSet {
        add(TextEntry.ACTION_ENTER_AMOUNT)
        add(TextEntry.ACTION_ENTER_FUEL_AMOUNT)
        add(TextEntry.ACTION_ENTER_TAX_AMOUNT)
        add(TextEntry.ACTION_ENTER_CASH_BACK)
        add(TextEntry.ACTION_ENTER_TIP)
        add(TextEntry.ACTION_ENTER_TOTAL_AMOUNT)
        add(TextEntry.ACTION_ENTER_CLERK_ID)
        add(TextEntry.ACTION_ENTER_SERVER_ID)
        add(TextEntry.ACTION_ENTER_TABLE_NUMBER)
        add(TextEntry.ACTION_ENTER_CS_PHONE_NUMBER)
        add(TextEntry.ACTION_ENTER_PHONE_NUMBER)
        add(TextEntry.ACTION_ENTER_GUEST_NUMBER)
        add(TextEntry.ACTION_ENTER_MERCHANT_TAX_ID)
        add(TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE)
        add(TextEntry.ACTION_ENTER_TRANS_NUMBER)
        add(TextEntry.ACTION_ENTER_ADDRESS)
        add(TextEntry.ACTION_ENTER_AUTH)
        add(TextEntry.ACTION_ENTER_CUSTOMER_CODE)
        add(TextEntry.ACTION_ENTER_ORDER_NUMBER)
        add(TextEntry.ACTION_ENTER_PO_NUMBER)
        add(TextEntry.ACTION_ENTER_PROD_DESC)
        add(TextEntry.ACTION_ENTER_ZIPCODE)
        add(TextEntry.ACTION_ENTER_DEST_ZIPCODE)
        add(TextEntry.ACTION_ENTER_INVOICE_NUMBER)
        add(TextEntry.ACTION_ENTER_VOUCHER_DATA)
        add(TextEntry.ACTION_ENTER_REFERENCE_NUMBER)
        add(TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER)
        add(TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER)
        add(TextEntry.ACTION_ENTER_AVS_DATA)
        add(TextEntry.ACTION_ENTER_EXPIRY_DATE)
        add(TextEntry.ACTION_ENTER_FSA_DATA)
        @Suppress("DEPRECATION")
        add(TextEntry.ACTION_ENTER_FLEET_DATA)
        add(TextEntry.ACTION_ENTER_ORIG_DATE)
        add(TextEntry.ACTION_ENTER_ORIGINAL_TRANSACTION_IDENTIFIER)
        add(TextEntry.ACTION_ENTER_TICKET_NUMBER)
        add(TextEntry.ACTION_ENTER_GLOBAL_UID)
        add(TextEntry.ACTION_ENTER_CUSTOMER_DATA)
        add(TextEntry.ACTION_ENTER_DEPARTMENT_NUMBER)
        add(TextEntry.ACTION_ENTER_DRIVER_ID)
        add(TextEntry.ACTION_ENTER_EMPLOYEE_NUMBER)
        add(TextEntry.ACTION_ENTER_FLEET_PROMPT_CODE)
        add(TextEntry.ACTION_ENTER_HUBOMETER)
        add(TextEntry.ACTION_ENTER_JOB_ID)
        add(TextEntry.ACTION_ENTER_LICENSE_NUMBER)
        add(TextEntry.ACTION_ENTER_MAINTENANCE_ID)
        add(TextEntry.ACTION_ENTER_ODOMETER)
        add(TextEntry.ACTION_ENTER_FLEET_PO_NUMBER)
        add(TextEntry.ACTION_ENTER_REEFER_HOURS)
        add(TextEntry.ACTION_ENTER_TRAILER_ID)
        add(TextEntry.ACTION_ENTER_TRIP_NUMBER)
        add(TextEntry.ACTION_ENTER_UNIT_ID)
        add(TextEntry.ACTION_ENTER_USER_ID)
        add(TextEntry.ACTION_ENTER_VEHICLE_ID)
        add(TextEntry.ACTION_ENTER_VEHICLE_NUMBER)
        add(TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_1)
        add(TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_2)
        add(TextEntry.ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID)
        add(TextEntry.ACTION_ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID)
    }

    /** Internal for instrumented smoke tests (synthetic [android.content.Intent] per action). */
    internal val securityEntryActions: Set<String> = buildSet {
        add(SecurityEntry.ACTION_INPUT_ACCOUNT)
        add(SecurityEntry.ACTION_MANAGE_INPUT_ACCOUNT)
        add(SecurityEntry.ACTION_ENTER_VCODE)
        add("com.pax.us.pay.action.ENTER_CVV")
        add(SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS)
        add(SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS)
        add(SecurityEntry.ACTION_ENTER_PIN)
        add(SecurityEntry.ACTION_ENTER_ADMINISTRATION_PASSWORD)
        add("com.pax.us.pay.action.ADMINISTRATOR_PASSWORD")
    }

    internal val informationEntryActions: Set<String> = buildSet {
        add(InformationEntry.ACTION_DISPLAY_TRANS_INFORMATION)
        add(InformationEntry.ACTION_DISPLAY_APPROVE_MESSAGE)
        add(EntryGapActions.ACTION_DISPLAY_VISA_INSTALLMENT_TRANSACTION_END)
    }

    internal val signatureEntryActions: Set<String> = buildSet {
        add(SignatureEntry.ACTION_SIGNATURE)
    }

    internal val confirmationEntryActions: Set<String> = buildSet {
        add(ConfirmationEntry.ACTION_CONFIRM_RECEIPT_VIEW)
        add(ConfirmationEntry.ACTION_DISPLAY_QR_CODE_RECEIPT)
        add(ConfirmationEntry.ACTION_CONFIRM_UNIFIED_MESSAGE)
        add(ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL)
        add(ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL)
        add(ConfirmationEntry.ACTION_CHECK_CARD_PRESENT)
        add(ConfirmationEntry.ACTION_CHECK_DEACTIVATE_WARN)
        add(ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE)
        add(ConfirmationEntry.ACTION_CONFIRM_UNTIPPED)
        add(ConfirmationEntry.ACTION_CONFIRM_DUPLICATE_TRANS)
        add(ConfirmationEntry.ACTION_CONFIRM_SURCHARGE_FEE)
        add(ConfirmationEntry.ACTION_CONFIRM_SERVICE_FEE)
        add(ConfirmationEntry.ACTION_CONFIRM_PRINTER_STATUS)
        add(ConfirmationEntry.ACTION_CONFIRM_UPLOAD_TRANS)
        add(ConfirmationEntry.ACTION_CONFIRM_PRINT_FAILED_TRANS)
        add(ConfirmationEntry.ACTION_CONFIRM_UPLOAD_RETRY)
        add(ConfirmationEntry.ACTION_CONFIRM_PRINT_FPS)
        add(ConfirmationEntry.ACTION_CONFIRM_DELETE_SF)
        add(ConfirmationEntry.ACTION_CONFIRM_PRINT_CUSTOMER_COPY)
        add(ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY)
        add(ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY_OFFLINE)
        add(ConfirmationEntry.ACTION_CONFIRM_ADJUST_TIP)
        add(ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT)
        add(ConfirmationEntry.ACTION_CONFIRM_RECEIPT_SIGNATURE)
        add(ConfirmationEntry.ACTION_CONFIRM_MERCHANT_SCOPE)
        add(ConfirmationEntry.ACTION_CONFIRM_CARD_ENTRY_RETRY)
        add(ConfirmationEntry.ACTION_CONFIRM_SIGNATURE_MATCH)
        add(ConfirmationEntry.ACTION_CONFIRM_DCC)
        add(ConfirmationEntry.ACTION_START_UI)
        add(ConfirmationEntry.ACTION_CONFIRM_BATCH_FOR_APPLICATION_UPDATE)
        add(ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE_WITH_INCOMPLETE_TRANSACTION)
        add(EntryGapActions.ACTION_CONFIRM_DEBIT_TRANS)
        add(ConfirmationEntry.ACTION_CONFIRM_TAX_AMOUNT)
        add(ConfirmationEntry.ACTION_CONFIRM_TOTAL_AMOUNT)
        add(ConfirmationEntry.ACTION_CONFIRM_BALANCE)
        add(ConfirmationEntry.ACTION_CONFIRM_CASH_PAYMENT)
    }

    internal val poslinkEntryActions: Set<String> = buildSet {
        add(PoslinkEntry.ACTION_INPUT_TEXT)
        add(PoslinkEntry.ACTION_SHOW_THANK_YOU)
        add(PoslinkEntry.ACTION_SHOW_DIALOG)
        add(PoslinkEntry.ACTION_SHOW_DIALOG_FORM)
        add(PoslinkEntry.ACTION_SHOW_MESSAGE)
        add(PoslinkEntry.ACTION_SHOW_ITEM)
        add(PoslinkEntry.ACTION_SHOW_SIGNATURE_BOX)
        add(PoslinkEntry.ACTION_SHOW_INPUT_TEXT_BOX)
        add(PoslinkEntry.ACTION_SHOW_TEXT_BOX)
    }

    internal val optionEntryActions: Set<String> = buildSet {
        add(OptionEntry.ACTION_SELECT_ORIG_CURRENCY)
        add(OptionEntry.ACTION_SELECT_MERCHANT)
        add(OptionEntry.ACTION_SELECT_LANGUAGE)
        add(OptionEntry.ACTION_SELECT_TIP_AMOUNT)
        add(OptionEntry.ACTION_SELECT_CASHBACK_AMOUNT)
        add(OptionEntry.ACTION_SELECT_INSTALLMENT_PLAN)
        add(OptionEntry.ACTION_SELECT_TRANS_FOR_ADJUST)
        add(OptionEntry.ACTION_SELECT_ACCOUNT_TYPE)
        add(OptionEntry.ACTION_SELECT_REPORT_TYPE)
        add(OptionEntry.ACTION_SELECT_EDC_GROUP)
        add(OptionEntry.ACTION_SELECT_BATCH_TYPE)
        add(OptionEntry.ACTION_SELECT_SEARCH_CRITERIA)
        add(OptionEntry.ACTION_SELECT_EDC_TYPE)
        add(OptionEntry.ACTION_SELECT_TRANS_TYPE)
        add(OptionEntry.ACTION_SELECT_CARD_TYPE)
        add(OptionEntry.ACTION_SELECT_DUPLICATE_OVERRIDE)
        add(OptionEntry.ACTION_SELECT_TAX_REASON)
        add(OptionEntry.ACTION_SELECT_MOTO_TYPE)
        add(OptionEntry.ACTION_SELECT_REFUND_REASON)
        add(OptionEntry.ACTION_SELECT_SUB_TRANS_TYPE)
        add(OptionEntry.ACTION_SELECT_AID)
        add(OptionEntry.ACTION_SELECT_BY_PASS)
        add(OptionEntry.ACTION_SELECT_EBT_TYPE)
        add(OptionEntry.ACTION_SELECT_COF_INITIATOR)
        add(OptionEntry.ACTION_SELECT_CASH_DISCOUNT)
        add(OptionEntry.ACTION_SELECT_BATCH_REPORT_TYPE)
        add(OptionEntry.ACTION_SELECT_CURRENCY)
    }

    fun canResolveEntry(intent: Intent): Boolean {
        val action = intent.action ?: return false
        val categories = intent.categories ?: return false
        val known = resolveAction(action, categories)
        if (!known) {
            Logger.w("Unsupported action or category for action=$action categories=$categories")
        }
        return known
    }

    /** For unit tests: same resolution as [canResolveEntry] without logging. */
    fun isKnownEntryAction(action: String, categories: Set<String>): Boolean =
        resolveAction(action, categories)

    private fun resolveAction(action: String, categories: Set<String>): Boolean = when {
        categories.contains(TextEntry.CATEGORY) -> action in textEntryActions
        categories.contains(SecurityEntry.CATEGORY) -> action in securityEntryActions
        categories.contains(InformationEntry.CATEGORY) -> action in informationEntryActions
        categories.contains(OptionEntry.CATEGORY) -> action in optionEntryActions
        categories.contains(SignatureEntry.CATEGORY) -> action in signatureEntryActions
        categories.contains(ConfirmationEntry.CATEGORY) -> action in confirmationEntryActions
        categories.contains(PoslinkEntry.CATEGORY) -> action in poslinkEntryActions
        else -> false
    }
}
