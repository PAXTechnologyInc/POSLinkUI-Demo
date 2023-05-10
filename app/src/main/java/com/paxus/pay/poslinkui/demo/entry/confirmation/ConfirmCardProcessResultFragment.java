package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
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
public class ConfirmCardProcessResultFragment extends AConfirmationFragment {
    private long timeout;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
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
            if (getActivity() != null) {
                submit(true);
            }
        }, timeout);
    }

    @Override
    protected String formatMessage(String message) {
        return message;
    }

}
