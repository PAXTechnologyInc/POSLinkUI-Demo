package com.paxus.pay.poslinkui.demo.status;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.status.CardStatus;
import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.paxus.pay.poslinkui.demo.R;

public class InformationDialogFragment extends DialogFragment {

    public static DialogFragment newInstance(String action){
        InformationDialogFragment dialogFragment = new InformationDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION,action);

        dialogFragment.setArguments(bundle);
        return dialogFragment;
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
        if(bundle != null) {
            String action  = bundle.getString(EntryRequest.PARAM_ACTION);
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
                case CardStatus.CARD_REMOVAL_REQUIRED:
                    message = getString(R.string.please_remove_card);
                    break;
                case CardStatus.CARD_PROCESS_STARTED:
                    message = getString(R.string.emv_process_start);
                default:
                    break;
            }
            textView.setText(message);
        }

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
