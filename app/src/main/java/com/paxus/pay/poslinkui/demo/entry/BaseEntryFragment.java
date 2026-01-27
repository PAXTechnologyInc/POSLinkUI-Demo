package com.paxus.pay.poslinkui.demo.entry;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.paxus.pay.poslinkui.demo.utils.DeviceUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.Toast;
import com.paxus.pay.poslinkui.demo.view.TextField;
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yanina.Yang on 5/11/2022.
 *
 * <p>
 * UI Tips:
 * 1. Load layout in onCreateView (getLayoutResourceId, loadParameter, loadView)
 * 2. On KEYCODE_BACK (on navigation bar) clicked , generally abort action
 * 3. After send next to BroadPOS, the request might be accepted or declined,
 * (1)when got declined, prompt declined message
 * (2)when got accepted, should not response AbortEvent any more.
 * </p>
 */
public abstract class BaseEntryFragment extends Fragment {

    private boolean active = false;
    private Context appContext = getActivity();
    private String senderPackage, action;
    protected SecondScreenInfoViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(this.getClass().getSimpleName() + " onCreateView");
        viewModel = new ViewModelProvider(requireActivity()).get(SecondScreenInfoViewModel.class);

        //1. Load layout in onCreateView (getLayoutResourceId, loadParameter, loadView)
        Bundle bundle = getArguments();
        if (bundle != null) {
            action = bundle.getString(EntryRequest.PARAM_ACTION);
            senderPackage = bundle.getString(EntryExtraData.PARAM_PACKAGE);
            loadArgument(bundle);
        } else {
            Logger.e(this.getClass().getSimpleName() + " arguments missing!!!");
        }


