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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.paxus.pay.poslinkui.demo.event.ResponseEvent;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;

/**
 * Created by Yanina.Yang on 5/12/2022.
 *
 * Base Dialog Fragment for some Confirmation Entry and Options Entry
 * <p>
 *     UI Tips:
 *     1. Load layout in onCreateView (getLayoutResourceId, loadParameter, loadView)
 *     2. Dialog should not be canceled by touch outside
 *     3. On KEYCODE_BACK (on navigation bar) clicked , generally close dialog and abort action
 *     4. After send next to BroadPOS, the request might be accepted or declined,
 *        (1)when got declined, prompt declined message
 *        (2)when got accepted, close dialog
 * </p>
 */
public abstract class BaseEntryDialogFragment extends DialogFragment {

    protected boolean isActive = false; //After entry request accepted, isActive will be false
    private BaseEntryDialogViewModel baseEntryDialogViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(this.getClass().getSimpleName()+" onCreateView");
        //------1. Load layout in onCreateView (getLayoutResourceId, loadParameter, loadView)---------
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        Bundle bundle = getArguments();
        if(bundle!= null) {
            loadParameter(bundle);
        }

        loadView(view);

        Dialog dialog = getDialog();
        if (dialog != null) {
            //-------2. Dialog should not be canceled by touch outside-------
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
                        executeBackPressEvent();
                        return true;
                    } else if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                        executeEnterKeyEvent();
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
        Logger.d(this.getClass().getSimpleName()+" onDismiss");
        super.onDismiss(dialog);
        deactivate();
    }

    private void activate(){
        isActive = true;
    }
    private void deactivate(){
        isActive = false;
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
     *
     * @param rootView root view
     */
    protected abstract void loadView(View rootView);

    protected abstract String getSenderPackageName();

    protected abstract String getEntryAction();

    protected void sendAbort() {
        try {
            dismiss();
        } catch (Exception e) {
            //Secure Dismiss dialog
        }
        EntryRequestUtils.sendAbort(requireContext(), getSenderPackageName(), getEntryAction());
    }

    /**
     * Entry Accepted means BroadPOS accepts the output from ACTION_NEXT
     */
    protected void onEntryAccepted() {
        //4.2when got accepted, close dialog
        Logger.i("receive Entry Response ACTION_ACCEPTED for action \"" + getEntryAction() + "\"");
        try {
            dismiss();
        } catch (Exception e) {
            //Secure Dismiss dialog
        }
    }

    /**
     * Entry Declined means BroadPOS declined the output from ACTION_NEXT cuz it was not valid
     * @param errCode Error Code
     * @param errMessage Error Message
     */
    protected void onEntryDeclined(long errCode, String errMessage){
        //4.1when got declined, prompt declined message
        Logger.i("receive Entry Response ACTION_DECLINED for action \"" + getEntryAction() + "\" (" + errCode + "-" + errMessage + ")");
        Toast.makeText(requireActivity(), errMessage, Toast.LENGTH_SHORT).show();
    }

    protected void onConfirmButtonClicked(){}

    protected void executeEnterKeyEvent(){ onConfirmButtonClicked(); }
    protected void executeBackPressEvent(){ sendAbort(); }

    Observer<ResponseEvent> responseEventObserver = new Observer<ResponseEvent>() {
        @Override
        public void onChanged(ResponseEvent event) {
            Logger.d(getClass().getSimpleName() + " receives " + event.action);
            switch (event.action){
                case EntryResponse.ACTION_ACCEPTED:
                    onEntryAccepted();
                    break;
                case EntryResponse.ACTION_DECLINED:{
                    onEntryDeclined(event.code,event.message);
                }
            }
            baseEntryDialogViewModel.resetResponseEvent();
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        baseEntryDialogViewModel = new ViewModelProvider(requireActivity()).get(BaseEntryDialogViewModel.class);
        baseEntryDialogViewModel.getResponseEvent().removeObservers(getViewLifecycleOwner());
        baseEntryDialogViewModel.getResponseEvent().observe(getViewLifecycleOwner(), responseEventObserver);
    }
}
