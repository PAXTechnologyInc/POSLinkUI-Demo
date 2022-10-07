package com.paxus.pay.poslinkui.demo.entry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.EntryConfirmEvent;
import com.paxus.pay.poslinkui.demo.event.EntryResponseEvent;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.view.KeyEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Yanina.Yang on 5/11/2022.
 *
 * <p>
 *     UI Tips:
 *     1. Load layout in onCreateView (getLayoutResourceId, loadParameter, loadView)
 *     2. On KEYCODE_BACK (on navigation bar) clicked , generally abort action
 *     3. After send next to BroadPOS, the request might be accepted or declined,
 *        (1)when got declined, prompt declined message
 *        (2)when got accepted, should not response AbortEvent any more.
 * </p>
 */
public abstract class BaseEntryFragment extends Fragment {

    private boolean active = false;
    private BaseSharedViewModel baseSharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(this.getClass().getSimpleName()+" onCreateView");

        //1. Load layout in onCreateView (getLayoutResourceId, loadParameter, loadView)
        Bundle bundle = getArguments();
        if(bundle != null) {
            loadArgument(bundle);
        }else {
            Logger.e(this.getClass().getSimpleName()+" arguments missing!!!");
        }

        View view = inflater.inflate(getLayoutResourceId(), container, false);
        loadView(view);
        activate();
        return view;
    }

    Observer<Integer> baseSharedViewModelObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer input) {
            Logger.d(input);
            switch (input){
                case KeyEvent.KEYCODE_ENTER:
                    implementEnterKeyEvent();
                    break;
                case KeyEvent.KEYCODE_BACK:
                    sendAbort();
            }
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Logger.d(getClass().getSimpleName() + " onViewCreated.");
        super.onViewCreated(view, savedInstanceState);

        baseSharedViewModel = new ViewModelProvider(requireActivity()).get(BaseSharedViewModel.class);
        baseSharedViewModel.getKeyCode().removeObservers(getViewLifecycleOwner());
        baseSharedViewModel.getKeyCode().observe(getViewLifecycleOwner(), baseSharedViewModelObserver);
    }

    @Override
    public void onDestroyView() {
        Logger.d(getClass().getSimpleName() + " onDestroyView.");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Logger.d(getClass().getSimpleName() + " onDestroy.");
        super.onDestroy();
        deactivate();
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

    public boolean isActive() {
        return active;
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
    protected abstract void loadArgument(@NonNull Bundle bundle);

    /**
     * Prepare View
     *
     * @param rootView root view
     */
    protected abstract void loadView(View rootView);

    protected void sendAbort() {
        EntryRequestUtils.sendAbort(requireContext(), getSenderPackageName(), getEntryAction());
    }

    protected abstract String getSenderPackageName();

    protected abstract String getEntryAction();

    /**
     * Entry Accepted means BroadPOS accepts the output from ACTION_NEXT
     */
    protected void onEntryAccepted() {
        Logger.i("receive Entry Response ACTION_ACCEPTED for action \"" + getEntryAction() + "\"");

        //3.2 when got accepted, should not response AbortEvent any more.
        deactivate();
    }

    /**
     * Entry Declined means BroadPOS declined the output from ACTION_NEXT cuz it was not valid
     * @param errCode Error Code
     * @param errMessage Error Message
     */
    protected void onEntryDeclined(long errCode, String errMessage){
        //3.1 when got declined, prompt declined message
        Logger.i("receive Entry Response ACTION_DECLINED for action \"" + getEntryAction() + "\" (" + errCode + "-" + errMessage + ")");
        Toast.makeText(requireActivity(), errMessage, Toast.LENGTH_SHORT).show();
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


    //2. On KEYCODE_BACK (on navigation bar) clicked , generally abort action
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryAbort(EntryAbortEvent event) {
        sendAbort();
    }

    protected void implementEnterKeyEvent(){}

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryConfirm(EntryConfirmEvent entryConfirmEvent){
        implementEnterKeyEvent();
    }

    protected void prepareEditTextsForSubmissionWithSoftKeyboard(EditText... editTexts){
        for(int i=0; i<editTexts.length-1; i++) {
            editTexts[i].setImeOptions(editTexts[i].getImeOptions() | EditorInfo.IME_ACTION_NEXT);
            editTexts[i].setNextFocusDownId(editTexts[i+1].getId());
        }
        editTexts[editTexts.length-1].setImeOptions(editTexts[editTexts.length-1].getImeOptions() | EditorInfo.IME_ACTION_DONE);
        editTexts[editTexts.length-1].setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE){
                implementEnterKeyEvent();
            }
            return true;
        });
    }
}
