package com.paxus.pay.poslinkui.demo.entry.text;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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

public class CashbackFragment extends Fragment {
    private String action;
    private String packageName;
    private String transType;
    private String transMode;

    private long timeOut;
    private int minLength;
    private int maxLength;

    private String currency = "";
    private long[] cashBackOptions;
    private boolean promptOther;

    private CashbackOption selectedItem;

    public static CashbackFragment newInstance(Intent intent){
        CashbackFragment numFragment = new CashbackFragment();
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
        return inflater.inflate(R.layout.fragment_cashback, container, false);
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

        boolean haveOptions = cashBackOptions != null && cashBackOptions.length>0;

        RecyclerView optionView = view.findViewById(R.id.options_layout);
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
        TextView textView = view.findViewById(R.id.message);
        if(haveOptions){
            textView.setText(getString(R.string.select_cashback_amount));
        }else {
            textView.setText(getString(R.string.prompt_input_cashback));
        }
        EditText editText = view.findViewById(R.id.edit_cashback);
        if(haveOptions){
            editText.setVisibility(View.GONE);
        }else {
            editText.setVisibility(View.VISIBLE);
            editText.addTextChangedListener(new AmountTextWatcher(maxLength,currency));
        }
        Button confirmBtn = view.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editText.getVisibility() == View.VISIBLE) {
                        long value = CurrencyUtils.parse(editText.getText().toString());
                        sendNext(value);
                    }else {
                        if(selectedItem != null){
                            sendNext(selectedItem.cashbackAmt);
                        }
                    }
                }
            });


    }


    private void sendNext(long value){

        String param = EntryRequest.PARAM_CASHBACK_AMOUNT;
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
