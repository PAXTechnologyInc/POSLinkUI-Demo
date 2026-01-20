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
     * Initialize with custom configuration
     *
     * @param corePoolSize Core thread count
     * @param maximumPoolSize Maximum thread count
     * @param keepAliveTime Keep alive time for idle threads
     * @param unit Time unit
     * @param queueSize Queue size
     */
    public static void initializeWithCustomConfig(int corePoolSize, int maximumPoolSize,
                                                  long keepAliveTime, TimeUnit unit, int queueSize) {
        if (instance != null) {
            throw new IllegalStateException("ThreadPoolManager already initialized");
        }

        synchronized (ThreadPoolManager.class) {
            if (instance == null) {
                Logger.d("ThreadPoolManager", "Initializing with custom configuration: core=" + corePoolSize + ", max=" + maximumPoolSize + ", keepAlive=" + keepAliveTime + ", queueSize=" + queueSize);
                instance = new ThreadPoolManager(corePoolSize, maximumPoolSize,
                        keepAliveTime, unit, queueSize);
                Logger.d("ThreadPoolManager", "Custom configuration initialized successfully");
            }
        }
    }

    /**
     * Private constructor - for custom configuration
     */
    private ThreadPoolManager(int corePoolSize, int maximumPoolSize,
                              long keepAliveTime, TimeUnit unit, int queueSize) {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(queueSize);

        threadPool = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                new PriorityThreadFactory("custom-io-pool"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        Logger.d("ThreadPoolManager", "Custom thread pool created with core=" + corePoolSize + ", max=" + maximumPoolSize + ", keepAlive=" + keepAliveTime + ", queueSize=" + queueSize);
    }

    /**
     * Ensures the thread pool is initialized and active.
     * If the pool has been shut down or is null, it triggers re-initialization.
     */
    private void ensurePoolActive() {
        if (threadPool == null || threadPool.isShutdown() || threadPool.isTerminated()) {
            synchronized (ThreadPoolManager.class) {
                // Double-checked locking to prevent redundant initializations
                if (threadPool == null || threadPool.isShutdown() || threadPool.isTerminated()) {
                    Logger.d("ThreadPoolManager", "Thread pool is inactive. Re-initializing pool instance.");
                    initializeThreadPool();
                }
            }
        }
    }

    /**
     * Executes a task. Re-initializes the pool automatically if needed.
     */
    public void execute(Runnable task) {
        if (task == null) {
            Logger.e("ThreadPoolManager", "Cannot execute a null task");
            return;
        }

        ensurePoolActive();

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

        ensurePoolActive();

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

        ensurePoolActive();

        try {
            return threadPool.submit(task);
        } catch (RejectedExecutionException e) {
            Logger.e("ThreadPoolManager", "Runnable task rejected: " + e.getMessage());
            return null;
        }
    }

    /**
     * Graceful shutdown - Stop accepting new tasks, wait for submitted tasks to complete
     */
    public void shutdownGracefully() {
        Logger.d("ThreadPoolManager", "Starting graceful shutdown");
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdown();
            Logger.d("ThreadPoolManager", "Thread pool shutdown initiated");
        } else {
            Logger.d("ThreadPoolManager", "Thread pool already shutdown or null");
        }
    }

    /**
     * Immediate shutdown - Attempt to interrupt executing tasks
     */
    public void shutdownNow() {
        Logger.d("ThreadPoolManager", "Starting immediate shutdown");
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdownNow();
            Logger.d("ThreadPoolManager", "Thread pool shutdownNow initiated");
        } else {
            Logger.d("ThreadPoolManager", "Thread pool already shutdown or null");
        }
    }

    /**
     * Check if terminated
     */
    public boolean isTerminated() {
        boolean isTerminated = threadPool != null && threadPool.isTerminated();
        Logger.d("ThreadPoolManager", "Thread pool is terminated: " + isTerminated);
        return isTerminated;
    }

    /**
     * Get active thread count
     */
    public int getActiveCount() {
        if (threadPool instanceof ThreadPoolExecutor) {
            int activeCount = ((ThreadPoolExecutor) threadPool).getActiveCount();
            Logger.d("ThreadPoolManager", "Active thread count: " + activeCount);
            return activeCount;
        }
        Logger.d("ThreadPoolManager", "Thread pool is not a ThreadPoolExecutor, returning 0");
        return 0;
    }

    /**
     * Get pool size
     */
    public int getPoolSize() {
        if (threadPool instanceof ThreadPoolExecutor) {
            int poolSize = ((ThreadPoolExecutor) threadPool).getPoolSize();
            Logger.d("ThreadPoolManager", "Pool size: " + poolSize);
            return poolSize;
        }
        Logger.d("ThreadPoolManager", "Thread pool is not a ThreadPoolExecutor, returning 0");
        return 0;
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