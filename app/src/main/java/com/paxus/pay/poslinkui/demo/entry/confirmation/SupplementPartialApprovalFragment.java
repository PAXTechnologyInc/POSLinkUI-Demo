package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_SUPPLEMENT_PARTIAL_APPROVAL}
 * <p>
 * UI Tips:
 * 1.If click Accept, sendNext(true)
 * 2.If click Decline, sendNext(false)
 * </p>
 */
public class SupplementPartialApprovalFragment extends AConfirmationFragment {
    private String currency;
    private long approvedAmt;
    private long total;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
        approvedAmt = bundle.getLong(EntryExtraData.PARAM_APPROVED_AMOUNT);
        total = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
    }

    @Override
    protected String formatMessage(String message) {
        long due = total - approvedAmt;
        return getString(R.string.select_supplement_partial,
                CurrencyUtils.convert(approvedAmt, currency),
                CurrencyUtils.convert(due, currency));
    }

}
