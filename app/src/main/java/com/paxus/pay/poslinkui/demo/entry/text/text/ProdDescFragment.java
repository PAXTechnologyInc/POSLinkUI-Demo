package com.paxus.pay.poslinkui.demo.entry.text.text;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

/**
 * Implement text entry actions:<br>
 * {@value TextEntry#ACTION_ENTER_PROD_DESC}<br>
 */

public class ProdDescFragment extends ATextFragment {
    protected long timeOut;
    protected int minLength;
    protected int maxLength;
    private String message = "";

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN, "0-40");

        if(!TextUtils.isEmpty(valuePatten)){
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }
    }

    @Override
    protected String formatMessage() {
        return getString(R.string.pls_input_proc_desc);
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_PROD_DESC;
    }
}
