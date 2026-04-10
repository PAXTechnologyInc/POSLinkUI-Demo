package com.paxus.pay.poslinkui.demo.entry.text

import android.content.res.Resources
import android.os.Bundle
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.TextEntry
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import com.pax.us.pay.ui.constant.entry.enumeration.FSAAmountType
import com.paxus.pay.poslinkui.demo.R

/**
 * Legacy text prompts from pre-Compose `*Fragment.formatMessage()` (git d8cb92c).
 * Host [EntryExtraData.PARAM_MESSAGE] / [EntryExtraData.PARAM_TITLE] are **not** used when a fixed
 * per-action string existed in the old UI.
 */
object TextEntryMessageFormatter {

    @Suppress("DEPRECATION")
    private val fleetTitleByAction: Map<String, Int> = buildMap {
        put(
            TextEntry.ACTION_ENTER_FLEET_DATA,
            R.string.prompt_input_fleet_customer_data
        )
        put(TextEntry.ACTION_ENTER_CUSTOMER_DATA, R.string.enter_customer_data)
        put(TextEntry.ACTION_ENTER_DEPARTMENT_NUMBER, R.string.enter_department_number)
        put(TextEntry.ACTION_ENTER_DRIVER_ID, R.string.enter_driver_id)
        put(TextEntry.ACTION_ENTER_EMPLOYEE_NUMBER, R.string.enter_employee_number)
        put(TextEntry.ACTION_ENTER_FLEET_PROMPT_CODE, R.string.enter_fleet_prompt_code)
        put(TextEntry.ACTION_ENTER_HUBOMETER, R.string.enter_hubometer)
        put(TextEntry.ACTION_ENTER_JOB_ID, R.string.enter_job_id)
        put(TextEntry.ACTION_ENTER_LICENSE_NUMBER, R.string.enter_license_number)
        put(TextEntry.ACTION_ENTER_MAINTENANCE_ID, R.string.enter_maintenance_id)
        put(TextEntry.ACTION_ENTER_ODOMETER, R.string.enter_odometer)
        put(TextEntry.ACTION_ENTER_FLEET_PO_NUMBER, R.string.enter_fleet_po_number)
        put(TextEntry.ACTION_ENTER_REEFER_HOURS, R.string.enter_reefer_hours)
        put(TextEntry.ACTION_ENTER_TRAILER_ID, R.string.enter_trailer_id)
        put(TextEntry.ACTION_ENTER_TRIP_NUMBER, R.string.enter_trip_number)
        put(TextEntry.ACTION_ENTER_UNIT_ID, R.string.enter_unit_id)
        put(TextEntry.ACTION_ENTER_USER_ID, R.string.enter_user_id)
        put(TextEntry.ACTION_ENTER_VEHICLE_ID, R.string.enter_vehicle_id)
        put(TextEntry.ACTION_ENTER_VEHICLE_NUMBER, R.string.enter_vehicle_number)
        put(TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_1, R.string.enter_additional_fleet_data_1)
        put(TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_2, R.string.enter_additional_fleet_data_2)
    }

    /**
     * Single-line / TextScreen-style Entry (not amount, not AVS/FSA).
     */
    fun singleLinePrompt(action: String?, extras: Bundle, res: Resources): String {
        if (action == null) return hostFallbackPrompt(extras)
        fleetTitleByAction[action]?.let { return res.getString(it) }
        return when (action) {
            TextEntry.ACTION_ENTER_ADDRESS -> res.getString(R.string.pls_input_address)
            TextEntry.ACTION_ENTER_AUTH -> res.getString(R.string.please_enter_auth_code)
            TextEntry.ACTION_ENTER_CUSTOMER_CODE -> res.getString(R.string.pls_input_customer_code)
            TextEntry.ACTION_ENTER_ORDER_NUMBER -> res.getString(R.string.pls_input_order_number)
            TextEntry.ACTION_ENTER_PO_NUMBER -> res.getString(R.string.pls_input_po_number)
            TextEntry.ACTION_ENTER_PROD_DESC -> res.getString(R.string.pls_input_proc_desc)
            TextEntry.ACTION_ENTER_GLOBAL_UID -> res.getString(R.string.enter_global_uid)
            TextEntry.ACTION_ENTER_ORIGINAL_TRANSACTION_IDENTIFIER ->
                res.getString(R.string.pls_input_orig_trans_identifier)
            TextEntry.ACTION_ENTER_EXPIRY_DATE -> res.getString(R.string.pls_input_expiry_date)
            TextEntry.ACTION_ENTER_ORIG_DATE -> res.getString(R.string.pls_input_orig_trans_date)
            TextEntry.ACTION_ENTER_CS_PHONE_NUMBER -> res.getString(R.string.pls_input_cs_phone_number)
            TextEntry.ACTION_ENTER_TICKET_NUMBER -> res.getString(R.string.enter_ticket_number)
            TextEntry.ACTION_ENTER_GUEST_NUMBER -> res.getString(R.string.pls_input_guest_number)
            TextEntry.ACTION_ENTER_MERCHANT_TAX_ID -> res.getString(R.string.prompt_merchant_tax_id)
            TextEntry.ACTION_ENTER_PHONE_NUMBER -> res.getString(R.string.pls_input_phone_number)
            TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE ->
                res.getString(R.string.pls_input_prompt_restriction_code)
            TextEntry.ACTION_ENTER_TABLE_NUMBER -> res.getString(R.string.pls_input_table_number)
            TextEntry.ACTION_ENTER_TRANS_NUMBER -> res.getString(R.string.pls_input_transaction_number)
            TextEntry.ACTION_ENTER_ZIPCODE -> res.getString(R.string.pls_input_zip_code)
            TextEntry.ACTION_ENTER_DEST_ZIPCODE -> res.getString(R.string.pls_input_dest_zip_code)
            TextEntry.ACTION_ENTER_CLERK_ID -> res.getString(R.string.pls_input_clerk_id)
            TextEntry.ACTION_ENTER_SERVER_ID -> res.getString(R.string.pls_input_server_id)
            TextEntry.ACTION_ENTER_INVOICE_NUMBER -> res.getString(R.string.pls_input_invoice_number)
            TextEntry.ACTION_ENTER_VOUCHER_DATA -> res.getString(R.string.please_enter_voucher_number)
            TextEntry.ACTION_ENTER_REFERENCE_NUMBER -> res.getString(R.string.pls_input_reference_number)
            TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER ->
                res.getString(R.string.pls_input_merchant_reference_number)
            TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER ->
                res.getString(R.string.pls_input_oct_reference_number)
            TextEntry.ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID ->
                res.getString(R.string.pls_input_visa_installmenttransID)
            TextEntry.ACTION_ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID ->
                res.getString(R.string.please_input_visa_installment_plan_acceptance_id)
            else -> hostFallbackPrompt(extras)
        }
    }

