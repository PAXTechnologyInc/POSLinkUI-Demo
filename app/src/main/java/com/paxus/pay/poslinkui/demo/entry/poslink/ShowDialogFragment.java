package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.StringUtils;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;
import com.paxus.pay.poslinkui.demo.view.TextField;

import java.util.Arrays;
import java.util.List;

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
    private LinearLayout llButton1;
    private LinearLayout llButton2;
    private LinearLayout llButton3;
    private LinearLayout llButton4;
    private String[] buttons ;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_dialog;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        String[] options = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
        if(options!=null && options.length >0) {
            buttons = options;
        }

    }

    @Override
    protected void loadView(View rootView) {
        LinearLayout titleLayout = rootView.findViewById(R.id.title_layout_show_dialog);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        TextShowingUtils.getTitleViewListAsync(
                requireContext(),
                title,
                lp,
                Color.WHITE,
                requireContext().getResources().getDimension(R.dimen.text_size_subtitle),
                true,
                titleLayout
        );
        llButton1 = rootView.findViewById(R.id.button1);

        llButton2 = rootView.findViewById(R.id.button2);

        llButton3 = rootView.findViewById(R.id.button3);

        llButton4 = rootView.findViewById(R.id.button4);

        rootView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if(keyCode == KeyEvent.KEYCODE_1 && llButton1.getVisibility() == View.VISIBLE) {
                    llButton1.callOnClick();
                    return true;
                }
                if(keyCode == KeyEvent.KEYCODE_2 && llButton2.getVisibility() == View.VISIBLE) {
                    llButton2.callOnClick();
                    return true;
                }
                if(keyCode == KeyEvent.KEYCODE_3 && llButton3.getVisibility() == View.VISIBLE) {
                    llButton3.callOnClick();
                    return true;
                }
                if(keyCode == KeyEvent.KEYCODE_4 && llButton4.getVisibility() == View.VISIBLE) {
                    llButton4.callOnClick();
                    return true;
                }
                return false;
            }
            return false;
        });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        showButtons();
        if(timeOut > 0 ) {
            getParentFragmentManager().setFragmentResult(TaskScheduler.SCHEDULE, TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeOut));
        }
    }


    private void showButtons(){
        List<LinearLayout> disPlayButtons = Arrays.asList(llButton1, llButton2, llButton3, llButton4);
        if (buttons!=null) {
            for (int i = 0; i < buttons.length; i++) {
                if (StringUtils.isEmpty(buttons[i])) {
                    break;
                }
                disPlayButtons.get(i).setVisibility(View.VISIBLE);
                setButtonView(disPlayButtons.get(i), buttons[i]);
                int index = i + 1;
                disPlayButtons.get(i).setOnClickListener(v -> {
                    submit(index);
                });
            }
        }
    }

    private void setButtonView(LinearLayout layoutBtn, String btnName) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        List<TextView> viewList = TextShowingUtils.getTitleViewList(requireContext(), btnName, lp,Color.WHITE, requireContext().getResources().getDimension(R.dimen.text_size_subtitle));
        for (TextView view : viewList) {
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) view.getLayoutParams();
            lps.gravity = lps.gravity | Gravity.CENTER;
            view.setGravity(lps.gravity);
            view.setLayoutParams(lps);
            view.setElegantTextHeight(true);
            view.setTextColor(Color.WHITE);
            layoutBtn.addView(view);
        }
    }

    private void submit(int index){
        Bundle bundle = new Bundle();
        bundle.putInt(EntryRequest.PARAM_INDEX, index);
        sendNext(bundle);
    }

    @Override
    protected TextField[] focusableTextFields() {
        return null;
    }
}
