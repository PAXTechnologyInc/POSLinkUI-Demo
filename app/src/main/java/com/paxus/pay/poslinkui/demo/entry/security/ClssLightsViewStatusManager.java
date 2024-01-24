package com.paxus.pay.poslinkui.demo.entry.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.ClssLightStatus;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.view.ClssLightsView;

public class ClssLightsViewStatusManager extends BroadcastReceiver implements LifecycleEventObserver {

    Context fragmentContext;
    ClssLightsView clssLightsView;
    boolean isActive = false;

    ClssLightsViewStatusManager(Context context, LifecycleOwner fragmentLifecycleOwner, ClssLightsView clssLightsView) {
        fragmentContext = context;
        fragmentLifecycleOwner.getLifecycle().addObserver(this);
        this.clssLightsView = clssLightsView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(clssLightsView == null) return;
        if(!isActive) return;
        Logger.intent(intent, "STATUS BROADCAST:\t" + intent.getAction());
        clssLightsView.updateStatus(intent.getAction());
    }

    private static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addCategory(ClssLightStatus.CATEGORY);
        intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_COMPLETED);
        intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_NOT_READY);
        intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_ERROR);
        intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_IDLE);
        intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_PROCESSING);
        intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_READY_FOR_TXN);
        intentFilter.addAction(ClssLightStatus.CLSS_LIGHT_REMOVE_CARD);

        intentFilter.addCategory(CardStatus.CATEGORY);
        intentFilter.addAction(CardStatus.CARD_TAP_REQUIRED);

        return intentFilter;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_START:
                fragmentContext.registerReceiver(this, getIntentFilter());
                activate(true);
                break;
            case ON_STOP:
                fragmentContext.unregisterReceiver(this);
                activate(false);
                break;
        }
    }

    public void activate(boolean isActive) {
        this.isActive = isActive;
        clssLightsView.setVisibility(isActive ? View.VISIBLE : View.INVISIBLE);
    }
}
