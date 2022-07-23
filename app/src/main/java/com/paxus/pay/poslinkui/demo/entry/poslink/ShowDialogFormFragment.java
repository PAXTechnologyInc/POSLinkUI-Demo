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
    private String transMode;
    private String title;
    private String[] labels;
    private String[] labelProps;
    private String buttonType;

    private Handler handler;
    private final Runnable timeoutRun = new Runnable() {
        @Override
        public void run() {
            EntryRequestUtils.sendTimeout(requireContext(), packageName, action);
        }
    };

    protected String packageName;
    protected String action;

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
        return R.layout.fragment_show_dialog_form;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
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
            handler = new Handler();
            handler.postDelayed(timeoutRun, timeOut);
        }
    }

    private void showRadioDialogForm(){
        Fragment fragment = ShowDialogFormRadioFragment.newInstance(title, labels);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, fragment).commit();
        getChildFragmentManager().setFragmentResultListener(ShowDialogFormRadioFragment.RESULT, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                sendNext(String.valueOf(bundle.getInt(ShowDialogFormRadioFragment.INDEX)));
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
                sendNext(bundle.getString(ShowDialogFormCheckBoxFragment.CHECKED_INDEX));
            }
        });
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

    private void sendNext(String selectLabel){
        EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_LABEL_SELECTED, selectLabel);
    }

}
