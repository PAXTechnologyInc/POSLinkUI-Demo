package com.paxus.pay.poslinkui.demo.ui.device

import android.content.res.Configuration
import android.os.Build
import kotlin.math.min

/**
 * Single place to map hardware / configuration to [DeviceLayoutSpec] (FR-017).
 * See [specs/002-compose-migration/device-profiles.md]; verify `Build.MODEL` on real P0 units (T034).
 */
object DeviceProfileRegistry {

    val fallback: DeviceLayoutSpec = DeviceLayoutSpec(
        profileId = DeviceProfileId.STANDARD_PHONE,
        screenHorizontalPaddingDp = 20,
        screenVerticalPaddingDp = 15,
        listItemMinHeightDp = 48,
        secondaryTitleSp = 22f,
        secondaryBodySp = 16f
    )

    /**
     * @param model Usually [Build.MODEL]; overridable in unit tests.
     * @param manufacturer Usually [Build.MANUFACTURER].
     */
    fun resolve(
        configuration: Configuration,
        model: String = Build.MODEL.orEmpty(),
        manufacturer: String = Build.MANUFACTURER.orEmpty()
    ): DeviceLayoutSpec {
        val profile = resolveProfileId(configuration, model, manufacturer)
        return specForProfile(profile)
    }

    internal fun resolveProfileId(
        configuration: Configuration,
        model: String,
        @Suppress("UNUSED_PARAMETER") manufacturer: String
    ): DeviceProfileId {
        val m = model.uppercase()
        val sw = min(configuration.screenWidthDp, configuration.screenHeightDp)
        return when {
            m.contains("A3700") -> DeviceProfileId.A3700
            m.contains("A920MAX") -> DeviceProfileId.A920MAX
            m.contains("A920") && m.contains("PRO") -> DeviceProfileId.A920_CLASS
            m.contains("A920") -> DeviceProfileId.A920_CLASS
            m.contains("A35") && !m.contains("A350") -> DeviceProfileId.A35
            m.contains("A80S") || m == "A80" || m.contains("A80 ") -> DeviceProfileId.A80_CLASS
            sw >= 500 -> DeviceProfileId.A3700
            sw <= 320 -> DeviceProfileId.A35
            else -> DeviceProfileId.STANDARD_PHONE
        }
    }

    private fun specForProfile(profile: DeviceProfileId): DeviceLayoutSpec = when (profile) {
        DeviceProfileId.A3700 -> DeviceLayoutSpec(
            profileId = profile,
            screenHorizontalPaddingDp = 20,
            screenVerticalPaddingDp = 15,
            listItemMinHeightDp = 52,
            secondaryTitleSp = 24f,
            secondaryBodySp = 17f
        )
        DeviceProfileId.A920MAX,
        DeviceProfileId.A920_CLASS -> DeviceLayoutSpec(
            profileId = profile,
            screenHorizontalPaddingDp = 20,
            screenVerticalPaddingDp = 12,
            listItemMinHeightDp = 48,
            secondaryTitleSp = 22f,
            secondaryBodySp = 16f
        )
        DeviceProfileId.A35,
        DeviceProfileId.A80_CLASS -> DeviceLayoutSpec(
            profileId = profile,
            screenHorizontalPaddingDp = 14,
            screenVerticalPaddingDp = 8,
            listItemMinHeightDp = 44,
            secondaryTitleSp = 20f,
            secondaryBodySp = 14f
        )
        DeviceProfileId.STANDARD_PHONE -> fallback.copy(profileId = profile)
    }
}
