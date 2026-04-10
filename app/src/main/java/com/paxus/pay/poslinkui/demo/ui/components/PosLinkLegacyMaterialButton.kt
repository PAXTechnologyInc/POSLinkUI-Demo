package com.paxus.pay.poslinkui.demo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paxus.pay.poslinkui.demo.ui.theme.LocalPosLinkLegacyMaterialButtonVerticalInset

/**
 * Visual layout for [PosLinkLegacyMaterialFilledButton] (slot height, shape, colors, padding).
 *
 * @param slotHeight Total row height (typically `dimensionResource(R.dimen.button_height)` or token).
 * @param shape Corner shape for the filled surface.
 * @param containerColor Background when enabled.
 * @param disabledContainerColor Background when disabled.
 * @param horizontalContentPadding Inner horizontal padding for label/content.
 */
data class PosLinkLegacyMaterialFillAppearance(
    val slotHeight: Dp,
    val shape: Shape,
    val containerColor: Color,
    val disabledContainerColor: Color,
    val horizontalContentPadding: Dp = 16.dp
)

/**
 * Filled action whose **layout slot** height is [PosLinkLegacyMaterialFillAppearance.slotHeight] while the tinted
 * [Surface] is vertically inset, matching XML `MaterialButton` under Material Components themes. Material3 `Button` paints
 * the container color to the full slot; use this composable for legacy layout parity. Inset is read
 * from [LocalPosLinkLegacyMaterialButtonVerticalInset] (provided by [PosLinkTheme]).
 *
 * **Project convention:** use [PosLinkPrimaryButton] for standard primary actions, or this composable
 * when you need custom label styling or colors; do not use raw Material3 [androidx.compose.material3.Button]
 * for filled legacy-parity controls.
 *
 * @param appearance Slot height, shape, colors, and content padding.
 * @param content Composable drawn inside the filled surface (set text colors for enabled/disabled).
 */
@Composable
fun PosLinkLegacyMaterialFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fillMaxWidth: Boolean = true,
    appearance: PosLinkLegacyMaterialFillAppearance,
    content: @Composable () -> Unit
) {
    val insetV = LocalPosLinkLegacyMaterialButtonVerticalInset.current
    val fillHeight = (appearance.slotHeight - insetV * 2).coerceAtLeast(1.dp)
    val fillColor = if (enabled) appearance.containerColor else appearance.disabledContainerColor
    Box(
        modifier = modifier
            .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
            .height(appearance.slotHeight),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(fillHeight)
                .clickable(
                    role = Role.Button,
                    enabled = enabled,
                    onClick = onClick
                ),
            shape = appearance.shape,
            color = fillColor,
            shadowElevation = 0.dp,
            tonalElevation = 0.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = appearance.horizontalContentPadding),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}