        View view = inflater.inflate(getLayoutResourceId(), container, false);
        loadView(view);
        setupFocusableTextFields(focusableTextFields(), view);
        activate();
        return view;
    }

    private void setupFocusableTextFields(TextField[] textFields, View view) {
        if(textFields!= null && textFields.length > 0) {
            // if devices have physical keyboard, hide keyboard at the beginning.
            // Otherwise, the keyboard is prone to abruptly dismiss.
            if (DeviceUtils.hasPhysicalKeyboard()) {
                // delay after the view is ready
                textFields[0].postDelayed(() -> textFields[0].requestFocus(), 200);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            } else {
                // for A3700, it need to requestFocus in the beginning, preventing the scrollview from obtaining the focus first
                textFields[0].requestFocus();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            for (int i = 0; i < textFields.length - 1; i++) {
                textFields[i].attachToLifecycle(getViewLifecycleOwner().getLifecycle());
                textFields[i].setImeOptions(textFields[i].getImeOptions() | EditorInfo.IME_ACTION_NEXT);
                textFields[i].setNextFocusDownId(textFields[i + 1].getId());
            }
            textFields[textFields.length - 1].attachToLifecycle(getViewLifecycleOwner().getLifecycle());
            textFields[textFields.length - 1].setImeOptions(textFields[textFields.length - 1].getImeOptions() | EditorInfo.IME_ACTION_DONE);
            textFields[textFields.length - 1].setOnEditorActionListener((textView, i, keyEvent) -> {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    executeEnterKeyEvent();
                }
                return true;
            });
        } else {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY|InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onDestroy() {
        Logger.d(getClass().getSimpleName() + " onDestroy.");
        super.onDestroy();
        deactivate();
    }

    private void activate() {
        active = true;
    }

    private void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Get Layout
     *
     * @return layout xml resource
     */
    protected abstract @LayoutRes int getLayoutResourceId();

    /**
     * Load Entry Parameters
     *
     * @param bundle input from Intent
     */
    protected abstract void loadArgument(@NonNull Bundle bundle);

    /**
     * Prepare View
     *
     * @param rootView root view
     */
    protected abstract void loadView(View rootView);

    /**
     * To be overridden by subclasses who contains a confirm button. Generally initiates broadcast to Manager
     */
    protected void onConfirmButtonClicked() {
    }

    private void executeEnterKeyEvent() {
        onConfirmButtonClicked();
    }

    protected void sendNext(Bundle bundle){
        // For DISPLAY_APPROVE_MESSAGE, it will display 3 seconds, and then send next,
        // so, if the adb command is used to perform this action again within 3 seconds, it will crash.
        // So if there is no view and context, then there is no need to proceed.
        if (getView() != null) {
            EditabilityBlocker.getInstance().block((ViewGroup) getView());
        }
        if (getContext() != null) {
            EntryRequestUtils.sendNext(requireContext(), senderPackage, action, bundle);
        }
    }

    protected void sendSecurityArea(TextView view, String... hint){
        if(view == null) {
            EntryRequestUtils.sendSecureArea(requireContext(), senderPackage, action);
            return;
        }

        int[] location = new int[2];
        view.getLocationInWindow(location);

        int barHeight = 0;
        boolean immersiveSticky = (requireActivity().getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) > 0;
        if (!immersiveSticky) {
            Rect rect = new Rect();
            requireActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            barHeight = rect.top;
        }

        int fontSize = (int) (view.getPaint().getTextSize() / view.getPaint().density);
        EntryRequestUtils.sendSecureArea(requireContext(), senderPackage, action, location[0], location[1] - barHeight,
                view.getWidth(), view.getHeight(), fontSize,
                (hint!=null && hint.length>0) ? hint[0] : "",
                String.format("%X", view.getCurrentTextColor()));
    }
    protected void sendSetPinKeyLayout(Bundle keyLocations){
        EntryRequestUtils.sendSetPinKeyLayout(requireContext(), senderPackage, action, keyLocations);
    }
    protected void sendTimeout(){
        EntryRequestUtils.sendTimeout(requireContext(), senderPackage, action);
    }
    protected void sendAbort() {
        EntryRequestUtils.sendAbort(requireContext(), senderPackage, action);
    }

    protected void executeBackPressEvent() {
        sendAbort();
    }

    protected abstract TextField[] focusableTextFields();

    /**
     * Entry Accepted means BroadPOS accepts the output from ACTION_NEXT
     */
    protected void onEntryAccepted() {
        Logger.i("Receive Response Broadcast ACTION_ACCEPTED for action \"" + action + "\"");
        deactivate();
    }

    /**
     * Entry Declined means BroadPOS declined the output from ACTION_NEXT cuz it was not valid
     *
     * @param errCode    Error Code
     * @param errMessage Error Message
     */
    protected void onEntryDeclined(int errCode, String errMessage) {
        Logger.i("Receive Response Broadcast ACTION_DECLINED for action \"" + action + "\" (" + errCode + "-" + errMessage + ")");
        new Toast(getActivity()).show(errMessage, Toast.TYPE.FAILURE);
    }

    //Used to get response broadcast from activity
    FragmentResultListener responseBroadcastListener = (requestKey, result) -> {
        switch (result.getString("action")) {
            case EntryResponse.ACTION_ACCEPTED:
                onEntryAccepted();
                break;
            case EntryResponse.ACTION_DECLINED:
                EditabilityBlocker.getInstance().release();
                onEntryDeclined(result.getInt(EntryResponse.PARAM_CODE), result.getString(EntryResponse.PARAM_MSG)); //POSUI-244
                break;
        }
    };

    //Used to get dispatched keyevent from activity
    FragmentResultListener keyEventListener = (requestKey, result) -> {
        switch (result.getInt("keyCode")) {
            case KeyEvent.KEYCODE_ENTER:
                executeEnterKeyEvent();
                break;
            case KeyEvent.KEYCODE_BACK:
                executeBackPressEvent();
                break;
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Logger.d(getClass().getSimpleName() + " onViewCreated.");
        super.onViewCreated(view, savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("response", this, responseBroadcastListener);
        getParentFragmentManager().setFragmentResultListener("keyCode", this, keyEventListener);
    }


    private static class EditabilityBlocker {
        private static EditabilityBlocker instance = null;
        private List<View> blockedViews = new ArrayList<>();
        private EditabilityBlocker() {}
        public synchronized static EditabilityBlocker getInstance(){
            if(instance == null) instance = new EditabilityBlocker();
            return instance;
        }
        public void block(ViewGroup parent){
            for(int i=0; i<parent.getChildCount(); i++){
                if (parent.getChildAt(i) instanceof TextField || parent.getChildAt(i) instanceof Button) {
                    if(parent.getChildAt(i).getVisibility() == View.VISIBLE && parent.getChildAt(i).isEnabled()){
                        parent.getChildAt(i).setEnabled(false);
                        blockedViews.add(parent.getChildAt(i));
                    }
                } else if (parent.getChildAt(i) instanceof CheckedTextView) {
                    if(parent.getVisibility() == View.VISIBLE && parent.isEnabled()){
                        parent.setEnabled(false);
                        blockedViews.add(parent);
                    }
                } else if (parent.getChildAt(i) instanceof ViewGroup) {
                    block((ViewGroup) parent.getChildAt(i));
                }
            }
        }
        public void release(){
            for(View blockedView: blockedViews) {
                blockedView.setEnabled(true);
            }
            blockedViews.clear();
        }
    }
}
