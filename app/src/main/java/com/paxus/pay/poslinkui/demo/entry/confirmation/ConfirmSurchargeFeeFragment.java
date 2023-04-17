package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_CONFIRM_SURCHARGE_FEE}
 * <p>
 * UI Tips:
 * 1.If click confirm, sendNext(true)
 * 2.If click cancel, sendAbort
 * 3.If click bypass, sendNext(false)
 * 4.If enableBypass is true, display bypass button, else hide it.
 * </p>
 */
public class ConfirmSurchargeFeeFragment extends BaseEntryFragment {
    private long timeout;
    private String feeName;
    private long totalAmount;
    private long feeAmount;
    private String currency;
    private boolean enableBypass;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_surcharge_fee;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        feeName = bundle.getString(EntryExtraData.PARAM_SURCHARGE_FEE_NAME);
        totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
        feeAmount = bundle.getLong(EntryExtraData.PARAM_SURCHARGE_FEE);
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY);
        enableBypass = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_BYPASS);
    }

    @Override
    protected void loadView(View rootView) {
        TextView feeNameTv = rootView.findViewById(R.id.fee_amount_name);
        feeNameTv.setText(feeName);

        TextView fee = rootView.findViewById(R.id.fee_amount);
        fee.setText(CurrencyUtils.convert(feeAmount, currency));

        Button confirm = rootView.findViewById(R.id.confirm_button);
        confirm.setOnClickListener(v -> onConfirmButtonClicked());

        Button cancel = rootView.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(v -> onCancelButtonClicked());

        Button bypass = rootView.findViewById(R.id.bypass_button);
        if (enableBypass) {
            bypass.setOnClickListener(v -> onBypassButtonClicked());
        } else {
            bypass.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onConfirmButtonClicked() {
        submit(true);
    }

    private void onCancelButtonClicked() {
        sendAbort();
    }

    private void onBypassButtonClicked() {
        submit(false);
    }

    private void submit(boolean confirm) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EntryRequest.PARAM_CONFIRMED, confirm);
        sendNext(bundle);
    }
}
