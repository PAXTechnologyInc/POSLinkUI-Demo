package com.paxus.pay.poslinkui.demo.entry.information;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.InformationEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

/**
 * Implement information entry action {@value InformationEntry#ACTION_DISPLAY_TRANS_INFORMATION}
 * <p>
 *     UI Tips:
 *     If confirm button clicked, sendNext()
 * </p>
 */
public class DisplayTransInfoFragment extends BaseEntryFragment {
    private String transType;
    private long timeOut;
    private String transMode;

    private String[] leftColumns;
    private String[] rightColumn;

    public static DisplayTransInfoFragment newInstance(Intent intent){
        DisplayTransInfoFragment numFragment = new DisplayTransInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        numFragment.setArguments(bundle);
        return numFragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_display_trans;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        leftColumns = bundle.getStringArray(EntryExtraData.PARAM_INFORMATION_KEY);
        rightColumn = bundle.getStringArray(EntryExtraData.PARAM_INFORMATION_VALUE);


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

        TextView key = rootView.findViewById(R.id.info_key);
        TextView value = rootView.findViewById(R.id.info_value);

        StringBuilder left = new StringBuilder();
        for(String s: leftColumns){
            left.append(s).append("\n");
        }

        StringBuilder right = new StringBuilder();
        for(String s: rightColumn){
            right.append(s).append("\n");
        }

        key.setText(left.toString());
        value.setText(right.toString());

        //Send Next when clicking confirm button
        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener( v -> onConfirmButtonClicked());
    }

    private void onConfirmButtonClicked(){
        sendNext();
    }

    private void sendNext(){
        EntryRequestUtils.sendNext(requireContext(), packageName, action);
    }

}
