package com.paxus.poslinkui;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.poslinkui.state.IPOSLinkUIState;

/**
 * Created by Kim.L 8/15/22
 */
public class POSLinkUIWrapper {
    private static final POSLinkUIWrapper instance = new POSLinkUIWrapper();
    
    @Nullable
    private IPOSLinkUICallback callback = null;
    
    private Handler handler;
    
    private POSLinkUIWrapper() {
    }
    
    public static POSLinkUIWrapper getInstance() {
        return instance;
    }
    
    void setHandler(Handler handler) {
        this.handler = handler;
    }
    
    
    public void enableStates(Context context, Class<? extends IPOSLinkUIState>... states) {
        disableAllStates(context);
        
        PackageManager pm = context.getPackageManager();
        //TODO enable component based on states
        ComponentName cn = new ComponentName(context.getPackageName(),
                TextEntry.ACTION_ENTER_AMOUNT);
        
        pm.setComponentEnabledSetting(cn,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    
    public void disableAllStates(Context context) {
        //disable all activity components
        PackageManager pm = context.getPackageManager();
        //TODO loop all components
        ComponentName cn = new ComponentName(context.getPackageName(),
                TextEntry.ACTION_ENTER_AMOUNT);
        
        pm.setComponentEnabledSetting(cn,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    
    public void setCallback(@NonNull IPOSLinkUICallback callback) {
        this.callback = callback;
    }
    
    public void setResult(IPOSLinkUIState state) {
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = state;
        handler.sendMessage(msg);
    }
    
    public void abort(IPOSLinkUIState state) {
        Message msg = Message.obtain();
        msg.what = 2;
        msg.obj = state;
        handler.sendMessage(msg);
    }
    
    public void timeout(IPOSLinkUIState state) {
        Message msg = Message.obtain();
        msg.what = 3;
        msg.obj = state;
        handler.sendMessage(msg);
    }
    
    @NonNull
    public IPOSLinkUICallback getCallback() {
        if (callback == null)
            throw new IllegalStateException("Please call POSLinkUIWrapper#setCallback first");
        return callback;
    }
}
