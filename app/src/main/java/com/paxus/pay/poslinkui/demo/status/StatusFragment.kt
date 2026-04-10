package com.paxus.pay.poslinkui.demo.status

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.enumeration.SFType
import com.pax.us.pay.ui.constant.status.BatchStatus
import com.pax.us.pay.ui.constant.status.CardStatus
import com.pax.us.pay.ui.constant.status.InformationStatus
import com.pax.us.pay.ui.constant.status.StatusData
import com.pax.us.pay.ui.constant.status.Uncategory
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel

open class StatusFragment : Fragment {
    protected var intent: Intent? = null
    protected var message: String? = null

    private var viewModel: SecondScreenInfoViewModel? = null
    private var transactionStatus: String? = null
    private var screenStatusMessage: String? = null
    private var screenStatusTitle: String? = null

    // Default constructor
    constructor()

    constructor(intent: Intent, resContext: Context) {
        this.intent = intent
        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, intent.action)
        intent.extras?.let { bundle.putAll(it) }
        arguments = bundle
        message = generateStatusMessage(bundle, resContext)
        transactionStatus = bundle.getString(EntryExtraData.PARAM_TRANS_STATUS, "")
        screenStatusTitle = bundle.getString(StatusData.PARAM_MSG_PRIMARY, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_status, container, false)

