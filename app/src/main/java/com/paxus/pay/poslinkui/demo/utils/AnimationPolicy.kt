package com.paxus.pay.poslinkui.demo.utils

/**
 * Animation policy profile (FR-007, T015).
 * Controls animation intensity for performance and thermal management.
 */
enum class AnimationPolicy {
    /** Full animations, performance-optimized implementation */
    STANDARD,
    /** Reduced duration, fewer concurrent animations */
    REDUCED,
    /** Minimal or no non-essential animations */
    MINIMAL
}

/** Current policy; defaults to STANDARD. Set by thermal/settings when needed. */
var currentAnimationPolicy: AnimationPolicy = AnimationPolicy.STANDARD
