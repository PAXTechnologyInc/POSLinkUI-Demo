package com.paxus.pay.poslinkui.demo.entry.security

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.SecurityEntry
import com.pax.us.pay.ui.constant.status.PINStatus
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyThemeButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkSurfaceCard
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.DeviceUtils
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel
import com.paxus.pay.poslinkui.demo.entry.compose.LocalEntryInteractionLocked
import kotlin.math.roundToInt

/**
 * Demo security Entry: notifies BroadPOS via [com.pax.us.pay.ui.constant.entry.EntryRequest.ACTION_SECURITY_AREA],
 * then **Continue** sends [com.pax.us.pay.poslinkui.demo.viewmodel.EntryViewModel.sendNext] with no sensitive payload
 * (PED/host captures PAN/PIN). Do not log cardholder data.
 *
 * @param entryAction Current Entry action (e.g. [SecurityEntry.ACTION_ENTER_PIN])
 * @param message Prompt from host or fallback
 * @param viewModel Host ViewModel for broadcasts
 * @param onContinue Invoked when user confirms (typically `sendNext(null)`)
 */
@Composable
fun SecuritySecureAreaScreen(
    entryAction: String?,
    extras: Bundle,
    message: String,
    viewModel: EntryViewModel,
    onContinue: () -> Unit
) {
    val isInputAccount = entryAction == SecurityEntry.ACTION_INPUT_ACCOUNT
    val isCvvEntry = isVcodeSecurityAction(entryAction)
    val isLegacySecurityFieldEntry = isVcodeSecurityAction(entryAction) ||
        entryAction == SecurityEntry.ACTION_ENTER_CARD_ALL_DIGITS ||
        entryAction == SecurityEntry.ACTION_ENTER_CARD_LAST_4_DIGITS
    val pinReadyOnly = entryAction == SecurityEntry.ACTION_ENTER_PIN
    val isCreditSalePinMain = pinReadyOnly && isCreditSaleTransType(extras)
    val hasPhysicalKeyboard = remember { DeviceUtils.hasPhysicalKeyboard() }
    val isUsingExternalPinPad = extras.getBooleanCompat(
        EntryExtraData.PARAM_IS_EXTERNAL_PINPAD,
        false,
        "isExternalPinPad"
    )
    val shouldShowCustomPinPad = pinReadyOnly && isCreditSalePinMain && !hasPhysicalKeyboard && !isUsingExternalPinPad
    val showPinBypass = remember(extras, pinReadyOnly) {
        pinReadyOnly && canBypassPin(extras)
    }
    var pinMaskText by remember(entryAction) { mutableStateOf("") }
    val pinLines = remember(message, pinReadyOnly) {
        if (!pinReadyOnly) emptyList()
        else {
            message
                .split('\n')
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }
    }

    SecuritySecureAreaEntrySideEffects(
        entryAction = entryAction,
        isCvvEntry = isCvvEntry,
        pinStatusEnabled = pinReadyOnly,
        onPinAppending = { pinMaskText += "*" },
        onPinCleared = { pinMaskText = "" }
    )

    var boundsSent by remember(entryAction) { mutableStateOf(false) }

    Column(
        modifier = securitySecureAreaRootColumnModifier(
            pinReadyOnly = pinReadyOnly,
            isInputAccount = isInputAccount,
            isCreditSalePinMain = isCreditSalePinMain
        )
    ) {
        SecuritySecureAreaPrimaryBody(
            SecuritySecureAreaPrimaryBodyParams(
                pinUi = SecuritySecureAreaPinUiParams(
                    pinReadyOnly = pinReadyOnly,
                    isCreditSalePinMain = isCreditSalePinMain,
                    shouldShowCustomPinPad = shouldShowCustomPinPad,
                    isLegacySecurityFieldEntry = isLegacySecurityFieldEntry,
                    isInputAccount = isInputAccount,
                    pinLines = pinLines,
                    message = message,
                    showPinBypass = showPinBypass,
                    pinMaskText = pinMaskText
                ),
                flow = SecuritySecureAreaFlowParams(
                    boundsSent = boundsSent,
                    onBoundsSent = { boundsSent = true },
                    viewModel = viewModel,
                    extras = extras,
                    onContinue = onContinue
                )
            )
        )

        SecuritySecureAreaPedPlaceholderSection(
            pinReadyOnly = pinReadyOnly,
            isInputAccount = isInputAccount,
            isLegacySecurityFieldEntry = isLegacySecurityFieldEntry,
            boundsSent = boundsSent,
            onBoundsSent = { boundsSent = true },
            viewModel = viewModel,
            onContinue = onContinue
        )
    }
}

