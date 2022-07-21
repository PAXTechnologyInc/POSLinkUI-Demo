package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.ConfirmationType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

public class ConfirmUnifiedMessageFragment extends AConfirmationDialogFragment{
    public static ConfirmUnifiedMessageFragment newInstance(Intent intent){
        ConfirmUnifiedMessageFragment dialogFragment = new ConfirmUnifiedMessageFragment();
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

        String[] options = new String[]{ConfirmationType.YES,ConfirmationType.NO};

        formatOptions(options);
    }

    @Override
    protected String formatMessage(String action, String message, Bundle bundle) {
        return message;
    }

    @Override
    protected void formatOptions(String[] options) {
        //--------------Get positive and negative option-----------------------
        String positive = "";
        String negative = "";
        for (String option : options) {
            if (ConfirmationType.YES.equals(option)) {
                positive = option;
            } else if (ConfirmationType.NO.equals(option)) {
                negative = option;
            }
        }

        //-----------------Customize option message---------------------------
        if(ConfirmationType.YES.equals(positive)){
            positiveText = getString(R.string.confirm_option_yes);
        }
        if(ConfirmationType.NO.equals(negative)){
            negativeText = getString(R.string.confirm_option_no);
        }
    }

    @Override
    protected void sendNext(boolean confirm) {
        dismiss();
        EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_CONFIRMED,confirm);
    }
}
