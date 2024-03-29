package com.paxus.pay.poslinkui.demo.entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryResponse;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.pax.us.pay.ui.constant.status.BatchStatus;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.Uncategory;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.status.StatusFragment;
import com.paxus.pay.poslinkui.demo.status.TransCompletedStatusFragment;
import com.paxus.pay.poslinkui.demo.utils.BundleMaker;
import com.paxus.pay.poslinkui.demo.utils.EntryActivityActionBar;
import com.paxus.pay.poslinkui.demo.utils.InterfaceHistory;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import java.util.Objects;

/**
 * Use fragment to implement all UI (Activity and Dialog).
 * <p>
 *     UI Tips:
 *     1. Display water mask according to {@link EntryExtraData#PARAM_TRANS_MODE}
 *     2. Display {@link EntryExtraData#PARAM_TRANS_TYPE} on navigation bar
 * </p>
 */
public class EntryActivity extends AppCompatActivity{

    private StatusBroadcastReceiver statusBroadcastReceiver;
    private ResponseBroadcastReceiver responseBroadcastReceiver;

    private String transType = "";
    private String transMode = "";

    TaskScheduler scheduler;
    InterfaceHistory interfaceHistory;

    EntryActivityActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.d( getClass().getSimpleName() + " onCreate");
        super.onCreate(savedInstanceState);
        interfaceHistory = new InterfaceHistory();

        setContentView(R.layout.activity_entry);
        actionBar = new EntryActivityActionBar(this, getSupportActionBar());

        registerUIReceiver();
        scheduler = new TaskScheduler(this);

        loadEntry(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Logger.d( getClass().getSimpleName() + " onNewIntent");
        super.onNewIntent(intent);
        loadEntry(intent);
        scheduler.cancelTasks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(getClass().getSimpleName() +" onDestroy");
        unregisterUIReceiver();
        scheduler.shutdown();
    }

