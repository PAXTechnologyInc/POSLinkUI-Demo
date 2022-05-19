package com.paxus.pay.poslinkui.demo.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
import com.pax.us.pay.ui.constant.status.Uncategory;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.UIFragmentHelper;
import com.paxus.pay.poslinkui.demo.utils.Logger;

/**
 * Created by Yanina.Yang on 5/18/2022.
 */
public class StatusActivity  extends AppCompatActivity {
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Logger.d("StatusActivity onCreate");

        registerUIReceiver();

        loadStatus(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d("StatusActivity onNewIntent");
        //If activity is at the top of stack, startActivity will trigger onNewIntent.
        //So you can load entry here
        loadStatus(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Logger.d("StatusActivity onSaveInstanceState");

        //If EntryActivity is not at the top of stack, a new EntryActivity will be created.
        //After that, the old one need kill itself.
        unregisterUIReceiver();
        this.finishAndRemoveTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Logger.d("StatusActivity onDestroy");
        unregisterUIReceiver();
    }

    public void loadStatus(Intent intent){
        String action = intent.getAction();
        Logger.i("receive Status Action \"" + action + "\"");
        if(InformationStatus.TRANS_COMPLETED.equals(action)){
            long code = intent.getLongExtra(StatusData.PARAM_CODE,0L);
            if(code == -3){//Transaction Cancelled
                finishAndRemoveTask();
                return;
            }
        }
        String dialogTag = UIFragmentHelper.createStatusDialogTag(action);
        if(!TextUtils.isEmpty(dialogTag)) {
            DialogFragment dialogFragment = UIFragmentHelper.createDialogFragment(intent);
            if (dialogFragment != null) {
                UIFragmentHelper.showDialog(getSupportFragmentManager(), dialogFragment, dialogTag);
            } else {
                UIFragmentHelper.closeDialog(getSupportFragmentManager(),dialogTag);
            }
        }else {
            Logger.e("unsupported receive Status Action"+action);
        }
    }

    private void registerUIReceiver(){
        receiver = new StatusActivity.POSLinkUIReceiver();
        IntentFilter filter = new IntentFilter();

        //-----------------Uncategory Status-----------------
        filter.addAction(Uncategory.PRINT_STARTED);
        filter.addAction(Uncategory.PRINT_COMPLETED);
        filter.addAction(Uncategory.FILE_UPDATE_STARTED);
        filter.addAction(Uncategory.FILE_UPDATE_COMPLETED);
        filter.addAction(Uncategory.FCP_FILE_UPDATE_STARTED);
        filter.addAction(Uncategory.FCP_FILE_UPDATE_COMPLETED);
        filter.addAction(Uncategory.CAPK_UPDATE_STARTED);
        filter.addAction(Uncategory.CAPK_UPDATE_COMPLETED);
        filter.addAction(Uncategory.LOG_UPLOAD_STARTED);
        filter.addAction(Uncategory.LOG_UPLOAD_CONNECTED);
        filter.addAction(Uncategory.LOG_UPLOAD_UPLOADING);
        filter.addAction(Uncategory.LOG_UPLOAD_COMPLETED);

        //----------------Information Status-----------------
        filter.addCategory(InformationStatus.CATEGORY);
        filter.addAction(InformationStatus.DCC_ONLINE_STARTED);
        filter.addAction(InformationStatus.DCC_ONLINE_FINISHED);
        filter.addAction(InformationStatus.EMV_TRANS_ONLINE_STARTED);
        filter.addAction(InformationStatus.EMV_TRANS_ONLINE_FINISHED);
        filter.addAction(InformationStatus.TRANS_ONLINE_STARTED);
        filter.addAction(InformationStatus.TRANS_ONLINE_FINISHED);
        filter.addAction(InformationStatus.RKI_STARTED);
        filter.addAction(InformationStatus.RKI_FINISHED);
        filter.addAction(InformationStatus.PINPAD_CONNECTION_STARTED);
        filter.addAction(InformationStatus.PINPAD_CONNECTION_FINISHED);
        filter.addAction(InformationStatus.TRANS_COMPLETED);
        filter.addAction(InformationStatus.ERROR);

        //----------------Card Status-----------------
        filter.addCategory(CardStatus.CATEGORY);
        filter.addAction(CardStatus.CARD_REMOVED);
        filter.addAction(CardStatus.CARD_REMOVAL_REQUIRED);
        filter.addAction(CardStatus.CARD_QUICK_REMOVAL_REQUIRED);
        filter.addAction(CardStatus.CARD_PROCESS_STARTED);
        filter.addAction(CardStatus.CARD_PROCESS_COMPLETED);

        this.registerReceiver(receiver, filter);
    }

    private void unregisterUIReceiver(){
        if(receiver != null){
            this.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public class POSLinkUIReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadStatus(intent);
        }
    }
}
