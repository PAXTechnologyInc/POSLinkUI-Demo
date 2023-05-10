package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.enumeration.ConfirmationType;

import java.util.Arrays;
import java.util.List;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_CONFIRM_UNIFIED_MESSAGE}
 * <p>
 * UI Tips:
 * 1.use options {@link ConfirmationType#YES,ConfirmationType#NO}
 * 2.If click YES, sendNext(true)
 * 3.If click NO, sendNext(false)
 * </p>
 */
public class ConfirmUnifiedMessageFragment extends AConfirmationFragment {

    @Override
    protected String formatMessage(String message) {
        return message;
    }

}
