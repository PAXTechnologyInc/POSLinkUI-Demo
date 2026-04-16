package com.paxus.pay.poslinkui.demo.entry.text.amount

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.compose.LocalEntryInteractionLocked
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSurfaceCard
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.device.DeviceLayoutSpec
import com.paxus.pay.poslinkui.demo.ui.device.DeviceProfileId
import com.paxus.pay.poslinkui.demo.ui.device.LocalDeviceLayoutSpec
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils
import com.paxus.pay.poslinkui.demo.utils.ViewUtils
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView
import kotlinx.coroutines.delay

data class TipInfo(val name: String?, val amount: String?)

/** Callbacks for tip / cashback-prompt flows shown by [TipScreen]. */
data class TipScreenCallbacks(
    val onTipOptionSelected: (Long) -> Unit,
    val onNoTipSelected: () -> Unit,
    val onConfirm: (Long, Boolean) -> Unit,
    val onError: (String) -> Unit
)

/** Amount, currency, and validation pattern for [TipScreen]. */
data class TipScreenAmountFields(
    val baseAmount: Long,
    val currency: String?,
    val maxLength: Int,
    val valuePattern: String?
)

/** Labels and preset tip options for [TipScreen]. */
data class TipScreenDisplayFields(
    val tipInfoList: List<TipInfo>,
    val tipName: String,
    val tipOptions: List<SelectOptionsView.Option>,
    val noTipEnabled: Boolean
)

/** Feature flags and logging for [TipScreen] parity / layout variants. */
data class TipScreenModeFlags(
    val isTipCentCase: Boolean = false,
    val parityLog: String = "TipDollar parity v3 active",
    val showAmountSection: Boolean = true,
    val forcePromptTitle: String? = null,
    val isCashbackPromptCase: Boolean = false,
    val noTipTriggersSubmit: Boolean = true
)

/**
 * Bundled inputs for [TipScreen] so call sites stay under Sonar parameter limits.
 */
data class TipScreenModel(
    val amount: TipScreenAmountFields,
    val display: TipScreenDisplayFields,
    val flags: TipScreenModeFlags,
    val callbacks: TipScreenCallbacks
)

private data class TipScreenLayoutMetrics(
    val topOffset: Dp,
    val centHorizontalInset: Dp,
    val optionSpacing: Dp,
    val optionHeight: Dp,
    val noTipHeight: Dp,
    val optionsToInputSpacing: Dp,
    val inputToConfirmSpacing: Dp,
    val inputHeight: Dp,
    val amountToTitleSpacing: Dp,
    val titleToOptionsSpacing: Dp
)

private fun tipScreenOptionHeight(
    isTipCentCase: Boolean,
    compactNoTipLayout: Boolean
): Dp = when {
    isTipCentCase -> 65.dp
    compactNoTipLayout -> PosLinkDesignTokens.ButtonHeight + 20.dp
    else -> PosLinkDesignTokens.ButtonHeight + 24.dp
}

private fun tipScreenNoTipHeight(isTipCentCase: Boolean): Dp =
    if (isTipCentCase) {
        PosLinkDesignTokens.ButtonHeight
    } else {
        PosLinkDesignTokens.ButtonHeight + PosLinkDesignTokens.SpaceBetweenTextView
    }

private fun tipScreenInputHeight(
    isTipCentCase: Boolean,
    compactNoTipLayout: Boolean
): Dp = when {
    isTipCentCase -> PosLinkDesignTokens.ButtonHeight
    compactNoTipLayout -> PosLinkDesignTokens.ButtonHeight + 10.dp
    else -> PosLinkDesignTokens.ButtonHeight + 18.dp
}

private fun tipScreenSectionVerticalSpacing(
    isTipCentCase: Boolean,
    isCashbackPromptCase: Boolean,
    compactNoTipLayout: Boolean
): Dp = when {
    isTipCentCase -> 0.dp
    isCashbackPromptCase -> PosLinkDesignTokens.SpaceBetweenTextView
    compactNoTipLayout -> PosLinkDesignTokens.DenseGridSpacing
    else -> PosLinkDesignTokens.SpaceBetweenTextView
}

private fun tipScreenTitleAdjacentSpacing(compactNoTipLayout: Boolean): Dp =
    if (compactNoTipLayout) PosLinkDesignTokens.DenseGridSpacing
    else PosLinkDesignTokens.SpaceBetweenTextView

