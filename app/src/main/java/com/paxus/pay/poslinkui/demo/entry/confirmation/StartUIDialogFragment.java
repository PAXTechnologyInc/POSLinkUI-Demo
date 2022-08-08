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
        bundle.putString(EntryRequest.PARAM_ACTION, action);

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_information;
    }

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
    }

    @Override
    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.message);

        //Display status by dialog
        textView.setText(R.string.start_ui);

    }

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    @Override
    protected void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onEntryAccepted() {
//        super.onEntryAccepted();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (active) {
            EntryRequestUtils.sendNext(requireContext(), getSenderPackageName(), getEntryAction());
        }
    }

}
