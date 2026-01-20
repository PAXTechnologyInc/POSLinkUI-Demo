package com.paxus.pay.poslinkui.demo.utils;

import android.os.Process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.paxus.pay.poslinkui.demo.utils.Logger;

/**
 * Simplified Thread Pool Manager - Optimized for IO-intensive tasks
 */
public class ThreadPoolManager {

    // Singleton instance
    private static volatile ThreadPoolManager instance;

    // Thread pool
    private ExecutorService threadPool;

    // Default configuration constants
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final int QUEUE_SIZE = 128;

    private ThreadPoolManager() {
        Logger.d("ThreadPoolManager", "Initializing IO-intensive thread pool");
        initializeThreadPool();
        Logger.d("ThreadPoolManager", "Thread pool initialized with core size: " + CORE_POOL_SIZE + ", max size: " + MAXIMUM_POOL_SIZE + ", queue size: " + QUEUE_SIZE);
    }

    /**
     * Get singleton instance
     */
    public static ThreadPoolManager getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolManager.class) {
                if (instance == null) {
                    Logger.d("ThreadPoolManager", "Creating singleton instance");
                    instance = new ThreadPoolManager();
                }
            }
        }
        return instance;
    }

    /**
     * Initialize IO-intensive thread pool
     */
    private void initializeThreadPool() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);

        threadPool = new ThreadPoolExecutor(
                CORE_POOL_SIZE,           // Core pool size
                MAXIMUM_POOL_SIZE,        // Maximum pool size
                KEEP_ALIVE_SECONDS,       // Keep alive time for idle threads
                TimeUnit.SECONDS,         // Time unit
                workQueue,               // Work queue
                new PriorityThreadFactory("io-pool"), // Thread factory
                new ThreadPoolExecutor.CallerRunsPolicy() // Rejection policy
        );
        Logger.d("ThreadPoolManager", "Thread pool created successfully");
    }


    /**
     * Executes a task. Re-initializes the pool automatically if needed.
     */
    public void execute(Runnable task) {
        if (task == null) {
            Logger.e("ThreadPoolManager", "Cannot execute a null task");
            return;
        }

        try {
            threadPool.execute(task);
        } catch (RejectedExecutionException e) {
            Logger.e("ThreadPoolManager", "Task rejected: " + e.getMessage());
            task.run(); // Fallback to current thread
        }
    }

    /**
     * Submits a Callable task. Re-initializes the pool automatically if needed.
     */
    public <T> Future<T> submit(Callable<T> task) {
        if (task == null) return null;


        try {
            return threadPool.submit(task);
        } catch (RejectedExecutionException e) {
            Logger.e("ThreadPoolManager", "Callable task rejected: " + e.getMessage());
            return null;
        }
    }

    /**
     * Submits a Runnable task. Re-initializes the pool automatically if needed.
     */
    public Future<?> submit(Runnable task) {
        if (task == null) return null;

        try {
            return threadPool.submit(task);
        } catch (RejectedExecutionException e) {
            Logger.e("ThreadPoolManager", "Runnable task rejected: " + e.getMessage());
            return null;
        }
    }


    /**
     * Custom thread factory - Set thread priority
     */
    private static class PriorityThreadFactory implements ThreadFactory {
        private final AtomicInteger counter = new AtomicInteger(0);
        private final String namePrefix;

        public PriorityThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, namePrefix + "-thread-" + counter.incrementAndGet());
            thread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Logger.d("ThreadPoolManager", "Created new thread: " + thread.getName());
            return thread;
        }
    }
}