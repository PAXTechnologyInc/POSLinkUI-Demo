package com.paxus.pay.poslinkui.demo.entry.compose

import android.os.Bundle
import android.graphics.Rect
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import java.util.Locale
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.PoslinkEntry
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.signature.ElectronicSignatureView
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButtonVariant
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.paxus.pay.poslinkui.demo.utils.DateUtils
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.Toast
import com.paxus.pay.poslinkui.demo.utils.Toast.TYPE
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel

/** First-row button count in SHOW_DIALOG / ShowTextBox legacy 2-column layouts. */
private const val POSLINK_DIALOG_TOP_ROW_SLOT_COUNT = 2

private class PoslinkSignatureViewRef {
    var view: ElectronicSignatureView? = null
}

private data class PoslinkShowDialogOption(
    val index: Int,
    val label: String
)

@Composable
private fun PoslinkShowDialogButton(
    rawText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val cells = remember(rawText) { buildPoslinkTitleLikeCells(rawText) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Surface(
        onClick = onClick,
        modifier = modifier.height(dimensionResource(R.dimen.button_height)),
        enabled = enabled,
        shape = RoundedCornerShape(PosLinkDesignTokens.LegacyButtonCornerRadius),
        color = when {
            !enabled -> PosLinkDesignTokens.DisabledColor
            isPressed -> PosLinkDesignTokens.LegacyButtonPressedColor
            else -> PosLinkDesignTokens.PrimaryColor
        },
        interactionSource = interactionSource,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PoslinkLegacyInlineTextContent(
                cells = cells,
                allowWrap = true,
                textColor = if (enabled) {
                    PosLinkDesignTokens.PrimaryTextColor
                } else {
                    PosLinkDesignTokens.PrimaryTextColor.copy(alpha = 0.38f)
                }
            )
        }
    }
}

private fun resolvePoslinkShowDialogVisibleOptions(opts: Array<String?>): List<PoslinkShowDialogOption> {
    return opts
        .take(4)
        .takeWhile { !it.isNullOrBlank() }
        .mapIndexed { index, label ->
            PoslinkShowDialogOption(
                index = index + 1,
                label = label.orEmpty()
            )
        }
}

private fun resolvePoslinkShowDialogKeyIndex(keyCode: Int): Int? = when (keyCode) {
    KeyEvent.KEYCODE_1 -> 1
    KeyEvent.KEYCODE_2 -> 2
    KeyEvent.KEYCODE_3 -> 3
    KeyEvent.KEYCODE_4 -> 4
    else -> null
}

private fun submitPoslinkShowDialogSelection(viewModel: EntryViewModel, index: Int) {
    viewModel.sendNext(
        Bundle().apply {
            putInt(EntryRequest.PARAM_INDEX, index)
        }
    )
}

@Composable
private fun PoslinkShowDialogOptionsLayout(
    options: List<PoslinkShowDialogOption>,
    viewModel: EntryViewModel,
    buttonsEnabled: Boolean
) {
    val buttonMargin = dimensionResource(R.dimen.margin_gap)
    Column(modifier = Modifier.fillMaxWidth()) {
        options.chunked(POSLINK_DIALOG_TOP_ROW_SLOT_COUNT).forEach { rowOptions ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = buttonMargin),
                horizontalArrangement = Arrangement.spacedBy(buttonMargin * 2)
            ) {
                rowOptions.forEach { option ->
                    PoslinkShowDialogButton(
                        rawText = option.label,
                        onClick = {
                            submitPoslinkShowDialogSelection(viewModel, option.index)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = buttonMargin),
                        enabled = buttonsEnabled
                    )
                }
            }
        }
    }
}

/**
 * Title and option buttons for [PoslinkEntry.ACTION_SHOW_DIALOG].
 */
@Composable
private fun PoslinkShowDialogContent(
    title: String,
    opts: Array<String?>,
    viewModel: EntryViewModel,
    buttonsEnabled: Boolean
) {
    val visibleOptions = remember(opts) { resolvePoslinkShowDialogVisibleOptions(opts) }
    if (visibleOptions.isNotEmpty() && buttonsEnabled) {
        LaunchedEffect(visibleOptions, buttonsEnabled) {
            viewModel.keyEvents.collect { keyCode ->
                val selectedIndex = resolvePoslinkShowDialogKeyIndex(keyCode) ?: return@collect
                val option = visibleOptions.firstOrNull { it.index == selectedIndex } ?: return@collect
                submitPoslinkShowDialogSelection(viewModel, option.index)
            }
        }
    }
    Column(Modifier.verticalScroll(rememberScrollState())) {
        if (title.isNotBlank()) {
            PoslinkShowDialogTitleLikeText(raw = title, supportLineSep = true)
        }
        if (visibleOptions.isNotEmpty()) {
            PoslinkShowDialogOptionsLayout(
                options = visibleOptions,
                viewModel = viewModel,
                buttonsEnabled = buttonsEnabled
            )
        }
    }
}

