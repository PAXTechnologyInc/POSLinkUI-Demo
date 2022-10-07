package com.paxus.pay.poslinkui.demo.event;

import androidx.lifecycle.LiveData;

public class BaseSharedEvent extends LiveData<BaseSharedEvent> {
    public String action;
    public long code;
    public String message;
    private int keyCode;

    public BaseSharedEvent(String action){
        this.action = action;
    }

    public BaseSharedEvent(String action, long code, String message){
        this.action = action;
        this.code = code;
        this.message = message;
    }

    public BaseSharedEvent(int keyCode){
        this.keyCode = keyCode;
    }

    public int getKeyCode(){
        return this.keyCode;
    }


}
