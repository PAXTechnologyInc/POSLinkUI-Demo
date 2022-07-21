package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.ConfirmationType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryDialogFragment;

/**
 * Implement all entry actions defined in {@link ConfirmationEntry} <br>
 * except for {@value ConfirmationEntry#ACTION_CONFIRM_SURCHARGE_FEE} and {@value ConfirmationEntry#ACTION_CONFIRM_RECEIPT_VIEW}
 *<p>
 *     UI Tips:
 *     1.For {@link ConfirmationEntry#ACTION_CONFIRM_UNIFIED_MESSAGE}, use options {@link ConfirmationType#YES,ConfirmationType#NO}
 *     2.For {@link ConfirmationEntry#ACTION_CONFIRM_CARD_PROCESS_RESULT}, there is only one option {@link ConfirmationType#YES}, so if timeout, treat it as confirmed.
 *       For other confirm actions, timeout is controlled by BroadPOS.
 *</p>
 *
 */
public abstract class AConfirmationDialogFragment extends BaseEntryDialogFragment {
    protected long timeout;

    protected String message;
    protected String positiveText;
    protected String negativeText;

//    public static DialogFragment newInstance(Intent intent){
//        ConfirmationDialogFragment dialogFragment = new ConfirmationDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
//        bundle.putAll(intent.getExtras());
//
//        dialogFragment.setArguments(bundle);
//        return dialogFragment;
//    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_confirmation_dialog;
    }

    @Override
    protected abstract void loadParameter(@NonNull Bundle bundle);
//    {
//        action = bundle.getString(EntryRequest.PARAM_ACTION);
//        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
//
//        if(ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT.equals(action)) {
//            timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 5000);
//        }else {
//            timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
//        }
//
//        message = bundle.getString(EntryExtraData.PARAM_MESSAGE);
//        message = formatMessage(action, message, bundle);
//
//        String[] options = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
//        if(options == null){
//            //For action ACTION_CONFIRM_UNIFIED_MESSAGE
//            options = new String[]{ConfirmationType.YES,ConfirmationType.NO};
//        }
//
//        formatOptions(options);
//    }

    @Override
    protected void loadView(View rootView) {
        TextView messageTv = rootView.findViewById(R.id.message);
        messageTv.setText(message);

        Button positiveButton = rootView.findViewById(R.id.confirm_button);
        if(!TextUtils.isEmpty(positiveText)) {
            positiveButton.setText(positiveText);
            positiveButton.setOnClickListener( v-> onPositiveButtonClicked());
        }else{
            positiveButton.setVisibility(View.GONE);
        }

        Button negativeButton = rootView.findViewById(R.id.cancel_button);

        if(!TextUtils.isEmpty(negativeText)) {
            negativeButton.setOnClickListener( v -> onNegativeButtonClicked());
        }else{
            negativeButton.setVisibility(View.GONE);
        }

        //For ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT, if timeout, treat it as confirmed.
        if (!TextUtils.isEmpty(positiveText) && TextUtils.isEmpty(negativeText)) {
            new Handler().postDelayed(() -> {
                if(active) {
                    sendNext(true);
                }
            }, timeout);
        }
    }

    private void onNegativeButtonClicked(){
        sendNext(false);
    }

    private void onPositiveButtonClicked(){
        sendNext(true);
    }

    protected abstract String formatMessage(String action, String message, Bundle bundle);
