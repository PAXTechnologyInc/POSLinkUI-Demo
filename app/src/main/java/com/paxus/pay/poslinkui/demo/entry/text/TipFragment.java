package com.paxus.pay.poslinkui.demo.entry.text;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.pax.us.pay.ui.constant.entry.enumeration.UnitType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.EntryResponseEvent;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TipFragment extends Fragment {
    private String action;
    private String packageName;
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

    private TipOption selectedItem;

    public static TipFragment newInstance(Intent intent){
        TipFragment numFragment = new TipFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        numFragment.setArguments(bundle);
        return numFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadArgument(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadView(view);
        EventBus.getDefault().register(this);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);

    }

    private void loadArgument(Bundle bundle){
        if(bundle == null){
            return;
        }
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        currency =  bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");

        if(!TextUtils.isEmpty(valuePatten) && valuePatten.contains("-")){
            String[] tmp = valuePatten.split("-");
            if(tmp.length == 2) {
                minLength = Integer.parseInt(tmp[0]);
                maxLength = Integer.parseInt(tmp[1]);
            }
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

    private void loadView(View view){
        if(!TextUtils.isEmpty(transType) && getActivity() instanceof AppCompatActivity){
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(transType);
            }
        }

        String mode = null;
        if(!TextUtils.isEmpty(transMode)){
            if(TransMode.DEMO.equals(transMode)){
                mode = getString(R.string.demo_only);
            }else if(TransMode.TEST.equals(transMode)){
                mode = getString(R.string.test_only);
            }else if(TransMode.TEST_AND_DEMO.equals(transMode)){
                mode = getString(R.string.test_and_demo);
            }else {
                mode = "";
            }
        }
        if(!TextUtils.isEmpty(mode)){
            ViewUtils.addWaterMarkView(requireActivity(),mode);
        }else{
            ViewUtils.removeWaterMarkView(requireActivity());
        }

        TextView tvBaseAmount = view.findViewById(R.id.base_amount);
        if(baseAmount > 0){
            tvBaseAmount.setText(CurrencyUtils.convert(baseAmount, currency));
        }else {
            tvBaseAmount.setVisibility(View.INVISIBLE);
        }

        TextView tvTipName = view.findViewById(R.id.tip_name);
        tvTipName.setText(tipName);

        boolean haveOptions = tipOptions != null && tipOptions.length>0 || noTip;

        RecyclerView optionView = view.findViewById(R.id.options_layout);
        if(haveOptions) {
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
        TextView textView = view.findViewById(R.id.message);
        if(haveOptions){
            textView.setVisibility(View.GONE);
        }else {
            textView.setText(getString(R.string.prompt_input_tip));
        }

        EditText editText = view.findViewById(R.id.edit_tip);
        if(haveOptions){
            editText.setVisibility(View.GONE);
        }else {
            editText.setVisibility(View.VISIBLE);
            if(UnitType.CENT.equals(tipUnit)) {
                editText.addTextChangedListener(new AmountTextWatcher(maxLength, currency));
            }
        }
        Button confirmBtn = view.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
            });

        View tipSummary = view.findViewById(R.id.tips_summary);
        if(enabledTipNames != null && enabledTipNames.length> 1){
            TextView sale = view.findViewById(R.id.summary_sale);

            sale.setText(CurrencyUtils.convert(baseAmount,currency));
            sale.setTextColor(Color.BLUE);

            ((TextView)view.findViewById(R.id.summary_tip1_name)).setText(enabledTipNames[0]);
            ((TextView)view.findViewById(R.id.summary_tip2_name)).setText(enabledTipNames[1]);
            if(enabledTipNames.length >= 3){
                ((TextView)view.findViewById(R.id.summary_tip3_name)).setText(enabledTipNames[2]);
            }else {
                view.findViewById(R.id.summary_tip3).setVisibility(View.GONE);
            }

            if(enabledTipValues != null){
                TextView tip1 = view.findViewById(R.id.summary_tip1_amt);
                TextView tip2 = view.findViewById(R.id.summary_tip2_amt);
                TextView tip3 = view.findViewById(R.id.summary_tip3_amt);

                if(enabledTipValues.length >= 1){
                    tip1.setText(CurrencyUtils.convert(enabledTipValues[0],currency));
                    tip1.setTextColor(Color.BLUE);
                }else {
                    tip1.setText(CurrencyUtils.convert(0,currency));
                }

                if(enabledTipValues.length >= 2){
                    tip2.setText(CurrencyUtils.convert(enabledTipValues[1],currency));
                    tip2.setTextColor(Color.BLUE);
                }else {
                    tip2.setText(CurrencyUtils.convert(0,currency));
                }

                if(enabledTipValues.length >= 3){
                    tip3.setText(CurrencyUtils.convert(enabledTipValues[2],currency));
                    tip3.setTextColor(Color.BLUE);
                }else {
                    tip3.setText(CurrencyUtils.convert(0,currency));
                }
            }
        }else {
            tipSummary.setVisibility(View.GONE);
        }


    }


    private void sendNext(long value){

        String param = EntryRequest.PARAM_TIP;
        EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
    }

    private void sendAbort(){
        EntryRequestUtils.sendAbort(requireContext(), packageName, action);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryAbort(EntryAbortEvent event) {
        
        sendAbort();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetEntryResponse(EntryResponseEvent event){
        switch (event.action){
            case EntryResponse.ACTION_ACCEPTED:
                Log.d("POSLinkUI","Entry "+action+" accepted");
                break;
            case EntryResponse.ACTION_DECLINED:{
                Log.d("POSLinkUI","Entry "+action+" declined("+event.code+"-"+event.message+")");
                Toast.makeText(requireActivity(),event.message,Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder>{

        private final List<TipOption> list;
        public Adapter(List<TipOption> list){
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
                }
            });
            if(option.editStyle){
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
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            option.tipAmt = CurrencyUtils.parse(editable.toString()) * 100;
                        }
                    });
                }
                holder.optionButton.setText("");
            }else {
                holder.editText.setVisibility(View.GONE);
                if(option.tipAmt == 0){
                    holder.optionButton.setText(getString(R.string.no_tip));
                }else {
                    if(!TextUtils.isEmpty(option.percentage)) {
                        holder.optionButton.setText(CurrencyUtils.convert(option.tipAmt, currency) + "(" + option.percentage + ")");
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

    private class TipOption {
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
