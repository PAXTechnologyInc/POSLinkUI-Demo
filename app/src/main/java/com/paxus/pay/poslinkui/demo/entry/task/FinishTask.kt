package com.paxus.pay.poslinkui.demo.entry.task

import android.app.Activity
import com.paxus.pay.poslinkui.demo.utils.Logger

class FinishTask(activity: Activity?) : ScheduledTask(activity) {
    init {
        Logger.d(javaClass.getSimpleName() + " initializing.")
    }

    override fun run() {
        Logger.d("Running Finish Task")
        activityForContext?.finishAndRemoveTask()
    }
}
