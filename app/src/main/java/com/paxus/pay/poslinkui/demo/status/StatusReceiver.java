package com.paxus.pay.poslinkui.demo.status;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;
import java.util.Set;

/**
 * Created by Yanina.Yang on 5/18/2022.
 */
public class StatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean hasActivityRunning = appInForeground(context);
        if(!hasActivityRunning){
            Intent activityIntent = new Intent(context.getApplicationContext(), StatusActivity.class);
            activityIntent.setAction(intent.getAction());
            activityIntent.putExtras(intent);
            Set<String> categories = intent.getCategories();
            for(String cat: categories){
                activityIntent.addCategory(cat);
            }
            //MainActivity might come to front. So use Intent.FLAG_ACTIVITY_CLEAR_TASK.
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }
    }

    private boolean appInForeground(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                String[] packageList = processInfo.pkgList;
                return packageList != null && packageList.length > 0 && context.getPackageName().equals(packageList[0]);
            }
        }
        return false;
    }
}
