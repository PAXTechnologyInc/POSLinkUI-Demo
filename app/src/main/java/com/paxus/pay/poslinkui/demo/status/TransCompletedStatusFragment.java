package com.paxus.pay.poslinkui.demo.status;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pax.us.pay.ui.constant.status.StatusData;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;

public class TransCompletedStatusFragment extends StatusFragment {
    private long code, delay;

    private static final long TRANS_RESULT_CODE_FOR_INSTANT_TERMINATION = -3;

    public TransCompletedStatusFragment(Intent intent, Context context) {
        super(intent, context);
        this.code = getArguments().getLong(StatusData.PARAM_CODE);
        this.delay = getArguments().getLong(StatusData.PARAM_HOST_RESP_TIMEOUT, DURATION_DEFAULT);
    }

    public long getDelay(){
        return delay;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ((TextView) view.findViewById(R.id.status_title)).setTextColor(getResources().getColor(code != 0 ? R.color.fail : R.color.success));
        return view;
    }

    @Override
    public boolean isImmediateTerminationNeeded() {
        return this.message == null || this.message.isEmpty() || this.code == TRANS_RESULT_CODE_FOR_INSTANT_TERMINATION;
    }
}