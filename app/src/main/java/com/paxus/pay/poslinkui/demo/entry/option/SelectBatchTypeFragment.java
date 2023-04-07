package com.paxus.pay.poslinkui.demo.entry.option;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;

/**
 * Implement option entry action {@link OptionEntry#ACTION_SELECT_BATCH_TYPE} <br>
 */
public class SelectBatchTypeFragment extends AOptionsDialogFragment {

    protected long timeout;
    private String[] items;

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {
        items = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
    }

    @NonNull
    @Override
    protected String[] getOptions() {
        return items;
    }

    @Override
    protected String formatTitle() {
        return requireContext().getString(R.string.select_batch_menu);
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_INDEX;
    }
}
