package com.paxus.poslinkui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.poslinkui.state.IPOSLinkUIHelper;
import com.paxus.poslinkui.state.IPOSLinkUIState;

import java.lang.ref.WeakReference;

public class EntryActivity extends Activity {
    
    private static class MyHandler extends Handler {
        
        private final WeakReference<EntryActivity> ref;
        
        MyHandler(EntryActivity activity) {
            super(Looper.getMainLooper());
            ref = new WeakReference<>(activity);
        }
        
        @Override
        public void handleMessage(@NonNull Message msg) {
            EntryActivity activity = ref.get();
            if (activity == null)
                return;
            int what = msg.what;
            IPOSLinkUIState state = (IPOSLinkUIState) msg.obj;
            boolean stateMatched = activity.lastStateHelper.getState().getActionName().equals(state.getActionName()) &&
                    activity.lastStateHelper.getState().getTargetPackageName().equals(state.getTargetPackageName());
            if (!stateMatched)
                return; //TODO
            switch (what) {
                case 1:
                    sendNext(activity,
                            state.getTargetPackageName(),
                            state.getActionName(),
                            activity.lastStateHelper.packResponse());
                    break;
                case 2:
                    sendAbort(activity,
                            state.getTargetPackageName(),
                            state.getActionName());
                    break;
                case 3:
                    sendTimeout(activity,
                            state.getTargetPackageName(),
                            state.getActionName());
                    break;
                
            }
        }
    }
    
    private IPOSLinkUIHelper lastStateHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        Window window = getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width =1;
        window.setAttributes(params);
        
        MyHandler handler = new MyHandler(this);
        
        POSLinkUIWrapper.getInstance().setHandler(handler);
        
        //TODO register status Broadcast receivers her
        
        performStateChanged(getIntent());
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        performStateChanged(intent);
    }
    
    private void performStateChanged(Intent intent) {
        IPOSLinkUICallback callback = POSLinkUIWrapper.getInstance().getCallback();
        lastStateHelper = StateHelperFactory.createFromIntent(intent);
        lastStateHelper.unpackRequest(intent);
        callback.onStateChanged(lastStateHelper.getState());
    }
    
    private static void sendNext(Context context,
                                 String packageName,
                                 String action,
                                 Bundle bundle) {
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        
        Intent intent = new Intent(EntryRequest.ACTION_NEXT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }
    
    public static void sendTimeout(Context context,
                                   String packageName,
                                   String action) {
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        
        Intent intent = new Intent(EntryRequest.ACTION_TIME_OUT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }
    
    public static void sendAbort(Context context,
                                 String packageName,
                                 String action) {
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        
        Intent intent = new Intent(EntryRequest.ACTION_ABORT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }
    
    private static void sendSecureArea(Context context, String packageName, String action,
                                       int x, int y,
                                       int width, int height,
                                       int fontSize,
                                       String hint,
                                       String fontColor) {
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        bundle.putInt(EntryRequest.PARAM_X, x);
        bundle.putInt(EntryRequest.PARAM_Y, y);
        bundle.putInt(EntryRequest.PARAM_WIDTH, width);
        bundle.putInt(EntryRequest.PARAM_HEIGHT, height);
        bundle.putInt(EntryRequest.PARAM_FONT_SIZE, fontSize);
        bundle.putString(EntryRequest.PARAM_HINT, hint);
        bundle.putString(EntryRequest.PARAM_COLOR, fontColor);
        
        Intent intent = new Intent(EntryRequest.ACTION_SECURITY_AREA);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
        
    }
    
    /**
     * For {@link com.pax.us.pay.ui.constant.entry.SecurityEntry#ACTION_ENTER_PIN} use only
     * For this action, EntryRequest.ACTION_SECURITY_AREA is just used to tell BroadPOS you are ready.
     *
     * @param context     Context
     * @param packageName package name
     * @param action      action name
     */
    private void sendSecureArea(Context context, String packageName, String action) {
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        
        Intent intent = new Intent(EntryRequest.ACTION_SECURITY_AREA);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }
}