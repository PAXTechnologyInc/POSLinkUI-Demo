package com.paxus.pay.poslinkui.demo.entry.signature;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.SignatureEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
    private long tempTimeout;
    private final long intervalMilis = 1000;

    ScheduledExecutorService countdownUpdateScheduler;
    ScheduledFuture<?> countdownFuture;
    Runnable updateCountdown = () -> {
        if(timeoutView != null) timeoutView.setText(String.valueOf(tempTimeout/intervalMilis));
        if(tempTimeout<=0) countdownFuture.cancel(true);
        tempTimeout -= intervalMilis;
    };

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_signature;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle){
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        tempTimeout = timeOut;

        signLine1 = bundle.getString(EntryExtraData.PARAM_SIGNLINE1);
        signLine2 = bundle.getString(EntryExtraData.PARAM_SIGNLINE2);
        enableCancel = bundle.getBoolean(EntryExtraData.PARAM_ENABLE_CANCEL);
        totalAmount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
        currency = bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD);
        countdownUpdateScheduler = Executors.newSingleThreadScheduledExecutor();
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
        getParentFragmentManager().setFragmentResult(TaskScheduler.SCHEDULE, TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeOut));

        timeoutView = rootView.findViewById(R.id.timeout);
        countdownFuture = countdownUpdateScheduler.scheduleAtFixedRate(updateCountdown, 0, intervalMilis, TimeUnit.MILLISECONDS);
    }

    //1.When cancel button clicked, sendAbort
    private void onCancelButtonClicked(){
        sendAbort();
    }

    //2.When clear button clicked, clear signature board and reset timeout.
    private void onClearButtonClicked(){
        mSignatureView.clear();
        tempTimeout = timeOut;
        getParentFragmentManager().setFragmentResult(TaskScheduler.SCHEDULE, TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeOut));
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

            submit(total);
        } finally {
            confirmBtn.setClickable(true);
        }
    }

    private void submit(short[] signature){
        countdownFuture.cancel(true);
        Bundle bundle = new Bundle();
        bundle.putShortArray(EntryRequest.PARAM_SIGNATURE, signature);
        sendNext(bundle);
    }

    @Override
    protected void sendAbort() {
        super.sendAbort();
        countdownFuture.cancel(true);
    }

    @Override
    public void onDestroy() {
        countdownFuture.cancel(true);
        countdownUpdateScheduler.shutdownNow();
        super.onDestroy();
    }
}
