package com.paxus.pay.poslinkui.demo.entry.text;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
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
import com.pax.us.pay.ui.constant.entry.enumeration.InputType;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.EntryResponseEvent;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NumTextFragment extends Fragment {
    private String action;
    private String packageName;
    private String transType;
    private long timeOut;
    private int minLength;
    private int maxLength;
    private String message = "";
    private String transMode;
    private boolean allText;

    public static NumTextFragment newInstance(Intent intent){
        NumTextFragment numFragment = new NumTextFragment();
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
        return inflater.inflate(R.layout.fragment_base_num_text, container, false);
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

        String valuePatten = "";
        if(TextEntry.ACTION_ENTER_ZIPCODE.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-9");
            message = getString(R.string.pls_input_zip_code);
        } else if(TextEntry.ACTION_ENTER_DEST_ZIPCODE.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-9");
            message = getString(R.string.pls_input_dest_zip_code);
        } else if(TextEntry.ACTION_ENTER_INVOICE_NUMBER.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-20");
            message = getString(R.string.pls_input_invoice_number);
        } else if(TextEntry.ACTION_ENTER_REFERENCE_NUMBER.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-16");
            message = getString(R.string.pls_input_reference_number);
        } else if(TextEntry.ACTION_ENTER_VOUCHER_DATA.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-15");
            message = getString(R.string.please_enter_voucher_number);
        }else if(TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");
            message = getString(R.string.pls_input_merchant_reference_number);
        } else if(TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");
            message = getString(R.string.pls_input_oct_reference_number);
        }

        if(!TextUtils.isEmpty(valuePatten) && valuePatten.contains("-")){
            String[] tmp = valuePatten.split("-");
            if(tmp.length == 2) {
                minLength = Integer.parseInt(tmp[0]);
                maxLength = Integer.parseInt(tmp[1]);
            }
        }

        allText = InputType.ALLTEXT.equals(bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE));

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

        EditText editText = view.findViewById(R.id.edit_text);
        if(maxLength > 0 ) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        if(allText){
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        }else {
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        }

        Button confirmBtn = view.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = editText.getText().toString();
                if(minLength == maxLength && maxLength>0){
                    Toast.makeText(requireContext(), "Must be "+minLength+" digits.", Toast.LENGTH_SHORT).show();
                }else if(value.length() < minLength){
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }else {
                    sendNext(value);
                }
            }
        });
    }

    private void sendNext(String value){

        String param = "";

        if(TextEntry.ACTION_ENTER_ZIPCODE.equals(action)){
            param = EntryRequest.PARAM_ZIP_CODE;
        } else if(TextEntry.ACTION_ENTER_DEST_ZIPCODE.equals(action)){
            param = EntryRequest.PARAM_DEST_ZIP_CODE;
        } else if(TextEntry.ACTION_ENTER_INVOICE_NUMBER.equals(action)){
            param = EntryRequest.PARAM_INVOICE_NUMBER;
        } else if(TextEntry.ACTION_ENTER_VOUCHER_DATA.equals(action)){
            param = EntryRequest.PARAM_VOUCHER_NUMBER;
        } else if(TextEntry.ACTION_ENTER_REFERENCE_NUMBER.equals(action)){
            param = EntryRequest.PARAM_REFERENCE_NUMBER;
        } else if(TextEntry.ACTION_ENTER_MERCHANT_REFERENCE_NUMBER.equals(action)){
            param = EntryRequest.PARAM_MERCHANT_REFERENCE_NUMBER;
        }else if(TextEntry.ACTION_ENTER_OCT_REFERENCE_NUMBER.equals(action)){
            param = EntryRequest.PARAM_OCT_REFERENCE_NUMBER;
        }

        if(!TextUtils.isEmpty(param)){
            EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
        }
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



}
