package com.paxus.pay.poslinkui.demo.entry.signature

import android.graphics.Rect
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButtonVariant
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import kotlinx.coroutines.delay

/**
 * Signature screen parity for `GET_SIGNATURE` (`fragment_signature.xml` + [SignatureFragment] in golive/v1.03).
 *
 * [SignatureFragment] never applies extra margins in code; spacing comes from the layout. The XML always
 * includes **two** `TextView`s under `sign_line_layout`. When `PARAM_SIGNLINE1/2` are absent, the Fragment
 * does not call `setText`, but the views still exist 鈥?on device they still occupy line box height. In
 * Compose, **always** render two line slots with [minLines] so empty lines do not collapse (Untitled 8 /
 * `CREDIT_SALE鈥斺€攕ignature.png` adb case). Typography uses configuration-qualified [R.dimen.text_size_normal]
 * like `fragment_signature.xml` default TextView sizing.
 */
data class SignatureDemoScreenProps(
    val signLine1: String,
    val signLine2: String,
    val timeoutMs: Long,
    val totalAmount: Long,
    val currency: String?,
    val enableCancel: Boolean,
    val controlsEnabled: Boolean = true
)

private class SignatureViewHolder {
    var view: ElectronicSignatureView? = null
}

@Composable
fun SignatureDemoScreen(
    props: SignatureDemoScreenProps,
    onHostTimeoutReset: () -> Unit = {},
    onCancel: () -> Unit,
    onSubmit: (ShortArray) -> Unit
) {
    val safeTimeoutMs = props.timeoutMs.coerceAtLeast(0L)
    var timeoutResetVersion by remember { mutableStateOf(0L) }
    var remainingMs by remember(safeTimeoutMs, timeoutResetVersion) { mutableStateOf(safeTimeoutMs) }
    val signatureRef = remember { SignatureViewHolder() }
    var isConfirmInFlight by remember { mutableStateOf(false) }

    LaunchedEffect(safeTimeoutMs, timeoutResetVersion) {
        var current = safeTimeoutMs
        remainingMs = current
        while (current > 0L) {
            delay(1000L)
            current -= 1000L
            remainingMs = current.coerceAtLeast(0L)
        }
    }

    fun submitIfTouched() {
        if (!props.controlsEnabled || isConfirmInFlight) return
        val signatureView = signatureRef.view ?: return
        if (!signatureView.getTouched()) return
        isConfirmInFlight = true
        try {
            onSubmit(buildSignaturePayload(signatureView.getPathPos()))
        } finally {
            isConfirmInFlight = false
        }
    }

    fun clearAndResetTimeout() {
        if (!props.controlsEnabled) return
        signatureRef.view?.clear()
        timeoutResetVersion += 1L
        onHostTimeoutReset()
    }

    val timeoutSeconds = (remainingMs / 1000L).toString()
    val density = LocalDensity.current
    val res = LocalContext.current.resources
    val dm = res.displayMetrics
    val bodyTextSize = (res.getDimension(R.dimen.text_size_normal) / dm.scaledDensity).sp
    val bodyLineHeight = (res.getDimension(R.dimen.text_size_normal) / dm.scaledDensity * 1.4f).sp
    val subtitleTextSize = (res.getDimension(R.dimen.text_size_subtitle) / dm.scaledDensity).sp
    val subtitleLineHeight = (res.getDimension(R.dimen.text_size_subtitle) / dm.scaledDensity * 1.4f).sp
    val signatureBoardHeightPx = (250f * density.density).toInt()
    val bottomBarReserved =
        PosLinkDesignTokens.buttonHeight() + PosLinkDesignTokens.InlineSpacing * 2 + 5.dp

    Box(
        Modifier
            .fillMaxSize()
            .background(PosLinkDesignTokens.BackgroundColor)
            .padding(PosLinkDesignTokens.CompactSpacing)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = bottomBarReserved)
        ) {
            if (remainingMs > 0L) {
                Text(
                    text = timeoutSeconds,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = subtitleTextSize,
                    fontWeight = FontWeight.Normal,
                    lineHeight = subtitleLineHeight,
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.total_amount),
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    fontSize = bodyTextSize,
                    lineHeight = bodyLineHeight
                )
                Text(
                    text = CurrencyUtils.convert(props.totalAmount, props.currency ?: CurrencyType.USD),
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    fontSize = bodyTextSize,
                    lineHeight = bodyLineHeight
                )
            }
            // Match fragment_signature: two TextViews always in sign_line_layout (SignatureFragment only setText when non-empty).
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = props.signLine1,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    fontSize = bodyTextSize,
                    lineHeight = bodyLineHeight,
                    minLines = 1
                )
                Text(
                    text = props.signLine2,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    fontSize = bodyTextSize,
                    lineHeight = bodyLineHeight,
                    minLines = 1
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                Surface(
                    color = Color.White,
                    shadowElevation = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    AndroidView(
                        factory = { context ->
                            createSignatureView(
                                context = context,
                                signatureBoardHeightPx = signatureBoardHeightPx,
                                controlsEnabled = props.controlsEnabled,
                                onSubmit = ::submitIfTouched,
                                onClear = ::clearAndResetTimeout,
                                onCancel = onCancel
                            ).also { signatureRef.view = it }
                        },
                        update = { view ->
                            signatureRef.view = view
                            view.isEnabled = props.controlsEnabled
                            updateSignatureViewLayout(view, signatureBoardHeightPx)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(PosLinkDesignTokens.BackgroundColor)
                .padding(bottom = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            if (props.enableCancel) {
                SignatureActionButton(
                    text = stringResource(R.string.cancel_sign),
                    background = Color(0xFFFF7878),
                    onClick = onCancel,
                    enabled = props.controlsEnabled,
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                )
            }
            SignatureActionButton(
                text = stringResource(R.string.clear_sign),
                background = Color(0xFF89AA97),
                onClick = { clearAndResetTimeout() },
                enabled = props.controlsEnabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            )
            SignatureActionButton(
                text = stringResource(R.string.confirm),
                background = Color(0xFF6E85B7),
                onClick = { submitIfTouched() },
                enabled = props.controlsEnabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            )
        }
    }
}

private fun buildSignaturePayload(pathPos: List<FloatArray>): ShortArray {
    var totalLen = 0
    for (segment in pathPos) {
        totalLen += segment.size
    }
    val signature = ShortArray(totalLen)
    var index = 0
    for (segment in pathPos) {
        for (value in segment) {
            signature[index++] = value.toInt().toShort()
        }
    }
    return signature
}

private fun createSignatureView(
    context: android.content.Context,
    signatureBoardHeightPx: Int,
    controlsEnabled: Boolean,
    onSubmit: () -> Unit,
    onClear: () -> Unit,
    onCancel: () -> Unit
): ElectronicSignatureView {
    return ElectronicSignatureView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            signatureBoardHeightPx
        )
        setBitmap(Rect(0, 0, 384, 128), 0, android.graphics.Color.WHITE)
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        setOnKeyListener { _, keyCode, event ->
            handleSignatureBoardKey(
                keyCode = keyCode,
                event = event,
                onSubmit = onSubmit,
                onClear = onClear,
                onCancel = onCancel
            )
        }
        isEnabled = controlsEnabled
    }
}

