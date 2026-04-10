package com.paxus.pay.poslinkui.demo.entry.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImage
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImageLoadHooks
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImageOptions
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImageRequestTuning
import com.paxus.pay.poslinkui.demo.utils.AnimationLogger
import com.paxus.pay.poslinkui.demo.utils.AnimationPolicy
import com.paxus.pay.poslinkui.demo.utils.AnimationSupport
import com.paxus.pay.poslinkui.demo.utils.currentAnimationPolicy

/**
 * Compose-only rendering surface for second-screen border animation.
 */
@Composable
fun SecondScreenBorderAnimation(modifier: Modifier = Modifier) {
    val animationPolicy = currentAnimationPolicy
    var loadFailed by remember(animationPolicy) { mutableStateOf(false) }

    if (!AnimationSupport.shouldShowSecondScreenGif(animationPolicy) || loadFailed) {
        if (animationPolicy == AnimationPolicy.MINIMAL) {
            LaunchedEffect(animationPolicy) {
                AnimationLogger.logDowngrade("A4", "hide second-screen border GIF", animationPolicy)
            }
        }
        if (loadFailed) {
            Image(
                painter = painterResource(R.drawable.border),
                contentDescription = null,
                modifier = modifier,
                contentScale = ContentScale.FillBounds
            )
        }
        return
    }

    val decodeSizePx = AnimationSupport.secondScreenGifSizePx(animationPolicy)
    if (animationPolicy == AnimationPolicy.REDUCED) {
        LaunchedEffect(animationPolicy, decodeSizePx) {
            AnimationLogger.logDowngrade(
                "A4",
                "reduce second-screen GIF decode size to ${decodeSizePx}px",
                animationPolicy
            )
        }
    }

    Box(modifier = modifier) {
        PosLinkAsyncImage(
            data = R.raw.border_animated,
            modifier = Modifier.fillMaxSize(),
            options = PosLinkAsyncImageOptions(
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                requestTuning = PosLinkAsyncImageRequestTuning(decodeSizePx = decodeSizePx),
                animated = true,
                loadHooks = PosLinkAsyncImageLoadHooks(
                    onSuccess = {
                        AnimationLogger.logPerformanceEvent(
                            event = "A4.borderGifLoaded",
                            durationMs = null,
                            extra = "policy=$animationPolicy sizePx=$decodeSizePx"
                        )
                    },
                    onError = { throwable ->
                        loadFailed = true
                        AnimationLogger.logDowngrade(
                            "A4",
                            "second-screen GIF load failed: ${throwable?.javaClass?.simpleName ?: "unknown"}",
                            animationPolicy
                        )
                    }
                )
            )
        )
    }
}
