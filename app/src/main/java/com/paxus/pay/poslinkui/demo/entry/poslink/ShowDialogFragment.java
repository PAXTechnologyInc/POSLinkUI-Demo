package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

/**
 * Implement text entry actions:<br>
 * {@value PoslinkEntry#ACTION_SHOW_DIALOG}
 *
 * <p>
 *     UI Tips:
 *     Index start from 1
 *
 * </p>
 */
public class ShowDialogFragment extends BaseEntryFragment {
    private String packageName;
    private String action;
    private long timeOut;
    private String transMode;
    private String title;
    private String button1;
    private String button2;
    private String button3;
    private String button4;

    private Handler handler;
    private final Runnable timeoutRun = new Runnable() {
        @Override
        public void run() {
            EntryRequestUtils.sendTimeout(requireContext(), packageName, action);
        }
    };


    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_dialog;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        String[] options = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
        if(options!=null) {
            if (options.length >= 1) {
                button1 = options[0];
            }
            if (options.length >= 2) {
                button2 = options[1];
            }
            if (options.length >= 3) {
                button3 = options[2];
            }
            if (options.length >= 4) {
                button4 = options[3];
            }
        }

    }

    @Override
    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.title);
        textView.setText(title);

        Button btn1 = rootView.findViewById(R.id.button1);
        formatButton(btn1, button1, 1);

        Button btn2 = rootView.findViewById(R.id.button2);
        formatButton(btn2, button2, 2);

        Button btn3 = rootView.findViewById(R.id.button3);
        formatButton(btn3, button3, 3);

        Button btn4 = rootView.findViewById(R.id.button4);
        formatButton(btn4, button4, 4);

        if(timeOut > 0 ) {
            handler = new Handler();
            handler.postDelayed(timeoutRun, timeOut);
        }
    }

    private void formatButton(Button button, String message, int index){
        if(!TextUtils.isEmpty(message)){
            button.setText(message);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendNext(index);
                }
            });
        }else{
            button.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onEntryAccepted() {
        super.onEntryAccepted();

        if(handler!= null) {
            handler.removeCallbacks(timeoutRun);
            handler = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(handler != null) {
            handler.removeCallbacks(timeoutRun);
            handler = null;
        }
    }

    private void sendNext(int index){
        EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_INDEX, index);
    }

}
