package com.paxus.pay.poslinkui.demo.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;

public class AmountTextWatcher implements TextWatcher {

    protected boolean mEditing;
    protected String mPreStr;
    private  int maxLength;
    private String currency;
    public AmountTextWatcher(int maxLength, String currency){
        this.maxLength = maxLength;
        this.currency = currency;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!mEditing) {
            mPreStr = s.toString();
        }
        Log.d("AmountFragment","beforeTextChanged:"+mPreStr);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("AmountFragment","onTextChanged:"+mPreStr);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!mEditing) {
            mEditing = true;
            String value = s.toString().replaceAll("[^0-9]", "");
            if (value.length() == 0) {
                value = "0";
            }
            if(value.length() > maxLength){
                s.replace(0, s.length(), mPreStr);
            }else {
                String formatted = CurrencyUtils.convert(Long.parseLong(value), currency);
                s.replace(0, s.length(), formatted);
                Log.d("AmountFragment","afterTextChanged:"+formatted);
            }
            mEditing = false;
        }
    }
}
