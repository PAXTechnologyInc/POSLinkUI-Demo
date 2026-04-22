package com.paxus.pay.poslinkui.demo.entry.text.amount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.compose.EntryHardwareConfirmEffect
import com.paxus.pay.poslinkui.demo.entry.compose.LocalEntryInteractionLocked
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyThemeButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSecondaryButton
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
    var localSubmitted by remember { mutableStateOf(false) }
    val interactionEnabled = !LocalEntryInteractionLocked.current && !localSubmitted
    val fieldShape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius)
    val inputHeight = PosLinkDesignTokens.buttonHeight()
    val sidePadding = 20.dp
    val submit = {
        localSubmitted = true
        onConfirm(CurrencyUtils.parse(displayValue))
    }

    EntryHardwareConfirmEffect(
        enabled = interactionEnabled,
        onConfirm = submit
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = CurrencyUtils.convert(content.baseAmount, content.currency),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            color = PosLinkDesignTokens.PrimaryTextColor,
            fontSize = PosLinkDesignTokens.SectionTitleTextSize,
            textAlign = TextAlign.Center
        )
        if (!content.tipName.isNullOrBlank()) {
            Text(
                text = content.tipName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                color = PosLinkDesignTokens.PrimaryTextColor,
                fontSize = PosLinkDesignTokens.SectionTitleTextSize,
                textAlign = TextAlign.Center
            )
        }
        Text(
            text = content.message,
            modifier = Modifier.fillMaxWidth(),
            color = PosLinkDesignTokens.PrimaryTextColor,
            fontSize = PosLinkDesignTokens.TitleTextSize,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.ControlGutter))
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
                .padding(sidePadding)
                .height(inputHeight),
            enabled = interactionEnabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = TextStyle(
                color = Color(0xFF222222),
                textAlign = TextAlign.Center
            ),
            decorationBox = { inner ->
                Box(
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
            PosLinkSecondaryButton(
                text = stringResource(R.string.no_tip),
                onClick = {
                    localSubmitted = true
                    onNoTip()
                },
                enabled = interactionEnabled
            )
        }
        PosLinkLegacyThemeButton(
            text = stringResource(R.string.confirm),
            onClick = submit,
            enabled = interactionEnabled
        )
    }
}
