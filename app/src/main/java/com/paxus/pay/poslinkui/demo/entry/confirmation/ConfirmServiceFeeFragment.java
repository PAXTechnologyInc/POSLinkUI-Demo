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
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_CONFIRM_SERVICE_FEE}
 * <p>
 * UI Tips:
 * 1.If click confirm, sendNext(true)
 * 2.If click cancel, sendAbort
 * 3.If click bypass, sendNext(false)
 * 4.If enableBypass is true, display bypass button, else hide it.
 * </p>
 */
public class ConfirmServiceFeeFragment extends AConfirmationFragment {
    private String feeName;
    private long feeAmount;
    private long totalAmount;
    private String currency;

    @Override
    protected int getLayoutResourceId() {
        /**
         * {@link AConfirmationFragment needs message, confirm_button, and cancel_button}
         */
        return R.layout.fragment_service_fee;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        super.loadArgument(bundle);
        feeName = bundle.getString(EntryExtraData.PARAM_SERVICE_FEE_NAME);
        totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
        feeAmount = bundle.getLong(EntryExtraData.PARAM_SERVICE_FEE);
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY);
    }

    @Override
    protected void loadView(View rootView) {
        super.loadView(rootView);

        TextView sale = rootView.findViewById(R.id.sale_amount);
        sale.setText(CurrencyUtils.convert(totalAmount - feeAmount, currency));

        TextView feeNameTv = rootView.findViewById(R.id.fee_amount_name);
        feeNameTv.setText(feeName);

        TextView fee = rootView.findViewById(R.id.fee_amount);
        fee.setText(CurrencyUtils.convert(feeAmount, currency));

        TextView total = rootView.findViewById(R.id.total_amount);
        total.setText(CurrencyUtils.convert(totalAmount, currency));
    }

    @Override
    protected String formatMessage(String message) {
        return null;
    }
}
