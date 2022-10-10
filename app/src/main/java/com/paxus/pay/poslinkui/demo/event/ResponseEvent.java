package com.paxus.pay.poslinkui.demo.event;

import androidx.lifecycle.LiveData;

public class ResponseEvent extends LiveData<ResponseEvent> {
    public String action;
    public long code;
    public String message;

    public ResponseEvent(String action){
        this.action = action;
    }

    public ResponseEvent(String action, long code, String message){
        this.action = action;
        this.code = code;
        this.message = message;
    }
}
