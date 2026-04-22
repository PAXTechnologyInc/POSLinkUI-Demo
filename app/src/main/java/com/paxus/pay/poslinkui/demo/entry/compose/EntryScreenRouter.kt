package com.paxus.pay.poslinkui.demo.entry.compose

import android.content.res.Resources
import android.os.Bundle
import android.view.KeyEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.pax.us.pay.ui.constant.entry.ConfirmationEntry
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.InformationEntry
import com.pax.us.pay.ui.constant.entry.OptionEntry
import com.pax.us.pay.ui.constant.entry.PoslinkEntry
import com.pax.us.pay.ui.constant.entry.SecurityEntry
import com.pax.us.pay.ui.constant.entry.SignatureEntry
import com.pax.us.pay.ui.constant.entry.TextEntry
import com.pax.us.pay.ui.constant.entry.enumeration.InputType
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import com.pax.us.pay.ui.constant.entry.enumeration.TransactionStatus
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmationMessageFormatter
import com.pax.us.pay.ui.constant.status.StatusData
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmationMessageStyle
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmationScreen
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmationScreenLayout
import com.paxus.pay.poslinkui.demo.entry.security.SecurityMessageFormatter
import com.paxus.pay.poslinkui.demo.entry.security.SecuritySecureAreaScreen
import com.paxus.pay.poslinkui.demo.entry.text.GenericStringEntryScreen
import com.paxus.pay.poslinkui.demo.entry.text.OrigTransDateScreen
import com.paxus.pay.poslinkui.demo.entry.text.TextEntryMessageFormatter
import com.paxus.pay.poslinkui.demo.entry.text.TextEntryKind
import com.paxus.pay.poslinkui.demo.entry.text.TextEntryResponseParams
import com.paxus.pay.poslinkui.demo.entry.text.invoice.InvoiceNumberScreen
import com.paxus.pay.poslinkui.demo.entry.text.amount.CashbackScreen
import com.paxus.pay.poslinkui.demo.entry.text.amount.CashbackScreenProps
import com.paxus.pay.poslinkui.demo.entry.text.amount.TipScreen
import com.paxus.pay.poslinkui.demo.entry.text.amount.TipScreenAmountFields
import com.paxus.pay.poslinkui.demo.entry.text.amount.TipScreenCallbacks
import com.paxus.pay.poslinkui.demo.entry.text.amount.TipScreenDisplayFields
import com.paxus.pay.poslinkui.demo.entry.text.amount.TipScreenModeFlags
import com.paxus.pay.poslinkui.demo.entry.text.amount.TipScreenModel
import com.paxus.pay.poslinkui.demo.entry.text.amount.TotalAmountScreen
import com.paxus.pay.poslinkui.demo.entry.text.amount.TotalAmountScreenContent
import com.paxus.pay.poslinkui.demo.ui.PosLinkScreenRoot
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.Toast
import com.paxus.pay.poslinkui.demo.utils.Toast.TYPE
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView
import com.paxus.pay.poslinkui.demo.viewmodel.EntryUiState
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel
import com.paxus.pay.poslinkui.demo.viewmodel.ManagerResponseEvent
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel
import kotlinx.coroutines.delay

/**
 * Maps [EntryUiState] to a concrete Compose tree. Extend with more `when` branches as screens leave Fragment.
 */
