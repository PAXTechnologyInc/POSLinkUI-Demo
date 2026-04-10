package com.paxus.pay.poslinkui.demo.utils

import android.app.Activity
import android.os.Bundle
import com.paxus.pay.poslinkui.demo.entry.task.FinishTask
import com.paxus.pay.poslinkui.demo.entry.task.ScheduledTask
import com.paxus.pay.poslinkui.demo.entry.task.TimeoutTask
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler.TASK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Lifecycle-aware task scheduler for timeout and finish tasks.
 *
 * Uses [lifecycleScope] and coroutines for synchronous cancellation during onDestroy.
 * Supports per-requestor task mapping and replace semantics (new task replaces old for same key).
 *
 * @param activityForContext Activity context for task execution
 * @param scope [LifecycleCoroutineScope] (e.g. activity.lifecycleScope) for cancellation
 */
class TaskScheduler(
    private val activityForContext: Activity?,
    private val scope: CoroutineScope
) {
    enum class TASK {
        TIMEOUT, FINISH
    }

    private val jobMap = mutableMapOf<Pair<String, TASK>, Job>()

    /**
     * Cancels all tasks for the given [requestorId]. Call when a Fragment is destroyed.
     */
    fun cancelForRequestor(requestorId: String) {
        val keysToRemove = jobMap.keys.filter { it.first == requestorId }
        keysToRemove.forEach { key ->
            jobMap[key]?.cancel()
            jobMap.remove(key)
        }
        if (keysToRemove.isNotEmpty()) {
            Logger.d("TaskScheduler", "Cancelled tasks for requestor: $requestorId")
        }
    }

    /**
     * Cancels all scheduled tasks.
     */
    fun cancelTasks() {
        jobMap.values.forEach { it.cancel() }
        jobMap.clear()
        Logger.d("TaskScheduler", "Cancelled all tasks")
    }

    /**
     * Shuts down the scheduler. Call from Activity.onDestroy.
     */
    fun shutdown() {
        cancelTasks()
    }

    /**
     * Schedules a task (Activity-level). Uses "activity" as requestorId.
     *
     * @return The scheduled [Job] handle for grouping via [TaskGroup.addToGroup], or null if run immediately
     */
    fun schedule(taskType: TASK, delayMs: Long, initTime: Long): Job? {
        return schedule("activity", taskType, delayMs, initTime)
    }

    /**
     * Schedules a task. If a task for the same (requestorId, taskType) exists, it is replaced.
     *
     * @param requestorId Identifier for the requester (e.g. Fragment)
     * @param taskType TIMEOUT or FINISH
     * @param delayMs Delay in milliseconds
     * @param initTime Initial timestamp for delay calculation
     * @return The scheduled [Job] handle for grouping via [TaskGroup.addToGroup], or null if run immediately
     */
    fun schedule(requestorId: String, taskType: TASK, delayMs: Long, initTime: Long): Job? {
        val key = Pair(requestorId, taskType)
        jobMap[key]?.cancel()
        jobMap.remove(key)

        val remainingDelay = (initTime + delayMs) - System.currentTimeMillis()
        if (remainingDelay <= 0) {
            runTaskImmediately(taskType)
            return null
        }

        val job = scope.launch {
            delay(remainingDelay)
            if (isActive) {
                jobMap.remove(key)
                runTaskImmediately(taskType)
            }
        }
        jobMap[key] = job
        return job
    }

    private fun runTaskImmediately(taskType: TASK) {
        val task: ScheduledTask = when (taskType) {
            TASK.TIMEOUT -> TimeoutTask(activityForContext)
            TASK.FINISH -> FinishTask(activityForContext)
        }
        task.run()
    }

    companion object {
        const val SCHEDULE: String = "schedule"
        const val PARAM_DELAY: String = "delay"
        const val PARAM_TASK: String = "taskType"
        const val PARAM_INIT_TIME: String = "initTime"
        const val PARAM_REQUESTOR_ID: String = "requestorId"

        /**
         * Creates a bundle for scheduling request. Include [requestorId] for per-Fragment cancellation.
         */
        fun generateTaskRequestBundle(taskType: TASK, delay: Long, requestorId: String): Bundle {
            return BundleMaker()
                .addLong(PARAM_DELAY, delay)
                .addString(PARAM_TASK, taskType.name)
                .addLong(PARAM_INIT_TIME, System.currentTimeMillis())
                .addString(PARAM_REQUESTOR_ID, requestorId)
                .get()
        }

        /** Legacy overload without requestorId; uses "activity" as default. */
        fun generateTaskRequestBundle(taskType: TASK, delay: Long): Bundle {
            return generateTaskRequestBundle(taskType, delay, "activity")
        }
    }
}
