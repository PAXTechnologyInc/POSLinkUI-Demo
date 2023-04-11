package com.paxus.pay.poslinkui.demo.entry.security;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.PinStyles;
import com.pax.us.pay.ui.constant.entry.enumeration.VCodeName;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

/**
 * Implement security entry actions:<br>
 * {@value SecurityEntry#ACTION_ENTER_PIN}<br>
 * <p>
 *     UI Tips:
 *     1.When input box layout ready, send secure area location (Done on ViewTreeObserver.OnGlobalLayoutListener)
 *     2.When confirm button clicked, sendNext
 *     3.Update confirm button status when received SecurityStatus
 * </p>
 */

public class EnterPinFragment extends ASecurityFragment {
    protected long timeOut;

    private String currencyType;
    private Long totalAmount;

    private String pinRange;
    private String pinStyle;
    private boolean isOnlinePin;
    private boolean isUsingExternalPinPad;
    String action, packageName;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);

        if(bundle.containsKey(EntryExtraData.PARAM_TOTAL_AMOUNT)) {
            totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
            currencyType = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
        }
        isUsingExternalPinPad = bundle.getBoolean(EntryExtraData.PARAM_IS_EXTERNAL_PINPAD,false);

        pinStyle = bundle.getString(EntryExtraData.PARAM_PIN_STYLES, PinStyles.NORMAL);
        isOnlinePin = bundle.getBoolean(EntryExtraData.PARAM_IS_ONLINE_PIN, true);
        pinRange = bundle.getString(EntryExtraData.PARAM_PIN_RANGE);
    }

    @Override
    protected void loadView(View rootView) {
        super.loadView(rootView);
        if(totalAmount != null){
            ((TextView)rootView.findViewById(R.id.fragment_security_info_key)).setText(R.string.total_amount);
            ((TextView)rootView.findViewById(R.id.fragment_security_info_value)).setText(CurrencyUtils.convert(totalAmount, currencyType));
            rootView.findViewById(R.id.fragment_security_info_container).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    @Override
    protected String formatMessage() {
        String message = "";

        if(PinStyles.RETRY.equals(pinStyle)){
            message = getString(R.string.pls_input_pin_again);
        }else if(PinStyles.LAST.equals(pinStyle)){
            message = getString(R.string.pls_input_pin_last);
        }else {
            message = getString(isOnlinePin? R.string.prompt_pin: R.string.prompt_offline_pin);
        }

        boolean isBypassAllowed = pinRange!= null && pinRange.startsWith("0,");
        if(isBypassAllowed) message += "\n" + getString(R.string.prompt_no_pin);

        return message;
    }

}
