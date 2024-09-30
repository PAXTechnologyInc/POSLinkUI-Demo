package com.paxus.pay.poslinkui.demo.entry.text.number;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

public class EnterTicketNumberFragment extends ANumFragment {

    protected int minLength;
    protected int maxLength;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN, "0-8");
        if (!TextUtils.isEmpty(valuePatten)) {
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }
    }

    @Override
    protected int getMaxLength() {
        return maxLength;
    }

    @Override
    protected String formatMessage() {
        return getString(R.string.enter_ticket_number);
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_TICKET_NUMBER;
    }
}
