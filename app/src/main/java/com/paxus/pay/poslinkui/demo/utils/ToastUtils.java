package com.paxus.pay.poslinkui.demo.utils;

import android.view.Gravity;
import android.widget.Toast;

/**
 * deng.xiong@pax.us 1/10/2024
 * <p>
 *     ACTION_DECLINED
 * </p>

 */
public class ToastUtils {

    public final static int Y_OFFSET = 100; // Customized

    public static int getScreenWidth() {
        // Could use the screen width/2 if required

        return 0; // Return 0 as default
    }

    public static void adjustToastPosition(Toast toast) {
        toast.setGravity(Gravity.CENTER_VERTICAL, 0,  getScreenWidth() - Y_OFFSET);
        toast.show();
    }

    public static void resetToastPosition(Toast toast) {
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


}

