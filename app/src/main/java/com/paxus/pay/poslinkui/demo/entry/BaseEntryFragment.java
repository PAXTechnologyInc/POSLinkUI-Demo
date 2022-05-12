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

    protected abstract @LayoutRes
    int getLayoutResourceId();

    protected abstract void loadArgument(@NonNull Bundle bundle);

    protected abstract void loadView(View rootView);

    protected void sendAbort(){
        EntryRequestUtils.sendAbort(requireContext(), packageName, action);
    }

    protected void onEntryAccepted(){
        Logger.i("receive Entry Response ACTION_ACCEPTED for action \""+action+"\"");

        //Yanina: After entry accepted, should not response AbortEvent.
        deactivate();
    }

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
