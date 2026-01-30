package com.paxus.pay.poslinkui.demo.entry.option;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;

/**
 * Implement option entry action {@link OptionEntry#ACTION_SELECT_CURRENCY} <br>
 */
public class SelectCurrencyFragment extends AOptionEntryFragment {

    protected long timeout;
    private String exchangeRate;
    private String title_message;
    private String markup;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_select_currency;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        super.loadArgument(bundle);
        exchangeRate = bundle.getString(EntryExtraData.PARAM_EXCHANGE_RATE);
        title_message = bundle.getString(EntryExtraData.PARAM_TITLE_MESSAGE);
        markup = bundle.getString(EntryExtraData.PARAM_MARKUP);
    }

    @Override
    protected void loadView(View rootView) {
        super.loadView(rootView);
        TextView titleView = rootView.findViewById(R.id.title_view);
        TextView exchangeRateView = rootView.findViewById(R.id.exchange_rate);
        TextView markupView = rootView.findViewById(R.id.markup);
        if (!TextUtils.isEmpty(title_message)) {
            titleView.setText(title_message);
        }
        if (!TextUtils.isEmpty(exchangeRate)) {
            exchangeRateView.setVisibility(View.VISIBLE);
            exchangeRateView.setText(exchangeRate);
        } else {
            exchangeRateView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(markup)) {
            markupView.setVisibility(View.VISIBLE);
            markupView.setText(markup);
        } else {
            markupView.setVisibility(View.GONE);
        }
    }


    @Override
    protected String formatTitle() {
        return requireContext().getString(R.string.select_currency);
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_INDEX;
    }
}
