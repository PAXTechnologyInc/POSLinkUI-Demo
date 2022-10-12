package com.paxus.pay.poslinkui.demo.entry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.paxus.pay.poslinkui.demo.event.ResponseEvent;

public class BaseEntryDialogViewModel extends ViewModel {
    private MutableLiveData<ResponseEvent> responseEvent = new MutableLiveData<>();

    public LiveData<ResponseEvent> getResponseEvent() { return responseEvent;}
    public void setResponseEvent(ResponseEvent newResponseEvent) { responseEvent.postValue(newResponseEvent);}
    public void resetResponseEvent() {
        if(getResponseEvent().getValue().action.equals("")) return;
        responseEvent.postValue(new ResponseEvent(""));
    }
}