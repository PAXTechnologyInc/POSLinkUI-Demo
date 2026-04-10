package com.paxus.pay.poslinkui.demo.entry.option

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSection
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSurfaceCard
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView

/**
 * Compose screen for option selection (Select* fragments).
 * Aligns with fragment_option_dialog.xml layout and design-tokens.
 *
 * @param title Display title
 * @param options List of options (text, value)
 * @param onOptionSelected Called with selected index
 */
@Composable
fun OptionScreen(
    title: String,
    options: List<SelectOptionsView.Option>,
    onOptionSelected: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PosLinkSection(title = title) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.CompactSpacing),
            contentPadding = PaddingValues(vertical = PosLinkDesignTokens.SpaceBetweenTextView)
        ) {
            itemsIndexed(options) { index, option ->
                PosLinkSurfaceCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(index) }
                ) {
                    PosLinkText(
                        text = option.title ?: "",
                        role = PosLinkTextRole.Body,
                        modifier = Modifier.padding(
                            horizontal = PosLinkDesignTokens.CardPadding,
                            vertical = PosLinkDesignTokens.SpaceBetweenTextView
                        )
                    )
                }
            }
        }
        }
    }
}
