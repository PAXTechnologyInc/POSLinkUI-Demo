package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.ConfirmationType;
import com.paxus.pay.poslinkui.demo.R;

import java.util.Arrays;
import java.util.List;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_CONFIRM_CARD_PROCESS_RESULT}
 * <p>
 * UI Tips:
 * 1.there is only one option {@link ConfirmationType#YES}, so if timeout, treat it as confirmed.
 * 2.If click YES, sendNext(true)
 * </p>
 */
public class ConfirmCardProcessResultFragment extends AConfirmationDialogFragment {
    private long timeout;
    private String message;
    private List<String> options;

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {
        timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        message = bundle.getString(EntryExtraData.PARAM_MESSAGE);
        String[] array = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
        if (array != null) {
            options = Arrays.asList(array);
        }
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_CONFIRMED;
    }

    @Override
    protected String getPositiveText() {
        if (options != null && options.contains(ConfirmationType.YES)) {
            return getString(R.string.confirm_option_yes);
        }
        return null;
    }

    @Override
    protected String getNegativeText() {
        return null;
    }

    @Override
    protected void loadView(View rootView) {
        super.loadView(rootView);

        //if timeout, treat it as confirmed.
        new Handler().postDelayed(() -> {
            if (isActive) {
                submit(true);
            }
        }, timeout);
    }

    @Override
    protected String formatMessage() {
        return message;
    }

}
