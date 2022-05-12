package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryDialogFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

public class ConfirmationSurchargeFeeDialogFragment extends BaseEntryDialogFragment {
    private long timeout;
    private String feeName;
    private long totalAmount;
    private long feeAmount;
    private String currency;
    private boolean enableBypass;

    public static DialogFragment newInstance(Intent intent){
        ConfirmationSurchargeFeeDialogFragment dialogFragment = new ConfirmationSurchargeFeeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_surcharge_fee;
    }

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        feeName = bundle.getString(EntryExtraData.PARAM_SURCHARGE_FEE_NAME);
        totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
        feeAmount = bundle.getLong(EntryExtraData.PARAM_SURCHARGE_FEE);
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY);
        enableBypass = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_BYPASS);
    }

    @Override
    protected void loadView(View rootView) {
        TextView sale = rootView.findViewById(R.id.sale_amount);
        sale.setText(CurrencyUtils.convert(totalAmount - feeAmount, currency));

        TextView feeNameTv = rootView.findViewById(R.id.fee_amount_name);
        feeNameTv.setText(feeName);

        TextView fee = rootView.findViewById(R.id.fee_amount);
        fee.setText(CurrencyUtils.convert(feeAmount, currency));

        TextView total = rootView.findViewById(R.id.total_amount);
        total.setText(CurrencyUtils.convert(totalAmount, currency));

        Button confirm = rootView.findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNext(true);
            }
        });

        Button cancel = rootView.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAbort();
            }
        });
        Button bypass = rootView.findViewById(R.id.bypass_button);
        if(enableBypass){
            bypass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendNext(false);
                }
            });

        }else{
            bypass.setVisibility(View.GONE);
        }
    }

    private void sendNext(boolean confirm){
        EntryRequestUtils.sendNext(requireContext(), packageName, action,EntryRequest.PARAM_CONFIRMED,confirm);
    }
}