    private void loadEntry(Intent intent){
        setIntent(intent);
        Logger.intent(intent, "ACTIVITY INTENT:\t" + intent.getAction());
        interfaceHistory.add(intent.getStringExtra("interfaceID"), intent.getAction());

        clearStatus();
        setScheduledTaskListener();

        updateTransMode(intent.getStringExtra(EntryExtraData.PARAM_TRANS_MODE));

        Fragment entryFragment = UIFragmentHelper.createFragment(intent);
        if (entryFragment != null) {
            updateTransType(intent.getStringExtra(EntryExtraData.PARAM_TRANS_TYPE));
            getSupportFragmentManager().executePendingTransactions();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left, R.anim.anim_enter_from_right, R.anim.anim_exit_to_left)
                    .replace(R.id.fragment_placeholder, entryFragment)
                    .commit();
        } else {
            Toast.makeText(this, "NOT FOUND:" + intent.getAction(), Toast.LENGTH_SHORT).show();
        }
    }


    private void setScheduledTaskListener() {
        //Used to schedule tasks requested by child fragments
        scheduler.cancelTasks();
        getSupportFragmentManager().setFragmentResultListener(TaskScheduler.SCHEDULE, this, (requestKey, result) -> {
            String taskType = result.getString(TaskScheduler.PARAM_TASK);
            long delay = result.getLong(TaskScheduler.PARAM_DELAY);
            long initTime = result.getLong(TaskScheduler.PARAM_INIT_TIME);
            scheduler.schedule(TaskScheduler.TASK.valueOf(taskType), delay, initTime);
        });
    }

    private void clearStatus() {
        if(!isStatusPresent()) return;
        getSupportFragmentManager().beginTransaction()
            .setReorderingAllowed(true)
            .setCustomAnimations(R.anim.anim_enter_from_bottom, R.anim.anim_exit_to_bottom)
            .remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.status_container)))
            .commitAllowingStateLoss(); // POSUI-243

    }

    private boolean isStatusPresent() {
        return getSupportFragmentManager().findFragmentById(R.id.status_container) != null;
    }

    public void loadStatus(Intent intent) {
        @NonNull
        StatusFragment statusFragment = InformationStatus.TRANS_COMPLETED.equals(intent.getAction())
                ? new TransCompletedStatusFragment(intent, this)
                : new StatusFragment(intent, this);
        getSupportFragmentManager().executePendingTransactions();

        if(statusFragment.isConclusive()){
            //clearStatus();
        } else if (statusFragment.isImmediateTerminationNeeded()) {
            clearStatus();
            scheduler.cancelTasks();
            scheduler.schedule(TaskScheduler.TASK.FINISH, StatusFragment.DURATION_SHORT, System.currentTimeMillis());
        } else {
            if(statusFragment instanceof TransCompletedStatusFragment) {
                scheduler.schedule(TaskScheduler.TASK.FINISH, ((TransCompletedStatusFragment) statusFragment).getDelay(), System.currentTimeMillis());
            }

            StatusFragment currentFragment = (StatusFragment) getSupportFragmentManager().findFragmentById(R.id.status_container);
            if(statusFragment.sameAs(currentFragment)){
                currentFragment.updateStatusMessage(intent, this);
                return;
            }

            if(!getSupportFragmentManager().isStateSaved()) {
                getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.anim_enter_from_bottom, R.anim.anim_exit_to_bottom)
                    .replace(R.id.status_container, statusFragment).commit();
            }
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
            actionBar.setTitle(transType);
        }
    }

    /**
     * Broadcast Receiver to receive status updates from BroadPOS Manager
     * void registerUIReceiver()
     * void unregisterUIReceiver()
     */
    private void registerUIReceiver(){
        statusBroadcastReceiver = new StatusBroadcastReceiver();
        this.registerReceiver(statusBroadcastReceiver, statusBroadcastReceiver.getFilter());

        responseBroadcastReceiver = new ResponseBroadcastReceiver();
        this.registerReceiver(responseBroadcastReceiver, responseBroadcastReceiver.getFilter());
    }
    
    private void unregisterUIReceiver(){
        if(statusBroadcastReceiver != null) {
            this.unregisterReceiver(statusBroadcastReceiver);
            statusBroadcastReceiver = null;
        }
        if(responseBroadcastReceiver != null) {
            this.unregisterReceiver(responseBroadcastReceiver);
            responseBroadcastReceiver = null;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(isStatusPresent()) return true;

        if( event.getAction() == KeyEvent.ACTION_UP &&
                (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_BACK) ){
            Bundle response = new Bundle();
            response.putInt("keyCode", event.getKeyCode());
            getSupportFragmentManager().setFragmentResult("keyCode", response);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * Forwards Manager's broadcasts to fragments
     */
    public class ResponseBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.intent(intent, "RESPONSE BROADCAST:\t" + intent.getAction());

            //Validation
            boolean isValid = interfaceHistory.validate(intent.getStringExtra("interfaceID"), intent.getStringExtra("originatingAction"));
            //if(!isValid) return; // Commented out until Manager supports interfaceID

            //Forward Response Broadcast to BaseEntryFragment
            Bundle responseBroadcastExtras = new BundleMaker()
                    .addString("action", intent.getAction())
                    .addBundle(intent.getExtras())
                    .get();
            getSupportFragmentManager().setFragmentResult("response", responseBroadcastExtras);
        }

        public IntentFilter getFilter(){
            IntentFilter filter = new IntentFilter();
            filter.addAction(EntryResponse.ACTION_ACCEPTED);
            filter.addAction(EntryResponse.ACTION_DECLINED);
            return filter;
        }
    }

    public class StatusBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.intent(intent, "STATUS BROADCAST:\t" + intent.getAction());
            loadStatus(intent);
        }

        public IntentFilter getFilter(){
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
            filter.addAction(InformationStatus.ENTER_PIN_STARTED);

            //----------------Card Status-----------------
            filter.addCategory(CardStatus.CATEGORY);
            filter.addAction(CardStatus.CARD_REMOVED);
            filter.addAction(CardStatus.CARD_REMOVAL_REQUIRED);
            filter.addAction(CardStatus.CARD_QUICK_REMOVAL_REQUIRED);
            filter.addAction(CardStatus.CARD_PROCESS_STARTED);
            filter.addAction(CardStatus.CARD_PROCESS_COMPLETED);

            //----------------Batch Status-----------------
            filter.addCategory(BatchStatus.CATEGORY);
            filter.addAction(BatchStatus.BATCH_UPLOADING);
            filter.addAction(BatchStatus.BATCH_SF_COMPLETED);
            filter.addAction(BatchStatus.BATCH_CLOSE_UPLOADING);
            filter.addAction(BatchStatus.BATCH_CLOSE_COMPLETED);

            return filter;
        }
    }
}