package com.paxus.pay.poslinkui.demo.utils

import javax.inject.Inject
import javax.inject.Singleton
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min

/**
 * Simplified Thread Pool Manager - Optimized for IO-intensive tasks
 */
@Singleton
class ThreadPoolManager @Inject constructor() {
    private val threadPool: ExecutorService = run {
        Logger.d("ThreadPoolManager", "Initializing IO-intensive thread pool")
        val workQueue: BlockingQueue<Runnable?> = LinkedBlockingQueue(QUEUE_SIZE)
        ThreadPoolExecutor(
            CORE_POOL_SIZE,  // Core pool size
            MAXIMUM_POOL_SIZE,  // Maximum pool size
            KEEP_ALIVE_SECONDS.toLong(),  // Keep alive time for idle threads
            TimeUnit.SECONDS,  // Time unit
            workQueue,  // Work queue
            PriorityThreadFactory("io-pool"),
            ThreadPoolExecutor.CallerRunsPolicy()
        ).also {
            Logger.d("ThreadPoolManager", "Thread pool initialized: core=$CORE_POOL_SIZE max=$MAXIMUM_POOL_SIZE queue=$QUEUE_SIZE")
        }
    }

    /**
     * Executes a task. Falls back to caller thread if rejected (CallerRunsPolicy).
     */
    fun execute(task: Runnable?) {
        if (task == null) {
            Logger.e("ThreadPoolManager", "Cannot execute a null task")
            return
        }
        try {
            threadPool.execute(task)
        } catch (e: RejectedExecutionException) {
            Logger.e("ThreadPoolManager", "event=rejected, fallback=caller, error=${e.message}")
            task.run()
        }
    }

    /**
     * Submits a Callable task. Returns null if rejected.
     */
    fun <T> submit(task: Callable<T?>?): Future<T?>? {
        if (task == null) return null
        return try {
            threadPool.submit(task)
        } catch (e: RejectedExecutionException) {
            Logger.e("ThreadPoolManager", "event=rejected, type=Callable, error=${e.message}")
            null
        }
    }

    /**
     * Submits a Runnable task. Returns null if rejected.
     */
    fun submit(task: Runnable?): Future<*>? {
        if (task == null) return null
        return try {
            threadPool.submit(task)
        } catch (e: RejectedExecutionException) {
            Logger.e("ThreadPoolManager", "event=rejected, type=Runnable, error=${e.message}")
            null
        }
    }


    /**
     * Custom thread factory - Set thread priority
     */
    private class PriorityThreadFactory(private val namePrefix: String?) : ThreadFactory {
        private val counter = AtomicInteger(0)

        override fun newThread(r: Runnable?): Thread {
            val thread = Thread(r, namePrefix + "-thread-" + counter.incrementAndGet())
            thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)
            Logger.d("ThreadPoolManager", "Created new thread: " + thread.getName())
            return thread
        }
    }

    companion object {
        // Default configuration constants
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        private val CORE_POOL_SIZE = max(2, min(CPU_COUNT - 1, 4))
        private val MAXIMUM_POOL_SIZE: Int = CPU_COUNT * 2 + 1
        private const val KEEP_ALIVE_SECONDS = 30
        private const val QUEUE_SIZE = 128
    }
}