@Composable
private fun PoslinkRouteShowThankYou(extras: Bundle, viewModel: EntryViewModel, buttonsEnabled: Boolean) {
    val t = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val m1 = extras.getString(EntryExtraData.PARAM_MESSAGE_1).orEmpty()
    val m2 = extras.getString(EntryExtraData.PARAM_MESSAGE_2).orEmpty()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.fillMaxWidth()) {
            if (t.isNotBlank()) {
                PoslinkLegacyTitleLikeText(
                    raw = t,
                    supportLineSep = true,
                    allowWrap = true,
                    maxLines = 4,
                    allowCharacterWrap = true
                )
            }
            if (m1.isNotBlank()) {
                PoslinkLegacyValueLikeText(
                    raw = m1,
                    allowWrap = true,
                    maxLines = 4,
                    allowCharacterWrap = true
                )
            }
            if (m2.isNotBlank()) {
                PoslinkLegacyValueLikeText(
                    raw = m2,
                    allowWrap = true,
                    maxLines = 4,
                    allowCharacterWrap = true
                )
            }
        }
    }
}

@Composable
private fun PoslinkRouteInputText(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    val minL = extras.getString(EntryExtraData.PARAM_MIN_LENGTH)?.toIntOrNull() ?: 0
    val maxL = extras.getString(EntryExtraData.PARAM_MAX_LENGTH)?.toIntOrNull() ?: 64
    PoslinkTypedInputContent(
        title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty(),
        body = null,
        inputType = extras.getString(EntryExtraData.PARAM_INPUT_TYPE).orEmpty(),
        minLength = minL,
        maxLength = maxL,
        defaultValue = extras.getString(EntryExtraData.PARAM_DEFAULT_VALUE).orEmpty(),
        onSubmit = { payload ->
            val b = Bundle()
            when (payload) {
                is Long -> b.putLong(EntryRequest.PARAM_INPUT_VALUE, payload)
                else -> b.putString(EntryRequest.PARAM_INPUT_VALUE, payload.toString())
            }
            viewModel.sendNext(b)
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
private fun PoslinkRouteShowMessage(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val tax = extras.getString(EntryExtraData.PARAM_TAX_LINE).orEmpty()
    val total = extras.getString(EntryExtraData.PARAM_TOTAL_LINE).orEmpty()
    val rawMessageList = resolvePoslinkShowMessageRaw(extras)
    val list = parsePoslinkMessageList(rawMessageList)
    // 无 messageList 载荷时不从其它字段“猜”正文，避免 message 区出现非预期占位文案
    val fallbackText =
        if (rawMessageList.isNotBlank()) resolvePoslinkShowMessageFallbackText(rawMessageList) else null
    val displayMessage = when {
        list.isNotBlank() -> list
        rawMessageList.isNotBlank() && fallbackText != null -> fallbackText.text
        else -> ""
    }
    if (list.isBlank() && fallbackText != null) {
        Logger.w(
            "SHOW_MESSAGE parsed empty, fallback used. source=%s constKey=%s rawLen=%d keys=%s rawPreview=%s",
            fallbackText.source,
            EntryExtraData.PARAM_MESSAGE_LIST,
            rawMessageList.length,
            extras.keySet().joinToString(","),
            rawMessageList.take(120)
        )
    } else if (list.isBlank()) {
        Logger.w(
            "SHOW_MESSAGE parsed empty, fallback missing. constKey=%s rawLen=%d keys=%s rawPreview=%s",
            EntryExtraData.PARAM_MESSAGE_LIST,
            rawMessageList.length,
            extras.keySet().joinToString(","),
            rawMessageList.take(120)
        )
    } else {
        Logger.i(
            "SHOW_MESSAGE parsed lines=%d rawLen=%d",
            list.lines().count { it.isNotBlank() },
            rawMessageList.length
        )
    }
    val image = resolvePoslinkDisplayImage(
        extras = extras,
        fallbackRawPayload = rawMessageList
    )
    val resolvedShowMessageImageUrl = image.url.ifBlank {
        resolveShowMessageDrawableUri(
            activity = activity,
            imageDesc = image.desc
        )
    }
    PoslinkMessageDisplayLayout(
        PoslinkMessageDisplayLayoutParams(
            body = PoslinkMessageDisplayBodyParams(
                title = title,
                messageText = displayMessage,
                imageUrl = resolvedShowMessageImageUrl,
                imageDesc = image.desc
            ),
            footer = PoslinkMessageDisplayFooterParams(
                tax = tax,
                total = total,
                visualMode = PoslinkMessageVisualMode.ShowMessageLegacy,
                showConfirmButton = false,
                onConfirm = { viewModel.sendNext(null) }
            )
        )
    )
}

@Composable
private fun PoslinkRouteShowItem(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val tax = extras.getString(EntryExtraData.PARAM_TAX_LINE).orEmpty()
    val total = extras.getString(EntryExtraData.PARAM_TOTAL_LINE).orEmpty()
    val currencySymbol = extras.getString(EntryExtraData.PARAM_CURRENCY_SYMBOL).orEmpty()
    val rawItems = resolvePoslinkShowItemRaw(extras)
    val normalizedRaw = remember(rawItems) { normalizePoslinkPayload(rawItems) }
    val parsedItems = remember(normalizedRaw) {
        val strictItems = parsePoslinkShowItemListFromJson(normalizedRaw)
        if (strictItems.isNotEmpty()) strictItems else parsePoslinkShowItemListLoose(normalizedRaw)
    }
    val fallbackText = remember(normalizedRaw, currencySymbol, parsedItems) {
        if (parsedItems.isNotEmpty()) "" else parsePoslinkShowItemList(normalizedRaw, currencySymbol)
    }
    PoslinkShowItemLegacyScreen(
        title = title,
        tax = tax,
        total = total,
        currencySymbol = currencySymbol,
        items = parsedItems,
        fallbackText = fallbackText
    )
}

@Composable
private fun PoslinkShowItemLegacyScreen(
    title: String,
    tax: String,
    total: String,
    currencySymbol: String,
    items: List<PoslinkItemDetail>,
    fallbackText: String
) {
    val subtitleSize = dimensionResource(R.dimen.text_size_subtitle).value.sp
    // golive TextShowingUtils default title size becomes FONT_NORMAL_SP (24sp).
    val titleSize = 24.sp
    // Match legacy darker divider look (avoid bright white separators).
    val dividerColor = Color(0x66000000)
    val symbol = currencySymbol.ifBlank { "$" }
    Column(modifier = Modifier.fillMaxSize()) {
        if (title.isNotBlank()) {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                color = PosLinkDesignTokens.PrimaryTextColor,
                textAlign = TextAlign.Center,
                fontSize = titleSize
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(dividerColor)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (items.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(items) { index, item ->
                        PoslinkShowItemRow(
                            index = index,
                            item = item,
                            symbol = symbol,
                            subtitleSize = subtitleSize
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(dividerColor)
                        )
                    }
                }
            } else if (fallbackText.isNotBlank()) {
                Text(
                    text = fallbackText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    fontSize = subtitleSize
                )
            }
        }
        if (tax.isNotBlank() || total.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .height(1.5.dp)
                    .background(Color(0x80000000))
            )
            if (tax.isNotBlank()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.detail_item_tax),
                        modifier = Modifier.padding(start = 24.dp),
                        color = PosLinkDesignTokens.PrimaryTextColor,
                        fontSize = subtitleSize,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = tax,
                        modifier = Modifier.padding(end = 24.dp),
                        color = PosLinkDesignTokens.PrimaryTextColor,
                        fontSize = subtitleSize,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            if (total.isNotBlank()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.pete_total),
                        modifier = Modifier.padding(start = 24.dp),
                        color = PosLinkDesignTokens.PrimaryTextColor,
                        fontSize = subtitleSize,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = total,
                        modifier = Modifier.padding(end = 24.dp),
                        color = PosLinkDesignTokens.PrimaryTextColor,
                        fontSize = subtitleSize,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun PoslinkShowItemRow(
    index: Int,
    item: PoslinkItemDetail,
    symbol: String,
    subtitleSize: androidx.compose.ui.unit.TextUnit
) {
    val name = item.productName?.takeIf { it.isNotBlank() } ?: "Item ${index + 1}"
    val unitDisplay = when (item.unit?.trim()) {
        "1" -> "1b"
        "2" -> "ft"
        else -> item.unit.orEmpty()
    }
    val quantityText = when {
        item.quantity.isNullOrBlank() -> ""
        unitDisplay.equals("x", ignoreCase = true) -> "x${item.quantity}"
        unitDisplay.isBlank() -> item.quantity.orEmpty()
        else -> "${item.quantity}$unitDisplay"
    }
    val unitPrice = formatPoslinkMoney(item.unitPrice ?: item.price, symbol)
    val quantityLine = when {
        quantityText.isBlank() || unitPrice.isBlank() -> ""
        unitDisplay.equals("x", ignoreCase = true) || unitDisplay.isBlank() -> "$quantityText  @$unitPrice"
        else -> "$quantityText  @$unitPrice/$unitDisplay"
    }
    val totalPrice = formatPoslinkMoney(item.price, symbol)
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${index + 1}. $name",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp),
                color = PosLinkDesignTokens.PrimaryTextColor,
                fontSize = subtitleSize
            )
            Text(
                text = totalPrice,
                modifier = Modifier.padding(end = 24.dp),
                color = PosLinkDesignTokens.PrimaryTextColor,
                fontSize = subtitleSize
            )
        }
        if (quantityLine.isNotBlank()) {
            Text(
                text = quantityLine,
                modifier = Modifier.padding(start = 50.dp),
                color = PosLinkDesignTokens.PrimaryTextColor,
                fontSize = subtitleSize
            )
        }
    }
}

@Composable
private fun PoslinkRouteShowInputTextBox(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    val minL = extras.getString(EntryExtraData.PARAM_MIN_LENGTH)?.toIntOrNull() ?: 0
    val maxL = extras.getString(EntryExtraData.PARAM_MAX_LENGTH)?.toIntOrNull() ?: 64
    PoslinkTypedInputContent(
        title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty(),
        body = extras.getString(EntryExtraData.PARAM_TEXT),
        inputType = extras.getString(EntryExtraData.PARAM_INPUT_TYPE).orEmpty(),
        minLength = minL,
        maxLength = maxL,
        defaultValue = extras.getString(EntryExtraData.PARAM_DEFAULT_VALUE).orEmpty(),
        onSubmit = { payload ->
            val b = Bundle()
            when (payload) {
                is Long -> b.putLong(EntryRequest.PARAM_INPUT_VALUE, payload)
                else -> b.putString(EntryRequest.PARAM_INPUT_VALUE, payload.toString())
            }
            viewModel.sendNext(b)
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

private const val POSLINK_DATE_HINT = "MM/DD/YYYY"
private const val POSLINK_TIME_HINT = "HH:MM:SS"
private const val POSLINK_PHONE_HINT = "(XXX)XXX-XXXX"
private const val POSLINK_SSN_HINT = "XXX-XX-XXXX"

private fun formatPoslinkInput(rawDigits: String, inputType: String): String = when (inputType) {
    "2" -> rawDigits.chunked(2).joinToString("/").take(POSLINK_DATE_HINT.length)
    "3" -> rawDigits.chunked(2).joinToString(":").take(POSLINK_TIME_HINT.length)
    "4" -> CurrencyUtils.convert(rawDigits.ifEmpty { "0" }.toLongOrNull() ?: 0L, "USD")
    "6" -> buildString {
        val d = rawDigits.take(10)
        if (d.isNotEmpty()) append("(")
        d.forEachIndexed { index, c ->
            append(c)
            if (index == 2) append(")")
            if (index == 5) append("-")
        }
    }
    "7" -> buildString {
        val d = rawDigits.take(9)
        d.forEachIndexed { index, c ->
            append(c)
            if (index == 2 || index == 4) append("-")
        }
    }
    else -> rawDigits
}

private fun poslinkInputHint(inputType: String): String = when (inputType) {
    "2" -> POSLINK_DATE_HINT
    "3" -> POSLINK_TIME_HINT
    "4" -> "$0.00"
    "6" -> POSLINK_PHONE_HINT
    "7" -> POSLINK_SSN_HINT
    else -> ""
}

private fun poslinkInputRawLengthLimit(inputType: String, maxLength: Int): Int = when (inputType) {
    "2" -> 8
    "3" -> 6
    "6" -> 10
    "7" -> 9
    else -> maxLength.coerceAtLeast(0)
}

@Composable
private fun PoslinkTypedInputContent(
    title: String,
    body: String?,
    inputType: String,
    minLength: Int,
    maxLength: Int,
    defaultValue: String,
    onSubmit: (Any) -> Unit,
    onError: (String) -> Unit
) {
    var localSubmitted by remember { mutableStateOf(false) }
    val interactionEnabled = !LocalEntryInteractionLocked.current && !localSubmitted
    val normalizedType = inputType.trim()
    val (effectiveMinLength, effectiveMaxLength) = remember(normalizedType, minLength, maxLength) {
        when (normalizedType) {
            "2" -> 8 to 8
            "3" -> 6 to 6
            "6" -> 10 to 10
            "7" -> 9 to 9
            else -> minLength to maxLength
        }
    }
    val initialDigits = remember(defaultValue, normalizedType) {
        defaultValue.filter { it.isDigit() }
    }
    val defaultDisplay = remember(defaultValue, normalizedType, initialDigits) {
        when (normalizedType) {
            "4" -> formatPoslinkInput(initialDigits, normalizedType)
            "2", "3", "6", "7" -> formatPoslinkInput(initialDigits, normalizedType)
            else -> defaultValue
        }
    }
    var fieldValue by remember(defaultDisplay) {
        mutableStateOf(
            TextFieldValue(
                text = defaultDisplay,
                selection = TextRange(defaultDisplay.length)
            )
        )
    }
    val displayValue = fieldValue.text
    val rawLengthLimit = poslinkInputRawLengthLimit(normalizedType, maxLength)
    val keyboardType = when (normalizedType) {
        "1", "2", "3", "4", "6", "7" -> KeyboardType.Number
        "5" -> KeyboardType.Password
        else -> KeyboardType.Text
    }
    val hint = poslinkInputHint(normalizedType)
    val promptInput = stringResource(R.string.prompt_input)
    val promptLength = stringResource(
        R.string.prompt_input_length,
        "$effectiveMinLength-$effectiveMaxLength"
    )
    val invalidDateMsg = stringResource(R.string.err_invalid_date)
    val invalidTimeMsg = stringResource(R.string.prompt_invalid_time)
    val promptInputType = stringResource(R.string.prompt_input_type)
    val titleMargin = PosLinkDesignTokens.SpaceBetweenTextView
    val bodyTopMargin = 8.dp
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Spacer(Modifier.height(titleMargin))
        Text(
            text = title.ifBlank { stringResource(R.string.enter) },
            modifier = Modifier.fillMaxWidth(),
            color = PosLinkDesignTokens.PrimaryTextColor,
            fontSize = 36.sp
        )
        Spacer(Modifier.height(titleMargin))
        if (!body.isNullOrBlank()) {
            Spacer(Modifier.height(bodyTopMargin))
            PoslinkLegacySimpleTextList(raw = body)
        }
        BasicTextField(
            value = fieldValue,
            onValueChange = { input ->
                if (normalizedType == "5") {
                    fieldValue = if (input.text.length <= rawLengthLimit) {
                        input
                    } else {
                        fieldValue
                    }
                    return@BasicTextField
                }
                if (normalizedType == "0" || normalizedType.isBlank()) {
                    fieldValue = if (input.text.length <= rawLengthLimit) {
                        input
                    } else {
                        fieldValue
                    }
                    return@BasicTextField
                }
                val previousText = fieldValue.text
                val previousDigits = previousText.filter { it.isDigit() }
                var digits = input.text.filter { it.isDigit() }
                // When user deletes a separator (e.g. '-' ')' '/'), legacy EditText+Watcher removes
                // the preceding digit as well; keep same behavior so backspace can clear continuously.
                val deletingSeparatorOnly =
                    input.text.length < previousText.length && digits.length == previousDigits.length
                if (deletingSeparatorOnly && previousDigits.isNotEmpty()) {
                    digits = previousDigits.dropLast(1)
                }
                digits = digits.take(rawLengthLimit)
                val formatted = formatPoslinkInput(digits, normalizedType)
                // Keep caret at end for watcher-style formatting parity with legacy EditText.
                fieldValue = TextFieldValue(
                    text = formatted,
                    selection = TextRange(formatted.length)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(PosLinkDesignTokens.buttonHeight()),
            singleLine = true,
            enabled = interactionEnabled,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (normalizedType == "5") {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF222222),
                textAlign = TextAlign.Center
            ),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFDBD4D9), RoundedCornerShape(PosLinkDesignTokens.CornerRadius))
                        .padding(horizontal = PosLinkDesignTokens.FieldInnerHorizontalPadding),
                    contentAlignment = Alignment.Center
                ) {
                    if (displayValue.isBlank() && hint.isNotBlank()) {
                        Text(
                            text = hint,
                            // Match golive TextField hint tint: pastel_text_color_on_light.
                            color = Color(0xFF222222),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    inner()
                }
            }
        )
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm).uppercase(Locale.ROOT),
            onClick = {
                val rawDigits = displayValue.filter { it.isDigit() }
                if (normalizedType == "4") {
                    val amount = CurrencyUtils.parse(displayValue)
                    if (minLength > 0 && amount <= 0L) {
                        onError(promptInput)
                        return@PosLinkPrimaryButton
                    }
                    localSubmitted = true
                    onSubmit(amount)
                    return@PosLinkPrimaryButton
                }
                val logicalValue = if (normalizedType in setOf("2", "3", "6", "7")) {
                    rawDigits
                } else {
                    displayValue.trim()
                }
                val length = logicalValue.length
                if (length < effectiveMinLength || length > effectiveMaxLength) {
                    onError(if (effectiveMinLength == effectiveMaxLength) promptInputType else promptLength)
                    return@PosLinkPrimaryButton
                }
                if (normalizedType == "2" && !DateUtils().isValidateDate(rawDigits)) {
                    onError(invalidDateMsg)
                    return@PosLinkPrimaryButton
                }
                if (normalizedType == "3" && !DateUtils().isValidateTime(rawDigits)) {
                    onError(invalidTimeMsg)
                    return@PosLinkPrimaryButton
                }
                localSubmitted = true
                onSubmit(logicalValue)
            },
            enabled = interactionEnabled,
            variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
        )
    }
}

