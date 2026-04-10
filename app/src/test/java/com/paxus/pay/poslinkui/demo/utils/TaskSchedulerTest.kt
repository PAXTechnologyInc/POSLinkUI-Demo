package com.paxus.pay.poslinkui.demo.utils

import com.paxus.pay.poslinkui.demo.entry.task.FinishTask
import com.paxus.pay.poslinkui.demo.entry.task.TimeoutTask
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskSchedulerTest {
    private val factory = TaskFactory()

    @Test
    fun taskFactory_createsTimeoutTask() {
        val task = factory.create(TaskScheduler.TASK.TIMEOUT, null)
        assertTrue(task is TimeoutTask)
    }

    @Test
    fun taskFactory_createsFinishTask() {
        val task = factory.create(TaskScheduler.TASK.FINISH, null)
        assertTrue(task is FinishTask)
    }
}
