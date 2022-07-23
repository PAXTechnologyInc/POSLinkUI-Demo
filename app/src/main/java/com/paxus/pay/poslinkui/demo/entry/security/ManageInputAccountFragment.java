package com.paxus.pay.poslinkui.demo.entry.security;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils;

/**
 * Implement security entry actions {@link SecurityEntry#ACTION_MANAGE_INPUT_ACCOUNT}
 * <p>
 *     UI Tips:
 *     1.When input box layout ready, send secure area location (Done on ViewTreeObserver.OnGlobalLayoutListener)
 *     2.When confirm button clicked, sendNext
 *     3.Update contactless light when received ClssLightStatus
 *     4.Update amount when received InformationStatus.TRANS_AMOUNT_CHANGED_IN_CARD_PROCESSING
 *     5.Update confirm button status when received SecurityStatus
 *     6.Update entry mode when received CardStatus.CARD_INSERT_REQUIRED, CardStatus.CARD_TAP_REQUIRED,CardStatus.CARD_SWIPE_REQUIRED:
 * </p>
 */

public class ManageInputAccountFragment extends AInputAccountFragment{

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        enableInsert = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_INSERT);
        enableTap = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_TAP);
        enableSwipe = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_SWIPE);
        enableManual = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_MANUAL);

        supportApplePay = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_APPLEPAY);
        supportGooglePay = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_GOOGLEPAY);
        supportSamsungPay = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_SAMSUNGPAY);
        supportNFC = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_NFCPAY);

        enableContactlessLight = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_CONTACTLESS_LIGHT);

        String valuePatten = bundle.getString(EntryExtraData.PARAM_VALUE_PATTERN, "0-19");
            manualMessage = getString(R.string.hint_enter_account);
            amountMessage = bundle.getString(EntryExtraData.PARAM_AMOUNT_MESSAGE);

        if(!TextUtils.isEmpty(valuePatten)){
            minLength = ValuePatternUtils.getMinLength(valuePatten);
            maxLength = ValuePatternUtils.getMaxLength(valuePatten);
        }

        merchantName = bundle.getString(EntryExtraData.PARAM_MERCHANT_NAME);
        if(bundle.containsKey(EntryExtraData.PARAM_TOTAL_AMOUNT)){
            totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
            currencyType = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
        }
    }

    @Override
    protected void onConfirmButtonClicked() {
        EntryRequestUtils.sendNext(requireContext(),packageName,action);
    }
}
