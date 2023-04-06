package com.paxus.pay.poslinkui.demo.entry.option;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;

/**
 * Implement option entry action {@link OptionEntry#ACTION_SELECT_TRANS_TYPE} <br>
 * <p>
 * UI Tips:
 * 1.If confirm button clicked, sendNext(index)
 * 2.index start from 0
 * </p>
 */
public class SelectTransTypeFragment extends AOptionsDialogFragment {

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
        return requireContext().getString(R.string.select_trans_type);
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_INDEX;
    }
}
