package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
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
        return R.layout.fragment_show_dialog_new;
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

        TextWithControlChar title = rootView.findViewById(R.id.show_dialog_title_container);
        title.setText(this.title);

        if(timeOut > 0 ) {
            getParentFragmentManager().setFragmentResult(TaskScheduler.SCHEDULE, TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeOut));
        }
    }

    private void submit(int index){
        Bundle bundle = new Bundle();
        bundle.putInt(EntryRequest.PARAM_INDEX, index);
        sendNext(bundle);
    }

}
