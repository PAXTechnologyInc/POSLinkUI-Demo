package com.paxus.pay.poslinkui.demo.entry.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.pax.us.pay.ui.constant.entry.enumeration.PinStyles;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.pax.us.pay.ui.constant.status.PINStatus;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.EntryResponseEvent;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

public class PINFragment extends Fragment {
    private String action;
    private String packageName;
    private String transType;
    private long timeOut;
    private String pinStyle;
    private boolean isOnlinePin;
    private String transMode;
    private boolean isUsingExternalPinPad;
    private Long totalAmount;
    private String currencyType;
    private String pinRange;

    private TextView pinBox;

    private BroadcastReceiver receiver;

    public static PINFragment newInstance(Intent intent){
        PINFragment numFragment = new PINFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        numFragment.setArguments(bundle);
        return numFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadArgument(getArguments());

        receiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(PINStatus.CATEGORY);
        intentFilter.addAction(PINStatus.PIN_ENTERING);
        intentFilter.addAction(PINStatus.PIN_ENTER_CLEARED);
        requireContext().registerReceiver(receiver, intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadView(view);
        EventBus.getDefault().register(this);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        requireContext().unregisterReceiver(receiver);
    }

    private void loadArgument(Bundle bundle){
        if(bundle == null){
            return;
        }
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

    private void loadView(View view){
        if(!TextUtils.isEmpty(transType) && getActivity() instanceof AppCompatActivity){
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(transType);
            }
        }

        String mode = null;
        if(!TextUtils.isEmpty(transMode)){
            if(TransMode.DEMO.equals(transMode)){
                mode = getString(R.string.demo_only);
            }else if(TransMode.TEST.equals(transMode)){
                mode = getString(R.string.test_only);
            }else if(TransMode.TEST_AND_DEMO.equals(transMode)){
                mode = getString(R.string.test_and_demo);
            }else {
                mode = "";
            }
        }
        if(!TextUtils.isEmpty(mode)){
            ViewUtils.addWaterMarkView(requireActivity(),mode);
        }else{
            ViewUtils.removeWaterMarkView(requireActivity());
        }

        TextView textView = view.findViewById(R.id.message);
        if(PinStyles.RETRY.equals(pinStyle)){
            textView.setText(getString(R.string.pls_input_pin_again));
        }else if(PinStyles.LAST.equals(pinStyle)){
            textView.setText(getString(R.string.pls_input_pin_last));
        }else {
            textView.setText(getString(isOnlinePin? R.string.prompt_pin: R.string.prompt_offline_pin));
        }

        pinBox = view.findViewById(R.id.edit_pin);

        TextView amountNameView = view.findViewById(R.id.amount_name);
        TextView amountView = view.findViewById(R.id.total_amount);

        if(totalAmount != null){
            amountView.setText(CurrencyUtils.convert(totalAmount, currencyType));
        }else {
            amountNameView.setVisibility(View.GONE);
            amountView.setVisibility(View.GONE);
        }

        boolean couldBypass = pinRange!= null && pinRange.startsWith("0,");
        view.findViewById(R.id.bypass).setVisibility(couldBypass? View.VISIBLE:View.GONE);

        if(isUsingExternalPinPad){
            view.findViewById(R.id.pinpad_layout).setVisibility(View.GONE);
            sendSecureArea();
        }else {
            ViewTreeObserver observer = pinBox.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            pinBox.getViewTreeObserver().removeOnGlobalLayoutListener(this);

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
                                View v = view.findViewById(entry.getValue());
                                int[] location = new int[2];
                                v.getLocationOnScreen(location);
                                bundle.putParcelable(entry.getKey(), new Rect(location[0], location[1], location[0] + v.getWidth(), location[1] + v.getHeight()));
                            }
                            Intent intent = new Intent(EntryRequest.ACTION_SET_PIN_KEY_LAYOUT);
                            intent.setPackage(packageName);
                            intent.putExtras(bundle);
                            requireContext().sendBroadcast(intent);
                            //---------------------Send Security Area------------------
                            sendSecureArea();
                        }
                    });

        }

    }


    private void sendSecureArea(){
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);

        Intent intent = new Intent(EntryRequest.ACTION_SECURITY_AREA);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        requireContext().sendBroadcast(intent);

    }

    private void sendAbort(){
        EntryRequestUtils.sendAbort(requireContext(), packageName, action);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryAbort(EntryAbortEvent event) {
        
        sendAbort();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetEntryResponse(EntryResponseEvent event){
        switch (event.action){
            case EntryResponse.ACTION_ACCEPTED:
                Log.d("POSLinkUI","Entry "+action+" accepted");
                break;
            case EntryResponse.ACTION_DECLINED:{
                Log.d("POSLinkUI","Entry "+action+" declined("+event.code+"-"+event.message+")");
                Toast.makeText(requireActivity(),event.message,Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = pinBox.getText().toString();

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
