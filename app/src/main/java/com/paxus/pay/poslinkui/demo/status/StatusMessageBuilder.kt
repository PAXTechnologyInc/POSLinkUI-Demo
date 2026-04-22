package com.paxus.pay.poslinkui.demo.status

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.enumeration.SFType
import com.pax.us.pay.ui.constant.status.BatchStatus
import com.pax.us.pay.ui.constant.status.CardStatus
import com.pax.us.pay.ui.constant.status.ClssLightStatus
import com.pax.us.pay.ui.constant.status.InformationStatus
import com.pax.us.pay.ui.constant.status.LanguageStatus
import com.pax.us.pay.ui.constant.status.PINStatus
import com.pax.us.pay.ui.constant.status.SecurityStatus
import com.pax.us.pay.ui.constant.status.StatusData
import com.pax.us.pay.ui.constant.status.Uncategory
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.utils.Logger

/**
 * Pure Kotlin port of [StatusFragment] message + second-screen line selection (no View/Fragment).
 */
data class ParsedStatus(
    val title: String?,
    val screenStatusMessage: String,
    val transactionStatus: String,
    val screenStatusTitle: String
)

object StatusMessageBuilder {

    fun intentToBundle(intent: Intent): Bundle =
        Bundle().apply {
            putString(EntryRequest.PARAM_ACTION, intent.action)
            putAll(intent.extras ?: Bundle())
        }

    fun build(intent: Intent, context: Context): ParsedStatus =
        build(intentToBundle(intent), context)

    fun build(bundle: Bundle, context: Context): ParsedStatus {
        val transactionStatus = bundle.getString(EntryExtraData.PARAM_TRANS_STATUS, "").orEmpty()
        val screenStatusTitle = bundle.getString(StatusData.PARAM_MSG_PRIMARY)
            ?: bundle.getString("resultMessagePrimary")
            ?: ""
        var screenStatusMessage = context.getString(R.string.second_screen_please_wait)
        val action = bundle.getString(EntryRequest.PARAM_ACTION)
        var message: String? = ""
        when (action) {
            InformationStatus.TRANS_ONLINE_STARTED -> {
                message = context.getString(R.string.info_trans_online)
                screenStatusMessage = context.getString(R.string.second_screen_processing)
            }
            InformationStatus.EMV_TRANS_ONLINE_STARTED -> {
                message = context.getString(R.string.info_emv_trans_online)
                screenStatusMessage = context.getString(R.string.second_screen_processing)
            }
            InformationStatus.DCC_ONLINE_STARTED ->
                message = context.getString(R.string.info_dcc_online_start)
            InformationStatus.PINPAD_CONNECTION_STARTED ->
                message = context.getString(R.string.info_pin_pad_connection_start)
            InformationStatus.RKI_STARTED ->
                message = context.getString(R.string.info_rki_start)
            InformationStatus.ENTER_PIN_STARTED ->
                message = context.getString(R.string.please_flip_over)
            CardStatus.CARD_REMOVAL_REQUIRED -> {
                message = context.getString(R.string.please_remove_card)
                screenStatusMessage = context.getString(R.string.please_remove_card)
            }
            CardStatus.CARD_QUICK_REMOVAL_REQUIRED ->
                message = context.getString(R.string.please_remove_card_quickly)
            CardStatus.CARD_SWIPE_REQUIRED ->
                message = context.getString(R.string.please_swipe_card)
            CardStatus.CARD_INSERT_REQUIRED ->
                message = context.getString(R.string.please_insert_chip_card)
            CardStatus.CARD_TAP_REQUIRED ->
                message = context.getString(R.string.please_tap_card)
            CardStatus.CARD_PROCESS_STARTED ->
                message = context.getString(R.string.emv_process_start)
            Uncategory.PRINT_STARTED ->
                message = context.getString(R.string.print_process)
            Uncategory.FILE_UPDATE_STARTED ->
                message = context.getString(R.string.update_process)
            Uncategory.FCP_FILE_UPDATE_STARTED ->
                message = context.getString(R.string.check_for_update_start)
            Uncategory.CAPK_UPDATE_STARTED ->
                message = context.getString(R.string.download_emv_capk)
            Uncategory.LOG_UPLOAD_STARTED ->
                message = context.getString(R.string.log_uploading_start)
            Uncategory.LOG_UPLOAD_CONNECTED -> {
                val uploadPercent = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_PERCENT, 0L)
                message = context.getString(R.string.log_connected) + " (" + uploadPercent + "%)"
            }
            Uncategory.LOG_UPLOAD_UPLOADING -> {
                val logUploadCount = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_COUNT, 0L)
                val logTotalCount = bundle.getLong(StatusData.PARAM_UPLOAD_TOTAL_COUNT, 0L)
                val logUploadPercent = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_PERCENT, 0L)
                message = context.getString(R.string.update_process) + " " + logUploadCount + "/" +
                    logTotalCount + "(" + logUploadPercent + "%)"
            }
            BatchStatus.BATCH_CLOSE_UPLOADING -> {
                val edcType = bundle.getString(StatusData.PARAM_EDC_TYPE)
                val currentCount = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_COUNT, 0)
                val totalCount = bundle.getLong(StatusData.PARAM_UPLOAD_TOTAL_COUNT, 0)
                message = context.getString(R.string.uploading_trans) + " " + edcType + "\n" +
                    currentCount + "/" + totalCount
            }
            BatchStatus.BATCH_UPLOADING -> {
                val sfType = bundle.getString(StatusData.PARAM_SF_TYPE)
                val sfCurrentCount = bundle.getLong(StatusData.PARAM_SF_CURRENT_COUNT, 0)
                val sfTotalCount = bundle.getLong(StatusData.PARAM_SF_TOTAL_COUNT, 0)
                message = (if (SFType.FAILED == sfType) {
                    context.getString(R.string.uploading_failed_trans)
                } else {
                    context.getString(R.string.uploading_sf_trans)
                }) + "\n" + sfCurrentCount + " out of " + sfTotalCount
            }
            InformationStatus.TRANS_COMPLETED -> {
                message = bundle.getString(StatusData.PARAM_MSG)
            }
            else -> Logger.i("Status action: $action")
        }
        return ParsedStatus(
            title = message,
            screenStatusMessage = screenStatusMessage,
            transactionStatus = transactionStatus,
            screenStatusTitle = screenStatusTitle
        )
    }
}

