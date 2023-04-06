package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.MerchantScope;
import com.paxus.pay.poslinkui.demo.R;

import java.util.Arrays;
import java.util.List;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_CONFIRM_MERCHANT_SCOPE}
 * <p>
 * UI Tips:
 * 1.If click Current, sendNext(true)
 * 2.If click All, sendNext(false)
 * </p>
 */
public class ConfirmMerchantScopeFragment extends AConfirmationDialogFragment {
    private long timeout;
    private String message;
    private List<String> options;

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {
        timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        message = bundle.getString(EntryExtraData.PARAM_MESSAGE);
        String[] array = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
        if (array != null) {
            options = Arrays.asList(array);
        }
    }

    @NonNull
    @Override
    protected String getRequestedParamName() {
        return EntryRequest.PARAM_CONFIRMED;
    }

    @Override
    protected String getPositiveText() {
        if (options != null) {
            if (options.contains(MerchantScope.CURRENT)) {
                return getString(R.string.confirm_current);
            } else {
                return options.get(0);
            }
        }
        return null;
    }

    @Override
    protected String getNegativeText() {
        if (options != null) {
            if (options.contains(MerchantScope.ALL)) {
                return getString(R.string.confirm_all);
            } else if (options.size() >= 1) {
                return options.get(1);
            }
        }
        return null;
    }

    @Override
    protected String formatMessage() {
        return !TextUtils.isEmpty(message) ? message : getString(R.string.confirm_merchant_scope);
    }
}
