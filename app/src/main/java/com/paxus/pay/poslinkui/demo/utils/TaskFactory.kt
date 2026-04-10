package com.paxus.pay.poslinkui.demo.utils

import android.app.Activity
import com.paxus.pay.poslinkui.demo.entry.task.FinishTask
import com.paxus.pay.poslinkui.demo.entry.task.ScheduledTask
import com.paxus.pay.poslinkui.demo.entry.task.TimeoutTask
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler.TASK
import javax.inject.Inject

/**
 * Creates scheduled tasks without reflection to keep failure paths explicit and testable.
 */
class TaskFactory @Inject constructor() {
    fun create(taskType: TASK, activity: Activity?): ScheduledTask {
        return when (taskType) {
            TASK.TIMEOUT -> TimeoutTask(activity)
            TASK.FINISH -> FinishTask(activity)
        }
    }
}
