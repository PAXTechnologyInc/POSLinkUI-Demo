@file:OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)

package com.paxus.pay.poslinkui.demo.entry.security

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.graphics.Rect
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import java.util.Locale

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
    val hasPhysicalKeyboard = remember { DeviceUtils.hasPhysicalKeyboard() }
    val isUsingExternalPinPad = extras.getBooleanCompat(
        EntryExtraData.PARAM_IS_EXTERNAL_PINPAD,
        false,
        "isExternalPinPad"
    )
    val shouldShowCustomPinPad = pinReadyOnly && !hasPhysicalKeyboard && !isUsingExternalPinPad
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
            isInputAccount = isInputAccount
        )
    ) {
        SecuritySecureAreaPrimaryBody(
            SecuritySecureAreaPrimaryBodyParams(
                pinUi = SecuritySecureAreaPinUiParams(
                    pinReadyOnly = pinReadyOnly,
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
    val keyboardController = LocalSoftwareKeyboardController.current
    if (pinStatusEnabled) {
        LaunchedEffect(entryAction) {
            keyboardController?.hide()
        }
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
    isInputAccount: Boolean
): Modifier = Modifier
    .fillMaxWidth()
    .then(if (pinReadyOnly || isInputAccount) Modifier.fillMaxHeight() else Modifier)
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

private data class LegacySecureAreaBounds(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
)

private fun LayoutCoordinates.toLegacySecureAreaBounds(activity: FragmentActivity): LegacySecureAreaBounds {
    val position = positionInWindow()
    val decorView = activity.window.decorView
    val barHeight = Rect().also { decorView.getWindowVisibleDisplayFrame(it) }.top
    return LegacySecureAreaBounds(
        x = position.x.roundToInt(),
        y = (position.y - barHeight).roundToInt(),
        width = size.width,
        height = size.height
    )
}

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
    val activity = LocalContext.current as FragmentActivity
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
                val legacyBounds = coords.toLegacySecureAreaBounds(activity)
                viewModel.sendSecurityAreaBounds(
                    legacyBounds.x,
                    legacyBounds.y,
                    legacyBounds.width,
                    legacyBounds.height,
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
    val pinTotalAmountText = if (pinReadyOnly) resolveInputAccountTotalAmount(extras) else null
    when {
        pinReadyOnly -> {
            PinMainBody(
                isLandscapeBySize = isLandscapeBySize,
                showCustomPinPad = shouldShowCustomPinPad,
                marginGap = marginGap,
                totalAmountText = pinTotalAmountText,
                pinLines = pinLines,
                pinPromptMessage = pinPromptMessage,
                showPinBypass = showPinBypass,
                pinMaskText = pinMaskText,
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
    totalAmountText: String?,
    pinLines: List<String>,
    pinPromptMessage: String,
    showPinBypass: Boolean,
    pinMaskText: String,
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
                isLandscapeBySize = isLandscapeBySize,
                totalAmountText = totalAmountText,
                pinLines = pinLines,
                fallbackMessage = pinPromptMessage,
                showPinBypass = showPinBypass,
                pinMaskText = pinMaskText,
                marginGap = marginGap
            )
            Spacer(modifier = Modifier.weight(1f))
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
                        isLandscapeBySize = isLandscapeBySize,
                        totalAmountText = totalAmountText,
                        pinLines = pinLines,
                        fallbackMessage = pinPromptMessage,
                        showPinBypass = showPinBypass,
                        pinMaskText = pinMaskText,
                        marginGap = marginGap
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
                    isLandscapeBySize = isLandscapeBySize,
                    totalAmountText = totalAmountText,
                    pinLines = pinLines,
                    fallbackMessage = pinPromptMessage,
                    showPinBypass = showPinBypass,
                    pinMaskText = pinMaskText,
                    marginGap = marginGap
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
    isLandscapeBySize: Boolean,
    totalAmountText: String?,
    pinLines: List<String>,
    fallbackMessage: String,
    showPinBypass: Boolean,
    pinMaskText: String,
    marginGap: androidx.compose.ui.unit.Dp
) {
    if (totalAmountText != null) {
        PinAmountRow(
            totalAmountText = totalAmountText,
            isLandscapeBySize = isLandscapeBySize
        )
    }
    val pinPrompt = if (pinLines.isNotEmpty()) pinLines.joinToString("\n") else fallbackMessage
    val promptTextSize = dimensionResource(R.dimen.text_size_normal).value.sp
    Text(
        text = pinPrompt,
        modifier = Modifier.fillMaxWidth(),
        color = PosLinkDesignTokens.PrimaryTextColor,
        fontSize = promptTextSize,
        lineHeight = promptTextSize * 1.3f,
        fontWeight = FontWeight.Normal
    )
    Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
    PinSecurePlaceholderField(
        pinMaskText = pinMaskText,
        marginGap = marginGap
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
    val activity = LocalContext.current as FragmentActivity
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
                    val legacyBounds = coords.toLegacySecureAreaBounds(activity)
                    viewModel.sendSecurityAreaBounds(
                        legacyBounds.x,
                        legacyBounds.y,
                        legacyBounds.width,
                        legacyBounds.height,
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
 * Legacy PIN amount row from `fragment_pin.xml`.
 */
@Composable
private fun PinAmountRow(
    totalAmountText: String,
    isLandscapeBySize: Boolean
) {
    val amountTextSize = if (isLandscapeBySize) {
        dimensionResource(R.dimen.text_size_normal).value.sp
    } else {
        dimensionResource(R.dimen.text_size_subtitle).value.sp
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.total_amount),
            color = PosLinkDesignTokens.PrimaryTextColor,
            fontSize = amountTextSize,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = totalAmountText,
            color = PosLinkDesignTokens.PrimaryTextColor,
            fontSize = amountTextSize,
            fontWeight = FontWeight.Normal
        )
    }
    Spacer(modifier = Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
}

/**
 * Visible PIN placeholder uses the legacy TextView look only; it never requests secure bounds.
 */
@Composable
private fun PinSecurePlaceholderField(
    pinMaskText: String,
    marginGap: androidx.compose.ui.unit.Dp
) {
    val fieldHeight = dimensionResource(R.dimen.button_height)
    val fieldCorner = dimensionResource(R.dimen.corner_radius)
    val fieldShape = RoundedCornerShape(fieldCorner)
    Box(
        modifier = Modifier
            .padding(horizontal = marginGap, vertical = 10.dp)
            .fillMaxWidth()
            .height(fieldHeight)
            .background(color = colorResource(R.color.pastel_on_background), shape = fieldShape)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = pinMaskText,
            modifier = Modifier.fillMaxWidth(),
            color = colorResource(R.color.pastel_text_color_on_light),
            fontSize = dimensionResource(R.dimen.text_size_subtitle).value.sp,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Compose-native legacy PIN pad that only reports key rects and never creates a second input path.
 */
@Composable
private fun PinPadKeyboard(
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel
) {
    val configuration = LocalConfiguration.current
    val keyRects = remember(configuration.orientation) { mutableStateMapOf<String, Rect>() }
    var pinLayoutSent by remember(configuration.orientation) { mutableStateOf(false) }
    val defaultGap = dimensionResource(R.dimen.default_gap)
    val lightKeyText = colorResource(R.color.pastel_text_color_on_light)
    val cancelColor = colorResource(R.color.cancel)
    val clearColor = colorResource(R.color.clear)
    val enterColor = colorResource(R.color.enter)

    LaunchedEffect(keyRects.size, pinLayoutSent) {
        if (!pinLayoutSent && keyRects.size == 13) {
            val keyBundle = Bundle().apply {
                keyRects.forEach { (key, value) -> putParcelable(key, value) }
            }
            viewModel.sendPinKeyLayout(keyBundle)
            viewModel.sendSecurityAreaPinReady()
            pinLayoutSent = true
        }
    }

    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(defaultGap)
    ) {
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(defaultGap)
        ) {
            PinDigitRow(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                keys = listOf(
                    EntryRequest.PARAM_KEY_1 to "1",
                    EntryRequest.PARAM_KEY_2 to "2",
                    EntryRequest.PARAM_KEY_3 to "3"
                ),
                gap = defaultGap,
                textColor = lightKeyText,
                onKeyRectChanged = { key, rect -> keyRects[key] = rect }
            )
            PinDigitRow(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                keys = listOf(
                    EntryRequest.PARAM_KEY_4 to "4",
                    EntryRequest.PARAM_KEY_5 to "5",
                    EntryRequest.PARAM_KEY_6 to "6"
                ),
                gap = defaultGap,
                textColor = lightKeyText,
                onKeyRectChanged = { key, rect -> keyRects[key] = rect }
            )
            PinDigitRow(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                keys = listOf(
                    EntryRequest.PARAM_KEY_7 to "7",
                    EntryRequest.PARAM_KEY_8 to "8",
                    EntryRequest.PARAM_KEY_9 to "9"
                ),
                gap = defaultGap,
                textColor = lightKeyText,
                onKeyRectChanged = { key, rect -> keyRects[key] = rect }
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(defaultGap)
            ) {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                PinPadKey(
                    requestKey = EntryRequest.PARAM_KEY_0,
                    label = "0",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    backgroundColor = Color.White,
                    textColor = lightKeyText,
                    onKeyRectChanged = { key, rect -> keyRects[key] = rect }
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(defaultGap)
        ) {
            PinPadKey(
                requestKey = EntryRequest.PARAM_KEY_CANCEL,
                label = "Cancel",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                backgroundColor = cancelColor,
                textColor = Color.White,
                onKeyRectChanged = { key, rect -> keyRects[key] = rect }
            )
            PinPadKey(
                requestKey = EntryRequest.PARAM_KEY_CLEAR,
                label = "Clear",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                backgroundColor = clearColor,
                textColor = Color.White,
                onKeyRectChanged = { key, rect -> keyRects[key] = rect }
            )
            PinPadKey(
                requestKey = EntryRequest.PARAM_KEY_ENTER,
                label = "Enter",
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                backgroundColor = enterColor,
                textColor = Color.White,
                onKeyRectChanged = { key, rect -> keyRects[key] = rect }
            )
        }
    }
}

@Composable
private fun PinDigitRow(
    modifier: Modifier = Modifier,
    keys: List<Pair<String, String>>,
    gap: androidx.compose.ui.unit.Dp,
    textColor: Color,
    onKeyRectChanged: (String, Rect) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(gap)
    ) {
        keys.forEach { (requestKey, label) ->
            PinPadKey(
                requestKey = requestKey,
                label = label,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = Color.White,
                textColor = textColor,
                onKeyRectChanged = onKeyRectChanged
            )
        }
    }
}

@Composable
private fun PinPadKey(
    requestKey: String,
    label: String,
    modifier: Modifier,
    backgroundColor: Color,
    textColor: Color,
    onKeyRectChanged: (String, Rect) -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .onGloballyPositioned { coords ->
                if (coords.size.width <= 0 || coords.size.height <= 0) return@onGloballyPositioned
                val pos = coords.positionInWindow()
                onKeyRectChanged(
                    requestKey,
                    Rect(
                        pos.x.roundToInt(),
                        pos.y.roundToInt(),
                        pos.x.roundToInt() + coords.size.width,
                        pos.y.roundToInt() + coords.size.height
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = dimensionResource(R.dimen.text_size_subtitle).value.sp,
            textAlign = TextAlign.Center
        )
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
    val activity = LocalContext.current as FragmentActivity
    val fieldHeight = dimensionResource(R.dimen.button_height)
    val fieldCorner = dimensionResource(R.dimen.corner_radius)
    val fieldShape = RoundedCornerShape(fieldCorner)
    val fieldSurface = Color(0xFFDBD4D9)
    val panTextPurple = Color(0xFF9C27B0)
    val panTextColorHex = String.format(Locale.ROOT, "%08X", panTextPurple.toArgb())
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
                val legacyBounds = coords.toLegacySecureAreaBounds(activity)
                viewModel.sendSecurityAreaBounds(
                    legacyBounds.x,
                    legacyBounds.y,
                    legacyBounds.width,
                    legacyBounds.height,
                    "Card Number",
                    panTextColorHex
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
