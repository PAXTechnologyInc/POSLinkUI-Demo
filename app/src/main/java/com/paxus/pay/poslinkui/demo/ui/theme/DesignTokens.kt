package com.paxus.pay.poslinkui.demo.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Design Tokens for POSLinkUI Compose migration.
 * Layout and typography baseline matches `res/values/dimens.xml` (`button_height` / `space_between_textview`; XML is authoritative for View inflation).
 */
object PosLinkDesignTokens {
    // Core colors
    val PrimaryColor = Color(0xFF6E85B7)
    val SecondaryColor = Color(0xFFA6B1E1)
    val BackgroundColor = Color(0xFF041D3C)
    val SurfaceColor = Color(0xFF172D4A)
    val SurfaceMutedColor = Color(0xFFE4E1E3)
    val BorderColor = Color(0xFF6E85B7)
    val PrimaryTextColor = Color(0xFFECECEC)
    val SecondaryTextColor = Color(0xFFA6B1E1)

    // Typography tokens
    val DisplayTextSize: TextUnit = 28.sp
    val TitleTextSize: TextUnit = 24.sp
    val SectionTitleTextSize: TextUnit = 18.sp
    val BodyTextSize: TextUnit = 14.sp
    val CaptionTextSize: TextUnit = 14.sp
    val SupportingTextSize: TextUnit = 11.sp

    // Layout tokens
    val CardPadding: Dp = 16.dp
    val ScreenPadding: Dp = 20.dp
    val CornerRadius: Dp = 8.dp
    val ButtonHeight: Dp = 54.dp
    val InputHeight: Dp = 54.dp

    /**
     * Approximates legacy XML `MaterialButton` vertical inset: outer slot height matches
     * `button_height` / [ButtonHeight]; the tinted shape is shorter and centered inside the slot.
     */
    val LegacyMaterialButtonVerticalInset: Dp = 4.dp
    val SpaceBetweenTextView: Dp = 12.dp
    val CompactSpacing: Dp = 4.dp
    val InlineSpacing: Dp = 5.dp
    val SectionSpacing: Dp = 20.dp

    /**
     * Horizontal gutter for label start inset, paired actions in a row, and [Arrangement.spacedBy]
     * between side-by-side buttons (legacy XML ~8dp).
     */
    val ControlGutter: Dp = 8.dp

    /**
     * Vertical gap between stacked sections when a larger pause than [SpaceBetweenTextView] is needed
     * (many demo routes used 16dp).
     */
    val SectionBreakSpacing: Dp = 16.dp

    /** Fine vertical gap between tightly coupled lines. */
    val MicroSpacing: Dp = 2.dp

    /**
     * Inner horizontal padding for amount / text fields (golive TextField + invoice parity).
     */
    val FieldInnerHorizontalPadding: Dp = 12.dp

    /** Gap between cells in dense option grids (e.g. cashback preset grid). */
    val DenseGridSpacing: Dp = 6.dp
    val ToolbarHeight: Dp = 48.dp
    val ToastHorizontalPadding: Dp = 16.dp
    val ToastVerticalPadding: Dp = 12.dp
    val ToastTopPadding: Dp = 12.dp

    // Second-screen tokens
    val SecondScreenTitleTextSize: TextUnit = 26.sp
    val SecondScreenAmountTextSize: TextUnit = 50.sp
    val SecondScreenMessageTextSize: TextUnit = 30.sp
    val SecondScreenStatusTextSize: TextUnit = 40.sp
    val SecondScreenTapIconWidth: Dp = 146.dp
    val SecondScreenTapIconHeight: Dp = 87.dp

    // Status colors
    val SuccessColor = Color(0xFF46B54B)
    val FailColor = Color(0xFFFB3A3A)
    val PartialSuccessColor = Color(0xFFBB893F)
    val CancelColor = Color(0xFFFB3A3A)
    val ToastSuccessColor = Color(0xFF46B54B)
    val ToastFailureColor = Color(0xFFF15B5B)
    val SecondScreenTextColor = Color(0xFFFFFFFF)
    val SecondScreenBackgroundColor = Color(0xFF172D4A)
}
