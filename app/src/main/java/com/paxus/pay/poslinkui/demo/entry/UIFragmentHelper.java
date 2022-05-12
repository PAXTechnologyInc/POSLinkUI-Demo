package com.paxus.pay.poslinkui.demo.entry;

import android.content.Intent;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.InformationEntry;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.SignatureEntry;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmationDialogFragment;
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmationSurchargeFeeDialogFragment;
import com.paxus.pay.poslinkui.demo.entry.information.DisplayTransInfoFragment;
import com.paxus.pay.poslinkui.demo.entry.option.OptionsDialogFragment;
import com.paxus.pay.poslinkui.demo.entry.security.InputAccountFragment;
import com.paxus.pay.poslinkui.demo.entry.security.PINFragment;
import com.paxus.pay.poslinkui.demo.entry.security.SecurityFragment;
import com.paxus.pay.poslinkui.demo.entry.signature.SignatureFragment;
import com.paxus.pay.poslinkui.demo.entry.text.AVSFragment;
import com.paxus.pay.poslinkui.demo.entry.text.AmountFragment;
import com.paxus.pay.poslinkui.demo.entry.text.CashbackFragment;
import com.paxus.pay.poslinkui.demo.entry.text.ExpiryFragment;
import com.paxus.pay.poslinkui.demo.entry.text.NumFragment;
import com.paxus.pay.poslinkui.demo.entry.text.NumTextFragment;
import com.paxus.pay.poslinkui.demo.entry.text.TextFragment;
import com.paxus.pay.poslinkui.demo.entry.text.TipFragment;
import com.paxus.pay.poslinkui.demo.entry.text.TotalAmountFragment;
import com.paxus.pay.poslinkui.demo.status.InformationDialogFragment;
import com.paxus.pay.poslinkui.demo.status.TransCompletedDialogFragment;

import java.util.Set;

/**
 * Created by Yanina.Yang on 5/12/2022.
 */
class UIFragmentHelper {
    private UIFragmentHelper(){

    }

    public static DialogFragment createDialogFragment(Intent intent){
        String action = intent.getAction();
        Set<String> categories = intent.getCategories();
        if(action != null) {
            if (categories.contains(OptionEntry.CATEGORY)) {
                return OptionsDialogFragment.newInstance(intent);
            } else if(categories.contains(ConfirmationEntry.CATEGORY)){
                if(ConfirmationEntry.ACTION_CONFIRM_SURCHARGE_FEE.equals(action)){
                    return ConfirmationSurchargeFeeDialogFragment.newInstance(intent);
                }

                return ConfirmationDialogFragment.newInstance(intent);
            }else if(categories.contains(InformationStatus.CATEGORY)){
                switch (action){
                    case InformationStatus.TRANS_COMPLETED:
                        return TransCompletedDialogFragment.newInstance(intent);
                    case InformationStatus.TRANS_ONLINE_STARTED:
                    case InformationStatus.EMV_TRANS_ONLINE_STARTED:
                    case InformationStatus.RKI_STARTED:
                    case InformationStatus.DCC_ONLINE_STARTED:
                    case InformationStatus.PINPAD_CONNECTION_STARTED:
                        return InformationDialogFragment.newInstance(action);
                    default:
                        break;
                }
            }else if(categories.contains(CardStatus.CATEGORY)){
                switch (action){
                    case CardStatus.CARD_REMOVAL_REQUIRED:
                    case CardStatus.CARD_PROCESS_STARTED:
                        return InformationDialogFragment.newInstance(action);
                    default:
                        break;
                }
            }
        }
        return null;
    }

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
            case CardStatus.CARD_REMOVED:
                return "remove_card";
            case CardStatus.CARD_PROCESS_STARTED:
            case CardStatus.CARD_PROCESS_COMPLETED:
                return "card_process";
            default:
                return null;
        }
    }

    public static Fragment createFragment(Intent intent){
        String action = intent.getAction();
        Set<String> categories = intent.getCategories();
        if(action != null){
            if(categories.contains(TextEntry.CATEGORY)) {
                switch (action) {
                    case TextEntry.ACTION_ENTER_AMOUNT:
                    case TextEntry.ACTION_ENTER_FUEL_AMOUNT:
                    case TextEntry.ACTION_ENTER_TAX_AMOUNT:
                        return AmountFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_CASH_BACK:
                        return CashbackFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_TIP:
                        return TipFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_TOTAL_AMOUNT:
                        return TotalAmountFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_CLERK_ID:
                    case TextEntry.ACTION_ENTER_SERVER_ID:
                    case TextEntry.ACTION_ENTER_TABLE_NUMBER:
                    case TextEntry.ACTION_ENTER_CS_PHONE_NUMBER:
                    case TextEntry.ACTION_ENTER_PHONE_NUMBER:
                    case TextEntry.ACTION_ENTER_GUEST_NUMBER:
                    case TextEntry.ACTION_ENTER_MERCHANT_TAX_ID:
                    case TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE:
                    case TextEntry.ACTION_ENTER_TRANS_NUMBER:
                        return NumFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_ADDRESS:
                    case TextEntry.ACTION_ENTER_AUTH:
                    case TextEntry.ACTION_ENTER_CUSTOMER_CODE:
                    case TextEntry.ACTION_ENTER_ORDER_NUMBER:
                    case TextEntry.ACTION_ENTER_PO_NUMBER:
                    case TextEntry.ACTION_ENTER_PROD_DESC:
                        return TextFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_ZIPCODE:
                    case TextEntry.ACTION_ENTER_DEST_ZIPCODE:
                    case TextEntry.ACTION_ENTER_INVOICE_NUMBER:
                    case TextEntry.ACTION_ENTER_VOUCHER_DATA:
                    case TextEntry.ACTION_ENTER_REFERENCE_NUMBER:
                    case TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER:
                    case TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER:
                        return NumTextFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_AVS_DATA:
                        return AVSFragment.newInstance(intent);
                    case TextEntry.ACTION_ENTER_EXPIRY_DATE:
                        return ExpiryFragment.newInstance(intent);
                    default:
                        return null;
                }
            }
            else if(categories.contains(SecurityEntry.CATEGORY)){
                switch (action) {
                    case SecurityEntry.ACTION_INPUT_ACCOUNT:
                    case SecurityEntry.ACTION_MANAGE_INPUT_ACCOUNT:
                        return InputAccountFragment.newInstance(intent);
                    case SecurityEntry.ACTION_ENTER_VCODE:
                    case SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS:
                    case SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS:
                        return SecurityFragment.newInstance(intent);
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
                //TODO Bug: Grant Permission for RECEIPT_URI
//                if(ConfirmationEntry.ACTION_CONFIRM_RECEIPT_VIEW.equals(action)){
//                    return ConfirmReceiptViewFragment.newInstance(intent);
//                }
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
