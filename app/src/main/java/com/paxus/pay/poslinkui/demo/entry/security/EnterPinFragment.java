package com.paxus.pay.poslinkui.demo.entry.security;

import android.os.Bundle;

import androidx.annotation.NonNull;

public class EnterPinFragment extends ASecurityFragment {

    private String senderPackage, action;
    private Bundle arguments;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        arguments = new Bundle(bundle);
    }

    @Override
    protected String getSenderPackageName() {
        return senderPackage;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    @Override
    protected String formatMessage() {
        String message =
        return null;
    }

}
