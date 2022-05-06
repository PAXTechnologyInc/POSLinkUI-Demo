package com.paxus.pay.poslinkui.demo.entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
import com.paxus.pay.poslinkui.demo.event.EntryResponseEvent;
import com.paxus.pay.poslinkui.demo.event.InformationStatusEvent;
import com.paxus.pay.poslinkui.demo.event.TransCompletedEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Using event bus could make code clean.
 */
public class POSLinkUIReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("POSLinkUIReceiver","Action: "+intent.getAction());
        switch (intent.getAction()){
            case EntryResponse.ACTION_ACCEPTED:
                EventBus.getDefault().post(new EntryResponseEvent(intent.getAction()));
                break;
            case EntryResponse.ACTION_DECLINED: {
                long resultCode = intent.getLongExtra(EntryResponse.PARAM_CODE,0);
                String message = intent.getStringExtra(EntryResponse.PARAM_MSG);
                EventBus.getDefault().post(new EntryResponseEvent(intent.getAction(),resultCode,message));
                break;
            }
            case InformationStatus.TRANS_COMPLETED:{
                long resultCode = intent.getLongExtra(StatusData.PARAM_CODE,0);
                String message = intent.getStringExtra(StatusData.PARAM_MSG);
                long timeout = intent.getLongExtra(StatusData.PARAM_HOST_RESP_TIMEOUT,2000);

                EventBus.getDefault().post(new TransCompletedEvent(resultCode,message,timeout));
                break;
            }

            case InformationStatus.TRANS_ONLINE_STARTED:
            case InformationStatus.TRANS_ONLINE_FINISHED:
            case InformationStatus.EMV_TRANS_ONLINE_STARTED:
            case InformationStatus.EMV_TRANS_ONLINE_FINISHED:
            case InformationStatus.DCC_ONLINE_STARTED:
            case InformationStatus.DCC_ONLINE_FINISHED:
            case InformationStatus.PINPAD_CONNECTION_STARTED:
            case InformationStatus.PINPAD_CONNECTION_FINISHED:
            case InformationStatus.RKI_STARTED:
            case InformationStatus.RKI_FINISHED:
            case CardStatus.CARD_REMOVAL_REQUIRED:
            case CardStatus.CARD_REMOVED:
            case CardStatus.CARD_PROCESS_STARTED:
            case CardStatus.CARD_PROCESS_COMPLETED:
                EventBus.getDefault().post(new InformationStatusEvent(intent.getAction()));
                break;
        }

    }
}
