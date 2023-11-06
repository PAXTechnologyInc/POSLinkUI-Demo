package com.paxus.pay.poslinkui.demo.utils;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

/**
 * Logger, but more pretty, simple and powerful
 */
public final class Logger {

    private Logger() {
        //no instance
    }

    public static void d(@NonNull String message, @Nullable Object... args) {
        com.orhanobut.logger.Logger.d(message, args);
    }

    public static void d(@Nullable Object object) {
        com.orhanobut.logger.Logger.d(object);
    }

    public static void e(@NonNull String message, @Nullable Object... args) {
        com.orhanobut.logger.Logger.e(message, args);
    }

    public static void e(@Nullable Throwable throwable) {
        e(throwable, throwable == null || throwable.getMessage() == null ? "" : throwable.getMessage());
    }

    public static void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        com.orhanobut.logger.Logger.e(throwable, message, args);
    }

    public static void i(@NonNull String message, @Nullable Object... args) {
        com.orhanobut.logger.Logger.i(message, args);
    }

    public static void v(@NonNull String message, @Nullable Object... args) {
        com.orhanobut.logger.Logger.v(message, args);
    }

    public static void w(@NonNull String message, @Nullable Object... args) {
        com.orhanobut.logger.Logger.w(message, args);
    }

    /**
     * Tip: Use this for exceptional situations to log
     * ie: Unexpected errors etc
     */
    public static void wtf(@NonNull String message, @Nullable Object... args) {
        com.orhanobut.logger.Logger.wtf(message, args);
    }

    /**
     * Formats the given json content and print it
     */
    public static void json(@Nullable String json) {
        com.orhanobut.logger.Logger.json(json);
    }

    /**
     * Formats the given xml content and print it
     */
    public static void xml(@Nullable String xml) {
        com.orhanobut.logger.Logger.d(xml);
    }


    public static void intent(Intent intent) {
        StringBuilder intentBuilder = new StringBuilder();
        if(intent.getExtras() != null){
            for(String key : intent.getExtras().keySet()){
                intentBuilder.append(key).append(": ");
                boolean isArray = intent.getExtras().get(key) != null && intent.getExtras().get(key).getClass().isArray();
                intentBuilder.append(isArray ? stringifyArray(intent.getExtras().get(key)) : intent.getExtras().get(key));
                intentBuilder.append(System.lineSeparator());
            }
        }
        Logger.i(intentBuilder.toString());
    }

    private static String stringifyArray(Object array){
        switch (array.getClass().getSimpleName()){
            case "short[]":
                return Arrays.toString((short[])array);
            case "Object[]":
            case "String[]":
                return Arrays.toString((String[])array);
            default:
                return "UNABLE TO LOG";
        }
    }

}