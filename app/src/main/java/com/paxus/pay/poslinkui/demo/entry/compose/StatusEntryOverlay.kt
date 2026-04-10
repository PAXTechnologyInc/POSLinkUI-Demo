package com.paxus.pay.poslinkui.demo.entry.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel
import com.paxus.pay.poslinkui.demo.viewmodel.StatusOverlayUi
import com.paxus.pay.poslinkui.demo.viewmodel.StatusTitleTone

/**
 * Full-screen status layer (replaces [com.paxus.pay.poslinkui.demo.status.StatusFragment] XML).
 */
@Composable
fun StatusEntryOverlay(overlay: StatusOverlayUi) {
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val secondVm = ViewModelProvider(activity)[SecondScreenInfoViewModel::class.java]

    LaunchedEffect(
        overlay.revision,
        overlay.screenStatusMessage,
        overlay.transactionStatus,
        overlay.screenStatusTitle
    ) {
        if (overlay.transactionStatus.isEmpty()) {
            secondVm.updateAllData("", overlay.screenStatusMessage, "", null, "", "")
        } else {
            secondVm.updateAllData("", "", overlay.transactionStatus, null, overlay.screenStatusTitle, "")
        }
    }

    val titleColor: Color = when (overlay.titleTone) {
        StatusTitleTone.Default -> Color(0xFF5A5A5A)
        StatusTitleTone.Success -> PosLinkDesignTokens.SuccessColor
        StatusTitleTone.Error -> PosLinkDesignTokens.FailColor
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(PosLinkDesignTokens.CardPadding),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PosLinkDesignTokens.CardPadding),
                contentAlignment = Alignment.Center
            ) {
                PosLinkText(
                    text = overlay.title,
                    role = PosLinkTextRole.Status,
                    color = titleColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
