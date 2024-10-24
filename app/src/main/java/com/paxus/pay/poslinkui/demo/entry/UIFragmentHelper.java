package com.paxus.pay.poslinkui.demo.entry;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.InformationEntry;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.SignatureEntry;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.status.BatchStatus;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.Uncategory;
import com.paxus.pay.poslinkui.demo.entry.confirmation.CheckCardPresentFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.CheckDeactivateWarnFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmAdjustTipFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmBalanceFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmBatchCloseFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmCardEntryRetryFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmCardProcessResultFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmCashPaymentFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmDCCFragment;
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
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmServiceFeeFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmSignatureMatchFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmSurchargeFeeFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmTotalAmountFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmUnifiedMessageFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmUntippedFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmUploadRetryFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmUploadTransFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.DisplayQRCodeReceiptFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ReversePartialApprovalFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.StartUIFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.SupplementPartialApprovalFragment;
import com.paxus.pay.poslinkui.demo.entry.information.DisplayTransInfoFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectAccountTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectAidFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectBatchTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectByPassFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectCardTypeFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectCashDiscountFragment;
import com.paxus.pay.poslinkui.demo.entry.option.SelectCofInitiatorFragment;
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
import com.paxus.pay.poslinkui.demo.entry.poslink.ShowInputTextBoxFragment;
import com.paxus.pay.poslinkui.demo.entry.poslink.ShowItemFragment;
import com.paxus.pay.poslinkui.demo.entry.poslink.ShowMessageFragment;
import com.paxus.pay.poslinkui.demo.entry.poslink.ShowSignatureBoxFragment;
import com.paxus.pay.poslinkui.demo.entry.poslink.ShowTextBoxFragment;
import com.paxus.pay.poslinkui.demo.entry.poslink.ShowThankYouFragment;
import com.paxus.pay.poslinkui.demo.entry.security.AdministratorPasswordFragment;
import com.paxus.pay.poslinkui.demo.entry.security.EnterCardAllDigitsFragment;
import com.paxus.pay.poslinkui.demo.entry.security.EnterCardLast4DigitsFragment;
import com.paxus.pay.poslinkui.demo.entry.security.EnterVcodeFragment;
import com.paxus.pay.poslinkui.demo.entry.security.InputAccountFragment;
import com.paxus.pay.poslinkui.demo.entry.security.ManageInputAccountFragment;
import com.paxus.pay.poslinkui.demo.entry.security.PINFragment;
import com.paxus.pay.poslinkui.demo.entry.signature.SignatureFragment;
import com.paxus.pay.poslinkui.demo.entry.text.AVSFragment;
import com.paxus.pay.poslinkui.demo.entry.text.EnterOrigTransDateFragment;
import com.paxus.pay.poslinkui.demo.entry.text.ExpiryFragment;
import com.paxus.pay.poslinkui.demo.entry.text.amount.AmountFragment;
import com.paxus.pay.poslinkui.demo.entry.text.amount.CashbackFragment;
import com.paxus.pay.poslinkui.demo.entry.text.amount.FuelAmountFragment;
import com.paxus.pay.poslinkui.demo.entry.text.amount.TaxAmountFragment;
import com.paxus.pay.poslinkui.demo.entry.text.amount.TipFragment;
import com.paxus.pay.poslinkui.demo.entry.text.amount.TotalAmountFragment;
import com.paxus.pay.poslinkui.demo.entry.text.fleet.FleetFragment;
import com.paxus.pay.poslinkui.demo.entry.text.fsa.FSAFragment;
import com.paxus.pay.poslinkui.demo.entry.text.number.CsPhoneNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.number.GuestNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.number.MerchantTaxIdFragment;
import com.paxus.pay.poslinkui.demo.entry.text.number.PhoneNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.number.PromptRestrictionCodeFragment;
import com.paxus.pay.poslinkui.demo.entry.text.number.TableNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.number.TransNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.numbertext.ClerkIdFragment;
import com.paxus.pay.poslinkui.demo.entry.text.numbertext.DestZipcodeFragment;
import com.paxus.pay.poslinkui.demo.entry.text.numbertext.InvoiceNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.numbertext.MerchantReferenceNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.numbertext.OctReferenceNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.numbertext.ReferenceNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.numbertext.ServerIdFragment;
import com.paxus.pay.poslinkui.demo.entry.text.numbertext.VoucherDataFragment;
import com.paxus.pay.poslinkui.demo.entry.text.numbertext.ZipcodeFragment;
import com.paxus.pay.poslinkui.demo.entry.text.text.AddressFragment;
import com.paxus.pay.poslinkui.demo.entry.text.text.AuthFragment;
import com.paxus.pay.poslinkui.demo.entry.text.text.CustomerCodeFragment;
import com.paxus.pay.poslinkui.demo.entry.text.text.OrderNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.text.OrigTransIdentifierFragment;
import com.paxus.pay.poslinkui.demo.entry.text.text.PoNumberFragment;
import com.paxus.pay.poslinkui.demo.entry.text.text.ProdDescFragment;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Yanina.Yang on 5/12/2022.
 */
