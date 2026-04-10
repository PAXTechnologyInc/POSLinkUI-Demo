package com.paxus.pay.poslinkui.demo.entry.compose

import android.os.Bundle
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.PoslinkEntry
import com.pax.us.pay.ui.constant.entry.enumeration.InputType
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.text.GenericStringEntryScreen
import com.paxus.pay.poslinkui.demo.entry.text.invoice.InvoiceNumberScreen
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyMaterialFillAppearance
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyMaterialFilledButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButtonVariant
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.Toast
import com.paxus.pay.poslinkui.demo.utils.Toast.TYPE
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel

/** First-row button count in [PoslinkShowDialogThreeOptionsLayout] (remaining option is the full-width row). */
private const val POSLINK_DIALOG_TOP_ROW_SLOT_COUNT = 2

private const val POSLINK_DIALOG_RESPONSE_INDEX_THIRD = 3

/**
 * Title and option buttons for [PoslinkEntry.ACTION_SHOW_DIALOG].
 */
@Composable
private fun PoslinkShowDialogContent(
    title: String,
    opts: Array<String?>,
    viewModel: EntryViewModel
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        if (title.isNotBlank()) {
            PoslinkFormattedTitle(title = title)
        }
        when (opts.size) {
            2 -> PoslinkShowDialogTwoOptionsLayout(opts = opts, viewModel = viewModel)
            3 -> PoslinkShowDialogThreeOptionsLayout(opts = opts, viewModel = viewModel)
            else -> {
                opts.forEachIndexed { index, label ->
                    PosLinkPrimaryButton(
                        text = label ?: "",
                        onClick = {
                            viewModel.sendNext(
                                Bundle().apply { putInt(EntryRequest.PARAM_INDEX, index + 1) }
                            )
                        },
                        modifier = Modifier
                            .padding(vertical = PosLinkDesignTokens.CompactSpacing),
                        variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
                    )
                }
            }
        }
    }
}

@Composable
private fun PoslinkShowDialogTwoOptionsLayout(
    opts: Array<String?>,
    viewModel: EntryViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PosLinkDesignTokens.CompactSpacing),
        horizontalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.ControlGutter)
    ) {
        opts.forEachIndexed { index, label ->
            PosLinkPrimaryButton(
                text = label ?: "",
                onClick = {
                    viewModel.sendNext(
                        Bundle().apply { putInt(EntryRequest.PARAM_INDEX, index + 1) }
                    )
                },
                modifier = Modifier.weight(1f),
                variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
            )
        }
    }
}

@Composable
private fun PoslinkShowDialogThreeOptionsLayout(
    opts: Array<String?>,
    viewModel: EntryViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PosLinkDesignTokens.CompactSpacing),
        horizontalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.ControlGutter)
    ) {
        repeat(POSLINK_DIALOG_TOP_ROW_SLOT_COUNT) { slot ->
            PosLinkPrimaryButton(
                text = opts[slot].orEmpty(),
                onClick = {
                    viewModel.sendNext(
                        Bundle().apply {
                            putInt(EntryRequest.PARAM_INDEX, slot + 1)
                        }
                    )
                },
                modifier = Modifier.weight(1f),
                variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
            )
        }
    }
    val thirdOptionLabel = opts.toList()
        .asSequence()
        .drop(POSLINK_DIALOG_TOP_ROW_SLOT_COUNT)
        .firstOrNull()
        .orEmpty()
    PosLinkPrimaryButton(
        text = thirdOptionLabel,
        onClick = {
            viewModel.sendNext(
                Bundle().apply {
                    putInt(EntryRequest.PARAM_INDEX, POSLINK_DIALOG_RESPONSE_INDEX_THIRD)
                }
            )
        },
        modifier = Modifier.padding(vertical = PosLinkDesignTokens.CompactSpacing),
        variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
    )
}

@Composable
private fun PoslinkRouteShowThankYou(extras: Bundle, viewModel: EntryViewModel) {
    val t = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val m1 = extras.getString(EntryExtraData.PARAM_MESSAGE_1).orEmpty()
    val m2 = extras.getString(EntryExtraData.PARAM_MESSAGE_2).orEmpty()
    Column(Modifier.verticalScroll(rememberScrollState())) {
        if (t.isNotBlank()) PosLinkText(text = t, role = PosLinkTextRole.ScreenTitle)
        if (m1.isNotBlank()) PosLinkText(text = m1)
        if (m2.isNotBlank()) PosLinkText(text = m2)
        Spacer(Modifier.height(PosLinkDesignTokens.SectionBreakSpacing))
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm),
            onClick = { viewModel.sendNext(null) },
            variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
        )
    }
}

