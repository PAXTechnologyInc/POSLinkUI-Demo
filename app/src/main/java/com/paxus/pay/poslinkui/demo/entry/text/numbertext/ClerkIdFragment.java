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
 * {@value TextEntry#ACTION_ENTER_CLERK_ID}<br>
 *
 * <p>
 * UI Tips:
 * If confirm button clicked, sendNext
 * </p>
 */

public class ClerkIdFragment extends ANumTextFragment {
    private String packageName;
    private String action;
    private String transType;
    protected long timeOut;
    protected int minLength;
    protected int maxLength;
    private String transMode;
    protected boolean allText;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN, "0-4");
        if (!TextUtils.isEmpty(valuePatten)) {
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }
        allText = InputType.ALLTEXT.equals(bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE));

    }

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
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
        return getString(R.string.pls_input_clerk_id);
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_CLERK_ID;
    }
}
