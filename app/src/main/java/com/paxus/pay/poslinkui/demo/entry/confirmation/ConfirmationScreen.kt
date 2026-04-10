package com.paxus.pay.poslinkui.demo.entry.confirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButtonVariant
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSecondaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSurfaceCard
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens

/**
 * How the confirmation message is presented (card vs plain text).
 */
enum class ConfirmationMessageStyle {
    /** Bordered surface card (default). */
    InCard,

    /** Centered body text without a surrounding card (legacy / prototype parity). */
    Plain
}

/**
 * Layout options for [ConfirmationScreen] (message placement, dual primary actions, etc.).
 *
 * @param messageStyle Card or plain message.
 * @param verticallyCenterContent When true, message and actions are grouped and centered in the available height
 * (e.g. cash payment); when false, content starts at the top under the host padding.
 * @param bothActionsPrimaryLegacy When true, cancel uses the same filled legacy style as OK.
 * @param messageRole Typography for the message ([PosLinkTextRole.Body] vs title-sized roles for XML `text_size_title`).
 */
data class ConfirmationScreenLayout(
    val messageStyle: ConfirmationMessageStyle = ConfirmationMessageStyle.InCard,
    val verticallyCenterContent: Boolean = false,
    val bothActionsPrimaryLegacy: Boolean = false,
    val messageRole: PosLinkTextRole = PosLinkTextRole.Body
)

/**
 * Compose screen for confirmation entry (message + confirm/cancel).
 * Aligns with fragment_confirmation.xml.
 *
 * @param message Display message
 * @param positiveText Confirm button text
 * @param negativeText Cancel button text (null = hide)
 * @param onConfirm Called when confirm clicked
 * @param onCancel Called when cancel clicked
 * @param layout Optional presentation overrides for prototype / legacy parity.
 */
@Composable
fun ConfirmationScreen(
    message: String,
    positiveText: String,
    negativeText: String?,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    layout: ConfirmationScreenLayout = ConfirmationScreenLayout()
) {
    @Composable
    fun MessageBlock() {
        when (layout.messageStyle) {
            ConfirmationMessageStyle.InCard -> {
                PosLinkSurfaceCard {
                    PosLinkText(
                        text = message,
                        role = layout.messageRole,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            ConfirmationMessageStyle.Plain -> {
                PosLinkText(
                    text = message,
                    role = layout.messageRole,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = PosLinkDesignTokens.SpaceBetweenTextView),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    fun ActionsRow() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.ControlGutter)
        ) {
            if (!negativeText.isNullOrBlank()) {
                if (layout.bothActionsPrimaryLegacy) {
                    PosLinkPrimaryButton(
                        text = negativeText,
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        fillMaxWidth = false,
                        variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
                    )
                } else {
                    PosLinkSecondaryButton(
                        text = negativeText,
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        fillMaxWidth = false
                    )
                }
            }
            PosLinkPrimaryButton(
                text = positiveText,
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                fillMaxWidth = false,
                variant = if (layout.bothActionsPrimaryLegacy) {
                    PosLinkPrimaryButtonVariant.PoslinkLegacy
                } else {
                    PosLinkPrimaryButtonVariant.Default
                }
            )
        }
    }

    val columnContent: @Composable () -> Unit = {
        MessageBlock()
        ActionsRow()
    }

    if (layout.verticallyCenterContent) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                columnContent()
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            columnContent()
        }
    }
}
