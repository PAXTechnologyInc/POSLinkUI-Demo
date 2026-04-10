package com.paxus.pay.poslinkui.demo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Skeleton test to host memory verification scenarios.
 */
@RunWith(AndroidJUnit4::class)
class MemoryVerificationTest {
    @Test
    fun instrumentationContext_available() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull(appContext)
    }
}
