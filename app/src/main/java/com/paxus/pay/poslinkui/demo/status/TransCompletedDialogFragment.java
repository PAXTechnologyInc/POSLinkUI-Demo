package com.paxus.pay.poslinkui.demo.status;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.status.StatusData;
import com.paxus.pay.poslinkui.demo.R;

public class TransCompletedDialogFragment extends DialogFragment {

    public static DialogFragment newInstance(long code, String result, long timeout){
        TransCompletedDialogFragment dialogFragment = new TransCompletedDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(StatusData.PARAM_CODE,code);
        bundle.putString(StatusData.PARAM_MSG, result);
        bundle.putLong(StatusData.PARAM_HOST_RESP_TIMEOUT,timeout);

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_trans_approve, container, false);
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setCancelable(false);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_trans_approve, null);

        TextView textView = view.findViewById(R.id.message);

        Bundle bundle = getArguments();
        if(bundle != null) {
            long code = bundle.getLong(StatusData.PARAM_CODE);
            String msg = bundle.getString(StatusData.PARAM_MSG);
            long time = bundle.getLong(StatusData.PARAM_HOST_RESP_TIMEOUT);

            String message = "Transaction Approval";
            if (code != 0) {
                message = code + " " + msg;
                textView.setTextColor(Color.RED);
            }else {
                textView.setTextColor(Color.GREEN);
            }
            textView.setText(message);

            new Handler().postDelayed(this::dismiss, time);
        }

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        requireActivity().finish();
    }
}
