package com.paxus.pay.poslinkui.demo.entry.security.keyboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class SecureKeyboard extends Fragment {
    protected abstract int getLayoutResourceId();
    protected Runnable initializationCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        view.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        return view;
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            try {
                if (getView() != null) getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if(initializationCallback != null) initializationCallback.run();
            } catch (Exception e) {
                Logger.e(e);
            }
        }
    };

    public void setInitializationCallback(Runnable initializationCallback) {
        this.initializationCallback = initializationCallback;
    }

    public Bundle toBundle() {
        List<Key> keys = getKeys(new ArrayList<>(), getView());

        JSONArray jsonArray = new JSONArray();
        for(Key key : keys){
            JSONObject jsonObject = new JSONObject();
            try {
                int[] location = new int[2];
                key.getLocationOnScreen(location);

                jsonObject.put(EntryRequest.PARAM_X, location[0]);
                jsonObject.put(EntryRequest.PARAM_Y, location[1] - ViewUtils.getBarHeight(getActivity()));
                jsonObject.put(EntryRequest.PARAM_WIDTH, key.getWidth());
                jsonObject.put(EntryRequest.PARAM_HEIGHT, key.getHeight());
                jsonObject.put(EntryRequest.PARAM_KEY_CODE, key.getCode());
                jsonObject.put(EntryRequest.PARAM_SECURE_KEYBOARD_PAYLOAD, key.getPayload());

            }catch (Exception e){
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_KEY_LOCATIONS, jsonArray.toString());
        return bundle;
    }
    protected List<Key> getKeys(List<Key> keys, View view) {
        if(view instanceof Key && ((Key)view).getCode() != null){
            keys.add((Key) view);
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

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Bundle locations = toBundle();
        for(String key : locations.keySet()){
            stringBuilder.append(key).append(": ").append(locations.get(key)).append("\n");
        }
        return stringBuilder.toString();
    }

}