/**
 * Derives spacing and control heights for tip/cashback-prompt layouts from device profile and case flags.
 */
private fun tipScreenLayoutMetrics(
    isTipCentCase: Boolean,
    isCashbackPromptCase: Boolean,
    compactNoTipLayout: Boolean,
    spec: DeviceLayoutSpec
): TipScreenLayoutMetrics {
    val topOffset = if (isTipCentCase && spec.profileId == DeviceProfileId.A920_CLASS) {
        (-spec.screenVerticalPaddingDp).dp
    } else {
        0.dp
    }
    val centHorizontalInset = if (isTipCentCase && spec.profileId == DeviceProfileId.A920_CLASS) {
        (28 - spec.screenHorizontalPaddingDp).coerceAtLeast(0).dp
    } else {
        0.dp
    }
    val optionSpacing = when {
        isTipCentCase -> PosLinkDesignTokens.InlineSpacing
        isCashbackPromptCase -> PosLinkDesignTokens.ControlGutter
        compactNoTipLayout -> PosLinkDesignTokens.DenseGridSpacing
        else -> PosLinkDesignTokens.ControlGutter
    }
    val optionHeight = tipScreenOptionHeight(isTipCentCase, compactNoTipLayout)
    val noTipHeight = tipScreenNoTipHeight(isTipCentCase)
    val optionsToInputSpacing = tipScreenSectionVerticalSpacing(
        isTipCentCase,
        isCashbackPromptCase,
        compactNoTipLayout
    )
    val inputToConfirmSpacing = tipScreenSectionVerticalSpacing(
        isTipCentCase,
        isCashbackPromptCase,
        compactNoTipLayout
    )
    val inputHeight = tipScreenInputHeight(isTipCentCase, compactNoTipLayout)
    val amountToTitleSpacing = tipScreenTitleAdjacentSpacing(compactNoTipLayout)
    val titleToOptionsSpacing = tipScreenTitleAdjacentSpacing(compactNoTipLayout)
    return TipScreenLayoutMetrics(
        topOffset = topOffset,
        centHorizontalInset = centHorizontalInset,
        optionSpacing = optionSpacing,
        optionHeight = optionHeight,
        noTipHeight = noTipHeight,
        optionsToInputSpacing = optionsToInputSpacing,
        inputToConfirmSpacing = inputToConfirmSpacing,
        inputHeight = inputHeight,
        amountToTitleSpacing = amountToTitleSpacing,
        titleToOptionsSpacing = titleToOptionsSpacing
    )
}

/**
 * Compose screen for tip entry.
 */
