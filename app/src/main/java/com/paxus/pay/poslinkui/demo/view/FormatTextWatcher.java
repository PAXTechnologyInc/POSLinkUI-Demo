package com.paxus.pay.poslinkui.demo.view;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * TextWatcher for input format
 */
public class FormatTextWatcher implements TextWatcher {
    private static final String FORMAT_DATE = "MM/DD/YYYY";
    private static final String FORMAT_TIME = "HH:MM:SS";
    private static final String FORMAT_PHONE = "(XXX)XXX-XXXX";
    private static final String FORMAT_SSN = "XXX-XX-XXXX";
    private static final String FORMAT_EXPIRY = "MM/YY";

    private final String format;
    public FormatTextWatcher(String format){
        this.format = format;
    }

    boolean editing;
    int deleteIndex = -1;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after){
        boolean needDelete = count == 1 && after == 0 && (s.charAt(start)<'0' ||s.charAt(start)>'9');
        if(needDelete){
            deleteIndex = start;
        }else {
            deleteIndex = -1;
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!editing) {
            editing = true;
            if(deleteIndex>0){
                editable.delete(deleteIndex-1, deleteIndex);
            }
            StringBuilder value = new StringBuilder(editable.toString().replaceAll("[^0-9]", ""));
            if(FORMAT_DATE.equals(format) || FORMAT_EXPIRY.equals(format)) {
                if (value.length() >= 2) {
                    value.insert(2, "/");
                }
                if (value.length() >= 5) {
                    value.insert(5, "/");
                }
            }else if(FORMAT_TIME.equals(format)){
                if (value.length() >= 2) {
                    value.insert(2, ":");
                }
                if (value.length() >= 5) {
                    value.insert(5, ":");
                }
            }else if(FORMAT_PHONE.equals(format)){
                if (value.length() > 0) {
                    value.insert(0, "(");
                }
                if (value.length() >= 4) {
                    value.insert(4, ")");
                }
                if (value.length() >= 8) {
                    value.insert(8, "-");
                }
            }else if(FORMAT_SSN.equals(format)){
                if (value.length() >= 3) {
                    value.insert(3, "-");
                }
                if (value.length() >= 6) {
                    value.insert(6, "-");
                }
            }
            editable.replace(0, editable.length(), value);
            editing = false;
        }
    }
}