@Composable
private fun PoslinkRouteShowSignatureBox(
    extras: Bundle,
    viewModel: EntryViewModel,
    buttonsEnabled: Boolean
) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val body = extras.getString(EntryExtraData.PARAM_TEXT).orEmpty()
    val signBoxStyle = extras.readCompatInt(
        keys = listOf(EntryExtraData.PARAM_SIGN_BOX, "signBox"),
        defaultValue = 0
    )
    val timeoutRaw = extras.readCompatLong(
        keys = listOf(EntryExtraData.PARAM_TIMEOUT, "timeout"),
        defaultValue = 23_000L
    )
    val timeoutSec = if (timeoutRaw >= 1000L) {
        (timeoutRaw / 1000L).toInt()
    } else {
        timeoutRaw.toInt()
    }.coerceAtLeast(0)
    val signatureViewRef = remember { PoslinkSignatureViewRef() }
    fun clearSignature() {
        signatureViewRef.view?.clear()
    }
    fun collectSignature(): ShortArray? {
        val signatureView = signatureViewRef.view ?: return null
        if (!signatureView.getTouched()) return null
        val pathPos = signatureView.getPathPos()
        var totalLen = 0
        for (segment in pathPos) {
            totalLen += segment.size
        }
        val signature = ShortArray(totalLen)
        var index = 0
        for (segment in pathPos) {
            for (value in segment) {
                signature[index++] = value.toInt().toShort()
            }
        }
        return signature
    }
    if (signBoxStyle == 2) {
        PoslinkSignBox2Screen(
            title = title,
            body = body,
            timeoutSec = timeoutSec,
            buttonsEnabled = buttonsEnabled,
            signatureViewRef = signatureViewRef,
            onCancel = {
                viewModel.sendNext(
                    Bundle().apply { putString(EntryRequest.PARAM_SIGN_STATUS, "DEMO_CANCEL") }
                )
            },
            onClear = { clearSignature() },
            onEnter = {
                val signature = collectSignature() ?: return@PoslinkSignBox2Screen
                viewModel.sendNext(
                    Bundle().apply {
                        putString(EntryRequest.PARAM_SIGN_STATUS, "DEMO_ACCEPT")
                        putShortArray(EntryRequest.PARAM_SIGNATURE, signature)
                    }
                )
            }
        )
    } else {
        PoslinkSignBox1Screen(
            title = title,
            body = body,
            timeoutSec = timeoutSec,
            buttonsEnabled = buttonsEnabled,
            signatureViewRef = signatureViewRef,
            onCancel = {
                viewModel.sendNext(
                    Bundle().apply { putString(EntryRequest.PARAM_SIGN_STATUS, "DEMO_CANCEL") }
                )
            },
            onClear = { clearSignature() },
            onEnter = {
                val signature = collectSignature() ?: return@PoslinkSignBox1Screen
                viewModel.sendNext(
                    Bundle().apply {
                        putString(EntryRequest.PARAM_SIGN_STATUS, "DEMO_ACCEPT")
                        putShortArray(EntryRequest.PARAM_SIGNATURE, signature)
                    }
                )
            }
        )
    }
}