private fun handleSignatureBoardKey(
    keyCode: Int,
    event: android.view.KeyEvent,
    onSubmit: () -> Unit,
    onClear: () -> Unit,
    onCancel: () -> Unit
): Boolean {
    if (event.action != KeyEvent.ACTION_UP) return false
    return when (keyCode) {
        KeyEvent.KEYCODE_ENTER -> {
            onSubmit()
            true
        }

        KeyEvent.KEYCODE_DEL -> {
            onClear()
            true
        }

        KeyEvent.KEYCODE_BACK -> {
            onCancel()
            true
        }

        else -> false
    }
}

private fun updateSignatureViewLayout(view: ElectronicSignatureView, signatureBoardHeightPx: Int) {
    if (view.layoutParams.height == signatureBoardHeightPx) return
    view.layoutParams = view.layoutParams.apply {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = signatureBoardHeightPx
    }
    view.requestLayout()
}

@Composable
private fun SignatureActionButton(
    text: String,
    background: Color,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier
) {
    PosLinkPrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        variant = PosLinkPrimaryButtonVariant.PoslinkLegacy,
        containerColorOverride = background,
        disabledContainerColorOverride = background.copy(alpha = 0.38f),
        textColorOverride = PosLinkDesignTokens.PrimaryActionTextColor,
        allCapsOverride = false
    )
}
