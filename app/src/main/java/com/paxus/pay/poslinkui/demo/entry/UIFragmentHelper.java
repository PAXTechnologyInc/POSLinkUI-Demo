package com.paxus.pay.poslinkui.demo.entry;

import android.content.Intent;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.InformationEntry;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.SignatureEntry;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.Uncategory;
import com.paxus.pay.poslinkui.demo.entry.confirmation.CheckCardPresentFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.CheckDeactivateWarnFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmAdjustTipFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmBatchCloseFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmCardEntryRetryFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmCardProcessResultFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmDeleteSfFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmDuplicateTransFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmMerchantScopeFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmOnlineRetryFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmPrintCustomerCopyFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmPrintFailedTransFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmPrintFpsFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmPrinterStatusFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmReceiptSignatureFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmReceiptViewFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmSignatureMatchFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmUnifiedMessageFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmUntippedFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmUploadRetryFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmUploadTransFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmationSurchargeFeeDialogFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ReversePartialApprovalFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.SupplementPartialApprovalFragment;
import com.paxus.pay.poslinkui.demo.entry.information.DisplayTransInfoFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectAccountTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectAidFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectBatchTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectByPassFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectCardTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectDuplicateOverrideFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectEbtTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectEdcGroupFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectEdcTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectLanguageFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectMerchantFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectMotoTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectOrigCurrencyFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectRefundReasonFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectReportTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectSearchCriteriaFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectSubTransTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectTaxReasonFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectTransTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.poslink.InputTextFragment;
import com.paxus.pay.poslinkui.demo.entry.poslink.ShowDialogFormFragment;
import com.paxus.pay.poslinkui.demo.entry.poslink.ShowDialogFragment;
import com.paxus.pay.poslinkui.demo.entry.poslink.ShowMessageFragment;
import com.paxus.pay.poslinkui.demo.entry.poslink.ShowThankYouFragment;
import com.paxus.pay.poslinkui.demo.entry.security.EnterCardAllDigitsFragment;
import com.paxus.pay.poslinkui.demo.entry.security.EnterCardLast4DigitsFragment;
import com.paxus.pay.poslinkui.demo.entry.security.EnterVcodeFragment;
import com.paxus.pay.poslinkui.demo.entry.security.InputAccountFragment;
import com.paxus.pay.poslinkui.demo.entry.security.ManageInputAccountFragment;
import com.paxus.pay.poslinkui.demo.entry.security.PINFragment;
import com.paxus.pay.poslinkui.demo.entry.signature.SignatureFragment;
import com.paxus.pay.poslinkui.demo.entry.text.AVSFragment;
import com.paxus.pay.poslinkui.demo.entry.text.CashbackFragment;
import com.paxus.pay.poslinkui.demo.entry.text.AddressFragment;
import com.paxus.pay.poslinkui.demo.entry.text.AmountFragment;
import com.paxus.pay.poslinkui.demo.entry.text.AuthFragment;
import com.paxus.pay.poslinkui.demo.entry.text.CsPhoneNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.ClerkIdFragment;
import com.paxus.pay.poslinkui.demo.entry.text.CustomerCodeFragment;
import com.paxus.pay.poslinkui.demo.entry.text.DestZipcodeFragment;
import com.paxus.pay.poslinkui.demo.entry.text.FuelAmountFragment;
import com.paxus.pay.poslinkui.demo.entry.text.GuestNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.InvoiceNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.MerchantReferenceNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.MerchantTaxIdFragment;
import com.paxus.pay.poslinkui.demo.entry.text.OctReferenceNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.OrderNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.PhoneNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.PoNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.ProdDescFragment;
import com.paxus.pay.poslinkui.demo.entry.text.PromptRestrictionCodeFragment;
import com.paxus.pay.poslinkui.demo.entry.text.ReferenceNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.ServerIdFragment;
import com.paxus.pay.poslinkui.demo.entry.text.TableNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.TaxAmountFragment;
import com.paxus.pay.poslinkui.demo.entry.text.TransNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.VoucherDataFragment;
import com.paxus.pay.poslinkui.demo.entry.text.ZipcodeFragment;
import com.paxus.pay.poslinkui.demo.entry.text.ExpiryFragment;
import com.paxus.pay.poslinkui.demo.entry.text.FSAFragment;
import com.paxus.pay.poslinkui.demo.entry.text.FleetFragment;
import com.paxus.pay.poslinkui.demo.entry.text.TipFragment;
import com.paxus.pay.poslinkui.demo.entry.text.TotalAmountFragment;
import com.paxus.pay.poslinkui.demo.status.InformationDialogFragment;
import com.paxus.pay.poslinkui.demo.status.TransCompletedDialogFragment;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.util.Set;

