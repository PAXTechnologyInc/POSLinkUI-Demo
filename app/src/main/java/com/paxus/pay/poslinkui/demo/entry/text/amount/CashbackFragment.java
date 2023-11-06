package com.paxus.pay.poslinkui.demo.entry.text.amount;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_CASH_BACK}<br>
 */
public class CashbackFragment extends BaseEntryFragment {
    private int minLength;
    private int maxLength;

    private String currency = "";
    private boolean promptOther;

    private List<SelectOptionsView.Option> optionList = new ArrayList<>();

    private long cashback = 0;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_cashback;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        currency =  bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");
        if(!TextUtils.isEmpty(valuePatten)){
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }

        String[] options = bundle.getStringArray(EntryExtraData.PARAM_CASHBACK_OPTIONS);
        if(options != null){
            for(int i=0; i<options.length; i++){
                long cashbackAmount = 0;
                try{
                    cashbackAmount = Long.parseLong(options[i]);
                    if(cashbackAmount == 0) continue; //Temporary workaround because jax hosts adds 0 to options to draw "No thanks" button
                } catch (NumberFormatException | IndexOutOfBoundsException e){
                }
                optionList.add(new SelectOptionsView.Option(null, CurrencyUtils.convert(cashbackAmount, currency), null, cashbackAmount));
            }
        }

        promptOther = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_OTHER_PROMPT) | true;
    }


    @Override @SuppressLint("ClickableViewAccessibility")
    protected void loadView(View rootView) {
        boolean isSelectCashbackEnabled = !optionList.isEmpty();

        SelectOptionsView cashbackOptionsView = rootView.findViewById(R.id.options_layout);
        EditText editCashback = rootView.findViewById(R.id.edit_cashback);

        if(isSelectCashbackEnabled) {
            ((TextView)rootView.findViewById(R.id.message)).setText(getString(R.string.select_cashback_amount));
            rootView.findViewById(R.id.options_layout).setVisibility(View.VISIBLE);
            cashbackOptionsView.initialize(getActivity(), 2, optionList, cashbackSelectCallback);

            if(promptOther) {
                rootView.findViewById(R.id.text_view_other_cashback).setVisibility(View.VISIBLE);
                editCashback.setVisibility(View.VISIBLE);
                editCashback.setImeOptions(editCashback.getImeOptions() | EditorInfo.IME_ACTION_DONE);
                editCashback.setOnEditorActionListener((v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) onConfirmButtonClicked();
                    return true;
                });
            }
        } else {
            editCashback.setVisibility(View.VISIBLE);
            focusableEditTexts = new EditText[]{editCashback};
        }
        if(editCashback.getVisibility() == View.VISIBLE){
            editCashback.addTextChangedListener(new CashbackEditTextWatcher(maxLength,currency, value -> this.cashback = value));
        }

        SelectOptionsView noCashbackOptionsView = rootView.findViewById(R.id.no_thanks_options_layout);
        List<SelectOptionsView.Option> noCashbackOptionList = new ArrayList<>(Arrays.asList(new SelectOptionsView.Option(null, "No Thanks!!", null, 0L)));
        noCashbackOptionsView.initialize(getActivity(), 1, noCashbackOptionList, cashbackSelectCallback);

        if(isSelectCashbackEnabled) cashbackOptionsView.append(noCashbackOptionsView);

        rootView.findViewById(R.id.confirm_button).setOnClickListener(v -> onConfirmButtonClicked());
    }

    private SelectOptionsView.OptionSelectListener cashbackSelectCallback = option -> {
        this.cashback = (long) option.getValue();
        onConfirmButtonClicked();
    };

    private @FunctionalInterface interface CashbackEditCallback {
        void onChange(long cashback);
    }
    private class CashbackEditTextWatcher extends AmountTextWatcher {
        private CashbackEditCallback callback;
        public CashbackEditTextWatcher(int maxLength, String currency, CashbackEditCallback callback) {
            super(maxLength, currency);
            this.callback = callback;
        }
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            new Handler(Looper.getMainLooper()).post(()->callback.onChange(CurrencyUtils.parse(s.toString())));
        }
    }

    @Override
    protected void onConfirmButtonClicked(){
        submit(cashback);
    }

    private void submit(long value){
        Bundle bundle = new Bundle();
        bundle.putLong(EntryRequest.PARAM_CASHBACK_AMOUNT, value);
        sendNext(bundle);
    }
}
