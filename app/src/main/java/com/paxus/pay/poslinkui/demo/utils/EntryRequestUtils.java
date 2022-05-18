package com.paxus.pay.poslinkui.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pax.us.pay.ui.constant.entry.EntryRequest;

/**
 * Utils for handle EntryRequest
 */
public class EntryRequestUtils {
    public static void sendNext(Context context, String packageName, String action, String param, String value){
        Logger.i("send Entry Request ACTION_NEXT from action  \""+action+"\"");

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        bundle.putString(param,value);

        Intent intent = new Intent(EntryRequest.ACTION_NEXT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }

    public static void sendNext(Context context, String packageName, String action, String param, long value){
        Logger.i("send Entry Request ACTION_NEXT from action  \""+action+"\"");

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        bundle.putLong(param,value);

        Intent intent = new Intent(EntryRequest.ACTION_NEXT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }

    public static void sendNext(Context context, String packageName, String action, String param, int value){
        Logger.i("send Entry Request ACTION_NEXT from action  \""+action+"\"");

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        bundle.putInt(param,value);

        Intent intent = new Intent(EntryRequest.ACTION_NEXT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }

    public static void sendNext(Context context, String packageName, String action, String param, boolean value){
        Logger.i("send Entry Request ACTION_NEXT from action  \""+action+"\"");
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        bundle.putBoolean(param, value);

        Intent intent = new Intent(EntryRequest.ACTION_NEXT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }

    public static void sendNext(Context context, String packageName, String action, String param, short[] value){
        Logger.i("send Entry Request ACTION_NEXT from action  \""+action+"\"");

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        bundle.putShortArray(param, value);

        Intent intent = new Intent(EntryRequest.ACTION_NEXT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }

    public static void sendNext(Context context, String packageName, String action){
        Logger.i("send Entry Request ACTION_NEXT from action  \""+action+"\"");

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);

        Intent intent = new Intent(EntryRequest.ACTION_NEXT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }

    public static void sendNextAVS(Context context, String packageName, String action, String address, String zip){
        Logger.i("send Entry Request ACTION_NEXT from action  \""+action+"\"");

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        bundle.putString(EntryRequest.PARAM_ADDRESS,address);
        bundle.putString(EntryRequest.PARAM_ZIP_CODE,zip);

        Intent intent = new Intent(EntryRequest.ACTION_NEXT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }

    public static void sendTimeout(Context context, String packageName, String action){
        Logger.i("Entry Request:ACTION_TIME_OUT \""+action+"\"");

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);

        Intent intent = new Intent(EntryRequest.ACTION_TIME_OUT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }

    public static void sendAbort(Context context, String packageName, String action){
        Logger.i("Entry Request:ACTION_ABORT \""+action+"\"");

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);

        Intent intent = new Intent(EntryRequest.ACTION_ABORT);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }

    public static void sendSecureArea(Context context, String packageName, String action,
                                      int x, int y, int width, int height, int fontSize, String hint,String fontColor){
        Logger.i("send Entry Request ACTION_SECURITY_AREA for action \""+action+"\"");

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);
        bundle.putInt(EntryRequest.PARAM_X, x);
        bundle.putInt(EntryRequest.PARAM_Y, y);
        bundle.putInt(EntryRequest.PARAM_WIDTH, width);
        bundle.putInt(EntryRequest.PARAM_HEIGHT, height);
        bundle.putInt(EntryRequest.PARAM_FONT_SIZE, fontSize);
        bundle.putString(EntryRequest.PARAM_HINT, hint);
        bundle.putString(EntryRequest.PARAM_COLOR, fontColor);

        Intent intent = new Intent(EntryRequest.ACTION_SECURITY_AREA);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);

    }

    /**
     * For {@link com.pax.us.pay.ui.constant.entry.SecurityEntry#ACTION_ENTER_PIN} use only
     * For this action, EntryRequest.ACTION_SECURITY_AREA is just used to tell BroadPOS you are ready.
     * @param context Context
     * @param packageName package name
     * @param action action name
     */
    public static void sendSecureArea(Context context, String packageName, String action){
        Logger.i("send Entry Request ACTION_SECURITY_AREA for action \""+action+"\"");

        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, action);

        Intent intent = new Intent(EntryRequest.ACTION_SECURITY_AREA);
        intent.putExtras(bundle);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }
}
