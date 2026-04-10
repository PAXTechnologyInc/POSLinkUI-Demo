package com.paxus.pay.poslinkui.demo.entry.compose

import android.content.res.Resources
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.OptionEntry
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.option.OptionEntryMessageFormatter
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.Logger
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
    if (action == OptionEntry.ACTION_SELECT_SUB_TRANS_TYPE) {
        LaunchedEffect(action) {
            Logger.i("SelectSubTransType parity v1 active")
        }
    }
    val opts = extras.getStringArray(EntryExtraData.PARAM_OPTIONS) ?: emptyArray()
    if (opts.isEmpty()) {
        BoxWithCenterText(stringResource(R.string.option_list_empty))
        return
    }
    var selected by remember(opts) { mutableStateOf(0) }
    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        PosLinkText(
            text = OptionEntryMessageFormatter.title(action, extras, resources),
            role = PosLinkTextRole.ScreenTitle,
            modifier = Modifier.fillMaxWidth()
        )
        if (action == OptionEntry.ACTION_SELECT_CURRENCY) {
            val exchange = extras.getString(EntryExtraData.PARAM_EXCHANGE_RATE).orEmpty()
            val markup = extras.getString(EntryExtraData.PARAM_MARKUP).orEmpty()
            if (exchange.isNotBlank()) {
                Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
                PosLinkText(text = exchange, role = PosLinkTextRole.Supporting)
            }
            if (markup.isNotBlank()) {
                Spacer(Modifier.height(PosLinkDesignTokens.CompactSpacing))
                PosLinkText(text = markup, role = PosLinkTextRole.Supporting)
            }
        }
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        opts.forEachIndexed { index, label ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(selected = selected == index, onClick = { selected = index })
                PosLinkText(text = label ?: "", modifier = Modifier.padding(start = PosLinkDesignTokens.ControlGutter))
            }
        }
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm),
            onClick = {
                buildOptionSubmitBundle(selectedIndex = selected, optionCount = opts.size)?.let { payload ->
                    viewModel.sendNext(payload)
                }
            }
        )
    }
}
