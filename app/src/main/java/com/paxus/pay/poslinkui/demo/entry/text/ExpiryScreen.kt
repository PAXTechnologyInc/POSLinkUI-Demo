package com.paxus.pay.poslinkui.demo.entry.text

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

private fun formatExpiry(input: String): String {
    val digits = input.replace("[^0-9]".toRegex(), "")
    return when {
        digits.length <= 2 -> digits
        else -> digits.take(2) + "/" + digits.drop(2).take(2)
    }
}

/**
 * Compose screen for expiry date (MM/YY) entry.
 */
@Composable
fun ExpiryScreen(
    message: String,
    onConfirm: (String) -> Unit
) {
    var value by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        PosLinkText(
            text = message,
            role = PosLinkTextRole.ScreenTitle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        OutlinedTextField(
            value = value,
            onValueChange = { value = formatExpiry(it).take(5) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { PosLinkText(text = "MM/YY") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm),
            onClick = { onConfirm(value.replace("[^0-9]".toRegex(), "")) }
        )
    }
}