object StatusDurations {
    const val DEFAULT_MS: Long = 5000L
    const val SHORT_MS: Long = 1000L
}

object StatusIntentPolicy {

    private val passiveActions: Set<String?> = buildSet {
        add(CardStatus.CARD_INSERTED)
        add(ClssLightStatus.CLSS_LIGHT_PROCESSING)
        add(PINStatus.PIN_ENTERING)
        add(SecurityStatus.SECURITY_ENTERING)
        add(LanguageStatus.SET_LANGUAGE)
    }

    private val conclusiveActions: Set<String?> = buildSet {
        add(CardStatus.CARD_REMOVED)
        add(CardStatus.CARD_PROCESS_COMPLETED)
        add(BatchStatus.BATCH_CLOSE_COMPLETED)
        add(BatchStatus.BATCH_SF_COMPLETED)
        add(InformationStatus.TRANS_ONLINE_FINISHED)
        add(InformationStatus.TRANS_REVERSAL_FINISHED)
        add(InformationStatus.PINPAD_CONNECTION_FINISHED)
        add(InformationStatus.EMV_TRANS_ONLINE_FINISHED)
        add(InformationStatus.DCC_ONLINE_FINISHED)
        add(InformationStatus.RKI_FINISHED)
        add(InformationStatus.ENTER_PIN_FINISHED)
        add(Uncategory.ACTIVATE_COMPLETED)
        add(Uncategory.CAPK_UPDATE_COMPLETED)
        add(Uncategory.PRINT_COMPLETED)
        add(Uncategory.FILE_UPDATE_COMPLETED)
        add(Uncategory.LOG_UPLOAD_COMPLETED)
        add(Uncategory.FCP_FILE_UPDATE_COMPLETED)
    }

    fun isPassive(action: String?): Boolean = action != null && passiveActions.contains(action)

    fun isConclusive(action: String?): Boolean = action != null && conclusiveActions.contains(action)

    /** Same as [TransCompletedStatusFragment.isImmediateTerminationNeeded]. */
    fun isTransCompletedImmediateAbort(action: String?, message: String?): Boolean =
        action == InformationStatus.TRANS_COMPLETED && message.isNullOrEmpty()
}
