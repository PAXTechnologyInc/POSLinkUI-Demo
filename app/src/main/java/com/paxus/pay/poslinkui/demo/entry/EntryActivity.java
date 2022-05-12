package com.paxus.pay.poslinkui.demo.entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.EntryResponseEvent;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import org.greenrobot.eventbus.EventBus;

/**
 * Use fragment to implement all UI (Activity and Dialog).
 */
public class EntryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private View fragmentContainer;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Logger.d("EntryActivity onCreate");

        fragmentContainer = findViewById(R.id.fragment_placeholder);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        registerUIReceiver();

        loadEntry(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d("EntryActivity onNewIntent");
        //If activity is at the top of stack, startActivity will trigger onNewIntent.
        //So you can load entry here
        loadEntry(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Logger.d("EntryActivity onSaveInstanceState");

        //If EntryActivity is not at the top of stack, a new EntryActivity will be created.
        //After that, the old one need kill itself.
        unregisterUIReceiver();
        this.finishAndRemoveTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Logger.d("EntryActivity onDestroy");
        unregisterUIReceiver();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Logger.d("EntryActivity onBackPressed");
        //Click KEY_BACK, trigger user abort
        EventBus.getDefault().post(new EntryAbortEvent());
    }

    private void loadEntry(Intent intent){
        Logger.i("start Entry Action \""+intent.getAction()+"\"");

        Fragment fragment = UIFragmentHelper.createFragment(intent);
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_placeholder);
        if(fragment != null) {
            if(frag == null) {
                //Show tool bar
                toolbar.setVisibility(View.VISIBLE);
                fragmentContainer.setVisibility(View.VISIBLE);
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_placeholder, fragment);
            ft.commit();
        }else {
            if(frag == null){
                //To show dialog like ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE, hide tool bar.
                toolbar.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.GONE);
            }
            DialogFragment dialogFragment = UIFragmentHelper.createDialogFragment(intent);
            if(dialogFragment != null) {
                dialogFragment.show(getSupportFragmentManager(), "EntryDialog");
            }else {
                Toast.makeText(this, "NOT FOUND:" + intent.getAction(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadStatus(Intent intent){
        String action = intent.getAction();
        Logger.i("receive Status Action \"" + action + "\"");
        if(InformationStatus.TRANS_COMPLETED.equals(action)){
            long code = intent.getLongExtra(StatusData.PARAM_CODE,0L);
            if(code == -3){//Transaction Cancelled
                finish();
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
        receiver = new POSLinkUIReceiver();
        IntentFilter filter = new IntentFilter();
        //----------------Entry Response-----------------
        filter.addAction(EntryResponse.ACTION_ACCEPTED);
        filter.addAction(EntryResponse.ACTION_DECLINED);

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
            if(EntryResponse.ACTION_ACCEPTED.equals(intent.getAction())){
                EventBus.getDefault().post(new EntryResponseEvent(intent.getAction()));
            }else if(EntryResponse.ACTION_DECLINED.equals(intent.getAction())){
                long resultCode = intent.getLongExtra(EntryResponse.PARAM_CODE,0);
                String message = intent.getStringExtra(EntryResponse.PARAM_MSG);
                EventBus.getDefault().post(new EntryResponseEvent(intent.getAction(),resultCode,message));
            }else{
                loadStatus(intent);
            }
        }
    }
}