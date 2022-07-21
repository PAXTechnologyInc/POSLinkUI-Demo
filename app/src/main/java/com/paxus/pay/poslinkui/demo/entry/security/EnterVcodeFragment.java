package com.paxus.pay.poslinkui.demo.entry.security;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.VCodeName;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
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

public class EnterVcodeFragment extends ASecurityFragment{
    public static EnterVcodeFragment newInstance(Intent intent){
        EnterVcodeFragment fragment = new EnterVcodeFragment();
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
        if(SecurityEntry.ACTION_ENTER_VCODE.equals(action)){
            valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN,"3-4");
            message = getString(R.string.pls_input_vcode);
            String vcodeName = bundle.getString(EntryExtraData.PARAM_VCODE_NAME);
            if(!TextUtils.isEmpty(vcodeName)) {
                if (VCodeName.CVV2.equals(vcodeName)) {
                    message = getString(R.string.pls_input_cvv2);
                } else if (VCodeName.CAV2.equals(vcodeName)) {
                    message = getString(R.string.pls_input_cav2);
                } else if (VCodeName.CID.equals(vcodeName)) {
                    message = getString(R.string.pls_input_cid);
                } else {
                    message = vcodeName;
                    Logger.e("unknown vcode name:"+vcodeName);
                }
            }
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
