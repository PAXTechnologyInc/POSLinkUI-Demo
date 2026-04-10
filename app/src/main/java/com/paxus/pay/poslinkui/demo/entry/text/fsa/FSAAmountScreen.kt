package com.paxus.pay.poslinkui.demo.entry.text.fsa

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
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils

/**
 * Compose screen for FSA amount entry (child of FSAFragment).
 */
@Composable
fun FSAAmountScreen(
    message: String?,
    totalAmount: Long,
    currency: String?,
    maxLength: Int,
    onConfirm: (Long) -> Unit
) {
    var displayValue by remember { mutableStateOf(CurrencyUtils.convert(0L, currency)) }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (totalAmount > 0) {
            PosLinkText(
                text = stringResource(R.string.history_total) + ": " + CurrencyUtils.convert(totalAmount, currency),
                role = PosLinkTextRole.Body
            )
            Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        }
        PosLinkText(
            text = message ?: "",
            role = PosLinkTextRole.ScreenTitle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        OutlinedTextField(
            value = displayValue,
            onValueChange = {
                val digits = it.replace("[^0-9]".toRegex(), "")
                if (digits.length <= maxLength) {
                    displayValue = CurrencyUtils.convert(digits.ifEmpty { "0" }.toLongOrNull() ?: 0L, currency)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm),
            onClick = { onConfirm(CurrencyUtils.parse(displayValue)) }
        )
    }
}
