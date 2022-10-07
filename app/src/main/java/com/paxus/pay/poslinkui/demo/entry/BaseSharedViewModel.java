package com.paxus.pay.poslinkui.demo.entry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseSharedViewModel extends ViewModel {

    private MutableLiveData<Integer> keyCode = new MutableLiveData<>();

    public LiveData<Integer> getKeyCode() { return keyCode;}
    public void setKeyCode(int newKeyCode) { keyCode.postValue(newKeyCode);}


}