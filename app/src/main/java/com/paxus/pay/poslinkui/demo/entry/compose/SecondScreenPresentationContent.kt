package com.paxus.pay.poslinkui.demo.entry.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.pax.us.pay.ui.constant.entry.enumeration.TransactionStatus
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel.ScreenInfo

@Composable
fun SecondScreenPresentationContent(screenInfo: ScreenInfo?) {
    val uiState = rememberSecondScreenUiState(screenInfo)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(uiState.backgroundColor)
    ) {
        if (uiState.showBorderAnimation) {
            SecondScreenBorderAnimation(modifier = Modifier.fillMaxSize())
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.title.isNotBlank()) {
                SecondScreenText(
                    text = uiState.title,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = PosLinkDesignTokens.SecondScreenTitleTextSize,
                    color = PosLinkDesignTokens.SecondScreenTextColor,
                    maxLines = 3
                )
            }

            if (uiState.amount.isNotBlank()) {
                SecondScreenText(
                    text = uiState.amount,
                    fontSize = PosLinkDesignTokens.SecondScreenAmountTextSize,
                    color = PosLinkDesignTokens.FailColor,
                    fontWeight = FontWeight.Bold
                )
            }

            uiState.imageResourceId?.let { imageId ->
                Image(
                    painter = painterResource(imageId),
                    contentDescription = null,
                    modifier = Modifier.size(
                        width = PosLinkDesignTokens.SecondScreenTapIconWidth,
                        height = PosLinkDesignTokens.SecondScreenTapIconHeight
                    ),
                    contentScale = ContentScale.FillBounds
                )
            }

            if (uiState.message.isNotBlank()) {
                SecondScreenText(
                    text = uiState.message,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = PosLinkDesignTokens.SecondScreenMessageTextSize,
                    color = PosLinkDesignTokens.SecondScreenTextColor
                )
            }

            if (uiState.statusMessage.isNotBlank()) {
                SecondScreenText(
                    text = uiState.statusMessage,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = PosLinkDesignTokens.SecondScreenStatusTextSize,
                    color = PosLinkDesignTokens.SecondScreenTextColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3
                )
            }
        }
    }
}

@Composable
private fun rememberSecondScreenUiState(screenInfo: ScreenInfo?): SecondScreenUiState {
    val fallbackApproved = stringResource(R.string.status_msg_approved)
    val fallbackDeclined = stringResource(R.string.status_msg_declined)
    val fallbackPartial = stringResource(R.string.status_msg_partially_approved)

    val info = screenInfo
    return when (info?.status) {
        TransactionStatus.APPROVED.name -> SecondScreenUiState(
            backgroundColor = PosLinkDesignTokens.SuccessColor,
            statusMessage = info.statusTitle?.takeIf { it.isNotBlank() } ?: fallbackApproved
        )

        TransactionStatus.DECLINED.name -> SecondScreenUiState(
            backgroundColor = PosLinkDesignTokens.FailColor,
            statusMessage = info.statusTitle?.takeIf { it.isNotBlank() } ?: fallbackDeclined
        )

        TransactionStatus.PARTIALLY_APPROVED.name -> SecondScreenUiState(
            backgroundColor = PosLinkDesignTokens.PartialSuccessColor,
            statusMessage = info.statusTitle?.takeIf { it.isNotBlank() } ?: fallbackPartial
        )

        else -> SecondScreenUiState(
            backgroundColor = PosLinkDesignTokens.SecondScreenBackgroundColor,
            title = info?.title.orEmpty(),
            amount = info?.amount.orEmpty(),
            message = info?.msg.orEmpty(),
            imageResourceId = info?.imageResourceId,
            showBorderAnimation = true
        )
    }
}

private data class SecondScreenUiState(
    val backgroundColor: Color,
    val title: String = "",
    val amount: String = "",
    val message: String = "",
    val statusMessage: String = "",
    val imageResourceId: Int? = null,
    val showBorderAnimation: Boolean = false
)

@Composable
private fun SecondScreenText(
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    color: Color,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        color = color,
        fontWeight = fontWeight,
        textAlign = TextAlign.Center,
        maxLines = maxLines
    )
}
