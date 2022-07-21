package com.paxus.pay.poslinkui.demo.entry.security;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

/**
 * Implement security entry actions:<br>
 * {@value SecurityEntry#ACTION_ENTER_CARD_ALL_DIGITS}<br>
 * <p>
 *     UI Tips:
 *     1.When input box layout ready, send secure area location (Done on ViewTreeObserver.OnGlobalLayoutListener)
 *     2.When confirm button clicked, sendNext
 *     3.Update confirm button status when received SecurityStatus
 * </p>
 */

public class EnterCardAllDigitsFragment extends ASecurityFragment{
    public static EnterCardAllDigitsFragment newInstance(Intent intent){
        EnterCardAllDigitsFragment fragment = new EnterCardAllDigitsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        String valuePatten = "";
        if(SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"0-19");
            message = getString(R.string.prompt_input_all_digit);
        }

        if(!TextUtils.isEmpty(valuePatten)){
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }
    }

    @Override
    protected void onConfirmButtonClicked() {
        EntryRequestUtils.sendNext(requireContext(),packageName,action);
    }
}
