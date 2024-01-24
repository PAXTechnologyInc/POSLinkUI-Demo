package com.paxus.pay.poslinkui.demo.utils;

import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yanina.Yang on 5/3/2022.
 *
 * Utils for parse Amount
 */
public class CurrencyUtils {

    public final static Map<String, String> CURRENCY_SYMBOL_MAP = new HashMap<String, String>() {{
        put(CurrencyType.CAD, "$");
        put(CurrencyType.EUR, "€");
        put(CurrencyType.GBP, "£");
        put(CurrencyType.USD, "$");
        put(CurrencyType.POINT, "");
    }};

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
