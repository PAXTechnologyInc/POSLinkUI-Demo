package com.paxus.pay.poslinkui.demo.entry.text.amount;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_AMOUNT}<br>
 * <p>
 * UI Tips:
 * If confirm button clicked, sendNext
 * </p>
 */

public class AmountFragment extends AAmountFragment {
    private String transType;
    private String transMode;
    protected long timeOut;
    protected int minLength;
    protected int maxLength;
    private String currency = "";

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN, "1-12");
        if (!TextUtils.isEmpty(valuePatten)) {
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }
    }

    @Override
    protected String formatMessage() {
        if (CurrencyType.POINT.equals(currency)) {
            return getString(R.string.prompt_input_point);
        } else {
            return getString(R.string.prompt_input_amount);
        }
    }

    @Override
    protected int getMaxLength() {
        return maxLength;
    }

    @Override
    protected String getCurrency() {
        return currency;
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_AMOUNT;
    }

}
