package com.paxus.pay.poslinkui.demo.entry.task

import android.app.Activity
import com.paxus.pay.poslinkui.demo.utils.Logger
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

abstract class ScheduledTask protected constructor(protected val activityForContext: Activity?) :
    Runnable {
    protected var future: ScheduledFuture<*>? = null

    fun attachFuture(scheduledFuture: ScheduledFuture<*>?) {
        future = scheduledFuture
    }

    fun cancel() {
        Logger.d(javaClass.getSimpleName() + " cancelling.")
        val f = future
        if (f != null && !f.isDone) f.cancel(true)
    }

    val delay: Long
        get() = future?.getDelay(TimeUnit.MILLISECONDS) ?: 0L

    override fun toString(): String {
        val f = future
        return javaClass.simpleName + " " + if (f != null) {
            "Cancelled: " + f.isCancelled + "\tDone: " + f.isDone + "\tDelay: " + f.getDelay(
                TimeUnit.MILLISECONDS
            )
        } else {
            "NULL"
        }
    }
}
