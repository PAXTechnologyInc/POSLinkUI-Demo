package com.paxus.pay.poslinkui.demo.entry.text.amount;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.UnitType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_TIP}<br>
 */
public class TipFragment extends BaseEntryFragment {
    private int minLength;
    private int maxLength;

    private String currency = "";
    private String tipName;
    private long baseAmount;

    private boolean isSelectTipEnabled = false;
    private boolean isNoTipSelectionAllowed;

    private List<SelectOptionsView.Option> tipOptionList = new ArrayList<>();
    private List<TipInfo> tipInfoList = new ArrayList<>();

    long tip = 0;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_tip;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        currency =  bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");
        if(!TextUtils.isEmpty(valuePatten)){
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }
        baseAmount = bundle.getLong(EntryExtraData.PARAM_BASE_AMOUNT, -1);

        tipName = bundle.getString(EntryExtraData.PARAM_TIP_NAME);
        String tipUnit = bundle.getString(EntryExtraData.PARAM_TIP_UNIT, UnitType.CENT);

        //Tip Summary
        String[] previousTipNames = bundle.getStringArray(EntryExtraData.PARAM_TIP_NAMES);
        String[] previousTipAmounts = bundle.getStringArray(EntryExtraData.PARAM_TIP_AMOUNTS);
        if(previousTipNames != null){
            for(int i=0; i<previousTipNames.length; i++){
                String name = previousTipNames[i];
                long amount = 0;
                try{
                    amount = Long.parseLong(previousTipAmounts[i]);
                } catch (NumberFormatException e){
                }
                if(amount>0) tipInfoList.add(new TipInfo(name, CurrencyUtils.convert(amount, currency)));
            }
        }

        //Select Tip Options
        String[] tipOptions = bundle.getStringArray(EntryExtraData.PARAM_TIP_OPTIONS);
        String[] tipRateOptions = bundle.getStringArray(EntryExtraData.PARAM_TIP_RATE_OPTIONS);
        if(tipOptions != null){
            for(int i=0; i<tipOptions.length; i++){
                long tipAmount = 0;
                String tipRate = null;
                try{
                    tipAmount = Long.parseLong(tipOptions[i]) * (tipUnit.equals(UnitType.DOLLAR) ? 100 : 1);
                    tipRate = tipRateOptions[i];
                } catch (NumberFormatException | IndexOutOfBoundsException e){
                }
                tipOptionList.add(new SelectOptionsView.Option(null, CurrencyUtils.convert(tipAmount, currency), tipRate, tipAmount));
            }
        }
        isSelectTipEnabled = !tipOptionList.isEmpty();
        isNoTipSelectionAllowed = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_NO_TIP_SELECTION);
    }


    @Override @SuppressLint("SetTextI18n")
    protected void loadView(View rootView) {
        //Base Amount
        ConstraintLayout baseAmountLayout = rootView.findViewById(R.id.layout_tip_base_amount);
        View baseAmountView = getLayoutInflater().inflate(R.layout.fragment_double_line_key_value, null);
        ((TextView)baseAmountView.findViewById(R.id.key)).setText("Amount");
        ((TextView)baseAmountView.findViewById(R.id.value)).setText(CurrencyUtils.convert(baseAmount, currency));
        ((TextView)baseAmountView.findViewById(R.id.value)).setTextSize(24);
        baseAmountLayout.addView(baseAmountView);

        //Tip Summary
        if(!tipInfoList.isEmpty()){
            RecyclerView tipSummaryView = rootView.findViewById(R.id.recycler_view_tip_summary);
            tipSummaryView.setLayoutManager(new GridLayoutManager(getContext(), tipInfoList.size()));
            tipSummaryView.setAdapter(new TipInfoAdapter(getContext(), tipInfoList));
        }

        //Prompt
        ((TextView)rootView.findViewById(R.id.textview_tip_name)).setText((isSelectTipEnabled? "Select " : "Enter ") + tipName);

        //Select and Enter Tip
        SelectOptionsView tipOptionsView = rootView.findViewById(R.id.select_view_tip_options);
        EditText tipInputEditText = rootView.findViewById(R.id.edit_text_tip_entry);
        if(isSelectTipEnabled){
            tipOptionsView.setVisibility(View.VISIBLE);
            tipOptionsView.initialize(getActivity(), tipOptionList.size(), tipOptionList, option -> {
                tip = (long) option.getValue();
                onConfirmButtonClicked();
            });

            ((TextView)rootView.findViewById(R.id.text_view_other_tip)).setVisibility(View.VISIBLE);

            //Soft Keyboard Submission
            tipInputEditText.setImeOptions(tipInputEditText.getImeOptions() | EditorInfo.IME_ACTION_DONE);
            tipInputEditText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) onConfirmButtonClicked();
                return true;
            });
        } else {
            focusableEditTexts = new EditText[]{tipInputEditText};
        }

        //No Tip Option
        SelectOptionsView noTipOptionView = rootView.findViewById(R.id.select_view_no_tip);
        if(isNoTipSelectionAllowed) {
            noTipOptionView.setVisibility(View.VISIBLE);
            List<SelectOptionsView.Option> noTipOptionList = new ArrayList<>(Arrays.asList(new SelectOptionsView.Option(null, "No Tip", null, -1L)));
            noTipOptionView.initialize(getActivity(), 1, noTipOptionList, option -> {
                tip = (long) option.getValue();
                onConfirmButtonClicked();
            });
            if(isSelectTipEnabled) tipOptionsView.append(noTipOptionView);
        }

        //Enter Tip or Enter Other Tip
        tipInputEditText.addTextChangedListener(new AmountTextWatcher(maxLength, currency){
            @Override public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                tip = CurrencyUtils.parse(s.toString());
            }
        });
        tipInputEditText.setText(String.valueOf(tip)); //Initialize TextWatcher with Default Tip Value

        //Confirm
        Button confirmButton = rootView.findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener( v -> onConfirmButtonClicked());
    }

    public class TipInfo{
        String name, amount;
        TipInfo(String name, String amount) {
            this.name = name;
            this.amount = amount;
        }
    }
    public class TipInfoAdapter extends RecyclerView.Adapter<TipInfoAdapter.TipInfoHolder> {
        private List<TipInfo> data;
        private LayoutInflater layoutInflater;

        TipInfoAdapter(Context context, List<TipInfo> tipInfoList){
            this.data = tipInfoList;
            this.layoutInflater = LayoutInflater.from(context);
        }
        @NonNull
        @Override
        public TipInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.fragment_double_line_key_value, parent, false);
            return new TipInfoHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TipInfoHolder holder, int position) {
            TipInfo tipInfo = data.get(position);
            holder.key.setText(tipInfo.name);
            holder.value.setText(tipInfo.amount);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class TipInfoHolder extends RecyclerView.ViewHolder {
            TextView key, value;
            public TipInfoHolder(@NonNull View itemView) {
                super(itemView);
                key = itemView.findViewById(R.id.key);
                key.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                value = itemView.findViewById(R.id.value);
                value.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
        }
    }

    @Override
    protected void onConfirmButtonClicked(){
        submit(tip);
    }

    private void submit(long value) {
        Bundle bundle = new Bundle();

        if(value>=0) {
            bundle.putLong(EntryRequest.PARAM_TIP, value);
        }

        sendNext(bundle);
    }
}