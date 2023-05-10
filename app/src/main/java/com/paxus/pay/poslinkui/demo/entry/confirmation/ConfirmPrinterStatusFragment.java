package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.enumeration.PrintStatusType;
import com.paxus.pay.poslinkui.demo.R;

import java.util.Arrays;
import java.util.List;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_CONFIRM_PRINTER_STATUS}
 * <p>
 * UI Tips:
 * 1.If click YES, sendNext(true)
 * 2.If click NO, sendNext(false)
 * </p>
 */
public class ConfirmPrinterStatusFragment extends AConfirmationFragment {
    private String printStatus;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        printStatus = bundle.getString(EntryExtraData.PARAM_PRINT_STATUS);
    }

    @Override
    protected String formatMessage(String message) {
        if (PrintStatusType.PRINTER_OUT_OF_PAPER.equals(printStatus)) {
            return getString(R.string.prompt_printer_out_of_paper);
        } else if (PrintStatusType.PRINTER_HOT.equals(printStatus)) {
            return getString(R.string.confirm_printer_over_hot);
        } else if (PrintStatusType.PRINTER_VOLTAGE_TOO_LOW.equals(printStatus)) {
            return getString(R.string.confirm_printer_voltage_low);
        } else {
            return getString(R.string.confirm_printer_status);
        }
    }

}
