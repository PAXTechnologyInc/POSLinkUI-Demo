package com.paxus.pay.poslinkui.demo.entry.task

import android.app.Activity
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils
import com.paxus.pay.poslinkui.demo.utils.Logger

class TimeoutTask(activity: Activity?) : ScheduledTask(activity) {
    init {
        Logger.d(javaClass.getSimpleName() + " initializing.")
    }

    override fun run() {
        Logger.d("Running Timeout Task")
        val activity = activityForContext ?: return
        val intent = activity.intent ?: return
        EntryRequestUtils.sendTimeout(
            activity,
            intent.getStringExtra(EntryExtraData.PARAM_PACKAGE),
            intent.action
        )
    }
}
