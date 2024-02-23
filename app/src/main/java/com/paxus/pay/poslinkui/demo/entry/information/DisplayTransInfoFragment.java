package com.paxus.pay.poslinkui.demo.entry.information;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.InformationEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;

/**
 * Implement information entry action {@value InformationEntry#ACTION_DISPLAY_TRANS_INFORMATION}
 */
public class DisplayTransInfoFragment extends BaseEntryFragment {
    private long timeOut;

    private String[] keys;
    private String[] values;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_display_trans;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        keys = bundle.getStringArray(EntryExtraData.PARAM_INFORMATION_KEY);
        values = bundle.getStringArray(EntryExtraData.PARAM_INFORMATION_VALUE);
    }

    @Override
    protected void loadView(View rootView) {
        if(keys != null && values != null && keys.length>0){

            StringBuilder transInfoBuilder = new StringBuilder();
            for(int i=0; i<keys.length; i++){
                if(i >= values.length) continue;
                transInfoBuilder.append(keys[i]).append(" ").append(values[i]).append("\n");
            }

            TextView transInfo = rootView.findViewById(R.id.trans_info);
            transInfo.setText(transInfoBuilder.toString());
        }

        //Send Next when clicking confirm button
        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener( v -> onConfirmButtonClicked());
    }

    @Override
    protected void onConfirmButtonClicked(){
        sendNext(null);
    }
}
