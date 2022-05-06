package com.paxus.pay.poslinkui.demo.entry.text;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.pax.us.pay.ui.constant.entry.TextEntry;
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

public class AmountFragment extends Fragment {
    private String action;
    private String packageName;
    private String transType;
    private String transMode;

    private long timeOut;
    private int minLength;
    private int maxLength;
    private String message = "";
    private String currency = "";

    public static AmountFragment newInstance(Intent intent){
        AmountFragment numFragment = new AmountFragment();
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
        return inflater.inflate(R.layout.fragment_amount, container, false);
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

        String valuePatten = "";
        if(TextEntry.ACTION_ENTER_AMOUNT.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-12");
            if(CurrencyType.POINT.equals(currency)) {
                message = getString(R.string.prompt_input_point);
            }else {
                message = getString(R.string.prompt_input_amount);
            }
        } else if(TextEntry.ACTION_ENTER_FUEL_AMOUNT.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");
            message = getString(R.string.prompt_input_fuel_amount);
        } else if(TextEntry.ACTION_ENTER_TAX_AMOUNT.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");
            message = getString(R.string.prompt_input_tax_amount);
        }

        if(!TextUtils.isEmpty(valuePatten) && valuePatten.contains("-")){
            String[] tmp = valuePatten.split("-");
            if(tmp.length == 2) {
                minLength = Integer.parseInt(tmp[0]);
                maxLength = Integer.parseInt(tmp[1]);
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

        TextView textView = view.findViewById(R.id.message);
        textView.setText(message);

        EditText editText = view.findViewById(R.id.edit_amount);
        editText.setSelected(false);
        editText.setText(CurrencyUtils.convert(0,currency));
        editText.setSelection(editText.getEditableText().length());

        editText.addTextChangedListener(new AmountTextWatcher(maxLength, currency));

        Button confirmBtn = view.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long value = CurrencyUtils.parse(editText.getText().toString());
                if(String.valueOf(value).length() < minLength){
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }else {
                    sendNext(value);
                }
            }
        });

    }


    private void sendNext(long value){

        String param = "";
        if(TextEntry.ACTION_ENTER_AMOUNT.equals(action)){
            param = EntryRequest.PARAM_AMOUNT;
        }else if(TextEntry.ACTION_ENTER_FUEL_AMOUNT.equals(action)){
            param = EntryRequest.PARAM_FUEL_AMOUNT;
        } else if(TextEntry.ACTION_ENTER_TAX_AMOUNT.equals(action)){
            param = EntryRequest.PARAM_TAX_AMOUNT;
        }
        if(!TextUtils.isEmpty(param)){
            EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
        }
    }

    private void sendAbort(){
        EntryRequestUtils.sendAbort(requireContext(), packageName, action);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryAbort(EntryAbortEvent event) {
        
        sendAbort();
    }

}
