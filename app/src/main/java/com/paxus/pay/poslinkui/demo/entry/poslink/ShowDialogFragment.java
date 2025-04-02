package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
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
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;

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
    private long timeOut;
    private String title;
    private String button1;
    private String button2;
    private String button3;
    private String button4;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_dialog;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
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

        rootView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if(keyCode == KeyEvent.KEYCODE_1 && btn1.getVisibility() == View.VISIBLE) {
                    btn1.callOnClick();
                    return true;
                }
                if(keyCode == KeyEvent.KEYCODE_2 && btn2.getVisibility() == View.VISIBLE) {
                    btn2.callOnClick();
                    return true;
                }
                if(keyCode == KeyEvent.KEYCODE_3 && btn3.getVisibility() == View.VISIBLE) {
                    btn3.callOnClick();
                    return true;
                }
                if(keyCode == KeyEvent.KEYCODE_4 && btn4.getVisibility() == View.VISIBLE) {
                    btn4.callOnClick();
                    return true;
                }
                return false;
            }
            return false;
        });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        if(timeOut > 0 ) {
            getParentFragmentManager().setFragmentResult(TaskScheduler.SCHEDULE, TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeOut));
        }
    }

    private void formatButton(Button button, String message, int index){
        if(!TextUtils.isEmpty(message)){
            button.setText(message);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit(index);
                }
            });
        }else{
            button.setVisibility(View.GONE);
        }
    }

    private void submit(int index){
        Bundle bundle = new Bundle();
        bundle.putInt(EntryRequest.PARAM_INDEX, index);
        sendNext(bundle);
    }

}
