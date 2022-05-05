package com.paxus.pay.poslinkui.demo.event;

public class EntryDeclinedEvent {
    public long code;
    public String message;

    public EntryDeclinedEvent(long code, String message){
        this.code = code;
        this.message = message;
    }
}
