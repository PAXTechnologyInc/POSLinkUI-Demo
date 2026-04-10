package com.paxus.pay.poslinkui.demo.entry.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils

/**
 * Generic Compose screen for text/number-text entry.
 * Aligns with fragment_base_text.xml and fragment_base_num_text.xml.
 *
 * @param message Prompt text
 * @param maxLength Max input length (0 = no limit)
 * @param valuePattern Value pattern for validation (e.g. "1-32", "0,2")
 * @param keyboardType KeyboardType.Number or KeyboardType.Text
 * @param usePasswordMask Whether to mask input (e.g. password)
 * @param onConfirm Called with value on valid submit
 * @param onError Called with error message for invalid input
 */
@Composable
fun TextScreen(
    message: String,
    maxLength: Int,
    valuePattern: String?,
    keyboardType: KeyboardType = KeyboardType.Number,
    usePasswordMask: Boolean = false,
    onConfirm: (String) -> Unit,
    onError: (String) -> Unit
) {
    var value by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val promptInputLength = stringResource(R.string.prompt_input_length, valuePattern ?: "")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        PosLinkText(
            text = message,
            role = PosLinkTextRole.ScreenTitle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (maxLength <= 0 || newValue.length <= maxLength) {
                    value = newValue
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (usePasswordMask) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm),
            onClick = {
                val lengthList = ValuePatternUtils.getLengthList(valuePattern ?: "")
                if (lengthList.contains(value.length)) {
                    onConfirm(value)
                } else {
                    onError(promptInputLength)
                }
            }
        )
    }
}
