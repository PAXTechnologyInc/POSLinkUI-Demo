package com.paxus.pay.poslinkui.demo.utils.interfacefilter;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.InformationEntry;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.SignatureEntry;
import com.pax.us.pay.ui.constant.entry.TextEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * It does not contain states. It is a repository for the list of categories and actions.
 * Do NOT use this directly.
 * Use {@link EntryActionFilterManager} instead.
 */
class EntryActionAndCategoryRepository {

    private EntryActionAndCategoryRepository() {
        //Private Constructor
    }

    private static final List<EntryCategory> ENTRY_CATEGORY_LIST = new ArrayList<EntryCategory>(){{
        add(new EntryCategory(TextEntry.CATEGORY, "Text"));
        add(new EntryCategory(SecurityEntry.CATEGORY, "Security"));
        add(new EntryCategory(InformationEntry.CATEGORY, "Information"));
        add(new EntryCategory(PoslinkEntry.CATEGORY, "POSLink"));
        add(new EntryCategory(SignatureEntry.CATEGORY, "Signature"));
        add(new EntryCategory(OptionEntry.CATEGORY, "Option"));
        add(new EntryCategory(ConfirmationEntry.CATEGORY, "Confirmation"));
    }};

    static List<EntryCategory> getCategories(){
        return new ArrayList<>(ENTRY_CATEGORY_LIST);
    }

