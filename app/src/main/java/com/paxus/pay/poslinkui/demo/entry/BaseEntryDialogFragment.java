package com.paxus.pay.poslinkui.demo.entry;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.paxus.pay.poslinkui.demo.R;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.POSLinkUIPastelDialog);
    }

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
            dialog.setOnKeyListener((dialog1, keyCode, event) -> {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
                    executeBackPressEvent();
                    return true;
                } else if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    executeEnterKeyEvent();
                    return true;
                }
                return false;
            });
        }
        activate();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
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

    /**
     * Entry Accepted means BroadPOS accepts the output from ACTION_NEXT
     */
    protected void onEntryAccepted() {
        Logger.i("receive Entry Response ACTION_ACCEPTED for action \"" + getEntryAction() + "\"");
        try {
            dismiss();
        } catch (Exception e) {
        }
    }

    /**
     * Entry Declined means BroadPOS declined the output from ACTION_NEXT cuz it was not valid
     */
    protected void onEntryDeclined(long errCode, String errMessage){
        Logger.i("receive Entry Response ACTION_DECLINED for action \"" + getEntryAction() + "\" (" + errCode + "-" + errMessage + ")");
        Toast.makeText(requireActivity(), errMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * To be overridden by subclasses who contains a confirm button. Generally initiates broadcast to Manager
     */
    protected void onConfirmButtonClicked(){}
    private void executeEnterKeyEvent(){ onConfirmButtonClicked(); }

    protected void sendAbort() {
        try {
            dismiss();
        } catch (Exception e) {
        }
        EntryRequestUtils.sendAbort(requireContext(), getSenderPackageName(), getEntryAction());
    }
    private void executeBackPressEvent(){ sendAbort(); }

    //Used to get information from activity
    FragmentResultListener fragmentResultListener = (requestKey, result) -> {
        Logger.i(getClass().getSimpleName() + " receives " + requestKey + "\n" + result.toString());
        switch(requestKey) {
            case "response":
                switch(result.getString("action")) {
                    case EntryResponse.ACTION_ACCEPTED:
                        onEntryAccepted();
                        break;
                    case EntryResponse.ACTION_DECLINED:
                        onEntryDeclined(result.getLong("code"), result.getString("message"));
                        break;
                }
                break;
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Logger.d(getClass().getSimpleName() + " onViewCreated.");
        super.onViewCreated(view, savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("response", this, fragmentResultListener);
    }
}
