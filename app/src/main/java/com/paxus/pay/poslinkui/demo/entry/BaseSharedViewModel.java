package com.paxus.pay.poslinkui.demo.entry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.paxus.pay.poslinkui.demo.event.ResponseEvent;

public class BaseSharedViewModel extends ViewModel {

    private MutableLiveData<Integer> keyCode = new MutableLiveData<>();
    public LiveData<Integer> getKeyCode() { return keyCode;}
    public void setKeyCode(int newKeyCode) { keyCode.postValue(newKeyCode);}

    private MutableLiveData<ResponseEvent> responseEvent = new MutableLiveData<>();
    public LiveData<ResponseEvent> getResponseEvent() { return responseEvent;}
    public void setResponseEvent(ResponseEvent newResponseEvent) { responseEvent.postValue(newResponseEvent);}

}