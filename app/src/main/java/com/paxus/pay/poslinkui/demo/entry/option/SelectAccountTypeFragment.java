package com.paxus.pay.poslinkui.demo.entry.option;

import android.os.Bundle;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.SelectOptionContent;
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView;
import java.util.ArrayList;
import java.util.List;

/**
 * Implement option entry action {@link OptionEntry#ACTION_SELECT_ACCOUNT_TYPE} <br>
 */
public class SelectAccountTypeFragment extends AOptionEntryFragment {
    protected long timeout;

    @Override
    protected String formatTitle() {
        return requireContext().getString(R.string.select_account_type);
    }
    @Override
    protected List<SelectOptionsView.Option> generateOptions(Bundle bundle) {
        List<SelectOptionsView.Option> options = new ArrayList<>();
        String[] tempOptions = bundle.containsKey(EntryExtraData.PARAM_OPTIONS) ? bundle.getStringArray(EntryExtraData.PARAM_OPTIONS) : new String[]{};
        for(int i=0; i<tempOptions.length; i++){
            String text = tempOptions[i];
            Integer defStrId = SelectOptionContent.SELECT_OPTION_MAP.get(text);
            if(defStrId != null && defStrId > 0) {
                text = getResources().getString(defStrId);
            }
            options.add(new SelectOptionsView.Option(i, text, null, i));
        }
        return options;

    }

    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_INDEX;
    }
}