/**
 * Created by Yanina.Yang on 5/12/2022.
 */
public class UIFragmentHelper {
    private UIFragmentHelper(){

    }

    /**
     * Create all dialogs
     * @param intent Intent
     * @return DialogFragment
     */
    public static DialogFragment createDialogFragment(Intent intent){
        String action = intent.getAction();
        Set<String> categories = intent.getCategories();
        if(action != null) {
            if(categories != null) {
                if (categories.contains(OptionEntry.CATEGORY)) {
                    switch (action) {
                        case OptionEntry.ACTION_SELECT_ORIG_CURRENCY:
                            return SelectOrigCurrencyFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_MERCHANT:
                            return SelectMerchantFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_LANGUAGE:
                            return SelectLanguageFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_ACCOUNT_TYPE:
                            return SelectAccountTypeFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_REPORT_TYPE:
                            return SelectReportTypeFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_EDC_GROUP:
                            return SelectEdcGroupFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_BATCH_TYPE:
                            return SelectBatchTypeFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_SEARCH_CRITERIA:
                            return SelectSearchCriteriaFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_EDC_TYPE:
                            return SelectEdcTypeFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_TRANS_TYPE:
                            return SelectTransTypeFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_CARD_TYPE:
                            return SelectCardTypeFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_DUPLICATE_OVERRIDE:
                            return SelectDuplicateOverrideFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_TAX_REASON:
                            return SelectTaxReasonFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_MOTO_TYPE:
                            return SelectMotoTypeFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_REFUND_REASON:
                            return SelectRefundReasonFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_SUB_TRANS_TYPE:
                            return SelectSubTransTypeFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_AID:
                            return SelectAidFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_BY_PASS:
                            return SelectByPassFragment.newInstance(intent);
                        case OptionEntry.ACTION_SELECT_EBT_TYPE:
                            return SelectEbtTypeFragment.newInstance(intent);
                    }
                } else if (categories.contains(ConfirmationEntry.CATEGORY)) {
                    switch (action) {
                        case ConfirmationEntry.ACTION_CONFIRM_UNIFIED_MESSAGE:
                            return ConfirmUnifiedMessageFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL:
                            return ReversePartialApprovalFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL:
                            return SupplementPartialApprovalFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CHECK_CARD_PRESENT:
                            return CheckCardPresentFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CHECK_DEACTIVATE_WARN:
                            return CheckDeactivateWarnFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE:
                            return ConfirmBatchCloseFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_UNTIPPED:
                            return ConfirmUntippedFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_DUPLICATE_TRANS:
                            return ConfirmDuplicateTransFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_SURCHARGE_FEE:
                            return ConfirmationSurchargeFeeDialogFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_PRINTER_STATUS:
                            return ConfirmPrinterStatusFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_UPLOAD_TRANS:
                            return ConfirmUploadTransFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_UPLOAD_RETRY:
                            return ConfirmUploadRetryFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_PRINT_FAILED_TRANS:
                            return ConfirmPrintFailedTransFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_PRINT_FPS:
                            return ConfirmPrintFpsFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_DELETE_SF:
                            return ConfirmDeleteSfFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_PRINT_CUSTOMER_COPY:
                            return ConfirmPrintCustomerCopyFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY:
                            return ConfirmOnlineRetryFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_ADJUST_TIP:
                            return ConfirmAdjustTipFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT:
                            return ConfirmCardProcessResultFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_RECEIPT_SIGNATURE:
                            return ConfirmReceiptSignatureFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_MERCHANT_SCOPE:
                            return ConfirmMerchantScopeFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_CARD_ENTRY_RETRY:
                            return ConfirmCardEntryRetryFragment.newInstance(intent);
                        case ConfirmationEntry.ACTION_CONFIRM_SIGNATURE_MATCH:
                            return ConfirmSignatureMatchFragment.newInstance(intent);
                    }
                }
            }

            switch (action){
                case InformationStatus.TRANS_COMPLETED: {
                    if(categories == null){
                        Logger.e("WARNING:\""+action+"\" Category is missing!");
                    }
                    return TransCompletedDialogFragment.newInstance(intent);
                }
                case InformationStatus.TRANS_ONLINE_STARTED:
                case InformationStatus.EMV_TRANS_ONLINE_STARTED:
                case InformationStatus.RKI_STARTED:
                case InformationStatus.DCC_ONLINE_STARTED:
                case InformationStatus.PINPAD_CONNECTION_STARTED:
                case CardStatus.CARD_REMOVAL_REQUIRED:
                case CardStatus.CARD_QUICK_REMOVAL_REQUIRED:
                case CardStatus.CARD_SWIPE_REQUIRED:
                case CardStatus.CARD_INSERT_REQUIRED:
                case CardStatus.CARD_TAP_REQUIRED:
                case CardStatus.CARD_PROCESS_STARTED: {
                    if (categories == null) {
                        Logger.e("WARNING:\"" + action + "\" Category is missing!");
                    }
                    return InformationDialogFragment.newInstance(action);
                }
                case Uncategory.PRINT_STARTED:
                case Uncategory.FILE_UPDATE_STARTED:
                case Uncategory.FCP_FILE_UPDATE_STARTED:
                case Uncategory.LOG_UPLOAD_STARTED:
                case Uncategory.LOG_UPLOAD_CONNECTED:
                case Uncategory.LOG_UPLOAD_UPLOADING:
                case Uncategory.CAPK_UPDATE_STARTED:
                    return InformationDialogFragment.newInstance(action);
            }
        }
        return null;
    }

