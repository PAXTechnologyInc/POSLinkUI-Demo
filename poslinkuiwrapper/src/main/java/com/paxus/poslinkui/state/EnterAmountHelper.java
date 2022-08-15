package com.paxus.poslinkui.state;

import android.content.Intent;
import android.os.Bundle;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;

/**
 * Created by Kim.L 8/15/22
 */
class EnterAmountHelper extends PageHelper implements IPOSLinkUIHelper {
    
    @Override
    public Bundle packResponse() {
        Bundle bundle = new Bundle();
        bundle.putLong(EntryRequest.PARAM_AMOUNT, ((EnterAmount) state).getAmount());
        return bundle;
    }
    
    @Override
    public void unpackRequest(Intent intent) {
        state = new EnterAmount(intent.getAction(),
                intent.getStringExtra(EntryExtraData.PARAM_PACKAGE),
                intent.getStringExtra(EntryExtraData.PARAM_TRANS_TYPE),
                intent.getStringExtra(EntryExtraData.PARAM_TRANS_MODE),
                intent.getLongExtra(EntryExtraData.PARAM_TIMEOUT, 30000),
                intent.getExtras().getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD),
                intent.getExtras().getString(EntryExtraData.PARAM_VALUE_PATTERN, "1-12"));
    }
    
    @Override
    public IPOSLinkUIState getState() {
        return state;
    }
}
