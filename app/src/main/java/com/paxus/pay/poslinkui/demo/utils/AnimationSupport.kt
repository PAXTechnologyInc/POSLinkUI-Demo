package com.paxus.pay.poslinkui.demo.utils

import android.content.Context
import android.os.Build
import androidx.fragment.app.FragmentTransaction
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

private val DEFAULT_REDUCED_TRANSITION_ENTER = android.R.anim.fade_in
private val DEFAULT_REDUCED_TRANSITION_EXIT = android.R.anim.fade_out

private const val CLSS_BLINK_STANDARD_MS = 500L
private const val CLSS_BLINK_REDUCED_MS = 800L

private const val SECOND_SCREEN_GIF_STANDARD_PX = 800
private const val SECOND_SCREEN_GIF_REDUCED_PX = 560

private const val APPROVAL_GIF_STANDARD_PX = 1200
private const val APPROVAL_GIF_REDUCED_PX = 840
private const val APPROVAL_GIF_STANDARD_DP = 600
private const val APPROVAL_GIF_REDUCED_DP = 420
private const val APPROVAL_GIF_STANDARD_DELAY_MS = 3500L
private const val APPROVAL_GIF_REDUCED_DELAY_MS = 2200L
private const val APPROVAL_TEXT_DELAY_MS = 3000L
private const val APPROVAL_TEXT_MINIMAL_DELAY_MS = 1500L

/**
 * Shared animation tuning and loaders for the app's active animation points.
 */
object AnimationSupport {
    @Volatile
    private var animatedImageLoader: ImageLoader? = null

    fun applyFragmentTransition(
        transaction: FragmentTransaction,
        pointId: String,
        policy: AnimationPolicy,
        standardEnter: Int,
        standardExit: Int,
        reducedEnter: Int = DEFAULT_REDUCED_TRANSITION_ENTER,
        reducedExit: Int = DEFAULT_REDUCED_TRANSITION_EXIT
    ): FragmentTransaction {
        return when (policy) {
            AnimationPolicy.STANDARD -> transaction.setCustomAnimations(standardEnter, standardExit)
            AnimationPolicy.REDUCED -> {
                AnimationLogger.logDowngrade(
                    pointId,
                    "use reduced fade transition",
                    policy
                )
                transaction.setCustomAnimations(reducedEnter, reducedExit)
            }

            AnimationPolicy.MINIMAL -> {
                AnimationLogger.logDowngrade(
                    pointId,
                    "skip non-essential fragment transition",
                    policy
                )
                transaction
            }
        }
    }

    fun clssBlinkDurationMs(policy: AnimationPolicy): Long? {
        return when (policy) {
            AnimationPolicy.STANDARD -> CLSS_BLINK_STANDARD_MS
            AnimationPolicy.REDUCED -> CLSS_BLINK_REDUCED_MS
            AnimationPolicy.MINIMAL -> null
        }
    }

    fun shouldShowSecondScreenGif(policy: AnimationPolicy): Boolean = policy != AnimationPolicy.MINIMAL

    fun secondScreenGifSizePx(policy: AnimationPolicy): Int {
        return when (policy) {
            AnimationPolicy.STANDARD -> SECOND_SCREEN_GIF_STANDARD_PX
            AnimationPolicy.REDUCED -> SECOND_SCREEN_GIF_REDUCED_PX
            AnimationPolicy.MINIMAL -> 0
        }
    }

    fun shouldAnimateApprovalGif(policy: AnimationPolicy): Boolean = policy != AnimationPolicy.MINIMAL

    fun approvalGifSizePx(policy: AnimationPolicy): Int {
        return when (policy) {
            AnimationPolicy.STANDARD -> APPROVAL_GIF_STANDARD_PX
            AnimationPolicy.REDUCED -> APPROVAL_GIF_REDUCED_PX
            AnimationPolicy.MINIMAL -> 0
        }
    }

    fun approvalGifSizeDp(policy: AnimationPolicy): Int {
        return when (policy) {
            AnimationPolicy.STANDARD -> APPROVAL_GIF_STANDARD_DP
            AnimationPolicy.REDUCED -> APPROVAL_GIF_REDUCED_DP
            AnimationPolicy.MINIMAL -> 0
        }
    }

    fun approvalDisplayDurationMs(policy: AnimationPolicy, animated: Boolean): Long {
        return when {
            animated && policy == AnimationPolicy.STANDARD -> APPROVAL_GIF_STANDARD_DELAY_MS
            animated && policy == AnimationPolicy.REDUCED -> APPROVAL_GIF_REDUCED_DELAY_MS
            policy == AnimationPolicy.MINIMAL -> APPROVAL_TEXT_MINIMAL_DELAY_MS
            else -> APPROVAL_TEXT_DELAY_MS
        }
    }

    fun shouldUseReceiptPreviewCrossfade(policy: AnimationPolicy): Boolean =
        policy == AnimationPolicy.STANDARD

    fun animatedImageLoader(context: Context): ImageLoader {
        val appContext = context.applicationContext
        return animatedImageLoader ?: synchronized(this) {
            animatedImageLoader ?: ImageLoader.Builder(appContext)
                .components {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()
                .also { animatedImageLoader = it }
        }
    }
}