@Composable
private fun PoslinkRouteInputText(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val inputType = extras.getString(EntryExtraData.PARAM_INPUT_TYPE).orEmpty()
    val normalizedInputType = inputType.trim()
    val minL = extras.getString(EntryExtraData.PARAM_MIN_LENGTH)?.toIntOrNull() ?: 0
    val maxL = extras.getString(EntryExtraData.PARAM_MAX_LENGTH)?.toIntOrNull() ?: 64
    val pattern = if (minL > 0 && maxL >= minL) "$minL-$maxL" else "1-$maxL"
    val message = title.ifBlank { stringResource(R.string.enter) }
    if (normalizedInputType.isBlank() || normalizedInputType == "0") {
        InvoiceNumberScreen(
            message = message,
            valuePattern = pattern,
            parityLog = "Poslink InputText type0 parity v1 active",
            forceTextKeyboard = true,
            eagerShowKeyboard = true,
            onConfirm = { v ->
                viewModel.sendNext(Bundle().apply { putString(EntryRequest.PARAM_INPUT_VALUE, v) })
            },
            onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
        )
    } else {
        val mappedInputType = if (normalizedInputType == "1") InputType.NUM else null
        GenericStringEntryScreen(
            message = message,
            valuePattern = pattern,
            maxLengthFallback = maxL,
            eInputType = mappedInputType,
            useLegacyFleetInputStyle = true,
            onConfirm = { v ->
                viewModel.sendNext(Bundle().apply { putString(EntryRequest.PARAM_INPUT_VALUE, v) })
            },
            onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
        )
    }
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
    val fallbackText = resolvePoslinkShowMessageFallbackText(rawMessageList)
    val displayMessage = when {
        list.isNotBlank() -> list
        fallbackText != null -> fallbackText.text
        else -> "Message content unavailable"
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
            "SHOW_MESSAGE parsed empty, fallback missing. using placeholder. constKey=%s rawLen=%d keys=%s rawPreview=%s",
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
private fun PoslinkRouteShowItem(extras: Bundle, viewModel: EntryViewModel) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val tax = extras.getString(EntryExtraData.PARAM_TAX_LINE).orEmpty()
    val total = extras.getString(EntryExtraData.PARAM_TOTAL_LINE).orEmpty()
    val rawItems = resolvePoslinkShowItemRaw(extras)
    val list = parsePoslinkShowItemList(rawItems)
    val image = resolvePoslinkDisplayImage(
        extras = extras,
        fallbackRawPayload = rawItems
    )
    PoslinkMessageDisplayLayout(
        PoslinkMessageDisplayLayoutParams(
            body = PoslinkMessageDisplayBodyParams(
                title = title,
                messageText = list,
                imageUrl = image.url,
                imageDesc = image.desc
            ),
            footer = PoslinkMessageDisplayFooterParams(
                tax = tax,
                total = total,
                onConfirm = { viewModel.sendNext(null) }
            )
        )
    )
}

@Composable
private fun PoslinkRouteShowInputTextBox(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val minL = extras.getString(EntryExtraData.PARAM_MIN_LENGTH)?.toIntOrNull() ?: 0
    val maxL = extras.getString(EntryExtraData.PARAM_MAX_LENGTH)?.toIntOrNull() ?: 64
    val pattern = if (minL > 0 && maxL >= minL) "$minL-$maxL" else "1-$maxL"
    GenericStringEntryScreen(
        message = title.ifBlank { stringResource(R.string.enter) },
        valuePattern = pattern,
        maxLengthFallback = maxL,
        eInputType = extras.getString(EntryExtraData.PARAM_INPUT_TYPE),
        useLegacyFleetInputStyle = true,
        onConfirm = { v ->
            viewModel.sendNext(Bundle().apply { putString(EntryRequest.PARAM_INPUT_VALUE, v) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
private fun PoslinkRouteShowSignatureBox(
    extras: Bundle,
    viewModel: EntryViewModel
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
    if (signBoxStyle == 2) {
        PoslinkSignBox2Screen(
            title = title,
            body = body,
            timeoutSec = timeoutSec,
            onCancel = {
                viewModel.sendNext(
                    Bundle().apply { putString(EntryRequest.PARAM_SIGN_STATUS, "DEMO_CANCEL") }
                )
            },
            onClear = { },
            onEnter = {
                viewModel.sendNext(
                    Bundle().apply {
                        putString(EntryRequest.PARAM_SIGN_STATUS, "DEMO_ACCEPT")
                        putShortArray(EntryRequest.PARAM_SIGNATURE, shortArrayOf(0, 0, 1, 0, 1, 1))
                    }
                )
            }
        )
    } else {
        PoslinkSignBox1Screen(
            title = title,
            body = body,
            timeoutSec = timeoutSec,
            onCancel = {
                viewModel.sendNext(
                    Bundle().apply { putString(EntryRequest.PARAM_SIGN_STATUS, "DEMO_CANCEL") }
                )
            },
            onClear = { },
            onEnter = {
                viewModel.sendNext(
                    Bundle().apply {
                        putString(EntryRequest.PARAM_SIGN_STATUS, "DEMO_ACCEPT")
                        putShortArray(EntryRequest.PARAM_SIGNATURE, shortArrayOf(0, 0, 1, 0, 1, 1))
                    }
                )
            }
        )
    }
}

@Composable
private fun PoslinkRouteShowDialogForm(extras: Bundle, viewModel: EntryViewModel) {
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
        if (title.isNotBlank()) PoslinkFormattedTitle(title = title)
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
                enabled = sel >= 0,
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
    when (action) {
        PoslinkEntry.ACTION_SHOW_DIALOG -> {
            val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
            val opts = extras.getStringArray(EntryExtraData.PARAM_OPTIONS) ?: emptyArray()
            PoslinkShowDialogContent(title = title, opts = opts, viewModel = viewModel)
        }
        PoslinkEntry.ACTION_SHOW_THANK_YOU -> PoslinkRouteShowThankYou(extras, viewModel)
        PoslinkEntry.ACTION_INPUT_TEXT -> PoslinkRouteInputText(extras, activity, viewModel)
        PoslinkEntry.ACTION_SHOW_MESSAGE -> PoslinkRouteShowMessage(extras, activity, viewModel)
        PoslinkEntry.ACTION_SHOW_ITEM -> PoslinkRouteShowItem(extras, viewModel)
        PoslinkEntry.ACTION_SHOW_TEXT_BOX -> PoslinkTextBoxButtons(extras, viewModel)
        PoslinkEntry.ACTION_SHOW_INPUT_TEXT_BOX -> PoslinkRouteShowInputTextBox(extras, activity, viewModel)
        PoslinkEntry.ACTION_SHOW_SIGNATURE_BOX -> PoslinkRouteShowSignatureBox(extras, viewModel)
        PoslinkEntry.ACTION_SHOW_DIALOG_FORM -> PoslinkRouteShowDialogForm(extras, viewModel)
        else -> BoxWithCenterText(stringResource(R.string.poslink_action_not_handled, action ?: ""))
    }
}

@Composable
private fun PoslinkSignBox1Screen(
    title: String,
    body: String,
    timeoutSec: Int,
    onCancel: () -> Unit,
    onClear: () -> Unit,
    onEnter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PosLinkDesignTokens.ScreenPadding)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title.ifBlank { "Signature" },
                modifier = Modifier.align(Alignment.Center),
                color = PosLinkDesignTokens.PrimaryTextColor,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = timeoutSec.toString(),
                modifier = Modifier.align(Alignment.CenterEnd),
                color = Color(0xFF2196F3),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        if (body.isNotBlank()) {
            Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
            Text(
                text = body,
                color = PosLinkDesignTokens.PrimaryTextColor,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color.White)
        )
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PosLinkDesignTokens.SpaceBetweenTextView),
            horizontalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.ControlGutter)
        ) {
            PoslinkSignatureButton(
                text = "CANCEL",
                background = Color(0xFFFC7D7D),
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            )
            PoslinkSignatureButton(
                text = "CLEAR",
                background = Color(0xFF8BAA97),
                onClick = onClear,
                modifier = Modifier.weight(1f)
            )
            PoslinkSignatureButton(
                text = "ENTER",
                background = Color(0xFF6E85B7),
                onClick = onEnter,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PoslinkSignBox2Screen(
    title: String,
    body: String,
    timeoutSec: Int,
    onCancel: () -> Unit,
    onClear: () -> Unit,
    onEnter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PosLinkDesignTokens.ScreenPadding)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title.ifBlank { "Signature" },
                modifier = Modifier.align(Alignment.Center),
                color = PosLinkDesignTokens.PrimaryTextColor,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = timeoutSec.toString(),
                modifier = Modifier.align(Alignment.CenterEnd),
                color = Color(0xFF2196F3),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = body,
                color = PosLinkDesignTokens.PrimaryTextColor,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(212.dp)
                    .background(Color(0xFFDADADA))
            )
        }
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PosLinkDesignTokens.SpaceBetweenTextView),
            horizontalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.ControlGutter)
        ) {
            PoslinkSignatureButton(
                text = "CANCEL",
                background = Color(0xFFFC7D7D),
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            )
            PoslinkSignatureButton(
                text = "CLEAR",
                background = Color(0xFF8BAA97),
                onClick = onClear,
                modifier = Modifier.weight(1f)
            )
            PoslinkSignatureButton(
                text = "ENTER",
                background = Color(0xFF6E85B7),
                onClick = onEnter,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PoslinkSignatureButton(
    text: String,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PosLinkLegacyMaterialFilledButton(
        onClick = onClick,
        modifier = modifier,
        appearance = PosLinkLegacyMaterialFillAppearance(
            slotHeight = PosLinkDesignTokens.ButtonHeight,
            shape = MaterialTheme.shapes.extraSmall,
            containerColor = background,
            disabledContainerColor = background.copy(alpha = 0.38f)
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 0.5.sp,
                color = Color(0xFFECECEC)
            )
        )
    }
}

@Composable
private fun PoslinkTextBoxTwoPrimaryButtonRow(
    buttons: List<PoslinkTextBoxButtonSpec>,
    viewModel: EntryViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PosLinkDesignTokens.CompactSpacing),
        horizontalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.ControlGutter)
    ) {
        buttons.forEach { button ->
            PosLinkPrimaryButton(
                text = button.name,
                onClick = {
                    viewModel.sendNext(
                        Bundle().apply {
                            putString(
                                EntryRequest.PARAM_BUTTON_NUMBER,
                                button.key.ifBlank { button.index.toString() }
                            )
                        }
                    )
                },
                modifier = Modifier.weight(1f),
                variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
            )
        }
    }
}

