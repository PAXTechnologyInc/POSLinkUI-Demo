package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.enumeration.ConfirmationType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;

public class ConfirmTotalAmountFragment extends AConfirmationFragment {
    private String currency;
    private long totalAmount;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, "USD");
        totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT, 0L);
    }

    @Override
    protected String formatMessage(String message) {
        return getString(R.string.confirm_total_amount_message, CurrencyUtils.convert(totalAmount, currency));
    }

}
