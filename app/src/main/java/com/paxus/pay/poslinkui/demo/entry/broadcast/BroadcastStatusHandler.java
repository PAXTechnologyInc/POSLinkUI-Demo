package com.paxus.pay.poslinkui.demo.entry.broadcast;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BroadcastStatusHandler extends BroadcastReceiver implements LifecycleEventObserver {
    private final List<String> ALLOWED_STATUS_ACTIONS;

    private Context fragmentContext;
    private Map<String, BroadcastHandler> handlerMap = new HashMap<>();

    public BroadcastStatusHandler(Context context, LifecycleOwner fragmentLifecycleOwner) {
        fragmentContext = context;
        fragmentLifecycleOwner.getLifecycle().addObserver(this);
        ALLOWED_STATUS_ACTIONS = listAllowedActions();
    }

    protected abstract List<String> listAllowedActions();

    public @FunctionalInterface interface BroadcastHandler {
        void handle(Intent intent);
    }

    public void registerHandler(String action, BroadcastHandler handler) {
        handlerMap.put(action, handler);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(getClass().getSimpleName() + " Receiving Status Broadcast " + intent.getAction());
        Logger.intent(intent);
        if (handlerMap.containsKey(intent.getAction())) {
            handlerMap.get(intent.getAction()).handle(intent);
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

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(POSLinkStatus.CATEGORY);
        for (String action : ALLOWED_STATUS_ACTIONS) {
            intentFilter.addAction(action);
        }
        return intentFilter;
    }
}