@Composable
private fun SecuritySecureAreaEntrySideEffects(
    entryAction: String?,
    isCvvEntry: Boolean,
    pinStatusEnabled: Boolean,
    onPinAppending: () -> Unit,
    onPinCleared: () -> Unit
) {
    val context = LocalContext.current
    if (pinStatusEnabled) {
        DisposableEffect(entryAction, context) {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    when (intent?.action) {
                        PINStatus.PIN_ENTERING -> onPinAppending()
                        PINStatus.PIN_ENTER_CLEARED -> onPinCleared()
                    }
                }
            }
            val filter = IntentFilter().apply {
                addCategory(PINStatus.CATEGORY)
                addAction(PINStatus.PIN_ENTERING)
                addAction(PINStatus.PIN_ENTER_CLEARED)
                addAction(PINStatus.PIN_ENTER_ABORTED)
                addAction(PINStatus.PIN_ENTER_COMPLETED)
            }
            context.registerReceiver(receiver, filter)
            onDispose { context.unregisterReceiver(receiver) }
        }
    }
    if (isCvvEntry) {
        LaunchedEffect(entryAction) {
            Logger.i("CvvSecurity parity v1 active")
        }
    }
}

private fun securitySecureAreaRootColumnModifier(
    pinReadyOnly: Boolean,
    isInputAccount: Boolean,
    isCreditSalePinMain: Boolean
): Modifier = Modifier
    .fillMaxWidth()
    .then(if (isCreditSalePinMain || isInputAccount) Modifier.fillMaxHeight() else Modifier)
    .padding(
        top = if (pinReadyOnly) {
            PosLinkDesignTokens.SpaceBetweenTextView
        } else if (isInputAccount) {
            0.dp
        } else {
            PosLinkDesignTokens.SpaceBetweenTextView
        },
        // `fragment_input_account.xml` uses parent `default_gap` only; extra 12dp here pushes the
        // contactless logo row too far above the bottom edge.
        bottom = if (pinReadyOnly) {
            0.dp
        } else if (isInputAccount) {
            0.dp
        } else {
            PosLinkDesignTokens.SpaceBetweenTextView
        }
    )

