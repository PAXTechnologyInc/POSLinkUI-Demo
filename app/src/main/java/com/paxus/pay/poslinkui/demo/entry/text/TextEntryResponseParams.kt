package com.paxus.pay.poslinkui.demo.entry.text

import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.TextEntry

/**
 * Maps [TextEntry] activity actions to [TextEntryKind] for Compose screens.
 *
 * Contract keys mirror `constant` library [EntryRequest] string constants.
 */
object TextEntryResponseParams {

    @Suppress("DEPRECATION") // ACTION_ENTER_FLEET_DATA still in manifest / registry for legacy hosts
    private val singleStringKindByAction: Map<String, TextEntryKind> = mapOf(
        TextEntry.ACTION_ENTER_TRANS_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_TRANS_NUMBER),
        TextEntry.ACTION_ENTER_EXPIRY_DATE to TextEntryKind.SingleString(EntryRequest.PARAM_EXPIRY_DATE),
        TextEntry.ACTION_ENTER_ADDRESS to TextEntryKind.SingleString(EntryRequest.PARAM_ADDRESS),
        TextEntry.ACTION_ENTER_ZIPCODE to TextEntryKind.SingleString(EntryRequest.PARAM_ZIP_CODE),
        TextEntry.ACTION_ENTER_AUTH to TextEntryKind.SingleString(EntryRequest.PARAM_AUTH_CODE),
        TextEntry.ACTION_ENTER_VOUCHER_DATA to TextEntryKind.SingleString(EntryRequest.PARAM_VOUCHER_NUMBER),
        TextEntry.ACTION_ENTER_REFERENCE_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_REFERENCE_NUMBER),
        TextEntry.ACTION_ENTER_INVOICE_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_INVOICE_NUMBER),
        TextEntry.ACTION_ENTER_CLERK_ID to TextEntryKind.SingleString(EntryRequest.PARAM_CLERK_ID),
        TextEntry.ACTION_ENTER_SERVER_ID to TextEntryKind.SingleString(EntryRequest.PARAM_SERVER_ID),
        TextEntry.ACTION_ENTER_ORIG_DATE to TextEntryKind.SingleString(EntryRequest.PARAM_ORIG_DATE),
        TextEntry.ACTION_ENTER_TABLE_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_TABLE_NUMBER),
        TextEntry.ACTION_ENTER_PHONE_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_PHONE_NUMBER),
        TextEntry.ACTION_ENTER_GUEST_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_GUEST_NUMBER),
        TextEntry.ACTION_ENTER_ORDER_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_ORDER_NUMBER),
        TextEntry.ACTION_ENTER_PO_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_PO_NUMBER),
        TextEntry.ACTION_ENTER_PROD_DESC to TextEntryKind.SingleString(EntryRequest.PARAM_PROD_DESC),
        TextEntry.ACTION_ENTER_CUSTOMER_CODE to TextEntryKind.SingleString(EntryRequest.PARAM_CUSTOMER_CODE),
        TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE to
            TextEntryKind.SingleString(EntryRequest.PARAM_PROMPT_RESTRICTION_CODE),
        TextEntry.ACTION_ENTER_CUSTOMER_DATA to TextEntryKind.SingleString(EntryRequest.PARAM_CUSTOMER_DATA),
        TextEntry.ACTION_ENTER_DEPARTMENT_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_DEPARTMENT_NUMBER),
        TextEntry.ACTION_ENTER_DRIVER_ID to TextEntryKind.SingleString(EntryRequest.PARAM_DRIVER_ID),
        TextEntry.ACTION_ENTER_EMPLOYEE_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_EMPLOYEE_NUMBER),
        TextEntry.ACTION_ENTER_FLEET_DATA to TextEntryKind.SingleString(EntryRequest.PARAM_FLEET_CUSTOMER_DATA),
        TextEntry.ACTION_ENTER_FLEET_PROMPT_CODE to TextEntryKind.SingleString(EntryRequest.PARAM_FLEET_PROMPT_CODE),
        TextEntry.ACTION_ENTER_HUBOMETER to TextEntryKind.SingleString(EntryRequest.PARAM_HUBOMETER),
        TextEntry.ACTION_ENTER_JOB_ID to TextEntryKind.SingleString(EntryRequest.PARAM_JOB_ID),
        TextEntry.ACTION_ENTER_LICENSE_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_LICENSE_NUMBER),
        TextEntry.ACTION_ENTER_MAINTENANCE_ID to TextEntryKind.SingleString(EntryRequest.PARAM_MAINTENANCE_ID),
        TextEntry.ACTION_ENTER_ODOMETER to TextEntryKind.SingleString(EntryRequest.PARAM_ODOMETER),
        TextEntry.ACTION_ENTER_FLEET_PO_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_FLEET_PO_NUMBER),
        TextEntry.ACTION_ENTER_REEFER_HOURS to TextEntryKind.SingleString(EntryRequest.PARAM_REEFER_HOURS),
        TextEntry.ACTION_ENTER_TRAILER_ID to TextEntryKind.SingleString(EntryRequest.PARAM_TRAILER_ID),
        TextEntry.ACTION_ENTER_TRIP_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_TRIP_NUMBER),
        TextEntry.ACTION_ENTER_UNIT_ID to TextEntryKind.SingleString(EntryRequest.PARAM_UNIT_ID),
        TextEntry.ACTION_ENTER_USER_ID to TextEntryKind.SingleString(EntryRequest.PARAM_USER_ID),
        TextEntry.ACTION_ENTER_VEHICLE_ID to TextEntryKind.SingleString(EntryRequest.PARAM_VEHICLE_ID),
        TextEntry.ACTION_ENTER_VEHICLE_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_VEHICLE_NUMBER),
        TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_1 to
            TextEntryKind.SingleString(EntryRequest.PARAM_ADDITIONAL_FLEET_DATA_1),
        TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_2 to
            TextEntryKind.SingleString(EntryRequest.PARAM_ADDITIONAL_FLEET_DATA_2),
        TextEntry.ACTION_ENTER_DEST_ZIPCODE to TextEntryKind.SingleString(EntryRequest.PARAM_DEST_ZIP_CODE),
        TextEntry.ACTION_ENTER_CS_PHONE_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_CS_PHONE_NUMBER),
        TextEntry.ACTION_ENTER_MERCHANT_TAX_ID to TextEntryKind.SingleString(EntryRequest.PARAM_MERCHANT_TAX_ID),
        TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER to
            TextEntryKind.SingleString(EntryRequest.PARAM_MERCHANT_REFERENCE_NUMBER),
        TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER to
            TextEntryKind.SingleString(EntryRequest.PARAM_OCT_REFERENCE_NUMBER),
        TextEntry.ACTION_ENTER_TICKET_NUMBER to TextEntryKind.SingleString(EntryRequest.PARAM_TICKET_NUMBER),
        TextEntry.ACTION_ENTER_GLOBAL_UID to TextEntryKind.SingleString(EntryRequest.PARAM_GLOBAL_UID),
        TextEntry.ACTION_ENTER_ORIGINAL_TRANSACTION_IDENTIFIER to
            TextEntryKind.SingleString(EntryRequest.PARAM_ORIGINAL_TRANSACTION_IDENTIFIER),
        TextEntry.ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID to
            TextEntryKind.SingleString(EntryRequest.PARAM_VISA_TRANSID),
        TextEntry.ACTION_ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID to
            TextEntryKind.SingleString(EntryRequest.PARAM_VISA_PLAN_ACCEPTANCE_ID)
    )

    /**
     * @param action [android.content.Intent.getAction] for a Text category Entry.
     */
    @Suppress("DEPRECATION")
    fun resolveKind(action: String?): TextEntryKind {
        if (action == null) return TextEntryKind.Unknown
        when (action) {
            TextEntry.ACTION_ENTER_TIP ->
                return TextEntryKind.AmountMinor(EntryRequest.PARAM_TIP)
            TextEntry.ACTION_ENTER_CASH_BACK ->
                return TextEntryKind.AmountMinor(EntryRequest.PARAM_CASHBACK_AMOUNT)
            TextEntry.ACTION_ENTER_FUEL_AMOUNT ->
                return TextEntryKind.AmountMinor(EntryRequest.PARAM_FUEL_AMOUNT)
            TextEntry.ACTION_ENTER_TAX_AMOUNT ->
                return TextEntryKind.AmountMinor(EntryRequest.PARAM_TAX_AMOUNT)
            TextEntry.ACTION_ENTER_TOTAL_AMOUNT ->
                return TextEntryKind.AmountMinor(EntryRequest.PARAM_TOTAL_AMOUNT)
            TextEntry.ACTION_ENTER_AVS_DATA -> return TextEntryKind.Avs
            TextEntry.ACTION_ENTER_FSA_DATA -> return TextEntryKind.Fsa
        }
        return singleStringKindByAction[action] ?: TextEntryKind.Unknown
    }
}
