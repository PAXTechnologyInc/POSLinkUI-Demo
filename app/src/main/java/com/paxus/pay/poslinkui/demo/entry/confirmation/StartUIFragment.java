package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_START_UI}
 *
 * <br>
 * UI Requirement:
 * Display "Processing,Please wait...". Do not close dialog by press KEY_BACK <br>
 */
public class StartUIFragment extends BaseEntryFragment {

    @Override protected int getLayoutResourceId() {
        return R.layout.fragment_start_ui;
    }

    @Override protected void loadArgument(@NonNull Bundle bundle) {}

    @Override protected void loadView(View rootView) {}

    @Override public void onResume() {
        super.onResume();
        sendNext(null);
    }

    @Override protected void executeBackPressEvent() {
        return;
    }
}
