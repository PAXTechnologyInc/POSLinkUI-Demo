package com.paxus.pay.poslinkui.demo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * POSLink UI typography.
 * Aligns with design-tokens and res/values/dimens.xml (text_size_title, text_size_normal).
 */
val PosLinkTypography = Typography(
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = PosLinkDesignTokens.DisplayTextSize,
        lineHeight = 34.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = PosLinkDesignTokens.TitleTextSize,
        lineHeight = 30.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = PosLinkDesignTokens.SectionTitleTextSize,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = PosLinkDesignTokens.BodyTextSize,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = PosLinkDesignTokens.BodyTextSize,
        lineHeight = 20.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = PosLinkDesignTokens.CaptionTextSize,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = PosLinkDesignTokens.SupportingTextSize,
        lineHeight = 18.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = PosLinkDesignTokens.BodyTextSize,
        lineHeight = 20.sp
    )
)
