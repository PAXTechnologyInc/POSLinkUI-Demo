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
 * {@value TextEntry#ACTION_ENTER_SERVER_ID}<br>
 */

public class ServerIdFragment extends ANumTextFragment {
    protected long timeOut;
    protected int minLength;
    protected int maxLength;
    protected boolean allText;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN, "0-4");

        if (!TextUtils.isEmpty(valuePatten)) {
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }
        allText = InputType.ALLTEXT.equals(bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE));
    }

    @Override
    protected int getMaxLength() {
        return maxLength;
    }

    @Override
    protected boolean allowText() {
        return allText;
    }

    @Override
    protected String formatMessage() {
        return getString(R.string.pls_input_server_id);
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_SERVER_ID;
    }
}
