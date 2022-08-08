package com.paxus.pay.poslinkui.demo.entry.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SecurityEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.PinStyles;
import com.pax.us.pay.ui.constant.status.PINStatus;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Implement security entry action {@link SecurityEntry#ACTION_ENTER_PIN}
 * <p>
 *     UI Tips:
 *     1.If want customized pin pad layout,
 *          (1)when pin pad layout ready, send pin pad and secure area location (Done on ViewTreeObserver.OnGlobalLayoutListener)
 *          (2)When using external pin pad or terminal has physical pin pad, do not use customized pin pad.
 *     2.If don't want customized pin pad, don't need send pin pad location
 *     3.Update input box according PinStatus
 * </p>
 */
public class PINFragment extends BaseEntryFragment {
    private String transType;
    private long timeOut;
    private String pinStyle;
    private boolean isOnlinePin;
    private String transMode;
    private boolean isUsingExternalPinPad;
    private Long totalAmount;
    private String currencyType;
    private String pinRange;
    private String packageName;
    private String action;

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    private View rootView;
    private TextView pinBox;

    private BroadcastReceiver receiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(PINStatus.CATEGORY);
        intentFilter.addAction(PINStatus.PIN_ENTERING);
        intentFilter.addAction(PINStatus.PIN_ENTER_CLEARED);
        requireContext().registerReceiver(receiver, intentFilter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        requireContext().unregisterReceiver(receiver);
    }
    
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_pin;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        if(bundle.containsKey(EntryExtraData.PARAM_TOTAL_AMOUNT)) {
             totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
             currencyType = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
        }
        isUsingExternalPinPad = bundle.getBoolean(EntryExtraData.PARAM_IS_EXTERNAL_PINPAD,false);

        pinStyle = bundle.getString(EntryExtraData.PARAM_PIN_STYLES, PinStyles.NORMAL);
        isOnlinePin = bundle.getBoolean(EntryExtraData.PARAM_IS_ONLINE_PIN, true);
        pinRange = bundle.getString(EntryExtraData.PARAM_PIN_RANGE);
    }

    @Override
    protected void loadView(View rootView) {
        this.rootView = rootView;

        

        

        TextView textView = rootView.findViewById(R.id.message);
        if(PinStyles.RETRY.equals(pinStyle)){
            textView.setText(getString(R.string.pls_input_pin_again));
        }else if(PinStyles.LAST.equals(pinStyle)){
            textView.setText(getString(R.string.pls_input_pin_last));
        }else {
            textView.setText(getString(isOnlinePin? R.string.prompt_pin: R.string.prompt_offline_pin));
        }

        pinBox = rootView.findViewById(R.id.edit_pin);

        TextView amountNameView = rootView.findViewById(R.id.amount_name);
        TextView amountView = rootView.findViewById(R.id.total_amount);

        if(totalAmount != null){
            amountView.setText(CurrencyUtils.convert(totalAmount, currencyType));
        }else {
            amountNameView.setVisibility(View.GONE);
            amountView.setVisibility(View.GONE);
        }

        boolean couldBypass = pinRange!= null && pinRange.startsWith("0,");
        rootView.findViewById(R.id.bypass).setVisibility(couldBypass? View.VISIBLE:View.GONE);

        View customizedPinPad = rootView.findViewById(R.id.pinpad_layout);
        if(isUsingExternalPinPad || hasPhysicalKeyboard()){
            //(2)When using external pin pad or terminal has physical pin pad, do not use customized pin pad.
            customizedPinPad.setVisibility(View.GONE);
            sendSecureArea();
        }else {
            ViewTreeObserver observer = customizedPinPad.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            customizedPinPad.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            onCustomizedPinPadLayoutReady();
                        }
                    });

        }

    }

    //(1)when pin pad layout ready, send pin pad layout and secure area location
    private void onCustomizedPinPadLayoutReady(){
        /*
         * If you don't want customize pin pad, you don't need send pin pad location
         */
        //---------------------Send Pin Pad Location------------------
        Map<String, Integer> padMap = new HashMap<>();
        padMap.put(EntryRequest.PARAM_KEY_0, R.id.key_0);
        padMap.put(EntryRequest.PARAM_KEY_1, R.id.key_1);
        padMap.put(EntryRequest.PARAM_KEY_2, R.id.key_2);
        padMap.put(EntryRequest.PARAM_KEY_3, R.id.key_3);
        padMap.put(EntryRequest.PARAM_KEY_4, R.id.key_4);
        padMap.put(EntryRequest.PARAM_KEY_5, R.id.key_5);
        padMap.put(EntryRequest.PARAM_KEY_6, R.id.key_6);
        padMap.put(EntryRequest.PARAM_KEY_7, R.id.key_7);
        padMap.put(EntryRequest.PARAM_KEY_8, R.id.key_8);
        padMap.put(EntryRequest.PARAM_KEY_9, R.id.key_9);
        padMap.put(EntryRequest.PARAM_KEY_CLEAR, R.id.key_clear);
        padMap.put(EntryRequest.PARAM_KEY_ENTER, R.id.key_enter);
        padMap.put(EntryRequest.PARAM_KEY_CANCEL, R.id.key_cancel);

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        for (Map.Entry<String, Integer> entry : padMap.entrySet()) {
            View v = rootView.findViewById(entry.getValue());
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            String key = entry.getKey();
            Rect value = new Rect(location[0], location[1], location[0] + v.getWidth(), location[1] + v.getHeight());
            bundle.putParcelable(key, value);
            Logger.d("PIN Layout["+key+"]:"+value);
        }
        Logger.i("send Entry Request ACTION_SET_PIN_KEY_LAYOUT for action \""+action+"\"");
        Intent intent = new Intent(EntryRequest.ACTION_SET_PIN_KEY_LAYOUT);
        intent.setPackage(packageName);
        intent.putExtras(bundle);
        requireContext().sendBroadcast(intent);
        //---------------------Send Security Area------------------
        sendSecureArea();
    }

    /**
     * For this action, EntryRequest.ACTION_SECURITY_AREA is just used to tell BroadPOS you are ready.
     */
    private void sendSecureArea(){
        EntryRequestUtils.sendSecureArea(requireContext(),packageName,action);
    }

    public boolean hasPhysicalKeyboard(){
        //For devices which has physical pin pad, do not prompt virtual pin pad
        String buildModel = Build.MODEL;

        return "A80".equals(buildModel)
                || "A30".equals(buildModel)
                || "A35".equals(buildModel)
                || "Aries6".equals(buildModel)
                || "Aries8".equals(buildModel);
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.i("receive Status Action \""+intent.getAction()+"\"");
            String text = pinBox.getText().toString();

            //3.Update input box according PinStatus
            if(PINStatus.PIN_ENTERING.equals(intent.getAction())){
                pinBox.setText(text + "*");
            }else if(PINStatus.PIN_ENTER_CLEARED.equals(intent.getAction())){
                if(text.length() > 0){
                    pinBox.setText(text.substring(0,text.length()-1));
                }
            }
        }
    }


}
