package com.paxus.pay.poslinkui.demo.utils;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ListIterator;
import java.util.Stack;

public class InterfaceHistory {

    private Stack<InterfaceRecord> history;

    public InterfaceHistory(){
        history = new Stack<>();
        Logger.d("Interface History Initialized.");
    }

    public void add(@NonNull String interfaceID, @NonNull String interfaceAction) {
        InterfaceRecord newInterfaceRecord = new InterfaceRecord(interfaceID, interfaceAction, System.currentTimeMillis());
        Logger.d("New Interface: " + newInterfaceRecord);
        history.push(newInterfaceRecord);
    }

    public boolean validate(String intendedInterfaceID, String intendedInterfaceAction) {
        if(history.empty()) return true;

        boolean isInterfaceIDValid = intendedInterfaceID != null && history.peek().interfaceID.equals(intendedInterfaceID);
        boolean isInterfaceActionValid = intendedInterfaceAction != null && history.peek().interfaceAction.equals(intendedInterfaceAction);
        if (isInterfaceIDValid && isInterfaceActionValid) history.peek().accept();
        print();
        return isInterfaceIDValid && isInterfaceActionValid;
    }

    private void print() {
        StringBuilder historyString = new StringBuilder(getClass().getSimpleName() + "\n");
        ListIterator<InterfaceRecord> iterator = history.listIterator(history.size());
        while(iterator.hasPrevious()) historyString.append(iterator.previous().toString());
        Logger.d(historyString.toString());
    }

    private class InterfaceRecord {
        public String interfaceID, interfaceAction;
        public long initTimeMillis;
        private boolean isAccepted = false;

        public InterfaceRecord(@NonNull String interfaceID, @NonNull String interfaceAction, long initTimeMillis) {
            this.interfaceID = interfaceID;
            this.interfaceAction = interfaceAction;
            this.initTimeMillis = initTimeMillis;
        }

        public void accept() {
            this.isAccepted = true;
        }

        @NonNull @Override @SuppressLint("SimpleDateFormat") public String toString() {
            return new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(this.initTimeMillis)) +
                    ":  " + this.interfaceAction + (this.isAccepted ? " (Accepted)" : "") + "\t\t" + this.interfaceID + "\n";
        }
    }
}
