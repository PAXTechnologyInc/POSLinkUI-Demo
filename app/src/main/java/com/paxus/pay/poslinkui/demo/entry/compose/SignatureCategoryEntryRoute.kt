package com.paxus.pay.poslinkui.demo.entry.compose

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import com.paxus.pay.poslinkui.demo.entry.signature.SignatureDemoScreen
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel

@Composable
fun SignatureCategoryEntryRoute(extras: Bundle, viewModel: EntryViewModel) {
    val controlsEnabled = !LocalEntryInteractionLocked.current
    val activity = LocalContext.current as FragmentActivity
    val l1 = extras.getString(EntryExtraData.PARAM_SIGNLINE1).orEmpty()
    val l2 = extras.getString(EntryExtraData.PARAM_SIGNLINE2).orEmpty()
    val timeoutMs = extras.getLong(EntryExtraData.PARAM_TIMEOUT, 30_000L)
    // Match SignatureFragment: Bundle#getBoolean(key) is false when key absent.
    val enableCancel = extras.getBoolean(EntryExtraData.PARAM_ENABLE_CANCEL, false)
    val totalAmount = extras.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT, 0L)
    val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)

    fun scheduleHostTimeout() {
        if (timeoutMs <= 0L) return
        activity.supportFragmentManager.setFragmentResult(
            TaskScheduler.SCHEDULE,
            TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeoutMs)
        )
    }

    LaunchedEffect(timeoutMs) {
        scheduleHostTimeout()
    }

    SignatureDemoScreen(
        signLine1 = l1,
        signLine2 = l2,
        timeoutMs = timeoutMs,
        totalAmount = totalAmount,
        currency = currency,
        enableCancel = enableCancel,
        controlsEnabled = controlsEnabled,
        onHostTimeoutReset = { scheduleHostTimeout() },
        onCancel = { viewModel.sendAbort() },
        onSubmit = { signature ->
            viewModel.sendNext(
                Bundle().apply {
                    putShortArray(EntryRequest.PARAM_SIGNATURE, signature)
                }
            )
        }
    )
}
