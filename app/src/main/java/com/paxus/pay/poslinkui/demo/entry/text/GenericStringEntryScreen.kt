package com.paxus.pay.poslinkui.demo.entry.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pax.us.pay.ui.constant.entry.enumeration.InputType
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.compose.EntryHardwareConfirmEffect
import com.paxus.pay.poslinkui.demo.entry.compose.LocalEntryInteractionLocked
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyThemeButton
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.DeviceUtils
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils
import kotlinx.coroutines.delay

private data class GenericStringEntryDerived(
    val pattern: String,
    val lengths: List<Int?>,
    val maxChars: Int,
    val keyboardType: KeyboardType,
    val mask: Boolean
)

private fun deriveGenericStringEntryState(
    valuePattern: String?,
    maxLengthFallback: Int?,
    eInputType: String?
): GenericStringEntryDerived {
    val pattern = valuePattern?.takeIf { it.isNotBlank() }
        ?: maxLengthFallback?.let { "1-$it" }
        ?: "1-64"
    val lengths = ValuePatternUtils.getLengthList(pattern)
    val maxChars = lengths.maxOf { it ?: 0 }
    val keyboardType = when (eInputType) {
        InputType.NUM, InputType.PASSCODE -> KeyboardType.Number
        else -> KeyboardType.Text
    }
    val mask = eInputType == InputType.PASSWORD || eInputType == InputType.PASSCODE
    return GenericStringEntryDerived(
        pattern = pattern,
        lengths = lengths,
        maxChars = maxChars,
        keyboardType = keyboardType,
        mask = mask
    )
}

/**
 * Single-line text Entry aligned with legacy [com.paxus.pay.poslinkui.demo.entry.text.text.ATextFragment] behavior:
 * prompt from extras, length validation via [com.pax.us.pay.ui.constant.entry.EntryExtraData.PARAM_VALUE_PATTERN].
 *
 * @param message Primary prompt
 * @param valuePattern Length pattern (e.g. `1-12`, `0-9`); falls back to a single max length when blank
 * @param maxLengthFallback Used when [valuePattern] is null/empty and host sent [com.pax.us.pay.ui.constant.entry.EntryExtraData.PARAM_MAX_LENGTH]
 * @param eInputType Optional [InputType] name for keyboard / masking
 * @param onConfirm Returns trimmed input when validation passes
 * @param onError User-visible validation error
 */
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun GenericStringEntryScreen(
    message: String,
    valuePattern: String?,
    maxLengthFallback: Int?,
    eInputType: String?,
    useLegacyFleetInputStyle: Boolean = false,
    onConfirm: (String) -> Unit,
    onError: (String) -> Unit
) {
    val interactionLocked = LocalEntryInteractionLocked.current
    var text by remember { mutableStateOf("") }
    val scroll = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val res = LocalContext.current.resources
    val dm = res.displayMetrics
    val derived = remember(valuePattern, maxLengthFallback, eInputType) {
        deriveGenericStringEntryState(valuePattern, maxLengthFallback, eInputType)
    }
    val sectionSpacing = dimensionResource(R.dimen.space_between_textview)
    val titleTextSize = (res.getDimension(R.dimen.text_size_title) / dm.scaledDensity).sp
    val lengths = derived.lengths
    val confirmText = if (useLegacyFleetInputStyle) {
        stringResource(R.string.trans_confirm_btn)
    } else {
        stringResource(R.string.confirm)
    }
    val promptInputStr = stringResource(R.string.prompt_input)
    val submit = {
        val t = text.trim()
        if (!lengths.contains(t.length)) {
            onError(promptInputStr)
        } else {
            onConfirm(t)
        }
    }

    LaunchedEffect(derived.keyboardType) {
        val shouldShowSoftKeyboard = !DeviceUtils.hasPhysicalKeyboard()
        delay(120)
        repeat(2) {
            focusRequester.requestFocus()
            if (shouldShowSoftKeyboard) keyboardController?.show()
            delay(150)
        }
    }

    EntryHardwareConfirmEffect(
        enabled = !interactionLocked,
        onConfirm = submit
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scroll)
    ) {
        Spacer(modifier = Modifier.height(sectionSpacing))
        Text(
            text = message,
            color = PosLinkDesignTokens.PrimaryTextColor,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Normal,
                fontSize = titleTextSize,
                lineHeight = titleTextSize * PosLinkDesignTokens.EntryTitleLineHeightMultiplier
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(sectionSpacing))
        GenericStringEntryTextField(
            text = text,
            onTextChange = { text = it },
            keyboardType = derived.keyboardType,
            mask = derived.mask,
            maxChars = derived.maxChars,
            readOnly = interactionLocked,
            focusRequester = focusRequester
        )
        PosLinkLegacyThemeButton(
            text = confirmText,
            enabled = !interactionLocked,
            modifier = Modifier.fillMaxWidth(),
            onClick = submit
        )
    }
}

@Composable
private fun GenericStringEntryTextField(
    text: String,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType,
    mask: Boolean,
    maxChars: Int,
    readOnly: Boolean,
    focusRequester: FocusRequester
) {
    val inputHeight = PosLinkDesignTokens.buttonHeight()
    val cornerRadius = dimensionResource(R.dimen.corner_radius)
    val fieldShape = RoundedCornerShape(cornerRadius)
    val applyFiltered: (String) -> Unit = { raw ->
        val next = when (keyboardType) {
            KeyboardType.Number -> raw.filter { it.isDigit() }
            else -> raw
        }
        if (next.length <= maxChars) {
            onTextChange(next)
        }
    }
    BasicTextField(
        value = text,
        onValueChange = applyFiltered,
        readOnly = readOnly,
        modifier = Modifier
            .fillMaxWidth()
            .height(inputHeight)
            .focusRequester(focusRequester),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (mask) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            textAlign = TextAlign.Center,
            color = PosLinkDesignTokens.OnLightTextColor
        ),
        cursorBrush = SolidColor(PosLinkDesignTokens.PastelAccent),
        decorationBox = { inner ->
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(inputHeight)
                    .border(
                        width = 2.dp,
                        color = PosLinkDesignTokens.BorderColor,
                        shape = fieldShape
                    )
                    .background(
                        color = PosLinkDesignTokens.BorderColor,
                        shape = fieldShape
                    )
                    .padding(horizontal = PosLinkDesignTokens.FieldInnerHorizontalPadding),
                contentAlignment = Alignment.Center
            ) {
                inner()
            }
        }
    )
}
