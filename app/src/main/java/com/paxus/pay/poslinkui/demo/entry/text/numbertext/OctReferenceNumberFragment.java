package com.paxus.pay.poslinkui.demo.entry.text.numbertext;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.InputType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

/**
 * Implement text entry actions:<br>
 * {@value TextEntry#ACTION_ENTER_OCT_REFERENCE_NUMBER}<br>
 *
 * <p>
 * UI Tips:
 * If confirm button clicked, sendNext
 * </p>
 */

public class OctReferenceNumberFragment extends ANumTextFragment {
    private String transType;
    protected long timeOut;
    protected int minLength;
    protected int maxLength;
    private String message = "";
    private String transMode;
    protected boolean allText;

    @Override
    protected int getMaxLength() {
        return maxLength;
    }

    @Override
    protected boolean allowText() {
        return allText;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN, "0-12");

        if(!TextUtils.isEmpty(valuePatten)){
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }

        allText = InputType.ALLTEXT.equals(bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE));
    }

    @Override
    protected String formatMessage() {
        return getString(R.string.pls_input_oct_reference_number);
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_OCT_REFERENCE_NUMBER;
    }
}
