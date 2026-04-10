package com.paxus.pay.poslinkui.demo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens

/**
 * Shared surfaced container for sections that need consistent padding and border treatment.
 *
 * @param modifier Optional modifier.
 * @param contentPadding Inner padding applied to the card body.
 * @param content Card content.
 */
@Composable
fun PosLinkSurfaceCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(PosLinkDesignTokens.CardPadding),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PosLinkDesignTokens.SurfaceColor
        ),
        border = BorderStroke(1.dp, PosLinkDesignTokens.BorderColor),
        shape = CardDefaults.shape
    ) {
        Column(modifier = Modifier.padding(contentPadding), content = content)
    }
}

/**
 * Vertical section wrapper with optional title.
 *
 * @param modifier Optional modifier (first optional parameter per Compose [ModifierParameter] lint).
 * @param title Optional section title rendered above [content].
 * @param content Section body.
 */
@Composable
fun PosLinkSection(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (!title.isNullOrBlank()) {
            PosLinkText(
                text = title,
                role = PosLinkTextRole.SectionTitle,
                modifier = Modifier.padding(bottom = PosLinkDesignTokens.SpaceBetweenTextView)
            )
        }
        content()
    }
}
