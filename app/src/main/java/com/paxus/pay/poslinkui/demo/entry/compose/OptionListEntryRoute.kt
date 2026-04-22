package com.paxus.pay.poslinkui.demo.entry.compose

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.OptionEntry
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.option.OptionEntryMessageFormatter
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.view.SelectOptionsView
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Option category: [EntryExtraData.PARAM_OPTIONS] and [com.pax.us.pay.ui.constant.entry.EntryRequest.PARAM_INDEX] (0-based).
 */
@Composable
fun OptionListEntryRoute(
    action: String?,
    extras: Bundle,
    viewModel: EntryViewModel,
    resources: Resources
) {
    val interactionLocked = LocalEntryInteractionLocked.current
    val latestInteractionLocked = rememberUpdatedState(interactionLocked)
    var optionSubmitted by remember(action, extras) { mutableStateOf(false) }
    val latestOptionSubmitted = rememberUpdatedState(optionSubmitted)
    val opts = extras.getStringArray(EntryExtraData.PARAM_OPTIONS) ?: emptyArray()
    if (opts.isEmpty()) {
        BoxWithCenterText(stringResource(R.string.option_list_empty))
        return
    }
    val context = LocalContext.current
    val titleTextSize = (resources.getDimension(R.dimen.text_size_title) / resources.displayMetrics.scaledDensity).sp
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = OptionEntryMessageFormatter.title(action, extras, resources),
            color = PosLinkDesignTokens.PrimaryTextColor,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Normal,
                fontSize = titleTextSize,
                lineHeight = titleTextSize * PosLinkDesignTokens.EntryTitleLineHeightMultiplier
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        if (action == OptionEntry.ACTION_SELECT_CURRENCY) {
            val exchange = extras.getString(EntryExtraData.PARAM_EXCHANGE_RATE).orEmpty()
            val markup = extras.getString(EntryExtraData.PARAM_MARKUP).orEmpty()
            if (exchange.isNotBlank()) {
                PosLinkText(text = exchange, role = PosLinkTextRole.Supporting)
                Spacer(Modifier.height(PosLinkDesignTokens.CompactSpacing))
            }
            if (markup.isNotBlank()) {
                PosLinkText(text = markup, role = PosLinkTextRole.Supporting)
                Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { viewContext ->
                    SelectOptionsView(viewContext).apply {
                        val consumed = AtomicBoolean(false)
                        val activity = (viewContext as? Activity) ?: (context as? Activity)
                        if (activity != null) {
                            val items = opts.mapIndexed { index, label ->
                                SelectOptionsView.Option(index, label ?: "", null, index)
                            }.toMutableList()
                            initialize(activity, 1, items) { option ->
                                if (!consumed.compareAndSet(false, true)) return@initialize
                                if (latestInteractionLocked.value || latestOptionSubmitted.value) return@initialize
                                val selectedIndex = (option?.value as? Int) ?: return@initialize
                                optionSubmitted = true
                                lockOptionView(this@apply)
                                viewModel.sendNext(
                                    Bundle().apply {
                                        putInt(EntryRequest.PARAM_INDEX, selectedIndex)
                                    }
                                )
                            }
                        }
                    }
                },
                update = { view ->
                    val enabled = !interactionLocked && !optionSubmitted
                    if (enabled) unlockOptionView(view) else lockOptionView(view)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun lockOptionView(view: SelectOptionsView) {
    view.isEnabled = false
    view.isClickable = false
    view.isFocusable = false
    view.setOnTouchListener { _, _ -> true }
    setDescendantsEnabled(view, false)
}

private fun unlockOptionView(view: SelectOptionsView) {
    view.isEnabled = true
    view.isClickable = true
    view.isFocusable = true
    view.setOnTouchListener(null)
    setDescendantsEnabled(view, true)
}

private fun setDescendantsEnabled(view: View, enabled: Boolean) {
    view.isEnabled = enabled
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            setDescendantsEnabled(view.getChildAt(i), enabled)
        }
    }
}
