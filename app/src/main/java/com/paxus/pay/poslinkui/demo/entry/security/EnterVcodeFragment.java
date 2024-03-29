package com.paxus.pay.poslinkui.demo.entry.security;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.VCodeName;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

/**
 * Implement security entry actions:<br>
 * {@value SecurityEntry#ACTION_ENTER_VCODE}<br>
 * <p>
 *     UI Tips:
 *     1.When input box layout ready, send secure area location (Done on ViewTreeObserver.OnGlobalLayoutListener)
 *     2.When confirm button clicked, sendNext
 *     3.Update confirm button status when received SecurityStatus
 * </p>
 */

public class EnterVcodeFragment extends ASecurityFragment {
    protected long timeOut;
    protected int minLength;
    protected int maxLength;
    private String vcodeName;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        vcodeName = bundle.getString(EntryExtraData.PARAM_VCODE_NAME);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN, "3-4");
        if (!TextUtils.isEmpty(valuePatten)) {
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }
    }

    @Override
    protected String formatMessage() {
        String message = getString(R.string.pls_input_vcode);
        if (!TextUtils.isEmpty(vcodeName)) {
            if (VCodeName.CVV2.equals(vcodeName)) {
                message = getString(R.string.pls_input_cvv2);
            } else if (VCodeName.CAV2.equals(vcodeName)) {
                message = getString(R.string.pls_input_cav2);
            } else if (VCodeName.CID.equals(vcodeName)) {
                message = getString(R.string.pls_input_cid);
            } else {
                message = vcodeName;
                Logger.e("unknown vcode name:" + vcodeName);
            }
        }
        return message;
    }

}
