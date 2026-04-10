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
 * Returns true when [fieldLength] matches one of the lengths encoded in [valuePattern].
 */
private fun isLengthAllowedForPattern(fieldLength: Int, valuePattern: String?): Boolean {
    for (candidate in ValuePatternUtils.getLengthList(valuePattern ?: "")) {
        if (candidate == fieldLength) return true
    }
    return false
}

/**
 * Field configuration for [AVSScreen].
 */
data class AvsScreenConfig(
    val maxLengthAddr: Int,
    val maxLengthZip: Int,
    val valuePatternAddr: String?,
    val valuePatternZip: String?,
    val allowText: Boolean,
    val usePasswordMask: Boolean
)

/**
 * Compose screen for AVS (Address + Zip) entry.
 */
@Composable
fun AVSScreen(
    config: AvsScreenConfig,
    onConfirm: (String, String) -> Unit,
    onError: (String) -> Unit
) {
    var addr by remember { mutableStateOf("") }
    var zip by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        PosLinkText(
            text = stringResource(R.string.pls_input_address),
            role = PosLinkTextRole.ScreenTitle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        OutlinedTextField(
            value = addr,
            onValueChange = { if (it.length <= config.maxLengthAddr) addr = it },
            modifier = Modifier.fillMaxWidth(),
            label = { PosLinkText(text = stringResource(R.string.pls_input_address)) },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        PosLinkText(
            text = stringResource(R.string.pls_input_zip_code),
            role = PosLinkTextRole.SectionTitle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        OutlinedTextField(
            value = zip,
            onValueChange = { if (it.length <= config.maxLengthZip) zip = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (config.allowText) KeyboardType.Text else KeyboardType.Number
            ),
            visualTransformation = if (config.usePasswordMask) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        val errAddr = stringResource(R.string.pls_input_address) + ": " +
            stringResource(R.string.prompt_input_length, config.valuePatternAddr ?: "")
        val errZip = stringResource(R.string.prompt_input_length, config.valuePatternZip ?: "") +
            stringResource(R.string.pls_input_zip_code)
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm),
            onClick = {
                when {
                    !isLengthAllowedForPattern(addr.length, config.valuePatternAddr) -> onError(errAddr)
                    !isLengthAllowedForPattern(zip.length, config.valuePatternZip) -> onError(errZip)
                    else -> onConfirm(addr, zip)
                }
            }
        )
    }
}
