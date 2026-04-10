package com.paxus.pay.poslinkui.demo.entry.confirmation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSecondaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSurfaceCard
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils

/**
 * Amounts and labels for [ServiceFeeScreen].
 */
data class ServiceFeeScreenContent(
    val saleAmount: Long,
    val feeName: String?,
    val feeAmount: Long,
    val totalAmount: Long,
    val currency: String?,
    val positiveText: String
)

/**
 * Compose screen for service fee confirmation.
 * Aligns with fragment_service_fee.xml.
 */
@Composable
fun ServiceFeeScreen(
    content: ServiceFeeScreenContent,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PosLinkSurfaceCard {
            FeeSummaryRow(
                label = "Sale:",
                value = CurrencyUtils.convert(content.saleAmount, content.currency)
            )
            Spacer(modifier = Modifier.height(PosLinkDesignTokens.InlineSpacing))
            FeeSummaryRow(
                label = content.feeName ?: "Fee:",
                value = CurrencyUtils.convert(content.feeAmount, content.currency)
            )
            Spacer(modifier = Modifier.height(PosLinkDesignTokens.InlineSpacing))
            FeeSummaryRow(
                label = "Total:",
                value = CurrencyUtils.convert(content.totalAmount, content.currency)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.ControlGutter)
        ) {
            PosLinkSecondaryButton(
                text = "Cancel",
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f),
                fillMaxWidth = false
            )
            PosLinkPrimaryButton(
                text = content.positiveText,
                onClick = onConfirm,
                modifier = Modifier
                    .weight(1f),
                fillMaxWidth = false
            )
        }
    }
}

@Composable
private fun FeeSummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        PosLinkText(text = label, role = PosLinkTextRole.Supporting)
        Spacer(modifier = Modifier.width(PosLinkDesignTokens.InlineSpacing))
        PosLinkText(text = value, role = PosLinkTextRole.Body)
    }
}
