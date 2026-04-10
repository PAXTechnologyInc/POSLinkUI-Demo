package com.paxus.pay.poslinkui.demo.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AnimationSupportTest {
    @Test
    fun `minimal policy disables non-essential image motion`() {
        assertFalse(AnimationSupport.shouldShowSecondScreenGif(AnimationPolicy.MINIMAL))
        assertFalse(AnimationSupport.shouldAnimateApprovalGif(AnimationPolicy.MINIMAL))
        assertFalse(AnimationSupport.shouldUseReceiptPreviewCrossfade(AnimationPolicy.MINIMAL))
    }

    @Test
    fun `reduced policy keeps motion with lower cost`() {
        assertTrue(AnimationSupport.shouldShowSecondScreenGif(AnimationPolicy.REDUCED))
        assertTrue(AnimationSupport.shouldAnimateApprovalGif(AnimationPolicy.REDUCED))
        assertEquals(800L, AnimationSupport.clssBlinkDurationMs(AnimationPolicy.REDUCED))
        assertEquals(560, AnimationSupport.secondScreenGifSizePx(AnimationPolicy.REDUCED))
        assertEquals(840, AnimationSupport.approvalGifSizePx(AnimationPolicy.REDUCED))
    }

    @Test
    fun `approval delays match policy and fallback state`() {
        assertEquals(3500L, AnimationSupport.approvalDisplayDurationMs(AnimationPolicy.STANDARD, true))
        assertEquals(2200L, AnimationSupport.approvalDisplayDurationMs(AnimationPolicy.REDUCED, true))
        assertEquals(3000L, AnimationSupport.approvalDisplayDurationMs(AnimationPolicy.REDUCED, false))
        assertEquals(1500L, AnimationSupport.approvalDisplayDurationMs(AnimationPolicy.MINIMAL, false))
    }
}
