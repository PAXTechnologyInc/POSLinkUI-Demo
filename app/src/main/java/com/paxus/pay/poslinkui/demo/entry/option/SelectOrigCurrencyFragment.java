package com.paxus.pay.poslinkui.demo.entry.option;

import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;

/**
 * Implement option entry action {@link OptionEntry#ACTION_SELECT_ORIG_CURRENCY} <br>
 */
public class SelectOrigCurrencyFragment extends AOptionEntryFragment {

    protected long timeout;

    @Override
    protected String formatTitle() {
        return requireContext().getString(R.string.select_orig_currency);
    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_INDEX;
    }
}
