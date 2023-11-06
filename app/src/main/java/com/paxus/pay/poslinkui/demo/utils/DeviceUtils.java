package com.paxus.pay.poslinkui.demo.utils;

import android.os.Build;

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
}
