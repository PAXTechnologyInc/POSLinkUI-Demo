package com.paxus.pay.poslinkui.demo.entry.information

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSurfaceCard
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import androidx.compose.ui.res.stringResource

/**
 * Compose screen for information display (DisplayTransInfo, DisplayApproveMessage).
 * Aligns with fragment_display_trans.xml layout and design-tokens.
 *
 * @param content Display content (key-value pairs or message)
 * @param onConfirm Called when confirm button is clicked
 */
@Composable
fun InfoScreen(
    title: String? = null,
    content: String,
    onConfirm: () -> Unit
) {
    val scrollState = rememberScrollState()
    val confirmText = stringResource(R.string.confirm)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        PosLinkSurfaceCard {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (!title.isNullOrBlank()) {
                    PosLinkText(
                        text = title,
                        role = PosLinkTextRole.ScreenTitle,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
                }
                PosLinkText(
                    text = content,
                    role = PosLinkTextRole.Body,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        PosLinkPrimaryButton(text = confirmText, onClick = onConfirm)
    }
}