@Composable
fun TipScreen(model: TipScreenModel) {
    val baseAmount = model.amount.baseAmount
    val currency = model.amount.currency
    val maxLength = model.amount.maxLength
    val valuePattern = model.amount.valuePattern
    val tipInfoList = model.display.tipInfoList
    val tipName = model.display.tipName
    val tipOptions = model.display.tipOptions
    val noTipEnabled = model.display.noTipEnabled
    val isTipCentCase = model.flags.isTipCentCase
    val parityLog = model.flags.parityLog
    val showAmountSection = model.flags.showAmountSection
    val forcePromptTitle = model.flags.forcePromptTitle
    val isCashbackPromptCase = model.flags.isCashbackPromptCase
    val noTipTriggersSubmit = model.flags.noTipTriggersSubmit
    val onTipOptionSelected = model.callbacks.onTipOptionSelected
    val onNoTipSelected = model.callbacks.onNoTipSelected
    val onConfirm = model.callbacks.onConfirm
    val onError = model.callbacks.onError
    val activity = LocalContext.current as? FragmentActivity
    val spec = LocalDeviceLayoutSpec.current
    var displayValue by remember(isCashbackPromptCase, currency) {
        mutableStateOf(if (isCashbackPromptCase) "" else CurrencyUtils.convert(0L, currency))
    }
    var tipFieldModified by remember { mutableStateOf(false) }
    val compactNoTipLayout = tipOptions.isNotEmpty() && !noTipEnabled
    val m = tipScreenLayoutMetrics(
        isTipCentCase = isTipCentCase,
        isCashbackPromptCase = isCashbackPromptCase,
        compactNoTipLayout = compactNoTipLayout,
        spec = spec
    )
    val promptTitle = forcePromptTitle ?: (if (tipOptions.isNotEmpty()) "Select " else "Enter ") + tipName
    val fieldShape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius)
    TipScreenParityLaunchedEffect(
        isTipCentCase = isTipCentCase,
        activity = activity,
        parityLog = parityLog
    )
    TipScreenMainColumn(
        TipScreenMainColumnParams(
            bundle = TipScreenMainColumnBundle(
                m = m,
                showAmountSection = showAmountSection,
                baseAmount = baseAmount,
                currency = currency,
                tipInfoList = tipInfoList,
                promptTitle = promptTitle,
                tipOptions = tipOptions
            ),
            field = TipScreenMainColumnField(
                isTipCentCase = isTipCentCase,
                isCashbackPromptCase = isCashbackPromptCase,
                noTipEnabled = noTipEnabled,
                displayValue = displayValue,
                tipFieldModified = tipFieldModified,
                currencyForConvert = currency,
                maxLength = maxLength
            ),
            edit = TipScreenMainColumnEditActions(
                valuePattern = valuePattern,
                fieldShape = fieldShape,
                onDisplayValueChange = { displayValue = it },
                onTipFieldEdited = { tipFieldModified = true }
            ),
            card = TipScreenMainColumnCardActions(
                onTipAmountCardChosen = { amt ->
                    displayValue = CurrencyUtils.convert(amt, currency)
                    tipFieldModified = false
                    onTipOptionSelected(amt)
                },
                onNoTipCardClick = {
                    if (noTipTriggersSubmit) {
                        onNoTipSelected()
                    } else {
                        displayValue = ""
                        tipFieldModified = false
                        onTipOptionSelected(0L)
                    }
                }
            ),
            submit = TipScreenMainColumnSubmitActions(
                onConfirm = onConfirm,
                onError = onError
            )
        )
    )
}

private data class TipScreenMainColumnBundle(
    val m: TipScreenLayoutMetrics,
    val showAmountSection: Boolean,
    val baseAmount: Long,
    val currency: String?,
    val tipInfoList: List<TipInfo>,
    val promptTitle: String,
    val tipOptions: List<SelectOptionsView.Option>
)

private data class TipScreenMainColumnField(
    val isTipCentCase: Boolean,
    val isCashbackPromptCase: Boolean,
    val noTipEnabled: Boolean,
    val displayValue: String,
    val tipFieldModified: Boolean,
    val currencyForConvert: String?,
    val maxLength: Int
)

private data class TipScreenMainColumnEditActions(
    val valuePattern: String?,
    val fieldShape: RoundedCornerShape,
    val onDisplayValueChange: (String) -> Unit,
    val onTipFieldEdited: () -> Unit
)

private data class TipScreenMainColumnCardActions(
    val onTipAmountCardChosen: (Long) -> Unit,
    val onNoTipCardClick: () -> Unit
)

private data class TipScreenMainColumnSubmitActions(
    val onConfirm: (Long, Boolean) -> Unit,
    val onError: (String) -> Unit
)

/**
 * Groups [TipScreenMainColumn] inputs to satisfy Sonar parameter-count limits.
 */
private data class TipScreenMainColumnParams(
    val bundle: TipScreenMainColumnBundle,
    val field: TipScreenMainColumnField,
    val edit: TipScreenMainColumnEditActions,
    val card: TipScreenMainColumnCardActions,
    val submit: TipScreenMainColumnSubmitActions
)

@Composable
private fun TipScreenParityLaunchedEffect(
    isTipCentCase: Boolean,
    activity: FragmentActivity?,
    parityLog: String
) {
    LaunchedEffect(Unit) {
        if (isTipCentCase) {
            // Keep TIP cent screenshot parity stable when host watermark is applied asynchronously.
            repeat(8) { idx ->
                activity?.let { ViewUtils().removeWaterMarkView(it) }
                if (idx < 7) delay(150)
            }
        }
        Logger.i(parityLog)
    }
}

