package com.paxus.pay.poslinkui.demo.entry.option;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;

/**
 * Implement option entry action {@link OptionEntry#ACTION_SELECT_DUPLICATE_OVERRIDE} <br>
 * <p>
 * UI Tips:
 * 1.If confirm button clicked, sendNext(index)
 * 2.index start from 0
 * </p>
 */
public class SelectDuplicateOverrideFragment extends AOptionsDialogFragment {

    protected long timeout;
    protected String message;
    private String action;
    private String packageName;
    private String[] items;

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        items = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
    }

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    @NonNull
    @Override
    protected String[] getOptions() {
        return items;
    }

    @Override
    protected String formatTitle() {
        return requireContext().getString(R.string.select_duplicate_override);
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_INDEX;
    }
}
