package com.paxus.pay.poslinkui.demo.entry.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pax.us.pay.ui.constant.entry.enumeration.CardType
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImage
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImageLoadHooks
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImageOptions
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImageRequestTuning
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.AnimationLogger
import com.paxus.pay.poslinkui.demo.utils.AnimationPolicy
import com.paxus.pay.poslinkui.demo.utils.AnimationSupport
import com.paxus.pay.poslinkui.demo.utils.currentAnimationPolicy
import kotlinx.coroutines.delay

/**
 * Pure Compose approval display with policy-aware GIF downgrade.
 */
@Composable
fun ApproveMessageScreen(
    cardType: String?,
    onComplete: () -> Unit
) {
    val animationPolicy = currentAnimationPolicy
    var approvalLoadFailed by remember(cardType, animationPolicy) { mutableStateOf(false) }
    val showAnimatedApproval =
        cardType == CardType.MASTER_CARD &&
            AnimationSupport.shouldAnimateApprovalGif(animationPolicy) &&
            !approvalLoadFailed
    val approvalSizeDp = AnimationSupport.approvalGifSizeDp(animationPolicy)
    val approvalSizePx = AnimationSupport.approvalGifSizePx(animationPolicy)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (showAnimatedApproval) {
            if (animationPolicy == AnimationPolicy.REDUCED) {
                LaunchedEffect(cardType, animationPolicy) {
                    AnimationLogger.logDowngrade(
                        "A5",
                        "render approval GIF with reduced size $approvalSizePx px",
                        animationPolicy
                    )
                }
            }
            PosLinkAsyncImage(
                data = R.drawable.mastercard_approval,
                modifier = Modifier.size(approvalSizeDp.dp),
                options = PosLinkAsyncImageOptions(
                    contentDescription = null,
                    requestTuning = PosLinkAsyncImageRequestTuning(decodeSizePx = approvalSizePx),
                    animated = true,
                    loadHooks = PosLinkAsyncImageLoadHooks(
                        onSuccess = {
                            AnimationLogger.logPerformanceEvent(
                                event = "A5.approvalGifLoaded",
                                durationMs = null,
                                extra = "policy=$animationPolicy sizePx=$approvalSizePx"
                            )
                        },
                        onError = { throwable ->
                            approvalLoadFailed = true
                            AnimationLogger.logDowngrade(
                                "A5",
                                "approval GIF load failed: ${throwable?.javaClass?.simpleName ?: "unknown"}",
                                animationPolicy
                            )
                        }
                    )
                )
            )
        } else {
            if (cardType == CardType.MASTER_CARD && animationPolicy == AnimationPolicy.MINIMAL) {
                LaunchedEffect(cardType, animationPolicy) {
                    AnimationLogger.logDowngrade(
                        "A5",
                        "replace approval GIF with text fallback",
                        animationPolicy
                    )
                }
            }
            ApprovalTextFallback(cardType = cardType)
        }
    }

    LaunchedEffect(cardType, animationPolicy, showAnimatedApproval) {
        delay(AnimationSupport.approvalDisplayDurationMs(animationPolicy, showAnimatedApproval))
        onComplete()
    }
}

@Composable
private fun ApprovalTextFallback(cardType: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PosLinkText(
            text = "Approved By",
            role = PosLinkTextRole.SectionTitle,
            textAlign = TextAlign.Center,
            color = PosLinkDesignTokens.PrimaryTextColor
        )
        PosLinkText(
            text = cardType.orEmpty(),
            role = PosLinkTextRole.Status,
            textAlign = TextAlign.Center,
            color = PosLinkDesignTokens.PrimaryTextColor
        )
    }
}
