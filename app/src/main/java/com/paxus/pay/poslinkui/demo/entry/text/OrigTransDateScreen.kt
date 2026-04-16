package com.paxus.pay.poslinkui.demo.entry.text

import com.pax.us.pay.ui.constant.entry.enumeration.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.compose.LocalEntryInteractionLocked
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyMaterialFillAppearance
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyMaterialFilledButton
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.ViewUtils
import java.util.Locale
import kotlinx.coroutines.delay

private fun formatDate(input: String, format: String): String {
    val digits = input.replace("[^0-9]".toRegex(), "")
    val sections = when (format) {
        DateFormat.YYYYMMDD -> intArrayOf(4, 2, 2)
        DateFormat.MMYY, DateFormat.MMDD -> intArrayOf(2, 2)
        else -> intArrayOf(4, 2, 2)
    }
    val maxLen = sections.sum()
    if (digits.length > maxLen) return input
    var pos = 0
    val sb = StringBuilder()
    for (i in sections.indices) {
        if (pos < digits.length) {
            sb.append(digits.substring(pos, minOf(pos + sections[i], digits.length)))
            pos += sections[i]
            if (i < sections.size - 1 && pos < digits.length) sb.append("/")
        }
    }
    return sb.toString()
}

private fun getPlaceholder(format: String?): String = when (format) {
    DateFormat.MMYY -> "MM/YY"
    DateFormat.MMDD -> "MM/DD"
    else -> "YYYY/MM/DD"
}

/**
 * Compose screen for original transaction date entry.
 */
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun OrigTransDateScreen(
    message: String,
    dateFormat: String?,
    onConfirm: (String) -> Unit
) {
    val interactionLocked = LocalEntryInteractionLocked.current
    val resolvedFormat = dateFormat?.takeIf { it.isNotBlank() } ?: DateFormat.YYYYMMDD
    val activity = LocalContext.current as? FragmentActivity
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    var isInputFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Request focus after first frame, then retry once if focus was stolen by host overlays.
        delay(120)
        focusRequester.requestFocus()
        keyboardController?.show()
        delay(180)
        if (!isInputFocused) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
        repeat(6) { idx ->
            activity?.let { ViewUtils().removeWaterMarkView(it) }
            if (idx < 5) delay(120)
        }
        Logger.i("OrigDateEntry parity v4 keyboard-open active")
    }

    LaunchedEffect(isInputFocused) {
        if (isInputFocused) {
            keyboardController?.show()
        }
    }
    var value by remember { mutableStateOf(TextFieldValue("", TextRange(0))) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        Text(
            text = message,
            color = Color(0xFFDBD4D9),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Normal,
                fontSize = PosLinkDesignTokens.TitleTextSize
            ),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        BasicTextField(
            value = value,
            onValueChange = { nextValue ->
                val formatted = formatDate(nextValue.text, resolvedFormat)
                if (formatted.length <= getPlaceholder(resolvedFormat).length) {
                    value = TextFieldValue(
                        text = formatted,
                        selection = TextRange(formatted.length)
                    )
                }
            },
            readOnly = interactionLocked,
            modifier = Modifier
                .fillMaxWidth()
                .height(PosLinkDesignTokens.ButtonHeight)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isInputFocused = focusState.isFocused
                },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF222222)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            cursorBrush = SolidColor(Color(0xFF66A579)),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PosLinkDesignTokens.ButtonHeight)
                        .border(
                            width = 2.dp,
                            color = Color(0xFFDBD4D9),
                            shape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius)
                        )
                        .background(
                            color = Color(0xFFDBD4D9),
                            shape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius)
                        )
                        .padding(horizontal = PosLinkDesignTokens.FieldInnerHorizontalPadding),
                    contentAlignment = Alignment.Center
                ) {
                    if (value.text.isEmpty()) {
                        Text(
                            text = getPlaceholder(resolvedFormat),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = Color(0xFF3C4045),
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        PosLinkLegacyMaterialFilledButton(
            onClick = { onConfirm(value.text.replace("[^0-9]".toRegex(), "")) },
            enabled = !interactionLocked,
            modifier = Modifier.fillMaxWidth(),
            appearance = PosLinkLegacyMaterialFillAppearance(
                slotHeight = PosLinkDesignTokens.ButtonHeight,
                shape = RoundedCornerShape(PosLinkDesignTokens.LegacyButtonCornerRadius),
                containerColor = Color(0xFF6E85B7),
                disabledContainerColor = Color(0xFF6E85B7).copy(alpha = 0.38f)
            )
        ) {
            Text(
                text = stringResource(R.string.confirm).uppercase(Locale.US),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    color = Color(0xFFECECEC)
                )
            )
        }
    }
}
