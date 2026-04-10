package com.paxus.pay.poslinkui.demo.utils.interfacefilter

import com.pax.us.pay.ui.constant.entry.OptionEntry
import com.pax.us.pay.ui.constant.entry.TextEntry

/**
 * Seeds Text and Option entry actions into the catalog map.
 */
internal fun MutableMap<String?, EntryAction?>.seedEntryActionsCatalogPartA() {
    // Text Entry
    put(
        TextEntry.ACTION_ENTER_AMOUNT,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_AMOUNT,
            "Enter Amount",
            ".TEXT.ENTER_AMOUNT",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_TIP,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_TIP,
            "Enter Tip",
            ".TEXT.ENTER_TIP",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_TRANS_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_TRANS_NUMBER,
            "Enter Transaction Number",
            ".TEXT.ENTER_TRANS_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_EXPIRY_DATE,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_EXPIRY_DATE,
            "Enter Expiry Date",
            ".TEXT.ENTER_EXPIRY_DATE",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_ADDRESS,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_ADDRESS,
            "Enter Address",
            ".TEXT.ENTER_ADDRESS",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_ZIPCODE,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_ZIPCODE,
            "Enter Zipcode",
            ".TEXT.ENTER_ZIPCODE",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_AUTH,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_AUTH,
            "Enter Auth",
            ".TEXT.ENTER_AUTH",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_FSA_DATA,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_FSA_DATA,
            "Enter FSA Data",
            ".TEXT.ENTER_FSA_DATA",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_VOUCHER_DATA,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_VOUCHER_DATA,
            "Enter Voucher Data",
            ".TEXT.ENTER_VOUCHER_DATA",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_AVS_DATA,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_AVS_DATA,
            "Enter AVS Data",
            ".TEXT.ENTER_AVS_DATA",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_REFERENCE_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_REFERENCE_NUMBER,
            "Enter Reference Number",
            ".TEXT.ENTER_REFERENCE_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_INVOICE_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_INVOICE_NUMBER,
            "Enter Invoice Number",
            ".TEXT.ENTER_INVOICE_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_CLERK_ID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_CLERK_ID,
            "Enter Clerk ID",
            ".TEXT.ENTER_CLERK_ID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_SERVER_ID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_SERVER_ID,
            "Enter Server ID",
            ".TEXT.ENTER_SERVER_ID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_CASH_BACK,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_CASH_BACK,
            "Enter Cash Back",
            ".TEXT.ENTER_CASH_BACK",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_ORIG_DATE,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_ORIG_DATE,
            "Enter Orig Date",
            ".TEXT.ENTER_ORIG_DATE",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_FUEL_AMOUNT,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_FUEL_AMOUNT,
            "Enter Fuel Amount",
            ".TEXT.ENTER_FUEL_AMOUNT",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_TAX_AMOUNT,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_TAX_AMOUNT,
            "Enter Tax Amount",
            ".TEXT.ENTER_TAX_AMOUNT",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_TABLE_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_TABLE_NUMBER,
            "Enter Table Number",
            ".TEXT.ENTER_TABLE_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_PHONE_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_PHONE_NUMBER,
            "Enter Phone Number",
            ".TEXT.ENTER_PHONE_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_GUEST_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_GUEST_NUMBER,
            "Enter Guest Number",
            ".TEXT.ENTER_GUEST_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_ORDER_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_ORDER_NUMBER,
            "Enter Order Number",
            ".TEXT.ENTER_ORDER_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_PO_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_PO_NUMBER,
            "Enter PO Number",
            ".TEXT.ENTER_PO_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_PROD_DESC,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_PROD_DESC,
            "Enter Product Description",
            ".TEXT.ENTER_PROD_DESC",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_CUSTOMER_CODE,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_CUSTOMER_CODE,
            "Enter Customer Code",
            ".TEXT.ENTER_CUSTOMER_CODE",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE,
            "Enter Prompt Restriction Code",
            ".TEXT.ENTER_PROMPT_RESTRICTION_CODE",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_FLEET_DATA,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_FLEET_DATA,
            "Enter Fleet Data",
            ".TEXT.ENTER_FLEET_DATA",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_TOTAL_AMOUNT,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_TOTAL_AMOUNT,
            "Enter Total Amount",
            ".TEXT.ENTER_TOTAL_AMOUNT",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_DEST_ZIPCODE,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_DEST_ZIPCODE,
            "Enter Destination Zipcode",
            ".TEXT.ENTER_DEST_ZIPCODE",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_CS_PHONE_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_CS_PHONE_NUMBER,
            "Enter Customer Service Phone Number",
            ".TEXT.ENTER_CS_PHONE_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_MERCHANT_TAX_ID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_MERCHANT_TAX_ID,
            "Enter Merchant Tax ID",
            ".TEXT.ENTER_MERCHANT_TAX_ID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER,
            "Enter Merchant Reference Number",
            ".TEXT.ENTER_MERCHANT_REFERENCE_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER,
            "Enter OCT Reference Number",
            ".TEXT.ENTER_OCT_REFERENCE_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_ORIGINAL_TRANSACTION_IDENTIFIER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_ORIGINAL_TRANSACTION_IDENTIFIER,
            "Enter Original Transaction Identifier",
            ".TEXT.ENTER_ORIGINAL_TRANSACTION_IDENTIFIER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID,
            "Enter Visa Installment Transaction ID",
            ".TEXT.ENTER_VISA_INSTALLMENT_TRANSACTIONID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID,
            "Enter Visa Installment Plan Acceptance ID",
            ".TEXT.ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_TICKET_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_TICKET_NUMBER,
            "Enter Ticket Number",
            ".TEXT.ENTER_TICKET_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_GLOBAL_UID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_GLOBAL_UID,
            "Enter Global UID",
            ".TEXT.ENTER_GLOBAL_UID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_CUSTOMER_DATA,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_CUSTOMER_DATA,
            "Enter Customer data",
            ".TEXT.ENTER_CUSTOMER_DATA",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_DEPARTMENT_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_DEPARTMENT_NUMBER,
            "Enter Department Number",
            ".TEXT.ENTER_DEPARTMENT_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_DRIVER_ID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_DRIVER_ID,
            "Enter Driver Id",
            ".TEXT.ENTER_DRIVER_ID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_EMPLOYEE_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_EMPLOYEE_NUMBER,
            "Enter Employee Number",
            ".TEXT.ENTER_EMPLOYEE_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_FLEET_PROMPT_CODE,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_FLEET_PROMPT_CODE,
            "Enter Fleet Prompt Code",
            ".TEXT.ENTER_FLEET_PROMPT_CODE",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_HUBOMETER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_HUBOMETER,
            "Enter Hubometer",
            ".TEXT.ENTER_HUBOMETER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_JOB_ID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_JOB_ID,
            "Enter Job Id",
            ".TEXT.ENTER_JOB_ID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_LICENSE_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_LICENSE_NUMBER,
            "Enter License Number",
            ".TEXT.ENTER_LICENSE_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_MAINTENANCE_ID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_MAINTENANCE_ID,
            "Enter Maintenance Id",
            ".TEXT.ENTER_MAINTENANCE_ID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_ODOMETER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_ODOMETER,
            "Enter Odometer",
            ".TEXT.ENTER_ODOMETER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_FLEET_PO_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_FLEET_PO_NUMBER,
            "Enter Fleet Po Number",
            ".TEXT.ENTER_FLEET_PO_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_REEFER_HOURS,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_REEFER_HOURS,
            "Enter Reefer Hours",
            ".TEXT.ENTER_REEFER_HOURS",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_TRAILER_ID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_TRAILER_ID,
            "Enter Trailer Id",
            ".TEXT.ENTER_TRAILER_ID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_TRIP_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_TRIP_NUMBER,
            "Enter Trip Number",
            ".TEXT.ENTER_TRIP_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_UNIT_ID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_UNIT_ID,
            "Enter Unit Id",
            ".TEXT.ENTER_UNIT_ID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_USER_ID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_USER_ID,
            "Enter User Id",
            ".TEXT.ENTER_USER_ID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_VEHICLE_ID,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_VEHICLE_ID,
            "Enter Vehicle Id",
            ".TEXT.ENTER_VEHICLE_ID",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_VEHICLE_NUMBER,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_VEHICLE_NUMBER,
            "Enter Vehicle Number",
            ".TEXT.ENTER_VEHICLE_NUMBER",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_1,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_1,
            "Enter Additional Fleet Data 1",
            ".TEXT.ENTER_ADDITIONAL_FLEET_DATA_1",
            true
        )
    )
    put(
        TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_2,
        EntryAction(
            TextEntry.CATEGORY,
            TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_2,
            "Enter Additional Fleet Data 2",
            ".TEXT.ENTER_ADDITIONAL_FLEET_DATA_2",
            true
        )
    )


    // Option Entry
    put(
        OptionEntry.ACTION_SELECT_EBT_TYPE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_EBT_TYPE,
            "Select EBT Type",
            ".OPTION.SELECT_EBT_TYPE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_BY_PASS,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_BY_PASS,
            "Select By Pass",
            ".OPTION.SELECT_BY_PASS",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_SUB_TRANS_TYPE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_SUB_TRANS_TYPE,
            "Select Sub Trans Type",
            ".OPTION.SELECT_SUB_TRANS_TYPE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_AID,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_AID,
            "Select AID",
            ".OPTION.SELECT_AID",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_REFUND_REASON,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_REFUND_REASON,
            "Select Refund Reason",
            ".OPTION.SELECT_REFUND_REASON",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_MOTO_TYPE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_MOTO_TYPE,
            "Select MOTO Type",
            ".OPTION.SELECT_MOTO_TYPE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_TAX_REASON,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_TAX_REASON,
            "Select Tax Reason",
            ".OPTION.SELECT_TAX_REASON",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_DUPLICATE_OVERRIDE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_DUPLICATE_OVERRIDE,
            "Select Duplicate Override",
            ".OPTION.SELECT_DUPLICATE_OVERRIDE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_CARD_TYPE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_CARD_TYPE,
            "Select Card Type",
            ".OPTION.SELECT_CARD_TYPE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_TRANS_TYPE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_TRANS_TYPE,
            "Select Trans Type",
            ".OPTION.SELECT_TRANS_TYPE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_EDC_TYPE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_EDC_TYPE,
            "Select EDC Type",
            ".OPTION.SELECT_EDC_TYPE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_SEARCH_CRITERIA,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_SEARCH_CRITERIA,
            "Select Search Criteria",
            ".OPTION.SELECT_SEARCH_CRITERIA",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_BATCH_TYPE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_BATCH_TYPE,
            "Select Batch Type",
            ".OPTION.SELECT_BATCH_TYPE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_EDC_GROUP,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_EDC_GROUP,
            "Select EDC Group",
            ".OPTION.SELECT_EDC_GROUP",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_REPORT_TYPE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_REPORT_TYPE,
            "Select Report Type",
            ".OPTION.SELECT_REPORT_TYPE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_ACCOUNT_TYPE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_ACCOUNT_TYPE,
            "Select Account Type",
            ".OPTION.SELECT_ACCOUNT_TYPE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_TIP_AMOUNT,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_TIP_AMOUNT,
            "Select Tip Amount",
            ".OPTION.SELECT_TIP_AMOUNT",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_CASHBACK_AMOUNT,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_CASHBACK_AMOUNT,
            "Select Cashback Amount",
            ".OPTION.SELECT_CASHBACK_AMOUNT",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_LANGUAGE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_LANGUAGE,
            "Select Language",
            ".OPTION.SELECT_LANGUAGE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_MERCHANT,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_MERCHANT,
            "Select Merchant",
            ".OPTION.SELECT_MERCHANT",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_ORIG_CURRENCY,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_ORIG_CURRENCY,
            "Select Orig Currency",
            ".OPTION.SELECT_ORIG_CURRENCY",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_INSTALLMENT_PLAN,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_INSTALLMENT_PLAN,
            "Select Installment Plan",
            ".OPTION.SELECT_INSTALLMENT_PLAN",
            true
        )
    )
    // v1.03 baseline gap backfill: route "Select Trans For Adjust" as regular option action.
    put(
        OptionEntry.ACTION_SELECT_TRANS_FOR_ADJUST,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_TRANS_FOR_ADJUST,
            "Select Trans For Adjust",
            ".OPTION.SELECT_TRANS_FOR_ADJUST",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_COF_INITIATOR,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_COF_INITIATOR,
            "Select COF Initiator",
            ".OPTION.SELECT_COF_INITIATOR",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_CASH_DISCOUNT,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_CASH_DISCOUNT,
            "Select Cash Discount",
            ".OPTION.SELECT_CASH_DISCOUNT",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_BATCH_REPORT_TYPE,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_BATCH_REPORT_TYPE,
            "Select Batch Report Type",
            ".OPTION.SELECT_BATCH_REPORT_TYPE",
            true
        )
    )
    put(
        OptionEntry.ACTION_SELECT_CURRENCY,
        EntryAction(
            OptionEntry.CATEGORY,
            OptionEntry.ACTION_SELECT_CURRENCY,
            "Select Currency",
            ".OPTION.SELECT_CURRENCY",
            true
        )
    )
}
