package com.paxus.pay.poslinkui.demo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.AnimationLogger
import com.paxus.pay.poslinkui.demo.utils.AnimationPolicy
import com.paxus.pay.poslinkui.demo.utils.Toast
import com.paxus.pay.poslinkui.demo.utils.ToastCenter
import com.paxus.pay.poslinkui.demo.utils.ToastMessage
import com.paxus.pay.poslinkui.demo.utils.currentAnimationPolicy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

/**
 * Activity-level toast host for Compose screens, replacing the fragment-backed toast container.
 */
@Composable
fun PosLinkToastHost(modifier: Modifier = Modifier) {
    var currentToast by remember { mutableStateOf<ToastMessage?>(null) }
    val animationPolicy = currentAnimationPolicy

    LaunchedEffect(Unit) {
        ToastCenter.events.collectLatest { toast ->
            if (animationPolicy != AnimationPolicy.STANDARD) {
                AnimationLogger.logDowngrade(
                    "A2",
                    "render Compose toast with ${animationPolicy.name.lowercase()} motion",
                    animationPolicy
                )
            }
            currentToast = toast
            delay(Toast.durationMs())
            currentToast = null
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
        AnimatedVisibility(
            visible = currentToast != null,
            enter = animationPolicy.toastEnter(),
            exit = animationPolicy.toastExit(),
            modifier = Modifier
                .padding(
                    top = PosLinkDesignTokens.ToastTopPadding,
                    end = PosLinkDesignTokens.ScreenPadding
                )
        ) {
            currentToast?.let { toast ->
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .background(
                            color = toast.backgroundColor(),
                            shape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius)
                        )
                        .padding(
                            horizontal = PosLinkDesignTokens.ToastHorizontalPadding,
                            vertical = PosLinkDesignTokens.ToastVerticalPadding
                        )
                ) {
                    PosLinkText(
                        text = toast.message,
                        color = PosLinkDesignTokens.SecondScreenTextColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private fun ToastMessage.backgroundColor() = when (type) {
    Toast.TYPE.SUCCESS -> PosLinkDesignTokens.ToastSuccessColor
    Toast.TYPE.FAILURE -> PosLinkDesignTokens.ToastFailureColor
}

private fun AnimationPolicy.toastEnter() = when (this) {
    AnimationPolicy.STANDARD -> slideInHorizontally { it / 2 } + fadeIn()
    AnimationPolicy.REDUCED -> fadeIn()
    AnimationPolicy.MINIMAL -> EnterTransition.None
}

private fun AnimationPolicy.toastExit() = when (this) {
    AnimationPolicy.STANDARD -> slideOutHorizontally { it / 2 } + fadeOut()
    AnimationPolicy.REDUCED -> fadeOut()
    AnimationPolicy.MINIMAL -> ExitTransition.None
}