@Composable
private fun PoslinkTextBoxThreeButtonRow(
    buttons: List<PoslinkTextBoxButtonSpec>,
    viewModel: EntryViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = PosLinkDesignTokens.InlineSpacing,
                vertical = PosLinkDesignTokens.InlineSpacing
            ),
        horizontalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.ControlGutter)
    ) {
        buttons.forEach { button ->
            PoslinkTextBoxButton(
                text = button.name,
                onClick = {
                    viewModel.sendNext(
                        Bundle().apply {
                            putString(
                                EntryRequest.PARAM_BUTTON_NUMBER,
                                button.key.ifBlank { button.index.toString() }
                            )
                        }
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PoslinkTextBoxButtons(extras: Bundle, viewModel: EntryViewModel) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val body = extras.getString(EntryExtraData.PARAM_TEXT).orEmpty()
    val normalizedTitle = normalizePoslinkTitleCommands(title)
    val normalizedBody = normalizePoslinkTitleCommands(body)
    val buttons = resolvePoslinkTextBoxButtons(extras)
    val centerAlignedTitleCase = shouldRenderShowTextBoxCenterTitle(
        normalizedTitle = normalizedTitle,
        normalizedBody = normalizedBody,
        buttonCount = buttons.size
    )
    Column(modifier = Modifier.fillMaxSize()) {
        if (normalizedTitle.isNotBlank()) {
            if (centerAlignedTitleCase) {
                PoslinkCenteredTextBoxTitle(rawTitle = normalizedTitle)
            } else {
                PoslinkFormattedTextBoxTitle(title = normalizedTitle)
            }
        }
        if (normalizedBody.isNotBlank()) {
            if (normalizedTitle.isBlank() && containsShowTextBoxLineSepCommand(body)) {
                PoslinkFormattedTitleLegacy(title = normalizedBody)
            } else {
                PosLinkText(text = normalizedBody)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column {
            when (buttons.size) {
                2 -> PoslinkTextBoxTwoPrimaryButtonRow(buttons, viewModel)
                3 -> PoslinkTextBoxThreeButtonRow(buttons, viewModel)
                else -> {
                    buttons.forEach { button ->
                        PosLinkPrimaryButton(
                            text = button.name,
                            onClick = {
                                viewModel.sendNext(
                                    Bundle().apply {
                                        putString(
                                            EntryRequest.PARAM_BUTTON_NUMBER,
                                            button.key.ifBlank { button.index.toString() }
                                        )
                                    }
                                )
                            },
                            modifier = Modifier
                                .padding(vertical = PosLinkDesignTokens.CompactSpacing),
                            variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
                        )
                    }
                }
            }
        }
    }
}

