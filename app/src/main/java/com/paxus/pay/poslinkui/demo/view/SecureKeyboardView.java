package com.paxus.pay.poslinkui.demo.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecureKeyboardView extends LinearLayout {
    public static final String INPUT_TYPE_NUMBER = "1";
    public static final String INPUT_TYPE_ALPHA = "2";
    public static final String INPUT_TYPE_ALPHANUMERIC = "3";

    private static Map<String, Integer> InputTypeToLayoutMap = new HashMap<String, Integer>(){{
        put(INPUT_TYPE_NUMBER, R.layout.secure_keyboard_view_numeric);
        put(INPUT_TYPE_ALPHA, R.layout.secure_keyboard_view_alpha);
        put(INPUT_TYPE_ALPHANUMERIC, R.layout.secure_keyboard_view_alphanumeric);
    }};

    public SecureKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs, defStyle, 0);
    }

    public SecureKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0, 0);
    }

    public SecureKeyboardView(Context context) {
        super(context);
        initialize(context, null, 0, 0);
    }

    public void initialize(Context context, AttributeSet attrs, int defStyle, int defStyleRes){
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SecureKeyboardView, defStyle, 0);
        String inputType = attributes.getString(R.styleable.SecureKeyboardView_secureKeyboardInputType);
        attributes.recycle();

        View view = inflate(context, getLayoutResourceId(inputType), this);
    }

    private @LayoutRes int getLayoutResourceId(String inputType) {
        return InputTypeToLayoutMap.containsKey(inputType) ? InputTypeToLayoutMap.get(inputType) :InputTypeToLayoutMap.get(INPUT_TYPE_NUMBER);
    }

    private List<View> getKeys(List<View> keys, View view) {
        if(view instanceof TextView && view.getTag() != null){
            keys.add(view);
            return keys;
        }
        else if(view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view;
            for(int i = 0; i < viewGroup.getChildCount(); i++){
                getKeys(keys, viewGroup.getChildAt(i));
            }
        }
        return keys;
    }


    public Bundle generateLocationBundle(Activity activity) {
        List<View> keys = getKeys(new ArrayList<>(), this);

        JSONArray jsonArray = new JSONArray();
        for(View key : keys){
            JSONObject jsonObject = new JSONObject();
            try {
                int[] location = new int[2];
                key.getLocationOnScreen(location);

                jsonObject.put(EntryRequest.PARAM_X, location[0]);
                jsonObject.put(EntryRequest.PARAM_Y, location[1] - ViewUtils.getBarHeight(activity));
                jsonObject.put(EntryRequest.PARAM_WIDTH, key.getWidth());
                jsonObject.put(EntryRequest.PARAM_HEIGHT, key.getHeight());
                jsonObject.put(EntryRequest.PARAM_KEY_CODE, key.getTag());

            }catch (Exception e){
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_KEY_LOCATIONS, jsonArray.toString());
        return bundle;
    }
}
