package com.paxus.pay.poslinkui.demo.entry.text.amount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
    val fieldShape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius)
    val inputHeight = PosLinkDesignTokens.ButtonHeight

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
        BasicTextField(
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
            modifier = Modifier
                .fillMaxWidth()
                .height(inputHeight),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = TextStyle(
                color = Color(0xFF222222),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            ),
            decorationBox = { inner ->
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(inputHeight)
                        .border(2.dp, Color(0xFFDBD4D9), fieldShape)
                        .background(Color(0xFFDBD4D9), fieldShape),
                    contentAlignment = Alignment.Center
                ) {
                    inner()
                }
            }
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        if (content.noTipEnabled) {
            PosLinkSecondaryButton(text = "No Tip", onClick = onNoTip)
            Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        }
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm).uppercase(),
            onClick = { onConfirm(CurrencyUtils.parse(displayValue)) }
        )
    }
}
