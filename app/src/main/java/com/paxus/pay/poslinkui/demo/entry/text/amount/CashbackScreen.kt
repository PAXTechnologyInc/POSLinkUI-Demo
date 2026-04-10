package com.paxus.pay.poslinkui.demo.entry.text.amount

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSurfaceCard
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView

private data class CashbackLayoutMetrics(
    val titleToOptionsSpacing: Dp,
    val optionsSpacing: Dp,
    val optionsGridHeight: Dp,
    val noThanksHeight: Dp
)

/**
 * Computes grid height and spacing for preset cashback options from option count and flow mode.
 */
private fun cashbackLayoutMetrics(
    options: List<SelectOptionsView.Option>,
    disableOtherMode: Boolean
): CashbackLayoutMetrics {
    val titleToOptionsSpacing = PosLinkDesignTokens.SpaceBetweenTextView
    val optionsSpacing = if (disableOtherMode) {
        PosLinkDesignTokens.DenseGridSpacing
    } else {
        PosLinkDesignTokens.CompactSpacing
    }
    val optionsGridHeight = if (disableOtherMode) {
        val rowCount = ((options.size + 1) / 2).coerceAtLeast(1)
        PosLinkDesignTokens.ButtonHeight * rowCount +
            PosLinkDesignTokens.DenseGridSpacing * (rowCount - 1) +
            PosLinkDesignTokens.SpaceBetweenTextView
    } else {
        200.dp
    }
    val noThanksHeight = if (disableOtherMode) {
        PosLinkDesignTokens.ButtonHeight + PosLinkDesignTokens.DenseGridSpacing
    } else {
        PosLinkDesignTokens.ButtonHeight
    }
    return CashbackLayoutMetrics(
        titleToOptionsSpacing = titleToOptionsSpacing,
        optionsSpacing = optionsSpacing,
        optionsGridHeight = optionsGridHeight,
        noThanksHeight = noThanksHeight
    )
}

/**
 * Static configuration for [CashbackScreen].
 */
data class CashbackScreenProps(
    val options: List<SelectOptionsView.Option>,
    val promptOther: Boolean,
    val currency: String?,
    val maxLength: Int,
    val valuePattern: String?,
    val parityLog: String = "CashBackEnable parity v3 active"
)

/**
 * Compose screen for cashback entry (options + optional other amount).
 */
@Composable
fun CashbackScreen(
    props: CashbackScreenProps,
    onOptionSelected: (Long) -> Unit,
    onConfirm: (Long) -> Unit,
    onError: (String) -> Unit
) {
    var displayValue by remember { mutableStateOf("") }
    val disableOtherMode = !props.promptOther && props.options.isNotEmpty()
    val m = cashbackLayoutMetrics(props.options, disableOtherMode)
    LaunchedEffect(Unit) {
        Logger.i(props.parityLog)
    }

    CashbackScreenMainColumn(
        CashbackScreenMainColumnParams(
            layout = CashbackScreenMainColumnLayoutParams(
                m = m,
                options = props.options,
                promptOther = props.promptOther,
                currency = props.currency,
                maxLength = props.maxLength,
                valuePattern = props.valuePattern
            ),
            interactions = CashbackScreenMainColumnInteractions(
                displayValue = displayValue,
                onDisplayValueChange = { displayValue = it },
                onPresetOptionChosen = { amt ->
                    if (props.promptOther) {
                        displayValue = CurrencyUtils.convert(amt, props.currency)
                        onOptionSelected(amt)
                    } else {
                        onConfirm(amt)
                    }
                },
                onNoThanksChosen = {
                    if (props.promptOther) {
                        displayValue = ""
                        onOptionSelected(0L)
                    } else {
                        onConfirm(0L)
                    }
                },
                onError = onError,
                onConfirm = onConfirm
            )
        )
    )
}

private data class CashbackScreenMainColumnLayoutParams(
    val m: CashbackLayoutMetrics,
    val options: List<SelectOptionsView.Option>,
    val promptOther: Boolean,
    val currency: String?,
    val maxLength: Int,
    val valuePattern: String?
)

private data class CashbackScreenMainColumnInteractions(
    val displayValue: String,
    val onDisplayValueChange: (String) -> Unit,
    val onPresetOptionChosen: (Long) -> Unit,
    val onNoThanksChosen: () -> Unit,
    val onError: (String) -> Unit,
    val onConfirm: (Long) -> Unit
)

private data class CashbackScreenMainColumnParams(
    val layout: CashbackScreenMainColumnLayoutParams,
    val interactions: CashbackScreenMainColumnInteractions
)

