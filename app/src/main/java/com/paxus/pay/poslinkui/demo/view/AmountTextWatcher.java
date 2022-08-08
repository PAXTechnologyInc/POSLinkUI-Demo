package com.paxus.pay.poslinkui.demo.view;

import android.text.Editable;
import android.text.TextWatcher;

import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;

/**
 * TextWatcher for input format
 * Example: <br>
 * if currency is {@value CurrencyType#USD}<br>
 *      "1" --> "$1.00" <br>
 * if currency is {@value CurrencyType#POINT}<br>
 *      "1" --> "$1.00" <br>
 */
public class AmountTextWatcher implements TextWatcher {

    protected boolean mEditing;
    protected String mPreStr;
    private int maxLength;
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
        Logger.d("AmountTextWatcher beforeTextChanged:"+mPreStr);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Logger.d("AmountTextWatcher onTextChanged:"+mPreStr);
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
            } else {
                String formatted = CurrencyUtils.convert(Long.parseLong(value), currency);
                s.replace(0, s.length(), formatted);
                Logger.d("AmountTextWatcher afterTextChanged:"+formatted);
            }
            mEditing = false;
        }
    }
}
