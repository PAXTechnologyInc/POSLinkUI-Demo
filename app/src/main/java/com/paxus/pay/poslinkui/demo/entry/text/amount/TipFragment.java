package com.paxus.pay.poslinkui.demo.entry.text.amount;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.UnitType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_TIP}<br>
 * <p>
 *     UI Tips:
 *     If confirm button clicked, sendNext
 * </p>
 */
public class TipFragment extends BaseEntryFragment {
    private String transType;
    private String transMode;

    private long timeOut;
    private int minLength;
    private int maxLength;

    private String currency = "";
    private long[] tipOptions;
    private String[] percentages;
    private boolean noTip;
    private String tipName;
    private long baseAmount;
    private String tipUnit;
    private String[] enabledTipNames;
    private long[] enabledTipValues;
    private String packageName;
    private String action;
    private boolean isEditingOngoing = false;
    private boolean isSelectTipEnabled = false;

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    private TipOption selectedItem;

    private EditText editText;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_tip;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        currency =  bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");

        if(!TextUtils.isEmpty(valuePatten)){
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }

        String[] options = bundle.getStringArray(EntryExtraData.PARAM_TIP_OPTIONS);
        if(options != null){
            tipOptions = new long[options.length];
            for(int i = 0;i< options.length;i++){
                try {
                    tipOptions[i] = Long.parseLong(options[i]);
                }catch (Exception e){
                    tipOptions[i] = 0;
                }
            }
        }
        isSelectTipEnabled = tipOptions != null && tipOptions.length>0 || noTip;

