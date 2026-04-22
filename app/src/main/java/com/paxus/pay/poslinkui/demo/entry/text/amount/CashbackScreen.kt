package com.paxus.pay.poslinkui.demo.entry.text.amount

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.compose.EntryHardwareConfirmEffect
import com.paxus.pay.poslinkui.demo.entry.compose.LocalEntryInteractionLocked
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyThemeButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView

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
 * Visual baseline follows `golive/v1.03.00` `fragment_cashback.xml`,
 * `layout_select_option_item.xml`, `item_cashback_option.xml`, and `values/dimens.xml`.
 */
@Composable
fun CashbackScreen(
    props: CashbackScreenProps,
    onOptionSelected: (Long) -> Unit,
    onConfirm: (Long) -> Unit,
    onError: (String) -> Unit
) {
    val interactionLocked = LocalEntryInteractionLocked.current
    var displayValue by remember { mutableStateOf("") }
    val res = LocalContext.current.resources
    val density = LocalDensity.current
    val buttonHeight = dimensionResource(R.dimen.button_height)
    val marginGap = dimensionResource(R.dimen.margin_gap)
    val sectionSpacing = dimensionResource(R.dimen.space_between_textview)
    val cornerRadius = dimensionResource(R.dimen.corner_radius)
    val optionTextSize = with(density) { res.getDimension(R.dimen.text_size_subtitle).toSp() }
    val hintTextSize = with(density) { res.getDimension(R.dimen.text_size_hint).toSp() }
    val bodyTextSize = with(density) { res.getDimension(R.dimen.text_size_normal).toSp() }
    val optionTextStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Normal,
        fontSize = optionTextSize,
        lineHeight = optionTextSize * 1.2f
    )
    val fieldTextStyle = MaterialTheme.typography.bodyLarge.copy(
        fontWeight = FontWeight.Normal,
        fontSize = bodyTextSize,
        lineHeight = bodyTextSize * 1.4f,
        textAlign = TextAlign.Center,
        color = PosLinkDesignTokens.OnLightTextColor
    )
    val fieldLabelStyle = MaterialTheme.typography.bodySmall.copy(
        fontWeight = FontWeight.Normal,
        fontSize = hintTextSize,
        lineHeight = hintTextSize * 1.2f
    )
    val promptInputStr = stringResource(R.string.prompt_input)
    val submit = {
        val value = if (displayValue.isBlank()) 0L else CurrencyUtils.parse(displayValue)
        val lengthList = props.valuePattern
            ?.takeIf { it.isNotBlank() }
            ?.let {
                runCatching { ValuePatternUtils.getLengthList(it) }
                    .getOrElse { mutableListOf<Int?>() }
            }
            ?: mutableListOf<Int?>(0)
        if (value == 0L && !lengthList.contains(0)) {
            onError(promptInputStr)
        } else {
            onConfirm(value)
        }
    }

    LaunchedEffect(Unit) {
        Logger.i(props.parityLog)
    }

    EntryHardwareConfirmEffect(
        enabled = !interactionLocked,
        onConfirm = submit
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        PosLinkText(
            text = stringResource(R.string.select_cashback_amount),
            role = PosLinkTextRole.ScreenTitle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(sectionSpacing))
        if (props.options.isNotEmpty()) {
            CashbackPresetOptionsBlock(
                options = props.options,
                buttonHeight = buttonHeight,
                marginGap = marginGap,
                cornerRadius = cornerRadius,
                optionTextStyle = optionTextStyle,
                onPresetOptionClick = { amt ->
                    displayValue = CurrencyUtils.convert(amt, props.currency)
                    onOptionSelected(amt)
                    onConfirm(amt)
                },
                onNoThanksClick = {
                    displayValue = ""
                    onOptionSelected(0L)
                    onConfirm(0L)
                }
            )
        }
        if (props.promptOther || props.options.isEmpty()) {
            Spacer(modifier = Modifier.height(sectionSpacing))
            CashbackOtherAmountField(
                displayValue = displayValue,
                maxLength = props.maxLength,
                currency = props.currency,
                buttonHeight = buttonHeight,
                marginGap = marginGap,
                cornerRadius = cornerRadius,
                fieldTextStyle = fieldTextStyle,
                fieldLabelStyle = fieldLabelStyle,
                onDisplayValueChange = { displayValue = it }
            )
            Spacer(modifier = Modifier.height(sectionSpacing))
            CashbackConfirmBar(
                onSubmit = submit
            )
        }
    }
}

