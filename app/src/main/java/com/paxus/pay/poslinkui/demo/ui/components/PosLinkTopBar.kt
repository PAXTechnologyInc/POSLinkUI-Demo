package com.paxus.pay.poslinkui.demo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens

/**
 * Simple top title aligned with design tokens (T030).
 *
 * @param title Screen title text.
 * @param modifier Optional modifier.
 */
@Composable
fun PosLinkTopBar(
    title: String,
    modifier: Modifier = Modifier
) {
    PosLinkText(
        text = title,
        role = PosLinkTextRole.ScreenTitle,
        color = PosLinkDesignTokens.PrimaryColor,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = PosLinkDesignTokens.SpaceBetweenTextView)
    )
}
