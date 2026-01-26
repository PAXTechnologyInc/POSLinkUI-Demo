package com.paxus.pay.poslinkui.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Author: Elaine Xie
 * Date: 2026/1/22
 * Desc:
 */
public class DateUtils {
    public static boolean isValidateDate(String input) {
        if (input == null) return false;

        try {
            String monthDay = input.substring(0, 4);
            SimpleDateFormat sdf = new SimpleDateFormat("MMdd", Locale.US);
            sdf.setLenient(false);
            sdf.parse(monthDay);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isValidateTime(String input) {
        if (input == null) return false;
        //  HHMMSS，HH: 00-23, MM: 00-59, SS: 00-59
        return input.matches("([01][0-9]|2[0-3])[0-5][0-9][0-5][0-9]");

    }
}