public class UIFragmentHelper {
    private UIFragmentHelper() {

    }

    private static final Map<String, Class<? extends Fragment>> TEXT_FRAGMENT_MAP = new HashMap<String, Class<? extends Fragment>>() {
        {
            put(TextEntry.ACTION_ENTER_AMOUNT, AmountFragment.class);
            put(TextEntry.ACTION_ENTER_FUEL_AMOUNT, FuelAmountFragment.class);
            put(TextEntry.ACTION_ENTER_TAX_AMOUNT, TaxAmountFragment.class);
            put(TextEntry.ACTION_ENTER_CASH_BACK, CashbackFragment.class);
            put(TextEntry.ACTION_ENTER_TIP, TipFragment.class);
            put(TextEntry.ACTION_ENTER_TOTAL_AMOUNT, TotalAmountFragment.class);
            put(TextEntry.ACTION_ENTER_CLERK_ID, ClerkIdFragment.class);
            put(TextEntry.ACTION_ENTER_SERVER_ID, ServerIdFragment.class);
            put(TextEntry.ACTION_ENTER_TABLE_NUMBER, TableNumberFragment.class);
            put(TextEntry.ACTION_ENTER_CS_PHONE_NUMBER, CsPhoneNumberFragment.class);
            put(TextEntry.ACTION_ENTER_PHONE_NUMBER, PhoneNumberFragment.class);
            put(TextEntry.ACTION_ENTER_GUEST_NUMBER, GuestNumberFragment.class);
            put(TextEntry.ACTION_ENTER_MERCHANT_TAX_ID, MerchantTaxIdFragment.class);
            put(TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE, PromptRestrictionCodeFragment.class);
            put(TextEntry.ACTION_ENTER_TRANS_NUMBER, TransNumberFragment.class);
            put(TextEntry.ACTION_ENTER_ADDRESS, AddressFragment.class);
            put(TextEntry.ACTION_ENTER_AUTH, AuthFragment.class);
            put(TextEntry.ACTION_ENTER_CUSTOMER_CODE, CustomerCodeFragment.class);
            put(TextEntry.ACTION_ENTER_ORDER_NUMBER, OrderNumberFragment.class);
            put(TextEntry.ACTION_ENTER_PO_NUMBER, PoNumberFragment.class);
            put(TextEntry.ACTION_ENTER_PROD_DESC, ProdDescFragment.class);
            put(TextEntry.ACTION_ENTER_ZIPCODE, ZipcodeFragment.class);
            put(TextEntry.ACTION_ENTER_DEST_ZIPCODE, DestZipcodeFragment.class);
            put(TextEntry.ACTION_ENTER_INVOICE_NUMBER, InvoiceNumberFragment.class);
            put(TextEntry.ACTION_ENTER_VOUCHER_DATA, VoucherDataFragment.class);
            put(TextEntry.ACTION_ENTER_REFERENCE_NUMBER, ReferenceNumberFragment.class);
            put(TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER, MerchantReferenceNumberFragment.class);
            put(TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER, OctReferenceNumberFragment.class);
            put(TextEntry.ACTION_ENTER_AVS_DATA, AVSFragment.class);
            put(TextEntry.ACTION_ENTER_EXPIRY_DATE, ExpiryFragment.class);
            put(TextEntry.ACTION_ENTER_FSA_DATA, FSAFragment.class);
            put(TextEntry.ACTION_ENTER_FLEET_DATA, FleetFragment.class);
            put(TextEntry.ACTION_ENTER_ORIG_DATE, EnterOrigTransDateFragment.class);
            put(TextEntry.ACTION_ENTER_ORIGINAL_TRANSACTION_IDENTIFIER, OrigTransIdentifierFragment.class);
        }
    };

