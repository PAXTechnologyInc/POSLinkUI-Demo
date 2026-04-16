package com.paxus.pay.poslinkui.demo.entry.compose

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
    val opts = extras.getStringArray(EntryExtraData.PARAM_OPTIONS) ?: emptyArray()
    if (opts.isEmpty()) {
        BoxWithCenterText(stringResource(R.string.option_list_empty))
        return
    }
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        PosLinkText(
            text = OptionEntryMessageFormatter.title(action, extras, resources),
            role = PosLinkTextRole.ScreenTitle,
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
                        val activity = (viewContext as? Activity) ?: (context as? Activity)
                        if (activity != null) {
                            val items = opts.mapIndexed { index, label ->
                                SelectOptionsView.Option(index, label ?: "", null, index)
                            }.toMutableList()
                            initialize(activity, 1, items) { option ->
                                val selectedIndex = (option?.value as? Int) ?: return@initialize
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
                    view.isEnabled = !interactionLocked
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
