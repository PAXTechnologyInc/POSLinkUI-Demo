package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;

/**
 * Implement confirmation entry action {@link ConfirmationEntry#ACTION_CONFIRM_CASH_PAYMENT} <br>
 */
public class ConfirmCashPaymentFragment extends AConfirmationFragment {
    private long totalAmount;
    private String currency;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        super.loadArgument(bundle);
        totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY);
    }

    @Override
    protected String formatMessage(String message) {
        return message!= null ? message : getString(R.string.confirm_cash_payment_message, CurrencyUtils.convert(totalAmount, currency));
    }

}
