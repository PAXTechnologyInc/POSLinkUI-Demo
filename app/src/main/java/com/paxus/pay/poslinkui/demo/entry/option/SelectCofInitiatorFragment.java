package com.paxus.pay.poslinkui.demo.entry.option;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;

/**
 * Implement option entry action {@link OptionEntry#ACTION_SELECT_COF_INITIATOR} <br>
 */
public class SelectCofInitiatorFragment extends AOptionEntryFragment {

    protected long timeout;

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_INDEX;
    }

    @Override
    protected String formatTitle() {
        return requireContext().getString(R.string.select_cof_initiator);
    }
}
