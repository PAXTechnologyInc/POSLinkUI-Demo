package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.ConfirmationType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;

import java.util.Arrays;
import java.util.List;

public class ConfirmTotalAmountFragment extends AConfirmationDialogFragment {
    private String currency;
    private long totalAmount;
    private String[] options;

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, "USD");
        totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT, 0L);
        options = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
        if(options.length != 2){
            options = ConfirmationType.values();
        }
    }

    @NonNull
    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_CONFIRMED;
    }

    @Override
    protected String getPositiveText() {
        return options[0];
    }

    @Override
    protected String getNegativeText() {
        return options[1];
    }

    @Override
    protected String formatMessage() {
        return getString(R.string.confirm_total_amount_message, CurrencyUtils.convert(totalAmount, currency));
    }

}
