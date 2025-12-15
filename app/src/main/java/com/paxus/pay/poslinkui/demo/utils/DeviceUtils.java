package com.paxus.pay.poslinkui.demo.utils;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.view.Display;

public class DeviceUtils {
    public static boolean hasPhysicalKeyboard(){
        //For devices which has physical pin pad, do not prompt virtual pin pad
        String buildModel = Build.MODEL;

        return "A80".equals(buildModel)
                || "A30".equals(buildModel)
                || "A35".equals(buildModel)
                || "Aries6".equals(buildModel)
                || "Aries8".equals(buildModel);
    }

    public static Display getSecondDisplay(Context context) {
        Display[] displays = ((DisplayManager) context.getSystemService(context.DISPLAY_SERVICE)).getDisplays();
        if (displays.length>1) {
            return displays[1];
        }
        return null;
    }
}
