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

import java.util.ArrayList;
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
    private String[] options;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_dialog;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        options = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);
    }

    @Override
    protected void loadView(View rootView) {

        TextWithControlChar title = rootView.findViewById(R.id.show_dialog_title_container);
        title.setText(this.title);

        if(options != null) {
            List<TextWithControlChar> texts = new ArrayList<>();
            for(int i=0; i<options.length; i++) {
                int indexInLayout = i+1;

                TextWithControlChar option = rootView.findViewById(getResources().getIdentifier("show_dialog_option_" + indexInLayout, "id", getContext().getPackageName()));
                option.setVisibility(View.VISIBLE);
                option.setText(options[i]).setCheckable(true);
                texts.add(option);

                texts.get(i).setOnClickListener(v -> {
                    for(int j=0; j<texts.size(); j++) texts.get(j).setChecked(false);
                    submit(indexInLayout);
                });
            }
        }

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