    private static final Map<String, Class<? extends Fragment>> SECURE_FRAGMENT_MAP = new HashMap<String, Class<? extends Fragment>>() {
        {
            put(SecurityEntry.ACTION_INPUT_ACCOUNT, InputAccountFragment.class);
            put(SecurityEntry.ACTION_MANAGE_INPUT_ACCOUNT, ManageInputAccountFragment.class);
            put(SecurityEntry.ACTION_ENTER_VCODE, EnterVcodeFragment.class);
            put(SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS, EnterCardLast4DigitsFragment.class);
            put(SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS, EnterCardAllDigitsFragment.class);
            put(SecurityEntry.ACTION_ENTER_PIN, PINFragment.class);
            put(SecurityEntry.ACTION_ENTER_ADMINISTRATION_PASSWORD, AdministratorPasswordFragment.class);
            put("com.pax.us.pay.action.ADMINISTRATOR_PASSWORD", AdministratorPasswordFragment.class);
        }
    };

    private static final Map<String, Class<? extends Fragment>> INFORMATION_FRAGMENT_MAP = new HashMap<String, Class<? extends Fragment>>() {
        {
            put(InformationEntry.ACTION_DISPLAY_TRANS_INFORMATION, DisplayTransInfoFragment.class);
        }
    };

    private static final Map<String, Class<? extends Fragment>> SIGNATURE_FRAGMENT_MAP = new HashMap<String, Class<? extends Fragment>>() {
        {
            put(SignatureEntry.ACTION_SIGNATURE, SignatureFragment.class);
        }
    };

    private static final Map<String, Class<? extends Fragment>> CONFIRM_FRAGMENT_MAP = new HashMap<String, Class<? extends Fragment>>() {
        {
            put(ConfirmationEntry.ACTION_CONFIRM_RECEIPT_VIEW, ConfirmReceiptViewFragment.class);
            put(ConfirmationEntry.ACTION_DISPLAY_QR_CODE_RECEIPT, DisplayQRCodeReceiptFragment.class);

            put(ConfirmationEntry.ACTION_CONFIRM_UNIFIED_MESSAGE, ConfirmUnifiedMessageFragment.class);
            put(ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL, ReversePartialApprovalFragment.class);
            put(ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL, SupplementPartialApprovalFragment.class);
            put(ConfirmationEntry.ACTION_CHECK_CARD_PRESENT, CheckCardPresentFragment.class);
            put(ConfirmationEntry.ACTION_CHECK_DEACTIVATE_WARN, CheckDeactivateWarnFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE, ConfirmBatchCloseFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_UNTIPPED, ConfirmUntippedFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_DUPLICATE_TRANS, ConfirmDuplicateTransFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_SURCHARGE_FEE, ConfirmSurchargeFeeFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_SERVICE_FEE, ConfirmServiceFeeFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_PRINTER_STATUS, ConfirmPrinterStatusFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_UPLOAD_TRANS, ConfirmUploadTransFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_PRINT_FAILED_TRANS, ConfirmPrintFailedTransFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_UPLOAD_RETRY, ConfirmUploadRetryFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_PRINT_FPS, ConfirmPrintFpsFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_DELETE_SF, ConfirmDeleteSfFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_PRINT_CUSTOMER_COPY, ConfirmPrintCustomerCopyFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY, ConfirmOnlineRetryFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_ADJUST_TIP, ConfirmAdjustTipFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT, ConfirmCardProcessResultFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_RECEIPT_SIGNATURE, ConfirmReceiptSignatureFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_MERCHANT_SCOPE, ConfirmMerchantScopeFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_CARD_ENTRY_RETRY, ConfirmCardEntryRetryFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_SIGNATURE_MATCH, ConfirmSignatureMatchFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_DCC, ConfirmDCCFragment.class);
            put(ConfirmationEntry.ACTION_START_UI, StartUIFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_TOTAL_AMOUNT, ConfirmTotalAmountFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_BALANCE, ConfirmBalanceFragment.class);
            put(ConfirmationEntry.ACTION_CONFIRM_CASH_PAYMENT, ConfirmCashPaymentFragment.class);
        }
    };

    private static final Map<String, Class<? extends Fragment>> POSLINK_FRAGMENT_MAP = new HashMap<String, Class<? extends Fragment>>() {
        {
            put(PoslinkEntry.ACTION_INPUT_TEXT, InputTextFragment.class);
            put(PoslinkEntry.ACTION_SHOW_THANK_YOU, ShowThankYouFragment.class);
            put(PoslinkEntry.ACTION_SHOW_DIALOG, ShowDialogFragment.class);
            put(PoslinkEntry.ACTION_SHOW_DIALOG_FORM, ShowDialogFormFragment.class);
            put(PoslinkEntry.ACTION_SHOW_MESSAGE, ShowMessageFragment.class);
            put(PoslinkEntry.ACTION_SHOW_ITEM, ShowItemFragment.class);
            put(PoslinkEntry.ACTION_SHOW_SIGNATURE_BOX, ShowSignatureBoxFragment.class);
            put(PoslinkEntry.ACTION_SHOW_INPUT_TEXT_BOX, ShowInputTextBoxFragment.class);
            put(PoslinkEntry.ACTION_SHOW_TEXT_BOX, ShowTextBoxFragment.class);
        }
    };

