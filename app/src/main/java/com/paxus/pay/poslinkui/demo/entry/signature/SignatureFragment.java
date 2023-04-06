package com.paxus.pay.poslinkui.demo.entry.signature;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SignatureEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.entry.UIFragmentHelper;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

import java.util.List;

/**
 * Implement signature entry action {@value SignatureEntry#ACTION_SIGNATURE}<br>
 * <p>
 *     UI Tips:
 *     1.When cancel button clicked, sendAbort
 *     2.When clear button clicked, clear signature board and reset timeout.
 *     3.When confirm button clicked, if signature board has been touched, sendNext
 *     4.If timeout, sendTimeout
 * </p>
 */
public class SignatureFragment extends BaseEntryFragment {
    private long timeOut;
    private long totalAmount;
    private String currency;

    private String signLine1;
    private String signLine2;
    private boolean enableCancel;
    private Button confirmBtn;
    private ElectronicSignatureView mSignatureView;
    private TextView timeoutView;
    private long tickTimeout;
    private final Handler handler = new Handler();
    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            tickTimeout = tickTimeout - 1000;
            long tick = tickTimeout/1000;
            if(timeoutView != null){
                timeoutView.setText(String.valueOf(tick));
            }
            if(tick == 0){
                //4.If timeout, sendTimeout
                sendTimeout();
            }else{
                handler.postDelayed(this,1000);
            }
        }
    };

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_signature;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle){
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        signLine1 = bundle.getString(EntryExtraData.PARAM_SIGNLINE1);
        signLine2 = bundle.getString(EntryExtraData.PARAM_SIGNLINE2);
        enableCancel = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_CANCEL);
        totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);

    }

    @Override
    protected void loadView(View rootView) {
        Button cancelBtn = rootView.findViewById(R.id.cancel_button);
        if(enableCancel){
            cancelBtn.setOnClickListener(view1-> onCancelButtonClicked());
        }else {
            cancelBtn.setVisibility(View.GONE);
        }

        Button clearBtn = rootView.findViewById(R.id.clear_button);
        clearBtn.setOnClickListener(view1-> onClearButtonClicked());
        confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(view1 -> onConfirmButtonClicked());

        TextView line1 = rootView.findViewById(R.id.sign_line1);
        if(!TextUtils.isEmpty(signLine1)){
            line1.setText(signLine1);
        }
        TextView line2 = rootView.findViewById(R.id.sign_line2);
        if(!TextUtils.isEmpty(signLine2)){
            line2.setText(signLine2);
        }

        TextView total = rootView.findViewById(R.id.total_amount);
        total.setText(CurrencyUtils.convert(totalAmount,currency));

        mSignatureView = rootView.findViewById(R.id.signature_board);
        mSignatureView.setBitmap(new Rect(0, 0, 384, 128), 0, Color.WHITE);
        mSignatureView.setOnKeyListener((view12, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP){
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    onConfirmButtonClicked();
                    return true;
                } else if(keyCode == KeyEvent.KEYCODE_DEL){
                    onClearButtonClicked();
                    return true;
                } else if(keyCode == KeyEvent.KEYCODE_BACK){
                    onCancelButtonClicked();
                    return true;
                }
            }

            return false;
        });
        timeoutView = rootView.findViewById(R.id.timeout);
        tickTimeout = timeOut;
        timeoutView.setText(String.valueOf(tickTimeout/1000));
        handler.postDelayed(tick,1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //1.When cancel button clicked, sendAbort
    private void onCancelButtonClicked(){
        sendAbort();
    }

    //2.When clear button clicked, clear signature board and reset timeout.
    private void onClearButtonClicked(){
        mSignatureView.clear();
        tickTimeout = timeOut;
    }

    @Override
    protected void onConfirmButtonClicked(){
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
        Bundle bundle = new Bundle();
        bundle.putShortArray(EntryRequest.PARAM_SIGNATURE, signature);
        sendNext(bundle);
    }

    @Override
    protected void sendAbort() {
        super.sendAbort();
        handler.removeCallbacks(tick); //Stop Tick
    }

}
