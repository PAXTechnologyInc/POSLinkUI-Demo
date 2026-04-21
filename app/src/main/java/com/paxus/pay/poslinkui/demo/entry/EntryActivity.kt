package com.paxus.pay.poslinkui.demo.entry

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import androidx.compose.runtime.CompositionLocalProvider
import kotlinx.coroutines.Job
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.PoslinkEntry
import com.pax.us.pay.ui.constant.entry.EntryResponse
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode
import com.pax.us.pay.ui.constant.status.StatusData
import com.pax.us.pay.ui.constant.status.BatchStatus
import com.pax.us.pay.ui.constant.status.CardStatus
import com.pax.us.pay.ui.constant.status.InformationStatus
import com.pax.us.pay.ui.constant.status.Uncategory
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.entry.navigation.EntryNavigationHost
import com.paxus.pay.poslinkui.demo.status.ParsedStatus
import com.paxus.pay.poslinkui.demo.status.StatusIntentPolicy
import com.paxus.pay.poslinkui.demo.status.StatusMessageBuilder
import com.paxus.pay.poslinkui.demo.status.StatusDurations
import com.paxus.pay.poslinkui.demo.ui.device.DeviceProfileRegistry
import com.paxus.pay.poslinkui.demo.ui.device.LocalDeviceLayoutSpec
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkTheme
import com.paxus.pay.poslinkui.demo.utils.BundleMaker
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.paxus.pay.poslinkui.demo.utils.DeviceUtils
import com.paxus.pay.poslinkui.demo.utils.EntryActivityActionBar
import com.paxus.pay.poslinkui.demo.utils.InterfaceHistory
import com.paxus.pay.poslinkui.demo.utils.Logger
import com.paxus.pay.poslinkui.demo.utils.TaskGroup
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler.TASK
import com.paxus.pay.poslinkui.demo.utils.ViewUtils
import com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel
import com.paxus.pay.poslinkui.demo.viewmodel.StatusOverlayUi
import com.paxus.pay.poslinkui.demo.viewmodel.StatusTitleTone
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Use fragment to implement all UI (Activity and Dialog).
 * 
 * 
 * UI Tips:
 * 1. Display water mask according to [EntryExtraData.PARAM_TRANS_MODE]
 * 2. Display [EntryExtraData.PARAM_TRANS_TYPE] on navigation bar
 * 
 */
@AndroidEntryPoint
class EntryActivity : AppCompatActivity() {
    private var statusBroadcastReceiver: StatusBroadcastReceiver? = null
    private var responseBroadcastReceiver: ResponseBroadcastReceiver? = null

    private var transType: String? = ""
    private var transMode = ""

    var scheduler: TaskScheduler? = null
    private var statusTaskGroup: MutableSet<Job>? = null
    @Inject
    lateinit var interfaceHistory: InterfaceHistory
    @Inject
    lateinit var taskGroup: TaskGroup
    @Inject
    lateinit var viewUtils: ViewUtils

    var actionBar: EntryActivityActionBar? = null

