package com.paxus.pay.poslinkui.demo.viewmodel

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.EntryResponse
import com.paxus.pay.poslinkui.demo.status.ParsedStatus
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils
import com.paxus.pay.poslinkui.demo.utils.SecureAreaBoundsPayload
import com.paxus.pay.poslinkui.demo.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Single source of truth for Entry UI when using Compose [androidx.navigation.compose.NavHost]:
 * incoming [Intent] updates [uiState]; screens call [sendNext] / [sendAbort]; manager responses arrive on [managerResponse].
 */
data class EntryUiState(
    val revision: Long = 0L,
    val entryAction: String? = null,
    val categories: Set<String> = emptySet(),
    val extras: Bundle = Bundle(),
    val senderPackage: String? = null,
    val poslinkContentCleared: Boolean = false
)

sealed class ManagerResponseEvent {
    object Accepted : ManagerResponseEvent()
    data class Declined(val code: Int, val message: String?) : ManagerResponseEvent()
}

enum class StatusTitleTone {
    Default,
    Success,
    Error
}

/**
 * Status broadcast overlay (former [com.paxus.pay.poslinkui.demo.status.StatusFragment] / [com.paxus.pay.poslinkui.demo.status.TransCompletedStatusFragment]).
 */
data class StatusOverlayUi(
    val revision: Long,
    val action: String,
    val title: String,
    val titleTone: StatusTitleTone,
    val transactionStatus: String,
    val screenStatusMessage: String,
    val screenStatusTitle: String,
    val transCompletedHostTimeoutMs: Long,
    val transCompletedResultCode: Long
)

@HiltViewModel
class EntryViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(EntryUiState())
    val uiState: StateFlow<EntryUiState> = _uiState.asStateFlow()

    private val _statusOverlay = MutableStateFlow<StatusOverlayUi?>(null)
    val statusOverlay: StateFlow<StatusOverlayUi?> = _statusOverlay.asStateFlow()

    private val _managerResponse = MutableSharedFlow<ManagerResponseEvent>(extraBufferCapacity = 8)
    val managerResponse: SharedFlow<ManagerResponseEvent> = _managerResponse.asSharedFlow()

    /**
     * Apply a new Entry [Intent] after the activity has validated it
     * ([com.paxus.pay.poslinkui.demo.entry.EntryActionRegistry.canResolveEntry] or demo extra).
     */
    fun consumeEntryIntent(intent: Intent) {
        _statusOverlay.value = null
        val extras = Bundle().apply { intent.extras?.let { putAll(it) } }
        _uiState.update { prev ->
            EntryUiState(
                revision = prev.revision + 1,
                entryAction = intent.action,
                categories = intent.categories?.toSet() ?: emptySet(),
                extras = extras,
                senderPackage = extras.getString(EntryExtraData.PARAM_PACKAGE),
                poslinkContentCleared = false
            )
        }
    }

    fun clearPoslinkContent() {
        _uiState.update { prev ->
            prev.copy(
                revision = prev.revision + 1L,
                extras = Bundle(),
                poslinkContentCleared = true
            )
        }
    }

    fun showStatusOverlay(ui: StatusOverlayUi) {
        _statusOverlay.value = ui.copy(revision = (_statusOverlay.value?.revision ?: 0L) + 1L)
    }

    fun updateStatusOverlay(parsed: ParsedStatus) {
        _statusOverlay.update { prev ->
            prev?.copy(
                revision = prev.revision + 1,
                title = parsed.title.orEmpty(),
                screenStatusMessage = parsed.screenStatusMessage,
                transactionStatus = parsed.transactionStatus,
                screenStatusTitle = parsed.screenStatusTitle
            )
        }
    }

    fun clearStatusOverlay() {
        _statusOverlay.value = null
    }

    fun onManagerResponse(action: String?, extras: Bundle?) {
        when (action) {
            EntryResponse.ACTION_ACCEPTED -> {
                _managerResponse.tryEmit(ManagerResponseEvent.Accepted)
            }
            EntryResponse.ACTION_DECLINED -> {
                val code = extras?.getInt(EntryResponse.PARAM_CODE) ?: -1
                val msg = extras?.getString(EntryResponse.PARAM_MSG)
                _managerResponse.tryEmit(ManagerResponseEvent.Declined(code, msg))
            }
        }
    }

    fun sendNext(bundle: Bundle?) {
        val s = _uiState.value
        val action = s.entryAction
        if (action == null) {
            Logger.w("EntryViewModel.sendNext skipped: no entryAction")
            return
        }
        EntryRequestUtils.sendNext(getApplication(), s.senderPackage, action, bundle)
    }

    /**
     * [com.pax.us.pay.ui.constant.entry.SecurityEntry.ACTION_ENTER_PIN]: notify BroadPOS that PIN UI is ready (no box coords).
     */
    fun sendSecurityAreaPinReady() {
        val s = _uiState.value
        val action = s.entryAction ?: return
        EntryRequestUtils.sendSecureArea(getApplication(), s.senderPackage, action)
    }

    /**
     * Reports secure input box bounds for non-PIN [com.pax.us.pay.ui.constant.entry.SecurityEntry] actions.
     */
    fun sendSecurityAreaBounds(x: Int, y: Int, width: Int, height: Int, hint: String?, colorArgb: String?) {
        val s = _uiState.value
        val action = s.entryAction ?: return
        EntryRequestUtils.sendSecureArea(
            getApplication(),
            s.senderPackage,
            action,
            SecureAreaBoundsPayload(
                x = x,
                y = y,
                width = width,
                height = height,
                fontSize = 14,
                hint = hint,
                fontColor = colorArgb
            )
        )
    }

    fun sendAbort() {
        val s = _uiState.value
        EntryRequestUtils.sendAbort(getApplication(), s.senderPackage, s.entryAction)
    }

    /**
     * Hardware / navigation key forwarded from [com.paxus.pay.poslinkui.demo.entry.EntryActivity]; collect in UI to mirror Fragment key listeners.
     */
    private val _keyEvents = MutableSharedFlow<Int>(extraBufferCapacity = 8)
    val keyEvents: SharedFlow<Int> = _keyEvents.asSharedFlow()

    fun submitKeyEvent(keyCode: Int) {
        viewModelScope.launch { _keyEvents.emit(keyCode) }
    }
}
