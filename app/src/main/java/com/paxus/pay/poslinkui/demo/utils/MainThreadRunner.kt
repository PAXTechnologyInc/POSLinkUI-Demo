package com.paxus.pay.poslinkui.demo.utils

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * Unified entry point for posting work to the main thread.
 *
 * Replaces scattered [Handler] usage to avoid wrong Looper, ensure lifecycle safety,
 * and improve testability. Per task-management-contract.md.
 *
 * @see post
 * @see postWhenAlive
 * @see postDelayed
 */
object MainThreadRunner {
    private val mainHandler = Handler(Looper.getMainLooper())

    /**
     * Posts [runnable] to the main thread. Executes regardless of lifecycle state.
     *
     * @param runnable The work to run on the main thread
     */
    fun post(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            mainHandler.post(runnable)
        }
    }

    /**
     * Posts [runnable] to the main thread. If [lifecycleOwner] is DESTROYED when the
     * runnable runs, it is skipped (no crash, no leak).
     *
     * @param lifecycleOwner The lifecycle to check; if DESTROYED, runnable is skipped
     * @param runnable The work to run on the main thread
     */
    fun postWhenAlive(lifecycleOwner: LifecycleOwner, runnable: Runnable) {
        mainHandler.post {
            if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                return@post
            }
            runnable.run()
        }
    }

    /**
     * Posts [runnable] to the main thread after [delayMs] milliseconds.
     *
     * @param delayMs Delay in milliseconds before execution
     * @param runnable The work to run on the main thread
     */
    fun postDelayed(delayMs: Long, runnable: Runnable) {
        mainHandler.postDelayed(runnable, delayMs)
    }

    /**
     * Posts [runnable] to the main thread after [delayMs] milliseconds.
     * If [lifecycleOwner] is DESTROYED when the runnable runs, it is skipped.
     *
     * @param lifecycleOwner The lifecycle to check; if DESTROYED, runnable is skipped
     * @param delayMs Delay in milliseconds before execution
     * @param runnable The work to run on the main thread
     */
    fun postDelayedWhenAlive(lifecycleOwner: LifecycleOwner, delayMs: Long, runnable: Runnable) {
        mainHandler.postDelayed({
            if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                return@postDelayed
            }
            runnable.run()
        }, delayMs)
    }

    /**
     * Convenience overload: posts [block] to the main thread.
     */
    fun post(block: () -> Unit) {
        post(Runnable(block))
    }
}