@Composable
private fun PoslinkRouteShowDialogForm(extras: Bundle, viewModel: EntryViewModel, buttonsEnabled: Boolean) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val labels = extras.getStringArray(EntryExtraData.PARAM_LABELS) ?: emptyArray()
    val buttonType = extras.getString("buttonType")?.toIntOrNull()
        ?: extras.getString("PARAM_BUTTON_TYPE")?.toIntOrNull()
        ?: extras.getInt("buttonType", 1)
    var sel by remember(labels) { mutableStateOf(-1) }
    val initialChecks = remember(labels, buttonType, extras) {
        if (buttonType != 2) {
            emptySet()
        } else {
            parseShowDialogFormCheckedIndexes(
                labelsProperty = extras.getString("labelsProperty"),
                labelsSize = labels.size
            )
        }
    }
    var checkedSet by remember(labels, initialChecks) { mutableStateOf(initialChecks) }
    Column(Modifier.verticalScroll(rememberScrollState())) {
        if (title.isNotBlank()) PoslinkLegacyTitleLikeText(raw = title, supportLineSep = true)
        labels.forEachIndexed { i, lb ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (buttonType == 2) {
                    Checkbox(
                        checked = checkedSet.contains(i),
                        onCheckedChange = { checked ->
                            checkedSet = if (checked) {
                                checkedSet + i
                            } else {
                                checkedSet - i
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,
                            uncheckedColor = Color.White,
                            checkmarkColor = PosLinkDesignTokens.BackgroundColor
                        )
                    )
                } else {
                    RadioButton(selected = sel == i, onClick = { sel = i })
                }
                PosLinkText(text = lb ?: "", modifier = Modifier.padding(start = PosLinkDesignTokens.ControlGutter))
            }
        }
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        if (buttonType == 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.ControlGutter)
            ) {
                PosLinkPrimaryButton(
                    text = "RESET",
                    onClick = { checkedSet = emptySet() },
                    modifier = Modifier.weight(1f),
                    enabled = buttonsEnabled,
                    variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
                )
                PosLinkPrimaryButton(
                    text = stringResource(R.string.confirm),
                    onClick = {
                        val chosen = checkedSet.toList()
                            .sorted()
                            .mapNotNull(labels::getOrNull)
                            .joinToString(",")
                        viewModel.sendNext(
                            Bundle().apply {
                                putString(EntryRequest.PARAM_LABEL_SELECTED, chosen)
                            }
                        )
                    },
                    modifier = Modifier.weight(1f),
                    enabled = buttonsEnabled,
                    variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
                )
            }
        } else {
            PosLinkPrimaryButton(
                text = stringResource(R.string.confirm),
                onClick = {
                    val chosen = labels.getOrNull(sel).orEmpty()
                    viewModel.sendNext(
                        Bundle().apply { putString(EntryRequest.PARAM_LABEL_SELECTED, chosen) }
                    )
                },
                enabled = buttonsEnabled && sel >= 0,
                variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
            )
        }
    }
}

