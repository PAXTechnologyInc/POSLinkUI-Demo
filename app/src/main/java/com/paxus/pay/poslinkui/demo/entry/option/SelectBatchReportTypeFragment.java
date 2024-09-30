package com.paxus.pay.poslinkui.demo.entry.option;

import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;

/**
 * Implement option entry action {@link OptionEntry#ACTION_SELECT_BATCH_REPORT_TYPE} <br>
 */
public class SelectBatchReportTypeFragment extends AOptionEntryFragment{

    @Override
    protected String formatTitle() {
        return getString(R.string.select_batch_report_type);
    }
}