    private static final Map<String, EntryAction> ENTRY_ACTION_MAP = new HashMap<String, EntryAction>(){{
        // Text Entry
        put(TextEntry.ACTION_ENTER_AMOUNT, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_AMOUNT, "Enter Amount", ".TEXT.ENTER_AMOUNT", true));
        put(TextEntry.ACTION_ENTER_TIP, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_TIP, "Enter Tip", ".TEXT.ENTER_TIP", true));
        put(TextEntry.ACTION_ENTER_TRANS_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_TRANS_NUMBER, "Enter Transaction Number", ".TEXT.ENTER_TRANS_NUMBER", true));
        put(TextEntry.ACTION_ENTER_EXPIRY_DATE, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_EXPIRY_DATE, "Enter Expiry Date", ".TEXT.ENTER_EXPIRY_DATE", true));
        put(TextEntry.ACTION_ENTER_ADDRESS, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_ADDRESS, "Enter Address", ".TEXT.ENTER_ADDRESS", true));
        put(TextEntry.ACTION_ENTER_ZIPCODE, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_ZIPCODE, "Enter Zipcode", ".TEXT.ENTER_ZIPCODE", true));
        put(TextEntry.ACTION_ENTER_AUTH, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_AUTH, "Enter Auth", ".TEXT.ENTER_AUTH", true));
        put(TextEntry.ACTION_ENTER_FSA_DATA, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_FSA_DATA, "Enter FSA Data", ".TEXT.ENTER_FSA_DATA", true));
        put(TextEntry.ACTION_ENTER_VOUCHER_DATA, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_VOUCHER_DATA, "Enter Voucher Data", ".TEXT.ENTER_VOUCHER_DATA", true));
        put(TextEntry.ACTION_ENTER_AVS_DATA, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_AVS_DATA, "Enter AVS Data", ".TEXT.ENTER_AVS_DATA", true));
        put(TextEntry.ACTION_ENTER_REFERENCE_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_REFERENCE_NUMBER, "Enter Reference Number", ".TEXT.ENTER_REFERENCE_NUMBER", true));
        put(TextEntry.ACTION_ENTER_INVOICE_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_INVOICE_NUMBER, "Enter Invoice Number", ".TEXT.ENTER_INVOICE_NUMBER", true));
        put(TextEntry.ACTION_ENTER_CLERK_ID, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_CLERK_ID, "Enter Clerk ID", ".TEXT.ENTER_CLERK_ID", true));
        put(TextEntry.ACTION_ENTER_SERVER_ID, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_SERVER_ID, "Enter Server ID", ".TEXT.ENTER_SERVER_ID", true));
        put(TextEntry.ACTION_ENTER_CASH_BACK, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_CASH_BACK, "Enter Cash Back", ".TEXT.ENTER_CASH_BACK", true));
        put(TextEntry.ACTION_ENTER_ORIG_DATE, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_ORIG_DATE, "Enter Orig Date", ".TEXT.ENTER_ORIG_DATE", true));
        put(TextEntry.ACTION_ENTER_FUEL_AMOUNT, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_FUEL_AMOUNT, "Enter Fuel Amount", ".TEXT.ENTER_FUEL_AMOUNT", true));
        put(TextEntry.ACTION_ENTER_TAX_AMOUNT, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_TAX_AMOUNT, "Enter Tax Amount", ".TEXT.ENTER_TAX_AMOUNT", true));
        put(TextEntry.ACTION_ENTER_TABLE_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_TABLE_NUMBER, "Enter Table Number", ".TEXT.ENTER_TABLE_NUMBER", true));
        put(TextEntry.ACTION_ENTER_PHONE_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_PHONE_NUMBER, "Enter Phone Number", ".TEXT.ENTER_PHONE_NUMBER", true));
        put(TextEntry.ACTION_ENTER_GUEST_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_GUEST_NUMBER, "Enter Guest Number", ".TEXT.ENTER_GUEST_NUMBER", true));
        put(TextEntry.ACTION_ENTER_ORDER_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_ORDER_NUMBER, "Enter Order Number", ".TEXT.ENTER_ORDER_NUMBER", true));
        put(TextEntry.ACTION_ENTER_PO_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_PO_NUMBER, "Enter PO Number", ".TEXT.ENTER_PO_NUMBER", true));
        put(TextEntry.ACTION_ENTER_PROD_DESC, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_PROD_DESC, "Enter Product Description", ".TEXT.ENTER_PROD_DESC", true));
        put(TextEntry.ACTION_ENTER_CUSTOMER_CODE, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_CUSTOMER_CODE, "Enter Customer Code", ".TEXT.ENTER_CUSTOMER_CODE", true));
        put(TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE, "Enter Prompt Restriction Code", ".TEXT.ENTER_PROMPT_RESTRICTION_CODE", true));
        put(TextEntry.ACTION_ENTER_FLEET_DATA, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_FLEET_DATA, "Enter Fleet Data", ".TEXT.ENTER_FLEET_DATA", true));
        put(TextEntry.ACTION_ENTER_TOTAL_AMOUNT, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_TOTAL_AMOUNT, "Enter Total Amount", ".TEXT.ENTER_TOTAL_AMOUNT", true));
        put(TextEntry.ACTION_ENTER_DEST_ZIPCODE, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_DEST_ZIPCODE, "Enter Destination Zipcode", ".TEXT.ENTER_DEST_ZIPCODE", true));
        put(TextEntry.ACTION_ENTER_CS_PHONE_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_CS_PHONE_NUMBER, "Enter Customer Service Phone Number", ".TEXT.ENTER_CS_PHONE_NUMBER", true));
        put(TextEntry.ACTION_ENTER_MERCHANT_TAX_ID, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_MERCHANT_TAX_ID, "Enter Merchant Tax ID", ".TEXT.ENTER_MERCHANT_TAX_ID", true));
        put(TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER, "Enter Merchant Reference Number", ".TEXT.ENTER_MERCHANT_REFERENCE_NUMBER", true));
        put(TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER, "Enter OCT Reference Number", ".TEXT.ENTER_OCT_REFERENCE_NUMBER", true));
        put(TextEntry.ACTION_ENTER_ORIGINAL_TRANSACTION_IDENTIFIER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_ORIGINAL_TRANSACTION_IDENTIFIER, "Enter Original Transaction Identifier", ".TEXT.ENTER_ORIGINAL_TRANSACTION_IDENTIFIER", true));
        put(TextEntry.ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID, "Enter Visa Installment Transaction ID", ".TEXT.ENTER_VISA_INSTALLMENT_TRANSACTIONID", true));
        put(TextEntry.ACTION_ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID, "Enter Visa Installment Plan Acceptance ID", ".TEXT.ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID", true));
        put(TextEntry.ACTION_ENTER_TICKET_NUMBER, new EntryAction(TextEntry.CATEGORY, TextEntry.ACTION_ENTER_TICKET_NUMBER, "Enter Ticket Number", ".TEXT.ENTER_TICKET_NUMBER", true));

        // Option Entry
        put(OptionEntry.ACTION_SELECT_EBT_TYPE, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_EBT_TYPE, "Select EBT Type", ".OPTION.SELECT_EBT_TYPE", true));
        put(OptionEntry.ACTION_SELECT_BY_PASS, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_BY_PASS, "Select By Pass", ".OPTION.SELECT_BY_PASS", true));
        put(OptionEntry.ACTION_SELECT_SUB_TRANS_TYPE, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_SUB_TRANS_TYPE, "Select Sub Trans Type", ".OPTION.SELECT_SUB_TRANS_TYPE", true));
        put(OptionEntry.ACTION_SELECT_AID, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_AID, "Select AID", ".OPTION.SELECT_AID", true));
        put(OptionEntry.ACTION_SELECT_REFUND_REASON, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_REFUND_REASON, "Select Refund Reason", ".OPTION.SELECT_REFUND_REASON", true));
        put(OptionEntry.ACTION_SELECT_MOTO_TYPE, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_MOTO_TYPE, "Select MOTO Type", ".OPTION.SELECT_MOTO_TYPE", true));
        put(OptionEntry.ACTION_SELECT_TAX_REASON, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_TAX_REASON, "Select Tax Reason", ".OPTION.SELECT_TAX_REASON", true));
        put(OptionEntry.ACTION_SELECT_DUPLICATE_OVERRIDE, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_DUPLICATE_OVERRIDE, "Select Duplicate Override", ".OPTION.SELECT_DUPLICATE_OVERRIDE", true));
        put(OptionEntry.ACTION_SELECT_CARD_TYPE, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_CARD_TYPE, "Select Card Type", ".OPTION.SELECT_CARD_TYPE", true));
        put(OptionEntry.ACTION_SELECT_TRANS_TYPE, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_TRANS_TYPE, "Select Trans Type", ".OPTION.SELECT_TRANS_TYPE", true));
        put(OptionEntry.ACTION_SELECT_EDC_TYPE, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_EDC_TYPE, "Select EDC Type", ".OPTION.SELECT_EDC_TYPE", true));
        put(OptionEntry.ACTION_SELECT_SEARCH_CRITERIA, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_SEARCH_CRITERIA, "Select Search Criteria", ".OPTION.SELECT_SEARCH_CRITERIA", true));
        put(OptionEntry.ACTION_SELECT_BATCH_TYPE, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_BATCH_TYPE, "Select Batch Type", ".OPTION.SELECT_BATCH_TYPE", true));
        put(OptionEntry.ACTION_SELECT_EDC_GROUP, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_EDC_GROUP, "Select EDC Group", ".OPTION.SELECT_EDC_GROUP", true));
        put(OptionEntry.ACTION_SELECT_REPORT_TYPE, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_REPORT_TYPE, "Select Report Type", ".OPTION.SELECT_REPORT_TYPE", true));
        put(OptionEntry.ACTION_SELECT_ACCOUNT_TYPE, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_ACCOUNT_TYPE, "Select Account Type", ".OPTION.SELECT_ACCOUNT_TYPE", true));
        put(OptionEntry.ACTION_SELECT_TIP_AMOUNT, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_TIP_AMOUNT, "Select Tip Amount", ".OPTION.SELECT_TIP_AMOUNT", true));
        put(OptionEntry.ACTION_SELECT_CASHBACK_AMOUNT, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_CASHBACK_AMOUNT, "Select Cashback Amount", ".OPTION.SELECT_CASHBACK_AMOUNT", true));
        put(OptionEntry.ACTION_SELECT_LANGUAGE, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_LANGUAGE, "Select Language", ".OPTION.SELECT_LANGUAGE", true));
        put(OptionEntry.ACTION_SELECT_MERCHANT, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_MERCHANT, "Select Merchant", ".OPTION.SELECT_MERCHANT", true));
        put(OptionEntry.ACTION_SELECT_ORIG_CURRENCY, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_ORIG_CURRENCY, "Select Orig Currency", ".OPTION.SELECT_ORIG_CURRENCY", true));
        put(OptionEntry.ACTION_SELECT_INSTALLMENT_PLAN, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_INSTALLMENT_PLAN, "Select Installment Plan", ".OPTION.SELECT_INSTALLMENT_PLAN", true));
        put(OptionEntry.ACTION_SELECT_TRANS_FOR_ADJUST, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_TRANS_FOR_ADJUST, "Select Trans For Adjust", ".OPTION.SELECT_TRANS_FOR_ADJUST", true));
        put(OptionEntry.ACTION_SELECT_COF_INITIATOR, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_COF_INITIATOR, "Select COF Initiator", ".OPTION.SELECT_COF_INITIATOR", true));
        put(OptionEntry.ACTION_SELECT_COF_INITIATOR, new EntryAction(OptionEntry.CATEGORY, OptionEntry.ACTION_SELECT_CASH_DISCOUNT, "Select Cash Discount", ".OPTION.SELECT_CASH_DISCOUNT", true));

        // Confirmation Entry
        put(ConfirmationEntry.ACTION_START_UI, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_START_UI, "Start UI", ".CONFIRMATION.START_UI", true));
        put(ConfirmationEntry.ACTION_CONFIRM_UNIFIED_MESSAGE, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_UNIFIED_MESSAGE, "Confirm Unified Message", ".CONFIRMATION.CONFIRM_UNIFIED_MESSAGE", true));
        put(ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL, "Reverse Partial Approval", ".CONFIRMATION.REVERSE_PARTIAL_APPROVAL", true));
        put(ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL, "Supplement Partial Approval", ".CONFIRMATION.SUPPLEMENT_PARTIAL_APPROVAL", true));
        put(ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY_OFFLINE, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY_OFFLINE, "Confirm Online Retry Offline", ".CONFIRMATION.CONFIRM_ONLINE_RETRY_OFFLINE", true));
        put(ConfirmationEntry.ACTION_CHECK_CARD_PRESENT, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CHECK_CARD_PRESENT, "Check Card Present", ".CONFIRMATION.CHECK_CARD_PRESENT", true));
        put(ConfirmationEntry.ACTION_CHECK_DEACTIVATE_WARN, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CHECK_DEACTIVATE_WARN, "Check Deactivate Warn", ".CONFIRMATION.CHECK_DEACTIVATE_WARN", true));
        put(ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE, "Confirm Batch Close", ".CONFIRMATION.CONFIRM_BATCH_CLOSE", true));
        put(ConfirmationEntry.ACTION_CONFIRM_UNTIPPED, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_UNTIPPED, "Confirm Untipped", ".CONFIRMATION.CONFIRM_UNTIPPED", true));
        put(ConfirmationEntry.ACTION_CONFIRM_DUPLICATE_TRANS, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_DUPLICATE_TRANS, "Confirm Duplicate Trans", ".CONFIRMATION.CONFIRM_DUPLICATE_TRANS", true));
        put(ConfirmationEntry.ACTION_CONFIRM_SURCHARGE_FEE, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_SURCHARGE_FEE, "Confirm Surcharge Fee", ".CONFIRMATION.CONFIRM_SURCHARGE_FEE", true));
        put(ConfirmationEntry.ACTION_CONFIRM_SERVICE_FEE, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_SERVICE_FEE, "Confirm Service Fee", ".CONFIRMATION.CONFIRM_SERVICE_FEE", true));
        put(ConfirmationEntry.ACTION_CONFIRM_PRINTER_STATUS, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_PRINTER_STATUS, "Confirm Printer Status", ".CONFIRMATION.CONFIRM_PRINTER_STATUS", true));
        put(ConfirmationEntry.ACTION_CONFIRM_UPLOAD_TRANS, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_UPLOAD_TRANS, "Confirm Upload Trans", ".CONFIRMATION.CONFIRM_UPLOAD_TRANS", true));
        put(ConfirmationEntry.ACTION_CONFIRM_UPLOAD_RETRY, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_UPLOAD_RETRY, "Confirm Upload Retry", ".CONFIRMATION.CONFIRM_UPLOAD_RETRY", true));
        put(ConfirmationEntry.ACTION_CONFIRM_PRINT_FAILED_TRANS, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_PRINT_FAILED_TRANS, "Confirm Print Failed Trans", ".CONFIRMATION.CONFIRM_PRINT_FAILED_TRANS", true));
        put(ConfirmationEntry.ACTION_CONFIRM_PRINT_FPS, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_PRINT_FPS, "Confirm Print FPS", ".CONFIRMATION.CONFIRM_PRINT_FPS", true));
        put(ConfirmationEntry.ACTION_CONFIRM_DELETE_SF, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_DELETE_SF, "Confirm Delete SF", ".CONFIRMATION.CONFIRM_DELETE_SF", true));
        put(ConfirmationEntry.ACTION_CONFIRM_PRINT_CUSTOMER_COPY, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_PRINT_CUSTOMER_COPY, "Confirm Print Customer Copy", ".CONFIRMATION.CONFIRM_PRINT_CUSTOMER_COPY", true));
        put(ConfirmationEntry.ACTION_CONFIRM_BATCH_FOR_APPLICATION_UPDATE, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_BATCH_FOR_APPLICATION_UPDATE, "Confirm Batch For Application Update", ".CONFIRMATION.CONFIRM_BATCH_FOR_APPLICATION_UPDATE", true));
        put(ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY, "Confirm Online Retry", ".CONFIRMATION.CONFIRM_ONLINE_RETRY", true));
        put(ConfirmationEntry.ACTION_CONFIRM_ADJUST_TIP, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_ADJUST_TIP, "Confirm Adjust Tip", ".CONFIRMATION.CONFIRM_ADJUST_TIP", true));
        put(ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT, "Confirm Card Process Result", ".CONFIRMATION.CONFIRM_CARD_PROCESS_RESULT", true));
        put(ConfirmationEntry.ACTION_CONFIRM_RECEIPT_SIGNATURE, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_RECEIPT_SIGNATURE, "Confirm Receipt Signature", ".CONFIRMATION.CONFIRM_RECEIPT_SIGNATURE", true));
        put(ConfirmationEntry.ACTION_CONFIRM_RECEIPT_VIEW, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_RECEIPT_VIEW, "Confirm Receipt View", ".CONFIRMATION.CONFIRM_RECEIPT_VIEW", true));
        put(ConfirmationEntry.ACTION_CONFIRM_BALANCE, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_BALANCE, "Confirm Balance", ".CONFIRMATION.CONFIRM_BALANCE", true));
        put(ConfirmationEntry.ACTION_CONFIRM_MERCHANT_SCOPE, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_MERCHANT_SCOPE, "Confirm Merchant Scope", ".CONFIRMATION.CONFIRM_MERCHANT_SCOPE", true));
        put(ConfirmationEntry.ACTION_CONFIRM_CARD_ENTRY_RETRY, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_CARD_ENTRY_RETRY, "Confirm Card Entry Retry", ".CONFIRMATION.CONFIRM_CARD_ENTRY_RETRY", true));
        put(ConfirmationEntry.ACTION_CONFIRM_SIGNATURE_MATCH, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_SIGNATURE_MATCH, "Confirm Signature Match", ".CONFIRMATION.CONFIRM_SIGNATURE_MATCH", true));
        put(ConfirmationEntry.ACTION_DISPLAY_QR_CODE_RECEIPT, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_DISPLAY_QR_CODE_RECEIPT, "Display QR Code Receipt", ".CONFIRMATION.DISPLAY_QR_CODE_RECEIPT", true));
        put(ConfirmationEntry.ACTION_CONFIRM_DCC, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_DCC, "Confirm DCC", ".CONFIRMATION.CONFIRM_DCC", true));
        put(ConfirmationEntry.ACTION_CONFIRM_TOTAL_AMOUNT, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_TOTAL_AMOUNT, "Confirm Total Amount", ".CONFIRMATION.CONFIRM_TOTAL_AMOUNT", true));
        put(ConfirmationEntry.ACTION_CONFIRM_CASH_PAYMENT, new EntryAction(ConfirmationEntry.CATEGORY, ConfirmationEntry.ACTION_CONFIRM_CASH_PAYMENT, "Confirm Cash Payment", ".CONFIRMATION.CONFIRM_CASH_PAYMENT", true));

        // Information Entry
        put(InformationEntry.ACTION_DISPLAY_TRANS_INFORMATION, new EntryAction(InformationEntry.CATEGORY, InformationEntry.ACTION_DISPLAY_TRANS_INFORMATION, "Display Transaction Information", ".INFORMATION.DISPLAY_TRANS_INFORMATION", true));

        // Signature Entry
        put(SignatureEntry.ACTION_SIGNATURE, new EntryAction(SignatureEntry.CATEGORY, SignatureEntry.ACTION_SIGNATURE, "Signature", ".SIGNATURE.SIGNATURE", true));

        // Security Entry
        put(SecurityEntry.ACTION_INPUT_ACCOUNT, new EntryAction(SecurityEntry.CATEGORY, SecurityEntry.ACTION_INPUT_ACCOUNT, "Input Account", ".SECURITY.INPUT_ACCOUNT", true));
        put(SecurityEntry.ACTION_ENTER_PIN, new EntryAction(SecurityEntry.CATEGORY, SecurityEntry.ACTION_ENTER_PIN, "Enter PIN", ".SECURITY.ENTER_PIN", true));
        put(SecurityEntry.ACTION_ENTER_VCODE, new EntryAction(SecurityEntry.CATEGORY, SecurityEntry.ACTION_ENTER_VCODE, "Enter V Code", ".SECURITY.ENTER_VCODE", true));
        put(SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS, new EntryAction(SecurityEntry.CATEGORY, SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS, "Enter Card Last 4 Digits", ".SECURITY.ENTER_CARD_LAST_4_DIGITS", true));
        put(SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS, new EntryAction(SecurityEntry.CATEGORY, SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS, "Enter Card All Digits", ".SECURITY.ENTER_CARD_ALL_DIGITS", true));
        put(SecurityEntry.ACTION_ENTER_ADMINISTRATION_PASSWORD, new EntryAction(SecurityEntry.CATEGORY, SecurityEntry.ACTION_ENTER_ADMINISTRATION_PASSWORD, "Enter Administration Password", ".SECURITY.ENTER_ADMINISTRATION_PASSWORD", true));
        put(SecurityEntry.ACTION_MANAGE_INPUT_ACCOUNT, new EntryAction(SecurityEntry.CATEGORY, SecurityEntry.ACTION_MANAGE_INPUT_ACCOUNT, "Manage Input Account", ".SECURITY.MANAGE_INPUT_ACCOUNT", true));

        // Poslink Entry
        put(PoslinkEntry.ACTION_SHOW_DIALOG, new EntryAction(PoslinkEntry.CATEGORY, PoslinkEntry.ACTION_SHOW_DIALOG, "Show Dialog", ".POSLINK.SHOW_DIALOG", true));
        put(PoslinkEntry.ACTION_SHOW_THANK_YOU, new EntryAction(PoslinkEntry.CATEGORY, PoslinkEntry.ACTION_SHOW_THANK_YOU, "Show Thank You", ".POSLINK.SHOW_THANK_YOU", true));
        put(PoslinkEntry.ACTION_INPUT_TEXT, new EntryAction(PoslinkEntry.CATEGORY, PoslinkEntry.ACTION_INPUT_TEXT, "Input Text", ".POSLINK.INPUT_TEXT", true));
        put(PoslinkEntry.ACTION_SHOW_DIALOG_FORM, new EntryAction(PoslinkEntry.CATEGORY, PoslinkEntry.ACTION_SHOW_DIALOG_FORM, "Show Dialog Form", ".POSLINK.SHOW_DIALOG_FORM", true));
        put(PoslinkEntry.ACTION_SHOW_MESSAGE, new EntryAction(PoslinkEntry.CATEGORY, PoslinkEntry.ACTION_SHOW_MESSAGE, "Show Message", ".POSLINK.SHOW_MESSAGE", true));
        put(PoslinkEntry.ACTION_SHOW_ITEM, new EntryAction(PoslinkEntry.CATEGORY, PoslinkEntry.ACTION_SHOW_ITEM, "Show Item", ".POSLINK.SHOW_ITEM", true));
        put(PoslinkEntry.ACTION_SHOW_TEXT_BOX, new EntryAction(PoslinkEntry.CATEGORY, PoslinkEntry.ACTION_SHOW_TEXT_BOX, "Show Text Box", ".POSLINK.SHOW_TEXT_BOX", true));
        put(PoslinkEntry.ACTION_SHOW_SIGNATURE_BOX, new EntryAction(PoslinkEntry.CATEGORY, PoslinkEntry.ACTION_SHOW_SIGNATURE_BOX, "Show Signature Box", ".POSLINK.SHOW_SIGNATURE_BOX", true));
        put(PoslinkEntry.ACTION_SHOW_INPUT_TEXT_BOX, new EntryAction(PoslinkEntry.CATEGORY, PoslinkEntry.ACTION_SHOW_INPUT_TEXT_BOX, "Show Input Text Box", ".POSLINK.SHOW_INPUT_TEXT_BOX", true));
    }};

    static EntryAction getEntryAction(String action) {
        if(ENTRY_ACTION_MAP.containsKey(action)) return ENTRY_ACTION_MAP.get(action);
        return null;
    }

    static List<String> getEntryActionsByCategoryWithDefaultValues(String category) {
        List<String> entryActions = new ArrayList<>();
        for(Map.Entry<String, EntryAction> entry : ENTRY_ACTION_MAP.entrySet()) {
            if(entry.getValue().category.equals(category)) {
                entryActions.add(entry.getKey());
            }
        }
        return entryActions;
    }

}