@Composable
private fun CashbackPresetOptionsBlock(
    options: List<SelectOptionsView.Option>,
    buttonHeight: Dp,
    marginGap: Dp,
    cornerRadius: Dp,
    optionTextStyle: TextStyle,
    onPresetOptionClick: (Long) -> Unit,
    onNoThanksClick: () -> Unit
) {
    val rowGap = marginGap * 2
    val optionRows = options.chunked(2)
    optionRows.forEachIndexed { index, rowOptions ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = marginGap),
            horizontalArrangement = Arrangement.spacedBy(rowGap)
        ) {
            rowOptions.forEach { option ->
                CashbackOptionCard(
                    modifier = Modifier.weight(1f),
                    text = option.title.orEmpty(),
                    buttonHeight = buttonHeight,
                    cornerRadius = cornerRadius,
                    textStyle = optionTextStyle,
                    onClick = { onPresetOptionClick((option.value as? Long) ?: 0L) }
                )
            }
            repeat(2 - rowOptions.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        if (index != optionRows.lastIndex) {
            Spacer(modifier = Modifier.height(rowGap))
        }
    }
    Spacer(modifier = Modifier.height(rowGap))
    CashbackOptionCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = marginGap),
        text = "No Thanks!!",
        buttonHeight = buttonHeight,
        cornerRadius = cornerRadius,
        textStyle = optionTextStyle,
        onClick = onNoThanksClick
    )
}

@Composable
private fun CashbackOptionCard(
    text: String,
    buttonHeight: Dp,
    cornerRadius: Dp,
    textStyle: TextStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val controlsEnabled = !LocalEntryInteractionLocked.current
    Box(
        modifier = modifier
            .height(buttonHeight)
            .border(
                width = PosLinkDesignTokens.BorderWidthThin,
                color = PosLinkDesignTokens.BorderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .background(Color.Transparent, RoundedCornerShape(cornerRadius))
            .clickable(enabled = controlsEnabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = textStyle,
            color = PosLinkDesignTokens.PrimaryTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = PosLinkDesignTokens.ButtonHorizontalContentPadding)
        )
    }
}

@Composable
private fun CashbackOtherAmountField(
    displayValue: String,
    maxLength: Int,
    currency: String?,
    buttonHeight: Dp,
    marginGap: Dp,
    cornerRadius: Dp,
    fieldTextStyle: TextStyle,
    fieldLabelStyle: TextStyle,
    onDisplayValueChange: (String) -> Unit
) {
    val interactionLocked = LocalEntryInteractionLocked.current
    val fieldShape = RoundedCornerShape(cornerRadius)
    BasicTextField(
        value = displayValue,
        readOnly = interactionLocked,
        onValueChange = { raw ->
            val digits = raw.replace("[^0-9]".toRegex(), "")
            if (digits.length <= maxLength) {
                onDisplayValueChange(
                    if (digits.isEmpty()) {
                        ""
                    } else {
                        CurrencyUtils.convert(digits.toLongOrNull() ?: 0L, currency)
                    }
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = fieldTextStyle,
        cursorBrush = SolidColor(PosLinkDesignTokens.PastelAccent),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .border(2.dp, PosLinkDesignTokens.BorderColor, fieldShape)
                    .background(PosLinkDesignTokens.BorderColor, fieldShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PosLinkDesignTokens.FieldInnerHorizontalPadding),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
                Text(
                    text = stringResource(R.string.other),
                    style = fieldLabelStyle,
                    color = PosLinkDesignTokens.OnLightTextColor,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = marginGap * 2, top = marginGap)
                )
            }
        }
    )
}

@Composable
private fun CashbackConfirmBar(
    onSubmit: () -> Unit
) {
    val controlsEnabled = !LocalEntryInteractionLocked.current
    PosLinkLegacyThemeButton(
        text = stringResource(R.string.trans_confirm_btn),
        enabled = controlsEnabled,
        modifier = Modifier.fillMaxWidth(),
        onClick = onSubmit
    )
}
