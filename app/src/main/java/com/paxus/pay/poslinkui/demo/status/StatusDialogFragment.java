package com.paxus.pay.poslinkui.demo.status;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.SFType;
import com.pax.us.pay.ui.constant.status.BatchStatus;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
import com.pax.us.pay.ui.constant.status.Uncategory;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.Logger;

/**
 * Implement dialog actions:<br>
 * {@value InformationStatus#TRANS_ONLINE_STARTED},<br>
 * {@value InformationStatus#EMV_TRANS_ONLINE_STARTED},<br>
 * {@value InformationStatus#RKI_STARTED},<br>
 * {@value InformationStatus#DCC_ONLINE_STARTED},<br>
 * {@value InformationStatus#PINPAD_CONNECTION_STARTED},<br>
 * {@value CardStatus#CARD_REMOVAL_REQUIRED},<br>
 * {@value CardStatus#CARD_PROCESS_STARTED},<br>
 * {@value Uncategory#PRINT_STARTED},<br>
 * {@value Uncategory#FILE_UPDATE_STARTED},<br>
 * {@value Uncategory#FCP_FILE_UPDATE_STARTED},<br>
 * {@value Uncategory#LOG_UPLOAD_STARTED},<br>
 * {@value Uncategory#LOG_UPLOAD_CONNECTED},<br>
 * {@value Uncategory#LOG_UPLOAD_UPLOADING},<br>
 * {@value Uncategory#CAPK_UPDATE_STARTED},<br>
 * {@value BatchStatus#BATCH_SF_UPLOADING},<br>
 * {@value BatchStatus#BATCH_CLOSE_UPLOADING},<br>
 *
 * <br>
 * UI Requirement:
 * Display status by dialog. Do not close dialog by press KEY_BACK <br>
 */
public class StatusDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.POSLinkUIPastelDialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setCancelable(false);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_information, null);

        TextView textView = view.findViewById(R.id.message);

        Bundle bundle = getArguments();
        if (bundle != null) {
            //Display status by dialog
            textView.setText(formatStatusMessage(bundle));
        }

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    //Do not close dialog by press KEY_BACK
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Hide Keyboard
        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private String formatStatusMessage(Bundle bundle) {
        String action = bundle.getString(EntryRequest.PARAM_ACTION);
        String message = "";
        switch (action) {
            case InformationStatus.TRANS_ONLINE_STARTED:
                message = getString(R.string.info_trans_online);
                break;
            case InformationStatus.EMV_TRANS_ONLINE_STARTED:
                message = getString(R.string.info_emv_trans_online);
                break;
            case InformationStatus.DCC_ONLINE_STARTED:
                message = getString(R.string.info_dcc_online_start);
                break;
            case InformationStatus.PINPAD_CONNECTION_STARTED:
                message = getString(R.string.info_pin_pad_connection_start);
                break;
            case InformationStatus.RKI_STARTED:
                message = getString(R.string.info_rki_start);
                break;
            case InformationStatus.ENTER_PIN_STARTED://Device with pinpad on the back
                message = getString(R.string.please_flip_over);
                break;
            case CardStatus.CARD_REMOVAL_REQUIRED:
                message = getString(R.string.please_remove_card);
                break;
            case CardStatus.CARD_QUICK_REMOVAL_REQUIRED:
                message = getString(R.string.please_remove_card_quickly);
                break;
            case CardStatus.CARD_SWIPE_REQUIRED:
                message = getString(R.string.please_swipe_card);
                break;
            case CardStatus.CARD_INSERT_REQUIRED:
                message = getString(R.string.please_insert_chip_card);
                break;
            case CardStatus.CARD_TAP_REQUIRED:
                message = getString(R.string.please_tap_card);
                break;
            case CardStatus.CARD_PROCESS_STARTED:
                message = getString(R.string.emv_process_start);
                break;
            case Uncategory.PRINT_STARTED:
                message = getString(R.string.print_process);
                break;
            case Uncategory.FILE_UPDATE_STARTED:
                message = getString(R.string.update_process);
                break;
            case Uncategory.FCP_FILE_UPDATE_STARTED:
                message = getString(R.string.check_for_update_start);
                break;
            case Uncategory.CAPK_UPDATE_STARTED:
                message = getString(R.string.download_emv_capk);
                break;
            case Uncategory.LOG_UPLOAD_STARTED:
                message = getString(R.string.log_uploading_start);
                break;
            case Uncategory.LOG_UPLOAD_CONNECTED: {
                long uploadPercent = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_PERCENT, 0L);
                message = getString(R.string.log_connected) + " (" + uploadPercent + "%)";
                break;
            }
            case Uncategory.LOG_UPLOAD_UPLOADING: {
                long logUploadCount = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_COUNT, 0L);
                long logTotalCount = bundle.getLong(StatusData.PARAM_UPLOAD_TOTAL_COUNT, 0L);
                long logUploadPercent = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_PERCENT, 0L);
                message = getString(R.string.update_process) + " " + logUploadCount + "/" + logTotalCount + "("
                        + logUploadPercent + "%)";
                break;
            }
            case BatchStatus.BATCH_CLOSE_UPLOADING: {
                String edcType = bundle.getString(StatusData.PARAM_EDC_TYPE);
                long currentCount = bundle.getLong(StatusData.PARAM_UPLOAD_CURRENT_COUNT, 0);
                long totalCount = bundle.getLong(StatusData.PARAM_UPLOAD_TOTAL_COUNT, 0);
                message = getString(R.string.uploading_trans) + " " + edcType + "\n" + currentCount + "/" + totalCount;
                break;
            }
            case BatchStatus.BATCH_SF_UPLOADING: {
                String sfType = bundle.getString(StatusData.PARAM_SF_TYPE);
                long sfCurrentCount = bundle.getLong(StatusData.PARAM_SF_CURRENT_COUNT, 0);
                long sfTotalCount = bundle.getLong(StatusData.PARAM_SF_TOTAL_COUNT, 0);
                message = SFType.FAILED.equals(sfType) ? getString(R.string.uploading_failed_trans) : getString(R.string.uploading_sf_trans) +
                        getString(R.string.total_count) + sfTotalCount + getString(R.string.current_count) + sfCurrentCount;
                break;
            }
            default:
                Logger.e("Unknown status action:" + action);
                break;
        }
        return message;
    }

}
