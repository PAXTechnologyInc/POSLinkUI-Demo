package com.paxus.pay.poslinkui.demo.entry.text.amount;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_CASH_BACK}<br>
 * <p>
 *     UI Tips:
 *     If confirm button clicked, sendNext
 * </p>
 */
public class CashbackFragment extends BaseEntryFragment {
    private String transType;
    private String transMode;

    private long timeOut;
    private int minLength;
    private int maxLength;

    private String currency = "";
    private long[] cashBackOptions;
    private boolean promptOther;
    protected String packageName;
    protected String action;

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    private CashbackOption selectedItem;

    private EditText editText;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_cashback;
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

        String[] options = bundle.getStringArray(EntryExtraData.PARAM_CASHBACK_OPTIONS);
        if(options != null){
            cashBackOptions = new long[options.length];
            for(int i = 0;i< options.length;i++){
                try {
                    cashBackOptions[i] = Long.parseLong(options[i]);
                }catch (Exception e){
                    cashBackOptions[i] = 0;
                }
            }
        }
        promptOther = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_OTHER_PROMPT);

    }

    @Override
    protected void loadView(View rootView) {

        boolean haveOptions = cashBackOptions != null && cashBackOptions.length>0;

        RecyclerView optionView = rootView.findViewById(R.id.options_layout);
        if(haveOptions) {
            List<CashbackOption> options = new ArrayList<>();
            for(long amt: cashBackOptions){
                options.add(new CashbackOption(amt));
            }
            if(promptOther){
                CashbackOption op = new CashbackOption(0);
                op.editStyle = true;
                options.add(op);
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            optionView.setLayoutManager(linearLayoutManager);
            optionView.setAdapter(new Adapter(options));
        }
        TextView textView = rootView.findViewById(R.id.message);
        if(haveOptions){
            textView.setText(getString(R.string.select_cashback_amount));
        }else {
            textView.setText(getString(R.string.prompt_input_cashback));
        }

        editText = rootView.findViewById(R.id.edit_cashback);
        if(haveOptions){
            editText.setVisibility(View.GONE);
        }else {
            editText.setVisibility(View.VISIBLE);
            editText.addTextChangedListener(new AmountTextWatcher(maxLength,currency));
        }
        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());

    }

    //If confirm button clicked, sendNext
    private void onConfirmButtonClicked(){
        if(editText.getVisibility() == View.VISIBLE) {
            long value = CurrencyUtils.parse(editText.getText().toString());
            sendNext(value);
        }else {
            if(selectedItem != null){
                sendNext(selectedItem.cashbackAmt);
            }
        }
    }


    private void sendNext(long value){

        String param = EntryRequest.PARAM_CASHBACK_AMOUNT;
        EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder>{

        private final List<CashbackOption> list;
        public Adapter(List<CashbackOption> list){
            this.list = list;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_cashback_option, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CashbackOption option = list.get(position);

            holder.optionButton.setChecked(option.selected);
            holder.optionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(CashbackOption option: list){
                        option.selected = false;
                    }
                    option.selected = true;
                    selectedItem = option;

                    notifyDataSetChanged();
                }
            });
            if(option.editStyle){
                holder.editText.setVisibility(View.VISIBLE);
                holder.editText.setHint(getString(R.string.other_amount));
                holder.editText.addTextChangedListener(new AmountTextWatcher(maxLength,currency){
                    @Override
                    public void afterTextChanged(Editable s) {
                        super.afterTextChanged(s);
                        option.cashbackAmt = CurrencyUtils.parse(s.toString());
                    }
                });
                holder.optionButton.setText("");
            }else {
                holder.editText.setVisibility(View.GONE);
                if(option.cashbackAmt == 0){
                    holder.optionButton.setText(getString(R.string.no_thanks));
                }else {
                    holder.optionButton.setText(CurrencyUtils.convert(option.cashbackAmt, currency));
                }
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class CashbackOption{
         boolean selected;
         long cashbackAmt;
         boolean editStyle;
         CashbackOption(long cashbackAmt){
             this.cashbackAmt = cashbackAmt;
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