@Composable
fun EntryScreenRouter(
    state: EntryUiState,
    viewModel: EntryViewModel,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as FragmentActivity
    val currentState by rememberUpdatedState(state)

    LaunchedEffect(Unit) {
        viewModel.managerResponse.collect { ev ->
            when (ev) {
                ManagerResponseEvent.Accepted -> { /* host may finish flow; optional UI later */ }
                is ManagerResponseEvent.Declined -> {
                    Toast(activity).show(ev.message, TYPE.FAILURE)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.keyEvents.collect { code ->
            val bypassBackAbort = currentState.entryAction == PoslinkEntry.ACTION_SHOW_TEXT_BOX &&
                isPoslinkTextBoxHardKeyEnabled(currentState.extras) &&
                hasPoslinkTextBoxPhysicalKeyboard(currentState.extras)
            if (code == KeyEvent.KEYCODE_BACK && !bypassBackAbort) {
                viewModel.sendAbort()
            }
        }
    }

    key(state.revision) {
        PosLinkScreenRoot(modifier = modifier) { _ ->
            when {
                state.categories.contains(SecurityEntry.CATEGORY) -> {
                    SecurityEntryRoute(
                        state.entryAction,
                        state.extras,
                        viewModel,
                        activity.resources
                    )
                }
                state.categories.contains(ConfirmationEntry.CATEGORY) -> {
                    ConfirmationSpecialRoute(
                        action = state.entryAction,
                        extras = state.extras,
                        viewModel = viewModel,
                        genericRoute = {
                            GenericConfirmationRoute(
                                action = state.entryAction,
                                revision = state.revision,
                                extras = state.extras,
                                viewModel = viewModel
                            )
                        }
                    )
                }
                state.entryAction == SignatureEntry.ACTION_SIGNATURE -> {
                    SignatureCategoryEntryRoute(state.extras, viewModel)
                }
                TextEntry.ACTION_ENTER_AMOUNT == state.entryAction -> {
                    AmountEntryRoute(
                        state.entryAction,
                        state.extras,
                        activity,
                        viewModel,
                        EntryRequest.PARAM_AMOUNT
                    )
                }
                state.categories.contains(TextEntry.CATEGORY) -> {
                    TextEntryComposeRoute(state.entryAction, state.extras, activity, viewModel)
                }
                state.categories.contains(OptionEntry.CATEGORY) -> {
                    OptionListEntryRoute(state.entryAction, state.extras, viewModel, activity.resources)
                }
                state.categories.contains(PoslinkEntry.CATEGORY) -> {
                    PoslinkEntryRoute(
                        action = state.entryAction,
                        extras = state.extras,
                        activity = activity,
                        viewModel = viewModel,
                        isContentCleared = state.poslinkContentCleared
                    )
                }
                state.categories.contains(SignatureEntry.CATEGORY) -> {
                    SignatureCategoryEntryRoute(state.extras, viewModel)
                }
                state.categories.contains(InformationEntry.CATEGORY) -> {
                    InformationEntryRoute(
                        action = state.entryAction,
                        extras = state.extras,
                        viewModel = viewModel
                    )
                }
                else -> UnmappedEntryPlaceholder(state.entryAction)
            }
        }
    }
}

@Composable
private fun SecurityEntryRoute(
    entryAction: String?,
    extras: Bundle,
    viewModel: EntryViewModel,
    resources: Resources
) {
    val display = SecurityMessageFormatter.prompt(entryAction, extras, resources)
        .ifBlank { stringResource(R.string.prompt_use_pinpad) }
    if (entryAction == SecurityEntry.ACTION_ENTER_PIN) {
        LaunchedEffect(entryAction) {
            Logger.i("Pin parity v3 active")
        }
    }
    SecuritySecureAreaScreen(
        entryAction = entryAction,
        extras = extras,
        message = display,
        viewModel = viewModel,
        onContinue = { viewModel.sendNext(null) }
    )
}

@Composable
internal fun ApproveEntryRoute(extras: Bundle, viewModel: EntryViewModel) {
    val activity = LocalContext.current as FragmentActivity
    val secondVm = ViewModelProvider(activity)[SecondScreenInfoViewModel::class.java]
    LaunchedEffect(
        extras.getString(EntryExtraData.PARAM_CARD_TYPE),
        extras.getString(StatusData.PARAM_MSG_PRIMARY)
    ) {
        secondVm.updateAllData(
            "",
            "",
            TransactionStatus.APPROVED.name,
            null,
            extras.getString(StatusData.PARAM_MSG_PRIMARY, ""),
            ""
        )
    }
    ApproveMessageScreen(
        cardType = extras.getString(EntryExtraData.PARAM_CARD_TYPE),
        onComplete = { viewModel.sendNext(null) }
    )
}

@Composable
private fun GenericConfirmationRoute(
    action: String?,
    revision: Long,
    extras: Bundle,
    viewModel: EntryViewModel
) {
    val interactionLocked by viewModel.interactionLocked.collectAsState()
    val activity = LocalContext.current as FragmentActivity
    val resources = activity.resources
    val secondVm = ViewModelProvider(activity)[SecondScreenInfoViewModel::class.java]
    val options = extras.getStringArray(EntryExtraData.PARAM_OPTIONS)
    val presentation = remember(revision, action) {
        ConfirmationMessageFormatter.build(action, extras, resources)
    }

    LaunchedEffect(revision, action) {
        when (action) {
            ConfirmationEntry.ACTION_CONFIRM_CASH_PAYMENT -> {
                Logger.i("CashPaymentConfirm parity v1 active")
            }
            ConfirmationEntry.ACTION_CONFIRM_PRINT_CUSTOMER_COPY -> {
                Logger.i("CustomerCopyConfirm parity v1 active")
            }
            ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL,
            ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL -> {
                secondVm.updateAllData(
                    "",
                    "",
                    TransactionStatus.PARTIALLY_APPROVED.name,
                    null,
                    ConfirmationMessageFormatter.partialApprovalStatusTitle(extras),
                    ""
                )
            }
        }
    }

    val autoMs = presentation.autoConfirmAfterMs
    LaunchedEffect(revision, autoMs) {
        if (autoMs != null && autoMs > 0L &&
            action == ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT
        ) {
            delay(autoMs)
            viewModel.sendNext(buildConfirmationSubmitBundle(confirmed = true))
        }
    }

    val isCashPayment = action == ConfirmationEntry.ACTION_CONFIRM_CASH_PAYMENT
    val positive = when {
        isCashPayment -> stringResource(R.string.dialog_ok)
        else -> presentation.positiveText
            ?: options?.firstOrNull()
            ?: stringResource(R.string.confirm_option_accept)
    }
    val negative: String? = when {
        presentation.hideNegativeButton -> null
        isCashPayment -> stringResource(R.string.cancel_btn)
        presentation.negativeText != null -> presentation.negativeText
        (options?.size ?: 0) > 1 -> options?.getOrNull(1)
        else -> null
    }

    // 对齐 golive fragment_confirmation.xml：双 Button 均为填充样式；现金与其它双键均用 legacy 主按钮槽
    val confirmationLayout = ConfirmationScreenLayout(
        messageStyle = ConfirmationMessageStyle.Plain,
        packedVerticalBias = 0.3f,
        messageRole = PosLinkTextRole.ConfirmationMessage,
        bothActionsPrimaryLegacy = isCashPayment || !negative.isNullOrBlank()
    )

    ConfirmationScreen(
        message = presentation.message,
        positiveText = positive,
        negativeText = negative,
        onConfirm = {
            viewModel.sendNext(buildConfirmationSubmitBundle(confirmed = true))
        },
        onCancel = {
            viewModel.sendNext(buildConfirmationSubmitBundle(confirmed = false))
        },
        buttonsEnabled = !interactionLocked,
        layout = confirmationLayout
    )
}

@Composable
private fun TextEntryComposeRoute(
    entryAction: String?,
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    if (entryAction == TextEntry.ACTION_ENTER_TIP) {
        TipEntryRoute(extras, activity, viewModel)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_TOTAL_AMOUNT) {
        TotalAmountEntryRoute(extras, activity, viewModel)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_CASH_BACK) {
        CashbackEntryRoute(extras, activity, viewModel)
        return
    }
    when (val kind = TextEntryResponseParams.resolveKind(entryAction)) {
        is TextEntryKind.AmountMinor ->
            AmountEntryRoute(entryAction, extras, activity, viewModel, kind.responseKey)
        is TextEntryKind.SingleString ->
            GenericStringEntryRoute(entryAction, extras, kind.responseKey, activity, viewModel)
        TextEntryKind.Avs ->
            AvsEntryRoute(extras, activity, viewModel)
        TextEntryKind.Fsa ->
            FsaEntryRoute(extras, activity, viewModel)
        TextEntryKind.Unknown ->
            UnmappedEntryPlaceholder(entryAction)
    }
}

@Composable
private fun TotalAmountEntryRoute(extras: Bundle, activity: FragmentActivity, viewModel: EntryViewModel) {
    val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
    val valuePattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN, "1-12")
    val maxLength = ValuePatternUtils.getMaxLength(valuePattern ?: "1-12")
    val lengthList = ValuePatternUtils.getLengthList(valuePattern ?: "1-12")
    val promptInput = stringResource(R.string.prompt_input)
    val baseAmount = extras.getLong(EntryExtraData.PARAM_BASE_AMOUNT)
    val noTipEnabled = extras.getBoolean(EntryExtraData.PARAM_ENABLE_NO_TIP_SELECTION, false)
    val tipName = extras.getString(EntryExtraData.PARAM_TIP_NAME)
    TotalAmountScreen(
        content = TotalAmountScreenContent(
            message = stringResource(R.string.prompt_input_total_amount),
            baseAmount = baseAmount,
            tipName = tipName,
            currency = currency,
            maxLength = maxLength,
            noTipEnabled = noTipEnabled
        ),
        onConfirm = { amount ->
            viewModel.sendNext(Bundle().apply { putLong(EntryRequest.PARAM_TOTAL_AMOUNT, amount) })
        },
        onNoTip = {
            if (baseAmount == 0L && !lengthList.contains(0)) {
                Toast(activity).show(promptInput, TYPE.FAILURE)
            } else {
                viewModel.sendNext(Bundle().apply { putLong(EntryRequest.PARAM_TOTAL_AMOUNT, baseAmount) })
            }
        }
    )
}

@Composable
private fun TipEntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
    val valuePattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)
    val maxLength = valuePattern
        ?.takeIf { it.isNotBlank() }
        ?.let {
            runCatching { ValuePatternUtils.getMaxLength(it) }
                .getOrDefault(12)
        }
        ?: 12
    val tipUnit = extras.getString(EntryExtraData.PARAM_TIP_UNIT, "C") ?: "C"
    val isTipCentCase = !tipUnit.equals("D", ignoreCase = true)
    val tipOptionsRaw = extras.getStringArray(EntryExtraData.PARAM_TIP_OPTIONS) ?: emptyArray()
    val tipRateOptions = extras.getStringArray(EntryExtraData.PARAM_TIP_RATE_OPTIONS) ?: emptyArray()
    val tipOptions = tipOptionsRaw
        .filterNotNull()
        .filter { it.isNotBlank() }
        .mapIndexed { idx, rawTitle ->
            val rawAmount = rawTitle.toLongOrNull() ?: 0L
            val parsedCents = if (isTipCentCase) rawAmount else rawAmount * 100L
            SelectOptionsView.Option(
                idx,
                CurrencyUtils.convert(parsedCents, currency),
                tipRateOptions.getOrNull(idx),
                parsedCents
            )
        }
    val tipName = extras.getString(EntryExtraData.PARAM_TIP_NAME)
        ?: activity.getString(R.string.tip_name)
    val baseAmount = when {
        extras.containsKey(EntryExtraData.PARAM_BASE_AMOUNT) ->
            extras.getLong(EntryExtraData.PARAM_BASE_AMOUNT, -1L)
        extras.containsKey("baseAmount") ->
            extras.getLong("baseAmount", -1L)
        else -> -1L
    }
    val noTipEnabled = extras.getBoolean(EntryExtraData.PARAM_ENABLE_NO_TIP_SELECTION, false)
    val tipLengthList = valuePattern
        ?.takeIf { it.isNotBlank() }
        ?.let { runCatching { ValuePatternUtils.getLengthList(it) }.getOrElse { mutableListOf(0) } }
        ?: mutableListOf(0)
    fun canSubmitTip(value: Long): Boolean = value != 0L || tipLengthList.contains(0)
    fun submitTip(value: Long, tipFieldModified: Boolean) {
        if (value == 0L && !canSubmitTip(value)) {
            Toast(activity).show(activity.getString(R.string.prompt_input), TYPE.FAILURE)
            return
        }
        val payload = Bundle().apply {
            if (!(value == 0L && tipFieldModified)) {
                putLong(EntryRequest.PARAM_TIP, value)
            }
        }
        viewModel.sendNext(payload)
    }
    val tipParityLog = when {
        !noTipEnabled -> "TipDisableNoTip parity v3 active"
        isTipCentCase -> "TipCent parity v2 active"
        else -> "TipDollar parity v3 active"
    }

    TipScreen(
        TipScreenModel(
            amount = TipScreenAmountFields(
                baseAmount = baseAmount,
                currency = currency,
                maxLength = maxLength,
                valuePattern = valuePattern
            ),
            display = TipScreenDisplayFields(
                tipInfoList = emptyList(),
                tipName = tipName,
                tipOptions = tipOptions,
                noTipEnabled = noTipEnabled
            ),
            flags = TipScreenModeFlags(
                isTipCentCase = isTipCentCase,
                parityLog = tipParityLog
            ),
            callbacks = TipScreenCallbacks(
                onTipOptionSelected = { selectedTip ->
                    submitTip(selectedTip, tipFieldModified = false)
                },
                onNoTipSelected = { viewModel.sendNext(Bundle()) },
                onConfirm = { amount, tipFieldModified ->
                    submitTip(amount, tipFieldModified)
                },
                onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
            )
        )
    )
}

