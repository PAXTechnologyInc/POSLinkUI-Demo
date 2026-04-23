package com.paxus.pay.poslinkui.demo.entry.security

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyThemeButton
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel
import com.paxus.pay.poslinkui.demo.entry.compose.LocalEntryInteractionLocked

private val InputAccountPanFieldPadding = 15.dp

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
    val panTextColorHex = String.format(java.util.Locale.ROOT, "%08X", panTextPurple.toArgb())
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
internal fun InputAccountSecurityBody(
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

private data class InputAccountSecurityMainColumnParams(
    val display: InputAccountSecurityMainColumnDisplay,
    val bounds: InputAccountSecurityMainColumnBounds,
    val modes: InputAccountSecurityPaymentModeFlags
)

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
            androidx.compose.foundation.layout.Row(
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
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
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
