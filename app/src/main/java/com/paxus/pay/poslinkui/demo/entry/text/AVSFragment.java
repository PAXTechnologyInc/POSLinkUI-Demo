package com.paxus.pay.poslinkui.demo.entry.text;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.InputType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.Toast;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;
import com.paxus.pay.poslinkui.demo.view.TextField;

import java.util.Arrays;
import java.util.List;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_AVS_DATA}<br>
 */
public class AVSFragment extends BaseEntryFragment {
    private long timeOut;
    private int minLengthAddr;
    private int maxLengthAddr;
    private int minLengthZip;
    private int maxLengthZip;
    private boolean zipText;

    private TextField editTextAddr;
    private TextField editTextZip;
    protected boolean allowPassword;
    protected boolean allowText;
    String valuePattenAddr;
    String valuePattenZip;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_avs;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        valuePattenAddr = bundle.getString(EntryExtraData.PARAM_ADDRESS_PATTERN,"0-30");
        valuePattenZip = bundle.getString(EntryExtraData.PARAM_ZIP_CODE_PATTERN,"0-9");


        if(!TextUtils.isEmpty(valuePattenAddr)){
            minLengthAddr = ValuePatternUtils.getMinLength(valuePattenAddr);
            maxLengthAddr = ValuePatternUtils.getMaxLength(valuePattenAddr);
        }

        if(!TextUtils.isEmpty(valuePattenZip) ){
            minLengthZip = ValuePatternUtils.getMinLength(valuePattenZip);
            maxLengthZip = ValuePatternUtils.getMaxLength(valuePattenZip);
        }
        allowText = Arrays.asList(InputType.ALLTEXT, InputType.PASSWORD).contains(bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE));
        allowPassword = Arrays.asList(InputType.PASSWORD, InputType.PASSCODE).contains(bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE));
    }

    @Override
    protected void loadView(View rootView) {
        editTextAddr = rootView.findViewById(R.id.edit_address);
        editTextZip = rootView.findViewById(R.id.edit_zip);

        if(maxLengthAddr > 0 ) editTextAddr.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthAddr)});
        if(maxLengthZip > 0 ) editTextZip.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthZip)});
        if (allowText) {
            editTextZip.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        } else {
            editTextZip.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        }
        if (allowPassword) {
            editTextZip.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            editTextZip.setTransformationMethod(null);
        }
        //Send Next when clicking confirm button
        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener( v-> onConfirmButtonClicked());
    }

    @Override
    protected void onConfirmButtonClicked(){
        String addr = editTextAddr.getText().toString();
        String zip = editTextZip.getText().toString();

        if(editTextAddr.hasFocus()) {
            if (getActivity().findViewById(editTextAddr.getNextFocusDownId()) != null) {
                (getActivity().findViewById(editTextAddr.getNextFocusDownId())).requestFocus();
                return;
            }

        }
        List<Integer> lengthListAddr = ValuePatternUtils.getLengthList(valuePattenAddr);
        List<Integer> lengthListZip = ValuePatternUtils.getLengthList(valuePattenZip);
        //For patterns like "0,2" which means 0 or 2, when the input length is 1,
        // we cannot immediately validate its effectiveness during the input process.
        // Therefore, we need to check before submission.
        if (!lengthListAddr.contains(addr.length())) {
            new Toast(getActivity()).show(getString(R.string.pls_input_address) + ": "+ getString(R.string.prompt_input_length, valuePattenAddr), Toast.TYPE.FAILURE);
        } else if (!lengthListZip.contains(zip.length())){
            new Toast(getActivity()).show(getString(R.string.pls_input_address) + ": "+ getString(R.string.prompt_input_length, valuePattenZip)+ getString(R.string.pls_input_zip_code), Toast.TYPE.FAILURE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(EntryRequest.PARAM_ADDRESS, addr);
            bundle.putString(EntryRequest.PARAM_ZIP_CODE, zip);
            sendNext(bundle);
        }
    }

    @Override
    protected TextField[] focusableTextFields() {
        return new TextField[]{editTextZip, editTextAddr};
    }
}
