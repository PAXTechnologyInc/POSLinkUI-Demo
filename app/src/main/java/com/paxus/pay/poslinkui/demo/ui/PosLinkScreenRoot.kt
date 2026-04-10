package com.paxus.pay.poslinkui.demo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paxus.pay.poslinkui.demo.ui.device.LocalDeviceLayoutSpec
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens

/**
 * Standard root for Nav destinations and standalone Compose screens (T030 / FR-016).
 * Expects an outer [com.paxus.pay.poslinkui.demo.ui.theme.PosLinkTheme]; applies [LocalDeviceLayoutSpec] padding and [Surface].
 *
 * @param modifier Modifier for the outer surface.
 * @param content Inner content; receives padding already applied for horizontal/vertical insets from spec.
 */
@Composable
fun PosLinkScreenRoot(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val spec = LocalDeviceLayoutSpec.current
    Surface(
        modifier = modifier.fillMaxSize(),
        color = PosLinkDesignTokens.BackgroundColor
    ) {
        val padding = PaddingValues(
            start = spec.screenHorizontalPadding,
            top = spec.screenVerticalPadding,
            end = spec.screenHorizontalPadding,
            bottom = spec.screenVerticalPadding
        )
        Box(Modifier.padding(padding)) {
            content(padding)
        }
    }
}
