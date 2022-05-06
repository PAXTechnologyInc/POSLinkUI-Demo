package com.paxus.pay.poslinkui.demo.entry.text;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.EntryAcceptedEvent;
import com.paxus.pay.poslinkui.demo.event.EntryDeclinedEvent;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TotalAmountFragment extends Fragment {
    private String action;
    private String packageName;
    private String transType;
    private String transMode;

    private long timeOut;
    private int minLength;
    private int maxLength;
    private String message = "";

    private String currency = "";
    private long baseAmount;
    private boolean noTipEnabled;
    private String tipName;

    public static TotalAmountFragment newInstance(Intent intent){
        TotalAmountFragment numFragment = new TotalAmountFragment();
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
        return inflater.inflate(R.layout.fragment_total_amount, container, false);
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

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-12");
        message = getString(R.string.prompt_input_total_amount);

        if(!TextUtils.isEmpty(valuePatten) && valuePatten.contains("-")){
            String[] tmp = valuePatten.split("-");
            if(tmp.length == 2) {
                minLength = Integer.parseInt(tmp[0]);
                maxLength = Integer.parseInt(tmp[1]);
            }
        }

        baseAmount = bundle.getLong(EntryExtraData.PARAM_BASE_AMOUNT);
        noTipEnabled = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_NO_TIP_SELECTION);
        tipName = bundle.getString(EntryExtraData.PARAM_TIP_NAME);

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

        editText.addTextChangedListener(new TextWatcher() {
            protected boolean mEditing;
            protected String mPreStr;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!mEditing) {
                    mPreStr = s.toString();
                }
                Log.d("AmountFragment","beforeTextChanged:"+mPreStr);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("AmountFragment","onTextChanged:"+mPreStr);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mEditing) {
                    mEditing = true;
                    String value = s.toString().replaceAll("[^0-9]", "");
                    if (value.length() == 0) {
                        value = "0";
                    }
                    if(value.length() > maxLength){
                        s.replace(0, s.length(), mPreStr);
                    }else {
                        String formatted = CurrencyUtils.convert(Long.parseLong(value), currency);
                        s.replace(0, s.length(), formatted);
                        Log.d("AmountFragment","afterTextChanged:"+formatted);
                    }
                    mEditing = false;
                }
            }
        });

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
        TextView baseAmountTv = view.findViewById(R.id.base_amount);
        baseAmountTv.setText(CurrencyUtils.convert(baseAmount,currency));

        TextView tipNameTv = view.findViewById(R.id.tip_name);
        if(!TextUtils.isEmpty(tipName)){
            tipNameTv.setText(tipName);
        }

        Button noTipButton = view.findViewById(R.id.no_tip_button);
        if(noTipEnabled){
            noTipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendNext(baseAmount);
                }
            });
        }else {
            noTipButton.setVisibility(View.GONE);
        }

    }


    private void sendNext(long value){

        String param = EntryRequest.PARAM_TOTAL_AMOUNT;
        EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
    }

    private void sendAbort(){
        EntryRequestUtils.sendAbort(requireContext(), packageName, action);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryAbort(EntryAbortEvent event) {
        // Do something
        sendAbort();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryAccepted(EntryAcceptedEvent event) {
        // Do something
        Toast.makeText(requireActivity(),"Accepted",Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryDeclined(EntryDeclinedEvent event) {
        // Do something

        Toast.makeText(requireActivity(),event.code+"-"+event.message,Toast.LENGTH_SHORT).show();
    }

}
