package com.paxus.pay.poslinkui.demo.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.paxus.pay.poslinkui.demo.utils.AnimationSupport

/**
 * Coil [ImageRequest] tuning grouped for [PosLinkAsyncImageOptions] so the options type stays within
 * parameter-count rules.
 *
 * @property crossfade Whether to enable Coil crossfade.
 * @property decodeSizePx Optional square decode size override (width and height).
 */
data class PosLinkAsyncImageRequestTuning(
    val crossfade: Boolean = false,
    val decodeSizePx: Int? = null
)

/**
 * Optional Coil load lifecycle callbacks for [PosLinkAsyncImageOptions].
 *
 * @property onSuccess Invoked when the image loads successfully.
 * @property onError Invoked when loading fails; receives the throwable if any.
 */
data class PosLinkAsyncImageLoadHooks(
    val onSuccess: (() -> Unit)? = null,
    val onError: ((Throwable?) -> Unit)? = null
)

/**
 * Optional Coil tuning and callbacks for [PosLinkAsyncImage] (keeps the aggregate type within parameter-count rules).
 *
 * @property contentDescription Accessibility description.
 * @property contentScale Scaling behavior.
 * @property requestTuning Crossfade and decode size.
 * @property animated Whether to use the shared animated image loader.
 * @property imageLoader Optional explicit image loader override.
 * @property loadHooks Success and error callbacks.
 */
data class PosLinkAsyncImageOptions(
    val contentDescription: String? = null,
    val contentScale: ContentScale = ContentScale.Fit,
    val requestTuning: PosLinkAsyncImageRequestTuning = PosLinkAsyncImageRequestTuning(),
    val animated: Boolean = false,
    val imageLoader: ImageLoader? = null,
    val loadHooks: PosLinkAsyncImageLoadHooks = PosLinkAsyncImageLoadHooks()
)

/**
 * Shared Coil entry point for still and animated assets used by Compose screens.
 *
 * @param data Image source passed to Coil.
 * @param modifier Optional modifier.
 * @param options Display and loader options; defaults are safe for most call sites.
 */
@Composable
fun PosLinkAsyncImage(
    data: Any?,
    modifier: Modifier = Modifier,
    options: PosLinkAsyncImageOptions = PosLinkAsyncImageOptions()
) {
    val context = LocalContext.current
    val resolvedLoader = options.imageLoader ?: remember(context, options.animated) {
        if (options.animated) AnimationSupport.animatedImageLoader(context) else ImageLoader(context)
    }
    val request = remember(
        context,
        data,
        options.requestTuning.crossfade,
        options.requestTuning.decodeSizePx,
        options.loadHooks.onSuccess,
        options.loadHooks.onError
    ) {
        ImageRequest.Builder(context)
            .data(data)
            .crossfade(options.requestTuning.crossfade)
            .apply {
                options.requestTuning.decodeSizePx?.let { size(it, it) }
                listener(
                    onSuccess = { _, _ -> options.loadHooks.onSuccess?.invoke() },
                    onError = { _, result -> options.loadHooks.onError?.invoke(result.throwable) }
                )
            }
            .build()
    }
    AsyncImage(
        model = request,
        contentDescription = options.contentDescription,
        imageLoader = resolvedLoader,
        modifier = modifier,
        contentScale = options.contentScale
    )
}
