package com.paxus.pay.poslinkui.demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * Second-screen line items for the demo; exposes aggregated [screenInfo] as [StateFlow] (no LiveData).
 *
 * Uses nested [combine] because the Flow API provides overloads only up to five flows.
 */
@HiltViewModel
class SecondScreenInfoViewModel @Inject constructor() : ViewModel() {
    private val title = MutableStateFlow<String?>("")
    private val amount = MutableStateFlow<String?>("")
    private val msg = MutableStateFlow<String?>("")
    private val status = MutableStateFlow<String?>("")
    private val statusTitle = MutableStateFlow<String?>("")
    private val imageResourceId = MutableStateFlow<Int?>(null)

    // kotlinx.coroutines.flow.combine has overloads up to 5 flows; nest for six inputs.
    val screenInfo: StateFlow<ScreenInfo?> = combine(
        combine(amount, msg, status) { am, m, st -> Triple(am, m, st) },
        combine(imageResourceId, title, statusTitle) { img, ti, stt -> Triple(img, ti, stt) }
    ) { amountMsgStatus, imageTitleStatusTitle ->
        buildScreenInfo(
            amountMsgStatus.first,
            amountMsgStatus.second,
            amountMsgStatus.third,
            imageTitleStatusTitle.first,
            imageTitleStatusTitle.second,
            imageTitleStatusTitle.third
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        buildScreenInfo("", "", "", null, "", "")
    )

    fun updateAmount(newContent: String?) {
        amount.value = newContent
    }

    fun updateTitle(newContent: String?) {
        title.value = newContent
    }

    fun updateMessage(newContent: String?) {
        msg.value = newContent
    }

    internal fun buildScreenInfo(
        amount: String?,
        msg: String?,
        status: String?,
        imageResourceId: Int?,
        title: String?,
        statusTitle: String?
    ): ScreenInfo {
        return ScreenInfo(amount, msg, status, imageResourceId, title, statusTitle)
    }

    fun updateAllData(
        newAmount: String?,
        newMsg: String?,
        newStatus: String?,
        newImageResourceId: Int?,
        newStatusTitle: String?,
        newTitle: String?
    ) {
        amount.value = newAmount
        msg.value = newMsg
        status.value = newStatus
        imageResourceId.value = newImageResourceId
        statusTitle.value = newStatusTitle
        title.value = newTitle
    }

    class ScreenInfo(
        val amount: String?,
        val msg: String?,
        val status: String?,
        val imageResourceId: Int?,
        val title: String?,
        val statusTitle: String?
    )
}
