package com.paxus.pay.poslinkui.demo.entry.text.fleet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens

/**
 * Compose screen for fleet data entry (single step in wizard).
 */
@Composable
fun FleetDataScreen(
    title: String,
    minLength: Int,
    maxLength: Int,
    isNumberInput: Boolean,
    onConfirm: (String) -> Unit,
    onError: (String) -> Unit
) {
    var value by remember { mutableStateOf("") }
    val errMsg = stringResource(R.string.min_length_verification, minLength)

    Column(modifier = Modifier.fillMaxWidth()) {
        PosLinkText(
            text = title,
            role = PosLinkTextRole.ScreenTitle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        OutlinedTextField(
            value = value,
            onValueChange = { if (it.length <= maxLength) value = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = if (isNumberInput) KeyboardType.Number else KeyboardType.Text),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm),
            onClick = {
                if (value.length < minLength) onError(errMsg)
                else onConfirm(value)
            }
        )
    }
}
