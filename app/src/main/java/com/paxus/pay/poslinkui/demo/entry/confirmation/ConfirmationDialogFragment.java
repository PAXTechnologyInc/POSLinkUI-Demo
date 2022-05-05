package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.OptionEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.ConfirmationType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

public class ConfirmationDialogFragment extends DialogFragment {
    private String action;
    private String packageName;
    private long timeout;

    public static DialogFragment newInstance(Intent intent){
        ConfirmationDialogFragment dialogFragment = new ConfirmationDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        Bundle bundle = getArguments();

        if(bundle!= null) {
            String message = bundle.getString(EntryExtraData.PARAM_MESSAGE);
            String[] options = bundle.getStringArray(EntryExtraData.PARAM_OPTIONS);

            action = bundle.getString(EntryRequest.PARAM_ACTION);
            packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);

            if(ConfirmationEntry.ACTION_CONFIRM_CARD_PROCESS_RESULT.equals(action)) {
                timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 5000);
            }else {
                timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
            }

            String negative = null;
            String positive = null;
            if(options.length == 2) {
                for (String option : options) {
                    if (ConfirmationType.YES.equals(option)) {
                        positive = option;
                    } else if (ConfirmationType.NO.equals(option)) {
                        negative = option;
                    }
                }
                if(TextUtils.isEmpty(negative) && TextUtils.isEmpty(positive)){
                    positive = options[0];
                    negative = options[1];
                }
            }else if(options.length == 1){
                positive = options[0];
            }


            builder.setTitle(message);

            if(!TextUtils.isEmpty(positive)) {
                builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_CONFIRMED,false);
                    }
                });
            }
            if(!TextUtils.isEmpty(negative)) {
                builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EntryRequestUtils.sendNext(requireContext(), packageName, action, EntryRequest.PARAM_CONFIRMED,true);
                    }
                });
            }
        }

        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                new Handler().postDelayed(()->{
                    if(dialog.isShowing()){
                        EntryRequestUtils.sendTimeout(requireContext(), packageName, action);
                        dialogInterface.cancel();
                    }
                },timeout);
            }
        });

        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
