package com.paxus.pay.poslinkui.demo.entry;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.EntryResponseEvent;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Yanina.Yang on 5/12/2022.
 */
public abstract class BaseEntryDialogFragment extends DialogFragment {
    protected String action;
    protected String packageName;
    protected boolean active = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(this.getClass().getSimpleName()+" onCreateView");
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        Bundle bundle = getArguments();
        if(bundle!= null) {
            loadParameter(bundle);
        }

        loadView(view);

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
                        onBackPressed();
                        return true;
                    }
                    return false;
                }
            });
        }
        activate();
        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        deactivate();
        Logger.d(this.getClass().getSimpleName()+" onDismiss");
    }

    private void activate(){
        if(!active) {
            EventBus.getDefault().register(this);
            active = true;
        }
    }
    private void deactivate(){
        if(active) {
            active = false;
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * Get Layout
     * @return layout xml resource
     */
    protected abstract @LayoutRes int getLayoutResourceId();

    /**
     * Load Entry Parameters
     * @param bundle input from Intent
     */
    protected abstract void loadParameter(@NonNull Bundle bundle);

    /**
     * Prepare View
     * @param rootView root view
     */
    protected abstract void loadView(View rootView);

    protected void sendAbort(){
        dismiss();
        EntryRequestUtils.sendAbort(requireContext(), packageName, action);
    }

    /**
     * On KEY_BACK clicked
     */
    protected void onBackPressed() {
        sendAbort();
    }

    /**
     * Entry Accepted means BroadPOS accepts the output from ACTION_NEXT
     */
    protected void onEntryAccepted(){
        Logger.i("receive Entry Response ACTION_ACCEPTED for action \""+action+"\"");
        dismiss();
    }

    /**
     * Entry Declined means BroadPOS declined the output from ACTION_NEXT cuz it was not valid
     * @param errCode Error Code
     * @param errMessage Error Message
     */
    protected void onEntryDeclined(long errCode, String errMessage){
        Logger.i("receive Entry Response ACTION_DECLINED for action \""+action+"\" ("+errCode+"-"+errMessage+")");
        Toast.makeText(requireActivity(),errMessage,Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetEntryResponse(EntryResponseEvent event){
        switch (event.action){
            case EntryResponse.ACTION_ACCEPTED:
                onEntryAccepted();
                break;
            case EntryResponse.ACTION_DECLINED:{
                onEntryDeclined(event.code,event.message);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryAbort(EntryAbortEvent event) {
        sendAbort();
    }
}
