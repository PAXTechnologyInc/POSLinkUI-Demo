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

import androidx.annotation.NonNull;
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
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import java.util.Map;

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

    private BroadcastReceiver receiver;

    private String transType = "";
    private String transMode = "";
    private String senderPackage = "";

    TaskScheduler scheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.d( getClass().getSimpleName() + " onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entry);
        toolbar = findViewById(R.id.toolbar);
        statusFragmentContainer = findViewById(R.id.status_container);
        entryFragmentContainer = findViewById(R.id.fragment_placeholder);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        registerUIReceiver();
        scheduler = new TaskScheduler(this);

        Logger.i(getClass().getSimpleName() + " receives " + getIntent().getAction() + "\n" + getIntent().getExtras().toString());
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Logger.d(getClass().getSimpleName() +" onSaveInstanceState");
        unregisterUIReceiver();
        scheduler.cancelTasks();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Logger.d(getClass().getSimpleName() +" onRestoreInstanceState");
        registerUIReceiver();
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
        getIntent().setAction(intent.getAction());
        clearStatus();
        senderPackage = intent.getStringExtra(EntryExtraData.PARAM_PACKAGE);
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
                intentBuilder.append(key + ":\t\t" + intent.getExtras().get(key) + "\n");
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
        getSupportFragmentManager().executePendingTransactions();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.scale_up, R.anim.scale_down)
                .remove(getSupportFragmentManager().findFragmentById(R.id.status_container))
                .commitNow();
    }

    private boolean isStatusPresent() {
        return getSupportFragmentManager().findFragmentById(R.id.status_container) != null;
    }

    public void loadStatus(Intent intent) {
        StatusFragment statusFragment = InformationStatus.TRANS_COMPLETED.equals(intent.getAction()) ? new TransCompletedStatusFragment(intent, this) : new StatusFragment(intent, this);
        getSupportFragmentManager().executePendingTransactions();

        if(statusFragment.isConclusive()){
            clearStatus();
        } else if (statusFragment.isTerminationNeeded()) {
            clearStatus();
            scheduler.cancelTasks();
            scheduler.schedule(TaskScheduler.TASK.FINISH, 1000, System.currentTimeMillis());
        } else {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(R.anim.scale_up, R.anim.scale_down)
                    .replace(R.id.status_container, statusFragment).commitNow();
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

        this.registerReceiver(receiver, filter);
    }
    
    private void unregisterUIReceiver(){
        if(receiver != null){
            this.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Logger.d(getClass().getSimpleName() +" dispatches KeyEvent. Code: " + event.getKeyCode() + " Action: " + event.getAction());
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
    public class POSLinkUIReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            logIntent(intent);

            if(EntryResponse.ACTION_ACCEPTED.equals(intent.getAction())){
                enableDarkOverlay(true);
                Bundle response = new BundleMaker().addString("action", EntryResponse.ACTION_ACCEPTED).get();
                getSupportFragmentManager().setFragmentResult("response", response);
            }else if(EntryResponse.ACTION_DECLINED.equals(intent.getAction())){
                Bundle response = new BundleMaker()
                        .addLong("code", intent.getLongExtra(EntryResponse.PARAM_CODE,0))
                        .addString("message", intent.getStringExtra(EntryResponse.PARAM_MSG))
                        .addString("action", EntryResponse.ACTION_DECLINED)
                        .get();
                getSupportFragmentManager().setFragmentResult("response", response);
            }else{
                loadStatus(intent);
            }
        }
    }
}