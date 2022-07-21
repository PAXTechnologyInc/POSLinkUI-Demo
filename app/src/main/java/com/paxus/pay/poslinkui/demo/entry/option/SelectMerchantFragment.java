package com.paxus.pay.poslinkui.demo.entry.option;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

public class SelectMerchantFragment extends AOptionsDialogFragment{
    public static SelectMerchantFragment newInstance(Intent intent){
        SelectMerchantFragment dialogFragment = new SelectMerchantFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected void loadParameter(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        items = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
    }

    @Override
    protected String formatTitle(String action) {
        return requireContext().getString(R.string.select_merchant);
    }

    @Override
    protected void sendNext(int index) {
        EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_INDEX, index);
    }
}
