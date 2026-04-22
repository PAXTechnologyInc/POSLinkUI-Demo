package com.paxus.pay.poslinkui.demo.entry.security

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyThemeButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens

/**
 * Enabled-state **mipmap** ids for insert / tap / swipe hints (PNG in `res/mipmap-*`).
 *
 * Compose [painterResource] cannot load legacy `R.drawable.selection_*` **selector** XML; disabled
 * state is approximated with alpha on [InputModeIconSlot] instead of `_disabled` mipmaps.
 */
private data class InputAccountSelectionDrawables(
    val swipe: Int,
    val insert: Int,
    val tap: Int
)

private fun selectionDrawablesForModel(model: String): InputAccountSelectionDrawables {
    var swipeId = R.mipmap.swipe_card_a920
    var insertId = R.mipmap.insert_card_a920
    var tapId = R.mipmap.tap_card_a920
    when (model) {
        "A60" -> {
            swipeId = R.mipmap.swipe_card_a60
            insertId = R.mipmap.insert_card_a60
            tapId = R.mipmap.tap_card_a920
        }
        "Aries8" -> {
            swipeId = R.mipmap.swipe_card_ar_x
            insertId = R.mipmap.insert_card_ar_x
            tapId = R.mipmap.tap_card_ar_x
        }
        "Aries6" -> {
            swipeId = R.mipmap.swipe_card_ar6
            insertId = R.mipmap.insert_card_ar6
            tapId = R.mipmap.tap_card_ar6
        }
        "A80" -> swipeId = R.mipmap.swipe_card_a80
        "A930" -> swipeId = R.mipmap.swipe_card_a930
        "A77" -> {
            swipeId = R.mipmap.swipe_card_a77
            insertId = R.mipmap.insert_card_a77
            tapId = R.mipmap.tap_card_a77
        }
        "PX7A" -> swipeId = R.mipmap.swipe_card_px7a
        "IM30" -> {
            swipeId = R.mipmap.swipe_card_im30
            insertId = R.mipmap.insert_card_im30
            tapId = R.mipmap.tap_card_im30
        }
        "A30" -> {
            swipeId = R.mipmap.swipe_card_a30
            insertId = R.mipmap.insert_card_a30
            tapId = R.mipmap.tap_card_a30
        }
        "A35" -> {
            swipeId = R.mipmap.swipe_card_a35
            insertId = R.mipmap.insert_card_a35
            tapId = R.mipmap.tap_card_a35
        }
        "A800" -> swipeId = R.mipmap.swipe_card_a800
    }
    return InputAccountSelectionDrawables(swipeId, insertId, tapId)
}

@Composable
internal fun RowScope.ContactlessLogoSlot(imageRes: Int) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .padding(PosLinkDesignTokens.InlineSpacing)
    )
}

/**
 * Wallet / tap brand strip (`fragment_input_account` [contactless_logo_container]).
 *
 * @param modifier Applied to the outer row (e.g. vertical margin); width/height/background are chained after.
 */
@Composable
internal fun ContactlessLogoRow(
    showNfc: Boolean,
    showApplePay: Boolean,
    showGooglePay: Boolean,
    showSamsungPay: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(PosLinkDesignTokens.buttonHeight())
            .background(
                color = Color(0xFFDBD4D9),
                shape = RoundedCornerShape(PosLinkDesignTokens.CornerRadius)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showNfc) ContactlessLogoSlot(R.mipmap.tap)
        if (showGooglePay) ContactlessLogoSlot(R.mipmap.google_pay)
        if (showApplePay) ContactlessLogoSlot(R.mipmap.apple_pay)
        if (showSamsungPay) ContactlessLogoSlot(R.mipmap.samsung_pay)
    }
}

@Composable
internal fun RowScope.InputModeIconSlot(imageRes: Int, enabled: Boolean) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = dimensionResource(R.dimen.margin_gap))
            .then(if (!enabled) Modifier.alpha(0.38f) else Modifier)
    )
}

@Composable
internal fun InputModeImageRow(
    enableInsert: Boolean,
    enableTap: Boolean,
    enableSwipe: Boolean,
    modifier: Modifier = Modifier
) {
    val drawables = selectionDrawablesForModel(Build.MODEL)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        InputModeIconSlot(imageRes = drawables.insert, enabled = enableInsert)
        InputModeIconSlot(imageRes = drawables.tap, enabled = enableTap)
        InputModeIconSlot(imageRes = drawables.swipe, enabled = enableSwipe)
    }
}

@Composable
internal fun InputAccountConfirmButton(onClick: () -> Unit, enabled: Boolean = true) {
    PosLinkLegacyThemeButton(
        text = stringResource(R.string.confirm),
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
    )
}
