package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryDialogFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_START_UI}
 *
 * <br>
 * UI Requirement:
 * Display "Processing,Please wait...". Do not close dialog by press KEY_BACK <br>
 */
public class StartUIDialogFragment extends BaseEntryDialogFragment {

    private String action;
    private String packageName;

    public static DialogFragment newInstance(String action) {
        StartUIDialogFragment dialogFragment = new StartUIDialogFragment();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_information;
    }

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {}

    @Override
    protected void loadView(View rootView) {
        rootView.findViewById(R.id.message).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isActive) {
            sendNext(null);
        }
    }

}