    /**
     * Create dialog tag cuz we use it to close dialog
     * @param action action
     * @return tag
     */
    public static String createStatusDialogTag(String action){
        switch (action) {
            case InformationStatus.TRANS_COMPLETED:
                return "trans_completed";
            case InformationStatus.TRANS_ONLINE_STARTED:
            case InformationStatus.TRANS_ONLINE_FINISHED:
                return "trans_online";
            case InformationStatus.EMV_TRANS_ONLINE_STARTED:
            case InformationStatus.EMV_TRANS_ONLINE_FINISHED:
                return "emv_trans_online";
            case InformationStatus.RKI_STARTED:
            case InformationStatus.RKI_FINISHED:
                return "rki_process";
            case InformationStatus.DCC_ONLINE_STARTED:
            case InformationStatus.DCC_ONLINE_FINISHED:
                return "dcc_online";
            case InformationStatus.PINPAD_CONNECTION_STARTED:
            case InformationStatus.PINPAD_CONNECTION_FINISHED:
                return "pin_pad_connection";
            case CardStatus.CARD_REMOVAL_REQUIRED:
            case CardStatus.CARD_QUICK_REMOVAL_REQUIRED:
            case CardStatus.CARD_REMOVED:
                return "remove_card";
            case CardStatus.CARD_PROCESS_STARTED:
            case CardStatus.CARD_PROCESS_COMPLETED:
                return "card_process";
            case Uncategory.PRINT_STARTED:
            case Uncategory.PRINT_COMPLETED:
                return "print_process";
            case Uncategory.FILE_UPDATE_STARTED:
            case Uncategory.FILE_UPDATE_COMPLETED:
                return "file_update";
            case Uncategory.FCP_FILE_UPDATE_STARTED:
            case Uncategory.FCP_FILE_UPDATE_COMPLETED:
                return "fcp_file_update";
            case Uncategory.CAPK_UPDATE_STARTED:
            case Uncategory.CAPK_UPDATE_COMPLETED:
                return "capk_update";
            case Uncategory.LOG_UPLOAD_STARTED:
            case Uncategory.LOG_UPLOAD_CONNECTED:
            case Uncategory.LOG_UPLOAD_UPLOADING:
            case Uncategory.LOG_UPLOAD_COMPLETED:
                return "log_upload";
            default:
                return null;
        }
    }

