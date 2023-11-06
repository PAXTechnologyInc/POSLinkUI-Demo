package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.entry.signature.ElectronicSignatureView;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Implement signature entry action {@value PoslinkEntry#ACTION_SHOW_SIGNATURE_BOX}<br>
 * <p>
 *     UI Tips:
 *     1.When cancel button clicked, sendAbort
 *     2.When clear button clicked, clear signature board and reset timeout.
 *     3.When confirm button clicked, if signature board has been touched, sendNext
 *     4.If timeout, sendTimeout
 * </p>
 */
public class ShowSignatureBoxFragment extends BaseEntryFragment {
    private String title;
    private String text;
    private long timeOut;
    private long signBox;

    private Button confirmBtn;
    private ElectronicSignatureView mSignatureView;

    private TextView timeoutView;
    private long tempTimeout;
    private final long intervalMilis = 1000;

    ScheduledExecutorService countdownUpdateScheduler;
    ScheduledFuture<?> countdownFuture;
    Runnable updateCountdown = () -> {
        try {
            if(tempTimeout<=0) {
                countdownFuture.cancel(true);
                if(timeoutView != null) new Handler(Looper.getMainLooper()).post(()-> timeoutView.setVisibility(View.INVISIBLE));
                return;
            }
            if(timeoutView != null) new Handler(Looper.getMainLooper()).post(()-> timeoutView.setText(String.valueOf(tempTimeout/intervalMilis)));
            tempTimeout -= intervalMilis;
        } catch (Exception e) {
            //scheduleAtFixedRate: If any execution of the task encounters an exception, subsequent executions are suppressed.
            Logger.e(e);
        }
    };

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_signature_box;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle){
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        tempTimeout = timeOut;

        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        text = bundle.getString(EntryExtraData.PARAM_TEXT);
        signBox = bundle.getLong(EntryExtraData.PARAM_SIGN_BOX);
        countdownUpdateScheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    protected void loadView(View rootView) {
        TextView titleView = rootView.findViewById(R.id.title_view);
        titleView.setText(title);

        Button cancelBtn = rootView.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(view1-> onCancelButtonClicked());

        Button clearBtn = rootView.findViewById(R.id.clear_button);
        clearBtn.setOnClickListener(view1-> onClearButtonClicked());
        confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(view1 -> onConfirmButtonClicked());

        LinearLayout textView = rootView.findViewById(R.id.text_view);
        if(text == null || text.isEmpty()){
            textView.setVisibility(View.GONE);
        }else {
            for(TextView tv: TextShowingUtils.getTextViewList(requireContext(),text)){
                textView.addView(tv);
            }
            textView.setVisibility(View.VISIBLE);
        }

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

    private void sendNext(short[] signature) {
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
