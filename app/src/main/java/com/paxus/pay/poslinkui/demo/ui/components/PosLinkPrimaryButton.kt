package com.paxus.pay.poslinkui.demo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import java.util.Locale

enum class PosLinkPrimaryButtonVariant {
    Default,
    PoslinkLegacy
}

private data class PosLinkPrimaryButtonStyle(
    val corner: Dp,
    val containerColor: Color,
    val disabledContainerColor: Color,
    val pressedContainerColor: Color?,
    val allCaps: Boolean
)

/**
 * Full-width primary action (T030). Slot height follows configuration-qualified `R.dimen.button_height`;
 * fill is inset like XML `MaterialButton` (see [PosLinkLegacyMaterialFilledButton]).
 *
 * @param text Button label.
 * @param onClick Click handler.
 * @param modifier Optional modifier.
 * @param enabled Whether the button is enabled.
 */
@Composable
fun PosLinkPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fillMaxWidth: Boolean = true,
    variant: PosLinkPrimaryButtonVariant = PosLinkPrimaryButtonVariant.Default,
    containerColorOverride: Color? = null,
    disabledContainerColorOverride: Color? = null,
    pressedContainerColorOverride: Color? = null,
    textColorOverride: Color? = null,
    textFontWeight: FontWeight = FontWeight.Medium,
    textLetterSpacing: TextUnit = PosLinkDesignTokens.ButtonTextLetterSpacing,
    allCapsOverride: Boolean? = null
) {
    val h = dimensionResource(R.dimen.button_height)
    val outerV = dimensionResource(R.dimen.margin_gap)
    val style = when (variant) {
        PosLinkPrimaryButtonVariant.Default -> PosLinkPrimaryButtonStyle(
            corner = PosLinkDesignTokens.LegacyButtonCornerRadius,
            containerColor = PosLinkDesignTokens.PrimaryColor,
            disabledContainerColor = PosLinkDesignTokens.DisabledColor,
            pressedContainerColor = PosLinkDesignTokens.LegacyButtonPressedColor,
            allCaps = true
        )
        PosLinkPrimaryButtonVariant.PoslinkLegacy -> PosLinkPrimaryButtonStyle(
            corner = PosLinkDesignTokens.LegacyButtonCornerRadius,
            containerColor = PosLinkDesignTokens.PrimaryColor,
            disabledContainerColor = PosLinkDesignTokens.DisabledColor,
            pressedContainerColor = PosLinkDesignTokens.LegacyButtonPressedColor,
            // golive XML Button 淇濈暀 extras 浼犲叆澶у皬鍐欙紙濡?Yes/No锛夛紱鍕垮己鍒跺叏澶у啓
            allCaps = true
        )
    }
    PosLinkLegacyMaterialFilledButton(
        onClick = onClick,
        modifier = modifier.padding(vertical = outerV),
        enabled = enabled,
        fillMaxWidth = fillMaxWidth,
        appearance = PosLinkLegacyMaterialFillAppearance(
            slotHeight = h,
            shape = RoundedCornerShape(style.corner),
            containerColor = containerColorOverride ?: style.containerColor,
            disabledContainerColor = disabledContainerColorOverride ?: style.disabledContainerColor,
            pressedContainerColor = pressedContainerColorOverride ?: style.pressedContainerColor
        )
    ) {
        val displayText = if (allCapsOverride ?: style.allCaps) text.uppercase(Locale.ROOT) else text
        Text(
            text = displayText,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = textFontWeight,
                letterSpacing = textLetterSpacing
            ),
            color = textColorOverride ?: if (enabled) {
                PosLinkDesignTokens.PrimaryTextColor
            } else {
                PosLinkDesignTokens.PrimaryTextColor.copy(alpha = 0.38f)
            }
        )
    }
}

/**
 * Secondary action matching the same sizing rules as [PosLinkPrimaryButton].
 *
 * @param text Button label.
 * @param onClick Click handler.
 * @param modifier Optional modifier.
 * @param enabled Whether the button is enabled.
 * @param fillMaxWidth Whether to stretch the button to the available width.
 */
@Composable
fun PosLinkSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fillMaxWidth: Boolean = true
) {
    val outerV = dimensionResource(R.dimen.margin_gap)
    PosLinkLegacyMaterialFilledButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.padding(vertical = outerV),
        fillMaxWidth = fillMaxWidth,
        appearance = PosLinkLegacyMaterialFillAppearance(
            slotHeight = dimensionResource(R.dimen.button_height),
            shape = RoundedCornerShape(PosLinkDesignTokens.LegacyButtonCornerRadius),
            containerColor = PosLinkDesignTokens.PrimaryColor,
            disabledContainerColor = PosLinkDesignTokens.DisabledColor,
            pressedContainerColor = PosLinkDesignTokens.LegacyButtonPressedColor
        )
    ) {
        Text(
            text = text.uppercase(Locale.ROOT),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = PosLinkDesignTokens.ButtonTextLetterSpacing
            ),
            color = if (enabled) {
                PosLinkDesignTokens.PrimaryTextColor
            } else {
                PosLinkDesignTokens.PrimaryTextColor.copy(alpha = 0.38f)
            }
        )
    }
}

/**
 * Legacy XML `<Button>` parity:
 * - uses [PosLinkPrimaryButtonVariant.PoslinkLegacy] selector colors (normal/pressed),
 * - keeps disabled background on primary color (legacy theme does not provide a disabled state shape),
 * - keeps uppercase behavior by default.
 */
@Composable
fun PosLinkLegacyThemeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fillMaxWidth: Boolean = true,
    allCaps: Boolean = true
) {
    PosLinkPrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        fillMaxWidth = fillMaxWidth,
        variant = PosLinkPrimaryButtonVariant.PoslinkLegacy,
        disabledContainerColorOverride = PosLinkDesignTokens.PrimaryColor,
        textColorOverride = PosLinkDesignTokens.PrimaryTextColor,
        allCapsOverride = allCaps
    )
}
