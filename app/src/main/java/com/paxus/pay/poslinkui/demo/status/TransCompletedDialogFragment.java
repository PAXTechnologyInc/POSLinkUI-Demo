package com.paxus.pay.poslinkui.demo.status;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.status.InformationStatus;
import com.pax.us.pay.ui.constant.status.StatusData;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.Logger;

/**
 *  Implement information status action {@value InformationStatus#TRANS_COMPLETED}
 */
public class TransCompletedDialogFragment extends DialogFragment {

    public static DialogFragment newInstance(Intent intent){
        TransCompletedDialogFragment dialogFragment = new TransCompletedDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(intent.getExtras());

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

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
            long time = bundle.getLong(StatusData.PARAM_HOST_RESP_TIMEOUT,2000);
            Logger.i("TRANS_COMPLETED:"+code+","+msg);
            if (code != 0) {
                textView.setTextColor(getResources().getColor(R.color.fail));
            }else {
                textView.setTextColor(getResources().getColor(R.color.success));
            }
            textView.setText(msg);

            new Handler().postDelayed(this::dismiss, time);
        }

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        //Close activity after Trans completed
        requireActivity().finishAndRemoveTask();
    }
}