@Composable
private fun TipScreenMainColumn(p: TipScreenMainColumnParams) {
    val b = p.bundle
    val f = p.field
    val e = p.edit
    val c = p.card
    val s = p.submit
    val m = b.m
    val showAmountSection = b.showAmountSection
    val baseAmount = b.baseAmount
    val currency = b.currency
    val tipInfoList = b.tipInfoList
    val promptTitle = b.promptTitle
    val tipOptions = b.tipOptions
    val isTipCentCase = f.isTipCentCase
    val isCashbackPromptCase = f.isCashbackPromptCase
    val noTipEnabled = f.noTipEnabled
    val displayValue = f.displayValue
    val onDisplayValueChange = e.onDisplayValueChange
    val onTipFieldEdited = e.onTipFieldEdited
    val tipFieldModified = f.tipFieldModified
    val currencyForConvert = f.currencyForConvert
    val maxLength = f.maxLength
    val valuePattern = e.valuePattern
    val fieldShape = e.fieldShape
    val onTipAmountCardChosen = c.onTipAmountCardChosen
    val onNoTipCardClick = c.onNoTipCardClick
    val onConfirm = s.onConfirm
    val onError = s.onError
    Column(
        modifier = Modifier
            .offset(y = m.topOffset)
            .padding(horizontal = m.centHorizontalInset)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        if (showAmountSection) {
            TipAmountIntroSection(
                baseAmount = baseAmount,
                currency = currency,
                tipInfoList = tipInfoList,
                amountToTitleSpacing = m.amountToTitleSpacing
            )
        }
        PosLinkText(
            text = promptTitle,
            role = PosLinkTextRole.ScreenTitle
        )
        Spacer(modifier = Modifier.height(m.titleToOptionsSpacing))
        if (tipOptions.isNotEmpty()) {
            TipTipOptionsSection(
                TipTipOptionsSectionParams(
                    layout = TipTipOptionsLayoutParams(
                        optionSpacing = m.optionSpacing,
                        optionHeight = m.optionHeight,
                        noTipHeight = m.noTipHeight
                    ),
                    behavior = TipTipOptionsBehaviorParams(
                        tipOptions = tipOptions,
                        isCashbackPromptCase = isCashbackPromptCase,
                        noTipEnabled = noTipEnabled,
                        onTipAmountChosen = onTipAmountCardChosen,
                        onNoTipClick = onNoTipCardClick
                    )
                )
            )
        }
        Spacer(modifier = Modifier.height(m.optionsToInputSpacing))
        TipValueInputSection(
            TipValueInputSectionParams(
                mode = TipValueInputMode(
                    isTipCentCase = isTipCentCase,
                    isCashbackPromptCase = isCashbackPromptCase,
                    tipOptions = tipOptions
                ),
                values = TipValueInputValues(
                    displayValue = displayValue,
                    onDisplayValueChange = onDisplayValueChange,
                    onTipFieldEdited = onTipFieldEdited,
                    currency = currencyForConvert,
                    maxLength = maxLength,
                    inputHeight = m.inputHeight,
                    fieldShape = fieldShape
                )
            )
        )
        Spacer(modifier = Modifier.height(m.inputToConfirmSpacing))
        TipConfirmBar(
            valuePattern = valuePattern,
            displayValue = displayValue,
            tipFieldModified = tipFieldModified,
            onConfirm = onConfirm,
            onError = onError
        )
    }
}

@Composable
private fun TipConfirmBar(
    valuePattern: String?,
    displayValue: String,
    tipFieldModified: Boolean,
    onConfirm: (Long, Boolean) -> Unit,
    onError: (String) -> Unit
) {
    val controlsEnabled = !LocalEntryInteractionLocked.current
    val promptInputStr = stringResource(R.string.prompt_input)
    PosLinkPrimaryButton(
        text = stringResource(R.string.confirm),
        enabled = controlsEnabled,
        onClick = {
            val value = if (displayValue.isBlank()) 0L else CurrencyUtils.parse(displayValue)
            val lengthList = valuePattern
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    runCatching { ValuePatternUtils.getLengthList(it) }
                        .getOrElse { mutableListOf<Int?>() }
                }
                ?: mutableListOf<Int?>(0)
            if (value == 0L && !lengthList.contains(0)) onError(promptInputStr)
            else onConfirm(value, tipFieldModified)
        }
    )
}

/**
 * Amount label, formatted total, and optional per-line tip breakdown before the main tip prompt.
 */
