package com.paxus.pay.poslinkui.demo.entry.text;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

public class NumFragment extends BaseEntryFragment {
    private String transType;
    private long timeOut;
    private int minLength;
    private int maxLength;
    private String message = "";
    private String transMode;

    public static NumFragment newInstance(Intent intent){
        NumFragment numFragment = new NumFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        numFragment.setArguments(bundle);
        return numFragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_base_num;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {

        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        String valuePatten = "";
        if(TextEntry.ACTION_ENTER_CLERK_ID.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-4");
            message = getString(R.string.pls_input_clerk_id);
        } else if(TextEntry.ACTION_ENTER_SERVER_ID.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-4");
            message = getString(R.string.pls_input_server_id);
        } else if(TextEntry.ACTION_ENTER_TABLE_NUMBER.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-4");
            message = getString(R.string.pls_input_table_number);
        } else if(TextEntry.ACTION_ENTER_CS_PHONE_NUMBER.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-32");
            message = getString(R.string.pls_input_cs_phone_number);
        } else if(TextEntry.ACTION_ENTER_PHONE_NUMBER.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-32");
            message = getString(R.string.pls_input_phone_number);
        } else if(TextEntry.ACTION_ENTER_GUEST_NUMBER.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-4");
            message = getString(R.string.pls_input_guest_number);
        } else if(TextEntry.ACTION_ENTER_MERCHANT_TAX_ID.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-15");
            message = getString(R.string.prompt_merchant_tax_id);
        } else if(TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"2-2");
            message = getString(R.string.pls_input_prompt_restriction_code);
        }else if(TextEntry.ACTION_ENTER_TRANS_NUMBER.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-4");
            message = getString(R.string.pls_input_transaction_number);
        }

        if(!TextUtils.isEmpty(valuePatten) && valuePatten.contains("-")){
            String[] tmp = valuePatten.split("-");
            if(tmp.length == 2) {
                minLength = Integer.parseInt(tmp[0]);
                maxLength = Integer.parseInt(tmp[1]);
            }
        }

    }

    @Override
    protected void loadView(View rootView) {
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

        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        EditText editText = rootView.findViewById(R.id.edit_number);
        if(maxLength > 0 ) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
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
        if(TextEntry.ACTION_ENTER_TRANS_NUMBER.equals(action)){
            EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_TRANS_NUMBER,Long.parseLong(value));
            return;
        }
        String param = "";

        if(TextEntry.ACTION_ENTER_CLERK_ID.equals(action)){
            param = EntryRequest.PARAM_CLERK_ID;
        }else if(TextEntry.ACTION_ENTER_SERVER_ID.equals(action)){
            param = EntryRequest.PARAM_SERVER_ID;
        } else if(TextEntry.ACTION_ENTER_TABLE_NUMBER.equals(action)){
            param = EntryRequest.PARAM_TABLE_NUMBER;
        } else if(TextEntry.ACTION_ENTER_CS_PHONE_NUMBER.equals(action)){
            param = EntryRequest.PARAM_CS_PHONE_NUMBER;
        } else if(TextEntry.ACTION_ENTER_PHONE_NUMBER.equals(action)){
            param = EntryRequest.PARAM_PHONE_NUMBER;
        } else if(TextEntry.ACTION_ENTER_GUEST_NUMBER.equals(action)){
            param = EntryRequest.PARAM_GUEST_NUMBER;
        } else if(TextEntry.ACTION_ENTER_MERCHANT_TAX_ID.equals(action)){
            param = EntryRequest.PARAM_MERCHANT_TAX_ID;
        } else if(TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE.equals(action)){
            param = EntryRequest.PARAM_PROMPT_RESTRICTION_CODE;
        }
        if(!TextUtils.isEmpty(param)){
            EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
        }
    }


}
