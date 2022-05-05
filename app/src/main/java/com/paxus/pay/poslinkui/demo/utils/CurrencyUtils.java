package com.paxus.pay.poslinkui.demo.utils;

import android.annotation.TargetApi;
import android.icu.util.ULocale;
import android.os.Build;
import android.util.Log;

import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Yanina.Yang on 5/3/2022.
 */
public class CurrencyUtils {
    public static String convert(long amount, String currencyType) {
        if (CurrencyType.POINT.equals(currencyType)) {
            //For POINT, don't nee prefix "USD".
            return new DecimalFormat(",###").format(amount);
        }
        String currencySymbol = "$";
        if(CurrencyType.EUR.equals(currencyType)){
            currencySymbol = "€";
        }
        return currencySymbol +new DecimalFormat("0.00").format(amount/100.0);
    }

    public static long parse(String formatterAmount) {
        if(formatterAmount == null || formatterAmount.isEmpty()){
            return 0;
        }
        try {
            return Long.parseLong(formatterAmount.replaceAll("[^0-9]",""));
        }catch (NumberFormatException e){
            return 0;
        }
    }


}
