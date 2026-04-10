package com.paxus.pay.poslinkui.demo.entry.compose

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.InformationEntry
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import com.paxus.pay.poslinkui.demo.entry.EntryGapActions
import com.paxus.pay.poslinkui.demo.entry.information.InfoScreen
import com.paxus.pay.poslinkui.demo.entry.text.AVSScreen
import com.paxus.pay.poslinkui.demo.entry.text.AvsScreenConfig
import com.paxus.pay.poslinkui.demo.entry.text.FsaAmountsEntryScreen
import com.paxus.pay.poslinkui.demo.entry.text.TextEntryMessageFormatter
import com.paxus.pay.poslinkui.demo.entry.text.invoice.InvoiceNumberScreen
import com.paxus.pay.poslinkui.demo.entry.text.amount.AmountScreen
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.Toast
import com.paxus.pay.poslinkui.demo.utils.Toast.TYPE
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel

/** Detail text/amount/information routes split from [EntryScreenRouter] for S104. */
@Composable
internal fun AddressEntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel,
    responseKey: String,
    entryAction: String?
) {
    val message = TextEntryMessageFormatter.singleLinePrompt(entryAction, extras, activity.resources)
    val pattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)
    val maxLen = if (extras.containsKey(EntryExtraData.PARAM_MAX_LENGTH)) {
        extras.getInt(EntryExtraData.PARAM_MAX_LENGTH)
    } else {
        null
    }
    val resolvedPattern = pattern?.takeIf { it.isNotBlank() }
        ?: maxLen?.let { "1-$it" }
        ?: "1-64"
    InvoiceNumberScreen(
        message = message,
        valuePattern = resolvedPattern,
        parityLog = "Address parity v1 active",
        forceTextKeyboard = true,
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
internal fun OrderNumberEntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel,
    responseKey: String,
    entryAction: String?
) {
    val message = TextEntryMessageFormatter.singleLinePrompt(entryAction, extras, activity.resources)
    val pattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)
    val maxLen = if (extras.containsKey(EntryExtraData.PARAM_MAX_LENGTH)) {
        extras.getInt(EntryExtraData.PARAM_MAX_LENGTH)
    } else {
        null
    }
    val resolvedPattern = pattern?.takeIf { it.isNotBlank() }
        ?: maxLen?.let { "1-$it" }
        ?: "1-64"
    InvoiceNumberScreen(
        message = message,
        valuePattern = resolvedPattern,
        parityLog = "OrderNumber parity v1 active",
        forceTextKeyboard = true,
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
internal fun ServerIdEntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel,
    responseKey: String,
    entryAction: String?
) {
    val message = TextEntryMessageFormatter.singleLinePrompt(entryAction, extras, activity.resources)
    val pattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)
    val maxLen = if (extras.containsKey(EntryExtraData.PARAM_MAX_LENGTH)) {
        extras.getInt(EntryExtraData.PARAM_MAX_LENGTH)
    } else {
        null
    }
    val resolvedPattern = pattern?.takeIf { it.isNotBlank() }
        ?: maxLen?.let { "1-$it" }
        ?: "1-64"
    InvoiceNumberScreen(
        message = message,
        valuePattern = resolvedPattern,
        parityLog = "ServerId parity v1 active",
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
internal fun TableNumberEntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel,
    responseKey: String,
    entryAction: String?
) {
    val message = TextEntryMessageFormatter.singleLinePrompt(entryAction, extras, activity.resources)
    val pattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)
    val maxLen = if (extras.containsKey(EntryExtraData.PARAM_MAX_LENGTH)) {
        extras.getInt(EntryExtraData.PARAM_MAX_LENGTH)
    } else {
        null
    }
    val resolvedPattern = pattern?.takeIf { it.isNotBlank() }
        ?: maxLen?.let { "1-$it" }
        ?: "1-64"
    InvoiceNumberScreen(
        message = message,
        valuePattern = resolvedPattern,
        parityLog = "TableNumber parity v1 active",
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
internal fun ClerkIdEntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel,
    responseKey: String,
    entryAction: String?
) {
    val message = TextEntryMessageFormatter.singleLinePrompt(entryAction, extras, activity.resources)
    val pattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)
    val maxLen = if (extras.containsKey(EntryExtraData.PARAM_MAX_LENGTH)) {
        extras.getInt(EntryExtraData.PARAM_MAX_LENGTH)
    } else {
        null
    }
    val resolvedPattern = pattern?.takeIf { it.isNotBlank() }
        ?: maxLen?.let { "1-$it" }
        ?: "1-64"
    InvoiceNumberScreen(
        message = message,
        valuePattern = resolvedPattern,
        parityLog = "ClerkId parity v1 active",
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
internal fun InvoiceNumberEntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel,
    responseKey: String,
    entryAction: String?
) {
    val message = TextEntryMessageFormatter.singleLinePrompt(entryAction, extras, activity.resources)
    val pattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)
    val maxLen = if (extras.containsKey(EntryExtraData.PARAM_MAX_LENGTH)) {
        extras.getInt(EntryExtraData.PARAM_MAX_LENGTH)
    } else {
        null
    }
    val resolvedPattern = pattern?.takeIf { it.isNotBlank() }
        ?: maxLen?.let { "1-$it" }
        ?: "1-64"
    InvoiceNumberScreen(
        message = message,
        valuePattern = resolvedPattern,
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
internal fun AvsEntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    AVSScreen(
        config = AvsScreenConfig(
            maxLengthAddr = ValuePatternUtils.getMaxLength(
                extras.getString(EntryExtraData.PARAM_ADDRESS_PATTERN) ?: "1-30"
            ),
            maxLengthZip = ValuePatternUtils.getMaxLength(
                extras.getString(EntryExtraData.PARAM_ZIP_CODE_PATTERN) ?: "1-12"
            ),
            valuePatternAddr = extras.getString(EntryExtraData.PARAM_ADDRESS_PATTERN),
            valuePatternZip = extras.getString(EntryExtraData.PARAM_ZIP_CODE_PATTERN),
            allowText = extras.getString(EntryExtraData.PARAM_EINPUT_TYPE) != "NUM",
            usePasswordMask = extras.getString(EntryExtraData.PARAM_EINPUT_TYPE) == "PASSWORD"
        ),
        onConfirm = { address, zip ->
            viewModel.sendNext(
                Bundle().apply {
                    putString(EntryRequest.PARAM_ADDRESS, address)
                    putString(EntryRequest.PARAM_ZIP_CODE, zip)
                }
            )
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
internal fun FsaEntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
    val opts = extras.getStringArray(EntryExtraData.PARAM_FSA_AMOUNT_OPTIONS)
    FsaAmountsEntryScreen(
        currency = currency,
        fsaAmountOptions = opts,
        onConfirm = { b -> viewModel.sendNext(b) },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
internal fun AmountEntryRoute(
    entryAction: String?,
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel,
    amountResponseKey: String
) {
    val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
    val valuePattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN, "1-12")
    val maxLength = ValuePatternUtils.getMaxLength(valuePattern ?: "")
    val message = TextEntryMessageFormatter.amountKindPrompt(
        entryAction,
        extras,
        currency,
        activity.resources
    )
    AmountScreen(
        message = message,
        maxLength = maxLength,
        currency = currency,
        valuePattern = valuePattern,
        onConfirm = { cents ->
            viewModel.sendNext(
                Bundle().apply { putLong(amountResponseKey, cents) }
            )
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
internal fun TransInfoRoute(extras: Bundle, viewModel: EntryViewModel) {
    val keys = extras.getStringArray(EntryExtraData.PARAM_INFORMATION_KEY)
    val values = extras.getStringArray(EntryExtraData.PARAM_INFORMATION_VALUE)
    val text = buildTransInfoText(keys, values)
    InfoScreen(
        content = text,
        onConfirm = { viewModel.sendNext(null) }
    )
}

@Composable
internal fun InformationEntryRoute(
    action: String?,
    extras: Bundle,
    viewModel: EntryViewModel
) {
    when (action) {
        InformationEntry.ACTION_DISPLAY_APPROVE_MESSAGE -> ApproveEntryRoute(extras, viewModel)
        EntryGapActions.ACTION_DISPLAY_VISA_INSTALLMENT_TRANSACTION_END ->
            VisaInstallmentEndInfoRoute(extras, viewModel)
        else -> TransInfoRoute(extras, viewModel)
    }
}

@Composable
internal fun VisaInstallmentEndInfoRoute(extras: Bundle, viewModel: EntryViewModel) {
    val title = extras.getString(EntryExtraData.PARAM_TITLE)
    val content = resolveVisaInstallmentEndContent(
        message = extras.getString(EntryExtraData.PARAM_MESSAGE),
        title = title
    )
    InfoScreen(
        title = title,
        content = content,
        onConfirm = { viewModel.sendNext(null) }
    )
}

internal fun resolveVisaInstallmentEndContent(message: String?, title: String?): String =
    message ?: title ?: ""

internal fun buildTransInfoText(keys: Array<String?>?, values: Array<String?>?): String {
    if (keys == null || values == null || keys.isEmpty()) return ""
    return buildString {
        for (i in keys.indices) {
            if (i >= values.size) continue
            append(keys[i]).append(" ").append(values[i]).append("\n")
        }
    }
}

@Composable
internal fun UnmappedEntryPlaceholder(action: String?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(PosLinkDesignTokens.ScreenPadding + PosLinkDesignTokens.CompactSpacing),
        contentAlignment = Alignment.Center
    ) {
        PosLinkText(
            text = "Compose route not handled: ${action ?: "(null)"}",
            role = PosLinkTextRole.Body
        )
    }
}