        percentages = bundle.getStringArray(EntryExtraData.PARAM_TIP_RATE_OPTIONS);
        noTip = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_NO_TIP_SELECTION);
        tipName = bundle.getString(EntryExtraData.PARAM_TIP_NAME);
        baseAmount = bundle.getLong(EntryExtraData.PARAM_BASE_AMOUNT, -1);
        tipUnit = bundle.getString(EntryExtraData.PARAM_TIP_UNIT, UnitType.CENT);
        enabledTipNames = bundle.getStringArray(EntryExtraData.PARAM_TIP_NAMES);

        String[] amounts = bundle.getStringArray(EntryExtraData.PARAM_TIP_AMOUNTS);
        if(amounts != null){
            enabledTipValues = new long[amounts.length];
            for(int i = 0;i< amounts.length;i++){
                try {
                    enabledTipValues[i] = Long.parseLong(amounts[i]);
                }catch (Exception e){
                    enabledTipValues[i] = 0;
                }
            }
        }
    }

    @Override
    protected void loadView(View rootView) {

        if(baseAmount > 0){
            rootView.findViewById(R.id.base_amount_layout).setVisibility(View.VISIBLE);
            ((TextView)rootView.findViewById(R.id.base_amount)).setText(CurrencyUtils.convert(baseAmount, currency));
        }

        if(enabledTipNames != null && enabledTipNames.length> 1){
            rootView.findViewById(R.id.tips_summary).setVisibility(View.VISIBLE);

            if(enabledTipValues != null){
                if(enabledTipValues.length >= 1 && enabledTipValues[0]!=0){
                    rootView.findViewById(R.id.summary_tip1).setVisibility(View.VISIBLE);
                    ((TextView)rootView.findViewById(R.id.summary_tip1_name)).setText(enabledTipNames[0]);
                    TextView tip1 = rootView.findViewById(R.id.summary_tip1_amt);
                    tip1.setText(CurrencyUtils.convert(enabledTipValues.length >= 1 ? enabledTipValues[0] : 0, currency));
                }
                if(enabledTipValues.length >= 2 && enabledTipValues[1]!=0){
                    rootView.findViewById(R.id.summary_tip2).setVisibility(View.VISIBLE);
                    ((TextView)rootView.findViewById(R.id.summary_tip2_name)).setText(enabledTipNames[1]);
                    TextView tip2 = rootView.findViewById(R.id.summary_tip2_amt);
                    tip2.setText(CurrencyUtils.convert(enabledTipValues.length >= 2 ? enabledTipValues[1] : 0, currency));
                }
                if(enabledTipValues.length >= 3 && enabledTipValues[2]!=0){
                    rootView.findViewById(R.id.summary_tip3).setVisibility(View.VISIBLE);
                    ((TextView)rootView.findViewById(R.id.summary_tip3_name)).setText(enabledTipNames[2]);
                    TextView tip3 = rootView.findViewById(R.id.summary_tip3_amt);
                    tip3.setText(CurrencyUtils.convert(enabledTipValues.length >= 3 ? enabledTipValues[2] : 0, currency));
                }
            }
        }

        TextView tvTipName = rootView.findViewById(R.id.tip_name);
        tvTipName.setText((isSelectTipEnabled ? "Select " : "Enter ") + tipName);

        RecyclerView optionView = rootView.findViewById(R.id.options_layout);
        if(isSelectTipEnabled) {
            List<TipOption> options = new ArrayList<>();
            for(long amt: tipOptions){
                options.add(new TipOption(amt));
            }
            for(int i = 0;i<percentages.length;i++){
                options.get(i).percentage = percentages[i];
            }
            if(noTip){
                options.add(new TipOption(0));
            }
            TipOption op = new TipOption(0);
            op.editStyle = true;
            options.add(op);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            optionView.setLayoutManager(linearLayoutManager);
            optionView.setAdapter(new Adapter(options));
        }

        editText = rootView.findViewById(R.id.edit_tip);
        if(isSelectTipEnabled){
            editText.setVisibility(View.GONE);
        }else {
            editText.setVisibility(View.VISIBLE);
            prepareEditTextsForSubmissionWithSoftKeyboard(editText);
            if(UnitType.CENT.equals(tipUnit)) {
                editText.addTextChangedListener(new AmountTextWatcher(maxLength, currency));
            }
        }

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener( v -> onConfirmButtonClicked());


    }

    @Override
    protected void onConfirmButtonClicked(){
        if(editText.getVisibility() == View.VISIBLE) {
            String text = editText.getText().toString();
            long value = CurrencyUtils.parse(text);
            if(UnitType.DOLLAR.equals(tipUnit)){
                value = value*100;
            }
            sendNext(value);
        }else {
            if(selectedItem != null){
                sendNext(selectedItem.tipAmt);
            }
        }
    }

    private void sendNext(long value){

        String param = EntryRequest.PARAM_TIP;
        EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder>{

        private final List<TipOption> list;
        public Adapter(List<TipOption> list){
            this.list = list;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cashback_option, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TipOption option = list.get(position);

            holder.optionButton.setChecked(option.selected);
            holder.optionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(TipOption option: list){
                        option.selected = false;
                    }
                    option.selected = true;
                    selectedItem = option;
                    notifyDataSetChanged();

                    ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });

            if(option.editStyle){
                holder.optionButton.setText("");

                holder.editText.setVisibility(View.VISIBLE);
                holder.editText.setHint(getString(R.string.other));

                if(UnitType.CENT.equals(tipUnit)) {
                    holder.editText.addTextChangedListener(new AmountTextWatcher(maxLength, currency) {
                        @Override
                        public void afterTextChanged(Editable s) {
                            super.afterTextChanged(s);
                            option.tipAmt = CurrencyUtils.parse(s.toString());
                        }
                    });
                }else {
                    holder.editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                        @Override
                        public void afterTextChanged(Editable editable) {
                            if(isEditingOngoing) return;
                            isEditingOngoing = true;
                            option.tipAmt = CurrencyUtils.parse(editable.toString()) * 100;
                            isEditingOngoing = false;
                        }
                    });
                }
            }else {
                holder.editText.setVisibility(View.GONE);
                if(option.tipAmt == 0){
                    holder.optionButton.setText(getString(R.string.no_tip));
                }else {
                    if(!TextUtils.isEmpty(option.percentage)) {
                        holder.optionButton.setText(CurrencyUtils.convert(option.tipAmt, currency) + " (" + option.percentage + ")");
                    }else {
                        holder.optionButton.setText(CurrencyUtils.convert(option.tipAmt, currency));
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private static class TipOption {
         boolean selected;
         long tipAmt;
         String percentage;
         boolean editStyle;
         TipOption(long tipAmt){
             this.tipAmt = tipAmt;
         }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatRadioButton optionButton;
        EditText editText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            optionButton = itemView.findViewById(R.id.option_item);
            editText = itemView.findViewById(R.id.edit_item);
        }
    }
}
