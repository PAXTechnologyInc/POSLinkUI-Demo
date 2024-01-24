package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.pax.us.pay.ui.constant.status.POSLinkStatus;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POSLinkStatusManager extends BroadcastReceiver implements LifecycleEventObserver {
    private static final List<String> ALLOWED_STATUS_ACTIONS = new ArrayList<String>(){{
        add(POSLinkStatus.CLEAR_MESSAGE);
    }};

    private Context fragmentContext;
    private Map<String, Runnable> handlerMap = new HashMap<>();

    public POSLinkStatusManager(Context context, LifecycleOwner fragmentLifecycleOwner) {
        fragmentContext = context;
        fragmentLifecycleOwner.getLifecycle().addObserver(this);
    }

    public void registerHandler(String action, Runnable handler) {
        handlerMap.put(action, handler);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.intent(intent, "STATUS BROADCAST:\t" + intent.getAction());
        if (handlerMap.containsKey(intent.getAction())) {
            handlerMap.get(intent.getAction()).run();
        }
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_START:
                fragmentContext.registerReceiver(this, getIntentFilter());
                break;
            case ON_STOP:
                fragmentContext.unregisterReceiver(this);
                break;
        }
    }

    private static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(POSLinkStatus.CATEGORY);
        for (String action : ALLOWED_STATUS_ACTIONS) {
            intentFilter.addAction(action);
        }
        return intentFilter;
    }
}
