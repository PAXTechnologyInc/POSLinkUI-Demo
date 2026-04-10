package com.paxus.pay.poslinkui.demo.utils.interfacefilter

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry
import com.pax.us.pay.ui.constant.entry.InformationEntry
import com.pax.us.pay.ui.constant.entry.PoslinkEntry
import com.pax.us.pay.ui.constant.entry.SecurityEntry
import com.pax.us.pay.ui.constant.entry.SignatureEntry
import com.paxus.pay.poslinkui.demo.entry.EntryGapActions

/**
 * Seeds Confirmation, Information, Signature, Security, and POSLink entry actions.
 */
internal fun MutableMap<String?, EntryAction?>.seedEntryActionsCatalogPartB() {
    put(
        ConfirmationEntry.ACTION_START_UI,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_START_UI,
            "Start UI",
            ".CONFIRMATION.START_UI",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_UNIFIED_MESSAGE,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_UNIFIED_MESSAGE,
            "Confirm Unified Message",
            ".CONFIRMATION.CONFIRM_UNIFIED_MESSAGE",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL,
            "Reverse Partial Approval",
            ".CONFIRMATION.REVERSE_PARTIAL_APPROVAL",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL,
            "Supplement Partial Approval",
            ".CONFIRMATION.SUPPLEMENT_PARTIAL_APPROVAL",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY_OFFLINE,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY_OFFLINE,
            "Confirm Online Retry Offline",
            ".CONFIRMATION.CONFIRM_ONLINE_RETRY_OFFLINE",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CHECK_CARD_PRESENT,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CHECK_CARD_PRESENT,
            "Check Card Present",
            ".CONFIRMATION.CHECK_CARD_PRESENT",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CHECK_DEACTIVATE_WARN,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CHECK_DEACTIVATE_WARN,
            "Check Deactivate Warn",
            ".CONFIRMATION.CHECK_DEACTIVATE_WARN",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE,
            "Confirm Batch Close",
            ".CONFIRMATION.CONFIRM_BATCH_CLOSE",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE_WITH_INCOMPLETE_TRANSACTION,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE_WITH_INCOMPLETE_TRANSACTION,
            "Confirm Batch Close With Incomplete Transaction",
            ".CONFIRMATION.CONFIRM_BATCH_CLOSE_WITH_INCOMPLETE_TRANSACTION",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_UNTIPPED,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_UNTIPPED,
            "Confirm Untipped",
            ".CONFIRMATION.CONFIRM_UNTIPPED",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_DUPLICATE_TRANS,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_DUPLICATE_TRANS,
            "Confirm Duplicate Trans",
            ".CONFIRMATION.CONFIRM_DUPLICATE_TRANS",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_SURCHARGE_FEE,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_SURCHARGE_FEE,
            "Confirm Surcharge Fee",
            ".CONFIRMATION.CONFIRM_SURCHARGE_FEE",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_SERVICE_FEE,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_SERVICE_FEE,
            "Confirm Service Fee",
            ".CONFIRMATION.CONFIRM_SERVICE_FEE",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_PRINTER_STATUS,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_PRINTER_STATUS,
            "Confirm Printer Status",
            ".CONFIRMATION.CONFIRM_PRINTER_STATUS",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_UPLOAD_TRANS,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_UPLOAD_TRANS,
            "Confirm Upload Trans",
            ".CONFIRMATION.CONFIRM_UPLOAD_TRANS",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_UPLOAD_RETRY,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_UPLOAD_RETRY,
            "Confirm Upload Retry",
            ".CONFIRMATION.CONFIRM_UPLOAD_RETRY",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_PRINT_FAILED_TRANS,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_PRINT_FAILED_TRANS,
            "Confirm Print Failed Trans",
            ".CONFIRMATION.CONFIRM_PRINT_FAILED_TRANS",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_PRINT_FPS,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_PRINT_FPS,
            "Confirm Print FPS",
            ".CONFIRMATION.CONFIRM_PRINT_FPS",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_DELETE_SF,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_DELETE_SF,
            "Confirm Delete SF",
            ".CONFIRMATION.CONFIRM_DELETE_SF",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_PRINT_CUSTOMER_COPY,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_PRINT_CUSTOMER_COPY,
            "Confirm Print Customer Copy",
            ".CONFIRMATION.CONFIRM_PRINT_CUSTOMER_COPY",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_BATCH_FOR_APPLICATION_UPDATE,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_BATCH_FOR_APPLICATION_UPDATE,
            "Confirm Batch For Application Update",
            ".CONFIRMATION.CONFIRM_BATCH_FOR_APPLICATION_UPDATE",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY,
            "Confirm Online Retry",
            ".CONFIRMATION.CONFIRM_ONLINE_RETRY",
            true
        )
    )
    put(
        EntryGapActions.ACTION_CONFIRM_DEBIT_TRANS,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            EntryGapActions.ACTION_CONFIRM_DEBIT_TRANS,
            "Confirm Debit Transaction",
            ".CONFIRMATION.CONFIRM_DEBIT_TRANS",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_ADJUST_TIP,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_ADJUST_TIP,
            "Confirm Adjust Tip",
            ".CONFIRMATION.CONFIRM_ADJUST_TIP",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT,
            "Confirm Card Process Result",
            ".CONFIRMATION.CONFIRM_CARD_PROCESS_RESULT",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_RECEIPT_SIGNATURE,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_RECEIPT_SIGNATURE,
            "Confirm Receipt Signature",
            ".CONFIRMATION.CONFIRM_RECEIPT_SIGNATURE",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_RECEIPT_VIEW,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_RECEIPT_VIEW,
            "Confirm Receipt View",
            ".CONFIRMATION.CONFIRM_RECEIPT_VIEW",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_BALANCE,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_BALANCE,
            "Confirm Balance",
            ".CONFIRMATION.CONFIRM_BALANCE",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_MERCHANT_SCOPE,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_MERCHANT_SCOPE,
            "Confirm Merchant Scope",
            ".CONFIRMATION.CONFIRM_MERCHANT_SCOPE",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_CARD_ENTRY_RETRY,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_CARD_ENTRY_RETRY,
            "Confirm Card Entry Retry",
            ".CONFIRMATION.CONFIRM_CARD_ENTRY_RETRY",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_SIGNATURE_MATCH,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_SIGNATURE_MATCH,
            "Confirm Signature Match",
            ".CONFIRMATION.CONFIRM_SIGNATURE_MATCH",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_DISPLAY_QR_CODE_RECEIPT,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_DISPLAY_QR_CODE_RECEIPT,
            "Display QR Code Receipt",
            ".CONFIRMATION.DISPLAY_QR_CODE_RECEIPT",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_DCC,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_DCC,
            "Confirm DCC",
            ".CONFIRMATION.CONFIRM_DCC",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_TAX_AMOUNT,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_TAX_AMOUNT,
            "Confirm Tax Amount",
            ".CONFIRMATION.CONFIRM_TAX_AMOUNT",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_TOTAL_AMOUNT,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_TOTAL_AMOUNT,
            "Confirm Total Amount",
            ".CONFIRMATION.CONFIRM_TOTAL_AMOUNT",
            true
        )
    )
    put(
        ConfirmationEntry.ACTION_CONFIRM_CASH_PAYMENT,
        EntryAction(
            ConfirmationEntry.CATEGORY,
            ConfirmationEntry.ACTION_CONFIRM_CASH_PAYMENT,
            "Confirm Cash Payment",
            ".CONFIRMATION.CONFIRM_CASH_PAYMENT",
            true
        )
    )

    // Information Entry
    put(
        InformationEntry.ACTION_DISPLAY_TRANS_INFORMATION,
        EntryAction(
            InformationEntry.CATEGORY,
            InformationEntry.ACTION_DISPLAY_TRANS_INFORMATION,
            "Display Transaction Information",
            ".INFORMATION.DISPLAY_TRANS_INFORMATION",
            true
        )
    )
    put(
        InformationEntry.ACTION_DISPLAY_APPROVE_MESSAGE,
        EntryAction(
            InformationEntry.CATEGORY,
            InformationEntry.ACTION_DISPLAY_APPROVE_MESSAGE,
            "Display Approve Message",
            ".INFORMATION.DISPLAY_APPROVE_MESSAGE",
            true
        )
    )
    put(
        EntryGapActions.ACTION_DISPLAY_VISA_INSTALLMENT_TRANSACTION_END,
        EntryAction(
            InformationEntry.CATEGORY,
            EntryGapActions.ACTION_DISPLAY_VISA_INSTALLMENT_TRANSACTION_END,
            "Display Visa Installment Transaction End",
            ".INFORMATION.DISPLAY_VISA_INSTALLMENT_TRANSACTION_END",
            true
        )
    )

    // Signature Entry
    put(
        SignatureEntry.ACTION_SIGNATURE,
        EntryAction(
            SignatureEntry.CATEGORY,
            SignatureEntry.ACTION_SIGNATURE,
            "Signature",
            ".SIGNATURE.SIGNATURE",
            true
        )
    )

    // Security Entry
    put(
        SecurityEntry.ACTION_INPUT_ACCOUNT,
        EntryAction(
            SecurityEntry.CATEGORY,
            SecurityEntry.ACTION_INPUT_ACCOUNT,
            "Input Account",
            ".SECURITY.INPUT_ACCOUNT",
            true
        )
    )
    put(
        SecurityEntry.ACTION_ENTER_PIN,
        EntryAction(
            SecurityEntry.CATEGORY,
            SecurityEntry.ACTION_ENTER_PIN,
            "Enter PIN",
            ".SECURITY.ENTER_PIN",
            true
        )
    )
    put(
        SecurityEntry.ACTION_ENTER_VCODE,
        EntryAction(
            SecurityEntry.CATEGORY,
            SecurityEntry.ACTION_ENTER_VCODE,
            "Enter V Code",
            ".SECURITY.ENTER_VCODE",
            true
        )
    )
    put(
        SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS,
        EntryAction(
            SecurityEntry.CATEGORY,
            SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS,
            "Enter Card Last 4 Digits",
            ".SECURITY.ENTER_CARD_LAST_4_DIGITS",
            true
        )
    )
    put(
        SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS,
        EntryAction(
            SecurityEntry.CATEGORY,
            SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS,
            "Enter Card All Digits",
            ".SECURITY.ENTER_CARD_ALL_DIGITS",
            true
        )
    )
    put(
        SecurityEntry.ACTION_ENTER_ADMINISTRATION_PASSWORD,
        EntryAction(
            SecurityEntry.CATEGORY,
            SecurityEntry.ACTION_ENTER_ADMINISTRATION_PASSWORD,
            "Enter Administration Password",
            ".SECURITY.ENTER_ADMINISTRATION_PASSWORD",
            true
        )
    )
    put(
        SecurityEntry.ACTION_MANAGE_INPUT_ACCOUNT,
        EntryAction(
            SecurityEntry.CATEGORY,
            SecurityEntry.ACTION_MANAGE_INPUT_ACCOUNT,
            "Manage Input Account",
            ".SECURITY.MANAGE_INPUT_ACCOUNT",
            true
        )
    )

    // Poslink Entry
    put(
        PoslinkEntry.ACTION_SHOW_DIALOG,
        EntryAction(
            PoslinkEntry.CATEGORY,
            PoslinkEntry.ACTION_SHOW_DIALOG,
            "Show Dialog",
            ".POSLINK.SHOW_DIALOG",
            true
        )
    )
    put(
        PoslinkEntry.ACTION_SHOW_THANK_YOU,
        EntryAction(
            PoslinkEntry.CATEGORY,
            PoslinkEntry.ACTION_SHOW_THANK_YOU,
            "Show Thank You",
            ".POSLINK.SHOW_THANK_YOU",
            true
        )
    )
    put(
        PoslinkEntry.ACTION_INPUT_TEXT,
        EntryAction(
            PoslinkEntry.CATEGORY,
            PoslinkEntry.ACTION_INPUT_TEXT,
            "Input Text",
            ".POSLINK.INPUT_TEXT",
            true
        )
    )
    put(
        PoslinkEntry.ACTION_SHOW_DIALOG_FORM,
        EntryAction(
            PoslinkEntry.CATEGORY,
            PoslinkEntry.ACTION_SHOW_DIALOG_FORM,
            "Show Dialog Form",
            ".POSLINK.SHOW_DIALOG_FORM",
            true
        )
    )
    put(
        PoslinkEntry.ACTION_SHOW_MESSAGE,
        EntryAction(
            PoslinkEntry.CATEGORY,
            PoslinkEntry.ACTION_SHOW_MESSAGE,
            "Show Message",
            ".POSLINK.SHOW_MESSAGE",
            true
        )
    )
    put(
        PoslinkEntry.ACTION_SHOW_ITEM,
        EntryAction(
            PoslinkEntry.CATEGORY,
            PoslinkEntry.ACTION_SHOW_ITEM,
            "Show Item",
            ".POSLINK.SHOW_ITEM",
            true
        )
    )
    put(
        PoslinkEntry.ACTION_SHOW_TEXT_BOX,
        EntryAction(
            PoslinkEntry.CATEGORY,
            PoslinkEntry.ACTION_SHOW_TEXT_BOX,
            "Show Text Box",
            ".POSLINK.SHOW_TEXT_BOX",
            true
        )
    )
    put(
        PoslinkEntry.ACTION_SHOW_SIGNATURE_BOX,
        EntryAction(
            PoslinkEntry.CATEGORY,
            PoslinkEntry.ACTION_SHOW_SIGNATURE_BOX,
            "Show Signature Box",
            ".POSLINK.SHOW_SIGNATURE_BOX",
            true
        )
    )
    put(
        PoslinkEntry.ACTION_SHOW_INPUT_TEXT_BOX,
        EntryAction(
            PoslinkEntry.CATEGORY,
            PoslinkEntry.ACTION_SHOW_INPUT_TEXT_BOX,
            "Show Input Text Box",
            ".POSLINK.SHOW_INPUT_TEXT_BOX",
            true
        )
    )
}
