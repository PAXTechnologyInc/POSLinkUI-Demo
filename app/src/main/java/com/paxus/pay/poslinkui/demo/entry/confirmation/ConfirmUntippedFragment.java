package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.ConfirmationType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

public class ConfirmUntippedFragment extends AConfirmationDialogFragment{
    public static ConfirmUntippedFragment newInstance(Intent intent){
        ConfirmUntippedFragment dialogFragment = new ConfirmUntippedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);

        timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        message = bundle.getString(EntryExtraData.PARAM_MESSAGE);
        message = formatMessage(action, message, bundle);

        String[] options = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);

        formatOptions(options);
    }

    @Override
    protected String formatMessage(String action, String message, Bundle bundle) {
        return getString(R.string.confirm_untipped_close);
    }

    @Override
    protected void formatOptions(String[] options) {
        //--------------Get positive and negative option-----------------------
        String positive = "";
        String negative = "";
        if(options.length == 2) {
            for (String option : options) {
                if (ConfirmationType.YES.equals(option)) {
                    positive = option;
                } else if (ConfirmationType.NO.equals(option)) {
                    negative = option;
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

    @Override
    protected void sendNext(boolean confirm) {
        dismiss();
        EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_CONFIRMED,confirm);
    }
}