    private static final Map<String, Class<? extends Fragment>> OPTIONS_FRAGMENT_MAP = new HashMap<String, Class<? extends Fragment>>() {
        {
            put(OptionEntry.ACTION_SELECT_ORIG_CURRENCY, SelectOrigCurrencyFragment.class);
            put(OptionEntry.ACTION_SELECT_MERCHANT, SelectMerchantFragment.class);
            put(OptionEntry.ACTION_SELECT_LANGUAGE, SelectLanguageFragment.class);
            put(OptionEntry.ACTION_SELECT_ACCOUNT_TYPE, SelectAccountTypeFragment.class);
            put(OptionEntry.ACTION_SELECT_REPORT_TYPE, SelectReportTypeFragment.class);
            put(OptionEntry.ACTION_SELECT_EDC_GROUP, SelectEdcGroupFragment.class);
            put(OptionEntry.ACTION_SELECT_BATCH_TYPE, SelectBatchTypeFragment.class);
            put(OptionEntry.ACTION_SELECT_SEARCH_CRITERIA, SelectSearchCriteriaFragment.class);
            put(OptionEntry.ACTION_SELECT_EDC_TYPE, SelectEdcTypeFragment.class);
            put(OptionEntry.ACTION_SELECT_TRANS_TYPE, SelectTransTypeFragment.class);
            put(OptionEntry.ACTION_SELECT_CARD_TYPE, SelectCardTypeFragment.class);
            put(OptionEntry.ACTION_SELECT_DUPLICATE_OVERRIDE, SelectDuplicateOverrideFragment.class);
            put(OptionEntry.ACTION_SELECT_TAX_REASON, SelectTaxReasonFragment.class);
            put(OptionEntry.ACTION_SELECT_MOTO_TYPE, SelectMotoTypeFragment.class);
            put(OptionEntry.ACTION_SELECT_REFUND_REASON, SelectRefundReasonFragment.class);
            put(OptionEntry.ACTION_SELECT_SUB_TRANS_TYPE, SelectSubTransTypeFragment.class);
            put(OptionEntry.ACTION_SELECT_AID, SelectAidFragment.class);
            put(OptionEntry.ACTION_SELECT_BY_PASS, SelectByPassFragment.class);
            put(OptionEntry.ACTION_SELECT_EBT_TYPE, SelectEbtTypeFragment.class);
            put(OptionEntry.ACTION_SELECT_COF_INITIATOR, SelectCofInitiatorFragment.class);
            put(OptionEntry.ACTION_SELECT_CASH_DISCOUNT, SelectCashDiscountFragment.class);
        }
    };

    /**
     * Create all entry fragments
     *
     * @param intent Intent
     * @return Fragment
     */
    public static Fragment createFragment(Intent intent) {
        String action = intent.getAction();
        Set<String> categories = intent.getCategories();
        if (action != null && categories != null) {
            if (categories.contains(TextEntry.CATEGORY)) {
                return createFragment(TEXT_FRAGMENT_MAP.get(action), intent);
            } else if (categories.contains(SecurityEntry.CATEGORY)) {
                return createFragment(SECURE_FRAGMENT_MAP.get(action), intent);
            } else if (categories.contains(InformationEntry.CATEGORY)) {
                return createFragment(INFORMATION_FRAGMENT_MAP.get(action), intent);
            } else if (categories.contains(OptionEntry.CATEGORY)) {
                return createFragment(OPTIONS_FRAGMENT_MAP.get(action), intent);
            } else if (categories.contains(SignatureEntry.CATEGORY)) {
                return createFragment(SIGNATURE_FRAGMENT_MAP.get(action), intent);
            } else if (categories.contains(ConfirmationEntry.CATEGORY)) {
                return createFragment(CONFIRM_FRAGMENT_MAP.get(action), intent);
            } else if (categories.contains(PoslinkEntry.CATEGORY)) {
                return createFragment(POSLINK_FRAGMENT_MAP.get(action), intent);
            }
        }
        return null;
    }

    private static Fragment createFragment(Class<? extends Fragment> clz, Intent intent) {
        if (clz == null) {
            return null;
        }
        Fragment fragment = null;
        try {
            fragment = clz.getConstructor().newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
            bundle.putAll(intent.getExtras()!=null ? intent.getExtras() : new Bundle());
            fragment.setArguments(bundle);
            return fragment;
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }
}