//    {
//        switch (action){
//            case ConfirmationEntry.ACTION_CHECK_CARD_PRESENT:
//                return getString(R.string.check_card_present);
//            case ConfirmationEntry.ACTION_CHECK_DEACTIVATE_WARN:
//                return getString(R.string.confirm_deactivate_warn);
//            case ConfirmationEntry.ACTION_CONFIRM_UNTIPPED:
//                return getString(R.string.confirm_untipped_close);
//            case ConfirmationEntry.ACTION_CONFIRM_DUPLICATE_TRANS:
//                return getString(R.string.prompt_confirm_dup_transaction);
//            case ConfirmationEntry.ACTION_CONFIRM_UPLOAD_TRANS:
//                return getString(R.string.confirm_upload_trans);
//            case ConfirmationEntry.ACTION_CONFIRM_UPLOAD_RETRY:
//                return getString(R.string.confirm_upload_retry);
//            case ConfirmationEntry.ACTION_CONFIRM_PRINT_FAILED_TRANS:
//                return getString(R.string.confirm_print_failed_trans);
//            case ConfirmationEntry.ACTION_CONFIRM_PRINT_FPS:
//                return getString(R.string.confirm_print_fps);
//            case ConfirmationEntry.ACTION_CONFIRM_DELETE_SF:
//                return getString(R.string.confirm_delete_sf);
//            case ConfirmationEntry.ACTION_CONFIRM_PRINT_CUSTOMER_COPY:
//                return getString(R.string.confirm_print_customer_copy);
//            case ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY:
//                return getString(R.string.confirm_online_retry);
//            case ConfirmationEntry.ACTION_CONFIRM_ADJUST_TIP:
//                return getString(R.string.confirm_adjust_tip);
//            case ConfirmationEntry.ACTION_CONFIRM_RECEIPT_SIGNATURE:
//                return getString(R.string.confirm_receipt_signature);
//            case ConfirmationEntry.ACTION_CONFIRM_SIGNATURE_MATCH:
//                return getString(R.string.confirm_signature_match);
//            case ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE:
//                if(!TextUtils.isEmpty(message)){
//                    return message;
//                }
//                return getString(R.string.confirm_batch_close);
//            case ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL: {
//                String currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
//                long approvedAmt = bundle.getLong(EntryExtraData.PARAM_APPROVED_AMOUNT);
//                long total = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
//                long due = total - approvedAmt;
//                return getString(R.string.select_reverse_partial,
//                        CurrencyUtils.convert(approvedAmt, currency),
//                        CurrencyUtils.convert(due, currency));
//            }
//            case ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL: {
//                String currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
//                long approvedAmt = bundle.getLong(EntryExtraData.PARAM_APPROVED_AMOUNT);
//                long total = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
//                long due = total - approvedAmt;
//                return getString(R.string.select_supplement_partial,
//                        CurrencyUtils.convert(approvedAmt, currency),
//                        CurrencyUtils.convert(due, currency));
//            }
//            case ConfirmationEntry.ACTION_CONFIRM_PRINTER_STATUS:
//                String key = bundle.getString(EntryExtraData.PARAM_PRINT_STATUS, "");
//                if (PrintStatusType.PRINTER_OUT_OF_PAPER.equals(key)) {
//                    return getString(R.string.prompt_printer_out_of_paper);
//                } else if (PrintStatusType.PRINTER_HOT.equals(key)) {
//                    return getString(R.string.confirm_printer_over_hot);
//                } else if (PrintStatusType.PRINTER_VOLTAGE_TOO_LOW.equals(key)) {
//                    return getString(R.string.confirm_printer_voltage_low);
//                } else {
//                    return getString(R.string.confirm_printer_status);
//                }
//            default:
//                return message;
//        }
//    }

    protected abstract void formatOptions(String[] options);
//    {
//        //--------------Get positive and negative option-----------------------
//        String positive = "";
//        String negative = "";
//        if(options.length == 2) {
//            for (String option : options) {
//                if(ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL.equals(action)){
//                    if ("Reverse".equals(option)) { //PartialApprovalOption.REVERSE
//                        positive = option;
//                    } else if ("Accept".equals(option)) {//PartialApprovalOption.ACCEPT
//                        negative = option;
//                    }
//                } else if(ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL.equals(action)){
//                    if ("Accept".equals(option)) {//PartialApprovalOption.ACCEPT
//                        positive = option;
//                    } else if ("Decline".equals(option)) {//PartialApprovalOption.DECLINE
//                        negative = option;
//                    }
//                } else {
//                    if (ConfirmationType.YES.equals(option)) {
//                        positive = option;
//                    } else if (ConfirmationType.NO.equals(option)) {
//                        negative = option;
//                    }
//                }
//            }
//            if(TextUtils.isEmpty(negative) && TextUtils.isEmpty(positive)){
//                positive = options[0];
//                negative = options[1];
//            }
//        }else if(options.length == 1){
//            positive = options[0];
//        }
//
//        //-----------------Customize option message---------------------------
//        if(ConfirmationType.YES.equals(positive)){
//            positiveText = getString(R.string.confirm_option_yes);
//        }else if("Reverse".equals(positive)){
//            positiveText = getString(R.string.confirm_option_reverse);
//        }else if("Accept".equals(positive)){
//            positiveText = getString(R.string.confirm_option_accept);
//        }else {
//            //If Option not defined, use original value
//            positiveText = positive;
//        }
//        if(ConfirmationType.NO.equals(negative)){
//            negativeText = getString(R.string.confirm_option_no);
//        }else if("Accept".equals(positive)){
//            negativeText = getString(R.string.confirm_option_accept);
//        }else if("Decline".equals(positive)){
//            negativeText = getString(R.string.confirm_option_decline);
//        }else {
//            //If Option not defined, use original value
//            negativeText = negative;
//        }
//    }

    protected abstract void sendNext(boolean confirm);
//    {
//        dismiss();
//        EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_CONFIRMED,confirm);
//    }

}