/**
 * POSLink category screens.
 */
@Composable
fun PoslinkEntryRoute(
    action: String?,
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel,
    isContentCleared: Boolean = false
) {
    if (isContentCleared) {
        Spacer(modifier = Modifier.fillMaxSize())
        return
    }
    val buttonsEnabled = !LocalEntryInteractionLocked.current
    when (action) {
        PoslinkEntry.ACTION_SHOW_DIALOG -> {
            val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
            val opts = extras.getStringArray(EntryExtraData.PARAM_OPTIONS) ?: emptyArray()
            PoslinkShowDialogContent(title = title, opts = opts, viewModel = viewModel, buttonsEnabled = buttonsEnabled)
        }
        PoslinkEntry.ACTION_SHOW_THANK_YOU -> PoslinkRouteShowThankYou(extras, viewModel, buttonsEnabled)
        PoslinkEntry.ACTION_INPUT_TEXT -> PoslinkRouteInputText(extras, activity, viewModel)
        PoslinkEntry.ACTION_SHOW_MESSAGE -> PoslinkRouteShowMessage(extras, activity, viewModel)
        PoslinkEntry.ACTION_SHOW_ITEM -> PoslinkRouteShowItem(extras, activity, viewModel)
        PoslinkEntry.ACTION_SHOW_TEXT_BOX -> PoslinkTextBoxButtons(extras, viewModel, buttonsEnabled)
        PoslinkEntry.ACTION_SHOW_INPUT_TEXT_BOX -> PoslinkRouteShowInputTextBox(extras, activity, viewModel)
        PoslinkEntry.ACTION_SHOW_SIGNATURE_BOX -> PoslinkRouteShowSignatureBox(extras, viewModel, buttonsEnabled)
        PoslinkEntry.ACTION_SHOW_DIALOG_FORM -> PoslinkRouteShowDialogForm(extras, viewModel, buttonsEnabled)
        else -> BoxWithCenterText(stringResource(R.string.poslink_action_not_handled, action ?: ""))
    }
}

