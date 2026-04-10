package com.paxus.pay.poslinkui.demo.entry.text.amount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSecondaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils

/**
 * Labels and limits for [TotalAmountScreen].
 */
data class TotalAmountScreenContent(
    val message: String,
    val baseAmount: Long,
    val tipName: String?,
    val currency: String?,
    val maxLength: Int,
    val noTipEnabled: Boolean
)

/**
 * Compose screen for total amount entry (amount + optional no-tip).
 */
@Composable
fun TotalAmountScreen(
    content: TotalAmountScreenContent,
    onConfirm: (Long) -> Unit,
    onNoTip: () -> Unit
) {
    var displayValue by remember { mutableStateOf(CurrencyUtils.convert(0L, content.currency)) }

    Column(modifier = Modifier.fillMaxWidth()) {
        PosLinkText(
            text = content.message,
            role = PosLinkTextRole.ScreenTitle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        PosLinkText(
            text = "Base: " + CurrencyUtils.convert(content.baseAmount, content.currency),
            role = PosLinkTextRole.Body
        )
        if (!content.tipName.isNullOrBlank()) {
            PosLinkText(text = content.tipName, role = PosLinkTextRole.Supporting)
        }
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        OutlinedTextField(
            value = displayValue,
            onValueChange = {
                val digits = it.replace("[^0-9]".toRegex(), "")
                if (digits.length <= content.maxLength) {
                    displayValue = CurrencyUtils.convert(
                        digits.ifEmpty { "0" }.toLongOrNull() ?: 0L,
                        content.currency
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        if (content.noTipEnabled) {
            PosLinkSecondaryButton(text = "No Tip", onClick = onNoTip)
            Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        }
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm),
            onClick = { onConfirm(CurrencyUtils.parse(displayValue)) }
        )
    }
}
