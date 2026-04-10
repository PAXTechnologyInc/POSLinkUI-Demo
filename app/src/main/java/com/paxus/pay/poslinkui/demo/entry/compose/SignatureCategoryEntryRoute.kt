package com.paxus.pay.poslinkui.demo.entry.compose

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import com.paxus.pay.poslinkui.demo.entry.signature.SignatureDemoScreen
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel

@Composable
fun SignatureCategoryEntryRoute(extras: Bundle, viewModel: EntryViewModel) {
    val l1 = extras.getString(EntryExtraData.PARAM_SIGNLINE1).orEmpty()
    val l2 = extras.getString(EntryExtraData.PARAM_SIGNLINE2).orEmpty()
    val timeoutSec = (extras.getLong(EntryExtraData.PARAM_TIMEOUT, 0L) / 1000L).toInt()
    val totalAmount = extras.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT, 0L)
    val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
    Column(Modifier.verticalScroll(rememberScrollState())) {
        if (l1.isNotBlank()) PosLinkText(text = l1, role = PosLinkTextRole.SectionTitle)
        if (l2.isNotBlank()) PosLinkText(text = l2)
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        SignatureDemoScreen(
            timeoutSec = timeoutSec,
            totalAmount = totalAmount,
            currency = currency,
            onSubmit = { viewModel.sendNext(it) }
        )
    }
}