@Composable
private fun CashbackScreenMainColumn(p: CashbackScreenMainColumnParams) {
    val m = p.layout.m
    val options = p.layout.options
    val promptOther = p.layout.promptOther
    val currency = p.layout.currency
    val maxLength = p.layout.maxLength
    val valuePattern = p.layout.valuePattern
    val displayValue = p.interactions.displayValue
    val onDisplayValueChange = p.interactions.onDisplayValueChange
    val onPresetOptionChosen = p.interactions.onPresetOptionChosen
    val onNoThanksChosen = p.interactions.onNoThanksChosen
    val onError = p.interactions.onError
    val onConfirm = p.interactions.onConfirm
    Column(modifier = Modifier.fillMaxWidth()) {
        PosLinkText(
            text = stringResource(R.string.select_cashback_amount),
            role = PosLinkTextRole.ScreenTitle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(m.titleToOptionsSpacing))
        if (options.isNotEmpty()) {
            CashbackPresetOptionsBlock(
                options = options,
                optionsSpacing = m.optionsSpacing,
                optionsGridHeight = m.optionsGridHeight,
                noThanksHeight = m.noThanksHeight,
                onPresetOptionClick = onPresetOptionChosen,
                onNoThanksClick = onNoThanksChosen
            )
        }
        if (promptOther || options.isEmpty()) {
            Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
            CashbackOtherAmountField(
                displayValue = displayValue,
                maxLength = maxLength,
                currency = currency,
                onDisplayValueChange = onDisplayValueChange
            )
        }
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        val promptInputStr = stringResource(R.string.prompt_input)
        if (promptOther || options.isEmpty()) {
            CashbackConfirmBar(
                displayValue = displayValue,
                valuePattern = valuePattern,
                promptInputStr = promptInputStr,
                confirmLabel = stringResource(R.string.confirm),
                onError = onError,
                onConfirm = onConfirm
            )
        }
    }
}

@Composable
private fun CashbackPresetOptionsBlock(
    options: List<SelectOptionsView.Option>,
    optionsSpacing: Dp,
    optionsGridHeight: Dp,
    noThanksHeight: Dp,
    onPresetOptionClick: (Long) -> Unit,
    onNoThanksClick: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(optionsGridHeight),
        contentPadding = PaddingValues(PosLinkDesignTokens.CompactSpacing),
        horizontalArrangement = Arrangement.spacedBy(optionsSpacing),
        verticalArrangement = Arrangement.spacedBy(optionsSpacing)
    ) {
        itemsIndexed(options) { _, opt ->
            val amt = (opt.value as? Long) ?: 0L
            PosLinkSurfaceCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PosLinkDesignTokens.ButtonHeight)
                    .clickable { onPresetOptionClick(amt) },
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    PosLinkText(text = opt.title ?: "", textAlign = TextAlign.Center)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
    PosLinkSurfaceCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(noThanksHeight)
            .clickable { onNoThanksClick() },
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PosLinkText(text = "No Thanks!!", textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun CashbackOtherAmountField(
    displayValue: String,
    maxLength: Int,
    currency: String?,
    onDisplayValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = displayValue,
        onValueChange = {
            val digits = it.replace("[^0-9]".toRegex(), "")
            if (digits.length <= maxLength) {
                onDisplayValueChange(
                    CurrencyUtils.convert(digits.ifEmpty { "0" }.toLongOrNull() ?: 0L, currency)
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { PosLinkText(text = "Other", role = PosLinkTextRole.Supporting) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFDBD4D9),
            unfocusedContainerColor = Color(0xFFDBD4D9),
            focusedBorderColor = Color(0xFFDBD4D9),
            unfocusedBorderColor = Color(0xFFDBD4D9),
            focusedTextColor = Color(0xFF222222),
            unfocusedTextColor = Color(0xFF222222)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun CashbackConfirmBar(
    displayValue: String,
    valuePattern: String?,
    promptInputStr: String,
    confirmLabel: String,
    onError: (String) -> Unit,
    onConfirm: (Long) -> Unit
) {
    PosLinkPrimaryButton(
        text = confirmLabel,
        onClick = {
            val value = if (displayValue.isNotEmpty()) CurrencyUtils.parse(displayValue) else 0L
            val lengthList = valuePattern
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    runCatching { ValuePatternUtils.getLengthList(it) }
                        .getOrElse { mutableListOf<Int?>() }
                }
                ?: mutableListOf<Int?>(0)
            if (value == 0L && !lengthList.contains(0)) onError(promptInputStr)
            else onConfirm(value)
        }
    )
}
