package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
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
 * {@value PoslinkEntry#ACTION_SHOW_THANK_YOU}
 *
 * <p>
 *     UI Tips:
 *
 * </p>
 */
public class ShowThankYouFragment extends BaseEntryFragment {
    public static final String LEFT_ALIGN = "\\L";
    public static final String RIGHT_ALIGN = "\\R";
    public static final String CENTER_ALIGN = "\\C";

    private long timeOut;
    private String title;
    private String message1;
    private String message2;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_thank_you;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        message1 = bundle.getString(EntryExtraData.PARAM_MESSAGE_1,"");
        message2 = bundle.getString(EntryExtraData.PARAM_MESSAGE_2,"");
    }

    @Override
    protected void loadView(View rootView) {
        TextView textView = rootView.findViewById(R.id.title);
        formatTextView(textView, title);

        TextView msg1View = rootView.findViewById(R.id.message1);
        formatTextView(msg1View, message1);

        TextView msg2View = rootView.findViewById(R.id.message2);
        formatTextView(msg2View, message2);

        if(timeOut > 0 ) {
            getParentFragmentManager().setFragmentResult(TaskScheduler.SCHEDULE, TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeOut));
        }
    }

    private void formatTextView(TextView textView, String msg){
        if(msg.startsWith(LEFT_ALIGN)){
            textView.setGravity(Gravity.START);
        }else if(msg.startsWith(RIGHT_ALIGN)){
            textView.setGravity(Gravity.END);
        }else {
            textView.setGravity(Gravity.CENTER);
        }

        msg = msg.replaceFirst("(\\\\[LRC])","");

        textView.setText(msg);
    }

}
