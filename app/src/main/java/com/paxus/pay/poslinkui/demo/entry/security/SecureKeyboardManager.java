package com.paxus.pay.poslinkui.demo.entry.security;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.entry.broadcast.BroadcastStatusHandler;
import com.paxus.pay.poslinkui.demo.entry.security.keyboard.LowercaseKeyboard;
import com.paxus.pay.poslinkui.demo.entry.security.keyboard.NumericKeyboard;
import com.paxus.pay.poslinkui.demo.entry.security.keyboard.SecureKeyboard;
import com.paxus.pay.poslinkui.demo.entry.security.keyboard.SymbolKeyboard;
import com.paxus.pay.poslinkui.demo.entry.security.keyboard.UppercaseKeyboard;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.util.HashMap;
import java.util.Map;

public class SecureKeyboardManager {
    public static final String HIDDEN_KEYBOARD = "0";
    public static final String INPUT_TYPE_NUMBER = "1";
    public static final String INPUT_TYPE_ALPHANUMERIC = "2";
    private static final String INPUT_TYPE_ALPHANUMERIC_UPPERCASE = "3";
    private static final String INPUT_TYPE_ALPHANUMERIC_SYMBOL = "4";
    public static Map<String, Class<? extends SecureKeyboard>> InputTypeToFragmentMap = new HashMap<String, Class<? extends SecureKeyboard>>(){{
        put(INPUT_TYPE_NUMBER, NumericKeyboard.class);
        put(INPUT_TYPE_ALPHANUMERIC, LowercaseKeyboard.class);
        put(INPUT_TYPE_ALPHANUMERIC_UPPERCASE, UppercaseKeyboard.class);
        put(INPUT_TYPE_ALPHANUMERIC_SYMBOL, SymbolKeyboard.class);
    }};

    private SecureKeyboardStatusHandler secureKeyboardStatusHandler;
    private Fragment parent;
    private int keyboardContainerResId;

    SecureKeyboardManager(BaseEntryFragment fragment, int keyboardContainerResId) {
        secureKeyboardStatusHandler = new SecureKeyboardStatusHandler(fragment.getContext(), fragment.getViewLifecycleOwner());
        parent = fragment;
        this.keyboardContainerResId = keyboardContainerResId;
    }

    public void registerBroadcastHandler(String action, BroadcastStatusHandler.BroadcastHandler handler) {
        secureKeyboardStatusHandler.registerHandler(action, handler);
    }

    SecureKeyboard getKeyboardFragment(String inputType, Runnable callback) {
        Class keyboardClass = InputTypeToFragmentMap.get(inputType);

        SecureKeyboard secureKeyboard = null;
        try {
            secureKeyboard = (SecureKeyboard) keyboardClass.getConstructor().newInstance();
            Bundle bundle = new Bundle();
            secureKeyboard.setArguments(bundle);
            secureKeyboard.setInitializationCallback(callback);
            return secureKeyboard;
        } catch (Exception e) {
            Logger.e(e);
        }
        return secureKeyboard;
    }

    public Bundle getKeyboardLocation() {
        SecureKeyboard secureKeyboard = (SecureKeyboard) parent.getActivity().getSupportFragmentManager().findFragmentById(keyboardContainerResId);
        return secureKeyboard != null ? secureKeyboard.toBundle() : null;
    }

    public void processPayload(String payload, Runnable callback) {
        if(payload.equals(HIDDEN_KEYBOARD)){
            minimizeKeyboard();
            return;
        }
        showKeyboard(payload, callback);
    }
    public void showKeyboard(String inputType, Runnable callback) {
        Logger.d("Setting up secure keyboard fragment");
        parent.getActivity().findViewById(R.id.secure_keyboard_container).setVisibility(View.VISIBLE);
        parent.getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_up_from_bottom, R.anim.anim_slide_down_from_origin, R.anim.anim_slide_up_from_bottom, R.anim.anim_slide_down_from_origin)
                .replace(R.id.secure_keyboard_container, getKeyboardFragment(inputType, callback))
                .commit();
    }

    public void minimizeKeyboard(){
        parent.getActivity().getSupportFragmentManager().beginTransaction()
                .remove(parent.getActivity().getSupportFragmentManager().findFragmentById(keyboardContainerResId))
                .commit();
        parent.getActivity().findViewById(R.id.secure_keyboard_container).setVisibility(View.GONE);
    }
}
