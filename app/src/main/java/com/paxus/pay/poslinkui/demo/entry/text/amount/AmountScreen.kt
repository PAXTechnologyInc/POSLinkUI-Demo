package com.paxus.pay.poslinkui.demo.entry.text.amount

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.compose.EntryHardwareConfirmEffect
import com.paxus.pay.poslinkui.demo.entry.compose.LocalEntryInteractionLocked
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyThemeButton
import com.paxus.pay.poslinkui.demo.ui.device.LocalDeviceLayoutSpec
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import kotlin.math.roundToInt
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.paxus.pay.poslinkui.demo.utils.DeviceUtils
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils
import com.paxus.pay.poslinkui.demo.utils.ViewUtils
import kotlinx.coroutines.delay

/**
 * Compose screen for amount entry.
 * Aligns with `golive/v1.03.00` `fragment_amount.xml` + dimens: `text_size_title` prompt,
 * `text_size_normal` / `text_size_title` and `button_height` / spacing from **configuration-qualified**
 * `dimens.xml` under `res/values-<qualifier>/` (same mechanism as `golive/v1.03.00` View inflation), not hard-coded tokens only.
 * Confirm uses [PosLinkLegacyMaterialFilledButton] so inset matches XML `MaterialButton` via [com.paxus.pay.poslinkui.demo.ui.theme.LocalPosLinkLegacyMaterialButtonVerticalInset].
 *
 * @param message Prompt text
 * @param maxLength Max digit length
 * @param currency Currency type (USD, EUR, etc.)
 * @param valuePattern Value pattern for validation
 * @param onConfirm Called with amount (in cents) on valid submit
 * @param onError Called with error message for invalid input
 */
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun AmountScreen(
    message: String,
    maxLength: Int,
    currency: String?,
    valuePattern: String?,
    onConfirm: (Long) -> Unit,
    onError: (String) -> Unit
) {
    val interactionLocked = LocalEntryInteractionLocked.current
    var displayValue by remember {
        val initialText = CurrencyUtils.convert(0L, currency)
        mutableStateOf(
            TextFieldValue(
                text = initialText,
                selection = TextRange(initialText.length)
            )
        )
    }
    val scrollState = rememberScrollState()
    val activity = LocalContext.current as? FragmentActivity
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val spec = LocalDeviceLayoutSpec.current
    val res = LocalContext.current.resources
    val dm = res.displayMetrics
    val inputHeight = dimensionResource(R.dimen.button_height)
    val sectionSpacing = dimensionResource(R.dimen.space_between_textview)
    val cornerRadius = dimensionResource(R.dimen.corner_radius)
    val fieldShape = RoundedCornerShape(cornerRadius)
    val titleTextSize = (res.getDimension(R.dimen.text_size_title) / dm.scaledDensity).sp
    val bodyTextSize = (res.getDimension(R.dimen.text_size_normal) / dm.scaledDensity).sp
    val bodyLineHeight = (res.getDimension(R.dimen.text_size_normal) / dm.scaledDensity * 1.4f).sp
    val targetHorizontalPaddingDp = dimensionResource(R.dimen.padding_horizontal).value.roundToInt()
    val legacyHorizontalInset = (targetHorizontalPaddingDp - spec.screenHorizontalPaddingDp)
        .coerceAtLeast(0)
        .dp
    val promptInputStr = stringResource(R.string.prompt_input)
    val submit = {
        val value = CurrencyUtils.parse(displayValue.text)
        val lengthList = ValuePatternUtils.getLengthList(valuePattern ?: "")
        if (value == 0L && !lengthList.contains(0)) {
            onError(promptInputStr)
        } else {
            onConfirm(value)
        }
    }

    LaunchedEffect(Unit) {
        // Keep ENTER_AMOUNT screenshot parity stable when host applies watermark asynchronously.
        repeat(10) { idx ->
            activity?.let { ViewUtils().removeWaterMarkView(it) }
            if (idx < 9) delay(150)
        }
        Logger.i("AmountScreen parity v10 active")
        val shouldShowSoftKeyboard = !DeviceUtils.hasPhysicalKeyboard()
        if (shouldShowSoftKeyboard) {
            focusRequester.requestFocus()
            keyboardController?.show()
            // Retry after first composition; some PAX builds ignore the initial request.
            delay(150)
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    EntryHardwareConfirmEffect(
        enabled = !interactionLocked,
        onConfirm = submit
    )

    Column(
        modifier = Modifier
            .padding(horizontal = legacyHorizontalInset)
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {
        // fragment_amount: first TextView only has marginVertical (no extra Spacer above); root padding
        // already separates content from the action bar 锟?avoid stacking another full section gap.
        Spacer(modifier = Modifier.height(sectionSpacing))
        Text(
            text = message,
            style = TextStyle(
                color = PosLinkDesignTokens.PrimaryTextColor,
                fontWeight = FontWeight.Normal,
                fontSize = titleTextSize,
                lineHeight = titleTextSize * PosLinkDesignTokens.EntryTitleLineHeightMultiplier
            ),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
        Spacer(modifier = Modifier.height(sectionSpacing))
        BasicTextField(
            value = displayValue,
            onValueChange = { newValue ->
                val digits = newValue.text.replace("[^0-9]".toRegex(), "")
                if (digits.length <= maxLength) {
                    val amount = digits.ifEmpty { "0" }.toLongOrNull() ?: 0L
                    val text = CurrencyUtils.convert(amount, currency)
                    displayValue = TextFieldValue(text = text, selection = TextRange(text.length))
                }
            },
            readOnly = interactionLocked,
            modifier = Modifier
                .fillMaxWidth()
                .height(inputHeight)
                .focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                color = Color(0xFF222222),
                fontWeight = FontWeight.Normal,
                fontSize = bodyTextSize,
                lineHeight = bodyLineHeight
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            cursorBrush = SolidColor(Color(0xFF66A579)),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(inputHeight)
                        .border(width = 2.dp, color = Color(0xFFDBD4D9), shape = fieldShape)
                        .background(
                            color = Color(0xFFDBD4D9),
                            shape = fieldShape
                        )
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
