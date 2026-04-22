package com.paxus.pay.poslinkui.demo.entry.text

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.enumeration.FSAAmountType
import com.pax.us.pay.ui.constant.entry.enumeration.FSAType
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.compose.EntryHardwareConfirmEffect
import com.paxus.pay.poslinkui.demo.entry.compose.LocalEntryInteractionLocked
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSecondaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils

/**
 * FSA amount lines driven by [com.pax.us.pay.ui.constant.entry.EntryExtraData.PARAM_FSA_AMOUNT_OPTIONS],
 * plus [EntryRequest.PARAM_FSA_OPTION] ([FSAType]).
 *
 * @param currency Currency for minor-unit display (same as amount Entry)
 * @param fsaAmountOptions Keys such as [FSAAmountType.CLINIC_AMOUNT]; unknown keys are ignored
 * @param onConfirm Builds the host [Bundle] including all requested long amount keys and FSA option
 * @param onError Validation message
 */
@Composable
fun FsaAmountsEntryScreen(
    currency: String?,
    fsaAmountOptions: Array<String>?,
    onConfirm: (Bundle) -> Unit,
    onError: (String) -> Unit
) {
    val interactionLocked = LocalEntryInteractionLocked.current
    val options = fsaAmountOptions?.mapNotNull { opt ->
        fsaAmountKey(opt)?.let { key -> opt to key }
    }.orEmpty()

    var fsaOption by remember { mutableStateOf(FSAType.HEALTH_CARE) }
    val displayByKey = remember(options) {
        mutableStateMapOf<String, String>().apply {
            options.forEach { (_, reqKey) ->
                put(reqKey, CurrencyUtils.convert(0L, currency))
            }
        }
    }
    val scroll = rememberScrollState()
    val resources = LocalContext.current.resources
    val amountPattern = "1-12"
    val amountLengths = remember(amountPattern) { ValuePatternUtils.getLengthList(amountPattern) }
    val maxDigits = amountLengths.maxOf { it ?: 0 }
    val promptInputStr = stringResource(R.string.prompt_input)
    val submit = {
        val bundle = Bundle().apply {
            putString(EntryRequest.PARAM_FSA_OPTION, fsaOption)
        }
        var valid = true
        for ((_, reqKey) in options) {
            val cents = CurrencyUtils.parse(displayByKey[reqKey])
            if (cents == 0L && !amountLengths.contains(0)) {
                onError(promptInputStr)
                valid = false
                break
            }
            bundle.putLong(reqKey, cents)
        }
        if (valid) {
            onConfirm(bundle)
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
        PosLinkText(
            text = stringResource(R.string.select_fsa_type),
            role = PosLinkTextRole.ScreenTitle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        Row {
            PosLinkSecondaryButton(
                text = FSAType.HEALTH_CARE,
                onClick = { fsaOption = FSAType.HEALTH_CARE },
                enabled = !interactionLocked,
                fillMaxWidth = false
            )
            Spacer(modifier = Modifier.width(PosLinkDesignTokens.ControlGutter))
            PosLinkSecondaryButton(
                text = FSAType.TRANSIT,
                onClick = { fsaOption = FSAType.TRANSIT },
                enabled = !interactionLocked,
                fillMaxWidth = false
            )
        }
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))

        if (options.isEmpty()) {
            PosLinkText(
                text = stringResource(R.string.fsa_no_amount_options_hint),
                role = PosLinkTextRole.Supporting
            )
        } else {
            options.forEach { (label, reqKey) ->
                PosLinkText(
                    text = TextEntryMessageFormatter.fsaAmountLinePrompt(label, resources),
                    role = PosLinkTextRole.SectionTitle
                )
                Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView / 2))
                OutlinedTextField(
                    value = displayByKey[reqKey] ?: CurrencyUtils.convert(0L, currency),
                    onValueChange = { newValue ->
                        val digits = newValue.replace("[^0-9]".toRegex(), "")
                        if (digits.length <= maxDigits) {
                            val amount = digits.ifEmpty { "0" }.toLongOrNull() ?: 0L
                            displayByKey[reqKey] = CurrencyUtils.convert(amount, currency)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !interactionLocked,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
            }
        }
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm),
            enabled = !interactionLocked,
            onClick = submit
        )
    }
}

private fun fsaAmountKey(option: String): String? = when (option) {
    FSAAmountType.HEALTH_CARE_AMOUNT -> EntryRequest.PARAM_HEALTH_CARE_AMOUNT
    FSAAmountType.CLINIC_AMOUNT -> EntryRequest.PARAM_CLINIC_AMOUNT
    FSAAmountType.PRESCRIPTION_AMOUNT -> EntryRequest.PARAM_PRESCRIPTION_AMOUNT
    FSAAmountType.DENTAL_AMOUNT -> EntryRequest.PARAM_DENTAL_AMOUNT
    FSAAmountType.VISION_AMOUNT -> EntryRequest.PARAM_VISION_AMOUNT
    FSAAmountType.COPAY_AMOUNT -> EntryRequest.PARAM_COPAY_AMOUNT
    FSAAmountType.OTC_AMOUNT -> EntryRequest.PARAM_OTC_AMOUNT
    FSAAmountType.TRANSIT_AMOUNT -> EntryRequest.PARAM_TRANSIT_AMOUNT
    else -> null
}
