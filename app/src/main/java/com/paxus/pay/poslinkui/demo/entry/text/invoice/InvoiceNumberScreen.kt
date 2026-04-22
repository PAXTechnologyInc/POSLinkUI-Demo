package com.paxus.pay.poslinkui.demo.entry.text.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.compose.EntryHardwareConfirmEffect
import com.paxus.pay.poslinkui.demo.entry.compose.LocalEntryInteractionLocked
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyThemeButton
import com.paxus.pay.poslinkui.demo.ui.device.DeviceProfileId
import com.paxus.pay.poslinkui.demo.ui.device.LocalDeviceLayoutSpec
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.DeviceUtils
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils
import com.paxus.pay.poslinkui.demo.utils.ViewUtils
import kotlinx.coroutines.delay

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun InvoiceNumberScreen(
    message: String,
    valuePattern: String,
    parityLog: String = "InvoiceNumber parity v2 active",
    forceTextKeyboard: Boolean = false,
    eagerShowKeyboard: Boolean = false,
    onConfirm: (String) -> Unit,
    onError: (String) -> Unit
) {
    val interactionLocked = LocalEntryInteractionLocked.current
    var value by remember { mutableStateOf(TextFieldValue("", TextRange(0))) }
    val scrollState = rememberScrollState()
    val activity = LocalContext.current as? FragmentActivity
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val spec = LocalDeviceLayoutSpec.current
    val sectionSpacing = PosLinkDesignTokens.SpaceBetweenTextView
    val inputHeight = PosLinkDesignTokens.buttonHeight()
    val fieldShape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius)
    val lengths = remember(valuePattern) { ValuePatternUtils.getLengthList(valuePattern) }
    val maxChars = remember(lengths) { lengths.maxOf { it ?: 0 } }
    val isNumeric = !forceTextKeyboard
    val keyboardType = if (forceTextKeyboard) KeyboardType.Text else KeyboardType.Number
    val targetHorizontalPaddingDp = when (spec.profileId) {
        DeviceProfileId.A920_CLASS, DeviceProfileId.A920MAX -> 20
        else -> 20
    }
    val topOffset = when (spec.profileId) {
        DeviceProfileId.A920_CLASS, DeviceProfileId.A920MAX -> (-spec.screenVerticalPaddingDp).dp
        else -> 0.dp
    }
    val legacyHorizontalInset = (targetHorizontalPaddingDp - spec.screenHorizontalPaddingDp)
        .coerceAtLeast(0)
        .dp
    val promptInputStr = stringResource(R.string.prompt_input)
    val submit = {
        val text = value.text.trim()
        if (!lengths.contains(text.length)) {
            onError(promptInputStr)
        } else {
            onConfirm(text)
        }
    }

    LaunchedEffect(Unit) {
        val shouldShowSoftKeyboard = forceTextKeyboard || !isNumeric || !DeviceUtils.hasPhysicalKeyboard()
        if (eagerShowKeyboard) {
            focusRequester.requestFocus()
            if (shouldShowSoftKeyboard) keyboardController?.show()
            delay(80)
        }
        repeat(10) { idx ->
            activity?.let { ViewUtils().removeWaterMarkView(it) }
            if (idx < 9) delay(150)
        }
        Logger.i(parityLog)
        focusRequester.requestFocus()
        if (shouldShowSoftKeyboard) keyboardController?.show()
        delay(150)
        focusRequester.requestFocus()
        if (shouldShowSoftKeyboard) keyboardController?.show()
    }

    EntryHardwareConfirmEffect(
        enabled = !interactionLocked,
        onConfirm = submit
    )

    Column(
        modifier = Modifier
            .offset(y = topOffset)
            .padding(horizontal = legacyHorizontalInset)
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {
        Spacer(modifier = Modifier.height(sectionSpacing))
        Text(
            text = message,
            color = PosLinkDesignTokens.PrimaryTextColor,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Normal,
                fontSize = PosLinkDesignTokens.TitleTextSize
            ),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
        Spacer(modifier = Modifier.height(sectionSpacing))
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                val nextText = if (isNumeric) newValue.text.filter { it.isDigit() } else newValue.text
                if (nextText.length <= maxChars) {
                    value = TextFieldValue(text = nextText, selection = TextRange(nextText.length))
                }
            },
            readOnly = interactionLocked,
            modifier = Modifier
                .fillMaxWidth()
                .height(inputHeight)
                .focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                color = Color(0xFF222222)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            cursorBrush = SolidColor(Color(0xFF66A579)),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(inputHeight)
                        .border(width = 2.dp, color = Color(0xFFDBD4D9), shape = fieldShape)
                        .background(color = Color(0xFFDBD4D9), shape = fieldShape)
                        .padding(horizontal = PosLinkDesignTokens.FieldInnerHorizontalPadding),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            }
        )
        PosLinkLegacyThemeButton(
            text = stringResource(R.string.trans_confirm_btn),
            onClick = submit,
            enabled = !interactionLocked,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
