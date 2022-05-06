package com.paxus.pay.poslinkui.demo.entry.signature;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.EntryResponseEvent;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class SignatureFragment extends Fragment {
    private String action;
    private String packageName;
    private String transType;
    private long timeOut;
    private String transMode;
    private long totalAmount;
    private String currency;

    private String signLine1;
    private String signLine2;
    private boolean enableCancel;
    private Button confirmBtn;
    private ElectronicSignatureView mSignatureView;
    private TextView timeoutView;
    private long tickTimeout;
    private Handler handler = new Handler();
    private Runnable tick = new Runnable() {
        @Override
        public void run() {
            tickTimeout = tickTimeout - 1000;
            long tick = tickTimeout/1000;
            if(timeoutView != null){
                timeoutView.setText(String.valueOf(tick));
            }
            if(tick == 0){
                sendTimeout();
            }else{
                handler.postDelayed(this,1000);
            }
        }
    };

    public static SignatureFragment newInstance(Intent intent){
        SignatureFragment numFragment = new SignatureFragment();
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signature, container, false);
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

        EventBus.getDefault().unregister(this);
        handler.removeCallbacks(tick);
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

        signLine1 = bundle.getString(EntryExtraData.PARAM_SIGNLINE1);
        signLine2 = bundle.getString(EntryExtraData.PARAM_SIGNLINE2);
        enableCancel = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_CANCEL);
        totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);

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

        Button cancelBtn = view.findViewById(R.id.cancel_button);
        if(enableCancel){
            cancelBtn.setOnClickListener(view1->onCancelClick());
        }else {
            cancelBtn.setVisibility(View.GONE);
        }

        Button clearBtn = view.findViewById(R.id.clear_button);
        clearBtn.setOnClickListener(view1->onClearClick());
        confirmBtn = view.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(view1 -> onConfirmClick());

        TextView line1 = view.findViewById(R.id.sign_line1);
        if(!TextUtils.isEmpty(signLine1)){
            line1.setText(signLine1);
        }
        TextView line2 = view.findViewById(R.id.sign_line2);
        if(!TextUtils.isEmpty(signLine2)){
            line2.setText(signLine2);
        }

        TextView total = view.findViewById(R.id.total_amount);
        total.setText(CurrencyUtils.convert(totalAmount,currency));

        mSignatureView = view.findViewById(R.id.signature_board);
        mSignatureView.setBitmap(new Rect(0, 0, 384, 128), 0, Color.WHITE);
        mSignatureView.setOnKeyListener((view12, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP){
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    onConfirmClick();
                    return true;
                } else if(keyCode == KeyEvent.KEYCODE_DEL){
                    onClearClick();
                    return true;
                } else if(keyCode == KeyEvent.KEYCODE_BACK){
                    onCancelClick();
                    return true;
                }
            }

            return false;
        });
        timeoutView = view.findViewById(R.id.timeout);
        tickTimeout = timeOut;
        timeoutView.setText(String.valueOf(tickTimeout/1000));
        handler.postDelayed(tick,1000);
    }

    private void onCancelClick(){
        sendAbort();
    }
    private void onClearClick(){
        mSignatureView.clear();
        tickTimeout = timeOut;
    }

    private void onConfirmClick(){
        if (!mSignatureView.getTouched()) {
            return;
        }

        try {
            confirmBtn.setClickable(false);
            List<float[]> pathPos = mSignatureView.getPathPos();
            int len = 0;
            for (float[] ba : pathPos) {
                len += ba.length;
            }
            short[] total = new short[len];
            int index = 0;
            for (float[] ba : pathPos) {
                for (float b : ba) {
                    total[index++] = (short) b;
                }
            }

            sendNext(total);
        } finally {
            confirmBtn.setClickable(true);
        }
    }

    private void sendNext(short[] signature){
        handler.removeCallbacks(tick); //Stop Tick

        EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_SIGNATURE, signature);
    }

    private void sendAbort(){
        handler.removeCallbacks(tick); //Stop Tick

        EntryRequestUtils.sendAbort(requireContext(), packageName, action);
    }

    private void sendTimeout(){
        EntryRequestUtils.sendTimeout(requireContext(), packageName, action);
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



}