        val titleTextView = view.findViewById<TextView>(R.id.status_title)
        titleTextView.text = message
        val vm = ViewModelProvider(requireActivity())[SecondScreenInfoViewModel::class.java]
        viewModel = vm
        if (transactionStatus.isNullOrEmpty()) {
            vm.updateAllData("", screenStatusMessage, "", null, "", "")
        } else {
            vm.updateAllData("", "", transactionStatus, null, screenStatusTitle, "")
        }
        return view
    }

    fun isConclusive(): Boolean {
        val action = intent?.action ?: return false
        return conclusiveStatusSet.contains(action)
    }

    private val conclusiveStatusSet: Set<String?> = buildSet {
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

    private fun generateStatusMessage(bundle: Bundle, resContext: Context): String? {
        val action = bundle.getString(EntryRequest.PARAM_ACTION)
        var message: String? = ""
        screenStatusMessage = resContext.getString(R.string.second_screen_please_wait)
        when (action) {
            InformationStatus.TRANS_ONLINE_STARTED -> {
                message = resContext.getString(R.string.info_trans_online)
                screenStatusMessage = resContext.getString(R.string.second_screen_processing)
            }

            InformationStatus.EMV_TRANS_ONLINE_STARTED -> {
                message = resContext.getString(R.string.info_emv_trans_online)
                screenStatusMessage = resContext.getString(R.string.second_screen_processing)
            }

            InformationStatus.DCC_ONLINE_STARTED ->
                message = resContext.getString(R.string.info_dcc_online_start)

            InformationStatus.PINPAD_CONNECTION_STARTED ->
                message = resContext.getString(R.string.info_pin_pad_connection_start)

            InformationStatus.RKI_STARTED ->
                message = resContext.getString(R.string.info_rki_start)

            InformationStatus.ENTER_PIN_STARTED ->
                message = resContext.getString(R.string.please_flip_over)

            CardStatus.CARD_REMOVAL_REQUIRED -> {
                message = resContext.getString(R.string.please_remove_card)
                screenStatusMessage = resContext.getString(R.string.please_remove_card)
            }

            CardStatus.CARD_QUICK_REMOVAL_REQUIRED ->
                message = resContext.getString(R.string.please_remove_card_quickly)

            CardStatus.CARD_SWIPE_REQUIRED ->
                message = resContext.getString(R.string.please_swipe_card)

            CardStatus.CARD_INSERT_REQUIRED ->
                message = resContext.getString(R.string.please_insert_chip_card)

            CardStatus.CARD_TAP_REQUIRED ->
                message = resContext.getString(R.string.please_tap_card)

            CardStatus.CARD_PROCESS_STARTED ->
                message = resContext.getString(R.string.emv_process_start)

            Uncategory.PRINT_STARTED ->
                message = resContext.getString(R.string.print_process)

            Uncategory.FILE_UPDATE_STARTED ->
                message = resContext.getString(R.string.update_process)

            Uncategory.FCP_FILE_UPDATE_STARTED ->
                message = resContext.getString(R.string.check_for_update_start)

            Uncategory.CAPK_UPDATE_STARTED ->
                message = resContext.getString(R.string.download_emv_capk)

            Uncategory.LOG_UPLOAD_STARTED ->
                message = resContext.getString(R.string.log_uploading_start)

            Uncategory.LOG_UPLOAD_CONNECTED -> {
                val uploadPercent = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_PERCENT, 0L)
                message = resContext.getString(R.string.log_connected) + " (" + uploadPercent + "%)"
            }

            Uncategory.LOG_UPLOAD_UPLOADING -> {
                val logUploadCount = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_COUNT, 0L)
                val logTotalCount = bundle.getLong(StatusData.PARAM_UPLOAD_TOTAL_COUNT, 0L)
                val logUploadPercent = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_PERCENT, 0L)
                message = resContext.getString(R.string.update_process) + " " + logUploadCount + "/" +
                    logTotalCount + "(" + logUploadPercent + "%)"
            }

            BatchStatus.BATCH_CLOSE_UPLOADING -> {
                val edcType = bundle.getString(StatusData.PARAM_EDC_TYPE)
                val currentCount = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_COUNT, 0)
                val totalCount = bundle.getLong(StatusData.PARAM_UPLOAD_TOTAL_COUNT, 0)
                message = resContext.getString(R.string.uploading_trans) + " " + edcType + "\n" +
                    currentCount + "/" + totalCount
            }

            BatchStatus.BATCH_UPLOADING -> {
                val sfType = bundle.getString(StatusData.PARAM_SF_TYPE)
                val sfCurrentCount = bundle.getLong(StatusData.PARAM_SF_CURRENT_COUNT, 0)
                val sfTotalCount = bundle.getLong(StatusData.PARAM_SF_TOTAL_COUNT, 0)
                val base = if (SFType.FAILED == sfType) {
                    resContext.getString(R.string.uploading_failed_trans)
                } else {
                    resContext.getString(R.string.uploading_sf_trans)
                }
                message = base + "\n" + sfCurrentCount + " out of " + sfTotalCount
            }

            InformationStatus.TRANS_COMPLETED -> {
                message = bundle.getString(StatusData.PARAM_MSG)
            }

            else -> Logger.i("Status action: $action")
        }

        return message
    }

    open fun isImmediateTerminationNeeded(): Boolean {
        return false
    }

    fun sameAs(another: StatusFragment?): Boolean {
        if (another == null) return false
        return this.intent?.action != null && this.intent?.action == another.intent?.action
    }

    fun updateStatusMessage(intent: Intent, resContext: Context) {
        this.intent = intent
        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, intent.action)
        intent.extras?.let { bundle.putAll(it) }

        this.message = generateStatusMessage(bundle, resContext)
        transactionStatus = bundle.getString(EntryExtraData.PARAM_TRANS_STATUS, "")
        screenStatusTitle = bundle.getString(StatusData.PARAM_MSG_PRIMARY, "")
        val vm = viewModel ?: return
        if (transactionStatus.isNullOrEmpty()) {
            vm.updateAllData("", screenStatusMessage, "", null, "", "")
        } else {
            vm.updateAllData("", "", transactionStatus, null, screenStatusTitle, "")
        }
        val titleView = view?.findViewById<TextView>(R.id.status_title) ?: return
        titleView.text = message
    }

    companion object {
        const val DURATION_DEFAULT: Long = 5000
        const val DURATION_SHORT: Long = 1000
    }
}