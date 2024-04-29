package com.paxus.pay.poslinkui.demo.entry.option;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement option entry action {@link OptionEntry#ACTION_SELECT_CASH_DISCOUNT} <br>
 */
public class SelectCashDiscountFragment extends AOptionEntryFragment {

    protected long timeout;

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_INDEX;
    }

    @Override
    protected List<SelectOptionsView.Option> generateOptions(Bundle bundle) {
        List<SelectOptionsView.Option> options = new ArrayList<>();
        String tempOptionsStringifiedJsonArray = bundle.containsKey(EntryExtraData.PARAM_OPTIONS) ? bundle.getString(EntryExtraData.PARAM_OPTIONS) : null;
        try {
            JSONArray tempOptionsJsonArray = new JSONArray(tempOptionsStringifiedJsonArray);
            for(int i=0; i<tempOptionsJsonArray.length(); i++){
                JSONObject jsonObject = tempOptionsJsonArray.getJSONObject(i);
                String title = jsonObject.getString(EntryExtraData.PARAM_TITLE);
                long amount = Long.parseLong(jsonObject.optString(EntryExtraData.PARAM_AMOUNT, "0"));
                String currency = jsonObject.optString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
                if(title != null || !title.isEmpty()){
                    options.add(new SelectOptionsView.Option(i, title, amount>0 ? CurrencyUtils.convert(amount, currency) : null, i));
                }
            }
        } catch (JSONException e) {
            Logger.e(e);
        }

        return options;
    }

    @Override
    protected String formatTitle() {
        return requireContext().getString(R.string.select_cash_discount);
    }
}
