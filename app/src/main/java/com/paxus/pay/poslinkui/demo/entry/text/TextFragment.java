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
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

/**
 * Implement text entry actions:<br>
 * {@value TextEntry#ACTION_ENTER_ADDRESS}<br>
 * {@value TextEntry#ACTION_ENTER_AUTH}<br>
 * {@value TextEntry#ACTION_ENTER_CUSTOMER_CODE}<br>
 * {@value TextEntry#ACTION_ENTER_ORDER_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_PO_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_PROD_DESC}<br>
 *
 * <p>
 *     UI Tips:
 *     If confirm button clicked, sendNext
 * </p>
 */
public class TextFragment extends BaseEntryFragment {
    private String transType;
    private long timeOut;
    private int minLength;
    private int maxLength;
    private String message = "";
    private String transMode;

    private EditText editText;
    public static Fragment newInstance(Intent intent){
        TextFragment fragment = new TextFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_base_text;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        String valuePatten = "";
        if(TextEntry.ACTION_ENTER_ADDRESS.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-30");
            message = getString(R.string.pls_input_address);
        } else if(TextEntry.ACTION_ENTER_AUTH.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"1-15");
            message = getString(R.string.please_enter_auth_code);
        } else if(TextEntry.ACTION_ENTER_CUSTOMER_CODE.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-25");
            message = getString(R.string.pls_input_customer_code);
        } else if(TextEntry.ACTION_ENTER_ORDER_NUMBER.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-12");
            message = getString(R.string.pls_input_order_number);
        } else if(TextEntry.ACTION_ENTER_PO_NUMBER.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-17");
            message = getString(R.string.pls_input_po_number);
        } else if(TextEntry.ACTION_ENTER_PROD_DESC.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-40");
            message = getString(R.string.pls_input_proc_desc);
        }

        if(!TextUtils.isEmpty(valuePatten)){
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }

    }

    @Override
    protected void loadView(View rootView) {
        

        

        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_text);
        if(maxLength > 0 ) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());
    }

    //If confirm button clicked, sendNext
    private void onConfirmButtonClicked(){
        String value = editText.getText().toString();
        if(minLength == maxLength && maxLength>0){
            Toast.makeText(requireContext(), "Must be "+minLength+" digits.", Toast.LENGTH_SHORT).show();
        }else if(value.length() < minLength){
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }else {
            sendNext(value);
        }
    }

    private void sendNext(String value){

        String param = "";

        if(TextEntry.ACTION_ENTER_ADDRESS.equals(action)){
            param = EntryRequest.PARAM_ADDRESS;
        } else if(TextEntry.ACTION_ENTER_AUTH.equals(action)){
            param = EntryRequest.PARAM_AUTH_CODE;
        } else if(TextEntry.ACTION_ENTER_CUSTOMER_CODE.equals(action)){
            param = EntryRequest.PARAM_CUSTOMER_CODE;
        } else if(TextEntry.ACTION_ENTER_ORDER_NUMBER.equals(action)){
            param = EntryRequest.PARAM_ORDER_NUMBER;
        } else if(TextEntry.ACTION_ENTER_PO_NUMBER.equals(action)){
            param = EntryRequest.PARAM_PO_NUMBER;
        } else if(TextEntry.ACTION_ENTER_PROD_DESC.equals(action)){
            param = EntryRequest.PARAM_PROD_DESC;
        }

        if(!TextUtils.isEmpty(param)){
            EntryRequestUtils.sendNext(requireContext(), packageName, action, param,value);
        }
    }

}
