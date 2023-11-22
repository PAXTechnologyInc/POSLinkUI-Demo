package com.paxus.pay.poslinkui.demo.utils;

import android.os.Build;

import java.util.Arrays;
import java.util.List;

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

    public static long brodcastProcessDelay(){
        // In some time-critical cases, there are no acknowledgement from BroadPOS.
        // So we need to delay the next request in slow devices to make sure that BraodPOS is ready.
        String buildModel = Build.MODEL;
        List<String> slowDevices = Arrays.asList("A35");

        return slowDevices.contains(buildModel) ? 100 : 0;
    }
}