@Composable
private fun PoslinkSignBox1Screen(
    title: String,
    body: String,
    timeoutSec: Int,
    buttonsEnabled: Boolean,
    signatureViewRef: PoslinkSignatureViewRef,
    onCancel: () -> Unit,
    onClear: () -> Unit,
    onEnter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PosLinkDesignTokens.BackgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            if (title.isNotBlank()) {
                PosLinkText(
                    text = title,
                    role = PosLinkTextRole.ScreenTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = timeoutSec.toString(),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(4.dp),
                color = Color(0xFF2196F3),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        if (body.isNotBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(4.dp)
            ) {
                Text(
                    text = body,
                    modifier = Modifier.fillMaxWidth(),
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 24.sp,
                        lineHeight = 31.2.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        PoslinkSignatureBoard(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 4.dp)
                .background(Color.White),
            buttonsEnabled = buttonsEnabled,
            signatureViewRef = signatureViewRef,
            onConfirm = onEnter,
            onClear = onClear,
            onCancel = onCancel
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PosLinkDesignTokens.InlineSpacing),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            PoslinkSignatureButton(
                text = stringResource(R.string.cancel_sign),
                background = PosLinkDesignTokens.PastelWarning,
                onClick = onCancel,
                enabled = buttonsEnabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(PosLinkDesignTokens.InlineSpacing)
            )
            PoslinkSignatureButton(
                text = stringResource(R.string.clear_sign),
                background = PosLinkDesignTokens.PastelAccent,
                onClick = onClear,
                enabled = buttonsEnabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(PosLinkDesignTokens.InlineSpacing)
            )
            PoslinkSignatureButton(
                text = stringResource(R.string.enter),
                background = PosLinkDesignTokens.PrimaryColor,
                onClick = onEnter,
                enabled = buttonsEnabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(PosLinkDesignTokens.InlineSpacing)
            )
        }
    }
}

@Composable
private fun PoslinkSignBox2Screen(
    title: String,
    body: String,
    timeoutSec: Int,
    buttonsEnabled: Boolean,
    signatureViewRef: PoslinkSignatureViewRef,
    onCancel: () -> Unit,
    onClear: () -> Unit,
    onEnter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PosLinkDesignTokens.BackgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            if (title.isNotBlank()) {
                PosLinkText(
                    text = title,
                    role = PosLinkTextRole.ScreenTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = timeoutSec.toString(),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(4.dp),
                color = Color(0xFF2196F3),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = body,
                    modifier = Modifier.fillMaxWidth(),
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 24.sp,
                        lineHeight = 31.2.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
            PoslinkSignatureBoard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.White),
                buttonsEnabled = buttonsEnabled,
                signatureViewRef = signatureViewRef,
                onConfirm = onEnter,
                onClear = onClear,
                onCancel = onCancel
            )
        }
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PosLinkDesignTokens.InlineSpacing),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            PoslinkSignatureButton(
                text = stringResource(R.string.cancel_sign),
                background = PosLinkDesignTokens.PastelWarning,
                onClick = onCancel,
                enabled = buttonsEnabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(PosLinkDesignTokens.InlineSpacing)
            )
            PoslinkSignatureButton(
                text = stringResource(R.string.clear_sign),
                background = PosLinkDesignTokens.PastelAccent,
                onClick = onClear,
                enabled = buttonsEnabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(PosLinkDesignTokens.InlineSpacing)
            )
            PoslinkSignatureButton(
                text = stringResource(R.string.enter),
                background = PosLinkDesignTokens.PrimaryColor,
                onClick = onEnter,
                enabled = buttonsEnabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(PosLinkDesignTokens.InlineSpacing)
            )
        }
    }
}