@Composable
private fun TipAmountIntroSection(
    baseAmount: Long,
    currency: String?,
    tipInfoList: List<TipInfo>,
    amountToTitleSpacing: Dp
) {
    PosLinkText(
        text = "Amount",
        role = PosLinkTextRole.Body,
    )
    PosLinkText(
        text = CurrencyUtils.convert(baseAmount, currency),
        role = PosLinkTextRole.ScreenTitle
    )
    if (tipInfoList.isNotEmpty()) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.ControlGutter)) {
            itemsIndexed(tipInfoList) { _, info ->
                PosLinkText(
                    text = "${info.name}: ${info.amount}",
                    role = PosLinkTextRole.Supporting
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(amountToTitleSpacing))
}

private data class TipTipOptionsLayoutParams(
    val optionSpacing: Dp,
    val optionHeight: Dp,
    val noTipHeight: Dp
)

private data class TipTipOptionsBehaviorParams(
    val tipOptions: List<SelectOptionsView.Option>,
    val isCashbackPromptCase: Boolean,
    val noTipEnabled: Boolean,
    val onTipAmountChosen: (Long) -> Unit,
    val onNoTipClick: () -> Unit
)

private data class TipTipOptionsSectionParams(
    val layout: TipTipOptionsLayoutParams,
    val behavior: TipTipOptionsBehaviorParams
)

@Composable
private fun TipTipOptionsSection(p: TipTipOptionsSectionParams) {
    val layout = p.layout
    val behavior = p.behavior
    if (behavior.isCashbackPromptCase) {
        TipCashbackPromptTipOptionRows(
            tipOptions = behavior.tipOptions,
            optionSpacing = layout.optionSpacing,
            optionHeight = layout.optionHeight,
            onTipAmountChosen = behavior.onTipAmountChosen
        )
    } else {
        TipStandardTipOptionRow(
            tipOptions = behavior.tipOptions,
            optionSpacing = layout.optionSpacing,
            optionHeight = layout.optionHeight,
            onTipAmountChosen = behavior.onTipAmountChosen
        )
    }
    if (behavior.noTipEnabled) {
        TipNoTipChoiceCard(
            isCashbackPromptCase = behavior.isCashbackPromptCase,
            noTipHeight = layout.noTipHeight,
            onNoTipClick = behavior.onNoTipClick
        )
    }
}

@Composable
private fun TipCashbackPromptTipOptionRows(
    tipOptions: List<SelectOptionsView.Option>,
    optionSpacing: Dp,
    optionHeight: Dp,
    onTipAmountChosen: (Long) -> Unit
) {
    val firstRow = tipOptions.take(2)
    val secondRow = tipOptions.drop(2).take(1)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(optionSpacing)
    ) {
        firstRow.forEach { opt ->
            TipOptionSurfaceCard(
                modifier = Modifier.weight(1f),
                opt = opt,
                optionHeight = optionHeight,
                onTipAmountChosen = onTipAmountChosen,
                showSubtitle = false
            )
        }
    }
    if (secondRow.isNotEmpty()) {
        Spacer(modifier = Modifier.height(optionSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(optionSpacing)
        ) {
            secondRow.forEach { opt ->
                TipOptionSurfaceCard(
                    modifier = Modifier.weight(1f),
                    opt = opt,
                    optionHeight = optionHeight,
                    onTipAmountChosen = onTipAmountChosen,
                    showSubtitle = false
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun TipStandardTipOptionRow(
    tipOptions: List<SelectOptionsView.Option>,
    optionSpacing: Dp,
    optionHeight: Dp,
    onTipAmountChosen: (Long) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(optionSpacing)
    ) {
        tipOptions.take(3).forEach { opt ->
            TipOptionSurfaceCard(
                modifier = Modifier.weight(1f),
                opt = opt,
                optionHeight = optionHeight,
                onTipAmountChosen = onTipAmountChosen,
                showSubtitle = true
            )
        }
    }
}

@Composable
private fun TipOptionSurfaceCard(
    modifier: Modifier = Modifier,
    opt: SelectOptionsView.Option,
    optionHeight: Dp,
    onTipAmountChosen: (Long) -> Unit,
    showSubtitle: Boolean
) {
    val controlsEnabled = !LocalEntryInteractionLocked.current
    val amt = (opt.value as? Long) ?: 0L
    PosLinkSurfaceCard(
        modifier = modifier
            .height(optionHeight)
            .clickable(enabled = controlsEnabled) { onTipAmountChosen(amt) }
    ) {
        PosLinkText(
            text = opt.title ?: "",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        if (showSubtitle && !opt.subtitle.isNullOrBlank()) {
            PosLinkText(
                text = opt.subtitle ?: "",
                role = PosLinkTextRole.Supporting,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TipNoTipChoiceCard(
    isCashbackPromptCase: Boolean,
    noTipHeight: Dp,
    onNoTipClick: () -> Unit
) {
    val controlsEnabled = !LocalEntryInteractionLocked.current
    Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
    PosLinkSurfaceCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(noTipHeight)
            .clickable(enabled = controlsEnabled) { onNoTipClick() }
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            PosLinkText(
                text = if (isCashbackPromptCase) "No Thanks!!" else "No Tip",
                textAlign = TextAlign.Center
            )
        }
    }
}

private data class TipValueInputMode(
    val isTipCentCase: Boolean,
    val isCashbackPromptCase: Boolean,
    val tipOptions: List<SelectOptionsView.Option>
)

private data class TipValueInputValues(
    val displayValue: String,
    val onDisplayValueChange: (String) -> Unit,
    val onTipFieldEdited: () -> Unit,
    val currency: String?,
    val maxLength: Int,
    val inputHeight: Dp,
    val fieldShape: RoundedCornerShape
)

private data class TipValueInputSectionParams(
    val mode: TipValueInputMode,
    val values: TipValueInputValues
)

@Composable
private fun TipValueInputSection(p: TipValueInputSectionParams) {
    if (p.mode.isTipCentCase) {
        TipCentAmountTextField(p.mode, p.values)
    } else {
        TipStandardAmountOutlinedField(p.mode, p.values)
    }
}

@Composable
private fun TipCentAmountTextField(mode: TipValueInputMode, v: TipValueInputValues) {
    val interactionLocked = LocalEntryInteractionLocked.current
    var textFieldValue by remember(v.displayValue) {
        mutableStateOf(
            TextFieldValue(
                text = v.displayValue,
                selection = TextRange(v.displayValue.length)
            )
        )
    }
    BasicTextField(
        value = textFieldValue,
        readOnly = interactionLocked,
        onValueChange = { newValue ->
            v.onTipFieldEdited()
            val digits = newValue.text.replace("[^0-9]".toRegex(), "")
            if (digits.length <= v.maxLength) {
                val normalized = CurrencyUtils.convert(
                    digits.ifEmpty { "0" }.toLongOrNull() ?: 0L,
                    v.currency
                )
                v.onDisplayValueChange(normalized)
                textFieldValue = TextFieldValue(
                    text = normalized,
                    selection = TextRange(normalized.length)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(v.inputHeight),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            textAlign = TextAlign.Center,
            color = Color(0xFF222222)
        ),
        cursorBrush = SolidColor(Color(0xFF66A579)),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(v.inputHeight)
                    .border(width = 2.dp, color = Color(0xFFDBD4D9), shape = v.fieldShape)
                    .background(color = Color(0xFFDBD4D9), shape = v.fieldShape)
                    .padding(horizontal = PosLinkDesignTokens.FieldInnerHorizontalPadding),
                contentAlignment = Alignment.Center
            ) {
                if (mode.tipOptions.isNotEmpty() && CurrencyUtils.parse(v.displayValue) == 0L) {
                    PosLinkText(text = "Other", role = PosLinkTextRole.Supporting)
                }
                innerTextField()
            }
        }
    )
}

@Composable
private fun TipStandardAmountOutlinedField(mode: TipValueInputMode, v: TipValueInputValues) {
    val interactionLocked = LocalEntryInteractionLocked.current
    OutlinedTextField(
        value = v.displayValue,
        enabled = !interactionLocked,
        onValueChange = {
            v.onTipFieldEdited()
            val digits = it.replace("[^0-9]".toRegex(), "")
            if (digits.length <= v.maxLength) {
                v.onDisplayValueChange(
                    if (mode.isCashbackPromptCase && digits.isEmpty()) {
                        ""
                    } else {
                        CurrencyUtils.convert(digits.ifEmpty { "0" }.toLongOrNull() ?: 0L, v.currency)
                    }
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(v.inputHeight),
        label = { PosLinkText(text = "Other", role = PosLinkTextRole.Supporting) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFDBD4D9),
            unfocusedContainerColor = Color(0xFFDBD4D9),
            focusedBorderColor = Color(0xFFDBD4D9),
            unfocusedBorderColor = Color(0xFFDBD4D9)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
