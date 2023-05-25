package com.paxus.pay.poslinkui.demo.entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

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

    private Toolbar toolbar;
    private FragmentContainerView statusFragmentContainer;
    private FragmentContainerView entryFragmentContainer;

    private StatusBroadcastReceiver statusBroadcastReceiver;
    private ResponseBroadcastReceiver responseBroadcastReceiver;

    private String transType = "";
    private String transMode = "";

    TaskScheduler scheduler;
    InterfaceHistory interfaceHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.d( getClass().getSimpleName() + " onCreate");
        super.onCreate(savedInstanceState);

        interfaceHistory = new InterfaceHistory();

        setContentView(R.layout.activity_entry);
        toolbar = findViewById(R.id.toolbar);
        statusFragmentContainer = findViewById(R.id.status_container);
        entryFragmentContainer = findViewById(R.id.fragment_placeholder);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

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
        logIntent(intent);
        interfaceHistory.add(intent.getStringExtra("interfaceID"), intent.getAction());
        getIntent().setAction(intent.getAction());
        clearStatus();
        setScheduledTaskListener(intent);
        enableDarkOverlay(false);

        updateTransMode(intent.getStringExtra(EntryExtraData.PARAM_TRANS_MODE));

        Fragment fragment = UIFragmentHelper.createFragment(intent);
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_placeholder);

        if (fragment != null) {
            if (fragment instanceof DialogFragment) {
                if (frag == null) {
                    //To show dialog like ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE, hide tool bar.
                    toolbar.setVisibility(View.GONE);
                    entryFragmentContainer.setVisibility(View.GONE);
                }
                ((DialogFragment) fragment).show(getSupportFragmentManager(), "EntryDialog");
            } else {
                UIFragmentHelper.closeDialog(getSupportFragmentManager(), "EntryDialog");
                updateTransType(intent.getStringExtra(EntryExtraData.PARAM_TRANS_TYPE));
                if (frag == null) {
                    //Show tool bar
                    toolbar.setVisibility(View.VISIBLE);
                    entryFragmentContainer.setVisibility(View.VISIBLE);
                }
                getSupportFragmentManager().executePendingTransactions();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, fragment).commit();
            }
        } else {
            Toast.makeText(this, "NOT FOUND:" + intent.getAction(), Toast.LENGTH_SHORT).show();
        }
    }

    private void logIntent(Intent intent) {
        StringBuilder intentBuilder = new StringBuilder(getClass().getSimpleName() + " receives " + intent.getAction() + "\n");
        if(intent.getExtras() != null){
            for(String key : intent.getExtras().keySet()){
                intentBuilder.append(key).append(":\t\t").append(intent.getExtras().get(key)).append("\n");
            }
        }
        Logger.i(intentBuilder.toString());
    }

    private void enableDarkOverlay(boolean show){
        View view = findViewById(R.id.entry_dark_overlap);
        if((view.getVisibility() == View.VISIBLE && show) || (view.getVisibility() == View.INVISIBLE && !show)) return;
        view.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(show ? 0 : 1, show ? 1 : 0);
        alphaAnimation.setDuration(100);
        view.startAnimation(alphaAnimation);
    }

    private void setScheduledTaskListener(Intent intent) {
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
            .commit();
    }

    private boolean isStatusPresent() {
        return getSupportFragmentManager().findFragmentById(R.id.status_container) != null;
    }

    public void loadStatus(Intent intent) {
        StatusFragment statusFragment = InformationStatus.TRANS_COMPLETED.equals(intent.getAction()) ? new TransCompletedStatusFragment(intent, this) : new StatusFragment(intent, this);
        getSupportFragmentManager().executePendingTransactions();

        if(statusFragment.isConclusive()){
            clearStatus();
        } else if (statusFragment.isImmediateTerminationNeeded()) {
            clearStatus();
            scheduler.cancelTasks();
            scheduler.schedule(TaskScheduler.TASK.FINISH, StatusFragment.DURATION_SHORT, System.currentTimeMillis());
        } else {
            if(statusFragment instanceof TransCompletedStatusFragment) {
                scheduler.schedule(TaskScheduler.TASK.FINISH, ((TransCompletedStatusFragment) statusFragment).getDelay(), System.currentTimeMillis());
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
//        Logger.d(getClass().getSimpleName() +" dispatches KeyEvent. Code: " + event.getKeyCode() + " Action: " + event.getAction());
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
            logIntent(intent);

            //Validation
            boolean isValid = interfaceHistory.validate(intent.getStringExtra("interfaceID"), intent.getStringExtra("originatingAction"));
            if(!isValid) return;

            //Acceptance needs to block the view
            if(EntryResponse.ACTION_ACCEPTED.equals(intent.getAction())) {
                enableDarkOverlay(true);
            }

            //For both acceptance and decline, forward response to BaseEntryFragment
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
            logIntent(intent);
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
            filter.addAction(BatchStatus.BATCH_SF_UPLOADING);
            filter.addAction(BatchStatus.BATCH_SF_COMPLETED);
            filter.addAction(BatchStatus.BATCH_CLOSE_UPLOADING);
            filter.addAction(BatchStatus.BATCH_CLOSE_COMPLETED);

            return filter;
        }
    }
}