    private fun hostFallbackPrompt(extras: Bundle): String {
        val message = extras.getString(EntryExtraData.PARAM_MESSAGE)
        if (!message.isNullOrEmpty()) return message
        return extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    }

    /**
     * [AmountScreen] title for minor-unit amount Entry actions.
     */
    fun amountKindPrompt(action: String?, extras: Bundle, currency: String?, res: Resources): String {
        if (action == null) return amountFallback(extras, currency, res)
        return when (action) {
            TextEntry.ACTION_ENTER_AMOUNT ->
                if (CurrencyType.POINT == currency) {
                    res.getString(R.string.prompt_input_point)
                } else {
                    res.getString(R.string.prompt_input_amount)
                }
            TextEntry.ACTION_ENTER_FUEL_AMOUNT -> res.getString(R.string.prompt_input_fuel_amount)
            TextEntry.ACTION_ENTER_TAX_AMOUNT -> res.getString(R.string.prompt_input_tax_amount)
            TextEntry.ACTION_ENTER_TOTAL_AMOUNT -> res.getString(R.string.prompt_input_total_amount)
            TextEntry.ACTION_ENTER_CASH_BACK -> res.getString(R.string.select_cashback_amount)
            TextEntry.ACTION_ENTER_TIP -> tipHeadline(extras, res)
            else -> amountFallback(extras, currency, res)
        }
    }

    /**
     * Matches legacy [com.paxus.pay.poslinkui.demo.entry.text.amount.TipScreen] title line.
     */
    private fun tipHeadline(extras: Bundle, res: Resources): String {
        val tipName = extras.getString(EntryExtraData.PARAM_TIP_NAME)
            ?: res.getString(R.string.tip_name)
        val hasOptions = extras.getStringArray(EntryExtraData.PARAM_TIP_OPTIONS)?.isNotEmpty() == true
        val prefix = if (hasOptions) "Select " else "Enter "
        return prefix + tipName
    }

    private fun amountFallback(extras: Bundle, currency: String?, res: Resources): String {
        val message = extras.getString(EntryExtraData.PARAM_MESSAGE)
        if (!message.isNullOrEmpty()) return message
        return if (CurrencyType.POINT == currency) {
            res.getString(R.string.prompt_input_point)
        } else {
            res.getString(R.string.prompt_input_amount)
        }
    }

    /**
     * Per-line label for FSA amount keys (legacy [FSAFragment] / FSAAmountFragment prompts).
     */
    fun fsaAmountLinePrompt(optionConstant: String, res: Resources): String = when (optionConstant) {
        FSAAmountType.HEALTH_CARE_AMOUNT -> res.getString(R.string.prompt_input_healthcare_amount)
        FSAAmountType.CLINIC_AMOUNT -> res.getString(R.string.prompt_input_clinical_amount)
        FSAAmountType.PRESCRIPTION_AMOUNT -> res.getString(R.string.prompt_input_rx_amount)
        FSAAmountType.DENTAL_AMOUNT -> res.getString(R.string.prompt_input_dental_amount)
        FSAAmountType.VISION_AMOUNT -> res.getString(R.string.prompt_input_vision_amount)
        FSAAmountType.COPAY_AMOUNT -> res.getString(R.string.prompt_input_co_pay_amount)
        FSAAmountType.OTC_AMOUNT -> res.getString(R.string.prompt_input_otc_amount)
        FSAAmountType.TRANSIT_AMOUNT -> res.getString(R.string.prompt_input_transit_amount)
        else -> optionConstant
    }
}
