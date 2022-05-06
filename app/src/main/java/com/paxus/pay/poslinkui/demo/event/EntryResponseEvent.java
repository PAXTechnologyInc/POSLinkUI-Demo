package com.paxus.pay.poslinkui.demo.event;

/**
 * Created by Yanina.Yang on 5/6/2022.
 */
public class EntryResponseEvent {
    public String action;
    public long code;
    public String message;

    public EntryResponseEvent(String action){
        this.action = action;
    }

    public EntryResponseEvent(String action, long code, String message){
        this.action = action;
        this.code = code;
        this.message = message;
    }
}
