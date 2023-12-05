package com.paxus.pay.poslinkui.demo.status;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.SFType;
import com.pax.us.pay.ui.constant.status.BatchStatus;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
import com.pax.us.pay.ui.constant.status.Uncategory;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.util.HashSet;
import java.util.Set;

public class StatusFragment extends Fragment {

    protected Intent intent;
    protected  String message;

    public static final long DURATION_DEFAULT = 5000, DURATION_SHORT = 1000;

    // Default constructor
    public StatusFragment() {
        // Default initialization code here
    }

    public StatusFragment(Intent intent, Context context) {
        this.intent = intent;
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        if (intent.getExtras() != null) bundle.putAll(intent.getExtras());
        setArguments(bundle);
        message = generateStatusMessage(bundle, context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

        TextView titleTextView = view.findViewById(R.id.status_title);
        titleTextView.setText(message);
        return view;
    }

    public boolean isConclusive() {
        return conclusiveStatusSet.contains(intent.getAction());
    }

    private Set<String> conclusiveStatusSet = new HashSet<String>() {{
        add(CardStatus.CARD_REMOVED);
        add(CardStatus.CARD_PROCESS_COMPLETED);

        add(BatchStatus.BATCH_CLOSE_COMPLETED);
        add(BatchStatus.BATCH_SF_COMPLETED);

        add(InformationStatus.TRANS_ONLINE_FINISHED);
        add(InformationStatus.TRANS_REVERSAL_FINISHED);
        add(InformationStatus.PINPAD_CONNECTION_FINISHED);
        add(InformationStatus.EMV_TRANS_ONLINE_FINISHED);
        add(InformationStatus.DCC_ONLINE_FINISHED);
        add(InformationStatus.RKI_FINISHED);
        add(InformationStatus.ENTER_PIN_FINISHED);

        add(Uncategory.ACTIVATE_COMPLETED);
        add(Uncategory.CAPK_UPDATE_COMPLETED);
        add(Uncategory.PRINT_COMPLETED);
        add(Uncategory.FILE_UPDATE_COMPLETED);
        add(Uncategory.LOG_UPLOAD_COMPLETED);
        add(Uncategory.FCP_FILE_UPDATE_COMPLETED);
    }};

    private String generateStatusMessage(Bundle bundle, Context context) {
        String action = bundle.getString(EntryRequest.PARAM_ACTION);
        String message = "";
        switch (action) {
            case InformationStatus.TRANS_ONLINE_STARTED:
                message = context.getResources().getString(R.string.info_trans_online);
                break;
            case InformationStatus.EMV_TRANS_ONLINE_STARTED:
                message = context.getResources().getString(R.string.info_emv_trans_online);
                break;
            case InformationStatus.DCC_ONLINE_STARTED:
                message = context.getResources().getString(R.string.info_dcc_online_start);
                break;
            case InformationStatus.PINPAD_CONNECTION_STARTED:
                message = context.getResources().getString(R.string.info_pin_pad_connection_start);
                break;
            case InformationStatus.RKI_STARTED:
                message = context.getResources().getString(R.string.info_rki_start);
                break;
            case InformationStatus.ENTER_PIN_STARTED://Device with pinpad on the back
                message = context.getResources().getString(R.string.please_flip_over);
                break;
            case CardStatus.CARD_REMOVAL_REQUIRED:
                message = context.getResources().getString(R.string.please_remove_card);
                break;
            case CardStatus.CARD_QUICK_REMOVAL_REQUIRED:
                message = context.getResources().getString(R.string.please_remove_card_quickly);
                break;
            case CardStatus.CARD_SWIPE_REQUIRED:
                message = context.getResources().getString(R.string.please_swipe_card);
                break;
            case CardStatus.CARD_INSERT_REQUIRED:
                message = context.getResources().getString(R.string.please_insert_chip_card);
                break;
            case CardStatus.CARD_TAP_REQUIRED:
                message = context.getResources().getString(R.string.please_tap_card);
                break;
            case CardStatus.CARD_PROCESS_STARTED:
                message = context.getResources().getString(R.string.emv_process_start);
                break;
            case Uncategory.PRINT_STARTED:
                message = context.getResources().getString(R.string.print_process);
                break;
            case Uncategory.FILE_UPDATE_STARTED:
                message = context.getResources().getString(R.string.update_process);
                break;
            case Uncategory.FCP_FILE_UPDATE_STARTED:
                message = context.getResources().getString(R.string.check_for_update_start);
                break;
            case Uncategory.CAPK_UPDATE_STARTED:
                message = context.getResources().getString(R.string.download_emv_capk);
                break;
            case Uncategory.LOG_UPLOAD_STARTED:
                message = context.getResources().getString(R.string.log_uploading_start);
                break;
            case Uncategory.LOG_UPLOAD_CONNECTED: {
                long uploadPercent = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_PERCENT, 0L);
                message = context.getResources().getString(R.string.log_connected) + " (" + uploadPercent + "%)";
                break;
            }
            case Uncategory.LOG_UPLOAD_UPLOADING: {
                long logUploadCount = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_COUNT, 0L);
                long logTotalCount = bundle.getLong(StatusData.PARAM_UPLOAD_TOTAL_COUNT, 0L);
                long logUploadPercent = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_PERCENT, 0L);
                message = context.getResources().getString(R.string.update_process) + " " + logUploadCount + "/" + logTotalCount + "("
                        + logUploadPercent + "%)";
                break;
            }
            case BatchStatus.BATCH_CLOSE_UPLOADING: {
                String edcType = bundle.getString(StatusData.PARAM_EDC_TYPE);
                long currentCount = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_COUNT, 0);
                long totalCount = bundle.getLong(StatusData.PARAM_UPLOAD_TOTAL_COUNT, 0);
                message = context.getResources().getString(R.string.uploading_trans) + " " + edcType + "\n" + currentCount + "/" + totalCount;
                break;
            }
            case BatchStatus.BATCH_UPLOADING: {
                String sfType = bundle.getString(StatusData.PARAM_SF_TYPE);
                long sfCurrentCount = bundle.getLong(StatusData.PARAM_SF_CURRENT_COUNT, 0);
                long sfTotalCount = bundle.getLong(StatusData.PARAM_SF_TOTAL_COUNT, 0);
                message = (SFType.FAILED.equals(sfType) ?
                        context.getResources().getString(R.string.uploading_failed_trans)
                        : context.getResources().getString(R.string.uploading_sf_trans)) +
                        "\n" + sfCurrentCount + " out of " + sfTotalCount;
                break;
            }
            case InformationStatus.TRANS_COMPLETED: {
                message = bundle.getString(StatusData.PARAM_MSG);
                break;
            }
            default:
                Logger.i("Status action: " + action);
                break;
        }

        return message;
    }

    public boolean isImmediateTerminationNeeded() {
        return false;
    }

    public boolean sameAs(@Nullable StatusFragment another){
        return another != null && this.intent.getAction().equals(another.intent.getAction());
    }

    public void updateStatus(Intent intent, Context context) {
        this.intent = intent;
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        if(intent.getExtras() != null) bundle.putAll(intent.getExtras());
        setArguments(bundle);
        this.message = generateStatusMessage(bundle, context);
        ((TextView)getView().findViewById(R.id.status_title)).setText(message);
    }
}