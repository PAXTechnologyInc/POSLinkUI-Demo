package com.paxus.pay.poslinkui.demo.utils

/**
 * Logging for animation downgrade triggers and key performance events (FR-008, T024).
 * Supports locating thermal and high-CPU sources.
 */
object AnimationLogger {
    private const val TAG = "Animation"

    fun logDowngrade(animationPoint: String, reason: String, policy: AnimationPolicy) {
        Logger.d("$TAG downgrade point=$animationPoint reason=$reason policy=$policy")
    }

    fun logPerformanceEvent(event: String, durationMs: Long?, extra: String? = null) {
        val msg = buildString {
            append("$TAG perf event=$event")
            durationMs?.let { append(" durationMs=$it") }
            extra?.let { append(" $it") }
        }
        Logger.d(msg)
    }
}
