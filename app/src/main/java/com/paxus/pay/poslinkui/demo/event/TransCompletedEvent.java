package com.paxus.pay.poslinkui.demo.event;

public class TransCompletedEvent {
    public long code;
    public String message;
    public long timeout;

    public TransCompletedEvent(long code, String message, long timeout){
        this.code = code;
        this.message = message;
        this.timeout = timeout;
    }
}
