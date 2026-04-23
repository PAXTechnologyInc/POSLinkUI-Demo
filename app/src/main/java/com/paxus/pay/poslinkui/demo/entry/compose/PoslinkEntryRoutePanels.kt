package com.paxus.pay.poslinkui.demo.entry.compose

import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.signature.ElectronicSignatureView
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButtonVariant
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel

private class PoslinkSignatureViewRef {
    var view: ElectronicSignatureView? = null
}

@Composable
internal fun PoslinkRouteShowSignatureBox(
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
        PoslinkSignBox2Screen( // NOSONAR
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
        PoslinkSignBox1Screen( // NOSONAR
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
internal fun PoslinkRouteShowDialogForm( // NOSONAR
    extras: Bundle,
    viewModel: EntryViewModel,
    buttonsEnabled: Boolean
) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val labels = extras.getStringArray(EntryExtraData.PARAM_LABELS)?.filterNotNull().orEmpty()
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
        labels.forEachIndexed { index, label ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (buttonType == 2) {
                    Checkbox(
                        checked = checkedSet.contains(index),
                        onCheckedChange = { checked ->
                            checkedSet = if (checked) {
                                checkedSet + index
                            } else {
                                checkedSet - index
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,
                            uncheckedColor = Color.White,
                            checkmarkColor = PosLinkDesignTokens.BackgroundColor
                        )
                    )
                } else {
                    RadioButton(selected = sel == index, onClick = { sel = index })
                }
                PosLinkText(
                    text = label,
                    modifier = Modifier.padding(start = PosLinkDesignTokens.ControlGutter)
                )
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

@Composable
private fun PoslinkSignBox1Screen( // NOSONAR
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
private fun PoslinkSignBox2Screen( // NOSONAR
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
                    .fillMaxSize()
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
                    .fillMaxSize()
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
private fun PoslinkTextBoxButtonRow(
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
internal fun PoslinkTextBoxButtons(extras: Bundle, viewModel: EntryViewModel, buttonsEnabled: Boolean) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE).orEmpty()
    val body = extras.getString(EntryExtraData.PARAM_TEXT).orEmpty()
    val normalizedTitle = normalizePoslinkTitleCommands(title)
    val normalizedBody = normalizePoslinkTitleCommands(body)
    val buttons = resolvePoslinkTextBoxButtons(extras)
    val shouldDisplayButtons = shouldDisplayPoslinkTextBoxButtons(extras)
    val enabledHardKeys = resolvePoslinkTextBoxHardKeyCodes(extras)
    val buttonMargin = dimensionResource(R.dimen.margin_gap)
    if (!shouldDisplayButtons && buttons.isNotEmpty() && buttonsEnabled) {
        LaunchedEffect(buttons, enabledHardKeys, buttonsEnabled) {
            viewModel.keyEvents.collect { keyCode ->
                resolvePoslinkTextBoxHardKeyResponse(
                    buttons = buttons,
                    enabledHardKeys = enabledHardKeys,
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
                    2, 3 -> PoslinkTextBoxButtonRow(buttons, viewModel, buttonsEnabled)
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
