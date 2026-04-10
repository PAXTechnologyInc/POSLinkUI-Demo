package com.paxus.pay.poslinkui.demo.ui.device

import android.content.res.Configuration
import org.junit.Assert.assertEquals
import org.junit.Test

class DeviceProfileRegistryTest {

    @Test
    fun resolveProfileId_a3700_by_model() {
        val c = Configuration().apply {
            screenWidthDp = 400
            screenHeightDp = 800
        }
        assertEquals(
            DeviceProfileId.A3700,
            DeviceProfileRegistry.resolveProfileId(c, "PAX_A3700", "")
        )
    }

    @Test
    fun resolveProfileId_a35_by_model() {
        val c = Configuration().apply {
            screenWidthDp = 400
            screenHeightDp = 800
        }
        assertEquals(
            DeviceProfileId.A35,
            DeviceProfileRegistry.resolveProfileId(c, "A35", "")
        )
    }

    @Test
    fun resolveProfileId_small_sw_defaults_to_a35_bucket() {
        val c = Configuration().apply {
            screenWidthDp = 300
            screenHeightDp = 480
        }
        assertEquals(
            DeviceProfileId.A35,
            DeviceProfileRegistry.resolveProfileId(c, "UNKNOWN", "")
        )
    }
}