@Composable
private fun SecuritySecureAreaPedPlaceholderSection(
    pinReadyOnly: Boolean,
    isInputAccount: Boolean,
    isLegacySecurityFieldEntry: Boolean,
    boundsSent: Boolean,
    onBoundsSent: () -> Unit,
    viewModel: EntryViewModel,
    onContinue: () -> Unit
) {
    val continueEnabled = !LocalEntryInteractionLocked.current
    if (pinReadyOnly || isInputAccount || isLegacySecurityFieldEntry) return
    OutlinedTextField(
        value = "",
        onValueChange = {},
        readOnly = true,
        enabled = false,
        label = {
            PosLinkText(
                text = stringResource(R.string.security_input_placeholder_label),
                role = PosLinkTextRole.Supporting
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(PosLinkDesignTokens.inputHeight())
            .onGloballyPositioned { coords ->
                if (boundsSent || coords.size.width <= 0) return@onGloballyPositioned
                onBoundsSent()
                val pos = coords.positionInWindow()
                viewModel.sendSecurityAreaBounds(
                    pos.x.roundToInt(),
                    pos.y.roundToInt(),
                    coords.size.width,
                    coords.size.height,
                    null,
                    null
                )
            }
    )
    PosLinkPrimaryButton(
        text = stringResource(R.string.security_continue_after_ped),
        onClick = onContinue,
        enabled = continueEnabled
    )
}

/** PIN / input-account presentation fields for [SecuritySecureAreaPrimaryBody]. */
private data class SecuritySecureAreaPinUiParams(
    val pinReadyOnly: Boolean,
    val isCreditSalePinMain: Boolean,
    val shouldShowCustomPinPad: Boolean,
    val isLegacySecurityFieldEntry: Boolean,
    val isInputAccount: Boolean,
    val pinLines: List<String>,
    val message: String,
    val showPinBypass: Boolean,
    val pinMaskText: String
)

/** Bounds reporting, navigation, and ViewModel hooks for [SecuritySecureAreaPrimaryBody]. */
private data class SecuritySecureAreaFlowParams(
    val boundsSent: Boolean,
    val onBoundsSent: () -> Unit,
    val viewModel: EntryViewModel,
    val extras: Bundle,
    val onContinue: () -> Unit
)

/**
 * Bundles arguments for [SecuritySecureAreaPrimaryBody] to satisfy parameter-count rules without changing behavior.
 */
private data class SecuritySecureAreaPrimaryBodyParams(
    val pinUi: SecuritySecureAreaPinUiParams,
    val flow: SecuritySecureAreaFlowParams
)

/**
 * PIN / input-account / generic secure-area layouts (extracted to keep [SecuritySecureAreaScreen] below cognitive limits).
 */
@Composable
private fun SecuritySecureAreaPrimaryBody(params: SecuritySecureAreaPrimaryBodyParams) {
    val pinReadyOnly = params.pinUi.pinReadyOnly
    val isCreditSalePinMain = params.pinUi.isCreditSalePinMain
    val shouldShowCustomPinPad = params.pinUi.shouldShowCustomPinPad
    val isLegacySecurityFieldEntry = params.pinUi.isLegacySecurityFieldEntry
    val isInputAccount = params.pinUi.isInputAccount
    val pinLines = params.pinUi.pinLines
    val message = params.pinUi.message
    val showPinBypass = params.pinUi.showPinBypass
    val pinMaskText = params.pinUi.pinMaskText
    val boundsSent = params.flow.boundsSent
    val onBoundsSent = params.flow.onBoundsSent
    val viewModel = params.flow.viewModel
    val extras = params.flow.extras
    val onContinue = params.flow.onContinue
    val resources = LocalContext.current.resources
    val configuration = LocalConfiguration.current
    val isLandscapeBySize = configuration.screenWidthDp > configuration.screenHeightDp
    val marginGap = dimensionResource(R.dimen.margin_gap)
    val pinPromptMessage = if (pinReadyOnly) {
        SecurityMessageFormatter.prompt(SecurityEntry.ACTION_ENTER_PIN, extras, resources)
    } else {
        message
    }
    when {
        pinReadyOnly && isCreditSalePinMain -> {
            PinMainBody(
                isLandscapeBySize = isLandscapeBySize,
                showCustomPinPad = shouldShowCustomPinPad,
                marginGap = marginGap,
                pinLines = pinLines,
                pinPromptMessage = pinPromptMessage,
                showPinBypass = showPinBypass,
                pinMaskText = pinMaskText,
                boundsSent = boundsSent,
                onBoundsSent = onBoundsSent,
                viewModel = viewModel
            )
        }
        pinReadyOnly -> {
            PinPromptAndInput(
                pinLines = pinLines,
                fallbackMessage = pinPromptMessage,
                showPinBypass = showPinBypass,
                pinMaskText = pinMaskText,
                boundsSent = boundsSent,
                onBoundsSent = onBoundsSent,
                viewModel = viewModel
            )
        }
        isLegacySecurityFieldEntry -> {
            LegacySecurityFieldBody(
                message = message,
                boundsSent = boundsSent,
                onBoundsSent = onBoundsSent,
                viewModel = viewModel,
                onContinue = onContinue
            )
        }
        isInputAccount -> {
            InputAccountSecurityBody(
                message = message,
                extras = extras,
                boundsSent = boundsSent,
                onBoundsSent = onBoundsSent,
                viewModel = viewModel,
                onContinue = onContinue
            )
        }
        else -> {
            PosLinkText(text = message, role = PosLinkTextRole.ScreenTitle)
            Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
            PosLinkSurfaceCard {
                PosLinkText(
                    text = stringResource(R.string.security_secure_area_hint),
                    role = PosLinkTextRole.Supporting
                )
            }
            Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        }
    }
}

@Composable
private fun PinMainBody(
    isLandscapeBySize: Boolean,
    showCustomPinPad: Boolean,
    marginGap: androidx.compose.ui.unit.Dp,
    pinLines: List<String>,
    pinPromptMessage: String,
    showPinBypass: Boolean,
    pinMaskText: String,
    boundsSent: Boolean,
    onBoundsSent: () -> Unit,
    viewModel: EntryViewModel
) {
    if (!showCustomPinPad) {
        LaunchedEffect(Unit) { viewModel.sendSecurityAreaPinReady() }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(marginGap)
        ) {
            PinPromptAndInput(
                pinLines = pinLines,
                fallbackMessage = pinPromptMessage,
                showPinBypass = showPinBypass,
                pinMaskText = pinMaskText,
                boundsSent = boundsSent,
                onBoundsSent = onBoundsSent,
                viewModel = viewModel
            )
        }
        return
    }
    if (isLandscapeBySize) {
        // Match golive layout-land/fragment_pin.xml: right pinpad width = 50%.
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(marginGap)
        ) {
            val availableWidth = maxWidth
            val keyboardWidth = maxWidth * 0.5f
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .width(availableWidth - keyboardWidth)
                        .fillMaxHeight()
                ) {
                    PinPromptAndInput(
                        pinLines = pinLines,
                        fallbackMessage = pinPromptMessage,
                        showPinBypass = showPinBypass,
                        pinMaskText = pinMaskText,
                        boundsSent = boundsSent,
                        onBoundsSent = onBoundsSent,
                        viewModel = viewModel
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Box(
                    modifier = Modifier
                        .width(keyboardWidth)
                        .fillMaxHeight()
                        .padding(marginGap)
                ) {
                    PinPadKeyboard(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        viewModel = viewModel
                    )
                }
            }
        }
    } else {
        // Match golive layout/fragment_pin.xml: bottom pinpad height = 50%.
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(marginGap)
        ) {
            val keyboardHeight = maxHeight * 0.5f
            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                PinPromptAndInput(
                    pinLines = pinLines,
                    fallbackMessage = pinPromptMessage,
                    showPinBypass = showPinBypass,
                    pinMaskText = pinMaskText,
                    boundsSent = boundsSent,
                    onBoundsSent = onBoundsSent,
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(keyboardHeight)
                        .padding(marginGap)
                ) {
                    PinPadKeyboard(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun PinPromptAndInput(
    pinLines: List<String>,
    fallbackMessage: String,
    showPinBypass: Boolean,
    pinMaskText: String,
    boundsSent: Boolean,
    onBoundsSent: () -> Unit,
    viewModel: EntryViewModel
) {
    val pinPrompt = fallbackMessage.ifBlank { pinLines.joinToString("\n") }
    Text(
        text = pinPrompt,
        modifier = Modifier.fillMaxWidth(),
        color = PosLinkDesignTokens.PrimaryTextColor,
        fontSize = dimensionResource(R.dimen.text_size_normal).value.sp
    )
    Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
    PinSecurePlaceholderField(
        pinMaskText = pinMaskText,
        boundsSent = boundsSent,
        onBoundsSent = onBoundsSent,
        viewModel = viewModel
    )
    if (showPinBypass) {
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        Text(
            text = stringResource(R.string.prompt_no_pin),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = PosLinkDesignTokens.PrimaryTextColor
        )
    }
}

@Composable
private fun LegacySecurityFieldBody(
    message: String,
    boundsSent: Boolean,
    onBoundsSent: () -> Unit,
    viewModel: EntryViewModel,
    onContinue: () -> Unit
) {
    val continueEnabled = !LocalEntryInteractionLocked.current
    val fieldHeight = dimensionResource(R.dimen.button_height)
    val fieldGap = dimensionResource(R.dimen.margin_gap)
    val fieldCorner = dimensionResource(R.dimen.corner_radius)
    val fieldInnerPadding = dimensionResource(R.dimen.padding_vertical)
    val fieldShape = RoundedCornerShape(fieldCorner)
    Column(modifier = Modifier.fillMaxWidth()) {
        PosLinkText(text = message, role = PosLinkTextRole.ScreenTitle)
        Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        BasicTextField(
            value = "",
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = PosLinkDesignTokens.SectionTitleTextSize,
                color = Color(0xFF9C27B0),
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .padding(fieldGap)
                .fillMaxWidth()
                .height(fieldHeight)
                .onGloballyPositioned { coords ->
                    if (boundsSent || coords.size.width <= 0) return@onGloballyPositioned
                    onBoundsSent()
                    val pos = coords.positionInWindow()
                    viewModel.sendSecurityAreaBounds(
                        pos.x.roundToInt(),
                        pos.y.roundToInt(),
                        coords.size.width,
                        coords.size.height,
                        null,
                        null
                    )
                },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(fieldHeight)
                        .background(color = Color(0xFFDBD4D9), shape = fieldShape)
                        .padding(fieldInnerPadding),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            }
        )
        PosLinkLegacyThemeButton(
            text = stringResource(R.string.confirm),
            onClick = onContinue,
            enabled = continueEnabled
        )
    }
}

/**
 * Visible PIN placeholder (light gray rounded bar) so the secure area bounds match the prototype;
 * the PED draws the real PIN, not this composable.
 */
@Composable
private fun PinSecurePlaceholderField(
    pinMaskText: String,
    boundsSent: Boolean,
    onBoundsSent: () -> Unit,
    viewModel: EntryViewModel
) {
    val fieldHeight = dimensionResource(R.dimen.button_height)
    val fieldCorner = dimensionResource(R.dimen.corner_radius)
    val fieldShape = RoundedCornerShape(fieldCorner)
    val fieldSurface = Color(0xFFDBD4D9)
    BasicTextField(
        value = pinMaskText,
        onValueChange = { },
        readOnly = true,
        singleLine = true,
        textStyle = TextStyle(
            fontSize = dimensionResource(R.dimen.text_size_subtitle).value.sp,
            color = colorResource(R.color.pastel_text_color_on_light),
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(fieldHeight)
            .onGloballyPositioned { coords ->
                if (boundsSent || coords.size.width <= 0) return@onGloballyPositioned
                onBoundsSent()
                val pos = coords.positionInWindow()
                viewModel.sendSecurityAreaBounds(
                    pos.x.roundToInt(),
                    pos.y.roundToInt(),
                    coords.size.width,
                    coords.size.height,
                    null,
                    null
                )
            },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(fieldHeight)
                    .background(color = fieldSurface, shape = fieldShape)
                        .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )
}

/**
 * Reuses legacy XML keyboard to keep key size/margins/font parity with golive.
 */
@Composable
private fun PinPadKeyboard(
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel
) {
    var pinLayoutSent by remember { mutableStateOf(false) }
    AndroidView(
        modifier = modifier.fillMaxWidth().fillMaxHeight(),
        factory = { context ->
            LayoutInflater.from(context).inflate(R.layout.fragment_pin_custom_keyboard, null, false)
        },
        update = { root ->
            if (pinLayoutSent) return@AndroidView
            root.post {
                if (pinLayoutSent) return@post
                val keyBundle = buildPinKeyLayoutBundle(root)
                if (keyBundle.size() == 13) {
                    viewModel.sendPinKeyLayout(keyBundle)
                    viewModel.sendSecurityAreaPinReady()
                    pinLayoutSent = true
                }
            }
        }
    )
}

private fun buildPinKeyLayoutBundle(root: View): Bundle {
    val keyMap = listOf(
        EntryRequest.PARAM_KEY_0 to R.id.key_0,
        EntryRequest.PARAM_KEY_1 to R.id.key_1,
        EntryRequest.PARAM_KEY_2 to R.id.key_2,
        EntryRequest.PARAM_KEY_3 to R.id.key_3,
        EntryRequest.PARAM_KEY_4 to R.id.key_4,
        EntryRequest.PARAM_KEY_5 to R.id.key_5,
        EntryRequest.PARAM_KEY_6 to R.id.key_6,
        EntryRequest.PARAM_KEY_7 to R.id.key_7,
        EntryRequest.PARAM_KEY_8 to R.id.key_8,
        EntryRequest.PARAM_KEY_9 to R.id.key_9,
        EntryRequest.PARAM_KEY_CLEAR to R.id.key_clear,
        EntryRequest.PARAM_KEY_ENTER to R.id.key_enter,
        EntryRequest.PARAM_KEY_CANCEL to R.id.key_cancel
    )
    return Bundle().apply {
        keyMap.forEach { (key, id) ->
            val keyView = root.findViewById<View>(id) ?: return@forEach
            val location = IntArray(2)
            keyView.getLocationOnScreen(location)
            putParcelable(
                key,
                Rect(
                    location[0],
                    location[1],
                    location[0] + keyView.width,
                    location[1] + keyView.height
                )
            )
        }
    }
}

/** Matches `golive/v1.03.00` `fragment_input_account` `edit_account`: `padding_vertical` (15dp) on all sides. */
private val InputAccountPanFieldPadding = 15.dp

/**
 * Read-only PAN placeholder with legacy [rounded_corner_on_background] look and exact inner padding for PED bounds.
 */
@Composable
private fun InputAccountPanSecureField(
    boundsSent: Boolean,
    onBoundsSent: () -> Unit,
    viewModel: EntryViewModel
) {
    val fieldHeight = dimensionResource(R.dimen.button_height)
    val fieldCorner = dimensionResource(R.dimen.corner_radius)
    val fieldShape = RoundedCornerShape(fieldCorner)
    val fieldSurface = Color(0xFFDBD4D9)
    val panTextPurple = Color(0xFF9C27B0)
    BasicTextField(
        value = "",
        onValueChange = {},
        readOnly = true,
        singleLine = true,
        textStyle = TextStyle(
            fontSize = PosLinkDesignTokens.SectionTitleTextSize,
            color = panTextPurple,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(fieldHeight)
            .onGloballyPositioned { coords ->
                if (boundsSent || coords.size.width <= 0) return@onGloballyPositioned
                onBoundsSent()
                val pos = coords.positionInWindow()
                viewModel.sendSecurityAreaBounds(
                    pos.x.roundToInt(),
                    pos.y.roundToInt(),
                    coords.size.width,
                    coords.size.height,
                    "Card Number",
                    null
                )
            },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(fieldHeight)
                    .border(width = 2.dp, color = fieldSurface, shape = fieldShape)
                    .background(color = fieldSurface, shape = fieldShape)
                    .padding(InputAccountPanFieldPadding),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )
}

/**
 * Returns true when at least one contactless brand flag (NFC or wallet) is enabled.
 */
private data class InputModeCreditSaleFallbackFlags(
    val insert: Boolean,
    val tap: Boolean,
    val swipe: Boolean
)

private fun inputModesWithCreditSaleNoNfcFallback(
    enableInsert: Boolean,
    enableTap: Boolean,
    enableSwipe: Boolean,
    applyCreditSaleMainLayout: Boolean,
    supportNfc: Boolean
): InputModeCreditSaleFallbackFlags {
    val forceEnabledIcons = applyCreditSaleMainLayout && !supportNfc
    return InputModeCreditSaleFallbackFlags(
        insert = enableInsert || forceEnabledIcons,
        tap = enableTap || forceEnabledIcons,
        swipe = enableSwipe || forceEnabledIcons
    )
}

private fun hasAnyContactlessWalletBrand(
    supportNfc: Boolean,
    supportApplePay: Boolean,
    supportGooglePay: Boolean,
    supportSamsungPay: Boolean
): Boolean = listOf(supportNfc, supportApplePay, supportGooglePay, supportSamsungPay).any { it }

@Composable
private fun InputAccountSecurityBody(
    message: String,
    extras: Bundle,
    boundsSent: Boolean,
    onBoundsSent: () -> Unit,
    viewModel: EntryViewModel,
    onContinue: () -> Unit
) {
    val activity = LocalContext.current as FragmentActivity
    val secondVm = ViewModelProvider(activity)[SecondScreenInfoViewModel::class.java]
    val enableInsert = extras.getBooleanCompat(EntryExtraData.PARAM_ENABLE_INSERT, false, "enableInsert")
    val enableTap = extras.getBooleanCompat(EntryExtraData.PARAM_ENABLE_TAP, false, "enableTap")
    val enableSwipe = extras.getBooleanCompat(EntryExtraData.PARAM_ENABLE_SWIPE, false, "enableSwipe")
    val enableManual = extras.getBooleanCompat(EntryExtraData.PARAM_ENABLE_MANUAL, true, "enableManual")
    val supportNfc = extras.getBooleanCompat(
        EntryExtraData.PARAM_ENABLE_NFCPAY,
        false,
        "enableNFCpay",
        "enableNfcpay",
        "enableNfcPay"
    )
    val supportApplePay = extras.getBooleanCompat(EntryExtraData.PARAM_ENABLE_APPLEPAY, false, "enableApplePay")
    val supportGooglePay = extras.getBooleanCompat(EntryExtraData.PARAM_ENABLE_GOOGLEPAY, false, "enableGooglePay")
    val supportSamsungPay = extras.getBooleanCompat(
        EntryExtraData.PARAM_ENABLE_SAMSUNGPAY,
        false,
        "enableSamsungPay"
    )
    val isCreditSaleMainCase = isInputAccountCreditSaleMainCase(
        extras = extras,
        enableInsert = enableInsert,
        enableTap = enableTap,
        enableSwipe = enableSwipe,
        enableManual = enableManual
    )
    val totalAmountText = resolveInputAccountTotalAmount(extras)
    val isLikelyCreditSalePrompt = isLikelyInputAccountCreditSalePrompt(
        message = message,
        totalAmountText = totalAmountText,
        enableInsert = enableInsert,
        enableTap = enableTap,
        enableSwipe = enableSwipe,
        enableManual = enableManual
    )
    val applyCreditSaleMainLayout = isCreditSaleMainCase || isLikelyCreditSalePrompt

    LaunchedEffect(totalAmountText, enableTap) {
        secondVm.updateAllData(
            totalAmountText ?: "",
            "",
            "",
            if (enableTap) R.mipmap.tap else null,
            "",
            ""
        )
    }

    LaunchedEffect(enableTap, supportNfc) {
        when {
            enableTap && supportNfc -> Logger.i("InputAccountNfc parity v3 active")
            enableTap && !supportNfc -> Logger.i("InputAccountNoNfc parity v3 active")
            else -> Unit
        }
    }

    InputAccountSecurityMainColumn(
        InputAccountSecurityMainColumnParams(
            display = InputAccountSecurityMainColumnDisplay(
                totalAmountText = totalAmountText,
                message = message,
                enableManual = enableManual,
                applyCreditSaleMainLayout = applyCreditSaleMainLayout
            ),
            bounds = InputAccountSecurityMainColumnBounds(
                boundsSent = boundsSent,
                onBoundsSent = onBoundsSent,
                viewModel = viewModel,
                onContinue = onContinue
            ),
            modes = InputAccountSecurityPaymentModeFlags(
                enableInsert = enableInsert,
                enableTap = enableTap,
                enableSwipe = enableSwipe,
                supportNfc = supportNfc,
                supportApplePay = supportApplePay,
                supportGooglePay = supportGooglePay,
                supportSamsungPay = supportSamsungPay
            )
        )
    )
}

private data class InputAccountSecurityMainColumnDisplay(
    val totalAmountText: String?,
    val message: String,
    val enableManual: Boolean,
    val applyCreditSaleMainLayout: Boolean
)

private data class InputAccountSecurityMainColumnBounds(
    val boundsSent: Boolean,
    val onBoundsSent: () -> Unit,
    val viewModel: EntryViewModel,
    val onContinue: () -> Unit
)

private data class InputAccountSecurityPaymentModeFlags(
    val enableInsert: Boolean,
    val enableTap: Boolean,
    val enableSwipe: Boolean,
    val supportNfc: Boolean,
    val supportApplePay: Boolean,
    val supportGooglePay: Boolean,
    val supportSamsungPay: Boolean
)

/**
 * Bundles arguments for [InputAccountSecurityMainColumn] to satisfy parameter-count rules.
 */
private data class InputAccountSecurityMainColumnParams(
    val display: InputAccountSecurityMainColumnDisplay,
    val bounds: InputAccountSecurityMainColumnBounds,
    val modes: InputAccountSecurityPaymentModeFlags
)

/**
 * Main column for input-account secure entry: amount row, message, PAN field, confirm, mode icons, logos.
 */
@Composable
private fun InputAccountSecurityMainColumn(p: InputAccountSecurityMainColumnParams) {
    val continueEnabled = !LocalEntryInteractionLocked.current
    val totalAmountText = p.display.totalAmountText
    val message = p.display.message
    val enableManual = p.display.enableManual
    val applyCreditSaleMainLayout = p.display.applyCreditSaleMainLayout
    val boundsSent = p.bounds.boundsSent
    val onBoundsSent = p.bounds.onBoundsSent
    val viewModel = p.bounds.viewModel
    val onContinue = p.bounds.onContinue
    val enableInsert = p.modes.enableInsert
    val enableTap = p.modes.enableTap
    val enableSwipe = p.modes.enableSwipe
    val supportNfc = p.modes.supportNfc
    val supportApplePay = p.modes.supportApplePay
    val supportGooglePay = p.modes.supportGooglePay
    val supportSamsungPay = p.modes.supportSamsungPay
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(PosLinkDesignTokens.CompactSpacing)
    ) {
        if (totalAmountText != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.detail_item_total_amount).removeSuffix(":"),
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    fontSize = PosLinkDesignTokens.SectionTitleTextSize,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = totalAmountText,
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    fontSize = PosLinkDesignTokens.SectionTitleTextSize,
                    fontWeight = FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.height(PosLinkDesignTokens.InlineSpacing))
        }
        Text(
            text = message,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                color = PosLinkDesignTokens.PrimaryTextColor,
                fontSize = PosLinkDesignTokens.TitleTextSize,
                fontWeight = FontWeight.Normal,
                lineHeight = PosLinkDesignTokens.TitleTextSize
            )
        )
        if (enableManual) {
            InputAccountPanSecureField(
                boundsSent = boundsSent,
                onBoundsSent = onBoundsSent,
                viewModel = viewModel
            )
            if (applyCreditSaleMainLayout) {
                InputAccountConfirmButton(onClick = onContinue, enabled = continueEnabled)
            } else {
                PosLinkLegacyThemeButton(
                    text = stringResource(R.string.confirm),
                    onClick = onContinue,
                    enabled = false
                )
            }
        }
        // golive fragment_input_account: entry_mode_view marginTop/Bottom 8dp; ImageViews gravity=bottom in flex area.
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                // Match `fragment_input_account` `entry_mode_view` top/bottom 8dp margins.
                .padding(
                    top = PosLinkDesignTokens.ControlGutter,
                    bottom = PosLinkDesignTokens.ControlGutter
                )
        ) {
            val modes = inputModesWithCreditSaleNoNfcFallback(
                enableInsert = enableInsert,
                enableTap = enableTap,
                enableSwipe = enableSwipe,
                applyCreditSaleMainLayout = applyCreditSaleMainLayout,
                supportNfc = supportNfc
            )
            InputModeImageRow(
                enableInsert = modes.insert,
                enableTap = modes.tap,
                enableSwipe = modes.swipe,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }
        val hasAnyContactlessBrand = hasAnyContactlessWalletBrand(
            supportNfc = supportNfc,
            supportApplePay = supportApplePay,
            supportGooglePay = supportGooglePay,
            supportSamsungPay = supportSamsungPay
        )
        if (enableTap && hasAnyContactlessBrand) {
            ContactlessLogoRow(
                showNfc = supportNfc,
                showApplePay = supportApplePay,
                showGooglePay = supportGooglePay,
                showSamsungPay = supportSamsungPay,
                modifier = Modifier.padding(
                    top = PosLinkDesignTokens.ControlGutter,
                    bottom = PosLinkDesignTokens.ControlGutter
                )
            )
        }
    }
}