@Composable
private fun CashbackEntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
    val promptOther = extras.getBoolean(EntryExtraData.PARAM_ENABLE_OTHER_PROMPT, false)
    val valuePattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)
    val normalizedValuePattern = valuePattern
        ?.takeIf { it.isNotBlank() }
        ?.takeIf { raw ->
            runCatching { ValuePatternUtils.getLengthList(raw) }
                .getOrElse { mutableListOf() }
                .isNotEmpty()
        }
        ?: "0-12"
    val maxLength = runCatching { ValuePatternUtils.getMaxLength(normalizedValuePattern) }
        .getOrDefault(12)
    val rawOptions = extras.getStringArray(EntryExtraData.PARAM_CASHBACK_OPTIONS)
        ?: extras.getStringArray(EntryExtraData.PARAM_OPTIONS)
        ?: emptyArray()
    val options = rawOptions
        .filterNotNull()
        .filter { it.isNotBlank() }
        .mapNotNull { raw ->
            val parsed = CurrencyUtils.parse(raw)
            if (parsed == 0L) null else parsed
        }
        .mapIndexed { idx, amount ->
            SelectOptionsView.Option(
                idx,
                CurrencyUtils.convert(amount, currency),
                null,
                amount
            )
        }

    CashbackScreen(
        props = CashbackScreenProps(
            options = options,
            promptOther = promptOther,
            currency = currency,
            maxLength = maxLength,
            valuePattern = normalizedValuePattern,
            parityLog = if (promptOther) {
                "CashBackEnable parity v3 active"
            } else {
                "CashBackDisable parity v3 active"
            }
        ),
        onOptionSelected = { /* selection reflected via screen local value */ },
        onConfirm = { amount ->
            viewModel.sendNext(Bundle().apply { putLong(EntryRequest.PARAM_CASHBACK_AMOUNT, amount) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
private fun GenericStringEntryRoute(
    entryAction: String?,
    extras: Bundle,
    responseKey: String,
    activity: FragmentActivity,
    viewModel: EntryViewModel
) {
    val useFleetLegacyInputStyle = isFleetTextInputAction(entryAction)
    val fleetDefaults = fleetTextEntryDefaults[entryAction]
    if (entryAction == TextEntry.ACTION_ENTER_INVOICE_NUMBER) {
        InvoiceNumberEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_CLERK_ID) {
        ClerkIdEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_TABLE_NUMBER) {
        TableNumberEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_SERVER_ID) {
        ServerIdEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_ORDER_NUMBER) {
        OrderNumberEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_ADDRESS) {
        AddressEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_DEST_ZIPCODE) {
        ZipCodeEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_GUEST_NUMBER) {
        GuestNumberEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_EXPIRY_DATE) {
        ExpiryDateEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_TRANS_NUMBER) {
        TransNumberEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_VOUCHER_DATA) {
        VoucherNumberEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_AUTH) {
        VoucherAuthCodeEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_PROMPT_RESTRICTION_CODE) {
        RestrictionCodeEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_2) {
        AdditionalFleetData2EntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    if (entryAction == TextEntry.ACTION_ENTER_ORIG_DATE) {
        OrigDateEntryRoute(extras, activity, viewModel, responseKey, entryAction)
        return
    }
    val message = TextEntryMessageFormatter.singleLinePrompt(entryAction, extras, activity.resources)
    val pattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)
        ?.takeIf { it.isNotBlank() }
        ?: fleetDefaults?.valuePattern
    val maxLen = if (extras.containsKey(EntryExtraData.PARAM_MAX_LENGTH)) {
        extras.getInt(EntryExtraData.PARAM_MAX_LENGTH)
    } else {
        pattern?.let { ValuePatternUtils.getMaxLength(it) }
            ?.takeIf { it > 0 }
    }
    val eInput = extras.getString(EntryExtraData.PARAM_EINPUT_TYPE)
        ?: fleetDefaults?.inputType
    GenericStringEntryScreen(
        message = message,
        valuePattern = pattern,
        maxLengthFallback = maxLen,
        eInputType = eInput,
        useLegacyFleetInputStyle = useFleetLegacyInputStyle,
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

private fun isFleetTextInputAction(action: String?): Boolean = when (action) {
    TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_1,
    TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_2,
    TextEntry.ACTION_ENTER_CUSTOMER_DATA,
    TextEntry.ACTION_ENTER_DEPARTMENT_NUMBER,
    TextEntry.ACTION_ENTER_DRIVER_ID,
    TextEntry.ACTION_ENTER_EMPLOYEE_NUMBER,
    TextEntry.ACTION_ENTER_FLEET_PO_NUMBER,
    TextEntry.ACTION_ENTER_FLEET_PROMPT_CODE,
    TextEntry.ACTION_ENTER_HUBOMETER,
    TextEntry.ACTION_ENTER_JOB_ID,
    TextEntry.ACTION_ENTER_LICENSE_NUMBER,
    TextEntry.ACTION_ENTER_MAINTENANCE_ID,
    TextEntry.ACTION_ENTER_ODOMETER,
    TextEntry.ACTION_ENTER_REEFER_HOURS,
    TextEntry.ACTION_ENTER_TRAILER_ID,
    TextEntry.ACTION_ENTER_TRIP_NUMBER,
    TextEntry.ACTION_ENTER_UNIT_ID,
    TextEntry.ACTION_ENTER_USER_ID,
    TextEntry.ACTION_ENTER_VEHICLE_ID,
    TextEntry.ACTION_ENTER_VEHICLE_NUMBER -> true
    else -> false
}

private data class FleetTextEntryDefaults(
    val valuePattern: String,
    val inputType: String
)

private val fleetTextEntryDefaults: Map<String, FleetTextEntryDefaults> = mapOf(
    TextEntry.ACTION_ENTER_CUSTOMER_DATA to FleetTextEntryDefaults("0-24", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_DEPARTMENT_NUMBER to FleetTextEntryDefaults("0-24", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_DRIVER_ID to FleetTextEntryDefaults("0-20", InputType.NUM),
    TextEntry.ACTION_ENTER_EMPLOYEE_NUMBER to FleetTextEntryDefaults("0-20", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_FLEET_PROMPT_CODE to FleetTextEntryDefaults("0,2", InputType.NUM),
    TextEntry.ACTION_ENTER_HUBOMETER to FleetTextEntryDefaults("0-9", InputType.NUM),
    TextEntry.ACTION_ENTER_JOB_ID to FleetTextEntryDefaults("0-24", InputType.NUM),
    TextEntry.ACTION_ENTER_LICENSE_NUMBER to FleetTextEntryDefaults("0-20", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_MAINTENANCE_ID to FleetTextEntryDefaults("0-15", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_ODOMETER to FleetTextEntryDefaults("0-16", InputType.NUM),
    TextEntry.ACTION_ENTER_FLEET_PO_NUMBER to FleetTextEntryDefaults("0-10", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_REEFER_HOURS to FleetTextEntryDefaults("0-7", InputType.NUM),
    TextEntry.ACTION_ENTER_TRAILER_ID to FleetTextEntryDefaults("0-10", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_TRIP_NUMBER to FleetTextEntryDefaults("0-15", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_UNIT_ID to FleetTextEntryDefaults("0-10", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_USER_ID to FleetTextEntryDefaults("0-24", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_VEHICLE_ID to FleetTextEntryDefaults("0-16", InputType.NUM),
    TextEntry.ACTION_ENTER_VEHICLE_NUMBER to FleetTextEntryDefaults("0-20", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_1 to FleetTextEntryDefaults("0-20", InputType.ALLTEXT),
    TextEntry.ACTION_ENTER_ADDITIONAL_FLEET_DATA_2 to FleetTextEntryDefaults("0-20", InputType.ALLTEXT)
)

@Composable
private fun AdditionalFleetData2EntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel,
    responseKey: String,
    entryAction: String?
) {
    val fleetDefaults = fleetTextEntryDefaults[entryAction]
    val message = TextEntryMessageFormatter.singleLinePrompt(entryAction, extras, activity.resources)
    val pattern = extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)
        ?.takeIf { it.isNotBlank() }
        ?: fleetDefaults?.valuePattern
    val maxLen = if (extras.containsKey(EntryExtraData.PARAM_MAX_LENGTH)) {
        extras.getInt(EntryExtraData.PARAM_MAX_LENGTH)
    } else {
        pattern?.let { ValuePatternUtils.getMaxLength(it) }
            ?.takeIf { it > 0 }
    }
    val eInput = extras.getString(EntryExtraData.PARAM_EINPUT_TYPE)
        ?: fleetDefaults?.inputType
    GenericStringEntryScreen(
        message = message,
        valuePattern = pattern,
        maxLengthFallback = maxLen,
        eInputType = eInput,
        useLegacyFleetInputStyle = true,
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
private fun AdditionalFleetData1EntryRoute(
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
        parityLog = "AdditionalFleetData1 parity v1 active",
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
private fun RestrictionCodeEntryRoute(
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
        parityLog = "RestrictionCode parity v1 active",
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
private fun VoucherAuthCodeEntryRoute(
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
        parityLog = "VoucherAuthCode parity v1 active",
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
private fun VoucherNumberEntryRoute(
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
        parityLog = "VoucherNumber parity v1 active",
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
private fun TransNumberEntryRoute(
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
        parityLog = "TransNumber parity v1 active",
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
private fun OrigDateEntryRoute(
    extras: Bundle,
    activity: FragmentActivity,
    viewModel: EntryViewModel,
    responseKey: String,
    entryAction: String?
) {
    val message = TextEntryMessageFormatter.singleLinePrompt(entryAction, extras, activity.resources)
    val dateFormat = extras.getString(EntryExtraData.PARAM_DATE_FORMAT)
        ?: extras.getString("dateFormat")
    OrigTransDateScreen(
        message = message,
        dateFormat = dateFormat,
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        }
    )
}

@Composable
private fun ExpiryDateEntryRoute(
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
    val safePattern = pattern
        ?.takeIf { it.isNotBlank() }
        ?.takeIf { raw ->
            runCatching { ValuePatternUtils.getLengthList(raw) }
                .getOrElse { mutableListOf() }
                .isNotEmpty()
        }
    val resolvedPattern = safePattern
        ?: maxLen?.takeIf { it > 0 }?.let { "1-$it" }
        ?: "4"
    InvoiceNumberScreen(
        message = message,
        valuePattern = resolvedPattern,
        parityLog = "ExpiryDate parity v2 active",
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
private fun GuestNumberEntryRoute(
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
        parityLog = "GuestNumber parity v1 active",
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

@Composable
private fun ZipCodeEntryRoute(
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
        parityLog = "ZipCode parity v1 active",
        onConfirm = { value ->
            viewModel.sendNext(Bundle().apply { putString(responseKey, value) })
        },
        onError = { msg -> Toast(activity).show(msg, TYPE.FAILURE) }
    )
}

