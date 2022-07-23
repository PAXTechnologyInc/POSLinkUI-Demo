package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.PartialApprovalOption;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_REVERSE_PARTIAL_APPROVAL}
 * <p>
 * UI Tips:
 * 1.If click Reverse, sendNext(true)
 * 2.If click Accept, sendNext(false)
 * </p>
 */
public class ReversePartialApprovalFragment extends AConfirmationDialogFragment {

    private String currency;
    private long approvedAmt;
    private long total;
    private String action;
    private String packageName;
    private long timeout;
    private String message;
    private List<String> options;

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        message = bundle.getString(EntryExtraData.PARAM_MESSAGE);
        String[] array = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
        if (array != null) {
            options = Arrays.asList(array);
        }
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
        approvedAmt = bundle.getLong(EntryExtraData.PARAM_APPROVED_AMOUNT);
        total = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @NonNull
    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_CONFIRMED;
    }

    @Override
    protected String formatMessage() {

        long due = total - approvedAmt;
        return getString(R.string.select_reverse_partial,
                CurrencyUtils.convert(approvedAmt, currency),
                CurrencyUtils.convert(due, currency));
    }

    @Override
    protected String getPositiveText() {
        if (options != null && options.contains(PartialApprovalOption.REVERSE)) {
            return getString(R.string.confirm_option_reverse);
        }
        return null;
    }

    @Override
    protected String getNegativeText() {
        if (options != null && options.contains(PartialApprovalOption.ACCEPT)) {
            return getString(R.string.confirm_option_accept);
        }
        return null;
    }
}
