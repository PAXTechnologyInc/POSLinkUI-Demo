package com.paxus.pay.poslinkui.demo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paxus.pay.poslinkui.demo.ui.device.LocalDeviceLayoutSpec
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens

/**
 * Single-line selectable row for option lists (T030).
 *
 * @param label Primary line.
 * @param onClick Invoked when the row is clicked.
 * @param modifier Optional modifier.
 */
@Composable
fun PosLinkListItemRow(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spec = LocalDeviceLayoutSpec.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = spec.listItemMinHeight)
            .clickable(onClick = onClick)
            .padding(vertical = PosLinkDesignTokens.SpaceBetweenTextView / 2)
    ) {
        PosLinkText(
            text = label,
            role = PosLinkTextRole.Body
        )
    }
}
