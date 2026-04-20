package com.paxus.pay.poslinkui.demo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.paxus.pay.poslinkui.demo.ui.device.LocalDeviceLayoutSpec
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import java.util.Locale

enum class PosLinkPrimaryButtonVariant {
    Default,
    PoslinkLegacy
}

private data class PosLinkPrimaryButtonStyle(
    val cornerDp: Int,
    val containerColor: Color,
    val disabledContainerColor: Color,
    val pressedContainerColor: Color?,
    val allCaps: Boolean
)

/**
 * Full-width primary action (T030). Slot height uses the larger of [PosLinkDesignTokens.ButtonHeight] and
 * [LocalDeviceLayoutSpec.listItemMinHeight]; fill is inset like XML `MaterialButton` (see [PosLinkLegacyMaterialFilledButton]).
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
    variant: PosLinkPrimaryButtonVariant = PosLinkPrimaryButtonVariant.Default
) {
    val spec = LocalDeviceLayoutSpec.current
    val h = maxOf(PosLinkDesignTokens.ButtonHeight, spec.listItemMinHeight)
    val style = when (variant) {
        PosLinkPrimaryButtonVariant.Default -> PosLinkPrimaryButtonStyle(
            cornerDp = PosLinkDesignTokens.CornerRadius.value.toInt(),
            containerColor = PosLinkDesignTokens.PrimaryColor,
            disabledContainerColor = PosLinkDesignTokens.PrimaryColor.copy(alpha = 0.38f),
            pressedContainerColor = null,
            allCaps = false
        )
        PosLinkPrimaryButtonVariant.PoslinkLegacy -> PosLinkPrimaryButtonStyle(
            cornerDp = PosLinkDesignTokens.LegacyButtonCornerRadius.value.toInt(),
            containerColor = PosLinkDesignTokens.PrimaryColor,
            disabledContainerColor = PosLinkDesignTokens.PrimaryColor.copy(alpha = 0.45f),
            pressedContainerColor = PosLinkDesignTokens.LegacyButtonPressedColor,
            // golive XML Button 保留 extras 传入大小写（如 Yes/No）；勿强制全大写
            allCaps = false
        )
    }
    PosLinkLegacyMaterialFilledButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        fillMaxWidth = fillMaxWidth,
        appearance = PosLinkLegacyMaterialFillAppearance(
            slotHeight = h,
            shape = RoundedCornerShape(style.cornerDp.dp),
            containerColor = style.containerColor,
            disabledContainerColor = style.disabledContainerColor,
            pressedContainerColor = style.pressedContainerColor
        )
    ) {
        val displayText = if (style.allCaps) text.uppercase(Locale.ROOT) else text
        Text(
            text = displayText,
            style = MaterialTheme.typography.labelLarge,
            color = if (enabled) {
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
    val spec = LocalDeviceLayoutSpec.current
    val h = maxOf(PosLinkDesignTokens.ButtonHeight, spec.listItemMinHeight)
    val sizedModifier = modifier.height(h).then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = sizedModifier,
        shape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = PosLinkDesignTokens.PrimaryColor
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}
