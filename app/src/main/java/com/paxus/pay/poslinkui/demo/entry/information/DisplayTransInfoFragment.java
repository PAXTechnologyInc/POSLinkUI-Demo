package com.paxus.pay.poslinkui.demo.entry.information;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.InformationEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.entry.UIFragmentHelper;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

/**
 * Implement information entry action {@value InformationEntry#ACTION_DISPLAY_TRANS_INFORMATION}
 * <p>
 * UI Tips:
 * If confirm button clicked, sendNext()
 * </p>
 */
public class DisplayTransInfoFragment extends BaseEntryFragment {
    private long timeOut;

    private String[] leftColumns;
    private String[] rightColumn;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_display_trans;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        leftColumns = bundle.getStringArray(EntryExtraData.PARAM_INFORMATION_KEY);
        rightColumn = bundle.getStringArray(EntryExtraData.PARAM_INFORMATION_VALUE);


    }

    @Override
    protected void loadView(View rootView) {


        TextView key = rootView.findViewById(R.id.info_key);
        TextView value = rootView.findViewById(R.id.info_value);

        StringBuilder left = new StringBuilder();
        for (String s : leftColumns) {
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

    @Override
    protected void onConfirmButtonClicked(){
        sendNext(null);
    }
}
