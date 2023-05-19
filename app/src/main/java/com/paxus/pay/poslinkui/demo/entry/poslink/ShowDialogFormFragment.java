package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.ManageUIConst;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;

/**
 * Implement text entry actions:<br>
 * {@value PoslinkEntry#ACTION_SHOW_DIALOG_FORM}
 *
 * <p>
 *     UI Tips:
 *     1. Index start from 1
 *
 * </p>
 */
public class ShowDialogFormFragment extends BaseEntryFragment {
    private long timeOut;
    private String title;
    private String[] labels;
    private String[] labelProps;
    private String buttonType;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_dialog_form;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        labels = bundle.getStringArray(EntryExtraData.PARAM_LABELS);
        labelProps = bundle.getStringArray(EntryExtraData.PARAM_LABELS_PROPERTY);
        buttonType = bundle.getString(EntryExtraData.PARAM_BUTTON_TYPE, ManageUIConst.ButtonType.RADIO_BUTTON);
    }

    @Override
    protected void loadView(View rootView) {
        if(ManageUIConst.ButtonType.RADIO_BUTTON.equals(buttonType)){
            showRadioDialogForm();
        }else {
            showCheckBoxDialogForm();
        }

        if(timeOut > 0 ) {
            getParentFragmentManager().setFragmentResult(TaskScheduler.SCHEDULE, TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeOut));
        }
    }

    private void showRadioDialogForm(){
        Fragment fragment = ShowDialogFormRadioFragment.newInstance(title, labels);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, fragment).commit();
        getChildFragmentManager().setFragmentResultListener(ShowDialogFormRadioFragment.RESULT, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                submit(String.valueOf(bundle.getInt(ShowDialogFormRadioFragment.INDEX)));
            }
        });
    }

    private void showCheckBoxDialogForm(){
        Fragment fragment = ShowDialogFormCheckBoxFragment.newInstance(title, labels, labelProps);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, fragment).commit();
        getChildFragmentManager().setFragmentResultListener(ShowDialogFormCheckBoxFragment.RESULT, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                submit(bundle.getString(ShowDialogFormCheckBoxFragment.CHECKED_INDEX));
            }
        });
    }

    private void submit(String selectLabel){
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_LABEL_SELECTED, selectLabel);
        sendNext(bundle);
    }

}
