package com.paxus.pay.poslinkui.demo.utils.interfacefilter;

public class EntryAction {
    public String category, action, name, alias;
    boolean enableByDefault;
    public boolean isCurrentlyEnabled = false;

    public EntryAction(String category, String action, String name, String alias, boolean enableByDefault) {
        this.category = category;
        this.action = action;
        this.name = name;
        this.alias = alias;
        this.enableByDefault = enableByDefault;
    }
}