@Composable
private fun PoslinkSignatureBoard(
    modifier: Modifier,
    buttonsEnabled: Boolean,
    signatureViewRef: PoslinkSignatureViewRef,
    onConfirm: () -> Unit,
    onClear: () -> Unit,
    onCancel: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = Color.White
    ) {
        AndroidView(
            factory = { context ->
                ElectronicSignatureView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBitmap(Rect(0, 0, 384, 128), 0, android.graphics.Color.WHITE)
                    isFocusable = true
                    isFocusableInTouchMode = true
                    requestFocus()
                    setOnKeyListener { _, keyCode, event ->
                        if (event.action != KeyEvent.ACTION_UP) {
                            return@setOnKeyListener false
                        }
                        when (keyCode) {
                            KeyEvent.KEYCODE_ENTER -> {
                                onConfirm()
                                true
                            }
                            KeyEvent.KEYCODE_DEL -> {
                                onClear()
                                true
                            }
                            KeyEvent.KEYCODE_BACK -> {
                                onCancel()
                                true
                            }
                            else -> false
                        }
                    }
                }
            },
            update = { view ->
                view.isEnabled = buttonsEnabled
                signatureViewRef.view = view
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun PoslinkSignatureButton(
    text: String,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    PosLinkPrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        variant = PosLinkPrimaryButtonVariant.PoslinkLegacy,
        containerColorOverride = background,
        disabledContainerColorOverride = background.copy(alpha = 0.38f),
        textColorOverride = PosLinkDesignTokens.PrimaryActionTextColor,
        allCapsOverride = true
    )
}

@Composable
private fun PoslinkTextBoxTwoPrimaryButtonRow(
    buttons: List<PoslinkTextBoxButtonSpec>,
    viewModel: EntryViewModel,
    buttonsEnabled: Boolean
) {
    val buttonMargin = dimensionResource(R.dimen.margin_gap)
    Row(modifier = Modifier.fillMaxWidth()) {
        buttons.forEach { button ->
            PoslinkTextBoxButton(
                text = button.name,
                onClick = { submitPoslinkTextBoxScreenSelection(viewModel, button.index) },
                modifier = Modifier
                    .weight(1f)
                    .padding(buttonMargin),
                enabled = buttonsEnabled,
                containerColor = button.containerColor ?: PosLinkDesignTokens.PrimaryColor
            )
        }
    }
}

@Composable
private fun PoslinkTextBoxThreeButtonRow(
    buttons: List<PoslinkTextBoxButtonSpec>,
    viewModel: EntryViewModel,
    buttonsEnabled: Boolean
) {
    val buttonMargin = dimensionResource(R.dimen.margin_gap)
    Row(modifier = Modifier.fillMaxWidth()) {
        buttons.forEach { button ->
            PoslinkTextBoxButton(
                text = button.name,
                onClick = { submitPoslinkTextBoxScreenSelection(viewModel, button.index) },
                modifier = Modifier
                    .weight(1f)
                    .padding(buttonMargin),
                enabled = buttonsEnabled,
                containerColor = button.containerColor ?: PosLinkDesignTokens.PrimaryColor
            )
        }
    }
}

private fun submitPoslinkTextBoxScreenSelection(viewModel: EntryViewModel, buttonIndex: Int) {
    viewModel.sendNext(
        Bundle().apply {
            putString(EntryRequest.PARAM_BUTTON_NUMBER, buttonIndex.toString())
        }
    )
}

@Composable
private fun PoslinkTextBoxButtons(extras: Bundle, viewModel: EntryViewModel, buttonsEnabled: Boolean) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val body = extras.getString(EntryExtraData.PARAM_TEXT).orEmpty()
    val normalizedTitle = normalizePoslinkTitleCommands(title)
    val normalizedBody = normalizePoslinkTitleCommands(body)
    val buttons = resolvePoslinkTextBoxButtons(extras)
    val shouldDisplayButtons = shouldDisplayPoslinkTextBoxButtons(extras)
    val hardKeyList = resolvePoslinkTextBoxHardKeyList(extras)
    val buttonMargin = dimensionResource(R.dimen.margin_gap)
    if (!shouldDisplayButtons && buttons.isNotEmpty() && buttonsEnabled) {
        LaunchedEffect(buttons, hardKeyList, buttonsEnabled) {
            viewModel.keyEvents.collect { keyCode ->
                resolvePoslinkTextBoxHardKeyResponse(
                    buttons = buttons,
                    hardKeyList = hardKeyList,
                    keyCode = keyCode
                )?.let { response ->
                    viewModel.sendNext(
                        Bundle().apply {
                            putString(EntryRequest.PARAM_BUTTON_NUMBER, response)
                        }
                    )
                }
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        if (normalizedTitle.isNotBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = buttonMargin * 2)
            ) {
                PoslinkLegacyTitleLikeText(raw = normalizedTitle, supportLineSep = true)
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            if (normalizedBody.isNotBlank()) {
                PoslinkLegacyTitleLikeText(raw = normalizedBody, supportLineSep = true)
            }
        }
        if (shouldDisplayButtons) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = buttonMargin)
                    .padding(bottom = buttonMargin)
            ) {
                when (buttons.size) {
                    2 -> PoslinkTextBoxTwoPrimaryButtonRow(buttons, viewModel, buttonsEnabled)
                    3 -> PoslinkTextBoxThreeButtonRow(buttons, viewModel, buttonsEnabled)
                    else -> {
                        buttons.forEach { button ->
                            PoslinkTextBoxButton(
                                text = button.name,
                                onClick = { submitPoslinkTextBoxScreenSelection(viewModel, button.index) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(buttonMargin),
                                enabled = buttonsEnabled,
                                containerColor = button.containerColor ?: PosLinkDesignTokens.PrimaryColor
                            )
                        }
                    }
                }
            }
        }
    }
}

