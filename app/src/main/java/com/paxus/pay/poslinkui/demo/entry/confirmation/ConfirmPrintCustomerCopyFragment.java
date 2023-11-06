package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.paxus.pay.poslinkui.demo.R;

import java.util.Arrays;
import java.util.List;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_CONFIRM_PRINT_CUSTOMER_COPY}
 * <p>
 * UI Tips:
 * 1.If click YES, sendNext(true)
 * 2.If click NO, sendNext(false)
 * </p>
 */
public class ConfirmPrintCustomerCopyFragment extends AConfirmationFragment {

    @Override
    protected String formatMessage(String message) {
        return !TextUtils.isEmpty(message) ? message : getString(R.string.confirm_print_customer_copy);
    }

}
