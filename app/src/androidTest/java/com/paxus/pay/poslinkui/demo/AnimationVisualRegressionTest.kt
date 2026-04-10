package com.paxus.pay.poslinkui.demo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation test skeleton for animation visual regression (FR-006a).
 *
 * Covers current animation points A1-A6:
 * - A1: MainActivity fragment transition
 * - A2: Toast transition
 * - A3: CLSS lights blink / steady fallback
 * - A4: Second screen border GIF
 * - A5: Approval GIF / text fallback
 * - A6: Receipt preview crossfade
 *
 * TODO: Add assertions for visibility, alpha, translation per animation point
 * after migration. Run with `./gradlew connectedAndroidTest`.
 */
@RunWith(AndroidJUnit4::class)
class AnimationVisualRegressionTest {

    @Test
    fun appContext_exists() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull(appContext)
    }

    /**
     * Main-flow animation points regression placeholder.
     * After migration: add assertions for transitions, CLSS blink, GIF visibility, and crossfade policy.
     */
    @Test
    fun animationPoints_resourcesExist() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull(ctx.resources.getResourceEntryName(R.anim.anim_enter_from_bottom))
        assertNotNull(ctx.resources.getResourceEntryName(R.raw.border_animated))
        assertNotNull(ctx.resources.getResourceEntryName(R.drawable.mastercard_approval))
    }
}
