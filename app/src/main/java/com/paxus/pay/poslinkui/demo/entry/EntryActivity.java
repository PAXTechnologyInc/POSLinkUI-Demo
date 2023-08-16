package com.paxus.pay.poslinkui.demo.entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.pax.us.pay.ui.constant.status.BatchStatus;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
import com.pax.us.pay.ui.constant.status.Uncategory;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.event.ResponseEvent;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

/**
 * Use fragment to implement all UI (Activity and Dialog).
 * <p>
 *     UI Tips:
 *     1. Display water mask according to {@link EntryExtraData#PARAM_TRANS_MODE}
 *     2. Display {@link EntryExtraData#PARAM_TRANS_TYPE} on navigation bar
 * </p>
 */
public class EntryActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private FragmentContainerView fragmentContainer;
    private BroadcastReceiver receiver;

    private String transType = "";
    private String transMode = "";

    EntryViewModelFactory entryViewModelFactory = new EntryViewModelFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.d("EntryActivity onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entry);
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
        Logger.d(getClass().getSimpleName() +" onNewIntent");
        //If activity is at the top of stack, startActivity will trigger onNewIntent. So you can load entry here.
        loadEntry(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Logger.d(getClass().getSimpleName() +" onSaveInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(getClass().getSimpleName() +" onDestroy");
        unregisterUIReceiver();
        getViewModelStore().clear();
    }

    private void logIntentExtras(Intent intent){
        Bundle bundle = intent.getExtras() != null ? intent.getExtras() : new Bundle();
        StringBuilder extras = new StringBuilder();
        for (String key : bundle.keySet()) {
            extras.append(key).append(":\"").append(bundle.get(key)).append("\",");
        }
        Logger.i("Action Extras:{" + extras + "}");
    }

    private void loadEntry(Intent intent){
        logIntentExtras(intent);
        Logger.i("Start Entry Action \"" + intent.getAction() + "\"");

        updateTransMode(intent.getStringExtra(EntryExtraData.PARAM_TRANS_MODE));

        Fragment fragment = UIFragmentHelper.createFragment(intent);
        entryViewModelFactory.createViewModel(this, fragment);
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_placeholder);

        if (fragment != null) {
            if (fragment instanceof DialogFragment) {
                if (frag == null) {
                    //To show dialog like ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE, hide tool bar.
                    toolbar.setVisibility(View.GONE);
                    fragmentContainer.setVisibility(View.GONE);
                }
                ((DialogFragment) fragment).show(getSupportFragmentManager(), "EntryDialog");
            } else {
                UIFragmentHelper.closeDialog(getSupportFragmentManager(), "EntryDialog");
                updateTransType(intent.getStringExtra(EntryExtraData.PARAM_TRANS_TYPE));
                if (frag == null) {
                    //Show tool bar
                    toolbar.setVisibility(View.VISIBLE);
                    fragmentContainer.setVisibility(View.VISIBLE);
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder, fragment);
                ft.commit();
            }
        } else {
            Toast.makeText(this, "NOT FOUND:" + intent.getAction(), Toast.LENGTH_SHORT).show();
        }
    }

    public void loadStatus(Intent intent) {
        logIntentExtras(intent);
        Logger.i("Receive Status Action \"" + intent.getAction() + "\"");

        try {
            String action = intent.getAction();
            if (InformationStatus.TRANS_COMPLETED.equals(action)) {
                String msg = intent.getStringExtra(StatusData.PARAM_MSG); //For POSLinkEntry, msg might be empty
                long code = intent.getLongExtra(StatusData.PARAM_CODE, 0L);
                if (TextUtils.isEmpty(msg) || code == -3) {//Transaction Cancelled
                    finishAndRemoveTask();
                    return;
                }
                //Close Entry Dialog before prompt Trans Complete Dialog
                UIFragmentHelper.closeDialog(getSupportFragmentManager(), "EntryDialog");
            }
            String dialogTag = UIFragmentHelper.createStatusDialogTag(action);
            if (!TextUtils.isEmpty(dialogTag)) {
                DialogFragment dialogFragment = UIFragmentHelper.createStatusDialogFragment(intent);
                if (dialogFragment != null) {
                    UIFragmentHelper.showDialog(getSupportFragmentManager(), dialogFragment, dialogTag);
                } else {
                    UIFragmentHelper.closeDialog(getSupportFragmentManager(), dialogTag);
                }
            } else {
                Logger.e("unsupported receive Status Action" + action);
            }
        }catch (IllegalStateException e){
            //To avoid crash
            Logger.e(e);
        }
    }

    /**
     * Add/Remove Watermark based on Trans Mode
     */
    private void updateTransMode(String transMode){
        if(!TextUtils.isEmpty(transMode) && !transMode.equals(this.transMode)){
            this.transMode = transMode;

            String mode = null;
            switch (transMode) {
                case TransMode.DEMO:
                    mode = getString(R.string.demo_only); break;
                case TransMode.TEST:
                    mode = getString(R.string.test_only); break;
                case TransMode.TEST_AND_DEMO:
                    mode = getString(R.string.test_and_demo); break;
            }

            if(!TextUtils.isEmpty(mode)) {
                ViewUtils.addWaterMarkView(this,mode);
            } else {
                ViewUtils.removeWaterMarkView(this);
            }
        }
    }

    /**
     * Update Transaction Type on Title Bar
     */
    private void updateTransType(String transType){
        if(transType!= null && !transType.equals(this.transType)){
            this.transType = transType;

            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(transType);
            }
        }
    }


    /**
     * Broadcast Receiver to receive status updates from BroadPOS Manager
     * void registerUIReceiver()
     * void unregisterUIReceiver()
     * class POSLinkUIReceiver
     */

    private void registerUIReceiver(){
        receiver = new POSLinkUIReceiver();
        IntentFilter filter = new IntentFilter();
        //----------------Entry Response-----------------
        filter.addAction(EntryResponse.ACTION_ACCEPTED);
        filter.addAction(EntryResponse.ACTION_DECLINED);

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

        //----------------Batch Status-----------------
        filter.addCategory(BatchStatus.CATEGORY);
        filter.addAction(BatchStatus.BATCH_SF_UPLOADING);
        filter.addAction(BatchStatus.BATCH_SF_COMPLETED);
        filter.addAction(BatchStatus.BATCH_CLOSE_UPLOADING);
        filter.addAction(BatchStatus.BATCH_CLOSE_COMPLETED);

        this.registerReceiver(receiver, filter);
    }
    
    private void unregisterUIReceiver(){
        if(receiver != null){
            this.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    /**
     * Forwards key events to fragments using ViewModel.
     * For BaseEntryFragment and BaseEntryDialogFragment, two different view models have been used
     * EntryViewModelFactory chooses the appropriate one
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Logger.d(getClass().getSimpleName() +" dispatches KeyEvent. Code: " + event.getKeyCode() + " Action: " + event.getAction());
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                entryViewModelFactory.onKeyDown(event.getKeyCode());
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    /**
     * Forwards Manager's broadcasts to fragments
     */
    public class POSLinkUIReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(EntryResponse.ACTION_ACCEPTED.equals(intent.getAction())){
                entryViewModelFactory.setResponseEvent(new ResponseEvent(intent.getAction()));
            }else if(EntryResponse.ACTION_DECLINED.equals(intent.getAction())){
                long resultCode = intent.getLongExtra(EntryResponse.PARAM_CODE,0);
                String message = intent.getStringExtra(EntryResponse.PARAM_MSG);
                entryViewModelFactory.setResponseEvent(new ResponseEvent(intent.getAction(), resultCode, message));
            }else{
                loadStatus(intent);
            }
        }
    }
}