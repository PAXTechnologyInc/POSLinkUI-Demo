package com.paxus.pay.poslinkui.demo.entry.compose

import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pax.us.pay.ui.constant.entry.ConfirmationEntry
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmationScreen
import com.paxus.pay.poslinkui.demo.entry.confirmation.ServiceFeeScreen
import com.paxus.pay.poslinkui.demo.entry.confirmation.ServiceFeeScreenContent
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImage
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImageOptions
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImageRequestTuning
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButtonVariant
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.AnimationLogger
import com.paxus.pay.poslinkui.demo.utils.AnimationSupport
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.QrBitmapEncoder
import com.paxus.pay.poslinkui.demo.utils.currentAnimationPolicy
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel

/**
 * Confirmation actions that need more than generic message + options.
 */
@Composable
fun ConfirmationSpecialRoute(
    action: String?,
    extras: Bundle,
    viewModel: EntryViewModel,
    genericRoute: @Composable () -> Unit
) {
    when (action) {
        ConfirmationEntry.ACTION_CONFIRM_RECEIPT_VIEW ->
            ReceiptPreviewEntryScreen(extras, viewModel)
        ConfirmationEntry.ACTION_DISPLAY_QR_CODE_RECEIPT ->
            QrCodeReceiptEntryScreen(extras, viewModel)
        ConfirmationEntry.ACTION_START_UI ->
            StartUiEntryScreen(viewModel)
        ConfirmationEntry.ACTION_CONFIRM_SURCHARGE_FEE ->
            SurchargeFeeEntryScreen(extras, viewModel)
        ConfirmationEntry.ACTION_CONFIRM_SERVICE_FEE ->
            ServiceFeeEntryScreen(extras, viewModel)
        else -> genericRoute()
    }
}

@Composable
private fun ReceiptPreviewEntryScreen(extras: Bundle, viewModel: EntryViewModel) {
    val uriStr = extras.getString(EntryExtraData.PARAM_RECEIPT_URI).orEmpty()
    val crossfadeEnabled = AnimationSupport.shouldUseReceiptPreviewCrossfade(currentAnimationPolicy)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = PosLinkDesignTokens.SpaceBetweenTextView)
    ) {
        PosLinkText(
            text = stringResource(R.string.receipt_preview_title),
            role = PosLinkTextRole.ScreenTitle
        )
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        if (uriStr.isBlank()) {
            PosLinkText(
                text = stringResource(R.string.receipt_preview_missing_uri),
                role = PosLinkTextRole.Supporting
            )
        } else {
            val uri = remember(uriStr) { Uri.parse(uriStr) }
            LaunchedEffect(uriStr, crossfadeEnabled) {
                if (!crossfadeEnabled) {
                    AnimationLogger.logDowngrade(
                        "A6",
                        "disable receipt preview crossfade",
                        currentAnimationPolicy
                    )
                }
            }
            PosLinkAsyncImage(
                data = uri,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PosLinkDesignTokens.ButtonHeight * 8),
                options = PosLinkAsyncImageOptions(
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    requestTuning = PosLinkAsyncImageRequestTuning(crossfade = crossfadeEnabled)
                )
            )
        }
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        ConfirmationScreen(
            message = stringResource(R.string.receipt_preview_confirm_prompt),
            positiveText = stringResource(R.string.confirm_option_accept),
            negativeText = stringResource(R.string.confirm_option_decline),
            onConfirm = {
                viewModel.sendNext(buildConfirmationSubmitBundle(confirmed = true))
            },
            onCancel = {
                viewModel.sendNext(buildConfirmationSubmitBundle(confirmed = false))
            }
        )
    }
}

