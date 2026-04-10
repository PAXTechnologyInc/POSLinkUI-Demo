package com.paxus.pay.poslinkui.demo.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Optional utility for lifecycle-bound background work with main-thread callback.
 *
 * Use when a task must run off the main thread and deliver results on the main thread,
 * and must be cancelled when the [LifecycleOwner] is destroyed.
 *
 * For caller-managed tasks (e.g. print check), use [ThreadPoolManager] directly.
 */
@Singleton
class BackgroundTaskRunner @Inject constructor() {

    /**
     * Executes [block] on a background thread. When complete, [onResult] runs on the main thread.
     * If [lifecycleOwner] is provided and destroyed before completion, the task is cancelled
     * and [onResult] is NOT called.
     *
     * @param lifecycleOwner Optional; when destroyed, cancels the task and skips callback
     * @param block The work to run off the main thread
     * @param onResult Called on the main thread with the result; skipped if cancelled
     * @return Job for explicit cancellation
     */
    fun <T> execute(
        lifecycleOwner: LifecycleOwner?,
        block: suspend () -> T,
        onResult: (T) -> Unit
    ): Job {
        val scope = lifecycleOwner?.lifecycleScope ?: CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        return scope.launch {
            try {
                val result = withContext(Dispatchers.IO) { block() }
                if (isActive) {
                    withContext(Dispatchers.Main.immediate) {
                        if (isActive) onResult(result)
                    }
                }
            } catch (e: Exception) {
                Logger.e("BackgroundTaskRunner", "Task failed, event=failed, error=${e.message}")
            }
        }
    }
}
