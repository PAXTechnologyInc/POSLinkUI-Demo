package com.paxus.pay.poslinkui.demo.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.FragmentActivity;

import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.status.ToastFragment;

import java.util.EnumMap;

public class Toast {

    private static final long DURATION_DEFAULT = 2500;
    public enum TYPE { SUCCESS, FAILURE };
    private static final EnumMap<TYPE, Integer> TYPE_TO_COLOR_MAP = new EnumMap<TYPE, Integer>(TYPE.class) {{
        put(TYPE.SUCCESS, R.color.pastel_accent);
        put(TYPE.FAILURE, R.color.pastel_warning);
    }};

    private FragmentActivity activity;

    public Toast(FragmentActivity activity) {
        this.activity = activity;
    }

    public void show(String message, TYPE type) {
        //validation
        if(message == null || message.isEmpty()) return;
        int colorResID = TYPE_TO_COLOR_MAP.containsKey(type) ? TYPE_TO_COLOR_MAP.get(type) : TYPE_TO_COLOR_MAP.get(TYPE.SUCCESS);

        //create toast fragment
        ToastFragment toastFragment = new ToastFragment().newInstance(message, colorResID);

        //add toast fragment
        activity.getSupportFragmentManager().executePendingTransactions();
        activity.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left)
                .replace(R.id.toast_container, toastFragment).commit();

        //remove toast fragment after defined duration
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .remove(toastFragment).commit();
            } catch (IllegalStateException e) {
                Logger.e(e.getMessage());
            }
        }, DURATION_DEFAULT);
    }
}

