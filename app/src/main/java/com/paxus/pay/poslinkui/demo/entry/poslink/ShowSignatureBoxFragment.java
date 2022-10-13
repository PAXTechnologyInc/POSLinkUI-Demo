package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.List;

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
    private String packageName;
    private String action;
    private String title;
    private String text;
    private long timeOut;
    private String transMode;
    private long signBox;

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
        return R.layout.fragment_show_signature_box;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle){
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        text = bundle.getString(EntryExtraData.PARAM_TEXT);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
        signBox = bundle.getLong(EntryExtraData.PARAM_SIGN_BOX);

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
        timeoutView = rootView.findViewById(R.id.timeout);
        tickTimeout = timeOut;
        timeoutView.setText(String.valueOf(tickTimeout/1000));
        handler.postDelayed(tick,1000);
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

    private void sendNext(short[] signature) {
        handler.removeCallbacks(tick); //Stop Tick

        EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_SIGNATURE, signature);
    }

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    @Override
    protected void sendAbort() {
        super.sendAbort();
        handler.removeCallbacks(tick); //Stop Tick
    }

    private void sendTimeout() {
        EntryRequestUtils.sendTimeout(requireContext(), packageName, action);
    }
}
