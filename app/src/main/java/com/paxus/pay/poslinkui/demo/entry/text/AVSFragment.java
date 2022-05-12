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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.InputType;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

/**
 * Implement text entry action {@value TextEntry#ACTION_ENTER_AVS_DATA}<br>
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

    public static AVSFragment newInstance(Intent intent){
        AVSFragment numFragment = new AVSFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        numFragment.setArguments(bundle);
        return numFragment;
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


        if(!TextUtils.isEmpty(valuePattenAddr) && valuePattenAddr.contains("-")){
            String[] tmp = valuePattenAddr.split("-");
            if(tmp.length == 2) {
                minLengthAddr = Integer.parseInt(tmp[0]);
                maxLengthAddr = Integer.parseInt(tmp[1]);
            }
        }

        if(!TextUtils.isEmpty(valuePattenZip) && valuePattenZip.contains("-")){
            String[] tmp = valuePattenZip.split("-");
            if(tmp.length == 2) {
                minLengthZip = Integer.parseInt(tmp[0]);
                maxLengthZip = Integer.parseInt(tmp[1]);
            }
        }
        zipText = InputType.ALLTEXT.equals(bundle.getString(EntryExtraData.PARAM_EINPUT_TYPE));

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

        EditText editTextAddr = rootView.findViewById(R.id.edit_address);
        if(maxLengthAddr > 0 ) {
            editTextAddr.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthAddr)});
        }

        EditText editTextZip = rootView.findViewById(R.id.edit_zip);
        if(maxLengthZip > 0 ) {
            editTextAddr.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthZip)});
        }

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addr = editTextAddr.getText().toString();
                String zip = editTextZip.getText().toString();

                if(zip.length() < minLengthZip){
                    Toast.makeText(requireContext(), getString(R.string.pls_input_zip_code), Toast.LENGTH_SHORT).show();
                }else {
                    EntryRequestUtils.sendNextAVS(requireContext(),packageName,action,addr,zip);
                }
            }
        });
    }

}