@Composable
private fun QrCodeReceiptEntryScreen(extras: Bundle, viewModel: EntryViewModel) {
    val content = extras.getString(EntryExtraData.PARAM_QR_CODE_CONTENT).orEmpty()
    val bmp = remember(content) {
        if (content.isBlank()) null else QrBitmapEncoder.encode(content, 512)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        if (bmp != null) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )
        } else {
            PosLinkText(
                text = stringResource(R.string.qr_content_missing),
                role = PosLinkTextRole.Body
            )
        }
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm),
            onClick = { viewModel.sendNext(null) }
        )
    }
}

@Composable
private fun StartUiEntryScreen(viewModel: EntryViewModel) {
    LaunchedEffect(Unit) {
        viewModel.sendNext(null)
    }
    BoxWithCenterText(stringResource(R.string.start_ui_advancing))
}

@Composable
private fun SurchargeFeeEntryScreen(extras: Bundle, viewModel: EntryViewModel) {
    LaunchedEffect(Unit) {
        Logger.i("SurchargeFee parity v1 active")
    }
    val name = extras.getString(EntryExtraData.PARAM_SURCHARGE_FEE_NAME).orEmpty()
    val fee = if (extras.containsKey(EntryExtraData.PARAM_SURCHARGE_FEE)) {
        extras.getLong(EntryExtraData.PARAM_SURCHARGE_FEE)
    } else {
        0L
    }
    val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
    val amountLine = CurrencyUtils.convert(fee, currency)
    val feeSummary = when {
        name.isNotBlank() -> {
            val trimmedName = name.trimEnd().trimEnd(':')
            "$trimmedName $amountLine"
        }
        else -> amountLine
    }.ifBlank { stringResource(R.string.surcharge_fallback_message) }
    val enableBypass = extras.getBoolean(EntryExtraData.PARAM_ENABLE_BYPASS, true)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PosLinkDesignTokens.ScreenPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.SpaceBetweenTextView)
        ) {
            PosLinkText(
                text = feeSummary,
                role = PosLinkTextRole.Body,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            PosLinkText(
                text = stringResource(R.string.surcharge_continue_prompt),
                role = PosLinkTextRole.Body,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            PosLinkPrimaryButton(
                text = stringResource(R.string.confirm_option_yes),
                onClick = {
                    viewModel.sendNext(buildSurchargeFeeSubmitBundle(SurchargeFeeSubmitChoice.Accept))
                },
                variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
            )
            PosLinkPrimaryButton(
                text = stringResource(R.string.confirm_option_no),
                onClick = {
                    viewModel.sendNext(buildSurchargeFeeSubmitBundle(SurchargeFeeSubmitChoice.Decline))
                },
                variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
            )
            if (enableBypass) {
                PosLinkPrimaryButton(
                    text = stringResource(R.string.confirm_option_bypass_fee),
                    onClick = {
                        viewModel.sendNext(buildSurchargeFeeSubmitBundle(SurchargeFeeSubmitChoice.Bypass))
                    },
                    variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
                )
            }
        }
    }
}

@Composable
private fun ServiceFeeEntryScreen(extras: Bundle, viewModel: EntryViewModel) {
    val feeName = extras.getString(EntryExtraData.PARAM_SERVICE_FEE_NAME)
    val totalAmount = extras.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT)
    val feeAmount = extras.getLong(EntryExtraData.PARAM_SERVICE_FEE)
    val currency = extras.getString(EntryExtraData.PARAM_CURRENCY)
    val options = extras.getStringArray(EntryExtraData.PARAM_OPTIONS)
    val positive = options?.firstOrNull() ?: stringResource(R.string.confirm_option_accept)
    val saleAmt = totalAmount - feeAmount
    ServiceFeeScreen(
        content = ServiceFeeScreenContent(
            saleAmount = saleAmt,
            feeName = feeName,
            feeAmount = feeAmount,
            totalAmount = totalAmount,
            currency = currency,
            positiveText = positive
        ),
        onConfirm = {
            viewModel.sendNext(buildConfirmationSubmitBundle(confirmed = true))
        },
        onCancel = {
            viewModel.sendNext(buildConfirmationSubmitBundle(confirmed = false))
        }
    )
}
