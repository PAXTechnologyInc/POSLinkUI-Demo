package com.paxus.pay.poslinkui.demo.utils;

import android.app.Activity;
import android.os.Bundle;

import com.paxus.pay.poslinkui.demo.entry.task.FinishTask;
import com.paxus.pay.poslinkui.demo.entry.task.ScheduledTask;
import com.paxus.pay.poslinkui.demo.entry.task.TimeoutTask;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {

//Static Members
    public static final String SCHEDULE = "schedule", PARAM_DELAY = "delay", PARAM_TASK = "taskType", PARAM_INIT_TIME = "initTime";
    public enum TASK {TIMEOUT, FINISH}

    private static Map<TASK, Class> TaskClassMap = new HashMap<TASK, Class>(){{
        put(TASK.TIMEOUT, TimeoutTask.class);
        put(TASK.FINISH, FinishTask.class);
    }};

    //This creates bundle necessary to send scheduling request from fragment to entryactivity
    public static Bundle generateTaskRequestBundle(TASK taskType, long delay){
        return new BundleMaker().addLong(PARAM_DELAY, delay).addString(PARAM_TASK, taskType.name()).addLong(PARAM_INIT_TIME, System.currentTimeMillis()).get();
    }

//Instance
    private Activity activityForContext;
    ScheduledExecutorService scheduledExecutorService;
    private Map<TASK, ScheduledTask> scheduledTaskMap = new HashMap<>();

    public TaskScheduler(Activity activity){
        activityForContext = activity;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void shutdown(){
        cancelTasks();
        scheduledExecutorService.shutdownNow();
    }

    public void cancelTask(TASK taskType){
        if(scheduledTaskMap.containsKey(taskType.name())){
            scheduledTaskMap.get(taskType.name()).cancel();
            scheduledTaskMap.remove(taskType.name());
        }
    }
    public void cancelTasks() {
        for(Map.Entry<TASK, ScheduledTask> scheduledTaskEntry : scheduledTaskMap.entrySet()){
            scheduledTaskEntry.getValue().cancel();
        }
        scheduledTaskMap.clear();
    }

    public void schedule(TASK taskType, long delay, long initTime){
        if(scheduledTaskMap.containsKey(taskType)){
            if(scheduledTaskMap.get(taskType).getDelay() < delay){
                cancelTask(taskType);
            } else {
                return;
            }
        }
        scheduledTaskMap.put(taskType, scheduleNewTask(activityForContext, taskType, delay, initTime));
    }

    private ScheduledTask scheduleNewTask(Activity activity, TASK taskType, long delay, long initTime){
        ScheduledTask scheduledTask = null;
        try {
            scheduledTask = (ScheduledTask) TaskClassMap.get(taskType).getConstructor(Activity.class).newInstance(activity);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            Logger.e(e);
        }
        ScheduledFuture<?> future = scheduledExecutorService.schedule(scheduledTask, System.currentTimeMillis() - initTime + delay, TimeUnit.MILLISECONDS);
        scheduledTask.attachFuture(future);
        return scheduledTask;
    }

}
