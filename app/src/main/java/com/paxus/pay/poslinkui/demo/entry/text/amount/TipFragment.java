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

import java.util.ArrayList;
import java.util.List;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_TIP}<br>
 */
public class TipFragment extends BaseEntryFragment {
    private long timeOut;
    private int minLength;
    private int maxLength;

    private String currency = "";
    private String tipName;
    private long baseAmount;
    private String tipUnit;

    private boolean isSelectTipEnabled = false;
    private boolean isNoTipSelectionAllowed;

    private List<TipOption> tipOptionList = new ArrayList<>();
    private List<TipInfo> tipInfoList = new ArrayList<>();

    long tip = 0;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_tip;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        currency =  bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");
        if(!TextUtils.isEmpty(valuePatten)){
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }
        baseAmount = bundle.getLong(EntryExtraData.PARAM_BASE_AMOUNT, -1);

        tipName = bundle.getString(EntryExtraData.PARAM_TIP_NAME);
        tipUnit = bundle.getString(EntryExtraData.PARAM_TIP_UNIT, UnitType.CENT);

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
                    tipAmount = Long.parseLong(tipOptions[i]);
                    tipRate = tipRateOptions[i];
                } catch (NumberFormatException | IndexOutOfBoundsException e){
                }
                tipOptionList.add(new TipOption(tipAmount, tipRate));
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

        //Tip Name
        ((TextView)rootView.findViewById(R.id.textview_tip_name)).setText((isSelectTipEnabled? "Select " : "Enter ") + tipName);

        //Select and Enter Tip
        if(isSelectTipEnabled){
            RecyclerView tipOptionsView = rootView.findViewById(R.id.recycler_view_tip_options);
            tipOptionsView.setVisibility(View.VISIBLE);
            tipOptionsView.setLayoutManager(new GridLayoutManager(getContext(), tipOptionList.size()));
            TipOptionAdapter tipOptionAdapter = new TipOptionAdapter(getContext(), tipOptionList, tipOption -> {
                ((EditText) rootView.findViewById(R.id.edit_text_tip_entry)).setText("0");
                tip = tipOption.tipAmount;
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            });
            tipOptionsView.setAdapter(tipOptionAdapter);

            ((TextView)rootView.findViewById(R.id.text_view_other_tip)).setVisibility(View.VISIBLE);

            //Soft Keyboard Submission
            EditText tipInputEditText = rootView.findViewById(R.id.edit_text_tip_entry);
            tipInputEditText.setImeOptions(tipInputEditText.getImeOptions() | EditorInfo.IME_ACTION_DONE);
            tipInputEditText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE)  onConfirmButtonClicked();
                return true;
            });
        } else {
            focusableEditTexts = new EditText[]{rootView.findViewById(R.id.edit_text_tip_entry)};
        }

        //Enter Tip or Enter Other Tip
        EditText tipInputEditText = rootView.findViewById(R.id.edit_text_tip_entry);
        tipInputEditText.addTextChangedListener(new AmountTextWatcher(maxLength, currency){
            @Override public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                tip = CurrencyUtils.parse(s.toString());
                clearCheckedText(rootView.findViewById(R.id.recycler_view_tip_options));
            }
        });
        tipInputEditText.setText("0");


        //No Tip
        Button noTipButton = rootView.findViewById(R.id.button_no_tip);
        if(isNoTipSelectionAllowed){
            noTipButton.setVisibility(View.VISIBLE);
            noTipButton.setOnClickListener( v -> submit(0));
        }

        //Confirm
        Button confirmButton = rootView.findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener( v -> onConfirmButtonClicked());
    }

    private void clearCheckedText(ViewGroup parent){
        for(int i=0; i<parent.getChildCount(); i++){
            if(parent.getChildAt(i) instanceof CheckedTextView) ((CheckedTextView) parent.getChildAt(i)).setChecked(false);
            else if(parent.getChildAt(i) instanceof ViewGroup) clearCheckedText((ViewGroup) parent.getChildAt(i));
        }
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

    private static class TipOption {
        long tipAmount;
        String tipRate;

        TipOption(long tipAmount, String tipRate){
            this.tipAmount = tipAmount;
            this.tipRate = tipRate;
        }
    }

    public interface TipOptionClickListener { void onTipOptionClick(TipOption tipOption);}

    public class TipOptionAdapter extends RecyclerView.Adapter<TipOptionAdapter.TipOptionHolder> {
        private List<TipOption> data;
        private LayoutInflater layoutInflater;
        private TipOptionClickListener tipOptionClickListener;

        TipOptionAdapter(Context context, List<TipOption> tipOptionList, TipOptionClickListener tipOptionClickListener){
            this.data = tipOptionList;
            this.layoutInflater = LayoutInflater.from(context);
            this.tipOptionClickListener = tipOptionClickListener;
        }

        @NonNull @Override public TipOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TipOptionHolder(layoutInflater.inflate(R.layout.fragment_checkable_item, parent, false));
        }


        @Override @SuppressLint("SetTextI18n") public void onBindViewHolder(@NonNull TipOptionHolder holder, int position) {
            String percentageSuffix = data.get(position).tipRate != null ? (" ("+ data.get(position).tipRate + ")") : "";
            holder.text.setText(CurrencyUtils.convert(data.get(position).tipAmount, currency) + percentageSuffix);
            holder.bindListener(data.get(position), tipOptionClickListener);
        }

        @Override public int getItemCount() {
            return data.size();
        }

        private class TipOptionHolder extends RecyclerView.ViewHolder{
            TextView text;

            public TipOptionHolder(@NonNull View itemView) {
                super(itemView);
                text = itemView.findViewById(R.id.checkable_text);
                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            public void bindListener(final TipOption tipOption, final TipOptionClickListener listener) {
                itemView.setOnClickListener(v -> {
                    listener.onTipOptionClick(tipOption);
                    clearCheckedText((ViewGroup) itemView.getParent());
                    ((CheckedTextView)itemView.findViewById(R.id.checkable_text)).setChecked(true);
                });
            }
        }
    }

    @Override
    protected void onConfirmButtonClicked(){
        submit(tip);
    }

    private void submit(long value) {
        Bundle bundle = new Bundle();
        bundle.putLong(EntryRequest.PARAM_TIP, value);
        sendNext(bundle);
    }
}