    /**
     * Create all entry fragments which not dialog
     * @param intent Intent
     * @return DialogFragment
     */
    public static Fragment createFragment(Intent intent){
        String action = intent.getAction();
        Set<String> categories = intent.getCategories();
        if(action != null && categories != null){
            if(categories.contains(TextEntry.CATEGORY)) {
                switch (action) {
                    case TextEntry.ACTION_ENTER_AMOUNT:
                        return AmountFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_FUEL_AMOUNT:
                        return FuelAmountFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_TAX_AMOUNT:
                        return TaxAmountFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_CASH_BACK:
                        return CashbackFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_TIP:
                        return TipFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_TOTAL_AMOUNT:
                        return TotalAmountFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_CLERK_ID:
                        return ClerkIdFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_SERVER_ID:
                        return ServerIdFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_TABLE_NUMBER:
                        return TableNumberFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_CS_PHONE_NUMBER:
                        return CsPhoneNumberFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_PHONE_NUMBER:
                        return PhoneNumberFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_GUEST_NUMBER:
                        return GuestNumberFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_MERCHANT_TAX_ID:
                        return MerchantTaxIdFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE:
                        return PromptRestrictionCodeFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_TRANS_NUMBER:
                        return TransNumberFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_ADDRESS:
                        return AddressFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_AUTH:
                        return AuthFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_CUSTOMER_CODE:
                        return CustomerCodeFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_ORDER_NUMBER:
                        return OrderNumberFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_PO_NUMBER:
                        return PoNumberFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_PROD_DESC:
                        return ProdDescFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_ZIPCODE:
                        return ZipcodeFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_DEST_ZIPCODE:
                        return DestZipcodeFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_INVOICE_NUMBER:
                        return InvoiceNumberFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_VOUCHER_DATA:
                        return VoucherDataFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_REFERENCE_NUMBER:
                        return ReferenceNumberFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER:
                        return MerchantReferenceNumberFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER:
                        return OctReferenceNumberFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_AVS_DATA:
                        return AVSFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_EXPIRY_DATE:
                        return ExpiryFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_FSA_DATA:
                        return FSAFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_FLEET_DATA:
                        return FleetFragment.newInstance(intent);
                    default:
                        return null;
                }
            }
            else if(categories.contains(SecurityEntry.CATEGORY)){
                switch (action) {
                    case SecurityEntry.ACTION_INPUT_ACCOUNT:
                        return InputAccountFragment.newInstance(intent);
                    case SecurityEntry.ACTION_MANAGE_INPUT_ACCOUNT:
                        return ManageInputAccountFragment.newInstance(intent);
                    case SecurityEntry.ACTION_ENTER_VCODE:
                        return EnterVcodeFragment.newInstance(intent);
                    case SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS:
                        return EnterCardLast4DigitsFragment.newInstance(intent);
                    case SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS:
                        return EnterCardAllDigitsFragment.newInstance(intent);
                    case SecurityEntry.ACTION_ENTER_PIN:
                        return PINFragment.newInstance(intent);
                    default:
                        return null;
                }
            }else if(categories.contains(InformationEntry.CATEGORY)){
                if(InformationEntry.ACTION_DISPLAY_TRANS_INFORMATION.equals(action)){
                    return DisplayTransInfoFragment.newInstance(intent);
                }

            }else if(categories.contains(SignatureEntry.CATEGORY)){
                if(SignatureEntry.ACTION_SIGNATURE.equals(action)){
                    return SignatureFragment.newInstance(intent);
                }

            } else if(categories.contains(ConfirmationEntry.CATEGORY)){
                if(ConfirmationEntry.ACTION_CONFIRM_RECEIPT_VIEW.equals(action)){
                    return ConfirmReceiptViewFragment.newInstance(intent);
                }
            } else if(categories.contains(PoslinkEntry.CATEGORY)){
                switch (action) {
                    case PoslinkEntry.ACTION_INPUT_TEXT:
                        return InputTextFragment.newInstance(intent);
                    case PoslinkEntry.ACTION_SHOW_THANK_YOU:
                        return ShowThankYouFragment.newInstance(intent);
                    case PoslinkEntry.ACTION_SHOW_DIALOG:
                        return ShowDialogFragment.newInstance(intent);
                    case PoslinkEntry.ACTION_SHOW_DIALOG_FORM:
                        return ShowDialogFormFragment.newInstance(intent);
                    case PoslinkEntry.ACTION_SHOW_MESSAGE:
                        return ShowMessageFragment.newInstance(intent);
                    default:
                        return null;
                }

            }
        }
        return null;
    }

    public static void showDialog(FragmentManager fragmentManager, DialogFragment dialogFragment, String tag){
        dialogFragment.show(fragmentManager, tag);
    }

    public static void closeDialog(FragmentManager fragmentManager, String tag){
        Fragment prev = fragmentManager.findFragmentByTag(tag);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }
}
