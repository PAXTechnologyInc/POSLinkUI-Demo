package com.paxus.pay.poslinkui.demo.entry.task;

import android.app.Activity;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;

public class TimeoutTask extends ScheduledTask {


    public TimeoutTask(Activity activity) {
        super(activity);
        Logger.d(getClass().getSimpleName() + " initializing.");
    }

    @Override
    public void run() {
        Logger.d("Running Timeout Task");
        EntryRequestUtils.sendTimeout(activityForContext, activityForContext.getIntent().getStringExtra(EntryExtraData.PARAM_PACKAGE), activityForContext.getIntent().getAction());
    }

}
