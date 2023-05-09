package com.paxus.pay.poslinkui.demo.entry.task;

import android.app.Activity;
import android.content.Intent;

import com.paxus.pay.poslinkui.demo.utils.Logger;

public class GoHomeTask extends ScheduledTask {


    public GoHomeTask(Activity activity) {
        super(activity);
        Logger.d(getClass().getSimpleName() + " initializing.");
    }

    @Override
    public void run() {
        Logger.d("Running GoHome Task");
        activityForContext.sendBroadcast(new Intent("com.pax.us.pay.GO_HOME"));
    }

}
