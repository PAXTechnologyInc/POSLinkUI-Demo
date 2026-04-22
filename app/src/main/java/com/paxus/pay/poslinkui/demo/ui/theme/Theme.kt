package com.paxus.pay.poslinkui.demo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp

/**
 * Vertical inset between the legacy Material button **slot** height and the filled surface, mirroring
 * XML Material Components `MaterialButton`. Override via [CompositionLocalProvider] when needed;
 * default matches [PosLinkDesignTokens.LegacyMaterialButtonVerticalInset].
 */
val LocalPosLinkLegacyMaterialButtonVerticalInset =
    compositionLocalOf { PosLinkDesignTokens.LegacyMaterialButtonVerticalInset }

/**
 * POSLink UI theme for Compose screens.
 * Aligns with POSLinkUI-Design_V1.03.00 and design-tokens.
 *
 * @param legacyMaterialButtonVerticalInset Drives [LocalPosLinkLegacyMaterialButtonVerticalInset] for legacy-style filled buttons.
 */
@Composable
fun PosLinkTheme(
    legacyMaterialButtonVerticalInset: Dp = PosLinkDesignTokens.LegacyMaterialButtonVerticalInset,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalPosLinkLegacyMaterialButtonVerticalInset provides legacyMaterialButtonVerticalInset
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = PosLinkTypography,
            content = content
        )
    }
}

private val LightColorScheme = lightColorScheme(
    primary = PosLinkDesignTokens.PrimaryColor,
    secondary = PosLinkDesignTokens.SecondaryColor,
    tertiary = PosLinkDesignTokens.PastelAccent,
    background = PosLinkDesignTokens.BackgroundColor,
    surface = PosLinkDesignTokens.SurfaceColor,
    surfaceVariant = PosLinkDesignTokens.SurfaceMutedColor,
    outline = PosLinkDesignTokens.BorderColor,
    error = PosLinkDesignTokens.FailColor,
    onPrimary = PosLinkDesignTokens.PrimaryTextColor,
    onSecondary = PosLinkDesignTokens.PrimaryTextColor,
    onBackground = PosLinkDesignTokens.PrimaryTextColor,
    onSurface = PosLinkDesignTokens.PrimaryTextColor,
    onSurfaceVariant = PosLinkDesignTokens.OnLightTextColor,
    onError = PosLinkDesignTokens.PrimaryTextColor
)
