package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;
import com.paxus.pay.poslinkui.demo.view.TextField;

import java.util.List;

/**
 * Implement text entry actions:<br>
 * {@value PoslinkEntry#ACTION_SHOW_THANK_YOU}
 *
 * <p>
 * UI Tips:
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
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        message1 = bundle.getString(EntryExtraData.PARAM_MESSAGE_1, "");
        message2 = bundle.getString(EntryExtraData.PARAM_MESSAGE_2, "");
    }

    @Override
    protected void loadView(View rootView) {
        LinearLayout titleLayout = rootView.findViewById(R.id.title_layout_show_thank_you);
        LinearLayout msg1Layout = rootView.findViewById(R.id.message1_layout_show_thank_you);
        LinearLayout msg2Layout = rootView.findViewById(R.id.message2_layout_show_thank_you);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);

        setTextView(TextShowingUtils.getTitleViewList(requireContext(), title, lp, Color.WHITE, requireContext().getResources().getDimension(R.dimen.text_size_title)), titleLayout, title);
        setTextView(TextShowingUtils.getViewList(requireContext(), message1, lp, Color.WHITE, requireContext().getResources().getDimension(R.dimen.text_size_subtitle)), msg1Layout, message1);
        setTextView(TextShowingUtils.getViewList(requireContext(), message2, lp, Color.WHITE, requireContext().getResources().getDimension(R.dimen.text_size_subtitle)), msg2Layout, message2);
        if (timeOut > 0) {
            getParentFragmentManager().setFragmentResult(TaskScheduler.SCHEDULE, TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeOut));
        }
    }


    private void setTextView(List<TextView> viewList, LinearLayout msgLayout, String msgStr) {
        for (TextView textView : viewList) {
            textView.setElegantTextHeight(true);
            textView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            textView.setSingleLine(false);
            textView.setMaxLines(4);
            msgLayout.addView(textView);
        }
    }

    @Override
    protected TextField[] focusableTextFields() {
        return null;
    }
}
