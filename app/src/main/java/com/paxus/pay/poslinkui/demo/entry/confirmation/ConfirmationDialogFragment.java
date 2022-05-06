package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.ConfirmationType;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.PrintStatusType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

public class ConfirmationDialogFragment extends DialogFragment {
    private String action;
    private String packageName;
    private long timeout;

    private String message;
    private String positiveText;
    private String negativeText;

    public static DialogFragment newInstance(Intent intent){
        ConfirmationDialogFragment dialogFragment = new ConfirmationDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation_dialog, container, false);
        loadParameter(getArguments());
        loadView(view);

        Dialog dialog = getDialog();
        if(dialog!= null) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            new Handler().postDelayed(()->{
                if(dialog.isShowing()){
                    EntryRequestUtils.sendTimeout(requireContext(), packageName, action);
                    dismiss();
                }
            },timeout);
        }
        return view;
    }

    private void loadParameter(Bundle bundle){
        if(bundle == null){
            Log.e("ConfirmDialog","No arguments");
            return;
        }

        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);

        if(ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT.equals(action)) {
            timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 5000);
        }else {
            timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        }

        message = bundle.getString(EntryExtraData.PARAM_MESSAGE);
        message = formatMessage(action, message, bundle);

        String[] options = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
        if(options == null){
            //For action ACTION_CONFIRM_UNIFIED_MESSAGE
            options = new String[]{ConfirmationType.YES,ConfirmationType.NO};
        }

        formatOptions(options);
    }

    private void loadView(View view){
        TextView messageTv = view.findViewById(R.id.message);
        messageTv.setText(message);

        Button positiveButton = view.findViewById(R.id.confirm_button);
        if(!TextUtils.isEmpty(positiveText)) {
            positiveButton.setText(positiveText);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_CONFIRMED,true);
                    dismiss();
                }
            });
        }else{
            positiveButton.setVisibility(View.GONE);
        }

        Button negativeButton = view.findViewById(R.id.cancel_button);

        if(!TextUtils.isEmpty(negativeText)) {
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_CONFIRMED,false);
                    dismiss();
                }
            });
        }else{
            negativeButton.setVisibility(View.GONE);
        }
    }

    private String formatMessage(String action, String message, Bundle bundle){
        switch (action){
            case ConfirmationEntry.ACTION_CHECK_CARD_PRESENT:
                return getString(R.string.check_card_present);
            case ConfirmationEntry.ACTION_CHECK_DEACTIVATE_WARN:
                return getString(R.string.confirm_deactivate_warn);
            case ConfirmationEntry.ACTION_CONFIRM_UNTIPPED:
                return getString(R.string.confirm_untipped_close);
            case ConfirmationEntry.ACTION_CONFIRM_DUPLICATE_TRANS:
                return getString(R.string.prompt_confirm_dup_transaction);
            case ConfirmationEntry.ACTION_CONFIRM_UPLOAD_TRANS:
                return getString(R.string.confirm_upload_trans);
            case ConfirmationEntry.ACTION_CONFIRM_UPLOAD_RETRY:
                return getString(R.string.confirm_upload_retry);
            case ConfirmationEntry.ACTION_CONFIRM_PRINT_FAILED_TRANS:
                return getString(R.string.confirm_print_failed_trans);
            case ConfirmationEntry.ACTION_CONFIRM_PRINT_FPS:
                return getString(R.string.confirm_print_fps);
            case ConfirmationEntry.ACTION_CONFIRM_DELETE_SF:
                return getString(R.string.confirm_delete_sf);
            case ConfirmationEntry.ACTION_CONFIRM_PRINT_CUSTOMER_COPY:
                return getString(R.string.confirm_print_customer_copy);
            case ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY:
                return getString(R.string.confirm_online_retry);
            case ConfirmationEntry.ACTION_CONFIRM_ADJUST_TIP:
                return getString(R.string.confirm_adjust_tip);
            case ConfirmationEntry.ACTION_CONFIRM_RECEIPT_SIGNATURE:
                return getString(R.string.confirm_receipt_signature);
            case ConfirmationEntry.ACTION_CONFIRM_SIGNATURE_MATCH:
                return getString(R.string.confirm_signature_match);
            case ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE:
                if(!TextUtils.isEmpty(message)){
                    return message;
                }
                return getString(R.string.confirm_batch_close);
            case ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL: {
                String currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
                long approvedAmt = bundle.getLong(EntryExtraData.PARAM_APPROVED_AMOUNT);
                long total = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
                long due = total - approvedAmt;
                return getString(R.string.select_reverse_partial,
                        CurrencyUtils.convert(approvedAmt, currency),
                        CurrencyUtils.convert(due, currency));
            }
            case ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL: {
                String currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
                long approvedAmt = bundle.getLong(EntryExtraData.PARAM_APPROVED_AMOUNT);
                long total = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
                long due = total - approvedAmt;
                return getString(R.string.select_supplement_partial,
                        CurrencyUtils.convert(approvedAmt, currency),
                        CurrencyUtils.convert(due, currency));
            }
            case ConfirmationEntry.ACTION_CONFIRM_PRINTER_STATUS:
                String key = bundle.getString(EntryExtraData.PARAM_PRINT_STATUS, "");
                if (PrintStatusType.PRINTER_OUT_OF_PAPER.equals(key)) {
                    return getString(R.string.prompt_printer_out_of_paper);
                } else if (PrintStatusType.PRINTER_HOT.equals(key)) {
                    return getString(R.string.confirm_printer_over_hot);
                } else if (PrintStatusType.PRINTER_VOLTAGE_TOO_LOW.equals(key)) {
                    return getString(R.string.confirm_printer_voltage_low);
                } else {
                    return getString(R.string.confirm_printer_status);
                }
            default:
                return message;
        }
    }

    private void formatOptions(String[] options){
        //--------------Get positive and negative option-----------------------
        String positive = "";
        String negative = "";
        if(options.length == 2) {
            for (String option : options) {
                if(ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL.equals(action)){
                    if ("Reverse".equals(option)) { //PartialApprovalOption.REVERSE
                        positive = option;
                    } else if ("Accept".equals(option)) {//PartialApprovalOption.ACCEPT
                        negative = option;
                    }
                } else if(ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL.equals(action)){
                    if ("Accept".equals(option)) {//PartialApprovalOption.ACCEPT
                        positive = option;
                    } else if ("Decline".equals(option)) {//PartialApprovalOption.DECLINE
                        negative = option;
                    }
                } else {
                    if (ConfirmationType.YES.equals(option)) {
                        positive = option;
                    } else if (ConfirmationType.NO.equals(option)) {
                        negative = option;
                    }
                }
            }
            if(TextUtils.isEmpty(negative) && TextUtils.isEmpty(positive)){
                positive = options[0];
                negative = options[1];
            }
        }else if(options.length == 1){
            positive = options[0];
        }

        //-----------------Customize option message---------------------------
        if(ConfirmationType.YES.equals(positive)){
            positiveText = getString(R.string.confirm_option_yes);
        }else if("Reverse".equals(positive)){
            positiveText = getString(R.string.confirm_option_reverse);
        }else if("Accept".equals(positive)){
            positiveText = getString(R.string.confirm_option_accept);
        }else {
            //If Option not defined, use original value
            positiveText = positive;
        }
        if(ConfirmationType.NO.equals(negative)){
            negativeText = getString(R.string.confirm_option_no);
        }else if("Accept".equals(positive)){
            negativeText = getString(R.string.confirm_option_accept);
        }else if("Decline".equals(positive)){
            negativeText = getString(R.string.confirm_option_decline);
        }else {
            //If Option not defined, use original value
            negativeText = negative;
        }
    }
}
