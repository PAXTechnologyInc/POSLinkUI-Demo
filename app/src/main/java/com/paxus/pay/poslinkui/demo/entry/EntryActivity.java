package com.paxus.pay.poslinkui.demo.entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.pax.us.pay.ui.constant.status.BatchStatus;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
import com.pax.us.pay.ui.constant.status.Uncategory;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.EntryResponseEvent;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

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
    private View fragmentContainer;
    private BroadcastReceiver receiver;

    private String transType = "";
    private String transMode = "";

    BaseSharedViewModel baseSharedViewModel;

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
        baseSharedViewModel = new ViewModelProvider(this).get(BaseSharedViewModel.class);
        loadEntry(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d("EntryActivity onNewIntent");
        //If activity is at the top of stack, startActivity will trigger onNewIntent.
        //So you can load entry here
        //getViewModelStore().clear();

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
        //EventBus.getDefault().post(new EntryAbortEvent());
        baseSharedViewModel.setKeyCode(KeyEvent.KEYCODE_BACK);
    }

    /**
     * This is being used to communicate with the fragments on top of this activity.
     * BaseEntryFragment and BaseEntryDialogFragment subscribe to this.
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getAction() == KeyEvent.ACTION_DOWN){
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                //EventBus.getDefault().post(new EntryConfirmEvent());
                baseSharedViewModel.setKeyCode(KeyEvent.KEYCODE_ENTER);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void loadEntry(Intent intent){
        Logger.i("start Entry Action \"" + intent.getAction() + "\"");
        Bundle bundle = intent.getExtras();
        StringBuilder extras = new StringBuilder();
        for (String key : bundle.keySet()) {
            extras.append(key).append(":\"").append(bundle.get(key)).append("\",");
        }
        Logger.i("Action Extras:{" + extras + "}");

        updateTransMode(intent.getStringExtra(EntryExtraData.PARAM_TRANS_MODE));

        Fragment fragment = UIFragmentHelper.createFragment(intent);
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
        String action = intent.getAction();
        Logger.i("receive Status Action \"" + action + "\"");
        Bundle bundle = intent.getExtras();
        StringBuilder extras = new StringBuilder();
        for (String key : bundle.keySet()) {
            extras.append(key).append(":\"").append(bundle.get(key)).append("\",");
        }
        Logger.i("Action Extras:{" + extras + "}");

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
        if(!TextUtils.isEmpty(dialogTag)) {
            DialogFragment dialogFragment = UIFragmentHelper.createStatusDialogFragment(intent);
            if (dialogFragment != null) {
                UIFragmentHelper.showDialog(getSupportFragmentManager(), dialogFragment, dialogTag);
            } else {
                UIFragmentHelper.closeDialog(getSupportFragmentManager(),dialogTag);
            }
        }else {
            Logger.e("unsupported receive Status Action"+action);
        }
    }

    //1. Display water mask according to {@link EntryExtraData#PARAM_TRANS_MODE}
    private void updateTransMode(String transMode){
        if(!TextUtils.isEmpty(transMode) && !transMode.equals(this.transMode)){
            this.transMode = transMode;

            String mode = null;
            if(!TextUtils.isEmpty(transMode)){
                if(TransMode.DEMO.equals(transMode)){
                    mode = getString(R.string.demo_only);
                }else if(TransMode.TEST.equals(transMode)){
                    mode = getString(R.string.test_only);
                }else if(TransMode.TEST_AND_DEMO.equals(transMode)){
                    mode = getString(R.string.test_and_demo);
                }else {
                    mode = "";
                }
            }
            if(!TextUtils.isEmpty(mode)){
                ViewUtils.addWaterMarkView(this,mode);
            }else{
                ViewUtils.removeWaterMarkView(this);
            }
        }
    }

    //2. Display {@link EntryExtraData#PARAM_TRANS_TYPE} on navigation bar
    private void updateTransType(String transType){
        if(transType!= null && !transType.equals(this.transType)){
            this.transType = transType;

            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(transType);
            }
        }
    }


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