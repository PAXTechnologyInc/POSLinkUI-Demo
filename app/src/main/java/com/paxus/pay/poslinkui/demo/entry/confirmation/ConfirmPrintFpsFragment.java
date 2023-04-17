package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.ConfirmationType;
import com.paxus.pay.poslinkui.demo.R;

import java.util.Arrays;
import java.util.List;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_CONFIRM_PRINT_FPS}
 * <p>
 * UI Tips:
 * 1.If click YES, sendNext(true)
 * 2.If click NO, sendNext(false)
 * </p>
 */
public class ConfirmPrintFpsFragment extends AConfirmationDialogFragment {
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

    @NonNull
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
        if (options != null && options.contains(ConfirmationType.NO)) {
            return getString(R.string.confirm_option_no);
        }
        return null;
    }


    @Override
    protected String formatMessage() {
        return getString(R.string.confirm_print_fps);
    }

}
