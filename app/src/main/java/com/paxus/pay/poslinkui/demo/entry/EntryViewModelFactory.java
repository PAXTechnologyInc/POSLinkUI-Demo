package com.paxus.pay.poslinkui.demo.entry;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.paxus.pay.poslinkui.demo.event.ResponseEvent;

public class EntryViewModelFactory {

    BaseEntryViewModel baseEntryViewModel;
    BaseEntryDialogViewModel baseEntryDialogViewModel;

    boolean isDialog = false;

    public void createViewModel(ViewModelStoreOwner activity, Fragment fragment){
        this.isDialog = fragment != null && fragment instanceof DialogFragment;
        if(!isDialog){
            if(baseEntryViewModel == null) baseEntryViewModel = new ViewModelProvider(activity).get(BaseEntryViewModel.class);
        } else {
            if(baseEntryDialogViewModel == null) baseEntryDialogViewModel = new ViewModelProvider(activity).get(BaseEntryDialogViewModel.class);
        }
    }

    public void setKeyCode(int keyCode){
        if(!isDialog) baseEntryViewModel.setKeyCode(keyCode);
    }

    public void setResponseEvent(ResponseEvent newResponseEvent){
        if(!isDialog) baseEntryViewModel.setResponseEvent(newResponseEvent);
        else baseEntryDialogViewModel.setResponseEvent(newResponseEvent);
    }

}