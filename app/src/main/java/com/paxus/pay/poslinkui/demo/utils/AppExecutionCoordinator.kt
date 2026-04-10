package com.paxus.pay.poslinkui.demo.utils

import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Job

/**
 * Injectable facade that centralizes background/main-thread execution entry points.
 */
@Singleton
class AppExecutionCoordinator @Inject constructor(
    private val threadPoolManager: ThreadPoolManager,
    private val backgroundTaskRunner: BackgroundTaskRunner
) {
    fun executeBackground(task: Runnable?) {
        threadPoolManager.execute(task)
    }

    fun <T> executeLifecycleBound(
        lifecycleOwner: LifecycleOwner?,
        block: suspend () -> T,
        onResult: (T) -> Unit
    ): Job {
        return backgroundTaskRunner.execute(lifecycleOwner, block, onResult)
    }

    fun postMain(runnable: Runnable) {
        MainThreadRunner.post(runnable)
    }
}