    private var presentation: TransactionPresentation? = null
    private var viewModel: SecondScreenInfoViewModel? = null
    private var entryViewModel: EntryViewModel? = null
    private var uiReceiversRegistered: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.d(javaClass.getSimpleName() + " onCreate")
        super.onCreate(savedInstanceState)
        val entryVm = ViewModelProvider(this)[EntryViewModel::class.java]
        entryViewModel = entryVm
        setContent {
            val deviceSpec = DeviceProfileRegistry.resolve(resources.configuration)
            PosLinkTheme {
                CompositionLocalProvider(LocalDeviceLayoutSpec provides deviceSpec) {
                    EntryNavigationHost(viewModel = entryVm)
                }
            }
        }
        supportActionBar?.let { actionBar = EntryActivityActionBar(this, it) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                entryVm.uiState.collect { state ->
                    actionBar?.setClssLightsVisible(EntryActionBarPolicy.shouldShowClssLights(state))
                }
            }
        }

        registerUIReceiver()
        scheduler = TaskScheduler(this, lifecycleScope)
        registerFragmentLifecycleCallback()

        // inti ViewModel
        viewModel =
            ViewModelProvider(this).get<SecondScreenInfoViewModel>(SecondScreenInfoViewModel::class.java)
        loadEntry(getIntent())
    }

    override fun onNewIntent(intent: Intent) {
        Logger.d(javaClass.getSimpleName() + " onNewIntent")
        super.onNewIntent(intent)
        loadEntry(intent)
        statusTaskGroup?.let { taskGroup.cancelGroup(it) }
        statusTaskGroup = null
        scheduler?.cancelTasks()
    }

    override fun onStop() {
        super.onStop()
        dismissPresentation()
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d(javaClass.getSimpleName() + " onDestroy")
        cleanupResources()
    }

    private fun cleanupResources() {
        unregisterUIReceiver()
        statusTaskGroup?.let { taskGroup.cancelGroup(it) }
        statusTaskGroup = null
        scheduler?.shutdown()
        dismissPresentation()
    }

    private fun loadEntry(intent: Intent) {
        val normalizedIntent = normalizeIntentForKnownCategory(intent)
        setIntent(normalizedIntent)
        val action = normalizedIntent.action ?: ""
        val isPoslinkShowMessage =
            action == PoslinkEntry.ACTION_SHOW_MESSAGE &&
                normalizedIntent.categories?.contains(PoslinkEntry.CATEGORY) == true
        Logger.intent(normalizedIntent, "ACTIVITY INTENT:\t$action")
        val interfaceId = normalizedIntent.getStringExtra("interfaceID") ?: ""
        interfaceHistory.add(interfaceId, action)

        clearStatus()
        setScheduledTaskListener()

        val transMode = if (isPoslinkShowMessage) {
            ""
        } else {
            normalizedIntent.getStringExtra(EntryExtraData.PARAM_TRANS_MODE) ?: ""
        }
        updateTransMode(transMode)

        if (EntryActionRegistry.canResolveEntry(normalizedIntent) ||
            normalizedIntent.getBooleanExtra(EXTRA_NAV_COMPOSE_DEMO, false)
        ) {
            // show second screen
            createPresentation(normalizedIntent)
            val transType = if (isPoslinkShowMessage) {
                null
            } else {
                normalizedIntent.getStringExtra(EntryExtraData.PARAM_TRANS_TYPE)
            }
            updateTransType(transType)
            entryViewModel?.consumeEntryIntent(normalizedIntent)
        } else {
            Toast.makeText(this, "NOT FOUND:" + normalizedIntent.action, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Some POSUI regression commands may launch POSLINK actions with only DEFAULT category.
     * Keep behavior local by inferring POSLINK category only for payloads that match known
     * SHOW_MESSAGE / INPUT_TEXT signatures.
     */
    private fun normalizeIntentForKnownCategory(intent: Intent): Intent {
        val categories = intent.categories.orEmpty()
        val hasBusinessCategory = categories.any { it.startsWith("com.pax.us.pay.ui.category.") }
        if (hasBusinessCategory) {
            return intent
        }
        when (intent.action) {
            PoslinkEntry.ACTION_INPUT_TEXT -> {
                if (hasPoslinkInputTextPayload(intent)) {
                    intent.addCategory(PoslinkEntry.CATEGORY)
                    Logger.i("Inferred POSLINK category for ACTION_INPUT_TEXT")
                }
            }
            PoslinkEntry.ACTION_SHOW_MESSAGE -> {
                if (hasShowMessageInferencePayload(intent)) {
                    intent.addCategory(PoslinkEntry.CATEGORY)
                    Logger.i("Inferred POSLINK category for ACTION_SHOW_MESSAGE")
                }
            }
            PoslinkEntry.ACTION_SHOW_SIGNATURE_BOX -> {
                if (hasShowSignatureBoxInferencePayload(intent)) {
                    intent.addCategory(PoslinkEntry.CATEGORY)
                    Logger.i("Inferred POSLINK category for ACTION_SHOW_SIGNATURE_BOX")
                }
            }
        }
        return intent
    }

    private fun hasPoslinkInputTextPayload(intent: Intent): Boolean =
        intent.hasExtra(EntryExtraData.PARAM_TITLE) &&
            intent.hasExtra(EntryExtraData.PARAM_MIN_LENGTH) &&
            intent.hasExtra(EntryExtraData.PARAM_MAX_LENGTH) &&
            intent.hasExtra(EntryExtraData.PARAM_INPUT_TYPE)

    private fun hasShowMessageInferencePayload(intent: Intent): Boolean {
        if (intent.hasExtra(EntryExtraData.PARAM_MESSAGE_LIST)) return true
        if (intent.hasExtra("PARAM_MESSAGE_LIST")) return true
        if (intent.hasExtra("messageList")) return true
        if (intent.hasExtra(EntryExtraData.PARAM_IMAGE_URL)) return true
        if (intent.hasExtra("PARAM_IMAGE_URL")) return true
        if (intent.hasExtra("imageURL")) return true
        if (intent.hasExtra(EntryExtraData.PARAM_IMAGE_DESC)) return true
        if (intent.hasExtra("PARAM_IMAGE_DESC")) return true
        if (intent.hasExtra("imageDesc")) return true
        return false
    }

    private fun hasShowSignatureBoxInferencePayload(intent: Intent): Boolean {
        if (intent.hasExtra(EntryExtraData.PARAM_SIGN_BOX)) return true
        if (intent.hasExtra("signBox")) return true
        if (intent.hasExtra(EntryExtraData.PARAM_TIMEOUT)) return true
        if (intent.hasExtra("timeout")) return true
        if (intent.hasExtra(EntryExtraData.PARAM_TITLE)) return true
        if (intent.hasExtra(EntryExtraData.PARAM_TEXT)) return true
        return false
    }

    private fun createPresentation(intent: Intent) {
        val secondDisplay = DeviceUtils.getSecondDisplay(this)
        if (secondDisplay != null) {
            val vm = viewModel ?: return
            vm.updateAllData(
                "", resources.getString(R.string.second_screen_please_wait), "", null, "", ""
            )
            val bundle = Bundle()
            bundle.putAll(intent.extras ?: Bundle())
            if (bundle.containsKey(EntryExtraData.PARAM_TRANS_AMOUNT)) {
                val amount = CurrencyUtils.convert(
                    bundle.getLong(EntryExtraData.PARAM_TRANS_AMOUNT),
                    bundle.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
                )
                vm.updateAmount(amount)
            }
            presentation = presentation ?: TransactionPresentation(this, secondDisplay, intent, vm).also { it.show() }
        }
    }

    private fun dismissPresentation() {
        presentation?.dismiss()
        presentation = null
    }


    private fun registerFragmentLifecycleCallback() {
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentDestroyed(fm: FragmentManager, fragment: Fragment) {
                    super.onFragmentDestroyed(fm, fragment)
                    val requestorId = "fragment_${System.identityHashCode(fragment)}"
                    scheduler?.cancelForRequestor(requestorId)
                }
            },
            false
        )
    }

    private fun setScheduledTaskListener() {
        //Used to schedule tasks requested by child fragments
        statusTaskGroup?.let { taskGroup.cancelGroup(it) }
        statusTaskGroup = null
        scheduler?.cancelTasks()
        supportFragmentManager.setFragmentResultListener(
            TaskScheduler.SCHEDULE,
            this,
            FragmentResultListener { _: String?, result: Bundle? ->
                val taskType = result?.getString(TaskScheduler.PARAM_TASK) ?: return@FragmentResultListener
                val delay = result.getLong(TaskScheduler.PARAM_DELAY)
                val initTime = result.getLong(TaskScheduler.PARAM_INIT_TIME)
                val requestorId = result.getString(TaskScheduler.PARAM_REQUESTOR_ID) ?: "activity"
                scheduler?.schedule(requestorId, TASK.valueOf(taskType), delay, initTime)
            })
    }

    private fun clearStatus() {
        entryViewModel?.clearStatusOverlay()
    }

    private val isStatusPresent: Boolean
        get() = entryViewModel?.statusOverlay?.value != null

    fun loadStatus(intent: Intent) {
        val parsed = StatusMessageBuilder.build(intent, this)
        val action = intent.action
        when {
            StatusIntentPolicy.isConclusive(action) -> Unit
            StatusIntentPolicy.isTransCompletedImmediateAbort(action, parsed.title) -> {
                clearStatus()
                scheduleFinishAfter(StatusDurations.SHORT_MS)
            }
            else -> presentStatusOverlayForLoadedIntent(intent, action, parsed)
        }
    }

    private fun presentStatusOverlayForLoadedIntent(
        intent: Intent,
        action: String?,
        parsed: ParsedStatus
    ) {
        val title = parsed.title.orEmpty()
        val tone = resolveStatusTitleTone(action, intent)
        entryViewModel?.showStatusOverlay(
            StatusOverlayUi(
                revision = 0L,
                action = action.orEmpty(),
                title = title,
                titleTone = tone,
                transactionStatus = parsed.transactionStatus,
                screenStatusMessage = parsed.screenStatusMessage,
                screenStatusTitle = parsed.screenStatusTitle,
                transCompletedHostTimeoutMs = intent.getLongExtra(
                    StatusData.PARAM_HOST_RESP_TIMEOUT,
                    StatusDurations.DEFAULT_MS
                ),
                transCompletedResultCode = intent.getLongExtra(StatusData.PARAM_CODE, 0L)
            )
        )
        if (action == InformationStatus.TRANS_COMPLETED) {
            val delayMs = intent.getLongExtra(
                StatusData.PARAM_HOST_RESP_TIMEOUT,
                StatusDurations.DEFAULT_MS
            )
            scheduleFinishAfter(delayMs)
        }
    }

    private fun resolveStatusTitleTone(action: String?, intent: Intent): StatusTitleTone {
        if (action != InformationStatus.TRANS_COMPLETED) return StatusTitleTone.Default
        return if (intent.getLongExtra(StatusData.PARAM_CODE, 0L) == 0L) {
            StatusTitleTone.Success
        } else {
            StatusTitleTone.Error
        }
    }

    private fun scheduleFinishAfter(delayMs: Long) {
        val sched = scheduler ?: return
        sched.cancelTasks()
        statusTaskGroup?.let { taskGroup.cancelGroup(it) }
        statusTaskGroup = taskGroup.createGroup()
        val group = statusTaskGroup
        sched.schedule(TASK.FINISH, delayMs, System.currentTimeMillis())?.let { job ->
            if (group != null) taskGroup.addToGroup(group, job)
        }
    }

    /**
     * Add/Remove Watermark based on Trans Mode
     */
    private fun updateTransMode(transMode: String) {
        if (TextUtils.isEmpty(transMode)) {
            this.transMode = ""
            viewUtils.removeWaterMarkView(this)
            return
        }
        if (transMode != this.transMode) {
            this.transMode = transMode
            val mode = when (transMode) {
                TransMode.DEMO -> getString(R.string.demo_only)
                TransMode.TEST -> getString(R.string.test_only)
                TransMode.TEST_AND_DEMO -> getString(R.string.test_and_demo)
                else -> null
            }
            if (!TextUtils.isEmpty(mode)) viewUtils.addWaterMarkView(this, mode)
            else viewUtils.removeWaterMarkView(this)
        }
    }

    /**
     * Update Transaction Type on Title Bar
     */
    private fun updateTransType(transType: String?) {
        if (transType.isNullOrBlank()) {
            this.transType = ""
            actionBar?.setTitle("")
            return
        }
        if (transType != this.transType) {
            this.transType = transType
            actionBar?.setTitle(transType)
        }
    }

    /**
     * Broadcast Receiver to receive status updates from BroadPOS Manager
     * void registerUIReceiver()
     * void unregisterUIReceiver()
     */
    private fun registerUIReceiver() {
        if (uiReceiversRegistered) return
        val statusRec = StatusBroadcastReceiver()
        statusBroadcastReceiver = statusRec
        // 须 EXPORTED：与 golive `registerReceiver(filter)` 一致，否则系统/ adb 发出的隐式
        // POSLINK_CLEAR_MESSAGE 等状态广播无法送达（RECEIVER_NOT_EXPORTED 会拦截非本 UID）。
        ContextCompat.registerReceiver(
            this,
            statusRec,
            statusRec.filter,
            ContextCompat.RECEIVER_EXPORTED,
        )

        val responseRec = ResponseBroadcastReceiver()
        responseBroadcastReceiver = responseRec
        ContextCompat.registerReceiver(
            this,
            responseRec,
            responseRec.filter,
            ContextCompat.RECEIVER_EXPORTED,
        )
        uiReceiversRegistered = true
    }

    private fun unregisterUIReceiver() {
        if (!uiReceiversRegistered) return
        if (statusBroadcastReceiver != null) {
            this.unregisterReceiver(statusBroadcastReceiver)
            statusBroadcastReceiver = null
        }
        if (responseBroadcastReceiver != null) {
            this.unregisterReceiver(responseBroadcastReceiver)
            responseBroadcastReceiver = null
        }
        uiReceiversRegistered = false
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (this.isStatusPresent) return true

        if (event.action == KeyEvent.ACTION_UP &&
            (event.keyCode == KeyEvent.KEYCODE_ENTER || event.keyCode == KeyEvent.KEYCODE_BACK)
        ) {
            val response = Bundle()
            response.putInt("keyCode", event.keyCode)
            supportFragmentManager.setFragmentResult("keyCode", response)
            entryViewModel?.submitKeyEvent(event.keyCode)
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    /**
     * Forwards Manager's broadcasts to fragments
     */
    inner class ResponseBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            Logger.intent(intent, "RESPONSE BROADCAST:\t" + intent.action)

            //Validation
            interfaceHistory.validate(
                intent.getStringExtra("interfaceID"),
                intent.getStringExtra("originatingAction")
            )

            //if(!isValid) return; // Commented out until Manager supports interfaceID

            //Forward Response Broadcast to BaseEntryFragment
            val responseBroadcastExtras = BundleMaker()
                .addString("action", intent.action)
                .addBundle(intent.extras)
                .get()
            supportFragmentManager.setFragmentResult("response", responseBroadcastExtras)
            entryViewModel?.onManagerResponse(intent.action, intent.extras)
        }

        val filter: IntentFilter
            get() {
                val filter = IntentFilter()
                filter.addAction(EntryResponse.ACTION_ACCEPTED)
                filter.addAction(EntryResponse.ACTION_DECLINED)
                return filter
            }
    }

    inner class StatusBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            Logger.intent(intent, "STATUS BROADCAST:\t" + intent.action)
            if (intent.action == ACTION_POSLINK_CLEAR_MESSAGE) {
                entryViewModel?.clearPoslinkContent()
                // 与主屏一致：清屏时重置副屏聚合状态，避免 Presentation 仍显示旧文案
                viewModel?.updateAllData("", "", "", null, "", "")
                clearStatus()
                return
            }
            loadStatus(intent)
        }

        val filter: IntentFilter
            get() {
                val filter = IntentFilter()
                //-----------------Uncategory Status-----------------
                filter.addAction(Uncategory.PRINT_STARTED)
                filter.addAction(Uncategory.PRINT_COMPLETED)
                filter.addAction(Uncategory.FILE_UPDATE_STARTED)
                filter.addAction(Uncategory.FILE_UPDATE_COMPLETED)
                filter.addAction(Uncategory.FCP_FILE_UPDATE_STARTED)
                filter.addAction(Uncategory.FCP_FILE_UPDATE_COMPLETED)
                filter.addAction(Uncategory.CAPK_UPDATE_STARTED)
                filter.addAction(Uncategory.CAPK_UPDATE_COMPLETED)
                filter.addAction(Uncategory.LOG_UPLOAD_STARTED)
                filter.addAction(Uncategory.LOG_UPLOAD_CONNECTED)
                filter.addAction(Uncategory.LOG_UPLOAD_UPLOADING)
                filter.addAction(Uncategory.LOG_UPLOAD_COMPLETED)

                //----------------Information Status-----------------
                filter.addCategory(InformationStatus.CATEGORY)
                filter.addAction(InformationStatus.DCC_ONLINE_STARTED)
                filter.addAction(InformationStatus.DCC_ONLINE_FINISHED)
                filter.addAction(InformationStatus.EMV_TRANS_ONLINE_STARTED)
                filter.addAction(InformationStatus.EMV_TRANS_ONLINE_FINISHED)
                filter.addAction(InformationStatus.TRANS_ONLINE_STARTED)
                filter.addAction(InformationStatus.TRANS_ONLINE_FINISHED)
                filter.addAction(InformationStatus.RKI_STARTED)
                filter.addAction(InformationStatus.RKI_FINISHED)
                filter.addAction(InformationStatus.PINPAD_CONNECTION_STARTED)
                filter.addAction(InformationStatus.PINPAD_CONNECTION_FINISHED)
                filter.addAction(InformationStatus.TRANS_COMPLETED)
                filter.addAction(InformationStatus.ERROR)
                filter.addAction(InformationStatus.ENTER_PIN_STARTED)

                //----------------Card Status-----------------
                filter.addCategory(CardStatus.CATEGORY)
                filter.addAction(CardStatus.CARD_REMOVED)
                filter.addAction(CardStatus.CARD_REMOVAL_REQUIRED)
                filter.addAction(CardStatus.CARD_QUICK_REMOVAL_REQUIRED)
                filter.addAction(CardStatus.CARD_PROCESS_STARTED)
                filter.addAction(CardStatus.CARD_PROCESS_COMPLETED)

                //----------------Batch Status-----------------
                filter.addCategory(BatchStatus.CATEGORY)
                filter.addAction(BatchStatus.BATCH_UPLOADING)
                filter.addAction(BatchStatus.BATCH_SF_COMPLETED)
                filter.addAction(BatchStatus.BATCH_CLOSE_UPLOADING)
                filter.addAction(BatchStatus.BATCH_CLOSE_COMPLETED)

                //----------------Poslink Status-----------------
                filter.addCategory(POSLINK_STATUS_CATEGORY)
                filter.addAction(ACTION_POSLINK_CLEAR_MESSAGE)

                return filter
            }
    }

    companion object {
        const val EXTRA_NAV_COMPOSE_DEMO: String = "extra_nav_compose_demo"
        private const val POSLINK_STATUS_CATEGORY = "com.pax.us.pay.status.category.POSLINK"
        private const val ACTION_POSLINK_CLEAR_MESSAGE = "com.pax.us.pay.POSLINK_CLEAR_MESSAGE"
    }
}