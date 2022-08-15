package com.paxus.poslinkui;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.TextEntry;
import com.paxus.poslinkui.state.IPOSLinkUIHelper;

import java.lang.reflect.Constructor;

/**
 * Created by Kim.L 8/15/22
 */
public class StateHelperFactory {
    
    @NonNull
    public static IPOSLinkUIHelper createFromIntent(Intent intent) {
        String action = intent.getAction();
        if (action == null || action.isEmpty())
            throw new IllegalArgumentException("It must have action name!");
        if (TextEntry.ACTION_ENTER_AMOUNT.equals(action))
            return getStateObjectFromInnerClass("com.paxus.poslinkui.state.EnterAmountHelper");
        //TODO add more entries
        throw new UnsupportedOperationException("Unknown action");
    }
    
    private static IPOSLinkUIHelper getStateObjectFromInnerClass(String clsName) {
        IPOSLinkUIHelper obj = null;
        try {
            Class c = Class.forName(clsName);
            if (c != null) {
                Constructor constructor = c.getDeclaredConstructor();
                if (constructor != null) {
                    constructor.setAccessible(true);
                    obj = (IPOSLinkUIHelper) constructor.newInstance();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
