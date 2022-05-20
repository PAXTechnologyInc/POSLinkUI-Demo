package com.paxus.pay.poslinkui.demo.entry.text;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Implement text entry action {@value TextEntry#ACTION_ENTER_AVS_DATA}<br>
 * <p>
 *     UI Tips:
 *     If confirm button clicked, sendNext
 * </p>
 */
public class AVSFragment extends BaseEntryFragment {
    private String transType;
    private long timeOut;
    private int minLengthAddr;
    private int maxLengthAddr;
    private int minLengthZip;
    private int maxLengthZip;
    private String transMode;
    private boolean zipText;

    private EditText editTextAddr;
    private EditText editTextZip;

    public static Fragment newInstance(Intent intent){
        AVSFragment fragment = new AVSFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_avs;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        String valuePattenAddr = bundle.getString(EntryExtraData.PARAM_ADDRESS_PATTERN,"0-30");
        String valuePattenZip = bundle.getString(EntryExtraData.PARAM_ZIP_CODE_PATTERN,"0-9");


        if(!TextUtils.isEmpty(valuePattenAddr)){
            minLengthAddr = ValuePatternUtils.getMinLength(valuePattenAddr);
            maxLengthAddr = ValuePatternUtils.getMaxLength(valuePattenAddr);
        }

        if(!TextUtils.isEmpty(valuePattenZip) ){
            minLengthZip = ValuePatternUtils.getMinLength(valuePattenZip);
            maxLengthZip = ValuePatternUtils.getMaxLength(valuePattenZip);
        }
        zipText = InputType.ALLTEXT.equals(bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE));

    }

    @Override
    protected void loadView(View rootView) {
        

        

        editTextAddr = rootView.findViewById(R.id.edit_address);
        if(maxLengthAddr > 0 ) {
            editTextAddr.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthAddr)});
        }

        editTextZip = rootView.findViewById(R.id.edit_zip);
        if(maxLengthZip > 0 ) {
            editTextAddr.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthZip)});
        }
        if(zipText){
            editTextAddr.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        }else {
            editTextAddr.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        }

        //Send Next when clicking confirm button
        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener( v-> onConfirmButtonClicked());
    }

    //If confirm button clicked, sendNext
    private void onConfirmButtonClicked(){
        String addr = editTextAddr.getText().toString();
        String zip = editTextZip.getText().toString();

        if(zip.length() < minLengthZip){
            Toast.makeText(requireContext(), getString(R.string.pls_input_zip_code), Toast.LENGTH_SHORT).show();
        }else {
            EntryRequestUtils.sendNextAVS(requireContext(),packageName,action,addr,zip);
        }
    }
}
