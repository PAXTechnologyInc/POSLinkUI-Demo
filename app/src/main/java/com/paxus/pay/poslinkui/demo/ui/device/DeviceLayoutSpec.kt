package com.paxus.pay.poslinkui.demo.ui.device

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Layout dimensions for the current device profile (FR-017). Values are **dp/sp as integers** where noted;
 * Composables use extension accessors that turn them into [Dp].
 *
 * @param profileId Resolved profile key for logging / debugging.
 * @param screenHorizontalPaddingDp Horizontal padding for screen root / Nav destinations.
 * @param screenVerticalPaddingDp Vertical padding for screen root.
 * @param listItemMinHeightDp Minimum touch height for list / form rows.
 * @param secondaryTitleSp Title text size on secondary display (Presentation), in sp.
 * @param secondaryBodySp Body text size on secondary display, in sp.
 */
data class DeviceLayoutSpec(
    val profileId: DeviceProfileId,
    val screenHorizontalPaddingDp: Int,
    val screenVerticalPaddingDp: Int,
    val listItemMinHeightDp: Int,
    val secondaryTitleSp: Float,
    val secondaryBodySp: Float
) {
    val screenHorizontalPadding: Dp get() = screenHorizontalPaddingDp.dp
    val screenVerticalPadding: Dp get() = screenVerticalPaddingDp.dp
    val listItemMinHeight: Dp get() = listItemMinHeightDp.dp
}

/**
 * Provides [DeviceLayoutSpec] for the composition subtree (T031). Defaults to [DeviceProfileRegistry.fallback].
 */
val LocalDeviceLayoutSpec = staticCompositionLocalOf { DeviceProfileRegistry.fallback }
