package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;

/**
 * Implement confirmation entry action {@link ConfirmationEntry#ACTION_CONFIRM_BALANCE} <br>
 */
public class ConfirmBalanceFragment extends AConfirmationFragment {
    private String currency;
    private long balance;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        super.loadArgument(bundle);
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, "USD");
        balance = bundle.getLong(EntryExtraData.PARAM_BALANCE, 0L);
    }

    @Override
    protected String formatMessage(String message) {
        return getString(R.string.confirm_balance_message, CurrencyUtils.convert(balance, currency));
    }

}
