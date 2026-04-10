package com.paxus.pay.poslinkui.demo.status

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.pax.us.pay.ui.constant.status.StatusData
import com.paxus.pay.poslinkui.demo.R

class TransCompletedStatusFragment(intent: Intent, resContext: Context) :
    StatusFragment(intent, resContext) {
    private val code: Long
    val delay: Long

    init {
        val args = requireArguments()
        this.code = args.getLong(StatusData.PARAM_CODE)
        this.delay = args.getLong(
            StatusData.PARAM_HOST_RESP_TIMEOUT,
            StatusFragment.DURATION_DEFAULT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val colorRes = if (code != 0L) R.color.fail else R.color.success
        view?.findViewById<TextView>(R.id.status_title)?.setTextColor(
            ContextCompat.getColor(requireContext(), colorRes)
        )
        return view
    }

    override fun isImmediateTerminationNeeded(): Boolean {
        // [BPOSANDJAX-1246] BroadPOS must show an ABORTED-style prompt when the transaction is canceled.
        return message.isNullOrEmpty()
    }
}
