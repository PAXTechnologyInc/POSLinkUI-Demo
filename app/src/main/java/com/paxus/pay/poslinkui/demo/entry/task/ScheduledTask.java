package com.paxus.pay.poslinkui.demo.entry.task;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class ScheduledTask implements Runnable {

    protected ScheduledFuture future;
    protected final Activity activityForContext;

    protected ScheduledTask(Activity activity) {
        activityForContext = activity;
    }
    public void attachFuture(ScheduledFuture scheduledFuture){
        future = scheduledFuture;
    }

    public void cancel(){
        Logger.d(getClass().getSimpleName() + " cancelling.");
        if(future != null && !future.isDone()) future.cancel(true);
    }

    public long getDelay() {
        return future.getDelay(TimeUnit.MILLISECONDS);
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + ((future != null) ? ("Cancelled: " + future.isCancelled() + "\tDone: " + future.isDone() + "\tDelay: " + future.getDelay(TimeUnit.MILLISECONDS)) : "NULL");
    }
}
