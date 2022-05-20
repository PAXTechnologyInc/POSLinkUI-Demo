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
import com.pax.us.pay.ui.constant.entry.enumeration.InputType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;
/**
 * Implement text entry actions:<br>
 * {@value TextEntry#ACTION_ENTER_ZIPCODE}<br>
 * {@value TextEntry#ACTION_ENTER_DEST_ZIPCODE}<br>
 * {@value TextEntry#ACTION_ENTER_INVOICE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_VOUCHER_DATA}<br>
 * {@value TextEntry#ACTION_ENTER_REFERENCE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_MERCHANT_REFERENCE_NUMBER}<br>
 * {@value TextEntry#ACTION_ENTER_OCT_REFERENCE_NUMBER}<br>
 *
 * <p>
 *     UI Tips:
 *     If confirm button clicked, sendNext
 * </p>
 */
public class NumTextFragment extends BaseEntryFragment {
    private String transType;
    private long timeOut;
    private int minLength;
    private int maxLength;
    private String message = "";
    private String transMode;
    private boolean allText;

    private EditText editText;

    public static Fragment newInstance(Intent intent){
        NumTextFragment fragment = new NumTextFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_base_num_text;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
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

        if(!TextUtils.isEmpty(valuePatten)){
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }

        allText = InputType.ALLTEXT.equals(bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE));

    }

    @Override
    protected void loadView(View rootView) {
        

        

        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_number_text);
        if(maxLength > 0 ) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        if(allText){
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        }else {
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        }

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());
    }

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

}
