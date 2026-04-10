package com.paxus.pay.poslinkui.demo.entry.signature

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyMaterialFillAppearance
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyMaterialFilledButton
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType

/**
 * Demo signature capture: submits a minimal [EntryRequest.PARAM_SIGNATURE] stub.
 * Production must replace with real stroke capture and valid [EntryRequest.PARAM_SIGN_STATUS].
 *
 * @param onSubmit Called with a bundle suitable for [com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel.sendNext]
 */
@Composable
fun SignatureDemoScreen(
    timeoutSec: Int,
    totalAmount: Long,
    currency: String?,
    onSubmit: (Bundle) -> Unit
) {
    Logger.i("Signature parity v5 active")
    val timeoutDisplay = if (timeoutSec > 0) timeoutSec else 23
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = PosLinkDesignTokens.ScreenPadding)
    ) {
        Text(
            text = timeoutDisplay.toString(),
            color = Color(0xFF2196F3),
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total Amount", color = PosLinkDesignTokens.PrimaryTextColor)
            Text(
                text = CurrencyUtils.convert(totalAmount, currency ?: CurrencyType.USD),
                color = PosLinkDesignTokens.PrimaryTextColor
            )
        }
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(306.dp)
                .border(1.dp, Color(0xFF13304C))
                .background(Color(0xFFDADADA))
        )
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PosLinkDesignTokens.SectionSpacing),
            horizontalArrangement = Arrangement.spacedBy(PosLinkDesignTokens.ControlGutter)
        ) {
            PosLinkLegacyMaterialFilledButton(
                onClick = { /* reserved for future stroke clear */ },
                modifier = Modifier.weight(1f),
                appearance = PosLinkLegacyMaterialFillAppearance(
                    slotHeight = PosLinkDesignTokens.ButtonHeight,
                    shape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius),
                    containerColor = Color(0xFF89AA97),
                    disabledContainerColor = Color(0xFF89AA97).copy(alpha = 0.38f)
                )
            ) {
                Text("Clear", letterSpacing = 1.sp, color = Color(0xFFECECEC))
            }
            PosLinkLegacyMaterialFilledButton(
                onClick = {
                    onSubmit(
                        Bundle().apply {
                            putString(EntryRequest.PARAM_SIGN_STATUS, "DEMO_ACCEPT")
                            putShortArray(EntryRequest.PARAM_SIGNATURE, shortArrayOf(0, 0, 1, 0, 1, 1))
                        }
                    )
                },
                modifier = Modifier.weight(1f),
                appearance = PosLinkLegacyMaterialFillAppearance(
                    slotHeight = PosLinkDesignTokens.ButtonHeight,
                    shape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius),
                    containerColor = Color(0xFF6E85B7),
                    disabledContainerColor = Color(0xFF6E85B7).copy(alpha = 0.38f)
                )
            ) {
                Text("Confirm", letterSpacing = 1.sp, color = Color(0xFFECECEC))
            }
        }
    }
}
