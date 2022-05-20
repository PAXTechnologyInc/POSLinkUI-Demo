package com.paxus.pay.poslinkui.demo.entry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.EntryResponseEvent;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Yanina.Yang on 5/11/2022.
 *
 * Base Dialog Fragment for most entry actions
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
    //Common Input for every Entry Action
    protected String packageName;
    protected String action;
    private boolean active = false;

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


    @Override
    public void onDestroy() {
        super.onDestroy();

        Logger.d(this.getClass().getSimpleName()+" onDestroy");
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
     * @param rootView root view
     */
    protected abstract void loadView(View rootView);

    protected void sendAbort(){
        EntryRequestUtils.sendAbort(requireContext(), packageName, action);
    }

    /**
     * Entry Accepted means BroadPOS accepts the output from ACTION_NEXT
     */
    protected void onEntryAccepted(){
        Logger.i("receive Entry Response ACTION_ACCEPTED for action \""+action+"\"");

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


    //2. On KEYCODE_BACK (on navigation bar) clicked , generally abort action
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryAbort(EntryAbortEvent event) {
        sendAbort();
    }
}
