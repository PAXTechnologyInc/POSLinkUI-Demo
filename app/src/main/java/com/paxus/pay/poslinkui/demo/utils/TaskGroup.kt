package com.paxus.pay.poslinkui.demo.utils

import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Groups multiple cancellable tasks for coordinated cancellation.
 *
 * Use when a screen schedules both TIMEOUT and FINISH (or other related tasks)
 * so they can be cancelled together on user cancel or navigation.
 *
 * @see createGroup
 * @see addToGroup
 * @see cancelGroup
 */
@Singleton
class TaskGroup @Inject constructor() {

    /**
     * Creates a new empty task group.
     *
     * @return A new mutable group to which handles can be added
     */
    fun createGroup(): MutableSet<Job> = mutableSetOf()

    /**
     * Adds a task handle to the group.
     *
     * @param group The group (from [createGroup])
     * @param handle The job/handle to add; will be cancelled when [cancelGroup] is called
     */
    fun addToGroup(group: MutableSet<Job>, handle: Job) {
        group.add(handle)
    }

    /**
     * Cancels all tasks in the group and clears it.
     *
     * @param group The group to cancel
     */
    fun cancelGroup(group: MutableSet<Job>) {
        val count = group.size
        group.forEach { it.cancel() }
        group.clear()
        if (count > 0) {
            Logger.d("TaskGroup", "Cancelled $count tasks in group")
        }
    }
}
