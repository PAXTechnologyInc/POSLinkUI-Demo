package com.paxus.pay.poslinkui.demo.entry.security;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.pax.us.pay.ui.constant.status.SecurityStatus;
import com.paxus.pay.poslinkui.demo.entry.broadcast.BroadcastStatusHandler;

import java.util.ArrayList;
import java.util.List;

public class SecureKeyboardStatusHandler extends BroadcastStatusHandler {

    public SecureKeyboardStatusHandler(Context context, LifecycleOwner fragmentLifecycleOwner) {
        super(context, fragmentLifecycleOwner);
    }

    @Override
    protected List<String> listAllowedActions() {
        List<String> allowedActions = new ArrayList<>();
        allowedActions.add(SecurityStatus.READY_FOR_KEYBOARD_LOCATION);
        allowedActions.add(SecurityStatus.SECURE_KEYBOARD_DEACTIVATED);
        return allowedActions;
    }
}
